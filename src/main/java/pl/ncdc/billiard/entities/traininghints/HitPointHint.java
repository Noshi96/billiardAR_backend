package pl.ncdc.billiard.entities.traininghints;

import com.vividsolutions.jts.geom.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.ncdc.billiard.models.trainingHints.HitPoint;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HitPointHint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Point position;
    private double radius;
    private HitPoint hitPoint;
}
