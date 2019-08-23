package pl.ncdc.billiard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ncdc.billiard.entities.IndividualTrainingEntity;
import pl.ncdc.billiard.models.DifficultyLevel;

import java.util.List;

public interface IndividualTrainingRepository extends JpaRepository<IndividualTrainingEntity, Long> {
    List<IndividualTrainingEntity> findAllByDifficultyLevel(DifficultyLevel difficultyLevel);
}
