package pl.ncdc.billiard.entities;

import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.ncdc.billiard.entities.traininghints.HitPointHint;
import pl.ncdc.billiard.entities.traininghints.HitPowerHint;
import pl.ncdc.billiard.entities.traininghints.TargetBallHitPointHint;
import pl.ncdc.billiard.models.DifficultyLevel;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "individual_training")
public class IndividualTrainingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private DifficultyLevel difficultyLevel;
    private Point whiteBallPosition;
    private Point selectedBallPosition;
    private MultiPoint disturbBallsPositions;
    private MultiPoint rectanglePosition;
    private int pocketId;
    private Point statusPosition;
    @OneToOne
    private HitPointHint hitPointHint;
    @OneToOne
    private HitPowerHint hitPowerHint;
    @OneToOne
    private TargetBallHitPointHint targetBallHitPointHint;
}
