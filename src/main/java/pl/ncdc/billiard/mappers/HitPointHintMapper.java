package pl.ncdc.billiard.mappers;

import org.mapstruct.*;
import org.opencv.core.Point;
import pl.ncdc.billiard.entities.traininghints.HitPointHintEntity;
import pl.ncdc.billiard.models.trainingHints.HitPointHint;

import java.util.List;

@Mapper(uses = {PointMapper.class, MultiPointMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface HitPointHintMapper {

    HitPointHint toModel(HitPointHintEntity hitPointHintEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "individualTraining", ignore = true)
    HitPointHintEntity toEntity(HitPointHint hitPointHint);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "individualTraining", ignore = true)
    HitPointHintEntity updateEntity(HitPointHint hitPointHint, @MappingTarget HitPointHintEntity entity);

    @Mapping(target = "radius", qualifiedByName = "doubleToPixel")
    @Mapping(target = "insideCirclesOffsets", qualifiedByName = "insideCirclesToPixel")
    HitPointHint toInPixelModel(HitPointHint hitPointHint, @Context Point viewport);

    @Named("doubleToPixel")
    default double toPixel(double number, @Context Point viewport) {
        return viewport.x * number;
    }

    @Named("insideCirclesToPixel")
    @IterableMapping(qualifiedByName = "insideCircleToPixel")
    List<Point> insideCirclesToPixel(List<Point> points, @Context Point viewport);

    @Named("insideCircleToPixel")
    default Point insideCircleToPixel(Point point, @Context Point viewport) {
        return new Point(point.x * viewport.x, point.y * viewport.x);
    }
}
