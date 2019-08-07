package pl.ncdc.billiard.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Point {
	
	@Id
	private Long id;

	private int positionX;

	private int positionY;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	public Point() {
	}

	public Point(Long id, int positionX, int positionY) {
		this.id = id;
		this.positionX = positionX;
		this.positionY = positionY;
	}
	
	
	

}
