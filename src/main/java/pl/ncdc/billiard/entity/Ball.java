package pl.ncdc.billiard.entity;

import org.opencv.core.Point;


public class Ball {

	private int id;

	private Point point;

	public Ball(int id,Point point) {
		this.id = id;
		this.point = point;
	}

	public Ball() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

}
