package pl.ncdc.billard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.ncdc.billard.entity.Hit;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long>{

}
