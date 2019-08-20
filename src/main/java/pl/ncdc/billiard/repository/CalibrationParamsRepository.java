package pl.ncdc.billiard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ncdc.billiard.entities.CalibrationParams;

import java.util.Optional;

public interface CalibrationParamsRepository extends JpaRepository<CalibrationParams, Long> {
    Optional<CalibrationParams> findFirstByOrderByIdAsc();
}
