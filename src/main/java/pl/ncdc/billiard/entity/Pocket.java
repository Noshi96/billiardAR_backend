package pl.ncdc.billiard.entity;

import java.awt.Point;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Pocket {
	
	@Id
	private int id;
	
	private boolean selected;
	
	private Point point;

	public Pocket() {
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

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}
	
}
