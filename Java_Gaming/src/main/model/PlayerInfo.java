package main.model;

public class PlayerInfo{

	private int totalScore = 0;
	private int worldLevel;
	private int stageLevel;
	
	public int getScore() {
		return totalScore;
	}
	public void addScore(Pellet pellet) {
		totalScore += pellet.getScore();
	}
	
	public int getWorldLevel() {
		return worldLevel;
	}
	
	public int getStageLevel() {
		return stageLevel;
	}
	
}