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
	
	@Autowired
	private PoolDrawerService poolDrawerService;

	public BilliardTable getTable() {
		return table;
	}

	public byte[] drawPoolImage() {
		return poolDrawerService.drawImage(table);
	}
	
	public void selectBall(Point point) {
		// table.setSelectedBall(null);
		table.setSelectedBall(new Ball(0, table.findBallByPoint(point)));
		System.out.println(table.getSelectedBall().getPoint());
	}
	
	public void selectPocket(Long pocketId) {
		table.setSelectedPocket(null);
		for (Pocket pocket : table.getPockets()) {
			if (pocket.getId() == pocketId) {
				table.setSelectedPocket(pocket);
			}
		}
	}

	/**
	 * Calculate new table height, width, and pockets positions
	 * 
	 * @param calibrationParams
	 */
	public void updateCalibration(CalibrationParams calibrationParams) {

		Point leftTop = calibrationParams.getLeftUpperCorner();
		Point leftBottom = calibrationParams.getLeftBottomCorner();
		Point rightBottom = calibrationParams.getRightBottomCorner();
		Point rightTop = calibrationParams.getRightUpperCorner();

		// calculate new area size
		int width = (int) Math.abs((rightTop.x + rightBottom.x - leftTop.x - leftBottom.x) / 2);
		int height = (int) Math.abs((leftBottom.y + rightBottom.y - leftTop.y - rightTop.y) / 2);
		
		this.table.setWidth(width);
		this.table.setHeight(height);
		
		this.table.setBallRadius(calibrationParams.getBallDiameter()/2);

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
	
	public void setViewMode(int viewMode) {
		table.setSelectedViewMode(viewMode);
	}

	public void setSelectedChallenge(int selectedChallenge) {
		table.setSelectedChallenge(selectedChallenge);
	}
}
