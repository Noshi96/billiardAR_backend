package pl.ncdc.billiard.entity;

import java.awt.Point;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Ball {

	@Id
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
