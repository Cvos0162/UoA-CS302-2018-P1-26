package main.model;

import javafx.scene.image.Image;

public class Pallet extends Object{

	private int score;

	public Pallet(Position cornor_1, Image im) {
		super(cornor_1, im);
		// TODO Auto-generated constructor stub
		
	}
	
	private void setPalletScore(int score){
		this.score = score;
	}
}