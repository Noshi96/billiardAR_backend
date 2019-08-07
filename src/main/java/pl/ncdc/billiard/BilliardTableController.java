package pl.ncdc.billiard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.ncdc.billiard.commands.HitCommand;
import pl.ncdc.billiard.entity.Point;
import pl.ncdc.billiard.service.BilliardTableService;
import pl.ncdc.billiard.service.HitService;

@RestController
@RequestMapping("/table")
public class BilliardTableController {

	@Autowired
	BilliardTableService tableService;

	@Autowired
	HitService hitService;

	@GetMapping
	public BilliardTable getTable() {
		return tableService.getTable();
	}

	@PutMapping("/{ballId}")
	public void selectBall(@RequestBody Long ballId) {
		tableService.selectBall(ballId);
	}

	@PutMapping("/{pocketId}")
	public void selectPocket(@RequestBody Long pocketId) {
		tableService.selectPocket(pocketId);
	}

	@PutMapping("/{updatedTable}")
	public BilliardTable update(@RequestBody BilliardTable tableToUpdate) {
		return tableService.update(tableToUpdate);

	}
	@PutMapping("/hit")
	public Point findHittingPoint(@RequestBody HitCommand hitcommand) {

		Point ball = hitcommand.getBall();
		int ballX = ball.getPositionX();
		int ballY = ball.getPositionY();

		Point pocket = hitcommand.getPocket();
		int pocketX = pocket.getPositionX();
		int pocketY = pocket.getPositionY();

		java.awt.Point point = hitService.findHittingPoint(ballX, ballY, pocketX, pocketY);
		return new Point(1L, (int) (point.getX()), ((int)(point.getY())));
	}

}
