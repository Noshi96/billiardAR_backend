package pl.ncdc.billiard.mappers;

import org.mapstruct.*;
import org.opencv.core.Point;
import pl.ncdc.billiard.entities.traininghints.HitPointHintEntity;
import pl.ncdc.billiard.models.trainingHints.HitPointHint;

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
    HitPointHint toInPixelModel(HitPointHint hitPointHint, @Context Point viewport);

    @Named("doubleToPixel")
    default double toPixel(double number, @Context Point viewport) {
        return viewport.x * number;
    }
}
