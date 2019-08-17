package pl.ncdc.billiard.models;

import org.opencv.core.Point;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class CalibrationParams {

    private Long id;
    private String presetName;
    private Point leftBottomCorner;
    private Point leftUpperCorner;
    private Point rightBottomCorner;
    private Point rightUpperCorner;

    private int ballDiameter;


    public CalibrationParams() {
    }

    public CalibrationParams(Long id, String presetName, Point leftBottomCorner, Point leftUpperCorner, Point rightBottomCorner, Point rightUpperCorner, int ballDiameter) {
        this.id = id;
        this.presetName = presetName;
        this.leftBottomCorner = leftBottomCorner;
        this.leftUpperCorner = leftUpperCorner;
        this.rightBottomCorner = rightBottomCorner;
        this.rightUpperCorner = rightUpperCorner;
        this.ballDiameter = ballDiameter;
    }

    public static CalibrationParams getDefaultCalibrationParams() {
        return new CalibrationParams(null, "Default",
                new Point(0, 0),
                new Point(0, 1080),
                new Point(1920, 0),
                new Point(1920, 1080),
                20);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPresetName() {
        return presetName;
    }

    public void setPresetName(String presetName) {
        this.presetName = presetName;
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
