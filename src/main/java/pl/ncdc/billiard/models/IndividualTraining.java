package pl.ncdc.billiard.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndividualTraining {
	@Id
	private long id;
	
	private String difficultyLvl;
	
	private String positionOfWhiteBall;
	
	private String positionOfSelectedBall;
	
	private String positionsOfDisturbBalls;
	
	private String positionOfRectangle;
}

