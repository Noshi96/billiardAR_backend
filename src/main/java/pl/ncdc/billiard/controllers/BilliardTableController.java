package pl.ncdc.billiard.controllers;

import java.util.List;

import org.opencv.core.Point;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.Informations;
import pl.ncdc.billiard.models.Pocket;
import pl.ncdc.billiard.service.BilliardTableService;
import pl.ncdc.billiard.service.HiddenPlacesService;
import pl.ncdc.billiard.service.HitService;
import pl.ncdc.billiard.service.IndividualTrainingService;
import pl.ncdc.billiard.service.KinectService;
import pl.ncdc.billiard.service.NewPoint;
import pl.ncdc.billiard.service.PoolDrawerService;

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
	private final HiddenPlacesService hiddenPlacesService;
	private final PoolDrawerService poolDrawerService;

	public BilliardTableController(BilliardTableService tableService, HitService hitService,
			KinectService kinectService, IndividualTrainingService individualTrainingService,
			SimpMessagingTemplate simpMessagingTemplate, HiddenPlacesService hiddenPlacesService,
			PoolDrawerService poolDrawerService) {
		this.tableService = tableService;
		this.hitService = hitService;
		this.kinectService = kinectService;
		this.individualTrainingService = individualTrainingService;
		this.simpMessagingTemplate = simpMessagingTemplate;
		this.hiddenPlacesService = hiddenPlacesService;
		this.poolDrawerService = poolDrawerService;
	}
	
	
	@GetMapping("")
	public BilliardTable getTable() {
		return tableService.getTable();
	}

	@Scheduled(fixedRate = 500)
	public void tableLive() {
		simpMessagingTemplate.convertAndSend("/table/live", tableService.getTable());
	}
	
	@Scheduled(fixedRate = 500)
	public void drawingLive() {
		simpMessagingTemplate.convertAndSend("/table/draw", poolDrawerService.drawImage());
	}
	
	@PutMapping("/ball")
	public void selectBall(@RequestBody Point point) {
		tableService.selectBall(point);
	}

	@PutMapping("/pocket/{pocketId}")
	public void selectPocket(@PathVariable Long pocketId) {
		tableService.selectPocket(pocketId);
	}

	@PutMapping("/setViewMode/{viewMode}")
	public void setViewMode(@PathVariable int viewMode) {
		tableService.setViewMode(viewMode);
	}

	@PutMapping("/setChallenge/{selectedChallenge}")
	public void setSelectedChallenge(@PathVariable int selectedChallenge) {
		tableService.setSelectedChallenge(selectedChallenge);
	}

	@PutMapping("/hit")
	public List<Point> findHittingPoint() {
		Ball white = tableService.getTable().getWhiteBall();
		Ball selected = tableService.getTable().getSelectedBall();
		Pocket pocket = tableService.getTable().getSelectedPocket();
		int idPocket = tableService.getTable().getSelectedPocket().getId();
		if (white == null || selected == null || pocket == null)
			return null;

		return hitService.findHittingPoint(white.getPoint(), selected.getPoint(), pocket.getPoint(),
				tableService.getTable().getBalls(), idPocket);
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
	
	@PutMapping("/bestPocket")
	public int findBestPocket() {
		Point white = tableService.getTable().getWhiteBall().getPoint();
		Point selected = tableService.getTable().getSelectedBall().getPoint();
		int idPocket = tableService.getTable().getSelectedPocket().getId();
		List<Ball> listBall = tableService.getTable().getBalls();
		List<Pocket> listPocket = tableService.getTable().getPockets();
		
		if (white == null || selected == null) {
			return -1;
		}
		
		return hitService.findBestPocket(white, selected, listPocket, listBall, idPocket);
		
	}
	
	@PutMapping("/hiddenPlaces")
	public List<Point> showHiddenPlaces(){
		
		Point white = tableService.getTable().getWhiteBall().getPoint();
		List<Ball> listBall = tableService.getTable().getBalls();
		
		return hiddenPlacesService.showHiddenPlaces(white, listBall);	
	}
	
	@PutMapping("/getInfo")
	public Informations getHitInfo() {
		
		Point white = tableService.getTable().getWhiteBall().getPoint();
		Point selected = tableService.getTable().getSelectedBall().getPoint();
		Point pocket = tableService.getTable().getSelectedPocket().getPoint();
		int idPocket = tableService.getTable().getSelectedPocket().getId();
		List<Ball> listBall = tableService.getTable().getBalls();
		
		if (white == null || selected == null || pocket == null) {
			return null;
		}
		
		return hitService.getHitInfo(white, selected, pocket, listBall, idPocket);
			
	}
	
//	@PutMapping("/kalibracja")
//	public void Kalibracja(@RequestBody List<Point> list) {
//		if (list != null || list.size() != 4) {
//			int leftMargin = (int) list.get(0).x;
//			int topMargin = (int) list.get(0).y;
//			int height = (int) (list.get(1).x - list.get(0).x);
//			int width = (int) (list.get(1).y - list.get(0).y);
//
//			if (leftMargin < 1 || topMargin < 1 || width < 1 || height < 1)
//				return;
//
//			kinectService.setLeft_margin(leftMargin);
//			kinectService.setTop_margin(topMargin);
//			kinectService.setAreaHeight(height);
//			kinectService.setAreaWidth(width);
//
//			this.kinectService.saveProperties();
//		}
//	}

}
