package pl.ncdc.billiard.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import pl.ncdc.billiard.models.trainingHints.TargetBallHitPointHint;

@Mapper(uses = PointMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TargetBallHitPointHintMapper {

    TargetBallHitPointHint toModel(pl.ncdc.billiard.entities.traininghints.TargetBallHitPointHint targetBallHitPointHintEntity);

    pl.ncdc.billiard.entities.traininghints.TargetBallHitPointHint toEntity(TargetBallHitPointHint targetBallHitPointHint);
}
