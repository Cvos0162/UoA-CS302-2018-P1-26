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
	ArrayList<Wall> walls;
	Protagonist pro;
	Item item;
	ArrayList<Pellet> pellets;
	ArrayList<Ghost> ghosts;
	public Protagonist getPro() {
		return pro;
	}
	public Item getItem() {
		return item;
	}
	public ArrayList<Wall> getWalls() {
		return walls;
	}
	public ArrayList<Object> getObjectList() {
		return objects;
	}
	public ArrayList<Pellet> getPellets() {
		return pellets;
	}
	public ArrayList<Ghost> getGhosts() {
		return ghosts;
	}
	public Level(int world, int stage) {	
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
			walls = new ArrayList<Wall>();
			pellets = new ArrayList<Pellet>();
			ghosts = new ArrayList<Ghost>();
			int j = 0;
			while ((line = bufferedReader.readLine()) != null) {
				for (int i = 0; i < line.length(); i++) {
					Position pos = new Position(25*i, 25*j+70);
					char id = line.charAt(i);
					if (id >= 'a' && id <= 'p') {
						String impath = "/resource/test_Wall_" + id + ".png";
						Wall wall = new Wall(pos, new Image(impath));
						walls.add(wall);
					}
					if (id == '0') {
						pos = new Position(25*i + 0.5, 25*j+70.5);
						pro = new Protagonist(pos, new Position(24.4,24.4), new Image("/resource/Untitled.png"));
					}
					
					if (id == '1') {
						pos = new Position(25*i + 0.5, 25*j+70.5);
						Ghost ghost = new Ghost(pos, new Position(24.4,24.4), new Image("/resource/Untitled.png"), Ability.RAINBOW_STAR);
						ghosts.add(ghost);
					}
					if (id == '2') {
						pos = new Position(25*i + 0.5, 25*j+70.5);
						Ghost ghost = new Ghost(pos, new Position(24.4,24.4), new Image("/resource/Untitled.png"), Ability.NURSE);
						ghosts.add(ghost);
					}
					if (id == '3') {
						pos = new Position(25*i + 0.5, 25*j+70.5);
						Ghost ghost = new Ghost(pos, new Position(24.4,24.4), new Image("/resource/Untitled.png"), Ability.WIZARD);
						ghosts.add(ghost);
					}
					if (id == '4') {
						pos = new Position(25*i + 0.5, 25*j+70.5);
						Ghost ghost = new Ghost(pos, new Position(24.4,24.4), new Image("/resource/Untitled.png"), Ability.ICE);
						ghosts.add(ghost);
					}
					if (id == '5') {
						pos = new Position(25*i + 0.5, 25*j+70.5);
						Ghost ghost = new Ghost(pos, new Position(24.4,24.4), new Image("/resource/Untitled.png"), Ability.NINJA);
						ghosts.add(ghost);
					}
					
					
					if (id == '*') {
						pos = new Position(25*i + 12.5, 25*j+82.5);
						Pellet pallet = new Pellet(pos, new Position(5,5), new Image("/resource/pellet.png"));
						pellets.add(pallet);
						
					}
					
					if (id == '#') {
						pos = new Position(25*i + 2.5, 25*j+72.5);
						item = new Item(pos, new Position(20,20), new Image("/resource/item.png"));
					}
				}
				j++;
				System.out.println(line);
			}
			bufferedReader.close();
			objects.addAll(walls);
			objects.addAll(pellets);
			objects.add(getItem());
			objects.add(pro);
			objects.addAll(ghosts);
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + path + "'");                
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
