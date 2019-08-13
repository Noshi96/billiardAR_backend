package pl.ncdc.billiard.service;

import org.opencv.core.Point;
import java.util.ArrayList;

import java.util.List;

import org.springframework.stereotype.Service;

import pl.ncdc.billiard.entity.Ball;
import pl.ncdc.billiard.entity.Pocket;

@Service
public class HitService {

	double diameter = 20;

	public double findAngle(Point white, Point target, Point pocket) {

		double p0c = Math.sqrt(Math.pow(target.x - white.x, 2) + Math.pow(target.y - white.y, 2));
		double p1c = Math.sqrt(Math.pow(target.x - pocket.x, 2) + Math.pow(target.y - pocket.y, 2));
		double p0p1 = Math.sqrt(Math.pow(pocket.x - white.x, 2) + Math.pow(pocket.y - white.y, 2));
		return Math.acos((p1c * p1c + p0c * p0c - p0p1 * p0p1) / (2 * p1c * p0c));
	}

	public double findAngleOfCollision(Point selected, Point disturb, Point pocket) {

		double p0c = Math.sqrt(Math.pow(disturb.x - selected.x, 2) + Math.pow(disturb.y - selected.y, 2));
		double p1c = Math.sqrt(Math.pow(disturb.x - pocket.x, 2) + Math.pow(disturb.y - pocket.y, 2));
		double p0p1 = Math.sqrt(Math.pow(pocket.x - selected.x, 2) + Math.pow(pocket.y - selected.y, 2));

		return Math.acos(((p1c * p1c + p0c * p0c - p0p1 * p0p1) / (2 * p1c * p0c)));

	}

	public Point findHittingPoint(Point white, Point selected, Point pocket) {

		Point point = new Point();

		double length = Math.sqrt(
				(selected.x - pocket.x) * (selected.x - pocket.x) + (selected.y - pocket.y) * (selected.y - pocket.y));

		double dx = (selected.x - pocket.x) / length;
		double dy = (selected.y - pocket.y) / length;

		double x = pocket.x + ((diameter + length) * dx);
		double y = pocket.y + ((diameter + length) * dy);

		point.x = x;
		point.y = y;

		if (findAngle(white, point, pocket) < 1.57)
			return null;

		return point;

	}

	public boolean findCollision(Pocket pocket, Ball target, List<Ball> listBall) {

		for (Ball ball : listBall) {
			if (ball != target) {
				double angle = findAngleOfCollision(target.getPoint(), ball.getPoint(), pocket.getPoint());
				angle *= 57;
				if (angle < 190 && angle > 160)
					return false;
			}
		}

		return true;

	}

	public List<NewPoint> allPossibleHits(List<Pocket> listPocket, List<Ball> listBall, Ball white) {

		List<NewPoint> list = new ArrayList<NewPoint>();

		for (Ball ball : listBall)
			for (Pocket pocket : listPocket) {
				Point target = findHittingPoint(white.getPoint(), ball.getPoint(), pocket.getPoint());
				if (target != null && findCollision(pocket, ball, listBall)) {
					list.add(new NewPoint(target, pocket.getPoint()));
				}
			}

		return list;
	}

}
