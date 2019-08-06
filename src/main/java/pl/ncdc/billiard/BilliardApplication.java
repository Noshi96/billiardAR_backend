package pl.ncdc.billiard;

import org.opencv.core.Mat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BilliardApplication {
	
	public static void main(String[] args) {
		System.loadLibrary("opencv_java347");
		
		Mat mat = new Mat();
		SpringApplication.run(BilliardApplication.class, args);
	}

}
