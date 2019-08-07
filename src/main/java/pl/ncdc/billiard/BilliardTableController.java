package pl.ncdc.billiard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.ncdc.billiard.service.BilliardTableService;

@RestController
@RequestMapping("/table")
public class BilliardTableController {


	@Autowired
	BilliardTableService tableService;
	
	@GetMapping
	public BilliardTable getTable() {
		return tableService.getTable();
	}
	
	@PutMapping
	public void selectBall(@RequestBody Long ballId) {
		tableService.selectBall(ballId);
	}

}
