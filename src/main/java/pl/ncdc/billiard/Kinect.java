package pl.ncdc.billiard;

import org.opencv.core.Point;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.Video;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import pl.ncdc.billiard.entity.Ball;
import pl.ncdc.billiard.entity.Pocket;

public class Kinect extends J4KSDK {

	BilliardTable table;
	Mat fgMask = null;
	Mat frame = null;

	int x, y, w, h;
	BackgroundSubtractor backSub;

	public Kinect() {
		super();
		// tmeporary settings
		x = 435;
		y = 280;
		w = 1190;
		h = 620;
		// recive = false;
		table = new BilliardTable();
		// read tmp mask
		fgMask = Imgcodecs.imread("mask.jpg");
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

		// move to service
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
				16);
		List<Ball> list = new ArrayList<Ball>();
		Ball ball;

		for (int x = 0; x < circles.cols(); x++) {
			double[] c = circles.get(0, x);
			Point point = new Point();
			point.x = w - c[0];
			point.y = c[1];
			ball = new Ball(x, point);
			if (x == 0)
				table.setWhiteBall(ball);
			else
				list.add(ball);
		}
		table.setBalls(list);
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
		return table;
	}

	public void setTable(BilliardTable table) {
		this.table = table;
	}

}
