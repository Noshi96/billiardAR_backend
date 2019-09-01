package pl.ncdc.billiard.entities.traininghints;

import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.ncdc.billiard.entities.IndividualTrainingEntity;
import pl.ncdc.billiard.models.trainingHints.HitPoint;

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
    private IndividualTrainingEntity individualTraining;

    private Point position;
    private double radius;
    private HitPoint hitPoint;
    private MultiPoint insideCirclesOffsets;
}
