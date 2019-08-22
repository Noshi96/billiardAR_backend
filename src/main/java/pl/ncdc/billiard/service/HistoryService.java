package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.Ball;

@Service
public class HistoryService {

	private List<List<Ball>> history;
	private final static int maxHistoryLength = 30;

	public HistoryService() {
		this.history = new ArrayList<List<Ball>>();
	}

	public List<Ball> updateHistory(List<Ball> list, int radius) {

		if (list == null)
			if (history.size() == 0)
				return null;
			else
				return history.get(history.size() - 1);

		for (Ball ball : list) {
			// prepare save to history
			Point origin = new Point(ball.getPoint().x, ball.getPoint().y);
			int detected = 1;

			for (List<Ball> prev : this.history) {

				Point point = findBallByPoint(ball.getPoint(), prev, radius);

				if (point != null) {
					ball.getPoint().x = ball.getPoint().x + point.x;
					ball.getPoint().y = ball.getPoint().y + point.y;
					detected++;
				}
			}

			ball.getPoint().x = ball.getPoint().x / detected;
			ball.getPoint().y = ball.getPoint().y / detected;

			double dx = ball.getPoint().x - origin.x;
			double dy = ball.getPoint().y - origin.y;

			if (Math.abs(dx) > radius / 3)
				ball.getPoint().x = origin.x + (Math.abs(dx) / dx) * (radius / 3);
			if (Math.abs(dy) > radius / 3)
				ball.getPoint().y = origin.y + (Math.abs(dy) / dy) * (radius / 3);
		}

		if (this.history.size() > maxHistoryLength)
			this.history.remove(0);
		// save to history history
		this.history.add(list);

		return list;
	}

	private Point findBallByPoint(Point point, List<Ball> list, int radius) {
		for (Ball ball : list) {
			if (Math.abs(point.x - ball.getPoint().x) < radius && Math.abs(point.y - ball.getPoint().y) < radius)
				return ball.getPoint();
		}
		return null;
	}

}
