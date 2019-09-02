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
    private String ballColor = "#FFFF00";
    private int whiteBallRadius = 20;
    private String whiteBallColor = "#FFFFFF";
    private String pocketColor = "#FFFF00";

    private int selectedBallRadius = 20;
    private String selectedBallColor = "#FF0000";
    private String selectedPocketColor = "#FF0000";

    private String trainingSelectedBallColor = "#64C8FF";
    private String disturbBallColor = "#EFB482";

    private int trajectoryLineThickness = 2;
    private String trajectoryLineColor = "#00FFFF";
    private String followRotationLineColor = "#9314FF";
    private String zeroRotationLineColor = "#FF0000";

    private String hiddenPlacesColor = "#FF0000";
}
