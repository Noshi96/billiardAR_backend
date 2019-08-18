package pl.ncdc.billiard.models;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;
import org.springframework.stereotype.Component;

import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.Pocket;
import pl.ncdc.billiard.service.NewPoint;

@Component
public class BilliardTable {

	private int height;

	private int width;

	private List<Ball> balls;

	private List<Pocket> pockets;

	private Ball whiteBall;

	private Ball selectedBall;

	private Pocket selectedPocket;

	private Point hittingPoint;
	
	private List<NewPoint> allPossibleHits;
	
	private List<Point> yellowBox;
	
	private String difficultyLevel;
	
	private List<Point> disturbPoints;
	
	// dodane przez Koala, przydatne do fronta
	// view mode- okresla wyswietlanie zawartosci na stronie
	// apka: 0 - pokazuej same bile, 1 - tryb pasywny 1, rysuje bile, rysuje zaznaczenia oraz trajektorie
	// display: 0 - rysuje okregi wokol wszystkich bil, odpowiednio oznacza biala, 1 - obrysowuje tylko biala bile, zaznaczona bile oraz zaznaczona luze i rysuje trajektorie
	private int selectedViewMode = 0;
	
	// okresla wybranie poziomu challange. 0 - zaden, normalna ryzgrywka i zczytywanie bil; 0<int - okresla id poziomu do zaladowania
	private int selectedChallenge = 0;

	
	public int getSelectedViewMode() {
		return selectedViewMode;
	}

	public void setSelectedViewMode(int selectedViewMode) {
		this.selectedViewMode = selectedViewMode;
	}

	public int getSelectedChallenge() {
		return selectedChallenge;
	}

	public void setSelectedChallenge(int selectedChallenge) {
		this.selectedChallenge = selectedChallenge;
	}


	// odwrocone bandy
	public BilliardTable() {
		balls = new ArrayList<>();
		pockets = new ArrayList<Pocket>();
		pockets.add(new Pocket(0, new Point(0, 620)));
		pockets.add(new Pocket(1, new Point(1190 / 2, 620)));
		pockets.add(new Pocket(2, new Point(1190, 620)));
		pockets.add(new Pocket(3, new Point(0, 0)));
		pockets.add(new Pocket(4, new Point(1190 / 2, 0)));
		pockets.add(new Pocket(5, new Point(1190, 0)));

		balls = new ArrayList<>();
		balls.add(new Ball(0, new Point(1000, 580)));
		balls.add(new Ball(1, new Point(55,222)));
		balls.add(new Ball(2, new Point(400,29) ));
		balls.add(new Ball(3, new Point(111,99)));
		balls.add(new Ball(4, new Point(480,29) ));		
		balls.add(new Ball(5, new Point(1190/2, 620/2)));
		balls.add(new Ball(6, new Point(1100, 600)));
		balls.add(new Ball(7, new Point(1000, 200)));
		balls.add(new Ball(8, new Point(400, 80)));
		balls.add(new Ball(9, new Point(666, 444)));
		//balls.add(new Ball(6, new Point(800,450)));
		//balls.add(new Ball(2, false, new Point(111,99), false));
		//balls.add(new Ball(2, false, new Point(111,99), false));
		this.setWhiteBall(balls.get(3));
		this.setSelectedBall(this.balls.get(5));
		this.setSelectedPocket(this.pockets.get(5));
	}

	public List<Ball> getBalls() {
		return balls;
	}

	public void setBalls(List<Ball> balls) {
		this.balls = balls;
	}

	public List<Pocket> getPockets() {
		return pockets;
	}

	public void setPockets(List<Pocket> pockets) {
		this.pockets = pockets;
	}

	public Ball getWhiteBall() {
		return whiteBall;
	}

	public void setWhiteBall(Ball whiteBall) {
		this.whiteBall = whiteBall;
	}

	public Ball getSelectedBall() {
		return selectedBall;
	}

	public void setSelectedBall(Ball selectedBall) {
		this.selectedBall = selectedBall;
	}

	public Pocket getSelectedPocket() {
		return selectedPocket;
	}

	public void setSelectedPocket(Pocket selectedPocket) {
		this.selectedPocket = selectedPocket;
	}

	public void setHittingPoint(Point hittingPoint) {
		this.hittingPoint = hittingPoint;
	}

	public Point getHittingPoint() {
		return hittingPoint;
	}

	public List<NewPoint> getAllPossibleHits() {
		return allPossibleHits;
	}

	public void setAllPossibleHits(List<NewPoint> allPossibleHits) {
		this.allPossibleHits = allPossibleHits;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public List<Point> getYellowBox() {
		return yellowBox;
	}

	public void setYellowBox(List<Point> yellowBox) {
		this.yellowBox = yellowBox;
	}

	public String getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(String difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	public List<Point> getDisturbPoints() {
		return disturbPoints;
	}

	public void setDisturbPoints(List<Point> disturbPoints) {
		this.disturbPoints = disturbPoints;
	}

	
	
}
