package pl.ncdc.billiard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.ncdc.billiard.entity.Pocket;

@Repository
public interface PocketRepository extends JpaRepository<Pocket, Long> {
	
}
