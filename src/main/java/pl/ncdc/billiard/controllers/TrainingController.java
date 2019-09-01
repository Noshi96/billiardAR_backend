package pl.ncdc.billiard.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import pl.ncdc.billiard.models.training.DifficultyLevel;
import pl.ncdc.billiard.models.training.Training;
import pl.ncdc.billiard.service.BilliardTableService;
import pl.ncdc.billiard.service.training.TrainingModeService;
import pl.ncdc.billiard.service.training.TrainingService;


@RestController
@RequestMapping("/training")
@CrossOrigin(value = "*")
@EnableScheduling
public class TrainingController {

    private final BilliardTableService tableService;
    private final TrainingService trainingService;
	private final TrainingModeService trainingModeService;

    public TrainingController(BilliardTableService tableService, TrainingService trainingService, TrainingModeService trainingModeService) {
        this.tableService = tableService;
        this.trainingService = trainingService;
		this.trainingModeService = trainingModeService;
	}

	@GetMapping("/getById/{id}")
	public ResponseEntity<Training> getById(@PathVariable Long id) {
		Training training = trainingService.getById(id);
		if (training == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Training>(training, HttpStatus.OK);
	}

	@GetMapping("/getInPixelById/{id}")
	public ResponseEntity<Training> getInPixelById(@PathVariable Long id) {
		Training training = trainingService.getInPixelById(id);
		if (training == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Training>(training, HttpStatus.OK);
	}

	@GetMapping(value = "/getByDifficultyLevel/{difficultyLevel}", params = {"page", "size"})
	public Page<Training> getAllByDifficultyLevel(@PathVariable DifficultyLevel difficultyLevel, @RequestParam int page, @RequestParam int size) {
		return trainingService.getByDifficultyLevel(difficultyLevel, page, size);
	}

	@GetMapping("/getIdsByDifficultyLevel/{difficultyLevel}")
	public List<Long> getIdsByDifficultyLevel(@PathVariable DifficultyLevel difficultyLevel) {
		return trainingService.getAllByDifficultyLevel(difficultyLevel)
				.stream()
				.map(Training::getId)
				.collect(Collectors.toList());
	}

    @PostMapping("/insert")
    public Training insert(@RequestBody Training training) {
        return trainingService.save(training);
    }

	@PutMapping("/update")
	public Training update(@RequestBody Training training) {
		return trainingService.save(training);
	}

	@PutMapping("/setChallenge/{selectedChallenge}")
	public void setSelectedChallenge(@PathVariable int selectedChallenge) {
		tableService.setSelectedChallenge(selectedChallenge);
		trainingModeService.setTraining(trainingService.getInPixelById((long) selectedChallenge));
	}

	@DeleteMapping("/delete/{id}")
	public Training delete(@PathVariable Long id) {
		return trainingService.deleteById(id);
	}
}
