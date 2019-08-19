package pl.ncdc.billiard.service;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.mappers.CalibrationParamsMapper;
import pl.ncdc.billiard.models.CalibrationParams;
import pl.ncdc.billiard.repository.CalibrationParamsRepository;


@Service
public class CalibrationService {

	private KinectService kinectService;
	private BilliardTableService billiardTableService;
	private final CalibrationParamsRepository calibrationParamsRepository;
    private final CalibrationParamsMapper calibrationParamsMapper;

	@Autowired
	public CalibrationService(@Lazy KinectService kinectService, BilliardTableService billiardTableService, CalibrationParamsRepository calibrationParamsRepository, CalibrationParamsMapper calibrationParamsMapper) {
		this.kinectService = kinectService;
		this.billiardTableService = billiardTableService;
		this.calibrationParamsRepository = calibrationParamsRepository;
        this.calibrationParamsMapper = calibrationParamsMapper;
	}

	@PostConstruct
	private void init() {
		this.billiardTableService.updateCalibration(getCalibrationParams());
		this.kinectService.updateCalibration(getCalibrationParams());
	}

	public CalibrationParams save(CalibrationParams calibrationParams) {
		this.billiardTableService.updateCalibration(calibrationParams);
		this.kinectService.updateCalibration(calibrationParams);

		pl.ncdc.billiard.entities.CalibrationParams entity = getCalibrationParamsEntity();
        calibrationParamsMapper.updateEntityFromModelIgnoreId(calibrationParams, entity);

        calibrationParamsRepository.save(entity);

        calibrationParamsMapper.updateModelFromEntity(entity, calibrationParams);
        return calibrationParams;
	}
	
	 public CalibrationParams getCalibrationParams() {
        return calibrationParamsMapper.toModel(getCalibrationParamsEntity());
    }

	private pl.ncdc.billiard.entities.CalibrationParams getCalibrationParamsEntity() {
        Optional<pl.ncdc.billiard.entities.CalibrationParams> optionalCalibrationParams = calibrationParamsRepository.findFirstByOrderByIdAsc();
        if(optionalCalibrationParams.isPresent()) {
            return optionalCalibrationParams.get();
        } else {
            CalibrationParams defaultCalibrationParams = CalibrationParams.getDefaultCalibrationParams();
            pl.ncdc.billiard.entities.CalibrationParams entity = calibrationParamsMapper.toEntity(defaultCalibrationParams);

            calibrationParamsRepository.save(entity);

            return entity;
        }
    }
}
