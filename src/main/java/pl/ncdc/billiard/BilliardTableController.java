package pl.ncdc.billiard;

import org.opencv.core.Point;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import pl.ncdc.billiard.models.IndividualTraining;
import pl.ncdc.billiard.commands.IndividualTrainingCommand;
import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.Pocket;
import pl.ncdc.billiard.service.BilliardTableService;
import pl.ncdc.billiard.service.HitService;
import pl.ncdc.billiard.service.IndividualTrainingService;
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

	    //Koala
//    @Scheduled(fixedRate = 5000)
//    public void tableLive() {
//    simpMessagingTemplate.convertAndSend("/table/live", tableService.getTable());
//    }
//    
//    @Autowired
//    SimpMessagingTemplate simpMessagingTemplate;

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

	@PutMapping("/hit")
	public List<Point> findHittingPoint() {
		Ball white = tableService.getTable().getWhiteBall();
		Ball selected = tableService.getTable().getSelectedBall();
		Pocket pocket = tableService.getTable().getSelectedPocket();
		int idPocket = tableService.getTable().getSelectedPocket().getId();
		if (white == null || selected == null || pocket == null)
			return null;

		return hitService.findHittingPoint(white.getPoint(), selected.getPoint(), pocket.getPoint(), tableService.getTable().getBalls(), idPocket);
	}

	@PutMapping("/kalibracja")
	public void Kalibracja(@RequestBody List<Point> list) {
		if (list != null || list.size() != 4) {
			int leftMargin = (int) list.get(0).x;
			int topMargin = (int) list.get(0).y;
			int height = (int) (list.get(1).x - list.get(0).x);
			int width = (int) (list.get(1).y - list.get(0).y);

			if (leftMargin < 1 || topMargin < 1 || width < 1 || height < 1)
				return;

			kinectService.setLeft_margin(leftMargin);
			kinectService.setTop_margin(topMargin);
			kinectService.setAreaHeight(height);
			kinectService.setAreaWidth(width);

			this.kinectService.saveProperties();
		}
	}

	@PutMapping("/hints")
	public List<NewPoint> allPossibleHits() {
		List<Pocket> listPocket = tableService.getTable().getPockets();
		List<Ball> listBall = tableService.getTable().getBalls();
		Ball white = tableService.getTable().getWhiteBall();
		int idPocket = tableService.getTable().getSelectedPocket().getId();
		List<NewPoint> points = hitService.allPossibleHits(listPocket, listBall, white, idPocket);

		tableService.getTable().setAllPossibleHits(points);

		if (points == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		return points;
	}   
      
}

