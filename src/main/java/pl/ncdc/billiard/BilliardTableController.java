package pl.ncdc.billiard;

import java.util.List;

import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

	private final BilliardTableService tableService;
	private final HitService hitService;
	private final KinectService kinectService;
	private final IndividualTrainingService individualTrainingService;
	private final SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	public BilliardTableController(BilliardTableService tableService, HitService hitService, KinectService kinectService,
								   IndividualTrainingService individualTrainingService, SimpMessagingTemplate simpMessagingTemplate) {
		this.tableService = tableService;
		this.hitService = hitService;
		this.kinectService = kinectService;
		this.individualTrainingService = individualTrainingService;
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

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

