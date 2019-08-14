package pl.ncdc.billiard;

import org.opencv.core.Point;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.Pocket;
import pl.ncdc.billiard.service.BilliardTableService;
import pl.ncdc.billiard.service.HitService;
import pl.ncdc.billiard.service.KinectService;
import pl.ncdc.billiard.service.NewPoint;

@RestController
@RequestMapping("/table")
@CrossOrigin(value = "*")
@EnableScheduling
public class BilliardTableController {

    @Autowired
    BilliardTableService tableService;

    @Autowired
    HitService hitService;

    @Autowired
    KinectService kinectService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("")
    public BilliardTable getTable() {
        return tableService.getTable();
    }

    // to remove
    @Scheduled(fixedRate = 500)
    public void tableLive() {
        simpMessagingTemplate.convertAndSend("/table/live", tableService.getTable());
    }

    @PutMapping("/ball/{ballId}")
    public void selectBall(@PathVariable Long ballId) {
        tableService.selectBall(ballId);
    }

    @PutMapping("/pocket/{pocketId}")
    public void selectPocket(@PathVariable Long pocketId) {
        tableService.selectPocket(pocketId);
    }

	@GetMapping("/hit/")
	public Point findHittingPoint() {
		Ball white = tableService.getTable().getWhiteBall();
		Ball selected = tableService.getTable().getSelectedBall();
		Pocket pocket = tableService.getTable().getSelectedPocket();
		if (white == null || selected == null || pocket == null)
			return null;
		return hitService.findHittingPoint(white.getPoint(), selected.getPoint(), pocket.getPoint(), tableService.getTable().getBalls());

	}

	@PutMapping("/hints")
	public List<NewPoint> allPossibleHits() {
		List<Pocket> listPocket = tableService.getTable().getPockets();
		List<Ball> listBall = tableService.getTable().getBalls();
		Ball white = tableService.getTable().getWhiteBall();
		List<NewPoint> points = hitService.allPossibleHits(listPocket, listBall, white);

		tableService.getTable().setAllPossibleHits(points);

		if (points == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		return points;
	}
}
