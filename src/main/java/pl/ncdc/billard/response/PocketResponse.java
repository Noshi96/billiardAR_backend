package pl.ncdc.billard.response;

import java.util.List;

public class PocketResponse {

	private Long id;

	private boolean selected;

	private List<BallResponse> balls;

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

	public List<BallResponse> getBalls() {
		return balls;
	}

	public void setBalls(List<BallResponse> balls) {
		this.balls = balls;
	}

}
