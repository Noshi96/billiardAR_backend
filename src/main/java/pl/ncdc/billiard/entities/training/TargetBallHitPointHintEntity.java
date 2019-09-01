package pl.ncdc.billiard.entities.training;

import com.vividsolutions.jts.geom.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @ToString.Exclude
    private TrainingEntity trainingEntity;

    private Point whiteBall;
    private Point targetBall;
    private double radius;
}
