package pl.ncdc.billiard.entities.training;

import com.vividsolutions.jts.geom.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "hit_power_hint")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HitPowerHintEntity {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @ToString.Exclude
    private TrainingEntity trainingEntity;

    private Point position;
    private Point size;
    private double hitPower;
}
