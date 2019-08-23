package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.Ball;

@Service
public class MathService {

	public double diameter = 20; // do zmiany

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

		double p0c = Math.sqrt(Math.pow(target.x - white.x, 2) + Math.pow(target.y - white.y, 2));
		double p1c = Math.sqrt(Math.pow(target.x - pocket.x, 2) + Math.pow(target.y - pocket.y, 2));
		double p0p1 = Math.sqrt(Math.pow(pocket.x - white.x, 2) + Math.pow(pocket.y - white.y, 2));
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






}
