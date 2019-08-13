package pl.ncdc.billiard.service;

import org.opencv.core.Point;

public class NewPoint {

	private Point bill;
	private Point pocket;

	public double getBillX() {
		return bill.x;
	}

	public void setBillX(double x) {
		this.bill.x = x;
	}

	public double getBillY() {
		return bill.y;
	}

	public void setBillY(double y) {
		this.bill.y = y;
	}

	public double getPocketX() {
		return pocket.x;
	}

	public void setPocketX(double x) {
		this.pocket.x = x;
	}

	public double getPocketY() {
		return pocket.y;
	}

	public void setPocketY(double y) {
		this.pocket.y = y;
	}

	public NewPoint(Point bill, Point pocket) {
		this.bill = bill;
		this.pocket = pocket;
	}

}
