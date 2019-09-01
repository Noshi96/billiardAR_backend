package pl.ncdc.billiard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ncdc.billiard.entities.training.TrainingModeParams;

import java.util.Optional;

public interface TrainingModeParamsRepository extends JpaRepository<TrainingModeParams, Long> {
    Optional<TrainingModeParams> findFirstByOrderByIdAsc();
}
