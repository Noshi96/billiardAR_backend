package pl.ncdc.billiard.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import pl.ncdc.billiard.models.DifficultyLevel;
import pl.ncdc.billiard.models.IndividualTraining;
import pl.ncdc.billiard.service.BilliardTableService;
import pl.ncdc.billiard.service.IndividualTrainingGameModeService;
import pl.ncdc.billiard.service.IndividualTrainingService;


@RestController
@RequestMapping("/training")
@CrossOrigin(value = "*")
@EnableScheduling
public class IndividualTrainingController {

    private final BilliardTableService tableService;
    private final IndividualTrainingService individualTrainingService;
	private final IndividualTrainingGameModeService individualTrainingGameModeService;

    public IndividualTrainingController(BilliardTableService tableService, IndividualTrainingService individualTrainingService, IndividualTrainingGameModeService individualTrainingGameModeService) {
        this.tableService = tableService;
        this.individualTrainingService = individualTrainingService;
		this.individualTrainingGameModeService = individualTrainingGameModeService;
	}

	@GetMapping("/getAll")
	public List<IndividualTraining> getAll() {
		return individualTrainingService.getAll();
	}

	@GetMapping("/getById/{id}")
	public ResponseEntity<IndividualTraining> getById(@PathVariable Long id) {
		IndividualTraining individualTraining = individualTrainingService.getById(id);
		if (individualTraining == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<IndividualTraining>(individualTraining, HttpStatus.OK);
	}

	@GetMapping("/getByDifficultyLevel/{difficultyLevel}")
	public List<IndividualTraining> getByDifficultyLevel(@PathVariable DifficultyLevel difficultyLevel) {
		return individualTrainingService.getAllByDifficultyLevel(difficultyLevel);
	}

	@GetMapping("/getIdsByDifficultyLevel/{difficultyLevel}")
	public List<Long> getIdsByDifficultyLevel(@PathVariable DifficultyLevel difficultyLevel) {
		return individualTrainingService.getAllByDifficultyLevel(difficultyLevel)
				.stream()
				.map(IndividualTraining::getId)
				.collect(Collectors.toList());
	}

    @PostMapping("/insert")
    public IndividualTraining insert(@RequestBody IndividualTraining individualTraining) {
        return individualTrainingService.saveIndividualTraining(individualTraining);
    }

	@PutMapping("/update")
	public IndividualTraining update(@RequestBody IndividualTraining individualTraining) {
		return individualTrainingService.saveIndividualTraining(individualTraining);
	}

	@PutMapping("/setChallenge/{selectedChallenge}")
	public void setSelectedChallenge(@PathVariable int selectedChallenge) {
		tableService.setSelectedChallenge(selectedChallenge);
		individualTrainingGameModeService.setIndividualTraining(individualTrainingService.getById((long) selectedChallenge));
	}
}
