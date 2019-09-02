package pl.ncdc.billiard.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.opencv.core.Scalar;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoolDrawerParams {
    private int lineThickness = 2;
    private int ballRadius = 20;
    private Scalar ballColor = new Scalar(255, 255, 0);
    private int whiteBallRadius = 23;
    private Scalar whiteBallColor = new Scalar(255, 255, 255);
    private Scalar pocketColor = new Scalar(255, 255, 0);

    private int selectedBallRadius = 20;
    private Scalar selectedBallColor = new Scalar(255, 0, 0);
    private Scalar selectedPocketColor = new Scalar(255, 0 , 0);

    private Scalar trainingSelectedBallColor = new Scalar(100, 200, 255);
    private Scalar disturbBallColor = new Scalar(239, 180, 130);

    private int trajectoryLineThickness = 2;
    private Scalar trajectoryLineColor = new Scalar(0, 255, 255);
    private Scalar followRotationLineColor = new Scalar(147, 20, 255);
    private Scalar zeroRotationLineColor = new Scalar(255, 0, 0);

    private Scalar hiddenPlacesColor = new Scalar(255, 0, 0);
}
