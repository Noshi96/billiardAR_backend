package pl.ncdc.billiard.controllers;

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
		calibrationParams = calibrationService.save(calibrationParams);
		simpMessagingTemplate.convertAndSend("/calibration/live", calibrationParams);
	}

	@GetMapping("/reset")
	public CalibrationParams resetToDefault() {
		CalibrationParams calibrationParams = this.calibrationService.resetToDefault();
		simpMessagingTemplate.convertAndSend("/calibration/live", calibrationParams);
		return calibrationParams;
	}
	
	@GetMapping("/automatic")
	public void automatiCalibration() {
		this.calibrationService.automaticCalibration();
	}
}
