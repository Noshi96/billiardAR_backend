package pl.ncdc.billiard.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.opencv.core.Point;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalibrationParams {

    private Long id;
    private String presetName;
    private Point leftBottomCorner;
    private Point leftUpperCorner;
    private Point rightBottomCorner;
    private Point rightUpperCorner;
    private int ballDiameter;
    private Point tableSizeInCm;
    private int whiteBallDensity;
    private Point leftBottomCornerProjector;
    private Point leftUpperCornerProjector;
    private Point rightBottomCornerProjector;
    private Point rightUpperCornerProjector;

    public static CalibrationParams getDefaultCalibrationParams() {
        return new CalibrationParams(null, "Default",
                new Point(0, 0),
                new Point(0, 1080),
                new Point(1920, 0),
                new Point(1920, 1080),
                20,
                new Point(254, 127),
                375000,
                new Point(0, 0),
                new Point(0, 1080),
                new Point(1920, 0),
                new Point(1920, 1080));
    }
}
