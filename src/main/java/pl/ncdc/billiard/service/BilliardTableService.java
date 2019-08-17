package pl.ncdc.billiard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.CalibrationParams;
import pl.ncdc.billiard.models.Pocket;

@Service
public class BilliardTableService {

	@Autowired
    private BilliardTable table;

	private CalibrationParams calibrationParams;

    public BilliardTableService() {
    }

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

	public void selectPocket(Long pocketId) {
		table.setSelectedPocket(null);
		for (Pocket pocket : table.getPockets()) {
			if (pocket.getId() == pocketId) {
				table.setSelectedPocket(pocket);
			}
		}
	}

    public void updateCalibrationParams(CalibrationParams calibrationParams) {
    	this.calibrationParams = calibrationParams;
    }
}
