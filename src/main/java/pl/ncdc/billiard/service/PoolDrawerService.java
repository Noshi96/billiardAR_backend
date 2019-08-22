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
	
	public void drawBalls(Ball balls[]) {
		
	}
	
	public byte[] drawImage()
	{
		
		//zmienne do jakiegos pliku
		int projectorMaxHeight = 1080-5;
		int projectorMaxWidth = 1920;
		// koniec zmiennych do pliku
		
		
		BilliardTable table = billiardTableService.getTable();
		System.out.println(table);
		Mat poolTableArea = new Mat(projectorMaxHeight,projectorMaxWidth, CvType.CV_8UC3);
		
		
		//poolTableArea = Imgcodecs.imread(this.filename);
		
		
		
		
		
		//Drawing a Circle
		Imgproc.circle (
			poolTableArea,          //Matrix obj of the image
			new Point(0, 0),    //Center of the circle
			100,                    //Radius
			new Scalar(0, 0, 255),  //Scalar object for color
			10                      //Thickness of the circle
		);
		

	    
//	    // Drawing a Rectangle
	    Imgproc.rectangle (
		    poolTableArea,             //Matrix obj of the image
		    new Point(0, 0),        //p1
		    new Point(projectorMaxWidth, projectorMaxHeight),       //p2
		    new Scalar(0, 255, 255),     //Scalar object for color
		    5                          //Thickness of the line
	    );
	    
	    MatOfPoint2f src = new MatOfPoint2f(
	    		new Point(0,0),
	    		new Point(projectorMaxWidth, 0),
	    		new Point(projectorMaxWidth,projectorMaxHeight),
	    		new Point(0, projectorMaxHeight)
	    		);
	    
	    MatOfPoint2f dst = new MatOfPoint2f(
	    		new Point(200,200),
	    		new Point(projectorMaxWidth-100, 100),
	    		new Point(projectorMaxWidth-50,projectorMaxHeight-100),
	    		new Point(100, projectorMaxHeight-200)
	    		);
	    
	    Mat xd = Imgproc.getPerspectiveTransform(src, dst);
	    
	    Imgproc.warpPerspective(
	    		poolTableArea,
	    		poolTableArea,
	    		xd,
	    		new Size(projectorMaxWidth,projectorMaxHeight));
	    //get perspective transform
	      
	      
		
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".png", poolTableArea, matOfByte);
		Base64.Encoder encoder = Base64.getEncoder();
		
		return encoder.encode(matOfByte.toArray());
	}
	
	
	public byte[] drawBalls()
	{
		//zmienne do jakiegos pliku
		int projectorMaxHeight = 1080-5;
		int projectorMaxWidth = 1920;
		// koniec zmiennych do pliku
		
		
		BilliardTable table = billiardTableService.getTable();
		System.out.println(table);
		Mat poolTableArea = new Mat(projectorMaxHeight,projectorMaxWidth, CvType.CV_8UC3);
		
		
		//poolTableArea = Imgcodecs.imread(this.filename);
		
		
		
		
		
		//Drawing a Circle
		Imgproc.circle (
			poolTableArea,          //Matrix obj of the image
			new Point(0, 0),    //Center of the circle
			100,                    //Radius
			new Scalar(0, 0, 255),  //Scalar object for color
			10                      //Thickness of the circle
		);
		

	    
//	    // Drawing a Rectangle
	    Imgproc.rectangle (
		    poolTableArea,             //Matrix obj of the image
		    new Point(0, 0),        //p1
		    new Point(projectorMaxWidth, projectorMaxHeight),       //p2
		    new Scalar(0, 255, 255),     //Scalar object for color
		    5                          //Thickness of the line
	    );
	    
	    MatOfPoint2f src = new MatOfPoint2f(
	    		new Point(0,0),
	    		new Point(projectorMaxWidth, 0),
	    		new Point(projectorMaxWidth,projectorMaxHeight),
	    		new Point(0, projectorMaxHeight)
	    		);
	    
	    MatOfPoint2f dst = new MatOfPoint2f(
	    		new Point(200,200),
	    		new Point(projectorMaxWidth-100, 100),
	    		new Point(projectorMaxWidth-50,projectorMaxHeight-100),
	    		new Point(100, projectorMaxHeight-200)
	    		);
	    
	    Mat xd = Imgproc.getPerspectiveTransform(src, dst);
	    
	    Imgproc.warpPerspective(
	    		poolTableArea,
	    		poolTableArea,
	    		xd,
	    		new Size(projectorMaxWidth,projectorMaxHeight));
	    //get perspective transform
	      
	      
		
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".png", poolTableArea, matOfByte);
		Base64.Encoder encoder = Base64.getEncoder();
		
		return encoder.encode(matOfByte.toArray());
	
	}
}