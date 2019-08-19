package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.Pocket;

@Service
public class HitService {

	double diameter = 20;
	

	/**
	 * 
	 * @param white Pozycja bialek bili
	 * @param target Pozycja Virtualnej bili
	 * @param pocket Pozycja luzy
	 * @return Zwraca kat miedzy biala bila, bila VIRTUALNA i luza.
	 */
	public double findAngle(Point white, Point target, Point pocket) {

		double p0c = Math.sqrt(Math.pow(target.x - white.x, 2) + Math.pow(target.y - white.y, 2));
		double p1c = Math.sqrt(Math.pow(target.x - pocket.x, 2) + Math.pow(target.y - pocket.y, 2));
		double p0p1 = Math.sqrt(Math.pow(pocket.x - white.x, 2) + Math.pow(pocket.y - white.y, 2));
		return Math.acos((p1c * p1c + p0c * p0c - p0p1 * p0p1) / (2 * p1c * p0c));
	}

	/**
	 * 
	 * @param white Pozycja bialek bili
	 * @param target Pozycja Virtualnej bili
	 * @param pocket Pozycja luzy
	 * @return Zwraca kat miedzy biala bila, bila VIRTUALNA i luza.
	 */
	public double findAngleOfCollision(Point selected, Point disturb, Point pocket) {

		double p0c = Math.sqrt(Math.pow(disturb.x - selected.x, 2) + Math.pow(disturb.y - selected.y, 2));
		double p1c = Math.sqrt(Math.pow(disturb.x - pocket.x, 2) + Math.pow(disturb.y - pocket.y, 2));
		double p0p1 = Math.sqrt(Math.pow(pocket.x - selected.x, 2) + Math.pow(pocket.y - selected.y, 2));

		return Math.acos(((p1c * p1c + p0c * p0c - p0p1 * p0p1) / (2 * p1c * p0c)));
	}
	
	/**
	 * 
	 * @param white Pozycja bialej bli
	 * @param selected Pozycja zaznaczonej bili
	 * @param pocket Pozycja luzy
	 * @param list lista wszystkich bill
	 * @param idPocket id luzy
	 * @return Zwraca srodek bili VIRTUALNEJ(Bila wirtualna styka siê bila wybrana i oznacza miejsce docelowe bialej bili, jesli chcemy trafic w luze) jako Point,
	 * dodatokowo sprawdza czy kat stworzony przez bia³a bile, virtualna i ³uze jest dozwolony.
	 * Jesli nie to zwraca NULL.
	 * Kat w tym wypadku okreslany jest w radianach.
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
		if (findAngle(white, pointTarget, pocket) < rightAngle  || findCollision(pocket, pointTarget, list) == false || findCollisionSecond(white, pointTarget, list) == false) {
			
			listPoints.add(find(pointTarget, white, pocket, idPocket + 1));		
		}			
		return listPoints;
	}

	/**
	 *
	 * @param pocketPoint Wspolzedne luzy
	 * @param selectedBall Wspolzedne zaznaczonej bili
	 * @param index	Index
	 * @param listBall	Lista wszystkich bili na stole
	 * @return Zwraca TRUE jezeli na drodze½ wyznaczonej bili do luzy NIE STOI inna bila
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
	 * Znajduje kolizje
	 * @param white Pozycja bialej
	 * @param target Pozycja Virtualnej
	 * @param listBall Lista wszystkich bill
	 * @return false jesli znajdzie kolizje
	 */
	public boolean findCollisionSecond(Point white, Point target, List<Ball> listBall) {

		for (Ball ball : listBall) {
			if (ball.getPoint() != target) {
				double angle = findAngleOfCollision(white, ball.getPoint(), target);
				angle *= 57;
				if (angle < 190 && angle > 170)
					return false;
			}
		}

		return true;
	}
	
	/**
	 *
	 * @param listPocket Lista z pozycjami kazdej luzy
	 * @param listBall Lista wszystkich bill na stole
	 * @return Zwracana wszystkie mozliwe sciezki
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

	/**
	 * Zwraca liste w ktorej na pierwszym miejscu znajduje sie wspolczynnik a a na drugim wspolczynnik b
	 * @param xBallWhite
	 * @param yBallWhite
	 * @param xBallSelected
	 * @param yBallSelected
	 * @return Zwraca wspolczynniki funkcji y = ax + b dla dwoch punktów podanych w parametrze
	 */
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
	 * Zwraca punkt odbicia od bandy
	 * @param whiteBall Pozycja bia³ek bili
	 * @param targetBall Pozycja wirtualnej bili
	 * @param bandPos Pozycja odbicia od bandy
	 * @param idBand Id bandy
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
	 * Metoda znajduje dwa mozliwe do wykonania odbicia od bandy, po czym wybiera
	 * bardziej optymalne
	 * @param target
	 * @param white
	 * @param pocket
	 * @param idPocket
	 * @return Punkt odbicia od bandy
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
