package pl.ncdc.billiard;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.Video;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import pl.ncdc.billiard.entity.Ball;

public class Kinect extends J4KSDK {

	//boolean recive;
	BilliardTable table;
	//int i = 0;
	Mat fgMask = null;
	Mat frame = null;

	int x, y, w, h;
	BackgroundSubtractor backSub;

	public Kinect() {
		super();

		//recive = false;
		table = new BilliardTable();

		// read tmp mask
		fgMask = Imgcodecs.imread("mask.jpg");
		// tmeporary settings
		x = 435;
		y = 280;
		w = 1190;
		h = 620;
		// crop mask
		fgMask = crop(x, y, w, h, fgMask);
		// convert mask to gray
		Imgproc.cvtColor(fgMask, fgMask, Imgproc.COLOR_BGR2GRAY);
		// create substractor
		backSub = Video.createBackgroundSubtractorMOG2();
		// apply mask
		backSub.apply(fgMask, fgMask, 1);

	}

	public void init() {
		// Learn VideoBackgroundSubstractor ( 10 samples )
		// cut frames to table
		// set x, y, w, h
	}

	@Override
	public void onSkeletonFrameEvent(boolean[] skeleton_tracked, float[] positions, float[] orientations,
			byte[] joint_status) {
	}

	@Override
	public void onColorFrameEvent(byte[] data) {
		//System.out.println("Frame: " + i++);
		frame = new Mat(getColorHeight(), getColorWidth(), CvType.CV_8UC4);
		frame.put(0, 0, data);
		Mat gray = new Mat();
		Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
		gray = crop(x, y, w, h, gray);
		fgMask = new Mat();
		backSub.apply(gray, fgMask, 0);
		Imgproc.medianBlur(fgMask, fgMask, 5);
		Mat circles = new Mat();
		Imgproc.HoughCircles(fgMask, circles, Imgproc.HOUGH_GRADIENT, 1.0, (double) gray.rows() / 16, 15.0, 10.0, 10,
				30);
		List<Ball> list = new ArrayList<Ball>();
		Ball ball;
		for (int x = 0; x < circles.cols(); x++) {
			double[] c = circles.get(0, x);
			// Point center = new Point();
			Point point = new Point();
			point.setLocation(w - c[0], h - c[1]);
/*
			double sum = 0;
			for (int i = 0; i < 2 * c[2]; i++) {
				for (int j = 0; j < 2 * c[2]; j++) {
					if (
							   i + (int) c[0] - (int) c[2] > 0 
							&& i + (int) c[0] - (int) c[2] < h
							&& j + (int) c[1] - (int) c[2] > 0 
							&& j + (int) c[1] - (int) c[2] < w)
						sum += fgMask.get(i + (int) c[0] - (int) c[2], j + (int) c[1] - (int) c[2])[0];
				}
			}
			if (sum > 10)
				System.out.println("X: " + c[0] + ", Y: " + c[1] + " Sum: " + sum);
*/
			ball = new Ball();
			ball.setPoint(point);
			list.add(ball);
/*
			org.opencv.core.Point center = new org.opencv.core.Point(Math.round(c[0]), Math.round(c[1]));
			// circle center
			Imgproc.circle(gray, center, 1, new Scalar(0, 100, 100), 3, 8, 0);
			// circle outline
			int radius = (int) Math.round(c[2]);
			Imgproc.circle(gray, center, radius, new Scalar(255, 0, 255), 3, 8, 0);
			org.opencv.core.Point lg = new org.opencv.core.Point(c[0] - c[2], c[1] - c[2]);
			org.opencv.core.Point pd = new org.opencv.core.Point(c[0] + c[2], c[1] + c[2]);
			Imgproc.rectangle(gray, lg, pd, new Scalar(255, 0, 0));
			*/

		}
		table.setBalls(list);
		// System.out.println(table.getBalls().size());

		//HighGui.imshow("Frame", gray);
		//HighGui.waitKey(1);
		//this.recive = true;
	}

	public Mat crop(int x, int y, int w, int h, Mat img) {
		Rect r = new Rect(x, y, w, h);
		Mat cropped = new Mat(img, r);
		return cropped;
	}

	@Override
	public void onDepthFrameEvent(short[] data, byte[] body_index, float[] xyz, float[] uv) {
	}

	public BilliardTable getTable() {
		//this.recive = false;
		return table;
	}

	public void setTable(BilliardTable table) {
		this.table = table;
	}

}
