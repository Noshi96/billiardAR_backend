package pl.ncdc.billiard;

import org.opencv.core.Point;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

//import pl.ncdc.billiard.commands.HitCommand;
import pl.ncdc.billiard.entity.Ball;
import pl.ncdc.billiard.entity.IndividualTraining;
import pl.ncdc.billiard.entity.Pocket;
import pl.ncdc.billiard.service.BilliardTableService;
import pl.ncdc.billiard.service.HitService;
//import pl.ncdc.billiard.websocket.SocketHandler;
import pl.ncdc.billiard.service.IndividualTrainingService;

import org.springframework.web.bind.annotation.*;

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

//	@Autowired
//	SocketHandler socketHandler;

	@Autowired
	ModelService modelService;

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

	@PutMapping
	public BilliardTable update(@RequestBody BilliardTable tableToUpdate) {
		return tableService.update(tableToUpdate);

	}
//	@PutMapping("/hit")
//	public Point findHittingPoint(@RequestBody HitCommand hitcommand) {
//		Point ball = hitcommand.getBall();
//		double ballX = ball.getX();
//		double ballY = ball.getY();
//		
//		Point pocket = hitcommand.getPocket();
//		double pocketX = pocket.getX();
//		double pocketY = pocket.getY();
//		
//		return hitService.findHittingPoint(ballX, ballY, pocketX, pocketY);
//
//	}

	@PutMapping("/hit/{x1}/{y1}/{x2}/{y2}/{x3}/{y3}")
	public Point findHittingPoint(@PathVariable double x1,@PathVariable double y1,@PathVariable double x2,@PathVariable double y2,@PathVariable double x3,@PathVariable double y3) {
		if (tableService.getTable().getSelectedBall() == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

    @Autowired
    HitService hitService;

    @Autowired
    ModelService modelService;

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
	public Point findHittingPoint() {
		Ball white = tableService.getTable().getWhiteBall();
		Ball selected = tableService.getTable().getSelectedBall();
		Pocket pocket = tableService.getTable().getSelectedPocket();
		if (white == null || selected == null || pocket == null)
			return null;
		return hitService.findHittingPoint(white.getPoint(), selected.getPoint(), pocket.getPoint());

	}

	@PutMapping("/hints")
	public List<NewPoint> allPossibleHits() {
		List<Pocket> listPocket = tableService.getTable().getPockets();
		List<Ball> listBall = tableService.getTable().getBalls();
		Ball white = tableService.getTable().getWhiteBall();
		List<NewPoint> points = hitService.allPossibleHits(listPocket, listBall, white);

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
    
    @RequestMapping(value = "/trainingDataBaseByLvl/{lvl}", method = RequestMethod.GET)
    public List<IndividualTraining> getArticleById(@PathVariable String lvl){
        return individualTrainingService.getIndividualTrainingByLvl(lvl);
    }

}
