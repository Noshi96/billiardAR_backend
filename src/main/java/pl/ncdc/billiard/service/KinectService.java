package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.Video;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.Kinect;
import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.CalibrationParams;

@Service
public class KinectService {

	@Autowired
	private BilliardTable table;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	private CalibrationParams calibrationParams;
	private Kinect kinect;
	// Mask from empty billiard table
	private Mat fgMask;
	//
	private BackgroundSubtractor backSub;

	// kalibracja
	private int left_margin;
	private int top_margin;
	private int areaHeight;
	private int areaWidth;

	public void init() {

		//table = new BilliardTable();
		table.setHeight(this.areaHeight);
		table.setWidth(this.areaWidth);
		// read tmp mask
		fgMask = Imgcodecs.imread("src/main/resources/mask.jpg");
		// crop mask
		fgMask = crop(left_margin, top_margin, this.areaWidth, this.areaHeight, fgMask);
		// convert mask to gray
		Imgproc.cvtColor(fgMask, fgMask, Imgproc.COLOR_BGR2GRAY);
		// create substractor
		backSub = Video.createBackgroundSubtractorMOG2();
		// apply mask
		backSub.apply(fgMask, fgMask, 1);
	}

	public void send(byte[] data, int height, int width) {
		// convert data to image
		Mat frame = new Mat(height, width, CvType.CV_8UC4);
		frame.put(0, 0, data);
		// create grayscale image
		Mat gray = new Mat();
		Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
		// cut image on edges
		gray = crop(left_margin, top_margin, this.areaWidth, this.areaHeight, gray);
		// create and apply mask
		fgMask = new Mat();
		backSub.apply(gray, fgMask, 0);
		// apply blur
		Imgproc.medianBlur(fgMask, fgMask, 5);
		// detect circles
		Mat circles = new Mat();
		Imgproc.HoughCircles(fgMask, circles, Imgproc.HOUGH_GRADIENT, 1.0, (double) gray.rows() / 16, 15.0, 10.0, 10,
				16);
		// save detected balls to a list
		List<Ball> list = new ArrayList<Ball>();
		Ball ball;

		// detecte white ball
		Point whiteBallPoint = whiteBallDetection(frame, circles, left_margin, top_margin, this.areaWidth, this.areaHeight, 14);
		ball = new Ball(0, whiteBallPoint);
		table.setWhiteBall(ball);

		for (int x = 0; x < circles.cols(); x++) {
			double[] c = circles.get(0, x);
			Point point = new Point();
			point.x = width - c[0];
			point.y = c[1];
			if (table.getWhiteBall().getPoint().x != point.x) {
				ball = new Ball(0, point);
				list.add(ball);
			}
		}
		// sort balls by X
		list.sort((o1, o2) -> {
			if (o1.getPoint().x > o2.getPoint().x)
				return -1;
			return 1;
		});
		for (int i = 1; i < list.size(); i++)
			list.get(i).setId(i);
		table.setBalls(list);
		// send table by websocket
		simpMessagingTemplate.convertAndSend("/table/live", table);
	}

	public static Point whiteBallDetection(Mat fullPicture, Mat circles, int xCut, int yCut, int width, int height,
			int radius) {
		double maxSum = Integer.MIN_VALUE;
		Point whiteBallPoint = new Point(null);

		for (int i = 0; i < circles.cols(); i++) {
			double[] c = circles.get(0, i);
			Point center = new Point(c[0], c[1]);
			Rect rect = new Rect((int) center.x - radius + xCut, (int) center.y - radius + yCut, radius * 2,
					radius * 2);

			double rectSum = 0;
			for (int j = rect.y; j <= rect.y + rect.height; j++) {
				for (int k = rect.x; k <= rect.x + rect.width; k++) {
					rectSum += fullPicture.get(j, k)[0];
					rectSum += fullPicture.get(j, k)[1];
					rectSum += fullPicture.get(j, k)[2];
				}
			}

			if (rectSum > maxSum) {
				whiteBallPoint = center;
				maxSum = rectSum;
			}
		}

		return whiteBallPoint;
	}

	public Mat crop(int x, int y, int width, int height, Mat img) {
		Rect r = new Rect(x, y, width, height);
		Mat cropped = new Mat(img, r);
		return cropped;
	}

	public int getLeft_margin() {
		return left_margin;
	}

	public void setLeft_margin(int left_margin) {
		this.left_margin = left_margin;
	}

	public int getTop_margin() {
		return top_margin;
	}

	public void setTop_margin(int top_margin) {
		this.top_margin = top_margin;
	}

	public int getAreaHeight() {
		return areaHeight;
	}

	public void setAreaHeight(int areaHeight) {
		this.areaHeight = areaHeight;
	}

	public int getAreaWidth() {
		return areaWidth;
	}

	public void setAreaWidth(int areaWidth) {
		this.areaWidth = areaWidth;
	}

    public void updateCalibrationParams(CalibrationParams calibrationParams) {
		this.calibrationParams = calibrationParams;
    }
}
