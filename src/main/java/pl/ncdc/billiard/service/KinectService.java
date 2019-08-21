package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.Kinect;
import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.CalibrationParams;

/**
 * Receive frames from Kinect device and generate data for BilliardTableService
 * 
 * @see BilliardTableService
 * @see KinectService#send(byte[], int, int) KinectService.Send()
 * @see pl.ncdc.billiard.Kinect Kinect
 * @see Kinect Kinect simulator
 **/

@Service
public class KinectService {

	@Autowired
	protected BilliardTable table;
	@Autowired
	protected SimpMessagingTemplate simpMessagingTemplate;

	private Kinect kinect;

	@Value("${kinectService.mask}")
	private String filename;
	private Mat mask;
	private BackgroundSubtractor backgroundSubstractor;
	private Mat perspectiveTransform;

	// kalibracja
	private int minBallRadius;
	private int maxBallRadius;

	private int status;
	private List<Ball> history;

	@Autowired
	public KinectService(@Lazy Kinect kinect) {
		this.kinect = kinect;
		backgroundSubstractor = Video.createBackgroundSubtractorMOG2();
	}

	@PostConstruct
	private void init() {
		this.kinect.start(Kinect.COLOR);
		this.status = 0;
	}

	/**
	 * Read mask image from a file, apply perspective transform and convert to a
	 * gray scale. Set mask to a backgroundSubstractor
	 */
	public void prepareMask() {

		// read mask
		this.mask = Imgcodecs.imread(this.filename);
		// wrap mask
		Imgproc.warpPerspective(this.mask, this.mask, this.perspectiveTransform,
				new Size(this.table.getWidth(), this.table.getHeight()), Imgproc.INTER_CUBIC);
		// convert mask to gray
		Imgproc.cvtColor(this.mask, this.mask, Imgproc.COLOR_BGR2GRAY);
		// apply mask
		backgroundSubstractor.apply(this.mask, this.mask, 1);

	}

	/**
	 * Calculates a perspective transform from four pairs of the corresponding
	 * points. Define min and max ball radius. Computes a foreground mask.
	 * 
	 * @param calibrationParams
	 */
	public void updateCalibration(CalibrationParams calibrationParams) {

		this.kinect.stop();

		// read points from calibration parameters
		Point leftTop = calibrationParams.getLeftUpperCorner();
		Point leftBottom = calibrationParams.getLeftBottomCorner();
		Point rightBottom = calibrationParams.getRightBottomCorner();
		Point rightTop = calibrationParams.getRightUpperCorner();

		// check detected ball's diameter
		// this.minBallRadius = calibrationParams.getBallDiameter() - 2;
		// this.maxBallRadius = calibrationParams.getBallDiameter() + 2;

		this.minBallRadius = 11;
		this.maxBallRadius = 15;

		// add points to a list and generate Mat object
		List<Point> pts = new ArrayList<Point>();

		pts.add(leftTop);
		pts.add(leftBottom);
		pts.add(rightBottom);
		pts.add(rightTop);

		Mat src = Converters.vector_Point2f_to_Mat(pts);

		pts = new ArrayList<Point>();
		pts.add(new Point(0, 0));
		pts.add(new Point(0, this.table.getHeight()));
		pts.add(new Point(this.table.getWidth(), this.table.getHeight()));
		pts.add(new Point(this.table.getWidth(), 0));

		Mat dst = Converters.vector_Point2f_to_Mat(pts);

		this.perspectiveTransform = Imgproc.getPerspectiveTransform(src, dst);

		// apply new calibration to mask image
		this.prepareMask();

		this.kinect.start(Kinect.COLOR);
	}

	/**
	 * Processes incoming data from <i>Kinect</i>.
	 * 
	 * @param data   <b>BYTE</b> array
	 * @param height number of rows - image <b>HEIGHT</b>
	 * @param width  number of columns - image <b>WIDTH</b>
	 */
	public void send(byte[] data, int height, int width) {
		Mat frame = new Mat(height, width, CvType.CV_8UC4);
		frame.put(0, 0, data);
		// counter++;
		// if (counter > 60)
		// counter = 0;
		// show(frame);
		// updateTable(frame);
		// send table by web socket
		List<Ball> newList = updateTable(frame);

		// this.table.setBalls(updateHipstory(newList));
		this.table.setBalls(newList);
		this.simpMessagingTemplate.convertAndSend("/table/live", this.table);

		// updateHipstory();

		// if calibrate
		if (this.status == 1) {
			this.simpMessagingTemplate.convertAndSend("/calibrate/live", frame);
		} else if (this.status == 2) {
			generateMask(frame);
		}
	}

	private List<Ball> updateHipstory(List<Ball> list) {
		Point point;
		List<Ball> newList = new ArrayList<Ball>();
		if (this.table.getBalls() == null || this.table.getBalls().size() == 0)
			return list;
		for (Ball ball : this.table.getBalls()) {
			point = this.table.findBallByPoint(ball.getPoint());
			if (point == null)
				newList.add(ball);
			else {
				ball.getPoint().x = (ball.getPoint().x + point.x) / 2;
				ball.getPoint().y = (ball.getPoint().y + point.y) / 2;
				newList.add(ball);
			}
		}
		return newList;
	}

	/**
	 * Prepare image to use <i>HoughCircles</i> algorithm: converts an image to gray
	 * scale, cut unnecessary part of an image and apply mask to remove background.
	 * After detection, circles are saved to a list and sort by x - position
	 * 
	 * @author charlie
	 * @param data   <b>BYTE</b> array
	 * @param height number of rows - image <b>HEIGHT</b>
	 * @param width  number of columns - image <b>WIDTH</b>
	 * @see Imgproc#HoughCircles(Mat, Mat, int, double, double, double, double, int,
	 *      int)
	 */
	private List<Ball> updateTable(Mat frame) {
		Mat circles = new Mat();
		Mat gray = new Mat();

		// wrap image
		Imgproc.warpPerspective(frame, frame, this.perspectiveTransform,
				new Size(this.table.getWidth(), this.table.getHeight()), Imgproc.INTER_CUBIC);
		Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
		// create and apply mask
		// this.mask = new Mat();
		this.backgroundSubstractor.apply(gray, this.mask, 0);
		// apply blur
		Imgproc.medianBlur(this.mask, this.mask, 9);
		// show(mask);
		Imgproc.HoughCircles(this.mask, circles, Imgproc.HOUGH_GRADIENT, 1.0, this.minBallRadius * 2, 15.0, 10.0,
				this.minBallRadius, this.maxBallRadius);
		// save detected balls to a list
		List<Ball> list = new ArrayList<Ball>();
		// Detect white ball
		Ball whiteBall = whiteBallDetection(frame, circles, this.maxBallRadius);
		if (whiteBall != null)
			whiteBall.getPoint().x = this.table.getWidth() - whiteBall.getPoint().x;
		this.table.setWhiteBall(whiteBall);

		for (int x = 0; x < circles.cols(); x++) {
			double[] c = circles.get(0, x);
			Point point = new Point(table.getWidth() - c[0], c[1]);
			if (whiteBall == null || Math.abs(whiteBall.getPoint().x - point.x) > this.minBallRadius
					|| Math.abs(whiteBall.getPoint().y - point.y) > this.minBallRadius) {
				list.add(new Ball(0, point));
			}
		}
		// sort balls by X
		list.sort((o1, o2) -> Double.compare(o1.getPoint().x, o1.getPoint().x));
		for (int i = 1; i < list.size(); i++)
			list.get(i).setId(i);
		return list;
	}

	/**
	 * Receive frame from Kinect device and compute a foreground mask. Increase a
	 * BallDetecting algorithm effectiveness
	 * 
	 * @param frame
	 */
	private void generateMask(Mat frame) {
		Imgcodecs.imwrite(this.filename, frame);
		this.prepareMask();
	}

	/**
	 * Use <i>HighGui.imshow()</i> to display an image, and wait 1 Ms
	 * <p>
	 * Require: {@code jvmArgs=['-Djava.awt.headless=false']} in <b>build.gradle</b>
	 * </p>
	 * 
	 * @param image Image to display
	 */
	private void show(Mat image) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (image.type() == CvType.CV_8UC4)
					Imgproc.cvtColor(image, image, Imgproc.COLOR_BGRA2BGR);
				HighGui.imshow("Image", image);
				HighGui.waitKey(5);
			}
		}, 0);
	}

	/**
	 * Iterate through the list and calculates the approximate number of white
	 * pixels. The Circle with the most white pixels is probably the White Ball
	 * 
	 * @author pablito
	 * 
	 * @param image   Colorful image of BilliardTable
	 * @param circles Vector of found circles. Each vector is encoded as 3 or 4
	 *                element floating-point vector (x, y, radius) or (x, y, radius,
	 *                votes) .
	 * @param r       Radius of detected circles
	 * @return Function return Ball pointed to the White Ball
	 */
	public Ball whiteBallDetection(Mat image, Mat circles, int r) {
		double maxSum = Integer.MIN_VALUE;
		maxSum = 350000.0;
		for (int i = 0; i < circles.cols(); i++) {
			double[] c = circles.get(0, i);
			int x = (int) c[0] - r;
			int y = (int) c[1] - r;
			double rectSum = 0;
			for (int j = y; j <= y + 2 * r; j++) {
				for (int k = x; k <= x + 2 * r; k++) {
					if (Math.pow(k - c[0], 2) + Math.pow(j - c[1], 2) <= Math.pow(c[2], 2)) {
						// min max table size
						if (j > 0 && k > 0 && j < table.getHeight() && k < table.getWidth()) {
							rectSum += image.get(j, k)[0];
							rectSum += image.get(j, k)[1];
							rectSum += image.get(j, k)[2];
						}
					}
				}
			}

			if (rectSum > maxSum)
				return new Ball(0, new Point(c[0], c[1]));
		}
		return null;

	}

}
