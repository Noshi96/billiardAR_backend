package pl.ncdc.billiard.service;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;
import org.springframework.stereotype.Service;

import edu.ufl.digitalworlds.j4k.DepthMap;

@Service
public class DepthImageService {

	private float[] xyz;
	private int depthWidth;
	private int depthHeight;
	private Mat perspectiveTransform;

	private final static float DOWN_TRESHOLD = 2.325f;
	private final static float UP_TRESHOLD = 2.3f;

	public DepthImageService() {
		this.perspectiveTransform = new Mat();
		// <- Usunac odtad
		this.depthHeight = 424;
		this.depthWidth = 512;
		// <- dotad
	}

	public void load(float[] xyz, int depthWidth, int depthHeight) {
		this.xyz = xyz;
		this.depthWidth = depthWidth;
		this.depthHeight = depthHeight;
	}

	public void validateCircles(Mat circles) {
		// <- Usunac odtad
		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("xyz"));
			this.xyz = (float[]) inputStream.readObject();
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// <- dotad

		DepthMap depthMap = new DepthMap(this.depthWidth, this.depthHeight, this.xyz);

		byte[] xd = maskZ(depthMap);

		Mat mask = new Mat(this.depthHeight, this.depthWidth, CvType.CV_8UC1);
		mask.put(0, 0, xd);

		Imgproc.warpPerspective(mask, mask, this.perspectiveTransform, new Size(1168, 584), Imgproc.INTER_CUBIC);

		HighGui.imshow("img", mask);
		HighGui.waitKey();
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
		pts.add(new Point(62,125));
		pts.add(new Point(64,327));
		pts.add(new Point(465,326));
		pts.add(new Point(465,125));

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
