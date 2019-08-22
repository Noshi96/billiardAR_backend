package pl.ncdc.billiard.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import pl.ncdc.billiard.models.DifficultyLevel;
import pl.ncdc.billiard.models.IndividualTraining;
import pl.ncdc.billiard.service.BilliardTableService;
import pl.ncdc.billiard.service.IndividualTrainingService;


@RestController
@RequestMapping("/training")
@CrossOrigin(value = "*")
@EnableScheduling
public class IndividualTrainingController {

    private final BilliardTableService tableService;
    private final IndividualTrainingService individualTrainingService;

    public IndividualTrainingController(BilliardTableService tableService, IndividualTrainingService individualTrainingService) {
        this.tableService = tableService;
        this.individualTrainingService = individualTrainingService;
    }

	@GetMapping(path = "/getAll")
	public List<IndividualTraining> getAll() {
		return individualTrainingService.getAll();
	}

	@GetMapping(path = "/getById/{id}")
	public ResponseEntity<IndividualTraining> getById(@PathVariable Long id) {
		IndividualTraining individualTraining = individualTrainingService.getById(id);
		if (individualTraining == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<IndividualTraining>(individualTraining, HttpStatus.OK);
	}

	@GetMapping(value = "/getByDifficultyLevel/{difficultyLevel}")
	public List<IndividualTraining> getByDifficultyLevel(@PathVariable DifficultyLevel difficultyLevel) {
		return individualTrainingService.getAllByDifficultyLevel(difficultyLevel);
	}

    @PostMapping(value = "/insert")
    public IndividualTraining insert(@RequestBody IndividualTraining individualTraining) {
        return individualTrainingService.saveIndividualTraining(individualTraining);
    }

	@PutMapping(value = "/update")
	public IndividualTraining update(@RequestBody IndividualTraining individualTraining) {
		return individualTrainingService.saveIndividualTraining(individualTraining);
	}
}
