package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.Ball;

@Service
public class HistoryService {

	@Autowired
	private MathService math;

	/** List of previously detected balls **/
	private List<List<Ball>> history;
	/** List of previously detected white balls **/
	private List<Ball> whiteHistory;
	/** How many previous states will be stored **/
	private final static int HISTORY_MAX_LENGTH = 90;
	/** How much current position should change new calculated position **/
	private final static double CORRECTION_RATE = 0.1;
	/** Number of frames to search disappearing balls **/
	private final static int HISTORY_SCAN_LIMIT = 20;

	public HistoryService() {
		this.history = new ArrayList<List<Ball>>();
		this.whiteHistory = new ArrayList<Ball>();
	}

	public void updateHistory(List<Ball> list, Ball whiteBall, int radius) {
		updateHistory(list, radius);
		removeFalseBalls(list, radius);
		findMissingBalls(list, radius);
		updateHistory(whiteBall, radius);
		removeFalseWhite(list, whiteBall, radius);
		this.math.sort(list);
	}

	/**
	 * Try to calculate new position of white ball;
	 * 
	 * @param whiteBall
	 * @param radius
	 * @return new position of white ball
	 */
	public void updateHistory(Ball whiteBall, int radius) {

		if (whiteBall == null) {
			if (this.whiteHistory.size() > 0)
				whiteBall = this.whiteHistory.get(this.whiteHistory.size() - 2);
			return;
		}
		this.whiteHistory.add(new Ball(0, new Point(whiteBall.getPoint().x, whiteBall.getPoint().y)));
		if (this.whiteHistory.size() > HISTORY_MAX_LENGTH)
			this.whiteHistory.remove(0);

		Point avg = new Point();
		int detected = 0;

		for (Ball ball : this.whiteHistory) {
			if (ball != null && math.isBallCloseToPoint(whiteBall, ball.getPoint(), radius)) {
				avg.x += ball.getPoint().x;
				avg.y += ball.getPoint().y;
				detected++;
			}
		}
		if (detected < 2)
			return;

		calculateAvg(whiteBall.getPoint(), avg, detected, radius);

	}

	/**
	 * Try to find location of each ball on the list. Calculate position in
	 * dependency of last known position
	 * 
	 * @param list
	 * @param radius
	 * @return
	 */
	public void updateHistory(List<Ball> list, int radius) {

		if (list == null) {
			if (history.size() > 0)
				list = history.get(history.size() - 1);
			return;
		}
		List<Ball> newList = new ArrayList<Ball>();

		for (Ball ball : list) {
			// prepare save to history
			newList.add(new Ball(ball.getId(), new Point(ball.getPoint().x, ball.getPoint().y)));

			Point avg = new Point();
			int detected = 0;

			for (List<Ball> prev : this.history) {

				Ball ball2 = math.findBallByPoint(prev, ball.getPoint(), radius);
				if (ball2 != null) {
					avg.x += ball2.getPoint().x;
					avg.y += ball2.getPoint().y;
					detected++;
				}
			}

			if (detected == 0)
				break;
			calculateAvg(ball.getPoint(), avg, detected, radius);

		}
		// save to history history
		this.history.add(newList);
		if (this.history.size() > HISTORY_MAX_LENGTH)
			this.history.remove(0);
	}

	private void calculateAvg(Point point, Point avg, int items, int radius) {

		avg.x /= (double) items;
		avg.y /= (double) items;

		double dx = point.x - avg.x;
		double dy = point.y - avg.y;

		if (Math.abs(dx) < radius / 2)
			point.x = avg.x;
		else if (Math.abs(dx) < radius)
			point.x = avg.x + dx * CORRECTION_RATE * 2;

		if (Math.abs(dy) < radius / 2)
			point.y = avg.y;
		else if (Math.abs(dy) < radius)
			point.y = avg.y + dy * CORRECTION_RATE * 2;
	}

	public List<Ball> findMissingBalls(List<Ball> list, int radius) {

		int lastIndex = this.history.size() - HISTORY_SCAN_LIMIT;
		if (lastIndex < 0)
			lastIndex = 0;

		for (int index = lastIndex; index < this.history.size(); index++) {
			for (Ball ball : this.history.get(index)) {
				if (math.findBallByPoint(list, ball.getPoint(), radius) == null) {
					int count = 0;
					for (int i = index; i < this.history.size(); i++)
						if (math.findBallByPoint(this.history.get(i), ball.getPoint(), radius) != null)
							count++;
					if (count > HISTORY_SCAN_LIMIT * 2 / 3)
						list.add(ball);
				}
			}

		}
		return list;
	}

	/**
	 * Remove ball from list if detected in less than 1/3 frames of historyScanLimit
	 * frames
	 * 
	 * @param list
	 * @param maxBallRadius
	 */
	public void removeFalseBalls(List<Ball> list, int maxBallRadius) {
		int lastIndex = this.history.size() - HISTORY_SCAN_LIMIT;
		if (lastIndex < 0)
			lastIndex = 0;

		List<Ball> newList = new ArrayList<Ball>();

		for (Ball ball : list) {
			int count = 0;
			for (int index = lastIndex; index < this.history.size(); index++)
				if (math.findBallByPoint(this.history.get(index), ball.getPoint(), maxBallRadius) != null)
					count++;
			if (count < HISTORY_SCAN_LIMIT / 2)
				newList.add(ball);
		}
		list.removeAll(newList);
	}

	/**
	 * Remove false ball from list if detected as white
	 * 
	 * @param list
	 */
	public void removeFalseWhite(List<Ball> list, Ball whiteBall, int radius) {
		if (whiteBall == null)
			return;
		Ball inList = math.findBallByPoint(list, whiteBall.getPoint(), radius);
		if (inList == null)
			return;
		list.remove(inList);
	}

}
