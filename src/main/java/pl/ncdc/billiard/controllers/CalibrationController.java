package pl.ncdc.billiard.Controllers;

import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		calibrationParams.setBallDiameter(20);
		calibrationParams.setLeftUpperCorner(new Point(442, 292));
		calibrationParams.setRightUpperCorner(new Point(1611, 308));
		calibrationParams.setRightBottomCorner(new Point(1603, 889));
		calibrationParams.setLeftBottomCorner(new Point(436, 880));
		calibrationParams = calibrationService.save(calibrationParams);
		simpMessagingTemplate.convertAndSend("/calibration/live", calibrationParams);
	}
}
