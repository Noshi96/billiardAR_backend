package pl.ncdc.billiard.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.opencv.core.Point;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndividualTraining {
    private Long id;
    private DifficultyLevel difficultyLevel;
    private Point whiteBallPosition;
    private Point selectedBallPosition;
    private List<Point> disturbBallsPositions;
    private List<Point> rectanglePosition;
    private int pocketId;
}
