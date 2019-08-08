package pl.ncdc.billard.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Hit {

	@Id
	private int id;

	@OneToOne
	private Ball selectedBall;

	@OneToOne
	private Pocket selectedPocket;

	private float angle;

	public Hit(int id, Ball selectedBall, Pocket selectedPocket, float angle) {
		this.id = id;
		this.selectedBall = selectedBall;
		this.selectedPocket = selectedPocket;
		this.angle = angle;
	}

	public Hit() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Ball getSelectedBall() {
		return selectedBall;
	}

	public void setSelectedBall(Ball selectedBall) {
		this.selectedBall = selectedBall;
	}

	public Pocket getSelectedPocket() {
		return selectedPocket;
	}

	public void setSelectedPocket(Pocket selectedPocket) {
		this.selectedPocket = selectedPocket;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

}
