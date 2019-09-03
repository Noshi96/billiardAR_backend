package pl.ncdc.billiard.service;

import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.ncdc.billiard.entities.PoolDrawerParamsEntity;
import pl.ncdc.billiard.mappers.PoolDrawerParamsMapper;
import pl.ncdc.billiard.models.PoolDrawerParams;
import pl.ncdc.billiard.repository.PoolDrawerParamsRepository;

import java.util.Optional;

@Service
public class PoolDrawerParamsService implements InitializingBean {

    private final PoolDrawerParamsRepository poolDrawerParamsRepository;
    private final PoolDrawerParamsMapper poolDrawerParamsMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public PoolDrawerParamsService(PoolDrawerParamsRepository poolDrawerParamsRepository, PoolDrawerParamsMapper poolDrawerParamsMapper, ApplicationEventPublisher applicationEventPublisher) {
        this.poolDrawerParamsRepository = poolDrawerParamsRepository;
        this.poolDrawerParamsMapper = poolDrawerParamsMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        PoolDrawerParams poolDrawerParams = getPoolDrawerParams();
        applicationEventPublisher.publishEvent(new PoolDrawerParamsUpdatedEvent(this, poolDrawerParams));
    }

    public PoolDrawerParams getPoolDrawerParams() {
        return poolDrawerParamsMapper.toModel(getPoolDrawerParamsEntity());
    }

    public PoolDrawerParams save(PoolDrawerParams poolDrawerParams) {
        PoolDrawerParamsEntity entity = getPoolDrawerParamsEntity();
        poolDrawerParamsMapper.updateEntityFromModel(poolDrawerParams, entity);
        poolDrawerParamsRepository.save(entity);

        poolDrawerParamsMapper.updateModelFromEntity(entity, poolDrawerParams);
        applicationEventPublisher.publishEvent(new PoolDrawerParamsUpdatedEvent(this, poolDrawerParams));
        return poolDrawerParams;
    }

    private PoolDrawerParamsEntity getPoolDrawerParamsEntity() {
        Optional<PoolDrawerParamsEntity> optionalPoolDrawerParamsEntity = poolDrawerParamsRepository.findFirstBy();

        if(optionalPoolDrawerParamsEntity.isPresent()) {
            return optionalPoolDrawerParamsEntity.get();
        } else {
            PoolDrawerParamsEntity poolDrawerParamsEntity = new PoolDrawerParamsEntity();
            poolDrawerParamsEntity = poolDrawerParamsRepository.save(poolDrawerParamsEntity);

            return poolDrawerParamsEntity;
        }
    }

    public class PoolDrawerParamsUpdatedEvent extends ApplicationEvent {
        @Getter
        private PoolDrawerParams poolDrawerParams;

        public PoolDrawerParamsUpdatedEvent(Object source, PoolDrawerParams poolDrawerParams) {
            super(source);
            this.poolDrawerParams = poolDrawerParams;
        }
    }
}
