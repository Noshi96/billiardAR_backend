package pl.ncdc.billiard.service;

import org.opencv.core.Point;

public class NewPoint {

	private Point bill;
	private Point pocket;
	private Point band;

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

	public double getBandX(double x) {
		return band.x = x;
	}

	public double getBandY(double y) {
		return band.y = y;
	}

	public void setBandX(double x) {
		this.band.x = x;
	}

	public void setBandY(double y) {
		this.band.y = y;
	}
	public NewPoint(Point bill, Point pocket, Point band) {
		this.bill = bill;
		this.pocket = pocket;
		this.band = band;
	}

}
