package pl.ncdc.billiard.controllers;

import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.ncdc.billiard.models.View;
import pl.ncdc.billiard.models.View.*;
import pl.ncdc.billiard.service.ViewModeService;


@RestController
@RequestMapping("/viewMode")
@CrossOrigin(value = "*")
public class ViewModeController {
	
	private final ViewModeService viewModeService;
	
	@Autowired
	public ViewModeController(ViewModeService viewModeService) {
		this.viewModeService = viewModeService;
	}
	
	
	@GetMapping("")
	public View getView() {
		return viewModeService.getView();
	}
	
	@PutMapping("/setViewMode")
	public void setViewMode(@RequestBody ViewMode viewMode) {
		viewModeService.setViewMode(viewMode);
	}
	
	@PutMapping("/setViewModeFeature")
	public void setViewModeFeature(@RequestBody ViewModeFeature viewModeFeature) {
		viewModeService.setViewModeFeature(viewModeFeature);
	}
	

}
