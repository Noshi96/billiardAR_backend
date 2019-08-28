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
                new Point(436, 880),
                new Point(442, 292),
                new Point(1603, 889),
                new Point(1611, 308),
                26,
                new Point(254, 127),
                320000,
                new Point(97, 954),
                new Point(70, 70),
                new Point(1803, 960),
                new Point(1827, 80));
    }
}
