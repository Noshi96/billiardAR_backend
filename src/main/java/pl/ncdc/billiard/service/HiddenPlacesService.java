package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.BilliardTable;

@Service
public class HiddenPlacesService {
	
	@Autowired
	private BilliardTable table;

	double diameter = table.getBallRadius() * 2; 
	double radius = table.getBallRadius();
	
	@Autowired
	MathService mathService;
	
	public List<Point> showHiddenPlaces(Point white, List<Ball> listBall) {
		List<Point> hiddenPointsList = new ArrayList<Point>();
		double radius = 18;

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

			double whitePosShiftVar = white.x + radius * Math.sqrt((1 / (1 +  Math.pow(aWhite,2))));
			whitePosShift.x = whitePosShiftVar;
			whitePosShift.y = aWhite * whitePosShiftVar + bWhite;

			double whiteNegShiftVar = white.x - radius * Math.sqrt((1 / (1 + Math.pow(aWhite,2))));
			whiteNegShift.x = whiteNegShiftVar;
			whiteNegShift.y = aWhite * whiteNegShiftVar + bWhite;

			double ballPosShiftVar = ballPoint.x + radius * Math.sqrt((1 / (1 + Math.pow(aBall,2))));
			ballPosShift.x = ballPosShiftVar;
			ballPosShift.y = aBall * ballPosShiftVar + bBall;

			double ballNegShiftVar = ballPoint.x - radius * Math.sqrt((1 / (1 +  Math.pow(aBall,2))));
			ballNegShift.x = ballNegShiftVar;
			ballNegShift.y = aBall * ballNegShiftVar + bBall;

			double lengthFirst = Math.sqrt(
					Math.pow(ballNegShift.x - whitePosShift.x, 2) + Math.pow(ballNegShift.y - whitePosShift.y, 2));

			double dx = (ballNegShift.x - whitePosShift.x) / lengthFirst;
			double dy = (ballNegShift.y - whitePosShift.y) / lengthFirst;

			double x = whitePosShift.x + ((diameter * 100 + lengthFirst) * dx);
			double y = whitePosShift.y + ((diameter * 100 + lengthFirst) * dy);

			Point pointTargetFirst = new Point();
			pointTargetFirst.x = x;
			pointTargetFirst.y = y;

			double lengthSecond = Math.sqrt(
					Math.pow(ballPosShift.x - whiteNegShift.x, 2) + Math.pow(ballPosShift.y - whiteNegShift.y, 2));

			double dxSec = (ballPosShift.x - whiteNegShift.x) / lengthSecond;
			double dySec = (ballPosShift.y - whiteNegShift.y) / lengthSecond;

			double xSec = whiteNegShift.x + ((diameter * 100 + lengthSecond) * dxSec);
			double ySec = whiteNegShift.y + ((diameter * 100 + lengthSecond) * dySec);

			Point pointTargetSecond = new Point();
			pointTargetSecond.x = xSec;
			pointTargetSecond.y = ySec;

			hiddenPointsList.add(ballNegShift);
			hiddenPointsList.add(pointTargetFirst);
			hiddenPointsList.add(pointTargetSecond);
			hiddenPointsList.add(ballPosShift);
			

		}
		return hiddenPointsList;

	}

	
	
	
	
	
}
