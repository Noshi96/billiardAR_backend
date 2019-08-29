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


	// konstruktor - wersja na kinecta
	public BilliardTable() {
		balls = new ArrayList<>();
		pockets = new ArrayList<Pocket>();
	}



//	// konstruktor - wersja mocnk (noszek)
//     public BilliardTable() {
//         balls = new ArrayList<>();
//         balls.add(new Ball(0, new Point(1000,800)));
//         balls.add(new Ball(1, new Point(250,600)));
//         balls.add(new Ball(2, new Point(400,29) ));
//
//         balls.add(new Ball(4, new Point(417,629) ));
//         balls.add(new Ball(5, new Point(600,450)));
//         balls.add(new Ball(6, new Point(1,600))); 
//         balls.add(new Ball(7, new Point(610,250)));
//         balls.add(new Ball(8, new Point(476, 186)));
//         balls.add(new Ball(9, new Point(600, 50)));
//
//         this.setWhiteBall(balls.get(3));
//         //this.setWhiteBall( new Ball( 0,  new Point(750,450)));
//         
//         //this.setSelectedBall(this.balls.get(7));
//         //this.setSelectedPocket(this.pockets.get(5));
//     }

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
