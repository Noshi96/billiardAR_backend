package pl.ncdc.billiard.service;

import java.awt.Point;

public class HitService {
double diameter = 10;
	
	public  Point findHittingPoint(int xBallSelected, int yBallSelected, int xPocket, int yPocket) {
		
		
		Point point = new Point();		
	
		
		double length = Math.sqrt((double)(xBallSelected - xPocket)*(double)(xBallSelected - xPocket) + (double)(yBallSelected- yPocket)* (double)(yBallSelected - yPocket));
		
		double dx = (double)(xBallSelected - xPocket) / length;
		double dy = (double)(yBallSelected - yPocket) / length;
		
		double x = xPocket + ((diameter+length) * dx);
		double y = yPocket + ((diameter+length)* dy);
		
		
		point.setLocation(x,y);
		return point;
	
	}
}
