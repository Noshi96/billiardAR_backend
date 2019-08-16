package pl.ncdc.billiard.service;

import org.springframework.stereotype.Service;
import pl.ncdc.billiard.models.CalibrationParams;

@Service
public class CalibrationService {

    private CalibrationParams calibrationParams = new CalibrationParams();

    public void updateCalibration(CalibrationParams calibrationParams) {
        this.calibrationParams = calibrationParams;
    }

    public CalibrationParams getCalibrationParams() {
        return calibrationParams;
    }
}
