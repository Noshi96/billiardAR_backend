package pl.ncdc.billiard.models;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;

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

	private int ballRadius;

	private List<Point> disturbPoints;

	/**
	 * dodane przez Koala, przydatne do fronta<p>view mode- okresla wyswietlanie
	 * zawartosci na stronie</p><p> apka: 0 - pokazuej same bile, 1 - tryb pasywny 1,
	 * rysuje bile, rysuje <p>zaznaczenia oraz trajektorie </p> display: 0 - rysuje
	 * okregi wokol wszystkich bil, odpowiednio oznacza biala, 1 - obrysowuje
	 * tylko biala bile, zaznaczona bile oraz zaznaczona luze i rysuje trajektorie
	 **/
	private int selectedViewMode = 0;

	// okresla wybranie poziomu challange. 0 - zaden, normalna ryzgrywka i
	// zczytywanie bil; 0<int - okresla id poziomu do zaladowania
	private int selectedChallenge = 0;

	public BilliardTable() {
		balls = new ArrayList<>();
		pockets = new ArrayList<Pocket>();
	}

	public int getSelectedViewMode() {
		return selectedViewMode;
	}

	public void setSelectedViewMode(int selectedViewMode) {
		this.selectedViewMode = selectedViewMode;
	}

	public int getSelectedChallenge() {
		return selectedChallenge;
	}

	public void setSelectedChallenge(int selectedChallenge) {
		this.selectedChallenge = selectedChallenge;
	}

	/**
	 * Search in Ball collection and try to find correct match
	 * 
	 * @param point
	 * @return {@code Point} position of ball.</br>
	 *         Null if not found
	 */
	public Point findBallByPoint(Point point) {
		for (Ball ball : this.balls) {
			if (Math.abs(ball.getPoint().x - point.x) < this.ballRadius
					&& Math.abs(ball.getPoint().y - point.y) < this.ballRadius)
				return ball.getPoint();
		}
		return null;
	}
}
