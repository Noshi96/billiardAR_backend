package pl.ncdc.billiard;

import org.opencv.core.Point;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import pl.ncdc.billiard.service.NewPoint;
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
	public BilliardTable update(@RequestBody BilliardTable tableToUpdate) {
		return tableService.update(tableToUpdate);

	}

	@PutMapping("/hit/")
	public Point findHittingPoint() {
		Ball white = tableService.getTable().getWhiteBall();
		Ball selected = tableService.getTable().getSelectedBall();
		Pocket pocket = tableService.getTable().getSelectedPocket();
		if (white == null || selected == null || pocket == null)
			return null;
		return hitService.findHittingPoint(white.getPoint(), selected.getPoint(), pocket.getPoint());

	}

	@PutMapping("/hints")
	public List<NewPoint> allPossibleHits() {
		List<Pocket> listPocket = tableService.getTable().getPockets();
		List<Ball> listBall = tableService.getTable().getBalls();
		Ball white = tableService.getTable().getWhiteBall();
		List<NewPoint> points = hitService.allPossibleHits(listPocket, listBall, white);

		tableService.getTable().setAllPossibleHits(points);
		// socketHandler.sendToAll(tableService.getTable());

		if (points == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		return points;
	}
}
