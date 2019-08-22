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
import pl.ncdc.billiard.models.Informations;
import pl.ncdc.billiard.models.Pocket;

@Service
public class HitService {

	@Autowired
	MathService mathService;
	
	double diameter = 20; // do zmiany


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
		if (mathService.findAngle(white, pointTarget, pocket) < rightAngle || findCollision(pocket, pointTarget, list) == false
				|| findCollisionSecond(white, pointTarget, list) == false) {

			listPoints.add(find(pointTarget, white, pocket, idPocket + 1));
		}
		return listPoints;
	}

	/**
	 *
	 * @param pocketPoint  Wsp��dne �uzy
	 * @param selectedBall Wsp��dne zaznaczonej bili
	 * @param index        Index
	 * @param listBall     Lista wszystkich bili na stole
	 * @return Zwraca TRUE je�li na drodz� wyznaczonej bili do �uzy NIE STOI
	 *         inna bila
	 */
	public boolean findCollision(Point pocket, Point target, List<Ball> listBall) {

		for (Ball ball : listBall) {
			if (ball.getPoint() != target) {
				double angle = mathService.findAngleOfCollision(target, ball.getPoint(), pocket);
				angle *= 57;
				if (angle < 184 && angle > 176)
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
				double angle = mathService.findAngleOfCollision(white, ball.getPoint(), target);
				angle *= 57;
				if (angle < 184 && angle > 176)
					return false;
			}
		}

		return true;
	}

	/**
	 *
	 * @param listPocket Lista z pozycjami ka�dej �uzy
	 * @param listBall   Lista wszystkich bill na stole
	 * @return Zwracana jest HashMapa gdzie kluczem jest �rodek bili VIRTUALNEJ, a
	 *         warto�ci� jest pozycja �uzy. Zwr�cone bile nie koliduj� z
	 *         innymi bilami na stole(Na drodze bili do �uzy nie stoi �adna inna
	 *         bila).
	 */
	public List<NewPoint> allPossibleHits(List<Pocket> listPocket, List<Ball> listBall, Ball white, int idPocket) {
		List<NewPoint> list = new ArrayList<NewPoint>();

		Point target = new Point();
		Point band = new Point();
		for (Ball ball : listBall)
			for (Pocket pocket : listPocket) {
				target = findHittingPoint(white.getPoint(), ball.getPoint(), pocket.getPoint(), listBall, idPocket)
						.get(0);
				band = findHittingPoint(white.getPoint(), ball.getPoint(), pocket.getPoint(), listBall, idPocket)
						.get(1);

				if (band == null) {
					list.add(new NewPoint(target, pocket.getPoint(), null));
				} else {
					list.add(new NewPoint(target, pocket.getPoint(), band));
				}
			}

		return list;
	}

	/**
	 *
	 * @param firstBall   Pierwsza bila np Najcz�ciej bia�a
	 * @param secondBall  Bila Virtualna(wyliczona wcze�niej oznaczaj�ca miejsce
	 *                    w kt�rym ma si� znale�� bia�a bila je�li
	 *                    chcemy trafi� zaznaczon� bil� do �uzy)
	 * @param bandAxisX   W zale�no�ci od sytuacji oznacza band� na osi X,
	 *                    mo�e przyj�� warto�� 0 lub 1000
	 * @param bandAxisY   W zale�no�ci od sytuacji oznacza band� na osi Y,
	 *                    mo�e przyj�� warto�� 0 lub 600
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

			List<Double> abList = mathService.abOfFunction(whiteBallNew.x, whiteBallNew.y, targetBallNew.x, targetBallNew.y);
			a = abList.get(0);
			b = abList.get(1);
			result.y = bandPos;

			result.x = (int) ((result.y / a) - (b / a));

		} else if (idBand == 2) {

			whiteBallNew.x = bandPos + (bandPos - whiteBallNew.x);

			a = mathService.abOfFunction(whiteBallNew.x, whiteBallNew.y, targetBallNew.x, targetBallNew.y).get(0);
			b = mathService.abOfFunction(whiteBallNew.x, whiteBallNew.y, targetBallNew.x, targetBallNew.y).get(1);
			result.x = bandPos;
			result.y = (int) (a * result.x) + (int) (b);

		} else if (idBand == 3) {

			whiteBallNew.y = -whiteBallNew.y;
			a = mathService.abOfFunction(whiteBallNew.x, whiteBallNew.y, targetBallNew.x, targetBallNew.y).get(0);
			b = mathService.abOfFunction(whiteBallNew.x, whiteBallNew.y, targetBallNew.x, targetBallNew.y).get(1);
			result.y = bandPos;
			result.x = (int) ((result.y / a) - (b / a));

		} else if (idBand == 4) {

			whiteBallNew.x = -whiteBallNew.x;

			List<Double> abList = mathService.abOfFunction(whiteBallNew.x, whiteBallNew.y, targetBallNew.x, targetBallNew.y);
			a = abList.get(0);
			b = abList.get(1);

			result.x = bandPos;

			result.y = (int) (a * result.x) + (int) (b);

		}

		return result;
	}

	/**
	 * Metoda znajduje dwa mozliwe do wykonania odbicia od bandy, po czym wybiera
	 * bardziej optymalne
	 * 
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

		angleFirstBandTarget = mathService.findAngle(white, firstPoint, target);
		angleFirstBandPocket = mathService.findAngle(white, firstPoint, pocket);
		angleFirstBandDifference = Math.abs(angleFirstBandTarget - angleFirstBandPocket);

		angleSecondBandTarget = mathService.findAngle(white, secondPoint, target);
		angleSecondBandPocket = mathService.findAngle(white, secondPoint, pocket);
		angleSecondBandDifference = Math.abs(angleSecondBandTarget - angleSecondBandPocket);

		if (angleFirstBandDifference < angleSecondBandDifference) {
			return firstPoint;
		} else {
			return secondPoint;
		}

	}

	public Informations getHitInfo(Point white, Point selected, Point pocket, List<Ball> list, int idPocket) {
		Informations hitInfo = new Informations();
		List<Point> hitPoints = findHittingPoint(white, selected, pocket, list, idPocket);
		double hitAngle, distanceWhiteSelected, distanceWhitePocket, totalDistance, distanceDifficultyBoundary = 50; // zmienic
																														// granice
		int difficultyLevel = 2;
		double unitConverter = 10; // pixel na cm
		;

		if (hitPoints == null) {
			return null;
		}

		else if (hitPoints.get(1) == null) {

			hitAngle = mathService.findAngle(white, hitPoints.get(0), pocket) * 57;

			distanceWhiteSelected = mathService.findDistance(white, selected); // tu jeszcze zamiana na cm
			distanceWhitePocket = mathService.findDistance(white, pocket);
			totalDistance = distanceWhitePocket + distanceWhiteSelected;

			if (hitAngle > 140 && totalDistance < distanceDifficultyBoundary) {
				difficultyLevel = 1;
			} else if (hitAngle < 140) {
				difficultyLevel = 2;

			}

			hitInfo.setHitAngle(hitAngle);
			hitInfo.setDistanceWhiteSelected(distanceWhiteSelected);
			hitInfo.setDistanceWhitePocket(distanceWhitePocket);
			hitInfo.setDifficultyLevel(difficultyLevel);
		}

		else {

			Point bandPoint = new Point();
			bandPoint = hitPoints.get(1);
			hitAngle = mathService.findAngle(white, bandPoint, selected) * 57;
			distanceWhiteSelected = mathService.findDistance(white, bandPoint) + mathService.findDistance(bandPoint, selected);
			distanceWhitePocket = mathService.findDistance(white, bandPoint) + mathService.findDistance(bandPoint, pocket);
			totalDistance = distanceWhitePocket + distanceWhiteSelected;

			if (hitAngle > 140 && totalDistance < distanceDifficultyBoundary) {
				difficultyLevel = 2;
			} else if (hitAngle < 140) {
				difficultyLevel = 3;
			}

			hitInfo.setHitAngle(hitAngle);
			hitInfo.setDistanceWhiteSelected(distanceWhiteSelected);
			hitInfo.setDistanceWhitePocket(distanceWhitePocket);
			hitInfo.setDifficultyLevel(difficultyLevel);

		}

		// poziom trudnosci -> latwy -> proste uderzenie, latwy kat i dystans
		// ->sredni -> proste uderzenie, ciezki kat i dystans / uderzenie od bandy ale
		// latwy kat i dystans
		// -> trudny -> uderzenie od bandy, ciezki kat i dystans

		return hitInfo;

	}

	public int findBestPocket(Point white, Point selected, List<Pocket> listPocket, List<Ball> balls, int idPocket) {

		int idPocketBest = -1;
		double angle = 0;
		double distanceWhiteSelected, distanceWhitePocket, totalDistance, distanceDifficultyBoundary = 50, angleCompare; // zmienic
																															// granice

		// List<Point> listPoint = findHittingPoint(white, selected, listPocket, balls,
		// idPocket);
		for (int x = 0; x < listPocket.size(); x++) {
			Point pocketPoint = listPocket.get(x).getPoint();
			List<Point> listPoint = findHittingPoint(white, selected, pocketPoint, balls, idPocket);

			if (listPoint.get(1) == null) {
				Point targetPoint = listPoint.get(0);

				distanceWhiteSelected = mathService.findDistance(white, selected); // tu jeszcze zamiana na cm
				distanceWhitePocket = mathService.findDistance(white, pocketPoint);
				totalDistance = distanceWhitePocket + distanceWhiteSelected;
				angleCompare = mathService.findAngle(white, targetPoint, pocketPoint);
				if (angleCompare > angle) {
					angle = angleCompare;
//					if (findAngle) {
//
//					}
					idPocketBest = x;

				}
			}
		}

		return idPocketBest;
	}


	
}
