package pl.ncdc.billiard.service;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.StopWatch;
import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.IndividualTraining;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class IndividualTrainingGameModeService {

    private final CalibrationService calibrationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Getter
    private IndividualTraining individualTraining;
    private final BilliardTable billiardTable;
    private List<Point> lastFrameBallsPositions;
    @Getter
    private State state;

    private StopWatch stopWatch = new StopWatch();

    private double waitingForBallsPlacementDelay = 2.0;
    private double ballsStopMovingDelay = 5.0;
    private double afterEndDelay = 5.0;

    @Autowired
    public IndividualTrainingGameModeService(CalibrationService calibrationService, SimpMessagingTemplate simpMessagingTemplate, BilliardTable billiardTable) {
        this.calibrationService = calibrationService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.billiardTable = billiardTable;
    }

    @Scheduled(fixedRate = 400)
    public void update() {
        if(individualTraining == null){
            return;
        }

        if(state == State.WaitingForBallsPlacement) {
            if(!isAllBallsPlacedCorrectly()) {
                stopWatch.reset();
                stopWatch.start();
            }

            if(stopWatch.getTime(TimeUnit.SECONDS) > waitingForBallsPlacementDelay) {
                stopWatch.reset();
                state = State.Ready;
            }
        } else if(state == State.Ready) {
            if(!isAllBallsPlacedCorrectly()) {
                state = State.WaitingForBallsStop;
            }
        } else if(state == State.WaitingForBallsStop) {
            if(doesBallsStopMoving() && stopWatch.getTime(TimeUnit.SECONDS) > ballsStopMovingDelay) {
                if(isAllWinningConditionMeet()) {
                    state = State.Success;
                } else {
                    state = State.Fail;
                }
                stopWatch.reset();
                stopWatch.start();
            } else {
                stopWatch.reset();
                stopWatch.start();
            }
        } else if(state == State.Success || state == State.Fail) {
            if(stopWatch.getTime(TimeUnit.SECONDS) > afterEndDelay) {
                setIndividualTraining(null);
            }
        }

        if(billiardTable.getBalls() == null) {
            lastFrameBallsPositions = new ArrayList<>();
        } else {
            lastFrameBallsPositions = billiardTable.getBalls().stream().map(ball -> new Point(ball.getPoint().x, ball.getPoint().y)).collect(Collectors.toList());
        }

        simpMessagingTemplate.convertAndSend("/individualTraining/state", state);
    }

    public void setIndividualTraining(IndividualTraining individualTraining) {
        this.individualTraining = individualTraining;
        state = State.WaitingForBallsPlacement;
    }

    private boolean isAllBallsPlacedCorrectly() {
        int whiteAndTargetBallCount = 2;
        int ballsCount = 0;

        if(billiardTable.getBalls() != null) {
            ballsCount = billiardTable.getBalls().size();
        }

        if(individualTraining.getDisturbBallsPositions().size() + whiteAndTargetBallCount != ballsCount) {
            return false;
        }

        if(billiardTable.getWhiteBall() == null || individualTraining.getWhiteBallPosition() == null) {
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
        int ballsCount = 0;

        if(billiardTable.getBalls() != null) {
            ballsCount = billiardTable.getBalls().size();
        }

        if(individualTraining.getDisturbBallsPositions().size() + whiteBallCount != ballsCount) {
           return false;
        }

        if(!individualTraining.getDisturbBallsPositions().stream().allMatch(this::isSomethingOnPoint)) {
            return false;
        }

        if(!isWhiteBallInRect()) {
           return false;
        }

        return true;
    }

    private boolean isWhiteBallInRect() {
        if(billiardTable.getWhiteBall() == null) {
            return false;
        }

        List<Point> rectanglePosition = individualTraining.getRectanglePosition();
        Point min = rectanglePosition.get(0);
        Point max = rectanglePosition.get(1);
        Point whiteBall = billiardTable.getWhiteBall().getPoint();

        if(whiteBall.x > min.x && whiteBall.x < max.x &&
            whiteBall.y > min.y && whiteBall.y < max.y){
            return true;
        }

        return false;
    }

    private boolean isSomethingOnPoint(Point point) {
        if(billiardTable.getBalls() == null) {
            return false;
        }

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
