package pl.ncdc.billiard.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import pl.ncdc.billiard.models.trainingHints.HitPowerHint;

@Mapper(uses = PointMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface HitPowerHintMapper {

    HitPowerHint toModel(pl.ncdc.billiard.entities.traininghints.HitPowerHint hitPowerHintEntity);

    pl.ncdc.billiard.entities.traininghints.HitPowerHint toEntity(HitPowerHint hitPowerHint);
}
