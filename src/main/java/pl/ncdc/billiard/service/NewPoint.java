package pl.ncdc.billiard.service;

public class NewPoint {

	private double xBill;
	private double yBill;
	private double xPocket;
	private double yPocket;
	public double getxBill() {
		return xBill;
	}
	public void setxBill(double xBill) {
		this.xBill = xBill;
	}
	public double getyBill() {
		return yBill;
	}
	public void setyBill(double yBill) {
		this.yBill = yBill;
	}
	public double getxPocket() {
		return xPocket;
	}
	public void setxPocket(double xPocket) {
		this.xPocket = xPocket;
	}
	public double getyPocket() {
		return yPocket;
	}
	public void setyPocket(double yPocket) {
		this.yPocket = yPocket;
	}
	public NewPoint(double xBill, double yBill, double xPocket, double yPocket) {
		super();
		this.xBill = xBill;
		this.yBill = yBill;
		this.xPocket = xPocket;
		this.yPocket = yPocket;
	}
	
	
	
	
	
	
}

