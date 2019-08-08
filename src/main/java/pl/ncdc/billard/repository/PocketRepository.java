package pl.ncdc.billard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.ncdc.billard.entity.Pocket;

@Repository
public interface PocketRepository extends JpaRepository<Pocket, Long> {
	
}
