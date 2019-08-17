package pl.ncdc.billiard.Entities;

import org.opencv.core.Point;
import javax.persistence.*;

@Entity
public class CalibrationParams {

    @Id
    @GeneratedValue
    private Long id;
    private String presetName;
    @Transient
    private Point leftBottomCorner;
    private double leftBottomCornerX;
    private double leftBottomCornerY;
    @Transient
    private Point leftUpperCorner;
    private double leftUpperCornerX;
    private double leftUpperCornerY;
    @Transient
    private Point rightBottomCorner;
    private double rightBottomCornerX;
    private double rightBottomCornerY;
    @Transient
    private Point rightUpperCorner;
    private double rightUpperCornerX;
    private double rightUpperCornerY;


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


    public pl.ncdc.billiard.models.CalibrationParams toModel() {
        pl.ncdc.billiard.models.CalibrationParams calibrationParams = new pl.ncdc.billiard.models.CalibrationParams();
        calibrationParams.setId(this.getId());
        calibrationParams.setPresetName(this.getPresetName());
        calibrationParams.setBallDiameter(this.getBallDiameter());
        calibrationParams.setLeftBottomCorner(this.getLeftBottomCorner());
        calibrationParams.setLeftUpperCorner(this.getLeftUpperCorner());
        calibrationParams.setRightBottomCorner(this.getRightBottomCorner());
        calibrationParams.setRightUpperCorner(this.getRightUpperCorner());

        return calibrationParams;
    }

    public void copyFromModel(pl.ncdc.billiard.models.CalibrationParams calibrationParams) {
        this.setPresetName(calibrationParams.getPresetName());
        this.setBallDiameter(calibrationParams.getBallDiameter());
        this.setLeftBottomCorner(calibrationParams.getLeftBottomCorner());
        this.setLeftUpperCorner(calibrationParams.getLeftUpperCorner());
        this.setRightBottomCorner(calibrationParams.getRightBottomCorner());
        this.setRightUpperCorner(calibrationParams.getRightUpperCorner());
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
        if(leftBottomCorner == null) {
            leftBottomCorner = new Point(leftBottomCornerX, leftBottomCornerY);
        }
        return leftBottomCorner;
    }

    public void setLeftBottomCorner(Point leftBottomCorner) {
        this.leftBottomCorner = leftBottomCorner;
        this.leftBottomCornerX = leftBottomCorner.x;
        this.leftBottomCornerY = leftBottomCorner.y;
    }

    public Point getLeftUpperCorner() {
        if(leftUpperCorner == null) {
            this.leftUpperCorner = new Point(leftUpperCornerX, leftUpperCornerY);
        }
        return leftUpperCorner;
    }

    public void setLeftUpperCorner(Point leftUpperCorner) {
        this.leftUpperCorner = leftUpperCorner;
        this.leftUpperCornerX = leftUpperCorner.x;
        this.leftUpperCornerY = leftUpperCorner.y;
    }

    public Point getRightBottomCorner() {
        if(rightBottomCorner == null) {
            rightBottomCorner = new Point(rightBottomCornerX, rightBottomCornerY);
        }
        return rightBottomCorner;
    }

    public void setRightBottomCorner(Point rightBottomCorner) {
        this.rightBottomCorner = rightBottomCorner;
        this.rightBottomCornerX = rightBottomCorner.x;
        this.rightBottomCornerY = rightBottomCorner.y;
    }

    public Point getRightUpperCorner() {
        if(rightUpperCorner == null) {
            rightUpperCorner = new Point(rightUpperCornerX, rightUpperCornerY);
        }
        return rightUpperCorner;
    }

    public void setRightUpperCorner(Point rightUpperCorner) {
        this.rightUpperCorner = rightUpperCorner;
        this.rightUpperCornerX = rightUpperCorner.x;
        this.rightUpperCornerY = rightUpperCorner.y;
    }

    public int getBallDiameter() {
        return ballDiameter;
    }

    public void setBallDiameter(int ballDiameter) {
        this.ballDiameter = ballDiameter;
    }
}
