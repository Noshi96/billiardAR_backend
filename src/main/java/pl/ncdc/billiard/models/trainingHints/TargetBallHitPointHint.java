package pl.ncdc.billiard.models.trainingHints;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.opencv.core.Point;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TargetBallHitPointHint {
    private Point whiteBall;
    private Point targetBall;
    private double radius;
}
