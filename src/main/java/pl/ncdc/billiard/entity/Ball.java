package pl.ncdc.billiard.entity;

import java.awt.Point;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Ball {

	@Id
	private int id;

	private static  final float DIAMETER = 5.72F;

	private boolean selected;

	private Point point;

	boolean white;

	public Ball(int id, boolean selected, Point point, boolean white) {
		this.id = id;
		this.selected = selected;
		this.point = point;
		this.white = white;
	}

	public Ball() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public boolean isWhite() {
		return white;
	}

	public void setWhite(boolean white) {
		this.white = white;
	}

}
