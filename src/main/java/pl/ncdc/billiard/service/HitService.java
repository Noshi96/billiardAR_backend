package pl.ncdc.billiard.service;

import org.opencv.core.Point;
import java.util.ArrayList;

import java.util.List;

import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.Pocket;

@Service
public class HitService {

	double diameter = 20;

	public double findAngle(double xBallWhite, double yBallWhite, double xCenterPoint, double yCenterPoint,
			double xPocket, double yPocket) {
		double p0c = Math.sqrt(Math.pow(xCenterPoint - xBallWhite, 2) + Math.pow(yCenterPoint - yBallWhite, 2));

		double p1c = Math.sqrt(Math.pow(xCenterPoint - xPocket, 2) + Math.pow(yCenterPoint - yPocket, 2));
		double p0p1 = Math.sqrt(Math.pow(xPocket - xBallWhite, 2) + Math.pow(yPocket - yBallWhite, 2));
		return Math.acos((p1c * p1c + p0c * p0c - p0p1 * p0p1) / (2 * p1c * p0c));
	}

	public double findAngleOfCollision(double xBallSelected, double yBallSelected, double xBallDisturb,
			double yBallDisturb, double xPocket, double yPocket) {

		double p0c = Math.sqrt(Math.pow(xBallDisturb - xBallSelected, 2) + Math.pow(yBallDisturb - yBallSelected, 2));

		double p1c = Math.sqrt(Math.pow(xBallDisturb - xPocket, 2) + Math.pow(yBallDisturb - yPocket, 2));
		double p0p1 = Math.sqrt(Math.pow(xPocket - xBallSelected, 2) + Math.pow(yPocket - yBallSelected, 2));

		return Math.acos(((p1c * p1c + p0c * p0c - p0p1 * p0p1) / (2 * p1c * p0c)));

	}

	public Point findHittingPoint(double xBallWhite, double yBallWhite, double xBallSelected, double yBallSelected,
			double xPocket, double yPocket) {

		Point point = new Point();

		double length = Math.sqrt((double) (xBallSelected - xPocket) * (double) (xBallSelected - xPocket)
				+ (double) (yBallSelected - yPocket) * (double) (yBallSelected - yPocket));

		double dx = (double) (xBallSelected - xPocket) / length;
		double dy = (double) (yBallSelected - yPocket) / length;

		double x = xPocket + ((diameter + length) * dx);
		double y = yPocket + ((diameter + length) * dy);

		
		point.x = x;
		point.y = y;
//		point.
		if (findAngle(xBallWhite, yBallWhite, x, y, xPocket, yPocket) < 1.57) {
			return null;
		}
		return point;

	}

	public boolean findCollision(Point pocketPoint, Point selectedBall, int index, List<Ball> listBall) {

		for (int x = 1; x < listBall.size(); x++) {
			if (index != x) {
				double angle = findAngleOfCollision(selectedBall.x, selectedBall.y,
						listBall.get(x).getPoint().x, listBall.get(x).getPoint().y, pocketPoint.x,
						pocketPoint.y);
				angle *= 57;
				if (angle < 190 && angle > 160) {
					return false;
				}

			}

		}

		return true;

	}

	public List<NewPoint> allPossibleHits(List<Pocket> listPocket, List<Ball> listBall) {

		List<NewPoint> list = new ArrayList<NewPoint>();
		
		for (int x = 1; x < listBall.size(); x++) { // pierwsza bila w liscie jest biala index 0
			for (int y = 0; y < listPocket.size(); y++) {

				Point targetPoint = findHittingPoint(listBall.get(0).getPoint().x,
					listBall.get(0).getPoint().y, listBall.get(x).getPoint().x,
						listBall.get(x).getPoint().y, listPocket.get(y).getPoint().getX(),
						listPocket.get(y).getPoint().getY());

				Point pocketPoint = new Point();

				pocketPoint.x = (double) listPocket.get(y).getPoint().getX();
				pocketPoint.y = (double) listPocket.get(y).getPoint().getY();
				
				
				Point selectedPoint = new Point();
				
				
				selectedPoint.x = listBall.get(x).getPoint().x;
				selectedPoint.y = listBall.get(x).getPoint().y;

				boolean collisionFound = findCollision(pocketPoint, selectedPoint, x, listBall);
				if (targetPoint != null && collisionFound) {

					//points.put(targetPoint, pocketPoint);
					list.add(new NewPoint(targetPoint.x, targetPoint.y, pocketPoint.x, pocketPoint.y));
				}

			}

		}

		return list;

	}
	

}
