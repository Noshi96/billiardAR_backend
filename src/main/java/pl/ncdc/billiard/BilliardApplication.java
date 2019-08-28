package pl.ncdc.billiard;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.vividsolutions.jts.geom.GeometryFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import pl.ncdc.billiard.configurations.WebSocketAppender;


@SpringBootApplication
public class BilliardApplication {
	
	public static void main(String[] args) {
		System.loadLibrary("opencv_java347");
		ConfigurableApplicationContext context = SpringApplication.run(BilliardApplication.class, args);
		context.start();
		addCustomAppender(context, (LoggerContext) LoggerFactory.getILoggerFactory());
	}

	@Bean
	public GeometryFactory geometryFactory() {
		return new GeometryFactory();
	}

	private static void addCustomAppender(ConfigurableApplicationContext context, LoggerContext loggerContext) {
		WebSocketAppender customAppender = context.getBean(WebSocketAppender.class);
		Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
		rootLogger.addAppender(customAppender);
	}
}
