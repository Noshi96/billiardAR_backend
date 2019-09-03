package pl.ncdc.billiard.mappers;

import org.mapstruct.*;
import org.opencv.core.Point;
import pl.ncdc.billiard.entities.training.TrainingEntity;
import pl.ncdc.billiard.models.training.Training;

import java.util.List;

@Mapper(uses = {PointMapper.class, MultiPointMapper.class, HitPointHintMapper.class, HitPowerHintMapper.class, TargetBallHitPointHintMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TrainingMapper {

    Training toModel(TrainingEntity trainingEntity);

    TrainingEntity updateEntityFromModel(Training model, @MappingTarget TrainingEntity entity);

    List<Training> toModels(List<TrainingEntity> trainingEntities);

    TrainingEntity toEntity(Training training);

    List<TrainingEntity> toEntities(List<Training> trainings);


    Training toInPixelModel(Training training, @Context Point viewport);
}
