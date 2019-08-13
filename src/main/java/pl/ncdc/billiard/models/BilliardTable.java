package pl.ncdc.billiard.models;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;
import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.Pocket;
import pl.ncdc.billiard.service.NewPoint;

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

	public BilliardTable() {
		balls = new ArrayList<>();
		pockets = new ArrayList<Pocket>();
		pockets.add(new Pocket(1, new Point(90, 85)));
		pockets.add(new Pocket(2, new Point(670, 58)));
		pockets.add(new Pocket(3, new Point(1250, 90)));
		pockets.add(new Pocket(4, new Point(80, 660)));
		pockets.add(new Pocket(5, new Point(665, 680)));
		pockets.add(new Pocket(6, new Point(1240, 665)));
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

}
