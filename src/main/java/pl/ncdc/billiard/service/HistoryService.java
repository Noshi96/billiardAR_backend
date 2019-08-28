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
	/** List of previously detected white balls **/
	private List<Ball> whiteHistory;
	/** How many previous states will be stored **/
	private final static int maxHistoryLength = 120;
	/** How much current position should change new calculated position **/
	private final static double corretionRate = 0.05;
	/** Number of frames to search disappearing balls **/
	private final static int historyScanLimit = 20;

	public HistoryService() {
		this.history = new ArrayList<List<Ball>>();
		this.whiteHistory = new ArrayList<Ball>();
	}

	/**
	 * Try to calculate new position of white ball;
	 * 
	 * @param whiteBall
	 * @param radius
	 * @return new position of white ball
	 */
	public void updateHistory(Ball whiteBall, int radius) {

		if (whiteBall == null)
			if (this.whiteHistory.size() == 0)
				return;
			else {
				this.whiteHistory.add(whiteBall);
				whiteBall = this.whiteHistory.get(this.whiteHistory.size() - 2);
				return;
			}

		Point avg = new Point();
		int detected = 0;

		for (Ball ball : this.whiteHistory) {
			if (ball != null) {
				avg.x += ball.getPoint().x;
				avg.y += ball.getPoint().y;
				detected++;
			}
		}
		if (detected == 0)
			return;

		avg.x /= (double) detected;
		avg.y /= (double) detected;

		double dx = whiteBall.getPoint().x - avg.x;
		double dy = whiteBall.getPoint().y - avg.y;

		if (Math.abs(dx) < radius / 2)
			whiteBall.getPoint().x = avg.x;
		else if (Math.abs(dx) < radius)
			whiteBall.getPoint().x = avg.x + dx * corretionRate * 2;

		if (Math.abs(dy) < radius / 2)
			whiteBall.getPoint().y = avg.y;
		else if (Math.abs(dy) < radius)
			whiteBall.getPoint().y = avg.y + dy * corretionRate * 2;

		this.whiteHistory.add(whiteBall);
		if (this.whiteHistory.size() > maxHistoryLength)
			this.whiteHistory.remove(0);
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

				Ball ball2 = findBallByPoint(ball.getPoint(), prev, radius);
				if (ball2 != null) {
					avg.x += ball2.getPoint().x;
					avg.y += ball2.getPoint().y;
					detected++;
				}
			}

			if (detected == 0)
				break;

			avg.x /= (double) detected;
			avg.y /= (double) detected;

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
					if (count > historyScanLimit * 2 / 3)
						list.add(ball);
				}
			}

		}
		return list;
	}

	/**
	 * Try to find Ball inside the list
	 * 
	 * @param point
	 * @param list
	 * @param radius
	 * @return
	 */
	public Ball findBallByPoint(Point point, List<Ball> list, int radius) {
		for (Ball ball : list) {
			if (Math.abs(point.x - ball.getPoint().x) < radius && Math.abs(point.y - ball.getPoint().y) < radius)
				return ball;
		}
		return null;
	}

	/**
	 * Remove ball from list if detected in less than 1/3 frames of historyScanLimit
	 * frames
	 * 
	 * @param list
	 * @param maxBallRadius
	 */
	public void removeFalseBalls(List<Ball> list, int maxBallRadius) {
		int lastIndex = this.history.size() - historyScanLimit;
		if (lastIndex < 0)
			lastIndex = 0;

		List<Ball> newList = new ArrayList<Ball>();

		for (Ball ball : list) {
			int count = 0;
			for (int index = lastIndex; index < this.history.size(); index++)
				if (findBallByPoint(ball.getPoint(), this.history.get(index), maxBallRadius) != null)
					count++;
			if (count < historyScanLimit / 2)
				newList.add(ball);
		}
		list.removeAll(newList);
	}

}
