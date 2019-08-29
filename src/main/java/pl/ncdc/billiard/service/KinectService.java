package pl.ncdc.billiard.service;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
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
	@Autowired
	private DepthImageService depthImageService;
	@Autowired
	MathService math;

	private Kinect kinect;

	public final static int FLAG = Kinect.COLOR | Kinect.XYZ | Kinect.DEPTH;

	private Mat perspectiveTransform;

	private int minBallRadius;
	private int maxBallRadius;

	Mat actualFrame;

	@Autowired
	public KinectService(@Lazy Kinect kinect) {
		this.kinect = kinect;
		this.actualFrame = new Mat();
		this.perspectiveTransform = new Mat();
	}

	byte[] data;
	float[] xyz;

	@PostConstruct
	private void init() {
		this.kinect.start(FLAG);
		// SIMULATED KINECT
		/*
		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("data"));
			data = (byte[]) inputStream.readObject();
			inputStream.close();

			inputStream = new ObjectInputStream(new FileInputStream("xyz"));
			xyz = (float[]) inputStream.readObject();
			inputStream.close();

			this.depthImageService.load(xyz, 512, 424);

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					send(data, 1080, 1920);
				}
			}, 5000, 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// END OF SIMULATED KINECT*/
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

		this.historyService.updateHistory(newList, this.table.getWhiteBall(), this.maxBallRadius);

		this.table.setBalls(newList);
		Ball selected = this.table.getSelectedBall();
		if (selected != null && math.findBallByPoint(newList, selected.getPoint(), maxBallRadius) == null)
			this.table.setSelectedBall(null);
		this.simpMessagingTemplate.convertAndSend("/table/live", this.table);

		frame.release();
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

		channelList.get(0).copyTo(channelL);

		// apply blur
		Imgproc.medianBlur(channelL, channelL, 5);
		// show(mask);
		Imgproc.HoughCircles(channelL, circles, Imgproc.HOUGH_GRADIENT, 1.0, this.minBallRadius * 2, 25.0, 10.0,
				this.minBallRadius, this.maxBallRadius);

		// save detected balls to a list
		List<Ball> list = depthImageService.validateCircles(circles, new Size(1168, 584), maxBallRadius);
		// Detect white ball

		if (list==null)
			return null;
		Ball whiteBall = whiteBallDetection(frame, list, this.maxBallRadius);

		// revert X-axis
		for (Ball ball : list)
			ball.getPoint().x = this.table.getWidth() - ball.getPoint().x;
		if (whiteBall!=null)
		whiteBall.getPoint().x = this.table.getWidth() - whiteBall.getPoint().x;

		this.table.setWhiteBall(whiteBall);

		circles.release();
		Lab.release();
		channelL.release();

		return list;
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
	public Ball whiteBallDetection(Mat frame, List<Ball> list, int radius) {

		Mat image = frame.clone();
		Imgproc.cvtColor(image, image, Imgproc.COLOR_BGRA2BGR);

		/** Actual detected white ball **/
		Ball white = null;

		Mat whiteMask = new Mat();
		image.copyTo(whiteMask);

		Scalar lowerVal = new Scalar(200, 200, 200);
		Scalar upperVal = new Scalar(255, 255, 255);

		Core.inRange(image, lowerVal, upperVal, whiteMask);

		// double percentage = 0.50;
		double maxWhite = Integer.MIN_VALUE;

		// for each circle
		for (Ball ball : list) {

			Point point = ball.getPoint();

			int xMin = Math.max((int) point.x - radius, 0);
			int yMin = Math.max((int) point.y - radius, 0);
			int xMax = Math.min((int) point.x + radius, frame.width() - 1);
			int yMax = Math.min((int) point.y + radius, frame.height() - 1);
			// reset sum
			double rectSum = 0;
			// for each pixel in distance < radius from circle center

			for (int x = xMin; x < xMax; x++)
				for (int y = yMin; y < yMax; y++)
					if (math.inCircle(x, y, point, radius) && whiteMask.get(y, x)[0] > 1)
						rectSum += 1;

			if (rectSum > maxWhite) {
				maxWhite = rectSum;
				white = new Ball(0, point);
			}
		}
		if (white != null)
			list.remove(white);

		whiteMask.release();
		image.release();

		// maxWhite = maxWhite / (Math.PI * r * r);
		// if (maxWhite > percentage) {
		return white;
		// }

		// return null;
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

	public Kinect getKinect() {
		return this.kinect;
	}

	public void generatePerspectiveTransform(Point leftTop, Point leftBottom, Point rightBottom, Point rightTop,
			int width, int height) {
		// add points to a list and generate Mat object
		List<Point> pts = new ArrayList<Point>();

		pts.add(leftTop);
		pts.add(leftBottom);
		pts.add(rightBottom);
		pts.add(rightTop);

		Mat src = Converters.vector_Point2f_to_Mat(pts);

		pts = new ArrayList<Point>();
		pts.add(new Point(0, 0));
		pts.add(new Point(0, height));
		pts.add(new Point(width, height));
		pts.add(new Point(width, 0));

		Mat dst = Converters.vector_Point2f_to_Mat(pts);

		this.perspectiveTransform.release();
		this.perspectiveTransform = Imgproc.getPerspectiveTransform(src, dst);

		src.release();
		dst.release();
	}

	public void setBallRadius(int radius) {
		this.minBallRadius = radius / 2 - 2;
		this.maxBallRadius = radius / 2 + 2;
	}
}
