package pl.ncdc.billiard.service;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import pl.ncdc.billiard.models.*;
import pl.ncdc.billiard.models.training.HitPointHint;
import pl.ncdc.billiard.models.training.HitPowerHint;
import pl.ncdc.billiard.models.training.Training;
import pl.ncdc.billiard.models.training.TargetBallHitPointHint;
import pl.ncdc.billiard.service.training.TrainingModeService;
import pl.ncdc.billiard.service.training.TrainingService;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import pl.ncdc.billiard.controllers.BilliardTableController;
import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.CalibrationParams;
import pl.ncdc.billiard.models.Gamer;
import pl.ncdc.billiard.models.IndividualTraining;
import pl.ncdc.billiard.models.Informations;
import pl.ncdc.billiard.models.Pocket;
import pl.ncdc.billiard.models.trainingHints.HitPoint;
import pl.ncdc.billiard.models.trainingHints.HitPointHint;
import pl.ncdc.billiard.models.trainingHints.HitPowerHint;
import pl.ncdc.billiard.models.trainingHints.TargetBallHitPointHint;
import pl.ncdc.billiard.service.IndividualTrainingGameModeService.State;

import static org.opencv.core.Core.FILLED;

@Service
public class PoolDrawerService implements ApplicationListener<PoolDrawerParamsService.PoolDrawerParamsUpdatedEvent> {

	
	@Autowired
	HitService hitService;
	
	@Autowired
	HiddenPlacesService hiddenPlacesService;
	
	@Autowired
	TrainingService trainingService;
	
	@Autowired
	TrainingModeService trainingModeService;
	
	@Autowired
	RotationService rotationService;
	
	@Autowired
	InformationsService informationsService;
	
	@Autowired
	GameService gameService;

	@Autowired
	BilliardTableService tableService;

	@Value("${kinectService.mask}")
	private String filename;
	
	
	
	//zmienne do jakiegos pliku
	int kinectHeight = 600;
	int kinectWidth = 1200;
	
	boolean displayBallId = false;
	boolean displayPockets = false;
	boolean displayShotInformations = true;

	int projectorMaxHeight = 1080-1;
	int projectorMaxWidth = 1920;
	
	int textXD = 30;

	int pocketRadius = 50;

	int pocketLineThickness = 5;
	int playZoneBorderThickness = 1;
	int selectedPocketLineThickness = 4;
	// koniec zmiennych do pliku
	
	List<Point> hitPoints;

	CalibrationParams calibrationParams = CalibrationParams.getDefaultCalibrationParams();
	Informations informations;

	private PoolDrawerParams poolDrawerParams = new PoolDrawerParams();
	private Scalar whiteColor = new Scalar(255, 255, 255);

	@Override
	public void onApplicationEvent(PoolDrawerParamsService.PoolDrawerParamsUpdatedEvent event) {
		poolDrawerParams = event.getPoolDrawerParams();
	}

	public byte[] drawImage(BilliardTable table)
	{
		kinectHeight = table.getHeight();
		kinectWidth = table.getWidth();
		informations = informationsService.getHitInformations();



		//ballRadius = table.getBallRadius();	
		//Mat poolTableArea = new Mat(projectorMaxHeight,projectorMaxWidth, CvType.CV_8UC3);
		//Mat poolPlayZoneMat = new Mat(table.getHeight(),table.getWidth(), CvType.CV_8UC3);
		
		//kinectHeight = table.getHeight();
		//kinectWidth = table.getWidth();
		
		
		Mat poolPlayZoneMat = new Mat(kinectHeight,kinectWidth, CvType.CV_8UC3);
		drawPlayZoneBorder(poolPlayZoneMat);
		
		
		if(table.getSelectedChallenge() == 0){
			// rysuje tryby tylko jesli nie ma zaznaczonego challenge'u
			switch(table.getSelectedViewMode()){
				case 0: drawViewMode0(poolPlayZoneMat, table); break;
				case 1: drawViewMode1(poolPlayZoneMat, table); break;
				case 22: drawViewMode22(poolPlayZoneMat, table); break;
				case 32: drawViewModeRotation(poolPlayZoneMat, table); break;
				case 33: drawViewModeZeroRotation(poolPlayZoneMat, table); break;
				case 34: drawViewModeBestPocket(poolPlayZoneMat, table); break;		// Nowy poprawiony z rysowaniem od razu po wyborze bili selected
				case 35 :drawViewModeBothRotation(poolPlayZoneMat, table); break;	// Wspolne rysowanie rotacji postepowej i bez rotacji
				case 36 :drawViewModeProMode(poolPlayZoneMat, table); break;	// Tryb pro
				case 37 :drawViewModeGameBoard(poolPlayZoneMat, gameService, table);
				//case 2: this.drawViewMode2(); break;
			}
		}
		else{
			drawTraining(poolPlayZoneMat, trainingService.getInPixelById(((long) table.getSelectedChallenge())), table.getPockets());
			// tryb challenge'u zostal wybrany.
			// this.fetchTrainingById(table.selectedChallenge);
		}
		
		
		if ( displayShotInformations ) {
			drawShotInformation(poolPlayZoneMat, informations);
		}else {
			//System.out.println("display informations: false");
		}
		
		

	    MatOfPoint2f sourceMat = new MatOfPoint2f(
    		new Point(0,0),
    		new Point(kinectWidth, 0),
    		new Point(kinectWidth, kinectHeight),
    		new Point(0, kinectHeight)
		);
	    
	    MatOfPoint2f destinationMat = new MatOfPoint2f(
    		calibrationParams.getLeftUpperCornerProjector(),
    		calibrationParams.getRightUpperCornerProjector(),
    		calibrationParams.getRightBottomCornerProjector(),
    		calibrationParams.getLeftBottomCornerProjector()
		);
	    
	    Mat xd = Imgproc.getPerspectiveTransform(sourceMat, destinationMat);  
	    Imgproc.warpPerspective(
    		poolPlayZoneMat,
    		poolPlayZoneMat,
    		xd,
    		new Size(projectorMaxWidth,projectorMaxHeight)
		);
	    //get perspective transform
	      
	      
		
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".png", poolPlayZoneMat, matOfByte);
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] data = encoder.encode(matOfByte.toArray());

		xd.release();
	    destinationMat.release();
	    sourceMat.release();
	    poolPlayZoneMat.release();
	    matOfByte.release();

		return data;
	} // end of drawImage(args);
		
		
	
	
	public void drawViewMode0(Mat mat, BilliardTable table) {
		drawBalls(mat, table.getBalls());
		drawWhiteBall(mat, table.getWhiteBall());
		if(displayPockets) {
			drawPockets(mat, table.getPockets());
		}
	}
	
	public void drawViewMode1(Mat mat, BilliardTable table) {
		drawWhiteBall(mat, table.getWhiteBall());
		drawSelected(mat, table.getSelectedBall(), table.getSelectedPocket());
		drawPockets(mat, table.getPockets());

		if((table.getSelectedBall() != null) && (table.getSelectedPocket() != null)){
			//drawTrajectory();
			Ball white = table.getWhiteBall();
			Ball selected = table.getSelectedBall();
			Pocket pocket = table.getSelectedPocket();
			int idPocket = table.getSelectedPocket().getId();
			if (white == null || selected == null || pocket == null) {
				return ;
			
			}
			hitPoints = hitService.findHittingPoint(white.getPoint(), selected.getPoint(), pocket.getPoint(),
					table.getBalls(), idPocket);
			
			drawTrajectory(mat, table, hitPoints);

		} else {
		  //this.drawBalls();
		}

	}
	
	
	public void drawViewMode22(Mat mat, BilliardTable table) {
		if ( table.getWhiteBall() == null ) {
			return;
			// error bo nie ma bialej bili.
		}
		
		Point white = table.getWhiteBall().getPoint();
		List<Ball> listBall = table.getBalls();
		List<Point> hiddenPointsList =  hiddenPlacesService.showHiddenPlaces(white, listBall);

		drawHiddenPlaces(mat, table, hiddenPointsList);
		drawBalls(mat, table.getBalls());
		drawWhiteBall(mat, table.getWhiteBall());
		if(displayPockets) {
			drawPockets(mat, table.getPockets());
		}
	}
	
	
	public void drawTraining(Mat mat, Training training, List<Pocket> pockets){
		if (training == null) {
			return;
		}

		// rysowanie bialej bili
		// nieco wiekszy, bialy okrag wokol bialej bili
		Imgproc.circle (
			mat,
			training.getWhiteBallPosition(),
			poolDrawerParams.getWhiteBallRadius(),
			poolDrawerParams.getWhiteBallColor(),
			poolDrawerParams.getLineThickness()
		);
		
		
	    // rysowanie punktu ustawienia bili do wbicia
		// pomaranczowy okrag wokol bili do wbicia
		drawTrainingBall(mat, training.getSelectedBallPosition(), poolDrawerParams.getTrainingSelectedBallColor());
		
		// rysowanie przeszkadzajek
		for ( Point disturbBallPosition: training.getDisturbBallsPositions() ) {
			// czerwony okrag wokol przeszkadzajki
			drawTrainingBall(mat, disturbBallPosition, poolDrawerParams.getDisturbBallColor());
			
		}
		
	    //rysowanie prostokatu w ktorym ma sie zatrzymac biala
		// obramowanie obszaru rysowania
	    Imgproc.rectangle (
			mat,
			training.getRectanglePosition().get(0),
			training.getRectanglePosition().get(1),
		    whiteColor,
		    poolDrawerParams.getLineThickness()
	    );

		String statusText = "";
	    if (trainingModeService.getState() == TrainingModeService.State.Ready) {
	        statusText = "READY";
	    } else if (trainingModeService.getState() == TrainingModeService.State.Fail) {
            statusText = "FAILED";
	    } else if (trainingModeService.getState() == TrainingModeService.State.Success) {
            statusText = "SUCCESS";
	    } else if (trainingModeService.getState() == TrainingModeService.State.WaitingForBallsPlacement) {
            statusText = "WaitingForBallsPlacement";
	    } else if (trainingModeService.getState() == TrainingModeService.State.WaitingForBallsStop) {
            statusText = "WaitingForBallsStop";
	    }
	    Point statusPosition = training.getStatusPosition();
	    if(statusPosition == null) {
	        statusPosition = new Point(300, 300);
        }
        Imgproc.putText(mat, statusText, statusPosition, 1, 2, whiteColor);

	    // rysowanie zaznaczenia luzy do ktorej ma wpasc bila
		// okrag wokol wybranego pocketu
		Pocket pocket = pockets.get(training.getPocketId());
		if ( pocket != null) {
	    	// jakis pocket zostal wyrbany
			Imgproc.circle (
				mat,
				pocket.getPoint(),
				pocketRadius,
				poolDrawerParams.getSelectedPocketColor(),
				pocketLineThickness
			);	
	    }

	    // tymczasowe rysowanie podpowiedzi
		drawHitPointHint(mat, training.getHitPointHint());
		drawHitPowerHint(mat, training.getHitPowerHint());
		drawTargetBallHitPointHint(mat, training.getTargetBallHitPointHint());
	}

	private void drawHitPointHint(Mat mat, HitPointHint hitPointHint) {
		if(hitPointHint != null) {
			Imgproc.circle(mat, hitPointHint.getPosition(), ((int) hitPointHint.getRadius()),
					whiteColor, poolDrawerParams.getLineThickness());

			int insideCircleRadius = (int) (hitPointHint.getRadius() * 0.23);

			List<Point> insideCirclesOffsets = hitPointHint.getInsideCirclesOffsets();
			for (int i = 0; i < insideCirclesOffsets.size(); i++) {
				Point offset = insideCirclesOffsets.get(i);
				Point insideCirclePosition = new Point(hitPointHint.getPosition().x + offset.x, hitPointHint.getPosition().y + offset.y);
				if(hitPointHint.getHitPoint().ordinal() == i) {
					Imgproc.circle(mat, insideCirclePosition, insideCircleRadius, whiteColor, FILLED);
				} else {
					Imgproc.circle(mat, insideCirclePosition, insideCircleRadius, whiteColor, poolDrawerParams.getLineThickness());
				}
			}
		}
	}

	private void drawHitPowerHint(Mat mat, HitPowerHint hitPowerHint) {
		if(hitPowerHint != null) {
			Point boundingBoxMax = new Point(hitPowerHint.getPosition().x + hitPowerHint.getSize().x, hitPowerHint.getPosition().y + hitPowerHint.getSize().y);
			Imgproc.rectangle(mat, hitPowerHint.getPosition(), boundingBoxMax, whiteColor);

			Imgproc.rectangle(mat, hitPowerHint.getPosition(), boundingBoxMax, whiteColor, poolDrawerParams.getLineThickness());
			Point fillBoundingBoxMin = hitPowerHint.getPosition();
			fillBoundingBoxMin.y += (1 - hitPowerHint.getHitPower() / 100) * hitPowerHint.getSize().y;
			Imgproc.rectangle(mat, fillBoundingBoxMin, boundingBoxMax, whiteColor, FILLED);
		}
	}

	private void drawTargetBallHitPointHint(Mat mat, TargetBallHitPointHint targetBallHitPointHint) {
		if(targetBallHitPointHint != null) {
            Imgproc.circle(mat, targetBallHitPointHint.getWhiteBall(), ((int) targetBallHitPointHint.getRadius()),
                    whiteColor, FILLED);
            Imgproc.circle(mat, targetBallHitPointHint.getTargetBall(), ((int) targetBallHitPointHint.getRadius()),
                    whiteColor, poolDrawerParams.getLineThickness());
        }
	}

	public void drawTrainingBall(Mat mat, Point ballPosition, Scalar color) {
		if( ballPosition == null ) {
			// error, bila nie istnieje
			return;
		}
		
		// okrag wokol pozycji bili 
		Imgproc.circle (
			mat,
			ballPosition,
			poolDrawerParams.getBallRadius(),
			color,
			poolDrawerParams.getLineThickness()
		);	
	}
	
	
	public void drawTrajectory(Mat mat, BilliardTable table, List<Point> hitPoints){
		if(hitPoints != null) {
		if( hitPoints.size() == 1 ){
			// jeden punkt oznacza, ze jest prosta droga do luzy
			List<MatOfPoint> listOfPoints = new ArrayList();
			
			listOfPoints.add(
				new MatOfPoint(
					table.getWhiteBall().getPoint(),
			        hitPoints.get(0),
			        table.getSelectedPocket().getPoint()
				)
			);
					

	      // rysowanie trajektorii
	      Imgproc.polylines(
	         mat,
	         listOfPoints,
	         false, // is Closed
	         poolDrawerParams.getTrajectoryLineColor(),
	         poolDrawerParams.getTrajectoryLineThickness()
	      );
	    } 
	    else if ( hitPoints.size() == 2){
	    	// dwa punkty oznaczaja, ze najpierw jest odbicie od bandy [1] a pozniej do bili [0].
			List<MatOfPoint> listOfPoints = new ArrayList();
			
			listOfPoints.add(
				new MatOfPoint(
					table.getWhiteBall().getPoint(),
			        hitPoints.get(1),
			        hitPoints.get(0),
			        table.getSelectedPocket().getPoint()
				)
			);
					
			// rysowanie trajektorii
			Imgproc.polylines(
				mat,
				listOfPoints,
				false, // is Closed
				poolDrawerParams.getTrajectoryLineColor(),
				poolDrawerParams.getTrajectoryLineThickness()
			);
	    }
		}
	}
	
	public void drawHiddenPlaces(Mat mat, BilliardTable table, List<Point> hiddenPointsList){
		
		
		for ( int i = 0; i < hiddenPointsList.size(); i = i +4 ) {
			List<MatOfPoint> points = new ArrayList<>();
			
	        points.add(
        		new MatOfPoint(
    				hiddenPointsList.get(i),
    				hiddenPointsList.get(i + 1),
    				hiddenPointsList.get(i + 2),
    				hiddenPointsList.get(i + 3)
				)
    		);
	        
	        
	      Imgproc.fillPoly(mat,
	          points,
	          poolDrawerParams.getHiddenPlacesColor()
          );
			
		}
		
	}
	
	/**
	 * 
	 * @param mat
	 * @param table
	 * @param roatationPoint
	 */
	public void drawRotationFollow(Mat mat, BilliardTable table, Point rotationPoint, List<Point> hitPoints, Point targetPoint) {
		
		List<MatOfPoint> listOfPoints = new ArrayList();
		
		listOfPoints.add(
			new MatOfPoint(
				table.getWhiteBall().getPoint(),
		        hitPoints.get(0),
		        table.getSelectedPocket().getPoint()
			)			
		);
		
		listOfPoints.add(
				new MatOfPoint(
					targetPoint,
					rotationPoint
				)							
			);					
		// rysowanie trajektorii
		Imgproc.polylines(
			mat,
			listOfPoints,
			false, // is Closed
			poolDrawerParams.getFollowRotationLineColor(),
			poolDrawerParams.getTrajectoryLineThickness()
		);		
	}
	
	
	/**
	 * 
	 * @param mat
	 * @param table
	 * @param roatationPoint
	 */
	public void drawRotationZero(Mat mat, BilliardTable table, Point rotationZeroPoint, List<Point> hitPoints, Point targetPoint) {
		
		List<MatOfPoint> listOfPoints = new ArrayList();
		List<MatOfPoint> listOfOtherPoints = new ArrayList();
		
		listOfPoints.add(
			new MatOfPoint(
				table.getWhiteBall().getPoint(),
		        hitPoints.get(0),
		        table.getSelectedPocket().getPoint()
			)			
		);
		
		listOfOtherPoints.add(
				new MatOfPoint(
					targetPoint,
					rotationZeroPoint
				)							
			);					
		// rysowanie trajektorii
		Imgproc.polylines(
			mat,
			listOfPoints,
			false, // is Closed
			poolDrawerParams.getTrajectoryLineColor(),
			poolDrawerParams.getTrajectoryLineThickness()
		);		
		
		Imgproc.polylines(
				mat,
				listOfOtherPoints,
				false, // is Closed
				poolDrawerParams.getZeroRotationLineColor(),
				poolDrawerParams.getTrajectoryLineThickness()
			);	
	}

	public void drawBothRotation(Mat mat, BilliardTable table, Point rotationZeroPoint, Point rotationFollowPoint, List<Point> hitPoints, Point targetPoint) {

		List<MatOfPoint> listOfPoints = new ArrayList();
		List<MatOfPoint> listWithZeroRotation = new ArrayList();
		List<MatOfPoint> listWithFollowRotation = new ArrayList();

		listOfPoints.add(
			new MatOfPoint(
				table.getWhiteBall().getPoint(),
		        hitPoints.get(0),
		        table.getSelectedPocket().getPoint()
			)
		);

		// Rotacja zero
		listWithZeroRotation.add(
				new MatOfPoint(
					targetPoint,
					rotationZeroPoint
				)
			);

		// Z rotacja postepowa
		listWithFollowRotation.add(
				new MatOfPoint(
					targetPoint,
					rotationFollowPoint
				)
			);


		// rysowanie trajektorii
		Imgproc.polylines(
			mat,
			listOfPoints,
			false, // is Closed
			poolDrawerParams.getTrajectoryLineColor(),
			poolDrawerParams.getTrajectoryLineThickness()
		);

		// Zero rotacja
		Imgproc.polylines(
				mat,
				listWithZeroRotation,
				false, // is Closed
				poolDrawerParams.getZeroRotationLineColor(),
				poolDrawerParams.getTrajectoryLineThickness()
			);

		// Follow rotacja
		Imgproc.polylines(
				mat,
				listWithFollowRotation,
				false, // is Closed
				poolDrawerParams.getFollowRotationLineColor(),
				poolDrawerParams.getTrajectoryLineThickness()
			);

	}

	public void drawViewModeRotation(Mat mat, BilliardTable table) {
		drawWhiteBall(mat, table.getWhiteBall());
		drawSelected(mat, table.getSelectedBall(), table.getSelectedPocket());
		drawPockets(mat, table.getPockets());

		if((table.getSelectedBall() != null) && (table.getSelectedPocket() != null)){
			//drawTrajectory();
			Ball white = table.getWhiteBall();
			Ball selected = table.getSelectedBall();
			Pocket pocket = table.getSelectedPocket();
			int idPocket = table.getSelectedPocket().getId();
			if (white == null || selected == null || pocket == null) {
				return ;
			
			}
			hitPoints = hitService.findHittingPoint(white.getPoint(), selected.getPoint(), pocket.getPoint(),
					table.getBalls(), idPocket);
			
			//Point rotationPoint = rotationService.newPointForFollowRotation(white.getPoint(), selected.getPoint(), pocket.getPoint(), table.getBalls(), idPocket);
			Point rotationPoint = rotationService.fixedFollowRotation(white.getPoint(), selected.getPoint(), pocket.getPoint(), table.getBalls(), idPocket);
			if (rotationPoint != null) {
				drawRotationFollow(mat, table,  rotationPoint, hitPoints, hitPoints.get(0));
			}


		} else {
		  //this.drawBalls();
		}
	}
	
	public void drawViewModeZeroRotation(Mat mat, BilliardTable table) {
		drawWhiteBall(mat, table.getWhiteBall());
		drawSelected(mat, table.getSelectedBall(), table.getSelectedPocket());
		drawPockets(mat, table.getPockets());

		if((table.getSelectedBall() != null) && (table.getSelectedPocket() != null)){
			Ball white = table.getWhiteBall();
			Ball selected = table.getSelectedBall();
			Pocket pocket = table.getSelectedPocket();
			int idPocket = table.getSelectedPocket().getId();
			if (white == null || selected == null || pocket == null) {
				return ;
			
			}
			hitPoints = hitService.findHittingPoint(white.getPoint(), selected.getPoint(), pocket.getPoint(),
					table.getBalls(), idPocket);
			
			Point rotationZeroPoint = rotationService.whiteBallZeroRotation(white.getPoint(), selected.getPoint(), pocket.getPoint(), table.getBalls(), idPocket);
			if (rotationZeroPoint != null) {
			drawRotationZero(mat, table,  rotationZeroPoint, hitPoints, hitPoints.get(0));
			}
		} else {
			
		}
	}
	

	public void drawViewModeBothRotation(Mat mat, BilliardTable table) {
		//System.out.println("elo");
		drawWhiteBall(mat, table.getWhiteBall());
		drawSelected(mat, table.getSelectedBall(), table.getSelectedPocket());
		drawPockets(mat, table.getPockets());

		if((table.getSelectedBall() != null) && (table.getSelectedPocket() != null)){
			Ball white = table.getWhiteBall();
			Ball selected = table.getSelectedBall();
			Pocket pocket = table.getSelectedPocket();
			int idPocket = table.getSelectedPocket().getId();
			if (white == null || selected == null || pocket == null) {
				return ;

			}
			hitPoints = hitService.findHittingPoint(white.getPoint(), selected.getPoint(), pocket.getPoint(),
					table.getBalls(), idPocket);

			Point rotationZeroPoint = rotationService.whiteBallZeroRotation(white.getPoint(), selected.getPoint(), pocket.getPoint(), table.getBalls(), idPocket);
			Point rotationPoint = rotationService.fixedFollowRotation(white.getPoint(), selected.getPoint(), pocket.getPoint(), table.getBalls(), idPocket);

			if (rotationZeroPoint != null && rotationPoint != null) {
				drawBothRotation(mat, table, rotationZeroPoint, rotationPoint, hitPoints, hitPoints.get(0));
			}
			//System.out.println("hit points: " + hitPoints);
		} else {

		}
	}
	
	public void drawViewModeGameBoard(Mat mat, GameService gameService, BilliardTable table) {
		drawCheckingPointsGame(mat, gameService.positionsForCheckingCircles());
		drawLinesGamers(mat, gameService.positionsForTracks(), table);
		drawLvl(mat, gameService, table);
		drawScoreForAllPlayers(mat, gameService);
		drawGameStatus(mat, gameService);
	}

	public void drawBestPocket(Mat mat, BilliardTable table, List<Point> hitPoints) {

		// Nie trzeba rysowa� bo robiac seta sie rysuje
//    	Imgproc.circle (
//			mat,
//			listOfPockets.get(idPocket).getPoint(),
//			ballRadius * 4,
//			new Scalar(255, 0, 0),
//			selectedPocketLineThickness
//		);

		if(hitPoints != null) {
			if( hitPoints.size() == 1 ){
			// jeden punkt oznacza, ze jest prosta droga do luzy
				List<MatOfPoint> listOfPoints = new ArrayList();

				listOfPoints.add(
					new MatOfPoint(
						table.getWhiteBall().getPoint(),
				        hitPoints.get(0),
				        table.getSelectedPocket().getPoint()
					)
				);

		      // rysowanie trajektorii
		      Imgproc.polylines(
		         mat,
		         listOfPoints,
		         false, // is Closed
		         poolDrawerParams.getTrajectoryLineColor(),
		         poolDrawerParams.getTrajectoryLineThickness()
		      );
	    } else {
	    	//System.out.println("Znalazlo odbicie od bandy");
	    }

		}


	}
	
	public void drawViewModeBestPocket(Mat mat, BilliardTable table) {
		drawWhiteBall(mat, table.getWhiteBall());
		drawPockets(mat, table.getPockets());

		if((table.getSelectedBall() != null)){

			Ball white = table.getWhiteBall();
			Ball selected = table.getSelectedBall();
			List<Pocket> listOfPockets = table.getPockets();
			List<Ball> listOfBallse = table.getBalls();

			int idPocket = hitService.findBestPocket(white.getPoint(), selected.getPoint(), listOfPockets, listOfBallse);

			if (idPocket != -1) {

			if (white == null || selected == null) {
				return ;
			
			}
			}
			//System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH = " + idPocket);
			//System.out.println("id pocket = " + idPocket);
			tableService.selectPocket((long)idPocket);

			Pocket pocket = table.getSelectedPocket();

			hitPoints = hitService.findHittingPoint(white.getPoint(), selected.getPoint(), pocket.getPoint(),
					table.getBalls(), idPocket);

			drawBestPocket(mat, table, hitPoints);
		} else {

		}

		drawSelected(mat, table.getSelectedBall(), table.getSelectedPocket());

	}

	public void drawProMode(Mat mat, BilliardTable table, List<Point> hitPoints) {

	}

	public void drawViewModeProMode(Mat mat, BilliardTable table) {
		//System.out.println("elo");
		//drawWhiteBall(mat, table.getWhiteBall());
		//drawSelected(mat, table.getSelectedBall(), table.getSelectedPocket());
		//drawPockets(mat, table.getPockets());

		//System.out.println("White ball pos: " + table.getWhiteBall().getPoint());
		//System.out.println("Selected ball pos: " + table.getSelectedBall().getPoint());


		if((table.getSelectedBall() != null) && (table.getSelectedPocket() != null)){
			//drawTrajectory();
			//System.out.println("elo");
			Ball white = table.getWhiteBall();
			Ball selected = table.getSelectedBall();
			Pocket pocket = table.getSelectedPocket();
			int idPocket = table.getSelectedPocket().getId();
			if (white == null || selected == null || pocket == null) {
				return ;

			}
			hitPoints = hitService.findHittingPoint(white.getPoint(), selected.getPoint(), pocket.getPoint(),
					table.getBalls(), idPocket);

			drawTarget(mat, hitPoints);



			//System.out.println("hit points: " + hitPoints);

		//console.log(this.hittingPoint);
		//this.drawTrajectory(this.hittingPoint);
		} else {
		  //this.drawBalls();
		}

	}
		
		



	


	
	


	public void drawBalls(Mat mat, List<Ball> balls) {
		if( balls == null ) {
			return;
		}
		
		//rysowaie bil
		for(Ball ball: balls) {
			// rysowanie okregu
			Imgproc.circle (
				mat,
				ball.getPoint(),
				poolDrawerParams.getBallRadius(),
				poolDrawerParams.getBallColor(),
				poolDrawerParams.getLineThickness()
			);
			
			Imgproc.circle (
				mat,
				ball.getPoint(),
				poolDrawerParams.getBallRadius() + 4,
				poolDrawerParams.getBallColor(),
				poolDrawerParams.getLineThickness()
			);

			// rysowanie napisu id
			if( displayBallId ) {
			    Imgproc.putText (
					mat,
					Integer.toString(ball.getId()),
					ball.getPoint(),
					Core.FONT_HERSHEY_SIMPLEX,	// front face
					0.3,						// front scale
					whiteColor,	// Scalar object for color
					2							// Thickness
			    );
			}
		}
	} // end of drawBalls
	
	public void drawWhiteBall(Mat mat, Ball whiteBall) {
		if ( whiteBall == null ) {
			return;
		}
		
		// rysowanie okregu
		Imgproc.circle (
			mat,
			whiteBall.getPoint(),
			poolDrawerParams.getWhiteBallRadius(),
			poolDrawerParams.getWhiteBallColor(),
			poolDrawerParams.getLineThickness()
		);
		
		Imgproc.circle (
			mat,
			whiteBall.getPoint(),
			poolDrawerParams.getWhiteBallRadius() + poolDrawerParams.getLineThickness(),
			new Scalar(0, 255, 255),
			poolDrawerParams.getLineThickness()
		);
		
		Imgproc.circle (
			mat,
			whiteBall.getPoint(),
			poolDrawerParams.getWhiteBallRadius() + poolDrawerParams.getLineThickness() * 2,
			poolDrawerParams.getWhiteBallColor(),
			poolDrawerParams.getLineThickness()
		);

	} // end of drawBalls(args);
	
	
	public void drawPockets(Mat mat, List<Pocket> pockets) {
		//rysowaie bil
		for( Pocket pocket: pockets ){
			// rysowanie okregu
			Imgproc.circle (
				mat,
				pocket.getPoint(),
				pocketRadius,
				poolDrawerParams.getPocketColor(),
				pocketLineThickness
			);
		}
	} // end of drawBalls
	
	
	public void drawSelected(Mat mat, Ball selectedBall, Pocket selectedPocket) {
	    // rysowanie wybranej zaznaczonej bili
		if ( selectedBall != null) {
			Imgproc.circle (
				mat,          //Matrix obj of the image
				selectedBall.getPoint(),    //Center of the circle
				poolDrawerParams.getBallRadius() * 2,                    //Radius
				poolDrawerParams.getSelectedBallColor(),  //Scalar object for color
				poolDrawerParams.getLineThickness()                      //Thickness of the circle
			);
		}
		
		// rysowanie obramowania zaznaczonego pocketu
	    if ( selectedPocket != null) {
	    	Imgproc.circle (
				mat,
				selectedPocket.getPoint(),
				poolDrawerParams.getBallRadius() * 4,
				poolDrawerParams.getSelectedPocketColor(),
				selectedPocketLineThickness
			);
	    }
	} // end of drawSelected(args);

	public void drawPlayZoneBorder(Mat mat) {
		// obramowanie obszaru rysowania
	    Imgproc.rectangle (
			mat,
		    new Point(0, 0),
		    new Point(kinectWidth-1, kinectHeight-1),
		    new Scalar(0, 255, 255),
		    playZoneBorderThickness
	    );
	} // end of drawPlayZoneBorder(args);

	public void updateCalibration(CalibrationParams calibrationParams) {
		this.calibrationParams = calibrationParams;
	}

	public void drawShotInformation(Mat mat, Informations informations) {

		if ( informations == null) {
			return;
		}



//     	"Dst White -> Ball   : " + (int)informations.getDistanceWhiteSelected() +
//     	"\nDst White -> Pocket : " + (int)informations.getDistanceWhitePocket() +
//     	"\nHit angle  : " + (int)informations.getHitAngle() +
//     	"\nDifficulty : " + informations.getDifficultyLevel()
//		 ),          // Text to be added

		//double tableIncm = CalibrationParams.getDefaultCalibrationParams().getTableSizeInCm().x / BilliardTab;

		drawText(mat, new Point(20, 40),          "Dst White to Ball    : " + (int)informations.getDistanceWhiteSelected());
		drawText(mat, new Point(20, 40 + textXD), "Dst White to Pocket : " + (int)informations.getDistanceWhitePocket());
		drawText(mat, new Point(20, 40 + textXD * 2), "Hit angle  : " + (int)informations.getHitAngle());
		//drawText(mat, new Point(20, 40 + textXD * 3), "Difficulty  : " + informations.getDifficultyLevel());



	}

	public void drawText(Mat mat, Point point, String text) {
	      Imgproc.putText (
	 	         mat,
	 	         text,
	 	         point,
	 	         Core.FONT_HERSHEY_TRIPLEX ,
	 	         1,
	 	         whiteColor,
	 	         1
	 	      );
	}


	public void toggleHitInfo() {
		displayShotInformations = ! displayShotInformations;
	}

	public void drawTarget(Mat mat, List<Point> hitList) {
	    // rysowanie wybranej zaznaczonej bili
		if ( hitList != null) {
			Imgproc.circle (
				mat,          //Matrix obj of the image
				hitList.get(0),    //Center of the circle
				poolDrawerParams.getBallRadius() * 2,                    //Radius
				new Scalar(255, 0, 0),  //Scalar object for color
				poolDrawerParams.getLineThickness()                      //Thickness of the circle
			);
		}
	}
	
	public void drawCheckingPointsGame(Mat mat, List<Point> positionsForCheckingCircles) {

		if ( positionsForCheckingCircles != null) {
			
			for (Point point : positionsForCheckingCircles) {
				Imgproc.circle (
						mat,          //Matrix obj of the image
						point,    //Center of the circle
						ballRadius,                    //Radius
						new Scalar(255, 0, 0),  //Scalar object for color
						ballLineThickness                      //Thickness of the circle
					);
			}
		}
	}
	
	public void drawLinesGamers(Mat mat, List<Double> listWithYForLines, BilliardTable table) {
		
		List<Point> listWithPoints = new ArrayList<>();
		
		for (Double yPos : listWithYForLines) {
			listWithPoints.add(new Point(0, yPos));
			listWithPoints.add(new Point(table.getWidth(), yPos));
		}
		
		if (listWithYForLines != null) {
			
			List<MatOfPoint> listOfPoints = new ArrayList();
			
			for (int i = 0; i < listWithPoints.size(); i=i+2) {
				listOfPoints.add(
						new MatOfPoint(
								listWithPoints.get(i),
								listWithPoints.get(i+1)
						)							
					);					
				// rysowanie trajektorii
				Imgproc.polylines(
					mat,
					listOfPoints,
					false, // is Closed
					new Scalar(0, 255, 255),
					trajectoryLineThickness
				);	
			}
				
		}
		
	}
	
	public void drawLvl(Mat mat, GameService gameService, BilliardTable table) {

		if (gameService.getListOfAllGamersFromService() != null) {
			
			for (Gamer gamer : gameService.getListOfAllGamersFromService()) {

				if (gamer.getCurrentScore() < 5) {

					List<MatOfPoint> listOfPoints = new ArrayList();
					
					listOfPoints.add(
							new MatOfPoint(
									new Point(gamer.getLineX(), gamer.getLeftOwnBand()),
									new Point(gamer.getLineX(), gamer.getRightOwnBand())
							)							
						);			
					
					Imgproc.polylines(
							mat,
							listOfPoints,
							false, // is Closed
							new Scalar(0, 255, 255),
							trajectoryLineThickness
						);	
					
				} else if (gamer.getCurrentScore() >= 5 && gamer.getCurrentScore() < 10) {
					Imgproc.circle (
							mat,          //Matrix obj of the image
							gamer.getMediumLvlPoint(),    //Center of the circle
							ballRadius * 2,                    //Radius
							new Scalar(255, 0, 0),  //Scalar object for color
							ballLineThickness                      //Thickness of the circle
						);
				} else if (gamer.getCurrentScore() >= 10) {
					Imgproc.circle (
							mat,          //Matrix obj of the image
							gamer.getHardLvlPoint(),    //Center of the circle
							ballRadius * 2,                    //Radius
							new Scalar(255, 0, 0),  //Scalar object for color
							ballLineThickness                      //Thickness of the circle
						);
				}
				//System.out.println("gamer.getStartCheckingPoint() = "+ gamer.getStartCheckingPoint());	
			}
		}
	}
	
	public void drawScoreForAllPlayers(Mat mat, GameService gameService) {
		
		if (gameService.getListOfAllGamersFromService() != null) {		
			for (Gamer gamer : gameService.getListOfAllGamersFromService()) {
				String score = new String(Integer.toString(gamer.getCurrentScore()));
				
		        Imgproc.putText(mat, score, gamer.getPosShowScore(), 6, 2.2, new Scalar(255,255,255), 3);
		        
			}
		}
	}
	
	public void drawGameStatus(Mat mat, GameService gameService) {
		String statusText = "";
		if (gameService.getState() == GameService.GameState.ReadyToPlay) {
			statusText = "ReadyToPlay";
		} else if (gameService.getState() == GameService.GameState.WaitingForAllBallsOnRightSide) {
			statusText = "WaitingForAllBallsOnRightSide";
		} else if (gameService.getState() == GameService.GameState.WaitingForCheckingBallsPlacement) {
			statusText = "WaitingForCheckingBallsPlacement";
		} else if (gameService.getState() == GameService.GameState.WaitingForSetScoreAndGenerateNewLvl) {
			statusText = "WaitingForSetScoreAndGenerateNewLvl";
		} else if (gameService.getState() == GameService.GameState.WaitingForStopBalls) {
			statusText = "WaitingForStopBalls";
		}
		
	    Point statusPosition = gameService.positionToShowMessages();
	    if(statusPosition == null) {
	        statusPosition = gameService.positionToShowMessages();
        }
	    
        Imgproc.putText(mat, statusText, statusPosition, 1, 2, new Scalar(255,255,255), 2);
	}
	
	
}