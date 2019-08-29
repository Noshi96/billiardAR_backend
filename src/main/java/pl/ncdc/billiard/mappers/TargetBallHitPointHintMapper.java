package pl.ncdc.billiard.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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
}
