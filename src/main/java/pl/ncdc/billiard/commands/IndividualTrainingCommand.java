package pl.ncdc.billiard.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndividualTrainingCommand {

	private Long id;

	private String difficultyLvl;

	private String positionOfWhiteBall;
	private String positionOfWhiteBallX;
	private String positionOfWhiteBallY;

	private String positionOfSelectedBall;
	private String positionOfSelectedBallX;
	private String positionOfSelectedBallY;

	private String positionsOfDisturbBalls;
	private String positionsOfDisturbBallsX;
	private String positionsOfDisturbBallsY;

	private String positionOfRectangle;
	private String positionOfRectangleX;
	private String positionOfRectangleY;

    private int idPocket;
}



