package pl.ncdc.billiard.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.opencv.core.Point;

@Data
@AllArgsConstructor
public class NewPoint {

	private Point bill;
	private Point pocket;
	private Point band;
}
