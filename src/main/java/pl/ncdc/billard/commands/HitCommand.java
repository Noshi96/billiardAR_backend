package pl.ncdc.billard.commands;

import java.awt.Point;

public class HitCommand {

	private Point Ball;

	private Point Pocket;

	public HitCommand(Point ball, Point pocket) {
		Ball = ball;
		Pocket = pocket;
	}
	
	public HitCommand() {
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
