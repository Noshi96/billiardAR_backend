package pl.ncdc.billiard.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import pl.ncdc.billiard.models.trainingHints.HitPointHint;

@Mapper(uses = PointMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface HitPointHintMapper {

    HitPointHint toModel(pl.ncdc.billiard.entities.traininghints.HitPointHint hitPointHintEntity);

    pl.ncdc.billiard.entities.traininghints.HitPointHint toEntity(HitPointHint hitPointHint);
}
