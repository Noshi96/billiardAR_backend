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

    private int lineThickness = 2;
    private int ballRadius = 20;
    private String ballColor = "#00FFFF";
    private int whiteBallRadius = 23;
    private String whiteBallColor = "#FFFFFF";
    private String pocketColor = "#00FFFF";

    private int selectedBallRadius = 20;
    private String selectedBallColor = "#0000FF";
    private String selectedPocketColor = "#0000FF";

    private String trainingSelectedBallColor = "#FFC864";
    private String disturbBallColor = "#82B4EF";

    private int trajectoryLineThickness = 2;
    private String trajectoryLineColor = "#FFFF00";
    private String followRotationLineColor = "#FF1493";
    private String zeroRotationLineColor = "#0000FF";

    private String hiddenPlacesColor = "#0000FF";
}
