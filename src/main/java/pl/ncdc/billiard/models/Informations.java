package pl.ncdc.billiard.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Informations {
    private double distanceWhiteSelected;

    private double distanceWhitePocket;

    private double hitAngle;

    private int difficultyLevel;
}
