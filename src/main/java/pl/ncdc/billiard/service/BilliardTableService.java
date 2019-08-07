package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.entity.Ball;
import pl.ncdc.billiard.entity.BilliardTable;
import pl.ncdc.billiard.entity.Pocket;
import pl.ncdc.billiard.entity.Point;
import pl.ncdc.billiard.repository.BilliardTableRepository;
import pl.ncdc.billiard.response.BallResponse;
import pl.ncdc.billiard.response.BilliardTableResponse;
import pl.ncdc.billiard.response.PocketResponse;
import pl.ncdc.billiard.response.PointResponse;

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
