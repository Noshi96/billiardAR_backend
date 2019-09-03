package pl.ncdc.billiard.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.opencv.core.Scalar;
import pl.ncdc.billiard.entities.PoolDrawerParamsEntity;
import pl.ncdc.billiard.models.PoolDrawerParams;

import java.awt.*;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PoolDrawerParamsMapper {

    PoolDrawerParams toModel(PoolDrawerParamsEntity poolDrawerParamsEntity);

    PoolDrawerParams updateModelFromEntity(PoolDrawerParamsEntity entity, @MappingTarget PoolDrawerParams model);

    @Mapping(target = "id", ignore = true)
    PoolDrawerParamsEntity toEntity(PoolDrawerParams poolDrawerParams);

    @Mapping(target = "id", ignore = true)
    PoolDrawerParamsEntity updateEntityFromModel(PoolDrawerParams model, @MappingTarget PoolDrawerParamsEntity entity);

    default Scalar toScalar(String hexColor) {
        Color color = Color.decode(hexColor);
        return new Scalar(color.getBlue(), color.getGreen(), color.getRed());
    }

    default String toHex(Scalar scalar) {
        return String.format("#%02x%02x%02x", (int)scalar.val[2], (int)scalar.val[1], (int)scalar.val[0]);
    }
}
