package pl.ncdc.billiard.entities;

import com.vividsolutions.jts.geom.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalibrationParams {

    @Id
    @GeneratedValue
    private Long id;
    private String presetName;
    private Point leftBottomCorner;
    private Point leftUpperCorner;
    private Point rightBottomCorner;
    private Point rightUpperCorner;
    private int ballDiameter;
}
