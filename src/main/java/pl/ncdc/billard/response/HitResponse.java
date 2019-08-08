package pl.ncdc.billard.response;

import pl.ncdc.billard.entity.Ball;
import pl.ncdc.billard.entity.Pocket;

public class HitResponse {

	private Long id;

	private Ball selectedBall;

	private Pocket selectedPocket;

	private float angle;

	public HitResponse() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
