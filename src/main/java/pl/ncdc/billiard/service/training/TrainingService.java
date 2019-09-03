package pl.ncdc.billiard.service.training;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.ncdc.billiard.entities.training.HitPointHintEntity;
import pl.ncdc.billiard.entities.training.HitPowerHintEntity;
import pl.ncdc.billiard.entities.training.TargetBallHitPointHintEntity;
import pl.ncdc.billiard.entities.training.TrainingEntity;
import pl.ncdc.billiard.mappers.TrainingMapper;
import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.Pocket;
import pl.ncdc.billiard.models.training.DifficultyLevel;
import pl.ncdc.billiard.models.training.Training;
import pl.ncdc.billiard.repository.TrainingRepository;
import pl.ncdc.billiard.service.PoolDrawerService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingService {

    private final TrainingRepository trainingRepository;
	private final TrainingMapper trainingMapper;
	private final BilliardTable billiardTable;
	private final PoolDrawerService poolDrawerService;

	@Autowired
	public TrainingService(TrainingRepository trainingRepository, TrainingMapper trainingMapper,
						   BilliardTable billiardTable, PoolDrawerService poolDrawerService) {
		this.trainingRepository = trainingRepository;
		this.trainingMapper = trainingMapper;
		this.billiardTable = billiardTable;
		this.poolDrawerService = poolDrawerService;
	}

	public List<Training> getAll() {
		return trainingMapper.toModels(getAllEntities());
	}

	public Training getById(Long id) {
		TrainingEntity trainingEntity = getEntityById(id);
		if(trainingEntity == null) {
			return null;
		}

		return trainingMapper.toModel(trainingEntity);
	}

	public Training getNextById(Long id) {
		Training training = getById(id);
		Optional<TrainingEntity> optionalTrainingEntity = trainingRepository.findFirstByDifficultyLevelAndIdAfter(training.getDifficultyLevel(), id);
		if(optionalTrainingEntity.isPresent()) {
			return trainingMapper.toModel(optionalTrainingEntity.get());
		}

		optionalTrainingEntity = trainingRepository.findFirstByDifficultyLevel(training.getDifficultyLevel());
		if(optionalTrainingEntity.isPresent()) {
			return trainingMapper.toModel(optionalTrainingEntity.get());
		}

		return null;
	}

	public Training getRandom() {
		long count = trainingRepository.count();
		int randomIndex = (int) (Math.random() * count);
		Page<TrainingEntity> trainingEntityPage = trainingRepository.findAll(PageRequest.of(randomIndex, 1));
		if(trainingEntityPage.hasContent()) {
			return trainingMapper.toModel(trainingEntityPage.getContent().get(0));
		}

		return null;
	}

	public Training getRandomByDifficultyLevel(DifficultyLevel difficultyLevel) {
		long count = trainingRepository.countAllByDifficultyLevel(difficultyLevel);
		int randomIndex = (int) (Math.random() * count);
		Page<TrainingEntity> trainingEntityPage = trainingRepository.findByDifficultyLevel(difficultyLevel, PageRequest.of(randomIndex, 1));
		if(trainingEntityPage.hasContent()) {
			return trainingMapper.toModel(trainingEntityPage.getContent().get(0));
		}

		return null;
	}

	public Training getInPixelById(Long id) {
		Training training = getById(id);
		if(training == null) {
			return null;
		}

		Point viewport = new Point(billiardTable.getWidth(), billiardTable.getHeight());
		return trainingMapper.toInPixelModel(training, viewport);
	}

	public List<Training> getAllByDifficultyLevel(DifficultyLevel difficultyLevel) {
    	return trainingMapper.toModels(trainingRepository.findAllByDifficultyLevel(difficultyLevel));
	}

	public Page<Training> getByDifficultyLevel(DifficultyLevel difficultyLevel, int page, int size) {
		return getEntitiesByDifficultyLevel(difficultyLevel, page, size).map(trainingMapper::toModel);
	}

	public Training save(Training training) {
		TrainingEntity trainingEntity = getEntityById(training.getId());

		if(training.getHitPointHint() != null) {
			training.getHitPointHint().recalculateInsideCirclesOffsets();
		}

		if(trainingEntity == null) {
			trainingEntity = trainingMapper.toEntity(training);
			trainingEntity.setId(null);
		} else {
			trainingMapper.updateEntityFromModel(training, trainingEntity);
		}


		HitPointHintEntity hitPointHintEntity = trainingEntity.getHitPointHint();
		HitPowerHintEntity hitPowerHintEntity = trainingEntity.getHitPowerHint();
		TargetBallHitPointHintEntity targetBallHitPointHintEntity = trainingEntity.getTargetBallHitPointHint();
		if(hitPointHintEntity != null) {
			hitPointHintEntity.setTrainingEntity(trainingEntity);
		}
		if(hitPowerHintEntity != null) {
			hitPowerHintEntity.setTrainingEntity(trainingEntity);
		}
		if(targetBallHitPointHintEntity != null) {
			targetBallHitPointHintEntity.setTrainingEntity(trainingEntity);
		}

		int width = Training.PREVIEW_WIDTH;
		int height = Training.PREVIEW_HEIGHT;
		Mat mat = new Mat(height, width, CvType.CV_8UC3);
		poolDrawerService.drawTraining(mat, trainingMapper.toInPixelModel(training, new Point(width, height)),
				Arrays.asList(new Pocket(0, new Point(0, 0)),
						new Pocket(1, new Point(width / 2, 0)),
						new Pocket(2, new Point(width, 0)),
						new Pocket(3, new Point(width, height)),
						new Pocket(4, new Point(width / 2, height)),
						new Pocket(5, new Point(0, height))
						));
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".png", mat, matOfByte);
		trainingEntity.setImagePreview(matOfByte.toArray());

		mat.release();
		matOfByte.release();

		return trainingMapper.toModel(trainingRepository.save(trainingEntity));
	}

	public Training deleteById(Long id) {
		TrainingEntity trainingEntity = getEntityById(id);
		if(trainingEntity == null) {
			return null;
		}
		Training training = trainingMapper.toModel(trainingEntity);
		trainingRepository.delete(trainingEntity);

		return training;
	}

	private List<TrainingEntity> getAllEntities() {
		return trainingRepository.findAll();
	}

	private Page<TrainingEntity> getEntitiesByDifficultyLevel(DifficultyLevel difficultyLevel, int page, int size) {
		return trainingRepository.findByDifficultyLevel(difficultyLevel, PageRequest.of(page, size));
	}

	private TrainingEntity getEntityById(Long id) {
		if(id == null)
			return null;

		Optional<TrainingEntity> optionalIndividualTrainingEntity = trainingRepository.findById(id);
		if(optionalIndividualTrainingEntity.isPresent()) {
			return optionalIndividualTrainingEntity.get();
		}

		return null;
	}
}
