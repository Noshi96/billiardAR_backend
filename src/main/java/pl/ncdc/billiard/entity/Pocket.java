package pl.ncdc.billiard.entity;

import java.awt.Point;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Pocket {
	
	@Id
	private int id;
	
	private boolean selected;
	
	private Point point = new Point();

	public Point getPoint() {
		return point;
	}

	public Pocket(int id, boolean selected, Point point) {
		super();
		this.id = id;
		this.selected = selected;
		this.point = point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}
	

	public Pocket(int id) {
		this.id = id;
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

	
}
