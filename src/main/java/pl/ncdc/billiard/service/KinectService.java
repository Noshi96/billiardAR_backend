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
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.Kinect;
import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.BilliardTable;

@Service
public class KinectService {
	
	@Autowired
	private BilliardTable table;
	
	Kinect kinect;
	// Mask from empty billiard table
	Mat fgMask;
	// recived frame
	Mat frame;
	//
	BackgroundSubtractor backSub;
	
	// kalibracja
	int x;
	int y;
	int h;
	int w;
	
	public KinectService() {
		
		x = 435;
		y = 280;
		w = 1190;
		h = 620;
		// read tmp mask
		fgMask = Imgcodecs.imread("src/main/resources/mask.jpg");
		// crop mask
		fgMask = crop(x, y, w, h, fgMask);
		// convert mask to gray
		Imgproc.cvtColor(fgMask, fgMask, Imgproc.COLOR_BGR2GRAY);
		// create substractor
		backSub = Video.createBackgroundSubtractorMOG2();
		// apply mask
		backSub.apply(fgMask, fgMask, 1);
	
	}
	
	public void send(byte[] data, int height, int width) {
		frame = new Mat(height, width, CvType.CV_8UC4);
		frame.put(0, 0, data);
		Mat gray = new Mat();
		Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
		gray = crop(x, y, w, h, gray);
		fgMask = new Mat();
		backSub.apply(gray, fgMask, 0);
		Imgproc.medianBlur(fgMask, fgMask, 5);
		Mat circles = new Mat();
		Imgproc.HoughCircles(fgMask, circles, Imgproc.HOUGH_GRADIENT, 1.0, (double) gray.rows() / 16, 15.0, 10.0, 10,
				16);
		List<Ball> list = new ArrayList<Ball>();
		Ball ball;

		Point whiteBallPoint = whiteBallDetection(frame, circles, x, y, w, h, 14);
		ball = new Ball(0, whiteBallPoint);
		table.setWhiteBall(ball);

		for (int x = 0; x < circles.cols(); x++) {
			double[] c = circles.get(0, x);
			Point point = new Point();
			point.x = w - c[0];
			point.y = c[1];
			if (table.getWhiteBall().getPoint().x != point.x) {
				ball = new Ball(0, point);
				list.add(ball);
			}
		}
		list.sort((o1, o2) -> {
			if (o1.getPoint().x > o2.getPoint().x)
				return -1;
			return 1;
		});
		for (int i = 1; i < list.size(); i++)
			list.get(i).setId(i);
		table.setBalls(list);
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
	public Mat crop(int x, int y, int w, int h, Mat img) {
		Rect r = new Rect(x, y, w, h);
		Mat cropped = new Mat(img, r);
		return cropped;
	}
}
