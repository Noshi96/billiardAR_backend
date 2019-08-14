package pl.ncdc.billiard;

import org.opencv.core.Point;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
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
import pl.ncdc.billiard.service.NewPoint;
//import pl.ncdc.billiard.websocket.SocketHandler;

@RestController
@RequestMapping("/table")
@CrossOrigin(value = "*")
public class BilliardTableController {

    @Autowired
    BilliardTableService tableService;
    
    @Autowired
    IndividualTrainingService individualTrainingService;

    @Autowired
    HitService hitService;


    @SendTo("/table/live")
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

	@PutMapping("/hit/")
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
		// socketHandler.sendToAll(tableService.getTable());

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

