package pl.ncdc.billiard.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import pl.ncdc.billiard.models.IndividualTraining;
import pl.ncdc.billiard.commands.IndividualTrainingCommand;
import pl.ncdc.billiard.service.BilliardTableService;
import pl.ncdc.billiard.service.IndividualTrainingService;


@RestController
@RequestMapping("/table")
@CrossOrigin(value = "*")
@EnableScheduling
public class IndividualTrainingController {

	@Autowired
	BilliardTableService tableService;
	
    @Autowired
    IndividualTrainingService individualTrainingService;
	
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
