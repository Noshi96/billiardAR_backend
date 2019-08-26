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
	private BilliardTable table;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	private Kinect kinect;

	@Value("${kinectService.mask}")
	private String filename;
	private Mat mask;
	private BackgroundSubtractor backgroundSubstractor;
	private Mat perspectiveTransform;

	// kalibracja
	private int minBallRadius;
	private int maxBallRadius;
	private int minWhiteBallDensity;

	Mat actualFrame;;

	private int status;

	@Autowired
	public KinectService(@Lazy Kinect kinect) {
		this.kinect = kinect;
		backgroundSubstractor = Video.createBackgroundSubtractorMOG2();
		actualFrame = new Mat();
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
		if (this.mask == null) {
			System.out.println("File '" + this.filename + "' cannot be found.");
			System.out.println("Create new mask");
			this.mask = new Mat(this.table.getWidth(), this.table.getHeight(), CvType.CV_8UC4);
		}
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
		this.minBallRadius = calibrationParams.getBallDiameter() / 2 - 2;
		this.maxBallRadius = calibrationParams.getBallDiameter() / 2 + 2;

		this.minWhiteBallDensity = calibrationParams.getWhiteBallDensity();

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
//	public void send(byte[] data, int height, int width) {
//		Mat frame = new Mat(height, width, CvType.CV_8UC4);
//		frame.put(0, 0, data);
//		this.actualFrame = frame.clone();
//		// send table by web socket
//		List<Ball> newList = updateTable(frame);
//		newList = this.historyService.updateHistory(newList, this.maxBallRadius);
//		newList = this.historyService.findMissingBalls(newList, this.maxBallRadius);
//		this.table.setBalls(newList);
//		//this.table.setBalls(newList);
//		this.simpMessagingTemplate.convertAndSend("/table/live", this.table);
//
//		// if calibrate
//		if (this.status == 1) {
//			this.simpMessagingTemplate.convertAndSend("/calibrate/live", frame);
//		} else if (this.status == 2) {
//			generateMask(frame);
//		}
//	}

	// podmienione pod mocka
	public void send(Mat frame) {
		//Mat frame = new Mat(height, width, CvType.CV_8UC4);
		//frame.put(0, 0, data);
		this.actualFrame = frame.clone();
		// send table by web socket
		List<Ball> newList = updateTable(frame);
		newList = this.historyService.updateHistory(newList, this.maxBallRadius);
		newList = this.historyService.findMissingBalls(newList, this.maxBallRadius);
		this.table.setBalls(newList);
		//this.table.setBalls(newList);
		this.simpMessagingTemplate.convertAndSend("/table/live", this.table);

		// if calibrate
		if (this.status == 1) {
			this.simpMessagingTemplate.convertAndSend("/calibrate/live", frame);
		} else if (this.status == 2) {
			generateMask(frame);
		}
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
		// for each circle
		for (int i = 0; i < circles.cols(); i++) {
			double[] c = circles.get(0, i);
			// get position
			int x = (int) c[0] - r;
			int y = (int) c[1] - r;
			// reset sum
			double rectSum = 0;
			// for each pixel in distance < radius from circle center
			for (int j = y; j <= y + 2 * r; j++) {
				for (int k = x; k <= x + 2 * r; k++) {
					if (Math.pow(k - c[0], 2) + Math.pow(j - c[1], 2) <= Math.pow(c[2], 2)) {
						// if position is inside the image (array)
						if (j > 0 && k > 0 && j < table.getHeight() && k < table.getWidth()) {
							rectSum += image.get(j, k)[0];
							rectSum += image.get(j, k)[1];
							rectSum += image.get(j, k)[2];
						}
					}
				}
			}
			// current ball accept criteria
			if (rectSum > this.minWhiteBallDensity)
				return new Ball(0, new Point(c[0], c[1]));
		}
		// white ball not detected
		return null;
	}

	/**
	 * Try to detect Billiard Table by pockets and compute his parameters
	 * 
	 * @param calibrationParams Parameters that will be updated
	 * @return CalibrationParams contains new values
	 */
	public CalibrationParams automaticCalibration(CalibrationParams calibrationParams) {
		Mat frame = this.actualFrame.clone();
		Mat circles = new Mat();
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
		Imgproc.medianBlur(frame, frame, 9);
		Imgproc.HoughCircles(frame, circles, Imgproc.HOUGH_GRADIENT, 1.0, 32, 20.0, 25.0, 20, 40);
		List<Point> pockets = new ArrayList<Point>();
		double radius = 0;
		for (int i = 0; i < circles.cols(); i++) {
			double[] c = circles.get(0, i);
			if (c[2] > radius)
				radius = c[2];
			pockets.add(new Point(circles.get(0, i)));
		}
		radius = Math.floor(radius / 2);
		if (pockets.size() != 6)
			return null;

		for (int index = 0; index < pockets.size(); index++) {
			pockets.get(index).x = Math.floor(pockets.get(index).x);
			pockets.get(index).y = Math.floor(pockets.get(index).y);
		}
		pockets.sort((p1, p2) -> Double.compare(p1.x, p2.x));
		System.out.println(pockets);
		if (pockets.get(0).y < pockets.get(1).y) {
			calibrationParams.setLeftUpperCorner(new Point(pockets.get(0).x + radius, pockets.get(0).y + radius));
			calibrationParams.setLeftBottomCorner(new Point(pockets.get(1).x + radius, pockets.get(1).y - radius));
		} else {
			calibrationParams.setLeftUpperCorner(new Point(pockets.get(1).x + radius, pockets.get(1).y + radius));
			calibrationParams.setLeftBottomCorner(new Point(pockets.get(0).x + radius, pockets.get(0).y - radius));
		}
		if (pockets.get(5).y < pockets.get(4).y) {
			calibrationParams.setRightUpperCorner(new Point(pockets.get(5).x - radius, pockets.get(5).y + radius));
			calibrationParams.setRightBottomCorner(new Point(pockets.get(4).x - radius, pockets.get(4).y - radius));
		} else {
			calibrationParams.setRightUpperCorner(new Point(pockets.get(4).x - radius, pockets.get(4).y + radius));
			calibrationParams.setRightBottomCorner(new Point(pockets.get(5).x - radius, pockets.get(5).y - radius));
		}

		return calibrationParams;
	}
}
