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
	 * @param xBallWhite Pozycja na osi X ï¿½rodka biaï¿½ej bili
	 * @param yBallWhite Pozycja na osi Y ï¿½rodka biaï¿½ej bili
	 * @param xCenterPoint Pozycja na osi X ï¿½rodka VIRTUALNEJ bili stojï¿½cej na przedï¿½uï¿½eniu lini do ï¿½uzy
	 * @param yCenterPoint Pozycja na osi Y ï¿½rodka VIRTUALNEJ bili stojï¿½cej na przedï¿½uï¿½eniu lini do ï¿½uzy
	 * @param xPocket Pozycja na osi X ï¿½uzy
	 * @param yPocket Pozycja na osi Y ï¿½uzy
	 * @return Zwraca kï¿½t miï¿½dzy biaï¿½ï¿½ bilï¿½, bilï¿½ VIRTUALNï¿½ i ï¿½uzï¿½.
	 */
	public double findAngle(Point white, Point target, Point pocket) {

		double p0c = Math.sqrt(Math.pow(target.x - white.x, 2) + Math.pow(target.y - white.y, 2));
		double p1c = Math.sqrt(Math.pow(target.x - pocket.x, 2) + Math.pow(target.y - pocket.y, 2));
		double p0p1 = Math.sqrt(Math.pow(pocket.x - white.x, 2) + Math.pow(pocket.y - white.y, 2));
		return Math.acos((p1c * p1c + p0c * p0c - p0p1 * p0p1) / (2 * p1c * p0c));
	}


	
	/**
	 *
	 * @param xBallSelected Pozycja na osi X ï¿½rodka wybranej bili
	 * @param yBallSelected Pozycja na osi Y ï¿½rodka wybranej bili
	 * @param xBallDisturb Pozycja na osi X ï¿½rodka bili, ktï¿½ra moï¿½na znaleï¿½ï¿½ siï¿½ na lini miï¿½dzy bilï¿½ wybranï¿½, a ï¿½uzï¿½.
	 * @param yBallDisturb Pozycja na osi Y ï¿½rodka bili, ktï¿½ra moï¿½na znaleï¿½ï¿½ siï¿½ na lini miï¿½dzy bilï¿½ wybranï¿½, a ï¿½uzï¿½.
	 * @param xPocket Pozycja na osi X ï¿½uzy
	 * @param yPocket Pozycja na osi Y ï¿½uzy
	 * @return Zwraca kï¿½t miï¿½dzy wybranï¿½ bilï¿½, bilï¿½ ktï¿½ra znajduje siï¿½ na drodzï¿½ do ï¿½uzy i ï¿½uzï¿½.
	 */
	public double findAngleOfCollision(Point selected, Point disturb, Point pocket) {

		double p0c = Math.sqrt(Math.pow(disturb.x - selected.x, 2) + Math.pow(disturb.y - selected.y, 2));
		double p1c = Math.sqrt(Math.pow(disturb.x - pocket.x, 2) + Math.pow(disturb.y - pocket.y, 2));
		double p0p1 = Math.sqrt(Math.pow(pocket.x - selected.x, 2) + Math.pow(pocket.y - selected.y, 2));

		return Math.acos(((p1c * p1c + p0c * p0c - p0p1 * p0p1) / (2 * p1c * p0c)));

	}


	
	/**
	 *
	 * @param xBallWhite Pozycja na osi X ï¿½rodka biaï¿½ej bili
	 * @param yBallWhite Pozycja na osi Y ï¿½rodka biaï¿½ej bili
	 * @param xBallSelected Pozycja na osi X ï¿½rodka wybranej bili
	 * @param yBallSelected Pozycja na osi Y ï¿½rodka wybranej bili
	 * @param xPocket Pozycja na osi X ï¿½uzy
	 * @param yPocket Pozycja na osi Y ï¿½uzy
	 * @return Zwraca ï¿½rodek bili VIRTUALNEJ(Bila wirtualna styka siï¿½ bilï¿½ wybranï¿½ i oznacza miejsce docelowe biaï¿½ej bili, jeï¿½li chcemy trafiï¿½ w ï¿½uzï¿½) jako Point,
	 * dodatokowo sprawdza czy kï¿½t stworzony przez biaï¿½ï¿½ bilï¿½, virtualnï¿½ i ï¿½uzï¿½ jest dozwolony.
	 * Jeï¿½li nie to zwraca NULL.
	 * Kï¿½t w tym wypadku okreï¿½lany jest w radianach.
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
	 * @param pocketPoint Wspï¿½ï¿½dne ï¿½uzy
	 * @param selectedBall Wspï¿½ï¿½dne zaznaczonej bili
	 * @param index	Index
	 * @param listBall	Lista wszystkich bili na stole
	 * @return Zwraca TRUE jeï¿½li na drodzï¿½ wyznaczonej bili do ï¿½uzy NIE STOI inna bila
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


	/**
	 *
	 * @param listPocket Lista z pozycjami kaï¿½dej ï¿½uzy
	 * @param listBall Lista wszystkich bill na stole
	 * @return Zwracana jest HashMapa gdzie kluczem jest ï¿½rodek bili VIRTUALNEJ, a wartoï¿½ciï¿½ jest pozycja ï¿½uzy.
	 * Zwrï¿½cone bile nie kolidujï¿½ z innymi bilami na stole(Na drodze bili do ï¿½uzy nie stoi ï¿½adna inna bila).
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
	 * @param firstBall   Pierwsza bila np Najczêœciej bia³a
	 * @param secondBall  Bila Virtualna(wyliczona wczeœniej oznaczaj¹ca miejsce w
	 *                    którym ma siê znaleŸæ bia³a bila jeœli chcemy trafiæ
	 *                    zaznaczon¹ bilê do ³uzy)
	 * @param bandAxisX   W zale¿noœci od sytuacji oznacza bandê na osi X, mo¿e
	 *                    przyj¹æ wartoœæ 0 lub 1000
	 * @param bandAxisY   W zale¿noœci od sytuacji oznacza bandê na osi Y, mo¿e
	 *                    przyj¹æ wartoœæ 0 lub 600
	 * @param currentAxis oznacza aktualn¹ oœ w zale¿noœci od kierunku
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
