package pl.ncdc.billiard.service;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.mappers.CalibrationParamsMapper;
import pl.ncdc.billiard.models.CalibrationParams;
import pl.ncdc.billiard.repository.CalibrationParamsRepository;

@Service
public class CalibrationService {

	private final KinectService kinectService;
	private final BilliardTableService billiardTableService;
	private final PoolDrawerService poolDrawerService;
	private final CalibrationParamsRepository calibrationParamsRepository;
	private final CalibrationParamsMapper calibrationParamsMapper;
	private final DepthImageService depthImageService;

	@Autowired
	public CalibrationService(@Lazy KinectService kinectService, BilliardTableService billiardTableService,
			CalibrationParamsRepository calibrationParamsRepository, CalibrationParamsMapper calibrationParamsMapper,
			PoolDrawerService poolDrawerService, DepthImageService depthImageService) {
		this.kinectService = kinectService;
		this.billiardTableService = billiardTableService;
		this.calibrationParamsRepository = calibrationParamsRepository;
		this.calibrationParamsMapper = calibrationParamsMapper;
		this.poolDrawerService = poolDrawerService;
		this.depthImageService = depthImageService;
	}

	@PostConstruct
	private void init() {
		updateServices(getCalibrationParams());
	}

	public CalibrationParams save(CalibrationParams calibrationParams) {

		updateServices(calibrationParams);
		pl.ncdc.billiard.entities.CalibrationParams entity = getCalibrationParamsEntity();
		calibrationParamsMapper.updateEntityFromModelIgnoreId(calibrationParams, entity);

		calibrationParamsRepository.save(entity);

		calibrationParamsMapper.updateModelFromEntity(entity, calibrationParams);
		return calibrationParams;
	}

	/**
	 * 
	 * @param calibrationParams
	 */
	private void updateServices(CalibrationParams calibrationParams) {

		// BilliardTableService
		Point leftTop = calibrationParams.getLeftUpperCorner();
		Point leftBottom = calibrationParams.getLeftBottomCorner();
		Point rightBottom = calibrationParams.getRightBottomCorner();
		Point rightTop = calibrationParams.getRightUpperCorner();

		int width = (int) Math.abs((rightTop.x + rightBottom.x - leftTop.x - leftBottom.x) / 2);
		int height = (int) Math.abs((leftBottom.y + rightBottom.y - leftTop.y - rightTop.y) / 2);

		this.billiardTableService.getTable().setWidth(width);
		this.billiardTableService.getTable().setHeight(height);

		this.billiardTableService.getTable().setBallRadius(calibrationParams.getBallDiameter() / 2);
		this.billiardTableService.calculatePocketsPosition(width, height);

		// KinectService
		this.kinectService.getKinect().stop();

		this.kinectService.setBallRadius(calibrationParams.getBallDiameter());

		this.kinectService.generatePerspectiveTransform(leftTop, leftBottom, rightBottom, rightTop, width, height);

		// DepthImageService
		this.depthImageService.generateMask(width, height);

		this.kinectService.getKinect().start(KinectService.FLAG);
		
		// PoolDrawerService
		this.poolDrawerService.updateCalibration(calibrationParams);
	}

	public CalibrationParams getCalibrationParams() {
		return calibrationParamsMapper.toModel(getCalibrationParamsEntity());
	}

	private pl.ncdc.billiard.entities.CalibrationParams getCalibrationParamsEntity() {
		Optional<pl.ncdc.billiard.entities.CalibrationParams> optionalCalibrationParams = calibrationParamsRepository
				.findFirstByOrderByIdAsc();
		if (optionalCalibrationParams.isPresent()) {
			return optionalCalibrationParams.get();
		} else {
			CalibrationParams defaultCalibrationParams = CalibrationParams.getDefaultCalibrationParams();
			pl.ncdc.billiard.entities.CalibrationParams entity = calibrationParamsMapper
					.toEntity(defaultCalibrationParams);
			calibrationParamsRepository.save(entity);

			return entity;
		}
	}

    public CalibrationParams resetToDefault() {
		return save(CalibrationParams.getDefaultCalibrationParams());
	}

	public void automaticCalibration() {
		CalibrationParams calibrationParams = getCalibrationParams();
		CalibrationParams newParams = null;
		while (newParams == null)
			newParams = this.kinectService.automaticCalibration(calibrationParams);
		this.save(newParams);
	}
}
