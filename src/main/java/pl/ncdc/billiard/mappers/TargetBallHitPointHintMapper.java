package pl.ncdc.billiard.mappers;

import org.mapstruct.*;
import org.opencv.core.Point;
import pl.ncdc.billiard.entities.traininghints.TargetBallHitPointHintEntity;
import pl.ncdc.billiard.models.trainingHints.TargetBallHitPointHint;

@Mapper(uses = PointMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TargetBallHitPointHintMapper {

    TargetBallHitPointHint toModel(TargetBallHitPointHintEntity targetBallHitPointHintEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "individualTraining", ignore = true)
    TargetBallHitPointHintEntity toEntity(TargetBallHitPointHint targetBallHitPointHint);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "individualTraining", ignore = true)
    TargetBallHitPointHintEntity updateEntity(TargetBallHitPointHint targetBallHitPointHint, @MappingTarget TargetBallHitPointHintEntity entity);

    @Mapping(target = "radius", qualifiedByName = "doubleToPixel")
    TargetBallHitPointHint toInPixelModel(TargetBallHitPointHint targetBallHitPointHint, @Context Point viewport);

    @Named("doubleToPixel")
    default double toPixel(double number, @Context Point viewport) {
        return viewport.x * number;
    }
}
