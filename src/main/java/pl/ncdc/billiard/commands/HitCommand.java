package pl.ncdc.billiard.commands;

import pl.ncdc.billiard.entity.Point;

public class HitCommand {

	private Point Ball;

	private Point Pocket;

	public HitCommand(Point ball, Point pocket) {
		Ball = ball;
		Pocket = pocket;
	}

	public HitCommand() {
		super();
	}

	public Point getBall() {
		return Ball;
	}

	public void setBall(Point ball) {
		Ball = ball;
	}

	public Point getPocket() {
		return Pocket;
	}

	public void setPocket(Point pocket) {
		Pocket = pocket;
	}


}
