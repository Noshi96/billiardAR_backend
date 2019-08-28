package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.Pocket;

@Service
public class BilliardTableService {

	@Autowired
	private BilliardTable table;
	
	@Autowired
	private PoolDrawerService poolDrawerService;

	public BilliardTable getTable() {
		return table;
	}

	public byte[] drawPoolImage() {
		return poolDrawerService.drawImage(table);
	}
	
	public void selectBall(Point point) {
		point = table.findBallByPoint(point);
		if (point == null)
			table.setSelectedBall(null);
		else
			table.setSelectedBall(new Ball(0, table.findBallByPoint(point)));
	}
	
	public void selectPocket(Long pocketId) {
		table.setSelectedPocket(null);
		for (Pocket pocket : table.getPockets()) {
			if (pocket.getId() == pocketId) {
				table.setSelectedPocket(pocket);
			}
		}
	}

	public void calculatePocketsPosition(int width, int height) {
		// add pockets
		List<Pocket> pockets = new ArrayList<Pocket>();
		// top left
		pockets.add(new Pocket(0, new Point(0, 0)));
		// top middle
		pockets.add(new Pocket(1, new Point(width / 2, 0)));
		// top right
		pockets.add(new Pocket(2, new Point(width, 0)));
		// bottom right
		pockets.add(new Pocket(3, new Point(width, height)));
		// bottom middle
		pockets.add(new Pocket(4, new Point(width / 2, height)));
		// bottom left
		pockets.add(new Pocket(5, new Point(0, height)));

		this.table.setPockets(pockets);
	}

	public void setViewMode(int viewMode) {
		table.setSelectedViewMode(viewMode);
	}

	public void setSelectedChallenge(int selectedChallenge) {
		table.setSelectedChallenge(selectedChallenge);
	}
}
