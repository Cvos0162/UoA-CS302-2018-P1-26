package main.model;

import javafx.scene.image.Image;

public class Protagonist extends Character{

	private int life;
	private Ability storedAbility;
	boolean alive;
	boolean item;
	private int score;
	
	
	public Protagonist(Position cornor_1, Image im) {
		super(cornor_1, im, Ability.DEFAULT);
		storedAbility = Ability.DEFAULT;
		item = false;
		life = 1;
		alive = true;
	}
	public Protagonist(Position cornor_1, Position size, Image im) {
		super(cornor_1, size, im, Ability.DEFAULT);
		storedAbility = Ability.DEFAULT;
		item = false;
		life = 1;
		alive = true;
	}
	private void setLife(int life) {
		this.life = life;
	}

	public void increaseLife() {
		life++;
	}
	
	public void decreaseLife() {
		life--;
	}
	
	public int getLife() {
		return life;
	}
	
	public int getScore() {
		return score;
	}
	
	public void addScore(int score) {
		this.score = this.score + score;
	}
	
	private void useAbility(Ability ability) {
		this.ability = ability;
	}
	
	public void setStoredAbility(Ability ability) {
		this.storedAbility = ability;
	}
	
	public Ability getStoredAbility() {
		return storedAbility;
	}
	
}