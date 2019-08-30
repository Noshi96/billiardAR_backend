package pl.ncdc.billiard.models;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.var;

@Data
@Component
public class View {
	
	ViewMode viewMode;// = ViewMode.BASIC;
	HashMap<ViewModeFeature, Boolean> features;

	
	public View() {
		this.setViewMode(this.viewMode.BASIC);
		
		features = new HashMap<>();
		features.put(ViewModeFeature.HIDDENPLACES, false);
		features.put(ViewModeFeature.ROTATION, false);
		features.put(ViewModeFeature.NOROTATION, false);
		features.put(ViewModeFeature.BESTPOCKET, false);
	}
	
	
	public void setViewModeFeature(ViewModeFeature viewModeFeature){
		boolean currentFeature = features.get(viewModeFeature);
		currentFeature = !currentFeature;
	}
	
	
	public enum ViewMode {
		BASIC,
		PASSIVE,
		PASSIVEPRO,
		TRAINING
	}
	
	public enum ViewModeFeature {
		HIDDENPLACES,
		ROTATION,
		NOROTATION,
		BESTPOCKET
	}
}
