package main.view;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.model.Object;

public class GameViewer {
	GraphicsContext graphic;
	Colour colour;
	
	public void setColour(Colour colour) {
		this.colour = colour;
	}
	//draws objects into viewer graphics, draws clears the frame then draws object with given list.
	public void draw(ArrayList<Object> objects) {
		switch(colour) {
		case WHITE:
			graphic.clearRect(0, 0, graphic.getCanvas().getWidth(), graphic.getCanvas().getHeight());
			break;
		case BLACK:
			graphic.setFill(Color.BLACK);
			graphic.fillRect(0, 0, graphic.getCanvas().getWidth(), graphic.getCanvas().getHeight());
			break;
		default:
			break;
		}
		for (int i = 0; i < objects.size(); i++) {
			graphic.drawImage(objects.get(i).getCurrentImage(),
					objects.get(i).getPosition().getX(),
					objects.get(i).getPosition().getY(),
					objects.get(i).getSize().getX(),
					objects.get(i).getSize().getY()
					);
		}
	}
	
	public GameViewer(GraphicsContext graphic) {
		this.graphic = graphic;
		this.colour = Colour.WHITE;
	}
	
}
