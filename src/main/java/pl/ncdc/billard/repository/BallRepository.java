package pl.ncdc.billard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.ncdc.billard.entity.Ball;

@Repository
public interface BallRepository extends JpaRepository<Ball, Long> {

}
