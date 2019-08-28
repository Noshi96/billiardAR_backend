package pl.ncdc.billiard.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.Timer;
import java.util.TimerTask;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.springframework.stereotype.Service;

import edu.ufl.digitalworlds.j4k.DepthMap;

@Service
public class DepthImageService {

	int i = 0;

	DepthMap sec;
	DepthMap depthMap;
	/** CV_16S ==> convertTo(frame, CV_8S) **/
	private short[] data;
	/** CV_8S **/
	private byte[] player_index;
	/** CV_32F **/
	private float[] xyz;
	private float[] uv;
	private int depthWidth;
	private int depthHeight;

	public void load(short[] data, byte[] player_index, float[] xyz, float[] uv, int depthWidth, int depthHeight) {
		this.data = data;
		this.player_index = player_index;
		this.xyz = xyz;
		this.uv = uv;
		this.depthWidth = depthWidth;
		this.depthHeight = depthHeight;
	}

	public void validateCircles(Mat circles) {
		
		if (xyz == null || xyz.length == 0)
			return;
		this.depthMap = new DepthMap(this.depthWidth, this.depthHeight, xyz);
	}

	/**
	 * Use <i>HighGui.imshow()</i> to display an image, and wait 1 Ms
	 * <p>
	 * Require: {@code jvmArgs=['-Djava.awt.headless=false']} in <b>build.gradle</b>
	 * </p>
	 * 
	 * @param image Image to display
	 */
	private void show(Mat image) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// Imgproc.cvtColor(image, image, Imgproc.COLOR_BGRA2GRAY);
				HighGui.imshow("Image", image);
				HighGui.waitKey();
			}
		}, 0);
	}

}
