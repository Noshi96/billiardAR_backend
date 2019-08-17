package pl.ncdc.billiard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ncdc.billiard.models.CalibrationParams;
import pl.ncdc.billiard.repository.CalibrationParamsRepository;

import java.util.Optional;


@Service
public class CalibrationService {

    private final BilliardTableService billiardTableService;

    private final KinectService kinectService;
    private CalibrationParamsRepository calibrationParamsRepository;


    @Autowired
    public CalibrationService(BilliardTableService billiardTableService, KinectService kinectService,
                                CalibrationParamsRepository calibrationParamsRepository){
        this.billiardTableService = billiardTableService;
        this.kinectService = kinectService;
        this.calibrationParamsRepository = calibrationParamsRepository;

        CalibrationParams calibrationParams = getCalibrationParams();
        this.billiardTableService.updateCalibrationParams(calibrationParams);
        this.kinectService.updateCalibrationParams(calibrationParams);
    }

    public CalibrationParams save(CalibrationParams calibrationParams) {
        Optional<pl.ncdc.billiard.Entities.CalibrationParams> optionalCalibrationParams = calibrationParamsRepository.findFirstByOrderByIdAsc();
        if(optionalCalibrationParams.isPresent()) {
            pl.ncdc.billiard.Entities.CalibrationParams params = optionalCalibrationParams.get();
            params.copyFromModel(calibrationParams);

            calibrationParamsRepository.save(params);
            return params.toModel();
        } else {
            pl.ncdc.billiard.Entities.CalibrationParams params = new pl.ncdc.billiard.Entities.CalibrationParams();
            params.copyFromModel(calibrationParams);
            calibrationParamsRepository.save(params);

            return params.toModel();
        }

    }

    public CalibrationParams getCalibrationParams() {
        Optional<pl.ncdc.billiard.Entities.CalibrationParams> optionalCalibrationParams = calibrationParamsRepository.findFirstByOrderByIdAsc();
        if(optionalCalibrationParams.isPresent())
            return optionalCalibrationParams.get().toModel();
        else {
            CalibrationParams defaultCalibrationParams = CalibrationParams.getDefaultCalibrationParams();
            pl.ncdc.billiard.Entities.CalibrationParams calibrationParams = new pl.ncdc.billiard.Entities.CalibrationParams();
            calibrationParams.copyFromModel(defaultCalibrationParams);
            calibrationParamsRepository.save(calibrationParams);

            return calibrationParams.toModel();
        }
    }
}
