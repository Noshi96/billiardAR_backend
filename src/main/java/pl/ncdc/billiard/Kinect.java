package pl.ncdc.billiard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import pl.ncdc.billiard.service.KinectService;

@Component
public class Kinect extends J4KSDK {

	@Autowired
	KinectService kinectService;
	
	@Override
	public void onSkeletonFrameEvent(boolean[] skeleton_tracked, float[] positions, float[] orientations,
			byte[] joint_status) {
	}

	@Override
	public void onColorFrameEvent(byte[] data) {
		//kinectService.send(data, getColorHeight(), getColorWidth());
	}

	@Override
	public void onDepthFrameEvent(short[] data, byte[] body_index, float[] xyz, float[] uv) {
	}

}
