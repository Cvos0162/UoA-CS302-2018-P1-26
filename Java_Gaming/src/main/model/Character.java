package main.model;

import java.util.ArrayList;

import javafx.scene.image.Image;

//character is extends of object
public class Character extends Object{

	Ability ability;
	Direction direction;
	//initalise alive flag
	boolean alive;
	
	//initalise character
	public Character(Position cornor_1, Image im, Ability ability) {
		super(cornor_1, im);
		this.ability = ability;
		direction = Direction.DOWN;
		alive = true;
	}
	public Character(Position cornor_1, Position size, Image im, Ability ability) {
		super(cornor_1, size, im);
		this.ability = ability;
		direction = Direction.DOWN;
		alive = true;
	}
	public Character(Position cornor_1, Position size, ArrayList<Image> im, Ability ability) {
		super(cornor_1, size, im);
		this.ability = ability;
		direction = Direction.DOWN;
		alive = true;
	}
	
	//set alive flag of character
	public void setAlive(boolean alive, ArrayList<Image> im) {
		this.alive = alive;
		this.images = im;
	}
	//set direction of character
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	//get alive flag
	public boolean getAlive() {
		return alive;
	}
	//get ability
	public Ability getAbility() {
		return ability;
	}
	//get direction
	public Direction getDirection() {
		return direction;
	}
}