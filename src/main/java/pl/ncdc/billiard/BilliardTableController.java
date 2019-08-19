package pl.ncdc.billiard;

import java.util.List;

import org.opencv.core.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pl.ncdc.billiard.commands.IndividualTrainingCommand;
import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.IndividualTraining;
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

	public BilliardTableController(BilliardTableService tableService, HitService hitService, KinectService kinectService,
								   IndividualTrainingService individualTrainingService, SimpMessagingTemplate simpMessagingTemplate) {
		this.tableService = tableService;
		this.hitService = hitService;
		this.kinectService = kinectService;
		this.individualTrainingService = individualTrainingService;
		this.simpMessagingTemplate = simpMessagingTemplate;
	}
	
	
    // Koala
    @Scheduled(fixedRate = 500)
    public void tableLive() {
    simpMessagingTemplate.convertAndSend("/table/live", tableService.getTable());
    }
    // dodane przez Koala
	@PutMapping("/setViewMode/{viewMode}")
	public void setViewMode(@PathVariable int viewMode) {
		tableService.setViewMode(viewMode);
	}
	// dodane przez Koala
	@PutMapping("/setChallenge/{selectedChallenge}")
	public void setSelectedChallenge(@PathVariable int selectedChallenge) {
		tableService.setSelectedChallenge(selectedChallenge);
	}

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
    
    @RequestMapping(value = "/updateIndividualTraining/{id}", method = RequestMethod.POST)
    public IndividualTraining updateIndividualTraining(@RequestBody IndividualTrainingCommand individualTrainingCommand, @PathVariable Long id){
        return individualTrainingService.updateIndividualTraining(individualTrainingCommand, id);
    }
    
    @RequestMapping(value = "/getIndividualTrainingsByLvl/{lvl}", method = RequestMethod.GET)
    public List<IndividualTraining> getArticleById(@PathVariable String lvl){
        return individualTrainingService.sortListByLvl(lvl);
    }
        
	@RequestMapping(method = RequestMethod.GET, path = "/fetchTreningById/{id}")
	public ResponseEntity<IndividualTraining> fetch(@PathVariable Long id) {
		IndividualTraining individualTraining = individualTrainingService.fetch(id);
		if(individualTraining == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<IndividualTraining>(individualTraining, HttpStatus.OK);
	}
	
	@RequestMapping(method  = RequestMethod.GET, path="/fetchAll")
	public List<IndividualTraining> listPerson(){
		return individualTrainingService.fetchAll();
	}
		
    @RequestMapping(value = "/getTreningAllInfoById/{id}", method = RequestMethod.GET)
    public List<IndividualTrainingCommand> returnIndividualTrainingCommand(@PathVariable long id){
        return individualTrainingService.returnIndividualTrainingCommand(id);
    }
    
	@RequestMapping(method = RequestMethod.POST, path="/save")
	public IndividualTraining save(@RequestBody IndividualTrainingCommand individualTrainingCommand) {
		return individualTrainingService.save(individualTrainingCommand);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, path="/deleteIndividualTrenig/{id}")
	public IndividualTraining delete(@PathVariable Long id) {
		return individualTrainingService.delete(id);
	}
    
}

