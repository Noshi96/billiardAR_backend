package pl.ncdc.billiard.service;

import org.springframework.stereotype.Service;

import pl.ncdc.billiard.BilliardTable;
import pl.ncdc.billiard.entity.Ball;
import pl.ncdc.billiard.entity.Pocket;

@Service
public class BilliardTableService {

	private BilliardTable table = new BilliardTable();

	public BilliardTable getTable() {
		return table;
	}

	public void selectBall(Long ballId) {
		for (Ball ball : table.getBalls()) {
			table.setSelectedBall(null);

			if (ball.getId() == ballId) {
				table.setSelectedBall(ball);
			}
		}
	}

	public void selectPocket(Long pocketId) {
		for (Pocket pocket : table.getPockets()) {
			table.setSelectedPocket(null);

			if (pocket.getId() == pocketId) {
				table.setSelectedPocket(pocket);
			}
		}
	}

	public BilliardTable update(BilliardTable tableToUpdate) {
		for (Ball ballInOpenCv : tableToUpdate.getBalls()) {
			boolean ballToUpdateIsInTable = false;

			for (Ball ball : table.getBalls()) {
				if (ball.getId() == ballInOpenCv.getId()) {
					ballToUpdateIsInTable = true;
					ball.setPoint(ballInOpenCv.getPoint());
					break;
				}
			}

			if (ballToUpdateIsInTable = false) {
				table.getBalls().add(ballInOpenCv);
			}

			boolean isBallInOpenCv = false;

			for (Ball ball : table.getBalls()) {
				for (Ball ballOpenCv : tableToUpdate.getBalls()) {
					if (ball.getId() == ballOpenCv.getId()) {
						isBallInOpenCv = true;
					}
				}
				if (isBallInOpenCv == false) {
					table.getBalls().remove(ball);
				}
			}
		}

		return table;
	}
}
