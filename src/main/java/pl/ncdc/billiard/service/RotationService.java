package pl.ncdc.billiard.service;

import java.util.List;

import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.Ball;

@Service
public class RotationService {

	
	@Autowired
	MathService mathService;
	
	@Autowired
	HitService hitService;

	public double angleFollowRotation(double angleX) {

		double result = 0;
		result = (-3770.112) + (136.0718 * angleX) - (1.954948 * (Math.pow(angleX, 2)))
				+ (0.01400508 * ((Math.pow(angleX, 3)))) - (0.00004967584 * (Math.pow(angleX, 4)))
				+ (6.926647 * (Math.pow(10, -8)) * ((Math.pow(angleX, 5))));
		return result;

	}

	public Point rotationFollow(Point whiteBall, Point targetBall, Point pocket, double angle) {

		Point returnedPoint = new Point();
		double findAngle = 0;
		double newX = 0, newY = 0, mainLineX = 0, mainLineY = 0;

		// Wspolczynniki lini prostej dla target i targetBall.
		List<Double> aBFactors = mathService.abOfFunction(targetBall.x, targetBall.y, pocket.x, pocket.y);

		// Biore dowolny punkt z tej prostej ^
		// Max X stolu
		mainLineX = 1190;
		// mainLineX = targetBall.x + 100;

		mainLineY = (aBFactors.get(0) * mainLineX) + aBFactors.get(1);

		System.out.println("A factor = " + aBFactors.get(0));
		System.out.println("B factor = " + aBFactors.get(1));

		findAngle = angle;

		newX = (((mainLineX - targetBall.x) * Math.cos(Math.toRadians(findAngle)))
				- ((mainLineY - targetBall.y) * Math.sin(Math.toRadians(findAngle))) + targetBall.x);
		newY = (((mainLineX - targetBall.x) * Math.sin(Math.toRadians(findAngle)))
				+ ((mainLineY - targetBall.y) * Math.cos(Math.toRadians(findAngle))) + targetBall.y);

		returnedPoint.x = (int) newX;
		returnedPoint.y = (int) newY;

		return returnedPoint;
	}

	public Point whiteBallRotation(Point white, Point selected, Point pocket, List<Ball> listBall, int idPocket) {

		Point targetPoint = hitService.findHittingPoint(white, selected, pocket, listBall, idPocket).get(0);
		Point pointRotation = new Point();
		if (targetPoint != null) {

			double angleHit = mathService.findAngle(white, targetPoint, selected);

			double angleFollowRotation = angleFollowRotation(angleHit);

			pointRotation = rotationFollow(white, targetPoint, pocket, angleFollowRotation);

		}

		return pointRotation;

	}

	public Point whiteBallZeroRotation(Point white, Point selected, Point pocket, List<Ball> listBall, int idPocket) {

		Point targetPoint = hitService.findHittingPoint(white, selected, pocket, listBall, idPocket).get(0);

		if (targetPoint != null) {

			List<Double> listt = mathService.abOfFunction(targetPoint.x, targetPoint.y, pocket.x, pocket.y);
			double a = listt.get(0);
			double a2 = -1 / a;
			System.out.println(a2);

			double b2 = 0;

			b2 = targetPoint.y - (targetPoint.x * a2);

			double xNew = targetPoint.x + 100;
			double yNew = a2 * xNew + b2;

			double xNew2, yNew2;
			Point newPoint = new Point();

			newPoint.x = xNew;
			newPoint.y = yNew;

			Point secondOption = new Point();
			double angle = (mathService.findAngle(white, targetPoint, newPoint) * 57);
			if (angle >= 88 && angle <= 92) {
				return targetPoint;
			}

			if (angle > 92) {
				return newPoint;
			}

			else {

				xNew2 = targetPoint.x - 100;
				yNew2 = a2 * xNew2 + b2;

				secondOption.x = xNew2;
				secondOption.y = yNew2;

				return secondOption;
			}
		}

		return null;

	}

	
	
}
