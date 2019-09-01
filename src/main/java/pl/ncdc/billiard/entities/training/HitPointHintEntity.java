package pl.ncdc.billiard.entities.training;

import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.ncdc.billiard.models.training.HitPoint;

import javax.persistence.*;

@Entity(name = "hit_point_hint")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HitPointHintEntity {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @ToString.Exclude
    private TrainingEntity trainingEntity;

    private Point position;
    private double radius;
    private HitPoint hitPoint;
    private MultiPoint insideCirclesOffsets;
}
