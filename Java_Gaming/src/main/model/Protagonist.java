package main.model;

import javafx.scene.image.Image;

public class Protagonist extends Character{

	private int life;
	private int maxLife;
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
		life = 3;
		maxLife = 5;
		alive = true;
	}
	public Protagonist(Position cornor_1, Position size, Image im) {
		super(cornor_1, size, im, Ability.DEFAULT);
		storedAbility = Ability.DEFAULT;
		usableAbility = false;
		untouchable = false;
		item = false;
		life = 3;
		maxLife = 5;
		alive = true;
	}
	
	public void setUntouchable(boolean b) {
		untouchable = b;
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
	
	public int getMaxLife() {
		return maxLife;
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