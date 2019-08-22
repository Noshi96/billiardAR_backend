package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.Ball;

@Service
public class HistoryService {

	/** List of previously detected balls **/
	private List<List<Ball>> history;
	/** How many previous states will be stored **/
	private final static int maxHistoryLength = 60;
	private final static double corretionRate = 0.75;

	public HistoryService() {
		this.history = new ArrayList<List<Ball>>();
	}

	/**
	 * Try to find location of each ball on the list. Calculate position in
	 * dependency of last known position
	 * 
	 * @param list
	 * @param radius
	 * @return
	 */
	public List<Ball> updateHistory(List<Ball> list, int radius) {

		if (list == null)
			if (history.size() == 0)
				return null;
			else
				return history.get(history.size() - 1);

		List<Ball> newList = new ArrayList<Ball>();

		for (Ball ball : list) {
			// prepare save to history
			newList.add(new Ball(ball.getId(), new Point(ball.getPoint().x, ball.getPoint().y)));

			Point avg = new Point();
			int detected = 1;

			for (List<Ball> prev : this.history) {

				Point point = findBallByPoint(ball.getPoint(), prev, radius);

				if (point != null) {
					avg.x += point.x;
					avg.y += point.y;
					detected++;
				}
			}

			avg.x /= detected;
			avg.y /= detected;

			double dx = ball.getPoint().x - avg.x;
			double dy = ball.getPoint().y - avg.y;

			// ball.getPoint().x = origin.x + 0.25 * dx;
			// ball.getPoint().y = origin.y + 0.25 * dy;
			// System.out.println("DX: " + dx + "| DY: " + dy);

			if (Math.abs(dx) < radius)
				ball.getPoint().x = avg.x;
			else
				ball.getPoint().x -= dx * corretionRate;
			if (Math.abs(dy) < radius)
				ball.getPoint().y = avg.y;
			else
				ball.getPoint().y -= dy * corretionRate;

		}
		// save to history history
		this.history.add(newList);
		if (this.history.size() > maxHistoryLength)
			this.history.remove(0);
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
