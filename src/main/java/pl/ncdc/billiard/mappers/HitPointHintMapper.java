package pl.ncdc.billiard.mappers;

import org.mapstruct.*;
import pl.ncdc.billiard.entities.traininghints.HitPointHintEntity;
import pl.ncdc.billiard.models.trainingHints.HitPointHint;

@Mapper(uses = PointMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface HitPointHintMapper {

    HitPointHint toModel(HitPointHintEntity hitPointHintEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "individualTraining", ignore = true)
    HitPointHintEntity toEntity(HitPointHint hitPointHint);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "individualTraining", ignore = true)
    HitPointHintEntity updateEntity(HitPointHint hitPointHint, @MappingTarget HitPointHintEntity entity);
}
