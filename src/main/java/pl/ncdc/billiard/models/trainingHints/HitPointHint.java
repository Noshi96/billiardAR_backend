package pl.ncdc.billiard.models.trainingHints;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.opencv.core.Point;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HitPointHint {
    private Point position;
    private double radius;
    private HitPoint hitPoint;
}
