package pl.ncdc.billard;
import java.awt.Point;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.ncdc.billard.commands.HitCommand;
import pl.ncdc.billard.service.BilliardTableService;
import pl.ncdc.billard.service.HitService;

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
		double ballX = ball.getX();
		double ballY = ball.getY();
		
		Point pocket = hitcommand.getPocket();
		double pocketX = pocket.getX();
		double pocketY = pocket.getY();
		
		return hitService.findHittingPoint(ballX, ballY, pocketX, pocketY);

	}

}
