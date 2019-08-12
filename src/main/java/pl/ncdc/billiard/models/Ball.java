package pl.ncdc.billiard.models;

import java.awt.Point;

public class Ball {

	private int id;

	public static  final float DIAMETER = 5.72F;

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
