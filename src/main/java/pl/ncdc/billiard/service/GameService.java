package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.StopWatch;
import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.Gamer;
import pl.ncdc.billiard.service.IndividualTrainingGameModeService.State;


@Service
public class GameService {

	@Autowired
	private BilliardTable table;
	
    @Autowired
    private CalibrationService calibrationService;
    
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    
    @Getter
    private GameState state;
	
    private double waitingForBallsPlacementDelay = 2.0;
    private double ballsStopMovingDelay = 2.0;
    private double afterEndDelay = 2.0;
    private double waitingForAllBallsOnRightSide = 5.0;
    private boolean startGame = true;
    
    private StopWatch stopWatch = new StopWatch();
    
    private List<Point> lastFrameBallsPositions;
    
    private List<Point> currentFrameBallsPositions;
    
    @Getter
	private List<Gamer> listOfAllGamersFromService;
	
	private List<Ball> listWithGameBalls;
	
	@Getter
	@Setter
	private int playerCount = 3;
	
	public void start(int playerCount) {
		
		listOfAllGamersFromService = setPositionsForTracksAndIdForNewGamers(playerCount);
		lvlGenerator(listOfAllGamersFromService);
		setPositionsForCheckingCirclesGamer(positionsForCheckingCircles(), listOfAllGamersFromService);
		setPositionToShowScoresToPlayers(positionToShowScore(playerCount),listOfAllGamersFromService);
		
		state = GameState.WaitingForCheckingBallsPlacement; 
		if (stopWatch.isStarted()) {
			stopWatch.reset();
		}
		stopWatch.start();
	}
	
	@Scheduled(fixedRate = 400)
	public void update() {
		// set startwatch
				
		if (startGame) {
			start(playerCount);
			startGame = false;
			System.out.println("0");
		}
		
        if(table.getBalls() == null) {
        	currentFrameBallsPositions = new ArrayList<>();
            listWithGameBalls = new ArrayList<>();
            System.out.println("1");
        } else {
        	currentFrameBallsPositions = table.getBalls().stream().map(ball -> new Point(ball.getPoint().x, ball.getPoint().y)).collect(Collectors.toList());
            
        	listWithGameBalls = new ArrayList<>(table.getBalls());
        	
            System.out.println("2");
        }
        if(table.getWhiteBall() != null) {
        	currentFrameBallsPositions.add(table.getWhiteBall().getPoint());
        	listWithGameBalls.add(table.getWhiteBall());
        	System.out.println("3");
        	//System.out.println("listWithGameBalls = " + listWithGameBalls);
        	//System.out.println("listWithGameBallsFromTable = " + table.getBalls());
        }
		
		
		if (state == GameState.WaitingForCheckingBallsPlacement) {
			System.out.println("4");
			if (!isAllBallsPlacedCorrectly(playerCount)) {
				stopWatch.reset();
				stopWatch.start();
				System.out.println("5");
				System.out.println(listWithGameBalls);
			}
			
			if (stopWatch.getTime(TimeUnit.SECONDS) > waitingForBallsPlacementDelay) {
				stopWatch.reset();
				
				lvlGenerator(listOfAllGamersFromService);
				state = GameState.ReadyToPlay;
				System.out.println("6");
			}
		} else if (state == GameState.ReadyToPlay) {
			System.out.println("7"); // Tu mi wchodzi
//			listWithGameBalls = new ArrayList<>();
//			listWithGameBalls.add(new Ball(0, new Point(800,200)));
//			listWithGameBalls.add(new Ball(1, new Point(789,300)));
//			listWithGameBalls.add(new Ball(2, new Point(779,450)));
//			table.setBalls(listWithGameBalls);
			
			if (!isAllBallsPlacedCorrectly(playerCount)) { // Zwraca true
				state = GameState.WaitingForStopBalls;
				System.out.println("8"); // Tu nie wchodzi
			}
		} else if (state == GameState.WaitingForStopBalls) {
			System.out.println("9");
			if (doesBallsStopMoving()) {
				System.out.println("10");
				if (stopWatch.getTime(TimeUnit.SECONDS) > ballsStopMovingDelay) {
					System.out.println("11");
					// Juz sie zatrzymaly po rzucie
					if (!areThereAnyBallsInTheStartingAreae(listWithGameBalls)) {
						System.out.println("12");
						state = GameState.WaitingForSetScoreAndGenerateNewLvl;

						//setScoreAfterRound(listOfAllGamersFromService, table.getBalls()); // czy to tu? 
						
					} else {
						System.out.println("13");
						state = GameState.WaitingForAllBallsOnRightSide;
					}
					System.out.println("14");
					//stopWatch.reset();
					//stopWatch.start();
				}
				
				if (state == GameState.WaitingForSetScoreAndGenerateNewLvl) {
					System.out.println("15");
					//if (stopWatch.getTime(TimeUnit.SECONDS) > waitingForAllBallsOnRightSide) { //
						System.out.println("16");
						setScoreAfterRound(listOfAllGamersFromService, listWithGameBalls); // czy to tu?
						
						for (Gamer gamer : listOfAllGamersFromService) {
							System.out.println("gamer.getLineX() = " + gamer.getLineX());
							System.out.println("gamer.getCurrentScore() = " + gamer.getCurrentScore());
							System.out.println(gamer);
						}
						
						stopWatch.reset();
						stopWatch.start();
					//}
				}
				
				
				
			} else {
				System.out.println("17");
				stopWatch.reset();
				stopWatch.start();
			}
			
		} else if (state == GameState.WaitingForSetScoreAndGenerateNewLvl) {
			System.out.println("18");
			if (stopWatch.getTime(TimeUnit.SECONDS) > afterEndDelay) {
				System.out.println("19");
				state = GameState.WaitingForCheckingBallsPlacement;
			}
		}
		

        lastFrameBallsPositions = currentFrameBallsPositions;
        
        //simpMessagingTemplate.convertAndSend("/game/state", state);
	}
	
	
//	// Zwraca punkty z ktorych beda rysowane tory, ilosc torow jest zalezna od ilosci graczy, pojedynczy gracz ma indywidualny tor 
//	// Punkty te sa na osi Y
	public List<Double> positionsForTracks(){
		
		double temp = 0;
		List<Double> trackPositionY = new ArrayList<>();
		
		if (playerCount == 0 || playerCount < 0) {
			return null;
		}
		if (playerCount == 1) {
			temp = table.getHeight() / 3;
			
			for (int i = 0; i < 3; i++) {			
				trackPositionY.add((temp*(i+1)));		
			}
		} else {
			temp = table.getHeight() / playerCount;
			
			for (int i = 0; i < playerCount; i++) {
				trackPositionY.add((temp * (i + 1)));		
			}
		}
		return trackPositionY;
	}
	
	// Zwraca punkty z ktorych beda rysowane tory, ilosc torow jest zalezna od ilosci graczy, pojedynczy gracz ma indywidualny tor 
	// Punkty te sa na osi Y
	// Tworzy nowych graczy z okreslonymi torami oraz idkami
	public List<Gamer> setPositionsForTracksAndIdForNewGamers(int playerCount){
		
		double temp = 0;
		List<Gamer> gamersList = new ArrayList<>();
		
		if (playerCount == 0 || playerCount < 0) {
			return null;
		}
		if (playerCount == 1) {
			Gamer gamer = new Gamer();
			gamer.setId(playerCount);
			temp = table.getHeight() / 3;
			
			for (int i = 0; i < 3; i++) {			
				if (i == 0) {
					gamer.setLeftOwnBand((temp*(i+1)));
				} else if (i == 1) {
					gamer.setRightOwnBand((temp*(i+1)));
				}
			}
			gamersList.add(gamer);
			
		} else {
			temp = table.getHeight() / playerCount;
			
			for (int i = 0; i < playerCount; i++) {
				Gamer gamer = new Gamer();
				gamer.setId(i+1);
				
				if (gamer.getId() == 1) {
					gamer.setLeftOwnBand(0);
				}
				
				gamer.setLeftOwnBand((temp * (i)));
				gamer.setRightOwnBand((temp * (i+1)));
				
//				if (i % 2 == 0) {
//					if (i == 0) {
//						gamer.setLeftOwnBand(0);
//						System.out.println("Setuje Left banda dla i = " + i + " Band = 0");
//					} else {
//						gamer.setLeftOwnBand((temp * (i + 1)));
//						System.out.println("Setuje Left banda dla i = " + i + " Band = " + (temp * (i + 1)));
//					}
//
//				} else if (i % 2 == 1) {
//
//					if (i == playerCount - 1) {
//						gamer.setRightOwnBand(table.getHeight());
//						System.out.println("Setuje Right banda dla i = " + i + " Band = " + table.getHeight());
//					} else {
//						gamer.setRightOwnBand((temp * (i + 1)));
//						System.out.println("Setuje Right banda dla i = " + i + " Band = " + (temp * (i + 1)));
//					}
//				}
				// Dla ostatniej iteracji ustawia dodatkowo ostania prawa bande czyli w sumie table.height prawa bande

				gamersList.add(gamer);
			}
		}
		return gamersList;
	}
	
	
	// Zwraca pozycje na jakich maja sie znalezc okregi, ktore beda wykorzystywane do weryfikacji gotowosci rozpoczecia poziomu
	public List<Point> positionsForCheckingCircles(){
		
		double temp = 0;
		List<Point> circlesPositions = new ArrayList<>();
		
		if (playerCount == 0 || playerCount < 0) {
			return null;
		}
			
		if (playerCount == 1) { // id = 3?
			temp = table.getHeight() / 2;
			

			circlesPositions.add(new Point(table.getWidth() / 8, temp));	
			
		} else {
			temp = table.getHeight() / ((playerCount * 2));
			
			for (int i = 0; i < playerCount * 2; i=i+2) {		
				circlesPositions.add(new Point(table.getWidth() / 8, (temp * (i + 1))));			
			}
		}
		return circlesPositions;
	}
	
	public void setPositionsForCheckingCirclesGamer(List<Point> positionsChechkingCircles, List<Gamer> gamersList) {
		
		System.out.println();
		for ( int i = 0; i < positionsChechkingCircles.size(); i++ ) {
			gamersList.get(i).setStartCheckingPoint(positionsChechkingCircles.get(i));
		}
	}
	
	public List<Point> positionToShowScore(int playerCount){
		
		double temp = 0;
		List<Point> circlesPositions = new ArrayList<>();
		
		if (playerCount == 0 || playerCount < 0) {
			return null;
		}
			
		if (playerCount == 1) { // id = 3?
			temp = table.getHeight() / ((playerCount * 2));
			
			for (int i = 0; i < 6; i=i+2) {		
				circlesPositions.add(new Point(table.getWidth()*3 / 8, (temp * (i + 1)) ));			
			}
		} else {
			temp = table.getHeight() / ((playerCount * 2));
			
			for (int i = 0; i < playerCount * 2; i=i+2) {		
				circlesPositions.add(new Point(table.getWidth()*3 / 8, (temp * (i + 1)) ));			
			}
		}
		return circlesPositions;
	}
	
	public void setPositionToShowScoresToPlayers(List<Point> positionToShowScoreList, List<Gamer> listWithGamers) {

		if (positionToShowScoreList != null && listWithGamers != null) {
		for (int i = 0; i < listWithGamers.size(); i++) {
			listWithGamers.get(i).setPosShowScore(positionToShowScoreList.get(i));
			}
		}
	}
	
	public Point positionToShowMessages() {
		double x = 0, y = 0;
		
		x = table.getWidth() / 2;
		y = table.getHeight() / 2;
		
		return new Point(x,y);
	}
	
	
	// Funkcja zwraca pozycje X na ktorym bedzie narysowana linia do ktorej nalezy toczyc bile [linia rysuje sie miedzy rotami]
	public double lineLvlEasyRandomX() {
		
		double lineX = (Math.random() *  ((table.getWidth() - (table.getWidth() / 2)) + 1 )) + (table.getWidth() / 2);	
		return lineX;
	}
	
	// Funkcja zwraca randomowy punkt dla poziomu sredniego, przyczym Y = polowie jednego toru [okrag jest na srodku toru]
	// Gamer musi juz istniec i miec dane zanim bedziemy liczyc dla niego lvle
	public Point pointLvlMediumRandom(Gamer gamer) {
		
		Point mediumLvlPoint = new Point();	
		double pointX = 0, pointY = 0;
			
		pointX = (Math.random() *  ((table.getWidth() - (table.getWidth() / 2)) + 1 )) + (table.getWidth() / 2);
		pointY = gamer.getStartCheckingPoint().y;
		
		mediumLvlPoint.x = pointX;
		mediumLvlPoint.y = pointY;
		
		return mediumLvlPoint;
	}
	
	// Funkcja zwraca punkt ktory miesci sie w randomowej odleglosci miedzy dwiema liniami toru
	public Point pointLvlHardRandom(Gamer gamer) {
		
		Point hardLvlPoint = new Point();	
		double pointX = 0, pointY = 0;
		
		pointX = (Math.random() *  ((table.getWidth() - (table.getWidth() / 2)) + 1 )) + (table.getWidth() / 2);
		pointY = (Math.random() *  ((gamer.getRightOwnBand() - gamer.getLeftOwnBand()) + 1 )) + (gamer.getLeftOwnBand());
		
		hardLvlPoint.x = pointX;
		hardLvlPoint.y = pointY;
		
		return hardLvlPoint;
	}
	
	// Odleglosc punktu do lini
	public double lengthBallToLine(double lineX, Point ball) {
		
		Point pointOnLine = new Point();
		pointOnLine.x = lineX;
		pointOnLine.y = ball.y;
			
		return lengthBetweenPoints(pointOnLine, ball);
	}
	
	// Odleglosc jedngo punktu od drugiego [bili od punktu]
	public double lengthBetweenPoints (Point first, Point second) {
		return Math.sqrt(Math.pow(second.x - first.x, 2) + Math.pow(second.y - first.y, 2));
	}
		
	// Funkcja sprawdza czy znajduja sie jakies bile po stronie z ktorej je zagrywamy
	// Funkcja wywoluje sie po tym jak juz bile zatrzymaja sie i ustabilizuja po drugiej stronie stolu
	// Mowi nam czy mozemy juz zaczac liczyc punkty czy musmy czekac az ktos jeszcze dorzuci pilke
	public boolean areThereAnyBallsInTheStartingAreae(List<Ball> listOfBalls) {
		System.out.println(listOfBalls);
		for (Ball ball : listOfBalls) {
			if (ball.getPoint().x < table.getWidth() / 2 ) {
				System.out.println("EEEEtable.getWidth() / 2 = " + table.getWidth() / 2 );
				System.out.println("EEEEball.getPoint().x = " + ball.getPoint().x);
				return true;
			}
			System.out.println("table.getWidth() / 2 = " + table.getWidth() / 2 );
			System.out.println("ball.getPoint().x = " + ball.getPoint().x);
		}		
		return false;
	}
	
	// 
	public boolean areThereMoreThenOneBallOnOneTrack(List<Ball> listOfBalls, List<Gamer> gamerList) {
		
		int counter = 0;
		if (listOfBalls != null && gamerList != null) {
		// przelatuje po kazdym graczu i patrzy dla jego torow czy nie znajduje sie w jego obrebie wiecej niz jedna bila
			for (Gamer gamer : gamerList) {		
				for (Ball ball : listOfBalls) {
					if ( (ball.getPoint().x > table.getWidth() / 2) && (ball.getPoint().y > gamer.getLeftOwnBand()) && (ball.getPoint().y < gamer.getRightOwnBand()) ) {
						counter++;
					}
				}
				
				if (counter > 1) {
					return true;
				}
				
				counter = 0;
			}
		}
		return false;
	}
	
	// Znajduje wszsytkie bile ktore znajduja sie na danym torze
	public List<Ball> findingBallsOnTheRightTrack(Gamer gamer, List<Ball> listOfBalls) {
		
		boolean flag = false;
		List<Ball> listWithBallsInRightTrack = new ArrayList<>();
		for (Ball ball : listOfBalls) {
			if ( (ball.getPoint().x > table.getWidth() / 2) && (ball.getPoint().y > gamer.getLeftOwnBand()) && (ball.getPoint().y < gamer.getRightOwnBand()) ) {
				flag = true;
			}
			if (flag) {
				listWithBallsInRightTrack.add(ball);
				flag = false;
			}
		}
		
		return listWithBallsInRightTrack;
	}
	
	// Funkcja zwraca liste graczy na ktorych torach nie znajduja sie bile, zeby potem moc im odjac punkty
	public List<Gamer> gamerTracksWithoutBalls(List<Ball> listOfBalls, List<Gamer> gamerList){
		
		if (listOfBalls != null && gamerList != null) {
			List<Gamer> listWithGamersWithoutBallsOnTrack = new ArrayList<>();
			int counter = 0;
			for (Gamer gamer : gamerList) {		
				for (Ball ball : listOfBalls) {
					if ( (ball.getPoint().x > table.getWidth() / 2) && (ball.getPoint().y > gamer.getLeftOwnBand()) && (ball.getPoint().y < gamer.getRightOwnBand()) ) {
						counter++;
						System.out.println( "Gammer w petli = " + gamer);
					}
				}
				
				if (counter == 0) {
					listWithGamersWithoutBallsOnTrack.add(gamer);
				}
				
				counter = 0;
			}
			return listWithGamersWithoutBallsOnTrack;	// moze byc NULLEM
		}
		return null;
	}
	
	// Wyznacza gracza ktory dostanie punkt bo d³ugoœæ jego bili do punktu jest najmniejsza
	public Gamer bestGamerInOneRound(List<Gamer> listOfGamers, List<Ball> listOfBalls) {
		
		double length = 0;
		HashMap<Integer, Double> hmWithIdAndLength = new HashMap<Integer, Double>();
		
		if (listOfGamers != null) {
		
			for (Gamer gamer : listOfGamers) {
				// Dla kazdego gracza porownywane sa odleglosci tylko z bilami na jego torze 
				List<Ball> ballsOnTheRightTrackList = new ArrayList<>(findingBallsOnTheRightTrack(gamer, listOfBalls));
				System.out.println("ballsOnTheRightTrackList = " + ballsOnTheRightTrackList );
				length = 0;
				
				if (ballsOnTheRightTrackList != null && ballsOnTheRightTrackList.size() > 0) {
				
				if (gamer.isBetterThenOthers()) {
					for (Ball ball : ballsOnTheRightTrackList) {
						// Znajduje najmniejsza odleglosc jesli na torze sa np dwie bile tak zeby zawsze szukalo z najkrotsza
						if (length > lengthBetweenPoints(gamer.getHardLvlPoint(), ball.getPoint()) || length == 0){
							length = lengthBetweenPoints(gamer.getHardLvlPoint(), ball.getPoint());
						}
					}	
				} 
				
				else if (gamer.getCurrentScore() < 5) {				
					for (Ball ball : ballsOnTheRightTrackList) {
						// Znajduje najmniejsza odleglosc jesli na torze sa np dwie bile tak zeby zawsze szukalo z najkrotsza
						if (length > lengthBallToLine(gamer.getLineX(), ball.getPoint()) || length == 0){
							length = lengthBallToLine(gamer.getLineX(), ball.getPoint());
						}
					}															
				}
				
				else if (gamer.getCurrentScore() >= 5 && gamer.getCurrentScore() < 10) {					
					for (Ball ball : ballsOnTheRightTrackList) {
						// Znajduje najmniejsza odleglosc jesli na torze sa np dwie bile tak zeby zawsze szukalo z najkrotsza
						if (length > lengthBetweenPoints(gamer.getMediumLvlPoint(), ball.getPoint()) || length == 0){
							length = lengthBetweenPoints(gamer.getMediumLvlPoint(), ball.getPoint());
						}
					}					
				}
				
				else if (gamer.getCurrentScore() >= 10) {					
					for (Ball ball : ballsOnTheRightTrackList) {
						// Znajduje najmniejsza odleglosc jesli na torze sa np dwie bile tak zeby zawsze szukalo z najkrotsza
						if (length > lengthBetweenPoints(gamer.getHardLvlPoint(), ball.getPoint()) || length == 0){
							length = lengthBetweenPoints(gamer.getHardLvlPoint(), ball.getPoint());
						}
					}					
				}			
				hmWithIdAndLength.put(gamer.getId(), length);
				}
			}
			
			Entry<Integer, Double> min = null;
			for (Entry<Integer, Double> entry : hmWithIdAndLength.entrySet()) {
			    if (min == null || min.getValue() > entry.getValue()) {
			        min = entry;
			    }
			}
					
			for (Gamer gamer : listOfGamers) {
				if (min.getKey().equals(gamer.getId())) {
					return gamer;
				}
			}
			
		}
		return null;
	}
	
	// dodaje jeden punkt graczowi
	public void giveWinPoint(Gamer gamer) {
		if (gamer != null)
			gamer.setCurrentScore(gamer.getCurrentScore() + 1);
	}
	
	// Odejmuje jeden punkt graczowi
	public void takeLosePoint(Gamer gamer) {
		if (gamer != null)
			gamer.setCurrentScore(gamer.getCurrentScore() - 1);
	}
	
	
	// Wywo³uje sie na poczatku kazdej rundy
	public void lvlGenerator(List<Gamer> listOfGamers) {
		
		boolean sameLvlForAll = true;
		List<Integer> listWithAllScores = new ArrayList<>();
		double easyLvlForAll = 0;
		
		easyLvlForAll = lineLvlEasyRandomX();
		
		for (Gamer gamer : listOfGamers) {
			listWithAllScores.add(gamer.getCurrentScore());
		}
		Collections.sort(listWithAllScores);
		
		if (listWithAllScores.size() > 1) {
			if (listWithAllScores.get(listWithAllScores.size() - 1) - listWithAllScores.get(listWithAllScores.size() - 2) >= 4) {
				for (Gamer gamer : listOfGamers) {
					if (listWithAllScores.get(listWithAllScores.size() - 1).equals(gamer.getCurrentScore())) {
						gamer.setBetterThenOthers(true);
						sameLvlForAll = false;
					}
				}
			}
		}
		
		for (Gamer gamer : listOfGamers) {
			if (gamer.isBetterThenOthers() || gamer.getCurrentScore() > 9) {
				gamer.setHardLvlPoint(pointLvlHardRandom(gamer));
				
			} else if (gamer.getCurrentScore() >= 5 && gamer.getCurrentScore() <= 9) {
				gamer.setMediumLvlPoint(pointLvlMediumRandom(gamer));
				sameLvlForAll = false;
			}			
			
			if (sameLvlForAll) {
				gamer.setLineX(easyLvlForAll);
			} else if (gamer.getCurrentScore() < 5) {
				gamer.setLineX(lineLvlEasyRandomX());
			}
		}
	}
	
	
	// Dodaje punkt najlepszemu, odejmuje tym co ich bila nie znajduje sie na ich torze, pozostali nie dostaja punktow
	public void setScoreAfterRound(List<Gamer> listOfGamers, List<Ball> listOfBalls) {
		
		List<Gamer> loseGamersList = new ArrayList<>();
		
		Gamer bestGamerInRound = bestGamerInOneRound(listOfGamers, listOfBalls);	
		giveWinPoint(bestGamerInRound);
		
		loseGamersList = new ArrayList<>(gamerTracksWithoutBalls(listOfBalls, listOfGamers));
		
		for (Gamer gamer : loseGamersList) {
			takeLosePoint(gamer);
			System.out.println("Jemu powinno odjac punkt ale moze ma 0 i nie idejmuje od zera =" + gamer );
		}
		
	}
	
    private double getBallPositionTolerance() {
        return calibrationService.getCalibrationParams().getBallDiameter() / 4.0;
    }
    
    
    // Nie ma byc bialej ??
    private boolean isSomethingOnPoint(Point point) {
        if(listWithGameBalls == null) {
            return false;
        }
        
        if(table.getWhiteBall() != null) {
        	if(distance(point, table.getWhiteBall().getPoint()) < getBallPositionTolerance()) {
        		return true;
        	}
        }
        
        return table.getBalls()
                .stream()
                .anyMatch(ball -> distance(point, ball.getPoint()) < getBallPositionTolerance());
    }
    
    private double distance(Point point, Point point2) {
        return Math.sqrt(Math.pow(point.x - point2.x, 2) + Math.pow(point.y - point2.y, 2));
    }
    
    private boolean doesBallsStopMoving() {
        if(lastFrameBallsPositions.stream().allMatch(this::isSomethingOnPoint)) {
        	return true;
        }
        return false;
    }
    
    // Zwraca true kiedy cos jest w okregach
    private boolean isAllBallsPlacedCorrectly(int playerCount) {
        if(!positionsForCheckingCircles().stream().allMatch(this::isSomethingOnPoint)) {
            return false;
        }

        return true;
    }
	
	public enum GameState{
		WaitingForCheckingBallsPlacement,
		ReadyToPlay,
		WaitingForStopBalls,
		WaitingForAllBallsOnRightSide,
		WaitingForSetScoreAndGenerateNewLvl,
	}
	
	
}
