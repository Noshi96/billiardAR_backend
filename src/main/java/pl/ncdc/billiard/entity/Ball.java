package pl.ncdc.billiard.entity;

import java.awt.Point;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Ball {

	@Id
	private int id;

	private float diameter;

	private boolean selected;

	private Point point;

	boolean white;

	public Ball(int id, float diameter, boolean selected, Point point, boolean white) {
		this.id = id;
		this.diameter = diameter;
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

	public float getDiameter() {
		return diameter;
	}

	public void setDiameter(float diameter) {
		this.diameter = diameter;
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
