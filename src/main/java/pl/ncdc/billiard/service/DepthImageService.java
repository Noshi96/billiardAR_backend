package pl.ncdc.billiard.service;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.springframework.stereotype.Service;

import edu.ufl.digitalworlds.j4k.DepthMap;

@Service
public class DepthImageService {
	/** CV_32F **/
	private float[] xyz;
	private int depthWidth;
	private int depthHeight;
	private DepthMap depthMap;
	
	private final float downTreshold = 2.325f;
	private final float upTreshold = 2.3f;
	
	public void load(float[] xyz, int depthWidth, int depthHeight) {
		this.xyz = xyz;
		this.depthWidth = depthWidth;
		this.depthHeight = depthHeight;
	}

	public void validateCircles(Mat circles) {
		this.depthMap = new DepthMap(this.depthWidth, this.depthHeight, xyz);
		
		byte[] xd = maskZ(depthMap, this.downTreshold, this.upTreshold);
		Mat mask = new Mat(depthMap.getHeight(), depthMap.getWidth(), CvType.CV_8UC1);
		mask.put(0, 0, xd);
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

}
