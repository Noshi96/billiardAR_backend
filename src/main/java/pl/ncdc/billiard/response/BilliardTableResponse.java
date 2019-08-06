package pl.ncdc.billiard.response;

import java.util.List;

public class BilliardTableResponse {
	
	private Long id;
	
	private float height;
	
	private float width;
	
	private List<BallResponse> balls;
	
	private List<PocketResponse> pockets;

	public BilliardTableResponse() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public List<BallResponse> getBalls() {
		return balls;
	}

	public void setBalls(List<BallResponse> balls) {
		this.balls = balls;
	}

	public List<PocketResponse> getPockets() {
		return pockets;
	}

	public void setPockets(List<PocketResponse> pockets) {
		this.pockets = pockets;
	}

	

}
