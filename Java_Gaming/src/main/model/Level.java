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
	ArrayList<Pellet> pellets;
	ArrayList<Ghost> ghosts;
	ArrayList<Item> items;
	public Protagonist getPro() {
		return pro;
	}
	public ArrayList<Item> getItem() {
		return items;
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
	public void removePellets(Pellet p) {
		pellets.remove(p);
	}
	public void removePro() {
		pro = null;
	}
	public void removeItems(Item i) {
		items.remove(i);
	}
	public void removeGhosts(Ghost g) {
		ghosts.remove(g);
	}
	public ArrayList<Ghost> getGhosts() {
		return ghosts;
	}
	public int getWorld() {
		return world;
	}
	public int getStage() {
		return stage;
	}
	
	public void resetCharacter() {
		System.out.println("resetting level");
		
		String path = "./src/level/" + (world + 1) + "-" + (stage + 1) + ".lvl";
		String line = null;
		
		objects.removeAll(ghosts);
		
		ghosts = new ArrayList<Ghost>();
		
		try {
			File file = new File(path);
			if (!file.exists()) {
				System.out.println("file does not exist");
			}
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			ghosts = new ArrayList<Ghost>();
			int j = 0;
			while ((line = bufferedReader.readLine()) != null) {
				for (int i = 0; i < line.length(); i++) {
					Position pos = new Position(25*i, 25*j+70);
					char id = line.charAt(i);
					if (id == '0') {
						pos = new Position(25*i + 0.5, 25*j+70.5);
						pro.setPosition(pos);
					}
					
					else if (id == '1') {
						pos = new Position(25*i + 0.5, 25*j+70.5);
						Ghost ghost = new Ghost(pos, new Position(24,24), new Image("/resource/rainbow.png"), Ability.RAINBOW_STAR);
						ghosts.add(ghost);
					}
					else if (id == '2') {
						pos = new Position(25*i + 0.5, 25*j+70.5);
						Ghost ghost = new Ghost(pos, new Position(24,24), new Image("/resource/nurse.png"), Ability.NURSE);
						ghosts.add(ghost);
					}
					else if (id == '3') {
						pos = new Position(25*i + 0.5, 25*j+70.5);
						Ghost ghost = new Ghost(pos, new Position(24,24), new Image("/resource/wizard.png"), Ability.WIZARD);
						ghosts.add(ghost);
					}
					else if (id == '4') {
						pos = new Position(25*i + 0.5, 25*j+70.5);
						Ghost ghost = new Ghost(pos, new Position(24,24), new Image("/resource/iceman.png"), Ability.ICE);
						ghosts.add(ghost);
					}
					else if (id == '5') {
						pos = new Position(25*i + 0.5, 25*j+70.5);
						Ghost ghost = new Ghost(pos, new Position(24,24), new Image("/resource/Untitled.png"), Ability.NINJA);
						ghosts.add(ghost);
					}
				}
				j++;
			}
			bufferedReader.close();

			objects.addAll(ghosts);
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + path + "'");                
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
	public Level(int world, int stage) {	
		this.world = world - 1;
		this.stage = stage - 1;
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
			items = new ArrayList<Item>();
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
						pro = new Protagonist(pos, new Position(24,24), new Image("/resource/pro.png"));
					}
					
					if (id == '1') {
						pos = new Position(25*i + 0.5, 25*j+70.5);
						Ghost ghost = new Ghost(pos, new Position(24,24), new Image("/resource/rainbow.png"), Ability.RAINBOW_STAR);
						ghosts.add(ghost);
					}
					if (id == '2') {
						pos = new Position(25*i + 0.5, 25*j+70.5);
						Ghost ghost = new Ghost(pos, new Position(24,24), new Image("/resource/nurse.png"), Ability.NURSE);
						ghosts.add(ghost);
					}
					if (id == '3') {
						pos = new Position(25*i + 0.5, 25*j+70.5);
						Ghost ghost = new Ghost(pos, new Position(24,24), new Image("/resource/wizard.png"), Ability.WIZARD);
						ghosts.add(ghost);
					}
					if (id == '4') {
						pos = new Position(25*i + 0.5, 25*j+70.5);
						Ghost ghost = new Ghost(pos, new Position(24,24), new Image("/resource/iceman.png"), Ability.ICE);
						ghosts.add(ghost);
					}
					if (id == '5') {
						pos = new Position(25*i + 0.5, 25*j+70.5);
						Ghost ghost = new Ghost(pos, new Position(24,24), new Image("/resource/Untitled.png"), Ability.NINJA);
						ghosts.add(ghost);
					}
					
					
					if (id == '*') {
						pos = new Position(25*i + 12.5, 25*j+82.5);
						Pellet pallet = new Pellet(pos, new Position(5,5), new Image("/resource/pellet.png"), 10);
						pellets.add(pallet);
						
					}
					
					if (id == '#') {
						pos = new Position(25*i + 2.5, 25*j+72.5);
						Item item = new Item(pos, new Position(20,20), new Image("/resource/item.png"), 100);
						items.add(item);
					}
				}
				j++;
				System.out.println(line);
			}
			bufferedReader.close();
			objects.addAll(walls);
			objects.addAll(pellets);
			objects.addAll(items);
			objects.add(pro);
			objects.addAll(ghosts);
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + path + "'");                
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
