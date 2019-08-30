package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.BilliardTable;

@Service
public class MathService {
	
	@Autowired
	private BilliardTable table;

	public double findDistance(Point start, Point end) {

		double length = Math.sqrt((start.x - end.x) * (start.x - end.x) + (start.y - end.y) * (start.y - end.y));
		return length;

	}

	/**
	 *
	 * @param xBallWhite   Pozycja na osi X �rodka bia�ej bili
	 * @param yBallWhite   Pozycja na osi Y �rodka bia�ej bili
	 * @param xCenterPoint Pozycja na osi X �rodka VIRTUALNEJ bili stoj�cej na
	 *                     przed�u�eniu lini do �uzy
	 * @param yCenterPoint Pozycja na osi Y �rodka VIRTUALNEJ bili stoj�cej na
	 *                     przed�u�eniu lini do �uzy
	 * @param xPocket      Pozycja na osi X �uzy
	 * @param yPocket      Pozycja na osi Y �uzy
	 * @return Zwraca k�t mi�dzy bia�� bil�, bil� VIRTUALN� i
	 *         �uz�.
	 */
	public double findAngle(Point white, Point target, Point pocket) {

//		white = new Point(0,0);
//		pocket = new Point(0,500);
//		target = new Point(-26,250);

		double p0c = Math.sqrt(Math.pow(target.x - white.x, 2) + Math.pow(target.y - white.y, 2));
		double p1c = Math.sqrt(Math.pow(target.x - pocket.x, 2) + Math.pow(target.y - pocket.y, 2));
		double p0p1 = Math.sqrt(Math.pow(pocket.x - white.x, 2) + Math.pow(pocket.y - white.y, 2));

		// System.out.println(Math.acos((p1c * p1c + p0c * p0c - p0p1 * p0p1) / (2 * p1c
		// * p0c)));

		return Math.acos((p1c * p1c + p0c * p0c - p0p1 * p0p1) / (2 * p1c * p0c));
	}

	/**
	 *
	 * @param xBallSelected Pozycja na osi X �rodka wybranej bili
	 * @param yBallSelected Pozycja na osi Y �rodka wybranej bili
	 * @param xBallDisturb  Pozycja na osi X �rodka bili, kt�ra mo�na
	 *                      znale�� si� na lini mi�dzy bil� wybran�, a
	 *                      �uz�.
	 * @param yBallDisturb  Pozycja na osi Y �rodka bili, kt�ra mo�na
	 *                      znale�� si� na lini mi�dzy bil� wybran�, a
	 *                      �uz�.
	 * @param xPocket       Pozycja na osi X �uzy
	 * @param yPocket       Pozycja na osi Y �uzy
	 * @return Zwraca k�t mi�dzy wybran� bil�, bil� kt�ra znajduje si�
	 *         na drodz� do �uzy i �uz�.
	 */
	public double findAngleOfCollision(Point selected, Point disturb, Point pocket) {

		double p0c = Math.sqrt(Math.pow(disturb.x - selected.x, 2) + Math.pow(disturb.y - selected.y, 2));
		double p1c = Math.sqrt(Math.pow(disturb.x - pocket.x, 2) + Math.pow(disturb.y - pocket.y, 2));
		double p0p1 = Math.sqrt(Math.pow(pocket.x - selected.x, 2) + Math.pow(pocket.y - selected.y, 2));

		return Math.acos(((p1c * p1c + p0c * p0c - p0p1 * p0p1) / (2 * p1c * p0c)));

	}

	public List<Double> abOfFunction(double xBallWhite, double yBallWhite, double xBallSelected, double yBallSelected) {
		List<Double> listOfAB = new ArrayList<>();
		double a = (yBallSelected - yBallWhite) / (xBallSelected - xBallWhite);
		double b = yBallSelected - a * xBallSelected;

		listOfAB.add(a);
		listOfAB.add(b);
		return listOfAB;
	}

	/**
	 * Sort ball list by x position. Set its id's
	 * 
	 * @param list list to sort
	 * @author charlie
	 */
	public void sort(List<Ball> list) {
		list.sort((o1, o2) -> Double.compare(o1.getPoint().x, o2.getPoint().x));
		for (int i = 1; i < list.size(); i++)
			list.get(i).setId(i);
	}

	/**
	 * <p>
	 * Check if current point is inside of given circle
	 * <p>
	 * ( a - x )<sup>2</sup> + ( b - y )<sup>2</sup> <= r<sup>2<sup>
	 * 
	 * @param a current point x-position
	 * @param b current point y-position
	 * @param x circle center x-position
	 * @param y circle center y-position
	 * @param r circle radius
	 * @return true if current point is inside the circle
	 * @author charlie
	 */
	public boolean inCircle(int a, int b, int x, int y, int r) {
		if (Math.pow(a - x, 2) + Math.pow(b - y, 2) <= Math.pow(r, 2))
			return true;
		return false;
	}

	/**
	 * Check if current point is inside of given circle
	 * <p>
	 * ( a - x )<sup>2</sup> - ( b + y )<sup>2</sup> <= r<sup>2<sup>
	 * 
	 * @param a      current point x-position
	 * @param b      current point y-position
	 * @param center circle center
	 * @param r      circle radius
	 * @return true if current point is inside the circle
	 * @author charlie
	 * @see MathService#inCircle(int, int, int, int, int) MathService.inCircle
	 */
	public boolean inCircle(int a, int b, Point point, int r) {
		return inCircle(a, b, (int) point.x, (int) point.y, r);
	}

	/**
	 * <p>
	 * Check if current point is inside of given circle
	 * <p>
	 * ( a - x )<sup>2</sup> - ( b + y )<sup>2</sup> <= r<sup>2<sup>
	 * 
	 * @param point  current point position
	 * @param center circle center
	 * @param r      circle radius
	 * @return true if current point is inside the circle
	 * @author charlie
	 * @see MathService#inCircle(int, int, int, int, int) MathService.inCircle
	 */
	public boolean inCircle(Point point, Point center, int r) {
		return inCircle((int) point.x, (int) point.y, (int) center.x, (int) center.y, r);
	}

	/**
	 * Search in Ball collection and try to find correct match
	 * 
	 * @param point
	 * @return {@code Point} position of ball.</br>
	 *         Null if not found
	 * @author charlie
	 */
	public Ball findBallByPoint(List<Ball> list, Point point, int radius) {
		for (Ball ball : list) {
			if (isBallCloseToPoint(ball, point, radius))
				return ball;
		}
		return null;
	}

	/**
	 * Check if ball coordinates are close to given point coordinates.
	 * 
	 * @author charlie
	 */
	public boolean isBallCloseToBall(Ball b1, Ball b2, int radius) {
		return isPointCloseToPoint(b1.getPoint(), b2.getPoint(), radius);
	}

	public boolean isBallCloseToPoint(Ball ball, Point point, int radius) {
		return isPointCloseToPoint(ball.getPoint(), point, radius);
	}

	public boolean isPointCloseToPoint(Point p1, Point p2, int radius) {
		if (Math.abs(p1.x - p2.x) < radius && Math.abs(p1.y - p2.y) < radius)
			return true;
		return false;
	}
}
