package main.model;

import javafx.scene.image.Image;

public class Ghost extends Character{
	
	Level level;

	public Ghost(Position cornor_1, Position size, Image im, Ability ability) {
		super(cornor_1, size, im, ability);
	}
	
}