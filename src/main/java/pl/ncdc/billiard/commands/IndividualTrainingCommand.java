package pl.ncdc.billiard.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndividualTrainingCommand {

	private long id;

	private String difficultyLvl;

	private String positionOfWhiteBallX;
	private String positionOfWhiteBallY;

	private String positionOfSelectedBallX;
	private String positionOfSelectedBallY;

	private String positionsOfDisturbBallsX;
	private String positionsOfDisturbBallsY;

	private String positionOfRectangleX;
	private String positionOfRectangleY;
}



