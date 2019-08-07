package pl.ncdc.billiard.service;

import org.springframework.stereotype.Service;

import pl.ncdc.billiard.BilliardTable;
import pl.ncdc.billiard.entity.Ball;

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
}
