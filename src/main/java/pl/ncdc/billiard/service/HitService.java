package pl.ncdc.billiard.service;

import java.awt.Point;

import org.springframework.stereotype.Service;

@Service
public class HitService {
double diameter = 10;
	
	public  Point findHittingPoint(double xBallSelected, double yBallSelected, double xPocket, double yPocket) {
		
		Point point = new Point();		
	
		double length = Math.sqrt((xBallSelected - xPocket)*(xBallSelected - xPocket) + (yBallSelected- yPocket)* (yBallSelected - yPocket));
		
		double dx = (double)(xBallSelected - xPocket) / length;
		double dy = (double)(yBallSelected - yPocket) / length;
		
		double x = xPocket + ((diameter+length) * dx);
		double y = yPocket + ((diameter+length)* dy);
		
		
		point.setLocation(x,y);
		return point;
	
	}
}
