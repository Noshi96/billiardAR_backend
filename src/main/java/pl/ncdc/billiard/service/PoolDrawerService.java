package pl.ncdc.billiard.service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.BilliardTable;

@Service
public class PoolDrawerService {

	@Autowired
	BilliardTableService billiardTableService;
	
	@Value("${kinectService.mask}")
	private String filename;
	
	
	
	//zmienne do jakiegos pliku
	int kinectHeight = 600;
	int kinectWidth = 1200;
	
	int projectorMaxHeight = 1080-1;
	int projectorMaxWidth = 1920;
	// koniec zmiennych do pliku
	
	
	public byte[] drawImage(BilliardTable table)
	{
		
		
		
		
//		Mat poolTableArea = new Mat(projectorMaxHeight,projectorMaxWidth, CvType.CV_8UC3);
		Mat poolPlayZoneMat = new Mat(kinectHeight,kinectWidth, CvType.CV_8UC3);
		
		examp(poolPlayZoneMat);
		
		
		
		
		//Drawing a Circle
		Imgproc.circle (
			poolPlayZoneMat,          //Matrix obj of the image
			new Point(0, 0),    //Center of the circle
			100,                    //Radius
			new Scalar(0, 0, 255),  //Scalar object for color
			10                      //Thickness of the circle
		);
		

	    
//	    // Drawing a Rectangle
	    Imgproc.rectangle (
			poolPlayZoneMat,          //Matrix obj of the image
		    new Point(0, 0),        //p1
		    new Point(kinectWidth-1, kinectHeight-1),       //p2
		    new Scalar(0, 255, 255),     //Scalar object for color
		    1                          //Thickness of the line
	    );
	    
//	    MatOfPoint2f src = new MatOfPoint2f(
//	    		new Point(0,0),
//	    		new Point(projectorMaxWidth, 0),
//	    		new Point(projectorMaxWidth,projectorMaxHeight),
//	    		new Point(0, projectorMaxHeight)
//	    		);
	    
	    MatOfPoint2f sourceMat = new MatOfPoint2f(
	    		new Point(0,0),
	    		new Point(kinectWidth, 0),
	    		new Point(kinectWidth, kinectHeight),
	    		new Point(0, kinectHeight)
	    		);
	    
//	    // example streching
//	    MatOfPoint2f dst = new MatOfPoint2f(
//	    		new Point( 105, 122),
//	    		new Point( projectorMaxWidth, 0),
//	    		new Point( projectorMaxWidth - 305, projectorMaxHeight - 322),
//	    		new Point( 105, projectorMaxHeight - 122)
//	    		);
	    
	    
	    MatOfPoint2f destinationMat = new MatOfPoint2f(
	    		new Point( 105, 122),
	    		new Point( projectorMaxWidth - 105, 122),
	    		new Point( projectorMaxWidth - 105, projectorMaxHeight - 122),
	    		new Point( 105, projectorMaxHeight - 122)
	    		);
	    
	    Mat xd = Imgproc.getPerspectiveTransform(sourceMat, destinationMat);
	    
	    Imgproc.warpPerspective(
	    		poolPlayZoneMat,
	    		poolPlayZoneMat,
	    		xd,
	    		new Size(projectorMaxWidth,projectorMaxHeight));
	    //get perspective transform
	      
	      
		
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".png", poolPlayZoneMat, matOfByte);
		Base64.Encoder encoder = Base64.getEncoder();
		
		return encoder.encode(matOfByte.toArray());
	}
	
	public void examp(Mat mat) {
		//Drawing a Circle
		Imgproc.circle (
			mat,          //Matrix obj of the image
			new Point(400, 440),    //Center of the circle
			100,                    //Radius
			new Scalar(70, 255, 55),  //Scalar object for color
			5                      //Thickness of the circle
		);
	}
	

}