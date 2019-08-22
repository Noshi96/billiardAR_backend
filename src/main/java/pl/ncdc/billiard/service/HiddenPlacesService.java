package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.Ball;

@Service
public class HiddenPlacesService {
	public double diameter = 20; // do zmiany
	
	@Autowired
	MathService mathService;
	
	public List<Point> showHiddenPlaces(Point white, List<Ball> listBall) {
		List<Point> hiddenPointsList = new ArrayList<Point>();
		double radius = 10;

		// petla
		for (int i = 0; i < listBall.size(); i++) {
			Point ballPoint = listBall.get(i).getPoint();

			List<Double> listt = mathService.abOfFunction(white.x, white.y, ballPoint.x, ballPoint.y);

			double a = listt.get(0);
			double aWhite = -1 / a;

			double bWhite = 0;

			bWhite = white.y - (white.x * aWhite);

			double aBall = -1 / a;
			double bBall = ballPoint.y - (ballPoint.x * aBall);

			Point whitePosShift = new Point();
			Point whiteNegShift = new Point();
			Point ballPosShift = new Point();
			Point ballNegShift = new Point();

			double whitePosShiftVar = white.x + radius * Math.sqrt((1 / (1 + bWhite)));
			whitePosShift.x = whitePosShiftVar;
			whitePosShift.y = aWhite * whitePosShiftVar + bWhite;

			double whiteNegShiftVar = white.x - radius * Math.sqrt((1 / (1 + bWhite)));
			whiteNegShift.x = whiteNegShiftVar;
			whiteNegShift.y = aWhite * whiteNegShiftVar + bWhite;

			double ballPosShiftVar = ballPoint.x + radius * Math.sqrt((1 / (1 + bBall)));
			ballPosShift.x = ballPosShiftVar;
			ballPosShift.y = aBall * ballPosShiftVar + bBall;

			double ballNegShiftVar = ballPoint.x + radius * Math.sqrt((1 / (1 + bBall)));
			ballNegShift.x = ballNegShiftVar;
			ballNegShift.y = aBall * ballNegShiftVar + bBall;

			double lengthFirst = Math.sqrt(
					Math.pow(ballNegShift.x - whitePosShift.x, 2) + Math.pow(ballNegShift.y - whitePosShift.y, 2));

			double dx = (ballNegShift.x - whitePosShift.x) / lengthFirst;
			double dy = (ballNegShift.y - whitePosShift.y) / lengthFirst;

			double x = whitePosShift.x + ((diameter * 3 + lengthFirst) * dx);
			double y = whitePosShift.y + ((diameter * 3 + lengthFirst) * dy);

			Point pointTargetFirst = new Point();
			pointTargetFirst.x = x;
			pointTargetFirst.y = y;

			double lengthSecond = Math.sqrt(
					Math.pow(ballPosShift.x - whiteNegShift.x, 2) + Math.pow(ballPosShift.y - whiteNegShift.y, 2));

			double dxSec = (ballPosShift.x - whiteNegShift.x) / lengthSecond;
			double dySec = (ballPosShift.y - whiteNegShift.y) / lengthSecond;

			double xSec = whiteNegShift.x + ((diameter * 3 + lengthSecond) * dxSec);
			double ySec = whiteNegShift.y + ((diameter * 3 + lengthSecond) * dySec);

			Point pointTargetSecond = new Point();
			pointTargetSecond.x = xSec;
			pointTargetSecond.y = ySec;

			hiddenPointsList.add(ballPosShift);
			hiddenPointsList.add(pointTargetFirst);
			hiddenPointsList.add(pointTargetSecond);
			hiddenPointsList.add(ballNegShift);
			

		}
		return hiddenPointsList;

	}

	
	
	
	
	
}
