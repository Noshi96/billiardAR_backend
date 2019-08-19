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

	private List<Point> yellowBox;

	private String difficultyLevel;

	private List<Point> disturbPoints;

	public BilliardTable() {
	}

}
