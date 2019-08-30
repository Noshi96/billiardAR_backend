package pl.ncdc.billiard.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.var;
import pl.ncdc.billiard.models.View;
import pl.ncdc.billiard.models.View.ViewMode;
import pl.ncdc.billiard.models.View.ViewModeFeature;

@Service
public class ViewModeService {

	private final SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
	private View view;

	public ViewModeService(SimpMessagingTemplate simpMessagingTemplate) {
		this.simpMessagingTemplate = simpMessagingTemplate;
	}
	
	

	
	public View getView() {
		return view;
	}
	
	
	public void setViewMode(ViewMode viewMode){
		view.setViewMode(viewMode);
		this.simpMessagingTemplate.convertAndSend("/viewMode", view);
	}
	
	public void setViewModeFeature(ViewModeFeature viewModeFeature){
		view.setViewModeFeature(viewModeFeature);
		this.simpMessagingTemplate.convertAndSend("/viewMode", view);
	}

	
	
	
	


}

