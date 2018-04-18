package main.model;

import java.util.ArrayList;
import javafx.scene.image.Image;

//protagonist is extends of character
public class Protagonist extends Character{
	
	//intialise flags and variables
	private int life;
	private int maxLife;
	private Ability storedAbility;
	boolean alive;
	boolean item;
	boolean usableAbility;
	boolean untouchable;
	
	//initialise protagonist
	public Protagonist(Position cornor_1, Image im) {
		super(cornor_1, im, Ability.DEFAULT);
		storedAbility = Ability.DEFAULT;
		usableAbility = false;
		untouchable = false;
		item = false;
		life = 3;
		maxLife = 5;
		alive = true;
	}
	public Protagonist(Position cornor_1, Position size, Image im) {
		super(cornor_1, size, im, Ability.DEFAULT);
		storedAbility = Ability.DEFAULT;
		usableAbility = false;
		untouchable = false;
		item = false;
		life = 3;
		maxLife = 5;
		alive = true;
	}
	
	public Protagonist(Position cornor_1, Position size, ArrayList<Image> im) {
		super(cornor_1, size, im, Ability.DEFAULT);
		storedAbility = Ability.DEFAULT;
		usableAbility = false;
		untouchable = false;
		item = false;
		life = 3;
		maxLife = 5;
		alive = true;
	}
	//set untouchable flag
	public void setUntouchable(boolean b) {
		untouchable = b;
	}
	//increase life
	public void increaseLife() {
		life++;
	}
	//decrease life
	public void decreaseLife() {
		life--;
	}
	//get life
	public int getLife() {
		return life;
	}
	//get max life
	public int getMaxLife() {
		return maxLife;
	}
	//change stored ability
	public void setStoredAbility(Ability ability) {
		this.storedAbility = ability;
	}
	//get stored ability
	public Ability getStoredAbility() {
		return storedAbility;
	}
	
}