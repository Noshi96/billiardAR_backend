package pl.ncdc.billiard.mappers;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class MultiPointMapper {

    @Autowired
    protected GeometryFactory geometryFactory;

    public List<Point> toList(MultiPoint multiPoint) {
        if(multiPoint == null)
            return new ArrayList<Point>();

        return Arrays.stream(multiPoint.getCoordinates())
                .map(coordinate -> new Point(coordinate.x, coordinate.y))
                .collect(Collectors.toList());
    }

    public MultiPoint toMultiPoint(List<Point> points) {
        Coordinate[] coordinates;
        if(points == null) {
            coordinates = new Coordinate[0];
        } else {
            coordinates = points.stream()
                    .map(point -> new Coordinate(point.x, point.y))
                    .toArray(Coordinate[]::new);
        }
        return geometryFactory.createMultiPoint(coordinates);
    }
}
