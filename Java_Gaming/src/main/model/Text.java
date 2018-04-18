package main.model;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Text {
	Position position;
	double maxWidth;
	String string;
	Color color;
	Font font;
	//initialise text
	public Text(Position position, double maxWidth, String string, int size, Color color) {
		this.position = position;
		this.maxWidth = maxWidth;
		this.string = string;
		this.color = color;
		this.font = new Font("Calibri", size);
	}
	public Text(Position position, double maxWidth, int string, int size, Color color) {
		this.position = position;
		this.maxWidth = maxWidth;
		this.string = ""+string;
		this.color = color;
		this.font = new Font("Calibri", size);
	}
	//basic getter and setter
	public void setString(String string) {
		this.string = string;
	}
	public void setString(int string) {
		this.string = "" + string;
	}
	public Font getFont() {
		return font;
	}
	public Color getColor() {
		return color;
	}
	public String getString() {
		return string;
	}
	public Position getPosition() {
		return position;
	}
	public double getMaxWidth() {
		return maxWidth;
	}
}
