package pl.ncdc.billiard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.ncdc.billiard.entity.Point;

public interface PointRepository extends JpaRepository<Point, Long> {

}
