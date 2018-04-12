package main.model;

import javafx.scene.image.Image;

public class Protagonist extends Character{

	private int life;
	private Ability storedAbility;
	boolean alive;
	boolean item;
	boolean usableAbility;
	boolean untouchable;
	private int score;
	
	
	public Protagonist(Position cornor_1, Image im) {
		super(cornor_1, im, Ability.DEFAULT);
		storedAbility = Ability.DEFAULT;
		usableAbility = false;
		untouchable = false;
		item = false;
		life = 1;
		alive = true;
	}
	public Protagonist(Position cornor_1, Position size, Image im) {
		super(cornor_1, size, im, Ability.DEFAULT);
		storedAbility = Ability.DEFAULT;
		usableAbility = false;
		untouchable = false;
		item = false;
		life = 1;
		alive = true;
	}
	
	public void beUntouchable() {
		untouchable = true;
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