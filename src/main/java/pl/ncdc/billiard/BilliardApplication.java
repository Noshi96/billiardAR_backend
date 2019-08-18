package pl.ncdc.billiard;

import com.vividsolutions.jts.geom.GeometryFactory;
import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import org.springframework.context.annotation.Bean;
import pl.ncdc.billiard.service.HitService;



@SpringBootApplication
public class BilliardApplication {
	
	private Kinect kinect;
	
	@Autowired
	public BilliardApplication(Kinect kinect) {
		this.kinect = kinect;
		/*this.kinect.init();
		this.kinect.start(Kinect.COLOR);*/
	}
	
	public static void main(String[] args) {
		System.loadLibrary("opencv_java347");
		SpringApplication.run(BilliardApplication.class, args);
		/*Kinect kinect = new Kinect();
		kinect.start(Kinect.COLOR);
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		kinect.stop();*/
		
	}

	@Bean
	public GeometryFactory geometryFactory() {
		return new GeometryFactory();
	}

}
