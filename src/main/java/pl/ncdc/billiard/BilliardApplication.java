package pl.ncdc.billiard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BilliardApplication {
	
	public static void main(String[] args) {
		System.loadLibrary("opencv_java347");
		SpringApplication.run(BilliardApplication.class, args);
	}

}
