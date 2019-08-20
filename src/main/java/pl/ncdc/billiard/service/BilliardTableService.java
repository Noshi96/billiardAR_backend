package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.CalibrationParams;
import pl.ncdc.billiard.models.Pocket;

@Service
public class BilliardTableService {

	@Autowired
	private BilliardTable table;

	public BilliardTable getTable() {
		return table;
	}

	public void selectBall(Long ballId) {
		table.setSelectedBall(null);

		for (Ball ball : table.getBalls()) {
			if (ball.getId() == ballId) {
				table.setSelectedBall(ball);
			}
		}
	}

	/**
	 * Calculate new table height, width, and pockets positions 
	 * @param calibrationParams
	 */
	public void updateCalibration(CalibrationParams calibrationParams) {

		Point leftTop = calibrationParams.getLeftUpperCorner();
		Point leftBottom = calibrationParams.getLeftBottomCorner();
		Point rightBottom = calibrationParams.getRightBottomCorner();
		Point rightTop = calibrationParams.getRightUpperCorner();

		// calculate new area size
		int width = (int) (rightTop.x + rightBottom.x - leftTop.x - leftBottom.x) / 2;
		int height = (int) (leftBottom.y + rightBottom.y - leftTop.y - rightTop.y) / 2;
		this.table.setWidth(width);
		this.table.setHeight(height);

		// add pockets
		List<Pocket> pockets = new ArrayList<Pocket>();
		// top left
		pockets.add(new Pocket(0, new Point(0, 0)));
		// top middle
		pockets.add(new Pocket(1, new Point(width / 2, 0)));
		// top right
		pockets.add(new Pocket(2, new Point(width, 0)));
		// bottom right
		pockets.add(new Pocket(3, new Point(width, height)));
		// bottom middle
		pockets.add(new Pocket(4, new Point(width / 2, height)));
		// bottom left
		pockets.add(new Pocket(5, new Point(0, height)));
		
		this.table.setPockets(pockets);
	}

	public void selectPocket(Long pocketId) {
		table.setSelectedPocket(null);
		for (Pocket pocket : table.getPockets()) {
			if (pocket.getId() == pocketId) {
				table.setSelectedPocket(pocket);
			}
		}
	}
}
