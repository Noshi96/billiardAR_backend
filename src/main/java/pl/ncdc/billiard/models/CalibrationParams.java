package pl.ncdc.billiard.models;

import org.opencv.core.Point;

public class CalibrationParams {
    private Point leftBottomCorner = new Point(0, 0);
    private Point leftUpperCorner = new Point(0, 1080);
    private Point rightBottomCorner = new Point(1920, 0);
    private Point rightUpperCorner = new Point(1920, 1080);
    private int ballDiameter = 20;

    public CalibrationParams() {
    }

    public CalibrationParams(Point leftBottomCorner, Point leftUpperCorner, Point rightBottomCorner, Point rightUpperCorner, int ballDiameter) {
        this.leftBottomCorner = leftBottomCorner;
        this.leftUpperCorner = leftUpperCorner;
        this.rightBottomCorner = rightBottomCorner;
        this.rightUpperCorner = rightUpperCorner;
        this.ballDiameter = ballDiameter;
    }

    public Point getLeftBottomCorner() {
        return leftBottomCorner;
    }

    public void setLeftBottomCorner(Point leftBottomCorner) {
        this.leftBottomCorner = leftBottomCorner;
    }

    public Point getLeftUpperCorner() {
        return leftUpperCorner;
    }

    public void setLeftUpperCorner(Point leftUpperCorner) {
        this.leftUpperCorner = leftUpperCorner;
    }

    public Point getRightBottomCorner() {
        return rightBottomCorner;
    }

    public void setRightBottomCorner(Point rightBottomCorner) {
        this.rightBottomCorner = rightBottomCorner;
    }

    public Point getRightUpperCorner() {
        return rightUpperCorner;
    }

    public void setRightUpperCorner(Point rightUpperCorner) {
        this.rightUpperCorner = rightUpperCorner;
    }

    public int getBallDiameter() {
        return ballDiameter;
    }

    public void setBallDiameter(int ballDiameter) {
        this.ballDiameter = ballDiameter;
    }
}
