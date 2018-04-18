package main.model;

import java.util.ArrayList;

import javafx.scene.image.Image;

//pellet is extends of object
public class Pellet extends Object{

	//intialise score and consumed flag
	private int score;
	boolean consumed;

	//intialise pellet
	public Pellet(Position cornor_1,Position size, Image im, int score) {
		super(cornor_1, size, im);
		this.score = score;
		
	}
	//get score
	public int getScore() {
		return score;
	}
	//make consumed and set consume flag
	public void setConsumed(boolean consumed, ArrayList<Image> im) {
		this.consumed = consumed;
		this.images = im;
	}
	//get consumed flag
	public boolean getConsumed() {
		return consumed;
	}
}