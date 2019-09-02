package pl.ncdc.billiard.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.opencv.core.Scalar;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoolDrawerParams {
    private int textXD = 30;

    private int ballRadius = 20;
    private int whiteBallRadius = 20;
    private int pocketRadius = 50;
    private int trainingBallRadius = 25;

    private int ballLineThickness = 2;
    private int pocketLineThickness = 5;
    private int trajectoryLineThickness = 2;
    private int playZoneBorderThickness = 1;
    private int selectedPocketLineThickness = 4;
    private int trainingRectangleThickness = 3;

    private Scalar whiteBallColor = new Scalar(255, 255, 255);
    private Scalar ballColor = new Scalar(240, 240, 240);
}
