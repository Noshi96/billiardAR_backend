package pl.ncdc.billiard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.ncdc.billiard.response.BilliardTableResponse;
import pl.ncdc.billiard.service.BilliardTableService;

@RestController
@RequestMapping("/table")
public class BilliardTableController {

	@Autowired
	BilliardTableService bs;

	@GetMapping
	public BilliardTableResponse billiardTable() {
		// return bs.billiardTable();
		return bs.getTable();
	}

	@PutMapping("/{id}")
	public void selectBall(@RequestBody Long ballId) {
		tableService.selectBall(ballId);
	}

}
