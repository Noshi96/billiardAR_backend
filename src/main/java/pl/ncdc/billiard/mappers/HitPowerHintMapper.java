package pl.ncdc.billiard.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.ncdc.billiard.entities.traininghints.HitPowerHintEntity;
import pl.ncdc.billiard.models.trainingHints.HitPowerHint;

@Mapper(uses = PointMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface HitPowerHintMapper {

    HitPowerHint toModel(HitPowerHintEntity hitPowerHintEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "individualTraining", ignore = true)
    HitPowerHintEntity toEntity(HitPowerHint hitPowerHint);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "individualTraining", ignore = true)
    HitPowerHintEntity updateEntity(HitPowerHint hitPowerHint, @MappingTarget HitPowerHintEntity entity);
}
