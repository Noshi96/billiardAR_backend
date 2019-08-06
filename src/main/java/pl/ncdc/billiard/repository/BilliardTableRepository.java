package pl.ncdc.billiard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.ncdc.billiard.entity.BilliardTable;

@Repository
public interface BilliardTableRepository extends JpaRepository<BilliardTable, Long> {

}
