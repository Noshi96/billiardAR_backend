package pl.ncdc.billiard.service;

import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ncdc.billiard.entities.IndividualTrainingEntity;
import pl.ncdc.billiard.mappers.IndividualTrainingMapper;
import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.DifficultyLevel;
import pl.ncdc.billiard.models.IndividualTraining;
import pl.ncdc.billiard.repository.IndividualTrainingRepository;

import java.util.List;
import java.util.Optional;

@Service
public class IndividualTrainingService {


    private final IndividualTrainingRepository individualTrainingRepository;
	private final IndividualTrainingMapper individualTrainingMapper;
	private final BilliardTable billiardTable;

	@Autowired
	public IndividualTrainingService(IndividualTrainingRepository individualTrainingRepository, IndividualTrainingMapper individualTrainingMapper,
									 BilliardTable billiardTable) {
		this.individualTrainingRepository = individualTrainingRepository;
		this.individualTrainingMapper = individualTrainingMapper;
		this.billiardTable = billiardTable;
	}

	public List<IndividualTraining> getAll() {
		return individualTrainingMapper.toModels(getAllEntities());
	}

	public IndividualTraining getById(Long id) {
		IndividualTrainingEntity individualTrainingEntity = getEntityById(id);
		if(individualTrainingEntity == null) {
			return null;
		}

		return individualTrainingMapper.toModel(individualTrainingEntity);
	}

	public IndividualTraining getInPixelById(Long id) {
		IndividualTraining individualTraining = getById(id);
		if(individualTraining == null) {
			return null;
		}

		Point viewport = new Point(billiardTable.getWidth(), billiardTable.getHeight());
		return individualTrainingMapper.toInPixelModel(individualTraining, viewport);
	}

	public List<IndividualTraining> getAllByDifficultyLevel(DifficultyLevel difficultyLevel) {
    	return individualTrainingMapper.toModels(individualTrainingRepository.findAllByDifficultyLevel(difficultyLevel));
	}

	public IndividualTraining saveIndividualTraining(IndividualTraining individualTraining) {
		IndividualTrainingEntity individualTrainingEntity = getEntityById(individualTraining.getId());

		if(individualTrainingEntity == null) {
			individualTrainingEntity = individualTrainingMapper.toEntity(individualTraining);
			individualTrainingEntity.setId(null);
		} else {
			individualTrainingMapper.updateEntityFromModel(individualTraining, individualTrainingEntity);
		}

		return individualTrainingMapper.toModel(individualTrainingRepository.save(individualTrainingEntity));
	}

	private List<IndividualTrainingEntity> getAllEntities() {
		return individualTrainingRepository.findAll();
	}

	private IndividualTrainingEntity getEntityById(Long id) {
		if(id == null)
			return null;

		Optional<IndividualTrainingEntity> optionalIndividualTrainingEntity = individualTrainingRepository.findById(id);
		if(optionalIndividualTrainingEntity.isPresent()) {
			return optionalIndividualTrainingEntity.get();
		}

		return null;
	}
}
