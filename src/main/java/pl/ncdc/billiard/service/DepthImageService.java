package pl.ncdc.billiard.service;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.opencv.core.Mat;
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

		if (data == null || data.length == 0)
			return;
		this.depthMap = new DepthMap(this.depthWidth, this.depthHeight, xyz);
		if (i == 0)
			try {
//				// save data to files
//				ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("data"));
//				outputStream.writeObject(data);
//				outputStream.close();
//				outputStream = new ObjectOutputStream(new FileOutputStream("player_index"));
//				outputStream.writeObject(player_index);
//				outputStream.close();
//				outputStream = new ObjectOutputStream(new FileOutputStream("xyz"));
//				outputStream.writeObject(xyz);
//				outputStream.close();
//				outputStream = new ObjectOutputStream(new FileOutputStream("uv"));
//				outputStream.writeObject(uv);
//				outputStream.close();

				// save data to files
				ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("data"));
				data = (short[]) inputStream.readObject();
				inputStream.close();
				inputStream = new ObjectInputStream(new FileInputStream("player_index"));
				player_index = (byte[]) inputStream.readObject();
				inputStream.close();
				inputStream = new ObjectInputStream(new FileInputStream("xyz"));
				xyz = (float[]) inputStream.readObject();
				inputStream.close();
				inputStream = new ObjectInputStream(new FileInputStream("uv"));
				uv = (float[]) inputStream.readObject();
				inputStream.close();
				System.out.println(data.length);
				System.out.println(player_index.length);
				System.out.println(xyz.length);
				System.out.println(uv.length);

			} catch (Exception e) {
				e.printStackTrace();
			}
		i++;
	}

}
