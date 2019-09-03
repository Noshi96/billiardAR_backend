package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.Informations;
import pl.ncdc.billiard.models.NewPoint;
import pl.ncdc.billiard.models.Pocket;

@Service
public class HitService {

	@Autowired
	private BilliardTable table;

	@Autowired
	MathService mathService;

	/**
	 *
	 * @param white  Pozycja bialej bili
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
	 * @param white  Pozycja bialek bili
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
	 * @param white    Pozycja bialej bli
	 * @param selected Pozycja zaznaczonej bili
	 * @param pocket   Pozycja luzy
	 * @param list     lista wszystkich bill
	 * @param idPocket id luzy
	 * @return Zwraca srodek bili VIRTUALNEJ(Bila wirtualna styka sie z bila wybrana
	 *         i oznacza miejsce docelowe bialej bili, jesli chcemy trafic w luze)
	 *         jako Point, dodatokowo sprawdza czy kat stworzony przez biala bile,
	 *         virtualna i luze jest dozwolony. Jesli nie to zwraca NULL. Kat w tym
	 *         wypadku okreslany jest w radianach. W przypadku gdy kat jest mniejszy
	 *         od 90 stopni szukany jest punkt zderzenia z banda (2 element w
	 *         liscie)
	 */
	public List<Point> findHittingPoint(Point white, Point selected, Point pocket, List<Ball> list, int idPocket) {

		if (selected != null) {
			Point pointTarget = new Point();
			List<Point> listPoints = new ArrayList<Point>();
			Point bandPoint = new Point();
			double length = Math.sqrt((selected.x - pocket.x) * (selected.x - pocket.x)
					+ (selected.y - pocket.y) * (selected.y - pocket.y));

			double dx = (selected.x - pocket.x) / length;
			double dy = (selected.y - pocket.y) / length;

			double x = pocket.x + ((table.getBallRadius() * 2 + length) * dx);
			double y = pocket.y + ((table.getBallRadius() * 2 + length) * dy);

			pointTarget.x = x;
			pointTarget.y = y;
			double rightAngle = 1.57;

			double angle = findAngle(white, pointTarget, pocket);

			listPoints.add(pointTarget);
		
			if (findAngle(white, pointTarget, pocket) < rightAngle || isCollision(white, pointTarget, list, selected) || isCollision(pointTarget, pocket, list, selected)) {
				
				bandPoint = find(pointTarget, white, pocket, idPocket + 1);

				if (isCollision(white, bandPoint, list, selected) == false
						&& isCollision(bandPoint, pointTarget, list, selected) == false
						&& isCollision(pointTarget, pocket, list, selected) == false) {
					if (findAngle(pocket, pointTarget, bandPoint) * 57 < 92) {
						return null;
					}

					listPoints.add(bandPoint);
				} else {
					return null;
				} 
			}

			return listPoints;
		} else {
			return null;
		}
	}

	/**
	 *
	 * @param pocketPoint  Wspolzedne luzy
	 * @param selectedBall Wspolzedne zaznaczonej bili
	 * @param index        Index
	 * @param listBall     Lista wszystkich bili na stole
	 * @return Zwraca TRUE jezeli na drodze wyznaczonej bili do luzy NIE STOI inna
	 *         bila
	 */

	public boolean isPointInRange(Point point, Point point2, double tolerance) {
		if (Math.abs(point2.x - point.x) < tolerance && Math.abs(point2.y - point.y) < tolerance) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Znajduje kolizje
	 * 
	 * @param white    Pozycja bialej
	 * @param target   Pozycja Virtualnej
	 * @param listBall Lista wszystkich bill
	 * @return false jesli znajdzie kolizje
	 */
	
	public boolean isCollision(Point white, Point target, List<Ball> listBall, Point selected) {

		List<Double> listt = mathService.abOfFunction(white.x, white.y, target.x, target.y);

		double a = listt.get(0);
		double b = listt.get(1);
		double aBall = -1 / a, bBall, angleBall, angleBoundary, pointPosShiftVar;
		Point crossPoint = new Point();
		Point pointPosShift = new Point();

		for (Ball ball : listBall) {
			if (!isPointInRange(ball.getPoint(), selected, table.getBallRadius())) {
				angleBall = findAngle(white, ball.getPoint(), target) * 57; //kat z bila potencjalnie przeszkadzajaca
				
				bBall = ball.getPoint().y - (ball.getPoint().x * aBall);
				
				crossPoint.x = (b - bBall)/(aBall - a);
				crossPoint.y = (aBall * b - bBall * a) / (aBall - a);
				
				pointPosShiftVar = crossPoint.x + table.getBallRadius() * 2 * Math.sqrt((1 / (1 + Math.pow(aBall, 2))));
				pointPosShift.x = pointPosShiftVar;
				pointPosShift.y = aBall * pointPosShiftVar + bBall;
				
				angleBoundary = findAngle(white, pointPosShift, target) * 57;
				
				if(angleBoundary >= 90) {
					if(angleBall >= angleBoundary) {
						// jest kolizja
						return true;
					} 
				}			
			}
		}
		return false;
	}

	/**
	 *
	 * @param listPocket Lista z pozycjami kazdej luzy
	 * @param listBall   Lista wszystkich bill na stole
	 * @return Zwracana wszystkie mozliwe sciezki
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
	 * Zwraca liste w ktorej na pierwszym miejscu znajduje sie wspolczynnik a, a na
	 * drugim wspolczynnik b
	 * 
	 * @param xBallWhite
	 * @param yBallWhite
	 * @param xBallSelected
	 * @param yBallSelected
	 * @return Zwraca wspolczynniki funkcji y = ax + b dla dwoch punktow podanych w
	 *         parametrze
	 */
	public List<Double> abOfFunction(double xBallWhite, double yBallWhite, double xBallSelected, double yBallSelected) {
		List<Double> listOfAB = new ArrayList<>();
		double a = (yBallSelected - yBallWhite) / (xBallSelected - xBallWhite);
		double b = yBallSelected - a * xBallSelected;

		listOfAB.add(a);
		listOfAB.add(b);
		return listOfAB;
	}

	/**
	 * Zwraca punkt odbicia od bandy
	 * 
	 * @param whiteBall  Pozycja bialej bili
	 * @param targetBall Pozycja wirtualnej bili
	 * @param bandPos    Pozycja odbicia od bandy
	 * @param idBand     Id bandy
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

			List<Double> abList = mathService.abOfFunction(whiteBallNew.x, whiteBallNew.y, targetBallNew.x,
					targetBallNew.y);
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

			List<Double> abList = mathService.abOfFunction(whiteBallNew.x, whiteBallNew.y, targetBallNew.x,
					targetBallNew.y);
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
	 * @param target
	 * @param white
	 * @param pocket
	 * @param idPocket
	 * @return Punkt odbicia od bandy
	 */
	public Point find(Point target, Point white, Point pocket, int idPocket) {

		int leftBand = 0;
		int rightBand = this.table.getWidth();
		int upperBand = 0;
		int lowerBand = this.table.getHeight();

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
				firstPoint = bandHitingPoint(white, target, lowerBand, 1);
				secondPoint = bandHitingPoint(white, target, rightBand, 2);
			}
			// lewa str
			else {
				firstPoint = bandHitingPoint(white, target, lowerBand, 1);
				secondPoint = bandHitingPoint(white, target, leftBand, 4);
			}
		} else if (idPocket == 3) {
			// lewa, dol
			firstPoint = bandHitingPoint(white, target, leftBand, 4);
			secondPoint = bandHitingPoint(white, target, lowerBand, 1);

		} else if (idPocket == 4) {
			// lewa, gora
			firstPoint = bandHitingPoint(white, target, leftBand, 4);
			secondPoint = bandHitingPoint(white, target, upperBand, 3);

		} else if (idPocket == 5) {
			// zalezy
			// prawa str
			if (target.x > pocket.x) {
				firstPoint = bandHitingPoint(white, target, upperBand, 3);
				secondPoint = bandHitingPoint(white, target, rightBand, 2);
			}

			// lewa str
			else {
				firstPoint = bandHitingPoint(white, target, upperBand, 3);
				secondPoint = bandHitingPoint(white, target, leftBand, 4);
			}

		} else if (idPocket == 6) {
			// prawa, gora
			firstPoint = bandHitingPoint(white, target, rightBand, 2);
			secondPoint = bandHitingPoint(white, target, upperBand, 1);
		}

		double angleFirstBandPocket;
		double angleSecondBandPocket;

		angleFirstBandPocket = mathService.findAngle(firstPoint, target, pocket);
		angleSecondBandPocket = mathService.findAngle(secondPoint, target, pocket);

		if (angleFirstBandPocket > angleSecondBandPocket) {
			return firstPoint;
		} else {
			return secondPoint;
		}
	}

	/**
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public double findDistance(Point start, Point end) {

		double length = Math.sqrt((start.x - end.x) * (start.x - end.x) + (start.y - end.y) * (start.y - end.y));
		return length;

	}

	/**
	 *
	 * @param white
	 * @param selected
	 * @param pocket
	 * @param list
	 * @param idPocket
	 * @return
	 */
	public Informations getHitInfo(Point white, Point selected, Point pocket, List<Ball> list, int idPocket) {
		Informations hitInfo = new Informations();
		List<Point> hitPoints = findHittingPoint(white, selected, pocket, list, idPocket);
		double hitAngle, distanceWhiteSelected, distanceWhitePocket;
		int difficultyLevel = 2;
		double unitConverter = 10; // pixel na cm

		if (hitPoints == null) {
			// failure
		}
		else if (hitPoints.size() == 1) {

			hitAngle = findAngle(white, hitPoints.get(0), pocket) * 57;

			distanceWhiteSelected = findDistance(white, selected); // tu jeszcze zamiana na cm
			distanceWhitePocket = findDistance(white, pocket);

			if (hitAngle > 140) {
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
			hitAngle = findAngle(white, bandPoint, selected) * 57;
			distanceWhiteSelected = findDistance(white, bandPoint) + findDistance(bandPoint, selected);
			distanceWhitePocket = findDistance(white, bandPoint) + findDistance(bandPoint, pocket);

			if (hitAngle > 140) {
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

	/**
	 *
	 * @param white
	 * @param selected
	 * @param listPocket
	 * @param balls
	 * @param idPocket
	 * @return
	 */
	public int findBestPocket(Point white, Point selected, List<Pocket> listPocket, List<Ball> balls) {

		int idPocketBest = -1;
		double angle = 0, angleCompare;
		// List<Point> listPoint = findHittingPoint(white, selected, listPocket, balls,
		// idPocket);
		for (int x = 0; x < listPocket.size(); x++) {
			Point pocketPoint = listPocket.get(x).getPoint();
			List<Point> listPoint = findHittingPoint(white, selected, pocketPoint, balls, listPocket.get(x).getId());

			if (listPoint == null) {
				// System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOoo");
			} else {
				if (listPoint.size() == 1) {
					Point targetPoint = listPoint.get(0);
					angleCompare = findAngle(white, targetPoint, pocketPoint);

					if (angleCompare > angle) {
						angle = angleCompare;
						idPocketBest = x;
					}
				}
			}
		}

		return idPocketBest;
	}

	/**
	 *
	 * @param white
	 * @param listBall
	 * @return
	 */
	public List<Point> showHiddenPlaces(Point white, List<Ball> listBall) {
		List<Point> hiddenPointsList = new ArrayList<Point>();

		Point hiddenPoint = new Point();

		for (int i = 0; i < listBall.size(); i++) {

			Point ballPoint = listBall.get(i).getPoint();

			double length = findDistance(white, ballPoint);
			double dx = (white.x - ballPoint.x) / length;
			double dy = (white.y - ballPoint.y) / length;

			double x = ballPoint.x + ((table.getBallRadius() * 2 + length) * dx);
			double y = ballPoint.y + ((table.getBallRadius() * 2 + length) * dy);

			hiddenPoint.x = x;
			hiddenPoint.y = y;

			hiddenPointsList.add(hiddenPoint);

		}
		return hiddenPointsList;

	}
}
