package pl.ncdc.billiard.mappers;

import org.mapstruct.*;
import org.opencv.core.Point;
import pl.ncdc.billiard.entities.IndividualTrainingEntity;
import pl.ncdc.billiard.models.IndividualTraining;

import java.util.List;

@Mapper(uses = {PointMapper.class, MultiPointMapper.class, HitPointHintMapper.class, HitPowerHintMapper.class, TargetBallHitPointHintMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface IndividualTrainingMapper {

    IndividualTraining toModel(IndividualTrainingEntity individualTrainingEntity);

    IndividualTrainingEntity updateEntityFromModel(IndividualTraining model, @MappingTarget IndividualTrainingEntity entity);

    List<IndividualTraining> toModels(List<IndividualTrainingEntity> individualTrainingEntities);

    IndividualTrainingEntity toEntity(IndividualTraining individualTraining);

    List<IndividualTrainingEntity> toEntities(List<IndividualTraining> individualTrainings);


    IndividualTraining toInPixelModel(IndividualTraining individualTraining, @Context Point viewport);

    default Point toPixel(Point point, @Context Point viewport) {
        return new Point(point.x * viewport.x, point.y * viewport.y);
    }

    List<Point> toPixel(List<Point> points, @Context Point viewPoint);
}
