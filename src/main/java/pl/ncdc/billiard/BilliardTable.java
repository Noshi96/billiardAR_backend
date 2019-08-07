package pl.ncdc.billiard;

import java.util.ArrayList;
import java.util.List;

import pl.ncdc.billiard.entity.Ball;
import pl.ncdc.billiard.entity.Pocket;

public class BilliardTable {

	private int height;

	private int width;

	private List<Ball> balls;

	private List<Pocket> pockets;

	private Ball whiteBall;

	private Ball selectedBall;

	private Pocket selectedPocket;

	public BilliardTable() {
		pockets = new ArrayList<>();
		pockets.add(new Pocket(1L));
		pockets.add(new Pocket(2L));
		pockets.add(new Pocket(3L));
		pockets.add(new Pocket(4L));
		pockets.add(new Pocket(5L));
		pockets.add(new Pocket(6L));
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
}
