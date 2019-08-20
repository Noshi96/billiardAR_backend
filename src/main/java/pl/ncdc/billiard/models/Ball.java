package pl.ncdc.billiard.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.opencv.core.Point;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ball {

	private int id;

	private Point point;
}
