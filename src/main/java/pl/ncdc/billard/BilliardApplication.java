package pl.ncdc.billard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import edu.ufl.digitalworlds.j4k.J4KSDK;



@SpringBootApplication
public class BilliardApplication {
	
	public static void main(String[] args) {
		System.loadLibrary("opencv_java347");
		SpringApplication.run(BilliardApplication.class, args);
		/*
		Kinect kinect = new Kinect();
		kinect.start(Kinect.COLOR);
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		kinect.stop();
		*/
	}

}
