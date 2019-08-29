package pl.ncdc.billiard.entities.traininghints;

import com.vividsolutions.jts.geom.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.ncdc.billiard.entities.IndividualTrainingEntity;

import javax.persistence.*;

@Entity(name = "target_ball_hit_point_hint")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TargetBallHitPointHintEntity {
    
    @Id
    private Long id;

    @OneToOne
    @MapsId
    private IndividualTrainingEntity individualTraining;

    private Point whiteBall;
    private Point targetBall;
}
