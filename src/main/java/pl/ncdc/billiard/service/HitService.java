package pl.ncdc.billiard.service;

import org.opencv.core.Point;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;

import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.Pocket;

@Service
public class HitService {

	double diameter = 20;
	

	/**
	 *
	 * @param xBallWhite Pozycja na osi X �rodka bia�ej bili
	 * @param yBallWhite Pozycja na osi Y �rodka bia�ej bili
	 * @param xCenterPoint Pozycja na osi X �rodka VIRTUALNEJ bili stoj�cej na przed�u�eniu lini do �uzy
	 * @param yCenterPoint Pozycja na osi Y �rodka VIRTUALNEJ bili stoj�cej na przed�u�eniu lini do �uzy
	 * @param xPocket Pozycja na osi X �uzy
	 * @param yPocket Pozycja na osi Y �uzy
	 * @return Zwraca k�t mi�dzy bia�� bil�, bil� VIRTUALN� i �uz�.
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
	 * @param xBallDisturb Pozycja na osi X �rodka bili, kt�ra mo�na znale�� si� na lini mi�dzy bil� wybran�, a �uz�.
	 * @param yBallDisturb Pozycja na osi Y �rodka bili, kt�ra mo�na znale�� si� na lini mi�dzy bil� wybran�, a �uz�.
	 * @param xPocket Pozycja na osi X �uzy
	 * @param yPocket Pozycja na osi Y �uzy
	 * @return Zwraca k�t mi�dzy wybran� bil�, bil� kt�ra znajduje si� na drodz� do �uzy i �uz�.
	 */
	public double findAngleOfCollision(Point selected, Point disturb, Point pocket) {

		double p0c = Math.sqrt(Math.pow(disturb.x - selected.x, 2) + Math.pow(disturb.y - selected.y, 2));
		double p1c = Math.sqrt(Math.pow(disturb.x - pocket.x, 2) + Math.pow(disturb.y - pocket.y, 2));
		double p0p1 = Math.sqrt(Math.pow(pocket.x - selected.x, 2) + Math.pow(pocket.y - selected.y, 2));

		return Math.acos(((p1c * p1c + p0c * p0c - p0p1 * p0p1) / (2 * p1c * p0c)));

	}


	
	/**
	 *
	 * @param xBallWhite Pozycja na osi X �rodka bia�ej bili
	 * @param yBallWhite Pozycja na osi Y �rodka bia�ej bili
	 * @param xBallSelected Pozycja na osi X �rodka wybranej bili
	 * @param yBallSelected Pozycja na osi Y �rodka wybranej bili
	 * @param xPocket Pozycja na osi X �uzy
	 * @param yPocket Pozycja na osi Y �uzy
	 * @return Zwraca �rodek bili VIRTUALNEJ(Bila wirtualna styka si� bil� wybran� i oznacza miejsce docelowe bia�ej bili, je�li chcemy trafi� w �uz�) jako Point,
	 * dodatokowo sprawdza czy k�t stworzony przez bia�� bil�, virtualn� i �uz� jest dozwolony.
	 * Je�li nie to zwraca NULL.
	 * K�t w tym wypadku okre�lany jest w radianach.
	 */
	public List<Point> findHittingPoint(Point white, Point selected, Point pocket, List<Ball> list, int idPocket) {

		Point pointTarget = new Point();
		List<Point> listPoints = new ArrayList<Point>();

		double length = Math.sqrt(
				(selected.x - pocket.x) * (selected.x - pocket.x) + (selected.y - pocket.y) * (selected.y - pocket.y));

		double dx = (selected.x - pocket.x) / length;
		double dy = (selected.y - pocket.y) / length;

		double x = pocket.x + ((diameter + length) * dx);
		double y = pocket.y + ((diameter + length) * dy);

		pointTarget.x = x;
		pointTarget.y = y;
		double rightAngle = 1.57;
		
		listPoints.add(pointTarget);
		if (findAngle(white, pointTarget, pocket) < rightAngle  || findCollision(pocket, pointTarget, list) || findCollisionSecond(white, pointTarget, list)) {
			
			listPoints.add(find(pointTarget, white, pocket, idPocket));		
		}			
		return listPoints;
	}



	
	/**
	 *
	 * @param pocketPoint Wsp��dne �uzy
	 * @param selectedBall Wsp��dne zaznaczonej bili
	 * @param index	Index
	 * @param listBall	Lista wszystkich bili na stole
	 * @return Zwraca TRUE je�li na drodz� wyznaczonej bili do �uzy NIE STOI inna bila
	 */
	public boolean findCollision(Point pocket, Point target, List<Ball> listBall) {

		for (Ball ball : listBall) {
			if (ball.getPoint() != target) {
				double angle = findAngleOfCollision(target, ball.getPoint(), pocket);
				angle *= 57;
				if (angle < 190 && angle > 160)
					return false;
			}
		}

		return true;
	}
	
	/**
	 * 
	 * @param white
	 * @param target
	 * @param listBall
	 * @return
	 */
	public boolean findCollisionSecond(Point white, Point target, List<Ball> listBall) {

		for (Ball ball : listBall) {
			if (ball.getPoint() != target) {
				double angle = findAngleOfCollision(white, ball.getPoint(), target);
				angle *= 57;
				if (angle < 190 && angle > 10)
					return false;
			}
		}

		return true;

	}
	
	
	public boolean findCollisionSecond(Point white, Point target, List<Ball> listBall) {

		for (Ball ball : listBall) {
			if (ball.getPoint() != target) {
				double angle = findAngleOfCollision(white, ball.getPoint(), target);
				angle *= 57;
				if (angle < 190 && angle > 10)
					return false;
			}
		}

		return true;

	}



	/**
	 *
	 * @param listPocket Lista z pozycjami ka�dej �uzy
	 * @param listBall Lista wszystkich bill na stole
	 * @return Zwracana jest HashMapa gdzie kluczem jest �rodek bili VIRTUALNEJ, a warto�ci� jest pozycja �uzy.
	 * Zwr�cone bile nie koliduj� z innymi bilami na stole(Na drodze bili do �uzy nie stoi �adna inna bila).
	 */
	public List<NewPoint> allPossibleHits(List<Pocket> listPocket, List<Ball> listBall, Ball white, int idPocket) {
		List<NewPoint> list = new ArrayList<NewPoint>();
		
		Point target = new Point();
		Point band = new Point();
		for (Ball ball : listBall)
			for (Pocket pocket : listPocket) {
				target = findHittingPoint(white.getPoint(), ball.getPoint(), pocket.getPoint(), listBall, idPocket).get(0);
				band = findHittingPoint(white.getPoint(), ball.getPoint(), pocket.getPoint(), listBall, idPocket).get(1);
				
				if (band == null) {
					list.add(new NewPoint(target, pocket.getPoint(), null));
				}
				else {
					list.add(new NewPoint(target, pocket.getPoint(), band));
				}
			}

		return list;
	}

	
	public List<Double> abOfFunction(double xBallWhite, double yBallWhite, double xBallSelected,
			double yBallSelected) {
		List<Double> listOfAB = new ArrayList<>();
		double a = (yBallSelected - yBallWhite) / (xBallSelected - xBallWhite);
		double b = yBallSelected - a * xBallSelected;
		

		listOfAB.add(a);
		listOfAB.add(b);
		return listOfAB;
	}

	/**
	 *
	 * @param firstBall   Pierwsza bila np Najcz�ciej bia�a
	 * @param secondBall  Bila Virtualna(wyliczona wcze�niej oznaczaj�ca miejsce w
	 *                    kt�rym ma si� znale�� bia�a bila je�li chcemy trafi�
	 *                    zaznaczon� bil� do �uzy)
	 * @param bandAxisX   W zale�no�ci od sytuacji oznacza band� na osi X, mo�e
	 *                    przyj�� warto�� 0 lub 1000
	 * @param bandAxisY   W zale�no�ci od sytuacji oznacza band� na osi Y, mo�e
	 *                    przyj�� warto�� 0 lub 600
	 * @param currentAxis oznacza aktualn� o� w zale�no�ci od kierunku
	 * @return
	 */
	public Point bandHitingPoint(Point whiteBall, Point targetBall, int bandPos, int idBand) {

		double a;
		double b;
		Point result = new Point();

		Point whiteBallNew = new Point(whiteBall.x, whiteBall.y);
		Point targetBallNew = new Point(targetBall.x, targetBall.y);

		if (idBand == 1) {

			whiteBallNew.y = bandPos + (bandPos - whiteBallNew.y);

			List<Double> abList = abOfFunction(whiteBallNew.x, whiteBallNew.y, targetBallNew.x, targetBallNew.y);
			a = abList.get(0);
			b = abList.get(1);
			result.y = bandPos;

			result.x = (int) ((result.y / a) - (b / a));

		} else if (idBand == 2) {

			whiteBallNew.x = bandPos + (bandPos - whiteBallNew.x);

			a = abOfFunction(whiteBallNew.x, whiteBallNew.y, targetBallNew.x, targetBallNew.y).get(0);
			b = abOfFunction(whiteBallNew.x, whiteBallNew.y, targetBallNew.x, targetBallNew.y).get(1);
			result.x = bandPos;
			result.y = (int) (a * result.x) + (int) (b);

		} else if (idBand == 3) {

			whiteBallNew.y = -whiteBallNew.y;
			a = abOfFunction(whiteBallNew.x, whiteBallNew.y, targetBallNew.x, targetBallNew.y).get(0);
			b = abOfFunction(whiteBallNew.x, whiteBallNew.y, targetBallNew.x, targetBallNew.y).get(1);
			result.y = bandPos;
			result.x = (int) ((result.y / a) - (b / a));

		} else if (idBand == 4) {

			whiteBallNew.x = -whiteBallNew.x;

			List<Double> abList = abOfFunction(whiteBallNew.x, whiteBallNew.y, targetBallNew.x, targetBallNew.y);
			a = abList.get(0);
			b = abList.get(1);

			result.x = bandPos;

			result.y = (int) (a * result.x) + (int) (b);

		}

		return result;
	}

	
	/**
	 * 
	 * @param target
	 * @param white
	 * @param pocket
	 * @param idPocket
	 * @return
	 */
	public Point find(Point target, Point white, Point pocket, int idPocket) {

		int leftBand = 0;
		int rightBand = 1190;
		int upperBand = 620;
		int lowerBand = 0;
		int idBand;
		// 1-gora, 2-prawa, 3-dol, 4-lewy

		Point firstPoint = new Point();
		Point secondPoint = new Point();

		if (idPocket == 1) {
			// prawa dol

			firstPoint = bandHitingPoint(white, target, lowerBand, 3);
			secondPoint = bandHitingPoint(white, target, rightBand, 2);
		} else if (idPocket == 2) {
			// zalezy
			// prawa str
			if (target.x > pocket.x) {
				firstPoint = bandHitingPoint(white, target, lowerBand, 3);
				secondPoint = bandHitingPoint(white, target, rightBand, 2);

			}

			// lewa str
			else {
				firstPoint = bandHitingPoint(white, target, lowerBand, 3);
				secondPoint = bandHitingPoint(white, target, leftBand, 4);

			}

		} else if (idPocket == 3) {
			// lewa, dol
			firstPoint = bandHitingPoint(white, target, leftBand, 4);
			secondPoint = bandHitingPoint(white, target, lowerBand, 3);

		} else if (idPocket == 4) {
			// lewa, gora
			firstPoint = bandHitingPoint(white, target, leftBand, 4);
			secondPoint = bandHitingPoint(white, target, upperBand, 1);

		} else if (idPocket == 5) {
			// zalezy

			// prawa str
			if (target.x > pocket.x) {
				firstPoint = bandHitingPoint(white, target, upperBand, 1);
				secondPoint = bandHitingPoint(white, target, rightBand, 2);

			}

			// lewa str
			else {
				firstPoint = bandHitingPoint(white, target, upperBand, 1);
				secondPoint = bandHitingPoint(white, target, leftBand, 4);

			}

		} else if (idPocket == 6) {
			// prawa, gora
			firstPoint = bandHitingPoint(white, target, rightBand, 2);
			secondPoint = bandHitingPoint(white, target, upperBand, 1);

		}

		double angleFirstBandTarget;
		double angleFirstBandPocket;
		double angleSecondBandTarget;
		double angleSecondBandPocket;

		double angleFirstBandDifference;
		double angleSecondBandDifference;

		angleFirstBandTarget = findAngle(white, firstPoint, target);
		angleFirstBandPocket = findAngle(white, firstPoint, pocket);
		angleFirstBandDifference = Math.abs(angleFirstBandTarget - angleFirstBandPocket);

		angleSecondBandTarget = findAngle(white, secondPoint, target);
		angleSecondBandPocket = findAngle(white, secondPoint, pocket);
		angleSecondBandDifference = Math.abs(angleSecondBandTarget - angleSecondBandPocket);

		if (angleFirstBandDifference < angleSecondBandDifference) {
			return firstPoint;
		} else {
			return secondPoint;
		}

	}


}
