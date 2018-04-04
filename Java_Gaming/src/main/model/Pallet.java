package main.model;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class Pallet extends Object{

	private int score;
	boolean consumed;

	public Pallet(Position cornor_1, Image im) {
		super(cornor_1, im);
		
	}
	
	private void setPalletScore(int score){
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