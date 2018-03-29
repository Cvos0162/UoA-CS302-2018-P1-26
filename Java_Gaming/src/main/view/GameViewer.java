package main.view;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import main.model.Object;

public class GameViewer {
	GraphicsContext graphic;
	public void draw(ArrayList<Object> objects) {
		graphic.clearRect(0, 0, 1280, 800);
		for (int i = 0; i < objects.size(); i++) {
			graphic.drawImage(objects.get(i).getCurrentImage(), objects.get(i).getPosition().getX(), objects.get(i).getPosition().getY());
		}
	}
	
	public GameViewer(GraphicsContext graphic) {
		this.graphic = graphic;
	}
	
}
