package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;
import org.springframework.stereotype.Service;

import edu.ufl.digitalworlds.j4k.DepthMap;
import pl.ncdc.billiard.models.Ball;

@Service
public class DepthImageService {

	private float[] xyz;
	private int depthWidth;
	private int depthHeight;
	private Mat perspectiveTransform;

	private final static float DOWN_TRESHOLD = 2.325f;
	private final static float UP_TRESHOLD = 2.3f;
	private final static double MIN_DETECTION_RATE = 0.6;

	public DepthImageService() {
		this.perspectiveTransform = new Mat();
	}

	public void load(float[] xyz, int depthWidth, int depthHeight) {
		this.xyz = xyz;
		this.depthWidth = depthWidth;
		this.depthHeight = depthHeight;
	}

	public List<Ball> validateCircles(Mat circles, Size size, int radius) {

		DepthMap depthMap = new DepthMap(this.depthWidth, this.depthHeight, this.xyz);

		byte[] xd = maskZ(depthMap);

		Mat mask = new Mat(this.depthHeight, this.depthWidth, CvType.CV_8UC1);
		mask.put(0, 0, xd);

		Imgproc.warpPerspective(mask, mask, this.perspectiveTransform, size, Imgproc.INTER_CUBIC);
		List<Ball> list = new ArrayList<Ball>();
		for (int index = 0; index < circles.cols(); index++) {

			double[] c = circles.get(0, index);
			double area = Math.PI * Math.pow(c[2], 2);
			int ballArea = 0;

			Point point = new Point(c[0], c[1]);

			int xMin = Math.max((int) point.x - radius, 0);
			int yMin = Math.max((int) point.y - radius, 0);
			int xMax = Math.min((int) point.x + radius, (int) size.width - 1);
			int yMax = Math.min((int) point.y + radius, (int) size.height - 1);

			for (int x = xMin; x < xMax; x++)
				for (int y = yMin; y < yMax; y++)
					if (mask.get(y, x)[0] == 0)
						ballArea++;
			
			if (ballArea > area * MIN_DETECTION_RATE) {
				// draw circles on image
				Imgproc.circle(mask, point, (int) c[2], new Scalar(255, 0, 0));
				// add ball to the list
				list.add(new Ball(0, point));
			}
		}
		HighGui.imshow("img", mask);
		HighGui.waitKey(1000);
		return list;
	}

	public byte[] maskZ(DepthMap depthMap) {
		byte mask[] = new byte[depthMap.getWidth() * depthMap.getHeight()];
		for (int i = 0; i < mask.length; i++) {
			if ((depthMap.realZ[i] < DOWN_TRESHOLD) && (depthMap.realZ[i] > UP_TRESHOLD))
				mask[i] = (byte) 255;
			else
				mask[i] = 0;
		}
		return mask;
	}

	public void generateMask(int width, int height) {

		List<Point> pts = new ArrayList<Point>();
		pts.add(new Point(62, 125));
		pts.add(new Point(64, 327));
		pts.add(new Point(465, 326));
		pts.add(new Point(465, 125));

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

}
