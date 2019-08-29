package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.controllers.BilliardTableController;
import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.CalibrationParams;
import pl.ncdc.billiard.models.Pocket;

@Service
public class PoolDrawerService {

	
	@Autowired
	HitService hitService;
	
	@Autowired
	HiddenPlacesService hiddenPlacesService;
	
	@Autowired
	RotationService rotationService;
	
	@Value("${kinectService.mask}")
	private String filename;
	
	
	
	//zmienne do jakiegos pliku
	int kinectHeight = 600;
	int kinectWidth = 1200;
	
	boolean displayBallId = false;
	boolean displayPockets = false;
	
	int projectorMaxHeight = 1080-1;
	int projectorMaxWidth = 1920;
	
	int ballRadius = 15;
	int whiteBallRadius = 20;
	int pocketRadius = 50;
	
	int ballLineThickness = 2;
	int pocketLineThickness = 5;
	int trajectoryLineThickness = 2;
	int playZoneBorderThickness = 1;
	int selectedPocketLineThickness = 4;
	// koniec zmiennych do pliku
	
	List<Point> hitPoints;

	CalibrationParams calibrationParams = CalibrationParams.getDefaultCalibrationParams();

	
	
	public byte[] drawImage(BilliardTable table)
	{
		
		kinectHeight = table.getHeight();
		kinectWidth = table.getWidth();
		
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
				case 34: drawViewModeBestPocket(poolPlayZoneMat, table); break;
				
				//case 2: this.drawViewMode2(); break;
			}
		}
		else{
			// tryb challenge'u zostal wybrany.
			//this.fetchTrainingById(this.table.selectedChallenge);
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
		//System.out.println("elo");
		drawWhiteBall(mat, table.getWhiteBall());
		drawSelected(mat, table.getSelectedBall(), table.getSelectedPocket());
		drawPockets(mat, table.getPockets());

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
			
			drawTrajectory(mat, table, hitPoints);
			

			
			//System.out.println("hit points: " + hitPoints);
			
		//console.log(this.hittingPoint);
		//this.drawTrajectory(this.hittingPoint);
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
		
		//System.out.println("HDEIN PONTY: " + hiddenPointsList);
		
		drawHiddenPlaces(mat, table, hiddenPointsList);
		
		drawBalls(mat, table.getBalls());
		drawWhiteBall(mat, table.getWhiteBall());
		if(displayPockets) {
			drawPockets(mat, table.getPockets());
		}
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
	         new Scalar(0, 255, 255),
	         trajectoryLineThickness
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
				new Scalar(0, 255, 255),
				trajectoryLineThickness
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
	          new Scalar( 255, 0, 0 )
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
			new Scalar(0, 255, 255),
			trajectoryLineThickness
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
			new Scalar(0, 255, 255),
			trajectoryLineThickness
		);		
		
		Imgproc.polylines(
				mat,
				listOfOtherPoints,
				false, // is Closed
				new Scalar(255, 0, 0),
				trajectoryLineThickness
			);	
	}
	
	public void drawViewModeRotation(Mat mat, BilliardTable table) {
		//System.out.println("elo");
		drawWhiteBall(mat, table.getWhiteBall());
		drawSelected(mat, table.getSelectedBall(), table.getSelectedPocket());
		drawPockets(mat, table.getPockets());

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
			
			//Point rotationPoint = rotationService.newPointForFollowRotation(white.getPoint(), selected.getPoint(), pocket.getPoint(), table.getBalls(), idPocket);
			Point rotationPoint = rotationService.fixedFollowRotation(white.getPoint(), selected.getPoint(), pocket.getPoint(), table.getBalls(), idPocket);
			if (rotationPoint != null) {
				drawRotationFollow(mat, table,  rotationPoint, hitPoints, hitPoints.get(0));
			}

			
			//System.out.println("hit points: " + hitPoints);
			
		//console.log(this.hittingPoint);
		//this.drawTrajectory(this.hittingPoint);
		} else {
		  //this.drawBalls();
		}
	}
	
	public void drawViewModeZeroRotation(Mat mat, BilliardTable table) {
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
			if (rotationZeroPoint != null) {
			drawRotationZero(mat, table,  rotationZeroPoint, hitPoints, hitPoints.get(0));
			}
			//System.out.println("hit points: " + hitPoints);	
		} else {
			
		}
	}
	
	public void drawBestPocket(Mat mat, BilliardTable table, List<Pocket> listOfPockets, int idPocket) {
				
	    	Imgproc.circle (
				mat,
				listOfPockets.get(idPocket).getPoint(),
				ballRadius * 4,
				new Scalar(255, 0, 0),
				selectedPocketLineThickness
			);
	}
	
	public void drawViewModeBestPocket(Mat mat, BilliardTable table) {
		drawWhiteBall(mat, table.getWhiteBall());
		drawSelected(mat, table.getSelectedBall(), table.getSelectedPocket());
		drawPockets(mat, table.getPockets());
		if((table.getSelectedBall() != null)){
			Ball white = table.getWhiteBall();
			Ball selected = table.getSelectedBall();
			List<Pocket> listOfPockets = table.getPockets();
			List<Ball> listOfBallse = table.getBalls();
			
			int idPocket = hitService.findBestPocket(white.getPoint(), selected.getPoint(), listOfPockets, listOfBallse);
			if (idPocket != -1) {
				
			
			//System.out.println("IDDD = " + idPocket);
			if (white == null || selected == null) {
				return ;
			
			}	
			//System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH = " + idPocket);
			
			drawBestPocket(mat, table, listOfPockets, idPocket);
			}
		} else {
			
		}
	}

		
		
		
		

			
	
//	List<Point> listPoints = new ArrayList<Point>();


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void examp(Mat mat) {
		//Drawing a Circle
		Imgproc.circle (
			mat,          //Matrix obj of the image
			new Point(400, 440),    //Center of the circle
			100,                    //Radius
			new Scalar(70, 255, 55),  //Scalar object for color
			5                      //Thickness of the circle
		);
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
				ballRadius,
				new Scalar(255, 255, 0),
				ballLineThickness
			);
			
			Imgproc.circle (
				mat,
				ball.getPoint(),
				ballRadius + 4,
				new Scalar(255, 255, 0),
				ballLineThickness
			);

			// rysowanie napisu id
			if( displayBallId ) {
			    Imgproc.putText (
					mat,
					Integer.toString(ball.getId()),
					ball.getPoint(),
					Core.FONT_HERSHEY_SIMPLEX,	// front face
					0.3,						// front scale
					new Scalar(255, 255, 255),	// Scalar object for color
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
			whiteBallRadius,
			new Scalar(255, 255, 255),
			ballLineThickness
		);
		
		Imgproc.circle (
			mat,
			whiteBall.getPoint(),
			whiteBallRadius + ballLineThickness,
			new Scalar(0, 255, 255),
			ballLineThickness
		);
		
		Imgproc.circle (
			mat,
			whiteBall.getPoint(),
			whiteBallRadius + ballLineThickness * 2,
			new Scalar(255, 255, 255),
			ballLineThickness
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
				new Scalar(255, 255, 0),
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
				ballRadius * 2,                    //Radius
				new Scalar(255, 0, 0),  //Scalar object for color
				ballLineThickness                      //Thickness of the circle
			);
		}
		
		// rysowanie obramowania zaznaczonego pocketu
	    if ( selectedPocket != null) {
	    	Imgproc.circle (
				mat,
				selectedPocket.getPoint(),
				ballRadius * 4,
				new Scalar(255, 0, 0),
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
}