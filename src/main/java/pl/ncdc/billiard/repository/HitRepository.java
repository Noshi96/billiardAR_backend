package pl.ncdc.billiard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.ncdc.billiard.entity.Hit;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long>{

}
