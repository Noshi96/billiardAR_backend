package pl.ncdc.billiard.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Pocket {
	
	@Id
	private Long id;
	
	private boolean selected;
	
	public Pocket(Long id) {
		this.id = id;
	}
	
	public Pocket() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
