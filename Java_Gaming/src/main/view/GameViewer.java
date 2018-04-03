package main.view;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import main.model.Object;

public class GameViewer {
	GraphicsContext graphic;
	
	//draws objects into viewer graphics, draws clears the frame then draws object with given list.
	public void draw(ArrayList<Object> objects) {
		graphic.clearRect(0, 0, 1280, 720);
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
	}
	
}
