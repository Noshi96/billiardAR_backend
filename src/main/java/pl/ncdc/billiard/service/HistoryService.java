package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.Ball;

@Service
public class HistoryService {

	private List<List<Ball>> history;
	private int maxHistoryLength;

	public HistoryService() {
		this.history = new ArrayList<List<Ball>>();
		this.maxHistoryLength = 30;
	}

	public List<Ball> updateHistory(List<Ball> list, int radius) {
		if (list == null)
			if (history.size() == 0)
				return null;
			else
				return history.get(history.size() - 1);

		List<Ball> newList = new ArrayList<Ball>();
		// deep copy of list
		for (Ball ball : list) {
			newList.add(new Ball(ball.getId(), ball.getPoint()));
		}

		for (Ball ball : newList) {
			int detected = 0;
			for (List<Ball> prev : this.history) {
				Point point = findBallByPoint(ball.getPoint(), prev, radius);
				if (point != null) {
					ball.getPoint().x += ball.getPoint().x;
					ball.getPoint().y += ball.getPoint().y;
					detected++;
				}
			}
			if (detected > 0) {
				ball.getPoint().x = ball.getPoint().x / detected;
				ball.getPoint().y = ball.getPoint().y / detected;
			}
		}

		// fill history
		this.history.add(list);

		if (this.history.size() > this.maxHistoryLength)
			this.history.remove(0);

		return newList;
	}

	private Point findBallByPoint(Point point, List<Ball> list, int radius) {
		for (Ball ball : list) {
			if (Math.abs(point.x - ball.getPoint().x) < radius && Math.abs(point.y - ball.getPoint().y) < radius)
				return ball.getPoint();
		}
		return null;
	}

}
