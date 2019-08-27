package pl.ncdc.billiard.service;

import java.util.List;

import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.Ball;

@Service
public class RotationService {

	
	@Autowired
	MathService mathService;
	
	@Autowired
	HitService hitService;

	public double angleFollowRotation(double angleX) {

		double result = 0;
		result = (-3770.112) + (136.0718 * angleX) - (1.954948 * (Math.pow(angleX, 2)))
				+ (0.01400508 * ((Math.pow(angleX, 3)))) - (0.00004967584 * (Math.pow(angleX, 4)))
				+ (6.926647 * (Math.pow(10, -8)) * ((Math.pow(angleX, 5))));
		return result;

	}

	public Point rotationFollow(Point whiteBall, Point targetBall, Point pocket, double angle) {

		Point returnedPoint = new Point();
		double findAngle = 0;
		double newX = 0, newY = 0, mainLineX = 0, mainLineY = 0;

		// Wspolczynniki lini prostej dla target i targetBall.
		List<Double> aBFactors = mathService.abOfFunction(targetBall.x, targetBall.y, pocket.x, pocket.y);

		// Biore dowolny punkt z tej prostej ^
		// Max X stolu
		mainLineX = 1190;
		// mainLineX = targetBall.x + 100;

		mainLineY = (aBFactors.get(0) * mainLineX) + aBFactors.get(1);

		System.out.println("A factor = " + aBFactors.get(0));
		System.out.println("B factor = " + aBFactors.get(1));

		findAngle = angle;

		newX = (((mainLineX - targetBall.x) * Math.cos(Math.toRadians(findAngle)))
				- ((mainLineY - targetBall.y) * Math.sin(Math.toRadians(findAngle))) + targetBall.x);
		newY = (((mainLineX - targetBall.x) * Math.sin(Math.toRadians(findAngle)))
				+ ((mainLineY - targetBall.y) * Math.cos(Math.toRadians(findAngle))) + targetBall.y);

		returnedPoint.x = newX;
		returnedPoint.y = newY;

		return returnedPoint;
	}

	public Point whiteBallRotation(Point white, Point selected, Point pocket, List<Ball> listBall, int idPocket) {

		Point targetPoint = hitService.findHittingPoint(white, selected, pocket, listBall, idPocket).get(0);
		Point pointRotation = new Point();
		if (targetPoint != null) {

			double angleHit = mathService.findAngle(white, targetPoint, selected);

			double angleFollowRotation = angleFollowRotation(angleHit);

			pointRotation = rotationFollow(white, targetPoint, pocket, angleFollowRotation);

		}

		return pointRotation;

	}

	public Point whiteBallZeroRotation(Point white, Point selected, Point pocket, List<Ball> listBall, int idPocket) {

		Point targetPoint = hitService.findHittingPoint(white, selected, pocket, listBall, idPocket).get(0);

		if (targetPoint != null) {

			List<Double> listt = mathService.abOfFunction(targetPoint.x, targetPoint.y, pocket.x, pocket.y);
			double a = listt.get(0);
			double a2 = -1 / a;
			System.out.println(a2);

			double b2 = 0;

			b2 = targetPoint.y - (targetPoint.x * a2);

			double xNew = targetPoint.x + 100;
			double yNew = a2 * xNew + b2;

			double xNew2, yNew2;
			Point newPoint = new Point();

			newPoint.x = xNew;
			newPoint.y = yNew;

			Point secondOption = new Point();
			double angle = (mathService.findAngle(white, targetPoint, newPoint) * 57);
			if (angle >= 88 && angle <= 92) {
				return targetPoint;
			}

			if (angle > 92) {
				return newPoint;
			}

			else {

				xNew2 = targetPoint.x - 100;
				yNew2 = a2 * xNew2 + b2;

				secondOption.x = xNew2;
				secondOption.y = yNew2;

				return secondOption;
			}
		}

		return null;

	}
	
    /**
     * Wymodelowana funkcja dla roznych katow
     * @param angleX
     * @return
     */
    public double angleFollowRotationFunction(double angleX) {

        double result = 0;
        result = (-3770.112) + (136.0718 * angleX) - (1.954948 * (Math.pow(angleX, 2)))
                + (0.01400508 * ((Math.pow(angleX, 3)))) - (0.00004967584 * (Math.pow(angleX, 4)))
                + (6.926647 * (Math.pow(10, -8)) * ((Math.pow(angleX, 5))));
        return result;

    }
	
    public Point newPointForFollowRotation(Point white, Point selected, Point pocket, List<Ball> listBall, int idPocket){
        Point returnedPoint = new Point();
        double findAngle = 0;
        double newX = 0, newY = 0, mainLineX = 0, mainLineY = 0;
        double angle = 0;



        Point targetPoint = hitService.findHittingPoint(white, selected, pocket, listBall, idPocket).get(0);
        List<Double> aBFactorsWhiteAndTarget = mathService.abOfFunction(targetPoint.x, targetPoint.y, white.x, white.y);

        if (targetPoint != null) {

            List<Double> listt = mathService.abOfFunction(targetPoint.x, targetPoint.y, pocket.x, pocket.y);
            double a = listt.get(0);
            double a2 = -1 / a;

            double minusInf = Double.POSITIVE_INFINITY * -1;

            if (minusInf != a2) {


                double b2 = 0, xNew,yNew;

                b2 = targetPoint.y - (targetPoint.x * a2);

                if(targetPoint.x > white.x) {
                     xNew = targetPoint.x + 100;
                     yNew = aBFactorsWhiteAndTarget.get(0) * xNew + aBFactorsWhiteAndTarget.get(1);
                }
                else{
                     xNew = targetPoint.x - 100;
                    System.out.println("dupppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp");
                     yNew = aBFactorsWhiteAndTarget.get(0) * xNew + aBFactorsWhiteAndTarget.get(1);
                }
                
                double xNew2, yNew2;
                Point newPoint = new Point();
                newPoint.x = xNew;
                newPoint.y = yNew;


                Point secondOption = new Point();
                angle = (mathService.findAngle(white, targetPoint, newPoint) * 57);


                // Prosta whiteBall --> Target ktora jesli obrocimy o dany kat otrzymamy punkt dla prostej oznaczajacej droge bialej bili z rotacja postepowa

                if (targetPoint.x > white.x) {
                    mainLineX = targetPoint.x + 1100;
                } else {
                    mainLineX = targetPoint.x - 1100;
                }

                mainLineY = (aBFactorsWhiteAndTarget.get(0) * mainLineX) + aBFactorsWhiteAndTarget.get(1);

                System.out.println("A factor = " + aBFactorsWhiteAndTarget.get(0));
                System.out.println("B factor = " + aBFactorsWhiteAndTarget.get(1));


                System.out.println("New Point " + newPoint);
                System.out.println("Target Point " + targetPoint);

//                 if (angle > 90) {

                System.out.println("Angle > 92  = " + angle);
                System.out.println("Sprawdzenie k¹ta " + hitService.findAngle(newPoint, targetPoint, pocket) * 57.2957);
                double angleHit = mathService.findAngle(white, targetPoint, selected) * 57;
                double angleFollowRotation = angleFollowRotationFunction(angleHit);
                System.out.println("Angle hitttttttttttttttttttttttttttttt" + angleHit);

                    findAngle = -angleFollowRotation; // jesli kat mniejszy to -
                    
                    Point pointTest = fixedFollowRotation(white, selected, pocket, listBall, 3);
                    
                    System.out.println("Punkt ktory obracamy o kat z wymodelowanej funkcji X = " + mainLineX + " Y = " + mainLineY);



                newX = (((mainLineX - targetPoint.x) * Math.cos(Math.toRadians(findAngle)))
                        - ((mainLineY - targetPoint.y) * Math.sin(Math.toRadians(findAngle))) + targetPoint.x);
                newY = (((mainLineX - targetPoint.x) * Math.sin(Math.toRadians(findAngle)))
                        + ((mainLineY - targetPoint.y) * Math.cos(Math.toRadians(findAngle))) + targetPoint.y);

                returnedPoint.x = newX;
                returnedPoint.y = newY;
                System.out.println("Kat o ktory obracamy punkt na prostej " + findAngle);
                System.out.println("returnedPoint " + returnedPoint);
                System.out.println();
                
                List<Double> newFactors = mathService.abOfFunction(targetPoint.x, targetPoint.y, returnedPoint.x, returnedPoint.y);
                System.out.println("Wspolczynniki najnowszej wynikowej prostej A = " + newFactors.get(0) + " B = " + newFactors.get(1));
                return returnedPoint;
//                     } else {
//
//                    xNew2 = targetPoint.x + 100;
//                    yNew2 = aBFactorsWhiteAndTarget.get(0) * xNew2 + aBFactorsWhiteAndTarget.get(1);
//
//                    double angleHit = mathService.findAngle(white, targetPoint, selected) * 57;
//                    double angleFollowRotation = angleFollowRotationFunction(angleHit);
//                    findAngle = angleFollowRotation;
//
//                    newX = (((xNew2 - targetPoint.x) * Math.cos(Math.toRadians(findAngle)))
//                            - ((yNew2 - targetPoint.y) * Math.sin(Math.toRadians(findAngle))) + targetPoint.x);
//                    newY = (((xNew2 - targetPoint.x) * Math.sin(Math.toRadians(findAngle)))
//                            + ((yNew2 - targetPoint.y) * Math.cos(Math.toRadians(findAngle))) + targetPoint.y);
//
//                    returnedPoint.x = newX;
//                    returnedPoint.y = newY;
//
//                    System.out.println("Kat o ktory obracamy punkt na prostej " + findAngle);
//                    System.out.println("Angle < 92 = " + angle);
//                    System.out.println("Sprawdzenie k¹ta " + hitService.findAngle(secondOption, targetPoint, pocket) * 57.2957);
//                    System.out.println("returnedPoint " + returnedPoint);
//                    return returnedPoint;
//                     }
            }
        } else {

        }

        return null;
    }
    
    
    /**
     * Okej no to tak
     * Najpierw liczymy wspolczynniki prostej prostopadlej do [Pocket--Target] wykorzystamy ja do okreslenia pod jakim katem 
     * uderzana jest selectedBall.
     * Nastepnie wyznaczam punkt na prostopadlej prostej, ktory posluzy nam do okreslenia kata
     * Liczymy kat angleOfWhiteTargetNew
     * Mamy ifa ktory na podstawie tego kata okresla czy wynikowy punkt ma byc przesuniety o kat angleFollowRotation albo -angleFollowRotation
     * angleFollowRotation jest katem wynikowym z zamodelowanej funkcji dla rotacji postepowej
     * Czyli katem o ktory trzeba obrocic punkt na prostej [white -- target]
     * W zaleznosci czy target stoi blizej lub dalej od bialej bili na osi X dobieramy punkt +1100 albo -1100 Czyli
     *  dowolny punkt na prostej ktora obrocimy o dany kat i otrzymamy nasz punkt wynikowy calej funkcji.
     * @param white
     * @param selected
     * @param pocket
     * @param listBall
     * @param idPocket
     * @return
     */
    public Point fixedFollowRotation(Point white, Point selected, Point pocket, List<Ball> listBall, int idPocket) {
    	
    	List<Point> listOfHittingPoints = hitService.findHittingPoint(white, selected, pocket, listBall, idPocket);
    	
    	if (listOfHittingPoints.size() == 2 || listOfHittingPoints.size() == 0 ) {
    		return null;
    	}
    	
		Point targetPoint = listOfHittingPoints.get(0);
		double a = 0, a2 = 0, b2 = 0, xNew = 0, yNew = 0, angleOfWhiteTargetNew = 0, checkAngle = 0;
		
		if (targetPoint != null) {
			List<Double> factorsTargetPocket = mathService.abOfFunction(targetPoint.x, targetPoint.y, pocket.x, pocket.y);
			a = factorsTargetPocket.get(0);
			a2 = -1 / a;
	
			b2 = 0;
	
			b2 = targetPoint.y - (targetPoint.x * a2);
	
			xNew = targetPoint.x + 100;			// Ciekawe czy to nie jest zalezne od x targeta i whita tak jak wtedy gdy + - 1000 dla wslasciwych punktow
			yNew = a2 * xNew + b2;
			Point newPoint = new Point(xNew, yNew);
			
			angleOfWhiteTargetNew = (mathService.findAngle(white, targetPoint, newPoint) * 57);
			checkAngle = (mathService.findAngle(newPoint, targetPoint, pocket) * 57); // Musi sie rownac 90
			
			System.out.println("-------------------------Spawdzenie kata stalego = " + checkAngle);
			System.out.println("-------------------------Spawdzenie angleOfWhiteTargetNew = " + angleOfWhiteTargetNew);
			System.out.println("-------------------------Point prostej prostopadlej " + newPoint);
			
			 Point returnedPoint = new Point();
		        double findAngle = 0;
		        double newX = 0, newY = 0, mainLineX = 0, mainLineY = 0;
		        double angle = 0;

		        List<Double> aBFactorsWhiteAndTarget = mathService.abOfFunction(targetPoint.x, targetPoint.y, white.x, white.y);

		        if (targetPoint != null) {

		            double minusInf = Double.POSITIVE_INFINITY * -1;
		            
	                double angleHit = mathService.findAngle(white, targetPoint, selected) * 57;
	                double angleFollowRotation = angleFollowRotationFunction(angleHit);

		            if (minusInf != a2) {

		                if (targetPoint.x > white.x) {
		                    mainLineX = targetPoint.x + 1100;
		                    
			                if (angleOfWhiteTargetNew < 90) {
			                    findAngle = -angleFollowRotation; // jesli kat mniejszy to -
			                    System.out.println("11111111111111111111111111111111111111");
			                } else {
			                	findAngle = -angleFollowRotation;
			                	System.out.println("22222222222222222222222222222222222222");
			                }
			                
		                } else {
		                	
		                    mainLineX = targetPoint.x - 1100;
		                    
			                if (angleOfWhiteTargetNew < 90) {
			                    findAngle = angleFollowRotation; // jesli kat mniejszy to -
			                    System.out.println("33333333333333333333333333333333333333");
			                } else {
			                	findAngle = angleFollowRotation;
			                	System.out.println("444444444444444444444444444444444444444");
			                }
		                }

		                mainLineY = (aBFactorsWhiteAndTarget.get(0) * mainLineX) + aBFactorsWhiteAndTarget.get(1); // 

		                System.out.println("A factor = " + aBFactorsWhiteAndTarget.get(0));
		                System.out.println("B factor = " + aBFactorsWhiteAndTarget.get(1));

		                System.out.println("New Point " + newPoint);
		                System.out.println("Target Point " + targetPoint);

		                System.out.println("Angle > 92  = " + angle);
		                System.out.println("Sprawdzenie k¹ta " + hitService.findAngle(newPoint, targetPoint, pocket) * 57.2957);

		                System.out.println("Angle hitttttttttttttttttttttttttttttt" + angleHit);

		                System.out.println("angleOfWhiteTargetNew = " + angleOfWhiteTargetNew);
		                //t.x

		                
		                    //Point pointTest = fixeFollowRotation(white, selected, pocket, listBall, 3);
		                    
		                    System.out.println("Punkt ktory obracamy o kat z wymodelowanej funkcji X = " + mainLineX + " Y = " + mainLineY);



		                newX = (((mainLineX - targetPoint.x) * Math.cos(Math.toRadians(findAngle)))
		                        - ((mainLineY - targetPoint.y) * Math.sin(Math.toRadians(findAngle))) + targetPoint.x);
		                newY = (((mainLineX - targetPoint.x) * Math.sin(Math.toRadians(findAngle)))
		                        + ((mainLineY - targetPoint.y) * Math.cos(Math.toRadians(findAngle))) + targetPoint.y);

		                returnedPoint.x = newX;
		                returnedPoint.y = newY;
		                System.out.println("Kat o ktory obracamy punkt na prostej " + findAngle);
		                System.out.println("returnedPoint " + returnedPoint);
		                System.out.println();
		                
		                List<Double> newFactors = mathService.abOfFunction(targetPoint.x, targetPoint.y, returnedPoint.x, returnedPoint.y);
		                System.out.println("Wspolczynniki najnowszej wynikowej prostej A = " + newFactors.get(0) + " B = " + newFactors.get(1));
		                return returnedPoint;
		            }
		        } else {

		        }

		        return null;

		}
		return null;
    }

	
	
}
