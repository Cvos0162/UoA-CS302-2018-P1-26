package main.model;

import javafx.scene.image.Image;

public class Protagonist extends Character{

	private int life;
	private Ability storedAbility;
	boolean alive;
	
	
	public Protagonist(Position cornor_1, Image im) {
		super(cornor_1, im, Ability.DEFAULT);
		storedAbility = Ability.DEFAULT;
	}
	public Protagonist(Position cornor_1, Position size, Image im) {
		super(cornor_1, size, im, Ability.DEFAULT);
		storedAbility = Ability.DEFAULT;
	}
	private void setLife(int life) {
		this.life = life;
	}

	private void increaseLife() {
		life++;
	}
	
	private void decreaseLife() {
		life--;
	}
	
	public int getLife() {
		return life;
	}
	
	private void useAbility(Ability ability) {
		this.ability = ability;
	}
	
	private void setStoredAbility(Ability ability) {
		this.storedAbility = ability;
	}
	
	public Ability getStoredAbility() {
		return storedAbility;
	}
	
}