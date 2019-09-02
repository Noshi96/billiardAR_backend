package pl.ncdc.billiard.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.opencv.core.Scalar;

import javax.persistence.*;

@Entity(name = "pool_drawer_params")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoolDrawerParamsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int textXD = 30;

    private int ballRadius = 20;
    private int whiteBallRadius = 20;
    private int pocketRadius = 50;
    private int trainingBallRadius = 25;

    private int ballLineThickness = 2;
    private int pocketLineThickness = 5;
    private int trajectoryLineThickness = 2;
    private int playZoneBorderThickness = 1;
    private int selectedPocketLineThickness = 4;
    private int trainingRectangleThickness = 3;

    private String whiteBallColor = "#FFFFFF";
    private String ballColor = "#EEEEEE";
}
