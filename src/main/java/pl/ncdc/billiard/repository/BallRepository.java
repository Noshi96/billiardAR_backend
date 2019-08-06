package pl.ncdc.billiard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.ncdc.billiard.entity.Ball;

@Repository
public interface BallRepository extends JpaRepository<Ball, Long> {

}
