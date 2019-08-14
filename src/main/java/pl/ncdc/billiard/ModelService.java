package pl.ncdc.billiard;

import java.util.List;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.service.BilliardTableService;

@Service
public class ModelService {

	private Mat obraz;
	// to remove
	private double ballDiameter = 20;
	BilliardTableService bts;

	@Autowired
	public ModelService(BilliardTableService bts) {
		this.obraz = new Mat(1920, 1180, CvType.CV_8UC4);
		this.bts = bts;
	}

	public Mat print() {

		List<Ball> balls = bts.getTable().getBalls();

		for (Ball ball : balls) {
			double x = ball.getPoint().x;
			double y = ball.getPoint().y;
			
			org.opencv.core.Point ballB = new org.opencv.core.Point(x, y);

			Imgproc.circle(obraz, ballB, (int) (ballDiameter/2), new Scalar(0, 0, 255), 10);
		}

		return obraz;
	}

}
