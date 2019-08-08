package pl.ncdc.billiard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import edu.ufl.digitalworlds.j4k.J4KSDK;



@SpringBootApplication
public class BilliardApplication {
	
	public static void main(String[] args) {
		System.loadLibrary("opencv_java347");
		SpringApplication.run(BilliardApplication.class, args);
		 Kinect kinect=new Kinect();
		// kinect.start(J4KSDK.COLOR);
	
	}

}
