package pl.ncdc.billiard.commands;

import java.util.List;

import org.opencv.core.Point;

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
		
		public IndividualTrainingCommand(){
			
		}
		
		public IndividualTrainingCommand(long id, String difficultyLvl, String positionOfWhiteBallX,
				String positionOfWhiteBallY, String positionOfSelectedBallX, String positionOfSelectedBallY,
				String positionsOfDisturbBallsX, String positionsOfDisturbBallsY, String positionOfRectangleX,
				String positionOfRectangleY) {
			super();
			this.id = id;
			this.difficultyLvl = difficultyLvl;
			this.positionOfWhiteBallX = positionOfWhiteBallX;
			this.positionOfWhiteBallY = positionOfWhiteBallY;
			this.positionOfSelectedBallX = positionOfSelectedBallX;
			this.positionOfSelectedBallY = positionOfSelectedBallY;
			this.positionsOfDisturbBallsX = positionsOfDisturbBallsX;
			this.positionsOfDisturbBallsY = positionsOfDisturbBallsY;
			this.positionOfRectangleX = positionOfRectangleX;
			this.positionOfRectangleY = positionOfRectangleY;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getDifficultyLvl() {
			return difficultyLvl;
		}
		public void setDifficultyLvl(String difficultyLvl) {
			this.difficultyLvl = difficultyLvl;
		}
		public String getPositionOfWhiteBallX() {
			return positionOfWhiteBallX;
		}
		public void setPositionOfWhiteBallX(String positionOfWhiteBallX) {
			this.positionOfWhiteBallX = positionOfWhiteBallX;
		}
		public String getPositionOfWhiteBallY() {
			return positionOfWhiteBallY;
		}
		public void setPositionOfWhiteBallY(String positionOfWhiteBallY) {
			this.positionOfWhiteBallY = positionOfWhiteBallY;
		}
		public String getPositionOfSelectedBallX() {
			return positionOfSelectedBallX;
		}
		public void setPositionOfSelectedBallX(String positionOfSelectedBallX) {
			this.positionOfSelectedBallX = positionOfSelectedBallX;
		}
		public String getPositionOfSelectedBallY() {
			return positionOfSelectedBallY;
		}
		public void setPositionOfSelectedBallY(String positionOfSelectedBallY) {
			this.positionOfSelectedBallY = positionOfSelectedBallY;
		}
		public String getPositionsOfDisturbBallsX() {
			return positionsOfDisturbBallsX;
		}
		public void setPositionsOfDisturbBallsX(String positionsOfDisturbBallsX) {
			this.positionsOfDisturbBallsX = positionsOfDisturbBallsX;
		}
		public String getPositionsOfDisturbBallsY() {
			return positionsOfDisturbBallsY;
		}
		public void setPositionsOfDisturbBallsY(String positionsOfDisturbBallsY) {
			this.positionsOfDisturbBallsY = positionsOfDisturbBallsY;
		}
		public String getPositionOfRectangleX() {
			return positionOfRectangleX;
		}
		public void setPositionOfRectangleX(String positionOfRectangleX) {
			this.positionOfRectangleX = positionOfRectangleX;
		}
		public String getPositionOfRectangleY() {
			return positionOfRectangleY;
		}
		public void setPositionOfRectangleY(String positionOfRectangleY) {
			this.positionOfRectangleY = positionOfRectangleY;
		}

				
	}



