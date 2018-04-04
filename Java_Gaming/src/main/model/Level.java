package main.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.image.Image;

public class Level {
	ArrayList<Object> objects;
	int world;
	int stage;
	public ArrayList<Object> getObjectList() {
		return objects;
	}
	public Level(int world, int stage) {
		
		//TODO: this function has to load a level design according to its world and stage selected 
		System.out.println("world : " + world);
		System.out.println("stage : " + stage);
		System.out.println("initialising level");
		
		String path = "./src/level/" + world + "-" + stage + ".lvl";
		String line = null;
		try {
			File file = new File(path);
			if (!file.exists()) {
				System.out.println("file does not exist");
			}
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			objects = new ArrayList<Object>();
			int j = 0;
			while ((line = bufferedReader.readLine()) != null) {
				for (int i = 0; i < line.length(); i++) {
					Position pos = new Position(25*i, 25*j+70);
					char id = line.charAt(i);
					if (id >= 'a' && id <= 'p') {
						String impath = "/resource/test_Wall_" + id + ".png";
						objects.add(new Wall(pos, new Image(impath)));
					}
					
				}
				j++;
				System.out.println(line);
			}
			bufferedReader.close();
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + path + "'");                
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
