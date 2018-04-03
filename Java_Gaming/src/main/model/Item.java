package main.model;

import javafx.scene.image.Image;

public class Item extends Object{

	private int score;

	public Item(Position cornor_1, Image im) {
		super(cornor_1, im);
		// TODO Auto-generated constructor stub
		
	}
	
	private void setItemScore(int score){
		this.score = score;
	}
}