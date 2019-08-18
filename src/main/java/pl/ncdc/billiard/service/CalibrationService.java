package pl.ncdc.billiard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ncdc.billiard.mappers.CalibrationParamsMapper;
import pl.ncdc.billiard.models.CalibrationParams;
import pl.ncdc.billiard.repository.CalibrationParamsRepository;
import java.util.Optional;


@Service
public class CalibrationService {

    private final CalibrationParamsRepository calibrationParamsRepository;
    private final CalibrationParamsMapper calibrationParamsMapper;


    @Autowired
    public CalibrationService(CalibrationParamsRepository calibrationParamsRepository, CalibrationParamsMapper calibrationParamsMapper){
        this.calibrationParamsRepository = calibrationParamsRepository;
        this.calibrationParamsMapper = calibrationParamsMapper;
    }

    public CalibrationParams save(CalibrationParams calibrationParams) {
        pl.ncdc.billiard.Entities.CalibrationParams entity = getCalibrationParamsEntity();
        calibrationParamsMapper.updateEntityFromModelIgnoreId(calibrationParams, entity);

        calibrationParamsRepository.save(entity);

        calibrationParamsMapper.updateModelFromEntity(entity, calibrationParams);
        return calibrationParams;
    }

    public CalibrationParams getCalibrationParams() {
        return calibrationParamsMapper.toModel(getCalibrationParamsEntity());
    }

    private pl.ncdc.billiard.Entities.CalibrationParams getCalibrationParamsEntity() {
        Optional<pl.ncdc.billiard.Entities.CalibrationParams> optionalCalibrationParams = calibrationParamsRepository.findFirstByOrderByIdAsc();
        if(optionalCalibrationParams.isPresent()) {
            return optionalCalibrationParams.get();
        } else {
            CalibrationParams defaultCalibrationParams = CalibrationParams.getDefaultCalibrationParams();
            pl.ncdc.billiard.Entities.CalibrationParams entity = calibrationParamsMapper.toEntity(defaultCalibrationParams);

            calibrationParamsRepository.save(entity);

            return entity;
        }
    }
}
