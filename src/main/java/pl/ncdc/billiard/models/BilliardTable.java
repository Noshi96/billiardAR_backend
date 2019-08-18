package pl.ncdc.billiard.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.opencv.core.Point;
import org.springframework.stereotype.Component;

import pl.ncdc.billiard.service.NewPoint;

@Component
@Data
@AllArgsConstructor
public class BilliardTable {

	private int height;

	private int width;

	private List<Ball> balls;

	private List<Pocket> pockets;

	private Ball whiteBall;

	private Ball selectedBall;

	private Pocket selectedPocket;

	private Point hittingPoint;
	
	private List<NewPoint> allPossibleHits;

	public BilliardTable() {
		balls = new ArrayList<>();
		pockets = new ArrayList<Pocket>();
		pockets.add(new Pocket(1, new Point(90, 85)));
		pockets.add(new Pocket(2, new Point(670, 58)));
		pockets.add(new Pocket(3, new Point(1250, 90)));
		pockets.add(new Pocket(4, new Point(80, 660)));
		pockets.add(new Pocket(5, new Point(665, 680)));
		pockets.add(new Pocket(6, new Point(1240, 665)));
	}
}
