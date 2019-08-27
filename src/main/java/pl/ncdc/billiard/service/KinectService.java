package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
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
		this.actualFrame = new Mat();
		this.perspectiveTransform = new Mat();
	}

	@PostConstruct
	private void init() {
		this.kinect.start(Kinect.COLOR);
		this.status = 0;
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

		this.perspectiveTransform.release();
		this.perspectiveTransform = Imgproc.getPerspectiveTransform(src, dst);

		src.release();
		dst.release();
		
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
		
		this.actualFrame.release();
		this.actualFrame = frame.clone();

		List<Ball> newList = updateTable(frame);
		
		this.historyService.updateHistory(newList, this.maxBallRadius);
		this.historyService.removeFalseBalls(newList, this.maxBallRadius);
		this.historyService.findMissingBalls(newList, this.maxBallRadius);
		this.historyService.updateHistory(this.table.getWhiteBall(), maxBallRadius);
		removeFalseWhite(newList);
		sort(newList);

		this.table.setBalls(newList);
		this.simpMessagingTemplate.convertAndSend("/table/live", this.table);
		
		frame.release();

	}

	/**
	 * Sort ball list by x position. Set its id's
	 * 
	 * @param list list to sort
	 */
	private void sort(List<Ball> list) {
		list.sort((o1, o2) -> Double.compare(o1.getPoint().x, o2.getPoint().x));
		for (int i = 1; i < list.size(); i++)
			list.get(i).setId(i);
	}

	/**
	 * Remove false ball from list if detected as white
	 * 
	 * @param list
	 */
	private void removeFalseWhite(List<Ball> list) {
		Ball whiteBall = this.table.getWhiteBall();
		if (whiteBall == null)
			return;
		Ball inList = this.historyService.findBallByPoint(whiteBall.getPoint(), list, this.maxBallRadius);
		if (inList == null)
			return;
		list.remove(inList);
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
		Mat Lab = new Mat();
		
		// wrap image
		Imgproc.warpPerspective(frame, frame, this.perspectiveTransform,
				new Size(this.table.getWidth(), this.table.getHeight()), Imgproc.INTER_CUBIC);
		
        Imgproc.cvtColor(frame, Lab, Imgproc.COLOR_BGR2Lab);
        
        List<Mat> channelList = new ArrayList<Mat>(3);
		Core.split(Lab, channelList);
		Mat channelL = new Mat();

		channelList.get(1).copyTo(channelL);
		
		// apply blur
		Imgproc.medianBlur(channelL, channelL, 5);
		// show(mask);
		Imgproc.HoughCircles(channelL, circles, Imgproc.HOUGH_GRADIENT, 1.0, this.minBallRadius * 2, 25.0, 10.0,
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
		
		circles.release();
		Lab.release();
		channelL.release();
		
		return list;
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
		/** Actual detected white ball **/
		Ball white = null;
		
		Mat whiteMask = new Mat();
		image.copyTo(whiteMask);
		
		Scalar lowerVal = new Scalar(200, 200, 200);
		Scalar upperVal = new Scalar(255, 255, 255);
		
		Core.inRange(image, lowerVal, upperVal, whiteMask);
		
		double percentage = 0.50;
		double maxWhite = Integer.MIN_VALUE;
		
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
							if(whiteMask.get(j,k)[0] > 1)
							{
								rectSum += 1;
							}
						}
					}
				}
			}
			if (rectSum > maxWhite) {
				maxWhite = rectSum;
				white = new Ball(0, new Point(c[0], c[1]));
			}
		}
		
		whiteMask.release();
		
		maxWhite = maxWhite / (Math.PI * r * r);
		if (maxWhite > percentage) {
			return white;
		}
		
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
		frame.release();
		circles.release();
		
		return calibrationParams;
	}
}
