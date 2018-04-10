package main.model;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class Character extends Object{

	Ability ability;
	Direction direction;
	boolean alive;
	
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
	
	public void changeDirection(int direction) {
		if(direction == 1) {
			this.direction = Direction.UP;
		}
		if(direction == 2) {
			this.direction = Direction.DOWN;
		}
		if(direction == 3) {
			this.direction = Direction.RIGHT;
		}
		if(direction == 4) {
			this.direction = Direction.LEFT;
		}
	}
	
	public void setAlive(boolean alive, ArrayList<Image> im) {
		this.alive = alive;
		this.images = im;
		
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public boolean getAlive() {
		return alive;
	}
	
	public Ability getAbility() {
		return ability;
	}

	public Direction getDirection() {
		return direction;
	}
}