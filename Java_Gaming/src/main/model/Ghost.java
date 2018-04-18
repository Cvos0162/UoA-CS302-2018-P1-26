package main.model;

import java.util.ArrayList;

import javafx.scene.image.Image;

//ghosts are extends of charcter
public class Ghost extends Character{
	
	Level level;
	//initialise freeze flag
	boolean freeze;

	//initialise ghost
	public Ghost(Position cornor_1, Position size, Image im, Ability ability) {
		super(cornor_1, size, im, ability);
		freeze = false;
	}
	public Ghost(Position cornor_1, Position size, ArrayList<Image> im, Ability ability) {
		super(cornor_1, size, im, ability);
		freeze = false;
	}
	
}