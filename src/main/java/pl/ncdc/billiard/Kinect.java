package pl.ncdc.billiard;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import edu.ufl.digitalworlds.j4k.J4KSDK;

public class Kinect extends J4KSDK {

	int i = 0;
	
	@Override
	public void onSkeletonFrameEvent(boolean[] skeleton_tracked, float[] positions, float[] orientations, byte[] joint_status) {
		System.out.println("SkeletonFrameEvent");
	}
	@Override
	public void onColorFrameEvent(byte[] data) {
		System.out.println("Frame: "+ (i++));
		HoughCircles(data);
	}
	public void HoughCircles(byte[] data) {
		Mat frame = new Mat(getColorHeight(), getColorWidth(), CvType.CV_8UC4);
		frame.put(0, 0, data);
		
		Mat gray = new Mat();
		Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.medianBlur(gray, gray, 5);
		Mat circles = new Mat();
		Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0, (double) gray.rows() / 16, // change this value
																									// to detect circles
																									// with different
																									// distances to each
																									// other
				100.0, 30.0, 1, 30); // change the last two parameters
		// (min_radius & max_radius) to detect larger circles
		for (int x = 0; x < circles.cols(); x++) {
			double[] c = circles.get(0, x);
			Point center = new Point(Math.round(c[0]), Math.round(c[1]));
			// circle center
			Imgproc.circle(gray, center, 1, new Scalar(0, 100, 100), 3, 8, 0);  
			// circle outline
			int radius = (int) Math.round(c[2]);
			Imgproc.circle(gray, center, radius, new Scalar(255, 0, 255), 3, 8, 0);
		}
		//HighGui.imshow("detected circles", gray);
		//HighGui.waitKey(1);
	}
	@Override
	public void onDepthFrameEvent(short[] depth_frame, byte[] body_index, float[] xyz, float[] uv) {
		System.out.println("DepthFrameEvent");
	}
	

	
}
