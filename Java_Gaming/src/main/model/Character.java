package main.model;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class Character extends Object{

	Ability ability;
	boolean alive;
	
	public Character(Position cornor_1, Image im, Ability ability) {
		super(cornor_1, im);
		this.ability = ability;
		alive = true;
	}
	
	public void setAlive(boolean alive, ArrayList<Image> im) {
		this.alive = alive;
		this.images = im;
		
	}
	
	public boolean getAlive() {
		return alive;
	}
	
	public Ability getAbility() {
		return ability;
	}

}