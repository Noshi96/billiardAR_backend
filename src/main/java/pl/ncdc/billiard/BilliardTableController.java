package pl.ncdc.billiard;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;

import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pl.ncdc.billiard.entity.Ball;
import pl.ncdc.billiard.entity.Pocket;
import pl.ncdc.billiard.service.BilliardTableService;
import pl.ncdc.billiard.service.HitService;
import pl.ncdc.billiard.websocket.SocketHandler;

@RestController
@RequestMapping("/table")
@CrossOrigin(value = "*")
public class BilliardTableController {

	@Autowired
	BilliardTableService tableService;

	@Autowired
	HitService hitService;

	@Autowired
	SocketHandler socketHandler;

	@Autowired
	ModelService modelService;

	@GetMapping("")
	public BilliardTable getTable() {
		return tableService.getTable();
	}

	@PutMapping("/ball/{ballId}")
	public void selectBall(@PathVariable Long ballId) {
		tableService.selectBall(ballId);
	}

	@PutMapping("/pocket/{pocketId}")
	public void selectPocket(@PathVariable Long pocketId) {
		tableService.selectPocket(pocketId);
	}

	@PutMapping
	@MessageMapping("/table")
	@SendTo("/table/update")
	public BilliardTable update(@RequestBody BilliardTable tableToUpdate) {
		return tableService.update(tableToUpdate);
	}

	@PutMapping("/hit/{x1}/{y1}/{x2}/{y2}/{x3}/{y3}")
	public Point findHittingPoint(@PathVariable double x1,@PathVariable double y1,@PathVariable double x2,@PathVariable double y2,@PathVariable double x3,@PathVariable double y3) {
		if (tableService.getTable().getSelectedBall() == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		if (tableService.getTable().getWhiteBall() == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		if (tableService.getTable().getSelectedPocket() == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		Point ball = tableService.getTable().getSelectedBall().getPoint();
		double ballX = x1;//ball.getX();
		double ballY = y1;//ball.getY();

		Point whiteBall = tableService.getTable().getWhiteBall().getPoint();
		double whiteBallX = x2;//whiteBall.getX();
		double whiteBallY = y2;//whiteBall.getY();

		Point pocket = tableService.getTable().getSelectedPocket().getPoint();
		double pocketX = x3;//pocket.getX();
		double pocketY = y3;//pocket.getY();

		Point hittingPoint = hitService.findHittingPoint(whiteBallX, whiteBallY, ballX, ballY, pocketX, pocketY);

		tableService.getTable().setHittingPoint(hittingPoint);
		// socketHandler.sendToAll(tableService.getTable());

		if (hittingPoint != null) {
			return hittingPoint;
		}

		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}

	@PutMapping("/hints")
	public HashMap<Point, Point> allPossibleHits() {
		List<Pocket> listPocket = tableService.getTable().getPockets();
		List<Ball> listBall = tableService.getTable().getBalls();

		HashMap<Point, Point> points = hitService.allPossibleHits(listPocket, listBall);

		tableService.getTable().setAllPossibleHits(points);
		//socketHandler.sendToAll(tableService.getTable());

		if (points != null) {
			return points;
		}

		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/print")
	public Mat print() {
		return modelService.print();
	}

}
