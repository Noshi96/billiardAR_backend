package pl.ncdc.billiard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BilliardApplication {
	
	private Kinect kinect;
	
	@Autowired
	public BilliardApplication(Kinect kinect) {
		this.kinect = kinect;
		this.kinect.init();
		this.kinect.start(Kinect.COLOR);
	}
	
	public static void main(String[] args) {
		System.loadLibrary("opencv_java347");
		SpringApplication.run(BilliardApplication.class, args);
	}

}
