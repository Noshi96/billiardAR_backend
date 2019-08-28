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
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;
import org.springframework.stereotype.Service;

import edu.ufl.digitalworlds.j4k.DepthMap;
import pl.ncdc.billiard.models.CalibrationParams;

@Service
public class DepthImageService {
	/** CV_32F **/
	private float[] xyz;
	private int depthWidth;
	private int depthHeight;
	private DepthMap depthMap;
	private Mat perspectiveTransform;
	
	private final float downTreshold = 2.325f;
	private final float upTreshold = 2.3f;
	
	//<- Usunac odtad
	public DepthImageService()
	{
		this.depthHeight = 424;
		this.depthWidth = 512;
		this.perspectiveTransform = new Mat();
	}
	//<- dotad
	
	public void load(float[] xyz, int depthWidth, int depthHeight) {
		this.xyz = xyz;
		this.depthWidth = depthWidth;
		this.depthHeight = depthHeight;
	}

	public void validateCircles(Mat circles) {
		//<- Usunac odtad
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("xyz"));
            this.xyz = (float[]) inputStream.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //<- dotad
		
		this.depthMap = new DepthMap(this.depthWidth, this.depthHeight, xyz);
		
		byte[] xd = maskZ(depthMap, this.downTreshold, this.upTreshold);
		Mat mask = new Mat(depthMap.getHeight(), depthMap.getWidth(), CvType.CV_8UC1);
		mask.put(0, 0, xd);
		
		Imgproc.warpPerspective(mask, mask, this.perspectiveTransform,
				new Size(1168, 584), Imgproc.INTER_CUBIC);
		
		HighGui.imshow("img", mask);
		HighGui.waitKey();
	}
	
	public byte[] maskZ(DepthMap depthMap, float downThreshold, float upThreshold)
	{
		byte mask[] = new byte[depthMap.getWidth() * depthMap.getHeight()];
		for(int i =0; i < mask.length; i++)
		{
			if((depthMap.realZ[i] < downThreshold)&&(depthMap.realZ[i] > upThreshold)) mask[i] = (byte)255;
			else mask[i] = (byte)0;
		}
		
		return mask;
	}

	public void updateCalibration(CalibrationParams calibrationParams) {
		List<Point> pts = new ArrayList<Point>();
		pts.add(new Point(62,125));
		pts.add(new Point(64,327));
		pts.add(new Point(465,326));
		pts.add(new Point(465,125));

		Mat src = Converters.vector_Point2f_to_Mat(pts);
		pts = new ArrayList<Point>();
		pts.add(new Point(0, 0));
		pts.add(new Point(0, 584));
		pts.add(new Point(1168, 584));
		pts.add(new Point(1168, 0));

		Mat dst = Converters.vector_Point2f_to_Mat(pts);

		this.perspectiveTransform.release();
		this.perspectiveTransform = Imgproc.getPerspectiveTransform(src, dst);	
	}

}
