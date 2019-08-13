package pl.ncdc.billiard.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class IndividualTraining {
	@Id
	private int id;
	
	private String difficultyLvl;
	
	private String positionOfWhiteBall;
	
	private String positionOfSelectedBall;
	
	private String positionsOfDisturbBalls;
	
	private String positionOfRectangle;
	


	public IndividualTraining(int id, String difficultyLvl, String positionOfWhiteBall, String positionOfSelectedBall,
			String positionsOfDisturbBalls, String positionOfRectangle) {
		this.id = id;
		this.difficultyLvl = difficultyLvl;
		this.positionOfWhiteBall = positionOfWhiteBall;
		this.positionOfSelectedBall = positionOfSelectedBall;
		this.positionsOfDisturbBalls = positionsOfDisturbBalls;
		this.positionOfRectangle = positionOfRectangle;
	}
	
	public IndividualTraining() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDifficultyLvl() {
		return difficultyLvl;
	}

	public void setDifficultyLvl(String difficultyLvl) {
		this.difficultyLvl = difficultyLvl;
	}

	public String getPositionOfWhiteBall() {
		return positionOfWhiteBall;
	}

	public void setPositionOfWhiteBall(String positionOfWhiteBall) {
		this.positionOfWhiteBall = positionOfWhiteBall;
	}

	public String getPositionOfSelectedBall() {
		return positionOfSelectedBall;
	}

	public void setPositionOfSelectedBall(String positionOfSelectedBall) {
		this.positionOfSelectedBall = positionOfSelectedBall;
	}

	public String getPositionsOfDisturbBalls() {
		return positionsOfDisturbBalls;
	}

	public void setPositionsOfDisturbBalls(String positionsOfDisturbBalls) {
		this.positionsOfDisturbBalls = positionsOfDisturbBalls;
	}

	public String getPositionOfRectangle() {
		return positionOfRectangle;
	}

	public void setPositionOfRectangle(String positionOfRectangle) {
		this.positionOfRectangle = positionOfRectangle;
	}
	
	
	
	
}

