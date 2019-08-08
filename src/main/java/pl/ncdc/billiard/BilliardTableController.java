package pl.ncdc.billiard;

import java.awt.Point;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pl.ncdc.billiard.service.BilliardTableService;
import pl.ncdc.billiard.service.HitService;
import pl.ncdc.billiard.websocket.SocketHandler;

@RestController
@RequestMapping("/table")
public class BilliardTableController {

	@Autowired
	BilliardTableService tableService;

	@Autowired
	HitService hitService;
	
	@Autowired
	SocketHandler socketHandler;
	
	@GetMapping
	public BilliardTable getTable() {
		return tableService.getTable();
	}

	@PutMapping("/ball/{ballId}")
	public void selectBall(@RequestBody Long ballId) {
		tableService.selectBall(ballId);
	}

	@PutMapping("/pocket/{pocketId}")
	public void selectPocket(@RequestBody Long pocketId) {
		tableService.selectPocket(pocketId);
	}

	@PutMapping
	public BilliardTable update(@RequestBody BilliardTable tableToUpdate) {
		return tableService.update(tableToUpdate);

	}
//	@PutMapping("/hit")
//	public Point findHittingPoint(@RequestBody HitCommand hitcommand) {
//		Point ball = hitcommand.getBall();
//		double ballX = ball.getX();
//		double ballY = ball.getY();
//		
//		Point pocket = hitcommand.getPocket();
//		double pocketX = pocket.getX();
//		double pocketY = pocket.getY();
//		
//		return hitService.findHittingPoint(ballX, ballY, pocketX, pocketY);
//
//	}

	@PutMapping("/hit")
	public Point findHittingPoint() {
		Point ball = tableService.getTable().getSelectedBall().getPoint();
		double ballX = ball.getX();
		double ballY = ball.getY();

		Point pocket = tableService.getTable().getSelectedPocket().getPoint();
		double pocketX = pocket.getX();
		double pocketY = pocket.getY();

		Point hittingPoint = hitService.findHittingPoint(ballX, ballY, pocketX, pocketY);
		
		tableService.getTable().setHittingPoint(hittingPoint);
		socketHandler.sendToAll(tableService.getTable());
		
		if (hittingPoint != null) {
			return hittingPoint;
		}

		throw new ResponseStatusException(HttpStatus.NOT_FOUND);

	}

}
