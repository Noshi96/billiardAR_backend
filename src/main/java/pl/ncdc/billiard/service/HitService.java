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
import pl.ncdc.billiard.IndividualTraining;

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
	public double findAngle(double xBallWhite, double yBallWhite, double xCenterPoint, double yCenterPoint,
			double xPocket, double yPocket) {
		double p0c = Math.sqrt(Math.pow(xCenterPoint - xBallWhite, 2) + Math.pow(yCenterPoint - yBallWhite, 2));

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
	public double findAngleOfCollision(double xBallSelected, double yBallSelected, double xBallDisturb,
			double yBallDisturb, double xPocket, double yPocket) {

		double p0c = Math.sqrt(Math.pow(xBallDisturb - xBallSelected, 2) + Math.pow(yBallDisturb - yBallSelected, 2));

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
	public Point findHittingPoint(double xBallWhite, double yBallWhite, double xBallSelected, double yBallSelected,
			double xPocket, double yPocket) {

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

	
	/**
	 *
	 * @param pocketPoint Wsp��dne �uzy
	 * @param selectedBall Wsp��dne zaznaczonej bili
	 * @param index	Index
	 * @param listBall	Lista wszystkich bili na stole
	 * @return Zwraca TRUE je�li na drodz� wyznaczonej bili do �uzy NIE STOI inna bila
	 */
	public boolean findCollision(Point pocketPoint, Point selectedBall, int index, List<Ball> listBall) {

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

	/**
	 *
	 * @param listPocket Lista z pozycjami ka�dej �uzy
	 * @param listBall Lista wszystkich bill na stole
	 * @return Zwracana jest HashMapa gdzie kluczem jest �rodek bili VIRTUALNEJ, a warto�ci� jest pozycja �uzy.
	 * Zwr�cone bile nie koliduj� z innymi bilami na stole(Na drodze bili do �uzy nie stoi �adna inna bila).
	 */
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
