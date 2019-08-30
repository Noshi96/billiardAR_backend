package pl.ncdc.billiard.models.trainingHints;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HitPointHint {
    private Point position;
    private double radius;
    private HitPoint hitPoint;
    private List<Point> insideCirclesOffsets;

    public void recalculateInsideCirclesOffsets() {
        if(insideCirclesOffsets == null) {
            insideCirclesOffsets = new ArrayList<>();
        }

        HitPoint[] hitPoints = HitPoint.values();

        if(insideCirclesOffsets.size() != hitPoints.length) {
            insideCirclesOffsets.clear();
            for (HitPoint hitPoint : hitPoints) {
                insideCirclesOffsets.add(new Point(0, 0));
            }
        }

        for (int i = 0; i < hitPoints.length - 1; i++) {
            insideCirclesOffsets.get(i + 1).x = 0.66 * this.radius * Math.sin(-((i + 4) % 8) * Math.PI / 4);
            insideCirclesOffsets.get(i + 1).y = 0.66 * this.radius * Math.cos(-((i + 4) % 8) * Math.PI / 4);
        }
    }
}
