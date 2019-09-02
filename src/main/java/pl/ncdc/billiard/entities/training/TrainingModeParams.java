package pl.ncdc.billiard.entities.training;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingModeParams {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double waitingForBallsPlacementDelay = 2.0;
    private double ballsStopMovingDelay = 2.0;
    private double afterEndDelay = 5.0;
    private double ballPositionTolerance = 0.25;

    private AfterEndAction afterSuccessAction = AfterEndAction.NextLevel;
    private AfterEndAction afterFailAction = AfterEndAction.SameLevel;

    public enum AfterEndAction {
        SameLevel,
        NextLevel,
        RandomLevelOfSameDifficulty,
        RandomLevelOfAnyDifficulty,
    }
}
