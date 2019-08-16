package pl.ncdc.billiard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.ncdc.billiard.models.IndividualTraining;

@Repository
public interface IndividualTrainingRepository extends JpaRepository<IndividualTraining, Long>{

}

