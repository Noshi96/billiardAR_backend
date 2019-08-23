package pl.ncdc.billiard.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.ncdc.billiard.entities.IndividualTrainingEntity;
import pl.ncdc.billiard.models.IndividualTraining;

import java.util.List;

@Mapper(uses = {PointMapper.class, MultiPointMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface IndividualTrainingMapper {

    IndividualTraining toModel(IndividualTrainingEntity individualTrainingEntity);

    IndividualTrainingEntity updateEntityFromModel(IndividualTraining model, @MappingTarget IndividualTrainingEntity entity);

    List<IndividualTraining> toModels(List<IndividualTrainingEntity> individualTrainingEntities);

    IndividualTrainingEntity toEntity(IndividualTraining individualTraining);

    List<IndividualTrainingEntity> toEntities(List<IndividualTraining> individualTrainings);
}
