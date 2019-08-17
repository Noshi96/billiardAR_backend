package pl.ncdc.billiard.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import pl.ncdc.billiard.models.CalibrationParams;
import pl.ncdc.billiard.service.CalibrationService;

@RestController
@RequestMapping("/calibration")
@CrossOrigin(value = "*")
public class CalibrationController {

    private final CalibrationService calibrationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public CalibrationController(CalibrationService calibrationService, SimpMessagingTemplate simpMessagingTemplate) {
        this.calibrationService = calibrationService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping
    public CalibrationParams getCalibrationParams() {
        return this.calibrationService.getCalibrationParams();
    }

    @PutMapping
    public void updateCalibration(@RequestBody CalibrationParams calibrationParams) {
        CalibrationParams params = calibrationService.save(calibrationParams);
        simpMessagingTemplate.convertAndSend("/calibration/live", params);
    }
}
