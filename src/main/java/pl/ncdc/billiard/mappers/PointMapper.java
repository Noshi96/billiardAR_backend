package pl.ncdc.billiard.mappers;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PointMapper {

    @Autowired
    protected GeometryFactory geometryFactory;

    public abstract Point spatialToOpenCv(com.vividsolutions.jts.geom.Point point);

    public com.vividsolutions.jts.geom.Point openCvToSpatial(Point point) {
        if(point == null)
            return null;

        return geometryFactory.createPoint(new Coordinate(point.x, point.y));
    }


    public Point toPixel(Point point, @Context Point viewport) {
        return new Point(point.x * viewport.x, point.y * viewport.y);
    }
}
