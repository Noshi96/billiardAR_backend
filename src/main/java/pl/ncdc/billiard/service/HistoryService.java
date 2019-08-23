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
	private final static int maxHistoryLength = 300;
	/** How much current position should change new calculated position **/
	private final static double corretionRate = 0.1;
	/** Number of frames to search disappearing balls **/
	private final static int historyScanLimit = 30;

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

			if (Math.abs(dx) < radius / 2)
				ball.getPoint().x = avg.x;
			else if (Math.abs(dx) < radius)
				ball.getPoint().x = avg.x + dx * corretionRate * 2;

			if (Math.abs(dy) < radius / 2)
				ball.getPoint().y = avg.y;
			else if (Math.abs(dy) < radius)
				ball.getPoint().y = avg.y + dy * corretionRate * 2;

		}
		// save to history history
		this.history.add(newList);
		if (this.history.size() > maxHistoryLength)
			this.history.remove(0);
		return list;
	}

	public List<Ball> findMissingBalls(List<Ball> list, int radius) {

		int lastIndex = this.history.size() - historyScanLimit;
		if (lastIndex < 0)
			lastIndex = 0;

		for (int index = lastIndex; index < this.history.size(); index++) {
			for (Ball ball : this.history.get(index)) {
				if (findBallByPoint(ball.getPoint(), list, radius) == null) {
					int count = 0;
					for (int i = index; i < this.history.size(); i++)
						if (findBallByPoint(ball.getPoint(), this.history.get(i), radius) != null)
							count++;
					if (count > historyScanLimit / 6)
						list.add(ball);
				}
			}

		}
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
