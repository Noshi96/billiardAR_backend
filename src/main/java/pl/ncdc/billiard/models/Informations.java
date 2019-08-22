package pl.ncdc.billiard.models;

public class Informations {

	
	private double distanceWhiteSelected;
	
	private double distanceWhitePocket;
	
	private double hitAngle;
	
	private int difficultyLevel;

	public double getDistanceWhiteSelected() {
		return distanceWhiteSelected;
	}

	public void setDistanceWhiteSelected(double distanceWhiteSelected) {
		this.distanceWhiteSelected = distanceWhiteSelected;
	}

	public double getDistanceWhitePocket() {
		return distanceWhitePocket;
	}

	public void setDistanceWhitePocket(double distanceWhitePocket) {
		this.distanceWhitePocket = distanceWhitePocket;
	}

	public double getHitAngle() {
		return hitAngle;
	}

	public void setHitAngle(double hitAngle) {
		this.hitAngle = hitAngle;
	}

	public int getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(int difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	public Informations(double distanceWhiteTarget, double distanceWhitePocket, double hitAngle, int difficultyLevel) {
		super();
		this.distanceWhiteSelected = distanceWhiteTarget;
		this.distanceWhitePocket = distanceWhitePocket;
		this.hitAngle = hitAngle;
		this.difficultyLevel = difficultyLevel;
	}
	
	public Informations() {
	
	}
	
	
	
	
}
