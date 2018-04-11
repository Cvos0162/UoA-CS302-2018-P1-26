package main.model;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class Pellet extends Object{

	private int score;
	boolean consumed;

	public Pellet(Position cornor_1,Position size, Image im, int score) {
		super(cornor_1, size, im);
		this.score = score;
		
	}
	
	public int getScore() {
		return score;
	}
	
	public void setConsumed(boolean consumed, ArrayList<Image> im) {
		this.consumed = consumed;
		this.images = im;
	}
	
	public boolean getConsumed() {
		return consumed;
	}
}