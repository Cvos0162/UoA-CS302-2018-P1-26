package main.model;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class Protagonist extends Character{

	private int life;
	private int maxLife;
	private Ability storedAbility;
	boolean alive;
	boolean item;
	boolean usableAbility;
	boolean untouchable;
	
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
	
	public Protagonist(Position cornor_1, Position size, ArrayList<Image> im) {
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
	
	public void setStoredAbility(Ability ability) {
		this.storedAbility = ability;
	}
	
	public Ability getStoredAbility() {
		return storedAbility;
	}
	
}