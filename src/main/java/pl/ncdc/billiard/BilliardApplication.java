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
	
	public static void main(String[] args) {
		System.loadLibrary("opencv_java347");
		SpringApplication.run(BilliardApplication.class, args);
	}

	@Bean
	public GeometryFactory geometryFactory() {
		return new GeometryFactory();
	}

}
