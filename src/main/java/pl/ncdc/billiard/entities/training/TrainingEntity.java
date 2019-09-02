package pl.ncdc.billiard.entities.training;

import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.ncdc.billiard.models.training.DifficultyLevel;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "training")
public class TrainingEntity {

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
    @OneToOne(mappedBy = "trainingEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private HitPointHintEntity hitPointHint;
    @OneToOne(mappedBy = "trainingEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private HitPowerHintEntity hitPowerHint;
    @OneToOne(mappedBy = "trainingEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private TargetBallHitPointHintEntity targetBallHitPointHint;
    @Lob
    private byte[] imagePreview;
}
