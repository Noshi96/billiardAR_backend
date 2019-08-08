package pl.ncdc.billiard.service;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import pl.ncdc.billiard.entity.Ball;
import pl.ncdc.billiard.entity.Pocket;

@Service
public class HitService {

	double diameter = 2;

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

		point.setLocation(x, y);

		if (findAngle(xBallWhite, yBallWhite, x, y, xPocket, yPocket) < 1.57) {
			return null;
		}
		return point;

	}

	public boolean findCollision(Point pocketPoint, Point selectedBall, int index, List<Ball> listBall) {

		for (int x = 1; x < listBall.size(); x++) {
			if (index != x) {
				double angle = findAngleOfCollision(selectedBall.getX(), selectedBall.getY(),
						listBall.get(x).getPoint().getX(), listBall.get(x).getPoint().getY(), pocketPoint.getX(),
						pocketPoint.getY());
				angle *= 57;
				if (angle < 190 && angle > 160) {
					return false;
				}

			}

		}

		return true;

	}

	public HashMap<Point, Point> allPossibleHits(List<Pocket> listPocket, List<Ball> listBall) {
		HashMap<Point, Point> points = new HashMap<Point, Point>();

		for (int x = 1; x < listBall.size(); x++) { // pierwsza bila w liscie jest biala index 0
			for (int y = 0; y < listPocket.size(); y++) {

				Point targetPoint = findHittingPoint(listBall.get(0).getPoint().getX(),
						listBall.get(0).getPoint().getY(), listBall.get(x).getPoint().getX(),
						listBall.get(x).getPoint().getY(), listPocket.get(y).getPoint().getX(),
						listPocket.get(y).getPoint().getY());

				Point pocketPoint = new Point();
				pocketPoint.setLocation((double) listPocket.get(y).getPoint().getX(),
						(double) listPocket.get(y).getPoint().getY());
				Point selectedPoint = new Point();
				selectedPoint.setLocation((double) listBall.get(x).getPoint().getX(),
						(double) listBall.get(x).getPoint().getY());

				boolean collisionFound = findCollision(pocketPoint, selectedPoint, x, listBall);
				if (targetPoint != null && collisionFound) {

					points.put(targetPoint, pocketPoint);

				}

			}

		}

		if (points == null) {
			return null;
		}
		return points;

	}

}
