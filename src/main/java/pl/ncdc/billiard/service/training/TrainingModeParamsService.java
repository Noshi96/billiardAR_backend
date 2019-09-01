package pl.ncdc.billiard.service.training;

import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.ncdc.billiard.entities.training.TrainingModeParams;
import pl.ncdc.billiard.repository.TrainingModeParamsRepository;

import java.util.Optional;

@Service
public class TrainingModeParamsService implements InitializingBean {

    private final TrainingModeParamsRepository trainingModeParamsRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TrainingModeParamsService(TrainingModeParamsRepository trainingModeParamsRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.trainingModeParamsRepository = trainingModeParamsRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public TrainingModeParams getTrainingModeParams() {
        Optional<TrainingModeParams> optionalTrainingModeParams = trainingModeParamsRepository.findFirstByOrderByIdAsc();
        if(optionalTrainingModeParams.isPresent()) {
            return optionalTrainingModeParams.get();
        } else {
            TrainingModeParams trainingModeParams = new TrainingModeParams();
            trainingModeParams = trainingModeParamsRepository.save(trainingModeParams);

            return trainingModeParams;
        }
    }

    public TrainingModeParams save(TrainingModeParams trainingModeParams) {
        TrainingModeParams trainingModeParamsEntity = getTrainingModeParams();
        trainingModeParams.setId(trainingModeParamsEntity.getId());
        trainingModeParams = trainingModeParamsRepository.save(trainingModeParams);

        applicationEventPublisher.publishEvent(new TrainingModeParamsUpdatedEvent(this, trainingModeParams));
        return trainingModeParams;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        TrainingModeParams trainingModeParams = getTrainingModeParams();
        applicationEventPublisher.publishEvent(new TrainingModeParamsUpdatedEvent(this, trainingModeParams));
    }

    public class TrainingModeParamsUpdatedEvent extends ApplicationEvent {
        @Getter
        private TrainingModeParams trainingModeParams;

        public TrainingModeParamsUpdatedEvent(Object source, TrainingModeParams trainingModeParams) {
            super(source);
            this.trainingModeParams = trainingModeParams;
        }
    }
}
