package pl.ncdc.billiard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.ncdc.billiard.entities.training.TrainingEntity;
import pl.ncdc.billiard.models.training.DifficultyLevel;

import java.util.List;
import java.util.Optional;

public interface TrainingRepository extends JpaRepository<TrainingEntity, Long> {
    List<TrainingEntity> findAllByDifficultyLevel(DifficultyLevel difficultyLevel);
    long countAllByDifficultyLevel(DifficultyLevel difficultyLevel);

    Page<TrainingEntity> findByDifficultyLevel(DifficultyLevel difficultyLevel, Pageable pageable);

    Optional<TrainingEntity> findFirstByDifficultyLevelAndIdAfter(DifficultyLevel difficultyLevel, Long id);
    Optional<TrainingEntity> findFirstByDifficultyLevel(DifficultyLevel difficultyLevel);
}
