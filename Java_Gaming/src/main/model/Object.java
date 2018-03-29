package main.model;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.image.Image;

public class Object {
	Position position;
	ArrayList<Image> image;
	int p; //iterator
	
	public Object(Position position) {
		this.position = position;
		image = new ArrayList<Image>();
		p = 0;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position pos) {
		this.position = pos;
	}
	public void setPosition(int x, int y) {
		this.position = new Position(x, y);
	}
	public Image getCurrentImage() {
		return image.get(p);
	}
	public void addImage (Image im) {
		image.add(im);
	}
}
