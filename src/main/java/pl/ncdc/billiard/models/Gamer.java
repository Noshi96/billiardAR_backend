package pl.ncdc.billiard.models;

import org.opencv.core.Point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Gamer {
	private int id;
	private Point startCheckingPoint;
	private int currentScore;
	private double leftOwnBand; // x = 0  -- x = table.width,  y=leftOwnBand
	private double rightOwnBand; // x = 0  -- x = table.widt,h y=rightOwnBand
	private double startLine;
	private double endLine;
	private double lineX;
	private Point mediumLvlPoint;
	private Point hardLvlPoint;
	private boolean betterThenOthers;
	private Point posShowScore;
	private boolean oppositeTrack; 
	
}
