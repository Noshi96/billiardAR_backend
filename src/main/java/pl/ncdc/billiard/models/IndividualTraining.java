package pl.ncdc.billiard.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.opencv.core.Point;
import pl.ncdc.billiard.models.trainingHints.HitPointHint;
import pl.ncdc.billiard.models.trainingHints.HitPowerHint;
import pl.ncdc.billiard.models.trainingHints.TargetBallHitPointHint;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndividualTraining {
    public static int PREVIEW_WIDTH = 1168; // taken from calibration
    public static int PREVIEW_HEIGHT = 584;

    private Long id;
    private DifficultyLevel difficultyLevel;
    private Point whiteBallPosition;
    private Point selectedBallPosition;
    private List<Point> disturbBallsPositions;
    private List<Point> rectanglePosition;
    private int pocketId;
    private Point statusPosition;
    private HitPointHint hitPointHint;
    private HitPowerHint hitPowerHint;
    private TargetBallHitPointHint targetBallHitPointHint;
    private byte[] imagePreview;
}
