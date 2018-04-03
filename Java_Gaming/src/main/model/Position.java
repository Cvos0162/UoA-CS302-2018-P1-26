package main.model;

public class Position {
	private double x, y;
	public void setX(int x) {this.x = x;}
	public void setY(int y) {this.y = y;}
	public double getX() {return x;}
	public double getY() {return y;}
	public Position(double x, double y) {
		this.x = x; this.y = y;
	}
}
