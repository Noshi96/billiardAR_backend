package pl.ncdc.billiard.service;

import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.Kinect;
import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.Pocket;

@Service
public class BilliardTableService {

    private Kinect kinect;

    private BilliardTable table;

    public BilliardTableService() {
        this.kinect = new Kinect();
        this.table = new BilliardTable();
        kinect.start(Kinect.COLOR);
    }

    public BilliardTable getTable() {
        table = kinect.getTable();
        return table;
    }

	public void selectBall(Long ballId) {
		table.setSelectedBall(null);

		for (Ball ball : table.getBalls()) {

			if (ball.getId() == ballId) {
				table.setSelectedBall(ball);
			}
		}
	}

	public void selectPocket(Long pocketId) {
		table.setSelectedPocket(null);
		for (Pocket pocket : table.getPockets()) {
			if (pocket.getId() == pocketId) {
				table.setSelectedPocket(pocket);
			}
		}
    }

    public BilliardTable update(BilliardTable tableToUpdate) {
        for (Ball ballInOpenCv : tableToUpdate.getBalls()) {
            boolean ballToUpdateIsInTable = false;

            for (Ball ball : table.getBalls()) {
                if (ball.getId() == ballInOpenCv.getId()) {
                    ballToUpdateIsInTable = true;
                    ball.setPoint(ballInOpenCv.getPoint());
                    break;
                }
            }

            if (ballToUpdateIsInTable = false) {
                table.getBalls().add(ballInOpenCv);
            }

            boolean isBallInOpenCv = false;

            for (Ball ball : table.getBalls()) {
                for (Ball ballOpenCv : tableToUpdate.getBalls()) {
                    if (ball.getId() == ballOpenCv.getId()) {
                        isBallInOpenCv = true;
                    }
                }
                if (isBallInOpenCv == false) {
                    table.getBalls().remove(ball);
                }
            }
        }

        return table;
    }
}
