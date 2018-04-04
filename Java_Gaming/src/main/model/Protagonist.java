package main.model;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class Protagonist extends Character{

	private int life;
	private Ability storedAbility;
	static Ability ability;
	boolean alive;
	
	
	public Protagonist(Position cornor_1, Image im, Ability ability) {
		super(cornor_1, im, ability);
		storedAbility = ability.DEFAULT;
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