package pl.ncdc.billiard.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.opencv.core.Point;

@Data
@AllArgsConstructor
public class Pocket {
	private int id;
	
	private Point point = new Point();

	public Pocket(int id) {
		this.id = id;
	}
}
