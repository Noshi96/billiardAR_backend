package pl.ncdc.billiard.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.ncdc.billiard.models.CalibrationParams;

@Mapper(uses = PointMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CalibrationParamsMapper {

    CalibrationParams toModel(pl.ncdc.billiard.Entities.CalibrationParams calibrationParams);

    CalibrationParams updateModelFromEntity(pl.ncdc.billiard.Entities.CalibrationParams entity, @MappingTarget CalibrationParams model);

    pl.ncdc.billiard.Entities.CalibrationParams toEntity(CalibrationParams calibrationParams);

    pl.ncdc.billiard.Entities.CalibrationParams updateEntityFromModel(CalibrationParams model, @MappingTarget pl.ncdc.billiard.Entities.CalibrationParams entity);

    @Mapping(target = "id", ignore = true)
    pl.ncdc.billiard.Entities.CalibrationParams updateEntityFromModelIgnoreId(CalibrationParams model, @MappingTarget pl.ncdc.billiard.Entities.CalibrationParams entity);
}
