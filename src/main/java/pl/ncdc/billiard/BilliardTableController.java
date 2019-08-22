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
import pl.ncdc.billiard.models.Informations;
import pl.ncdc.billiard.commands.IndividualTrainingCommand;
import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.Pocket;
import pl.ncdc.billiard.service.BilliardTableService;
import pl.ncdc.billiard.service.HiddenPlacesService;
import pl.ncdc.billiard.service.HitService;
import pl.ncdc.billiard.service.IndividualTrainingService;
import pl.ncdc.billiard.service.KinectService;
import pl.ncdc.billiard.service.NewPoint;
import pl.ncdc.billiard.service.HiddenPlacesService;

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
	HiddenPlacesService hiddenPlacesService;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@GetMapping("")
	public BilliardTable getTable() {
		return tableService.getTable();
	}

	@PutMapping("/ball/{ballId}")
	public void selectBall(@PathVariable Long ballId) {
		tableService.selectBall(ballId);
	}

    @Autowired
    IndividualTrainingService individualTrainingService;

	@PutMapping("/pocket/{pocketId}")
	public void selectPocket(@PathVariable Long pocketId) {
		tableService.selectPocket(pocketId);
	}

	@PutMapping("/hit/")
	public List<Point> findHittingPoint() {
		Ball white = tableService.getTable().getWhiteBall();
		Ball selected = tableService.getTable().getSelectedBall();
		Pocket pocket = tableService.getTable().getSelectedPocket();
		int idPocket = tableService.getTable().getSelectedPocket().getId();
		if (white == null || selected == null || pocket == null) {
			return null;
		}
		return hitService.findHittingPoint(white.getPoint(), selected.getPoint(), pocket.getPoint(), tableService.getTable().getBalls(), idPocket);
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


	
    @GetMapping("/fetchFromDatabase")
    public List<IndividualTraining> testSql() {
        return individualTrainingService.getIndividualTraining();
    }
    
    
    @RequestMapping(value="/trainingDataBase", method=RequestMethod.POST)
    public String insertUser(@RequestBody IndividualTraining individualTraining){
    	individualTrainingService.createIndividualTraining(individualTraining);
        return "Training created successfully";
    }
    
    @RequestMapping(value = "/trainingDataBase/{id}", method = RequestMethod.GET)
    public List<IndividualTraining> getArticleById(@PathVariable int id){
        return individualTrainingService.getIndividualTrainingById(id);
    }
    
    @RequestMapping(value = "/getTrainingDataBaseByLvl/{lvl}", method = RequestMethod.GET)
    public List<IndividualTraining> getArticleById(@PathVariable String lvl){
        return individualTrainingService.getIndividualTrainingByLvl(lvl);
    }
    
    @RequestMapping(value = "/returnPoints/{id}", method = RequestMethod.GET)
    public List<IndividualTrainingCommand> returnPointsById(@PathVariable int id){
        return individualTrainingService.returnPoints(id);
    }
    
	@RequestMapping(method = RequestMethod.GET, path = "/fetchTreningById/{id}")
	public ResponseEntity<IndividualTraining> fetch(@PathVariable Long id) {
		IndividualTraining individualTraining = individualTrainingService.fetch(id);
		if(individualTraining == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<IndividualTraining>(individualTraining, HttpStatus.OK);
	}
	
    @RequestMapping(value = "/getTreningAllInfoById/{id}", method = RequestMethod.GET)
    public List<IndividualTrainingCommand> returnIndividualTrainingCommand(@PathVariable long id){
        return individualTrainingService.returnIndividualTrainingCommand(id);
    }
    

}

