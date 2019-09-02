package pl.ncdc.billiard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ncdc.billiard.entities.PoolDrawerParamsEntity;

import java.util.Optional;

public interface PoolDrawerParamsRepository extends JpaRepository<PoolDrawerParamsEntity, Long> {
    Optional<PoolDrawerParamsEntity> findFirstBy();
}
