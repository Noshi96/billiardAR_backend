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

	@Autowired
	BilliardTableRepository billiardTableRepository;
	
	//BillardTable table;

	public BilliardTableResponse billiardTable() {
		BilliardTableResponse billiardTableResponse = new BilliardTableResponse();
		BilliardTable bt = billiardTableRepository.findAll().get(0);
		billiardTableResponse.setId(bt.getId());

		List<BallResponse> ballsResponse = new ArrayList<>();

		for (Ball ball : bt.getBalls()) {
			BallResponse ballResponse = new BallResponse();
			ballResponse.setId(ball.getId());
			
			
			PointResponse pointResponse = new PointResponse();
			Point point = new Point();
			pointResponse.setPositionX(point.getPositionX());
			pointResponse.setPositionY(point.getPositionY());
			
			ballResponse.setPoint(point);

			ballResponse.setSelected(ball.isSelected());
			ballResponse.setWhite(ball.isWhite());
			ballsResponse.add(ballResponse);
		}

		List<PocketResponse> pocketResponses = new ArrayList<>();

		for (Pocket pocket : bt.getPockets()) {
			PocketResponse pocketResponse = new PocketResponse();
			pocketResponse.setId(pocket.getId());
			pocketResponse.setSelected(pocket.isSelected());
			pocketResponse.setBalls(new ArrayList<>());
			pocketResponses.add(pocketResponse);
		}

		billiardTableResponse.setBalls(ballsResponse);
		billiardTableResponse.setPockets(pocketResponses);

		return billiardTableResponse;
	}

}
