package pl.ncdc.billiard.service;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.StopWatch;
import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.IndividualTraining;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class IndividualTrainingGameModeService {

    private final CalibrationService calibrationService;

    @Getter @Setter
    private IndividualTraining individualTraining;
    private final BilliardTable billiardTable;
    private List<Point> lastFrameBallsPositions;
    @Getter
    private State state;

    private StopWatch stopWatch = new StopWatch();

    // it needs better name
    private double delay = 2.0;

    @Autowired
    public IndividualTrainingGameModeService(CalibrationService calibrationService, BilliardTable billiardTable) {
        this.calibrationService = calibrationService;
        this.billiardTable = billiardTable;
    }

    public void update() {
        if(individualTraining == null){
            return;
        }

        if(state == State.WaitingForBallsPlacement) {
            if(!isAllBallsPlacedCorrectly()) {
                stopWatch.reset();
                stopWatch.start();
            }

            if(stopWatch.getTime(TimeUnit.SECONDS) > delay) {
                stopWatch.reset();
                state = State.Ready;
            }
        } else if(state == State.Ready) {
            if(!isAllBallsPlacedCorrectly()) {
                state = State.WaitingForBallsStop;
            }
        } else if(state == State.WaitingForBallsStop) {
            if(doesBallsStopMoving() && stopWatch.getTime(TimeUnit.SECONDS) > delay) {
                if(isAllWinningConditionMeet()) {
                    state = State.Success;
                } else {
                    state = State.Fail;
                }
            } else {
                stopWatch.reset();
                stopWatch.start();
            }

        }

        lastFrameBallsPositions = billiardTable.getBalls().stream().map(ball -> new Point(ball.getPoint().x, ball.getPoint().y)).collect(Collectors.toList());
    }

    private boolean isAllBallsPlacedCorrectly() {
        int whiteAndTargetBallCount = 2;
        if(individualTraining.getDisturbBallsPositions().size() + whiteAndTargetBallCount != billiardTable.getBalls().size()) {
            return false;
        }

        if(distance(billiardTable.getWhiteBall().getPoint(), individualTraining.getWhiteBallPosition()) > getBallPositionTolerance()) {
            return false;
        }

        if(!individualTraining.getDisturbBallsPositions().stream().allMatch(this::isSomethingOnPoint)) {
            return false;
        }

        return true;
    }

    private boolean doesBallsStopMoving() {
        return lastFrameBallsPositions.stream().allMatch(this::isSomethingOnPoint);
    }

    private boolean isAllWinningConditionMeet() {
        int whiteBallCount = 1;
        if(individualTraining.getDisturbBallsPositions().size() + whiteBallCount != billiardTable.getBalls().size()) {
           return false;
        } else if(!individualTraining.getDisturbBallsPositions().stream().allMatch(this::isSomethingOnPoint)) {
            return false;
        } else if(!isWhiteBallInRect()) {
           return false;
        }

        return true;
    }

    private boolean isWhiteBallInRect() {
        Point min = individualTraining.getRectanglePosition().get(0);
        Point max = individualTraining.getRectanglePosition().get(1);
        Point whiteBall = billiardTable.getWhiteBall().getPoint();

        if(whiteBall.x > min.x && whiteBall.x < max.x &&
            whiteBall.y > min.y && whiteBall.y < max.y){
            return true;
        }

        return false;
    }

    private boolean isSomethingOnPoint(Point point) {
        return billiardTable.getBalls()
                .stream()
                .anyMatch(ball -> distance(point, ball.getPoint()) < getBallPositionTolerance());
    }

    private double distance(Point point, Point point2) {
        return Math.sqrt(Math.pow(point.x - point2.x, 2) + Math.pow(point.y - point2.y, 2));
    }
    // Move to calibrationParams
    private double getBallPositionTolerance() {
        return calibrationService.getCalibrationParams().getBallDiameter() / 4.0;
    }

    public enum State {
        WaitingForBallsPlacement,
        Ready,
        WaitingForBallsStop,
        Success,
        Fail
    }
}
