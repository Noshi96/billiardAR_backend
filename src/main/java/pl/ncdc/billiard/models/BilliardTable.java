package pl.ncdc.billiard.models;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.ncdc.billiard.models.NewPoint;

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

	// dodane przez Koala, przydatne do fronta
	// view mode- okresla wyswietlanie zawartosci na stronie
	// apka: 0 - pokazuej same bile, 1 - tryb pasywny 1, rysuje bile, rysuje
	// zaznaczenia oraz trajektorie
	// display: 0 - rysuje okregi wokol wszystkich bil, odpowiednio oznacza biala, 1
	// - obrysowuje tylko biala bile, zaznaczona bile oraz zaznaczona luze i rysuje
	// trajektorie
	private int selectedViewMode = 32;

	// okresla wybranie poziomu challange. 0 - zaden, normalna ryzgrywka i
	// zczytywanie bil; 0<int - okresla id poziomu do zaladowania
	private int selectedChallenge = 0;



    // odwrocone bandy
    public BilliardTable() {
        //balls = new ArrayList<>();
//		pockets = new ArrayList<Pocket>();
//		pockets.add(new Pocket(0, new Point(0, 620)));
//		pockets.add(new Pocket(1, new Point(1190 / 2, 620)));
//		pockets.add(new Pocket(2, new Point(1190, 620)));
//		pockets.add(new Pocket(3, new Point(0, 0)));
//		pockets.add(new Pocket(4, new Point(1190 / 2, 0)));
//		pockets.add(new Pocket(5, new Point(1190, 0)));

        balls = new ArrayList<>();
        balls.add(new Ball(0, new Point(1000,800))); // ostatnie true- jest biala

        balls.add(new Ball(2, new Point(400,29) ));

        balls.add(new Ball(4, new Point(417,29) ));
        balls.add(new Ball(5, new Point(850,100)));	// White ball WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
        
        //balls.add(new Ball(6, new Point(800,450)));
        //balls.add(new Ball(2, false, new Point(111,99), false));
        //balls.add(new Ball(2, false, new Point(111,99), false));
        balls.add(new Ball(3, new Point(750,450)));
        balls.add(new Ball(1, new Point(250,600))); // true - jest selected
        
        balls.add(new Ball(6, new Point(1,600))); // 
        
        //Dximi - kat nie potej stronie
        balls.add(new Ball(7, new Point(600,250)));// SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSelected
        
        this.setWhiteBall(balls.get(3));
        this.setSelectedBall(this.balls.get(7));
        //this.setSelectedPocket(this.pockets.get(5));
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
