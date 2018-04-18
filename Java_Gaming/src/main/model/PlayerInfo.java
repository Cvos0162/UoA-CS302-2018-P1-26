package main.model;

public class PlayerInfo{

	//intialise variables
	private int totalScore = 0;
	private int worldLevel;
	private int stageLevel;
	
	//get total score
	public int getScore() {
		return totalScore;
	}
	//add score to total score
	public void addScore(Pellet pellet) {
		totalScore += pellet.getScore();
	}
	//get world level
	public int getWorldLevel() {
		return worldLevel;
	}
	//get stage level
	public int getStageLevel() {
		return stageLevel;
	}
	
}