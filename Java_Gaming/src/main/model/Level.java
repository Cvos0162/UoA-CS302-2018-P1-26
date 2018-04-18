package main.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.image.Image;

public class Level {
	//initialise objects and array list
	int world;
	int stage;
	Protagonist pro;
	ArrayList<Object> objects;
	ArrayList<Wall> walls;
	ArrayList<Pellet> pellets;
	ArrayList<Ghost> ghosts;
	ArrayList<Item> items;
	Object background;
	Object top;
	Object side;
	//return objects
	public Object getTop() {
		return top;
	}
	public Object getSide() {
		return side;
	}
	public Object getBackground() {
		return background;
	}
	//return protagonist
	public Protagonist getPro() {
		return pro;
	}
	//return array list
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
	//remove objects
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
	//reset chracter
	public void resetCharacter() {		
		String path = "./src/level/" + (world + 1) + "-" + (stage + 1) + ".lvl";
		String line = null;
		//remove ghosts
		objects.removeAll(ghosts);
		//reload file
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
					//get position of ghosts
					Position pos = new Position(25*i, 25*j+66);
					char id = line.charAt(i);
					//initialise protagonist
					if (id == '0') {
						pos = new Position(25*i + 0.5, 25*j+66);
						pro.setPosition(pos);
					}
					//initialise rainbow_star ghost
					else if (id == '1') {
						pos = new Position(25*i, 25*j+66);
						ArrayList<Image> ghostImage = new ArrayList<Image>();
						ghostImage.add(new Image("/resource/rainbow_0.png"));
						ghostImage.add(new Image("/resource/rainbow_1.png"));
						Ghost ghost = new Ghost(pos, new Position(24,24), ghostImage, Ability.RAINBOW_STAR);
						ghosts.add(ghost);
					}
					//initialise nurse ghost
					else if (id == '2') {
						pos = new Position(25*i, 25*j+66);
						ArrayList<Image> ghostImage = new ArrayList<Image>();
						ghostImage.add(new Image("/resource/nurse_0.png"));
						ghostImage.add(new Image("/resource/nurse_1.png"));
						Ghost ghost = new Ghost(pos, new Position(24,24), ghostImage, Ability.NURSE);
						ghosts.add(ghost);
					}
					//initialise wizard ghost
					else if (id == '3') {
						pos = new Position(25*i , 25*j+66);
						ArrayList<Image> ghostImage = new ArrayList<Image>();
						ghostImage.add(new Image("/resource/wizard_0.png"));
						ghostImage.add(new Image("/resource/wizard_1.png"));
						Ghost ghost = new Ghost(pos, new Position(24,24), ghostImage, Ability.WIZARD);
						ghosts.add(ghost);
					}
					//initialise iceman ghost
					else if (id == '4') {
						pos = new Position(25*i, 25*j+66);
						ArrayList<Image> ghostImage = new ArrayList<Image>();
						ghostImage.add(new Image("/resource/iceman_0.png"));
						ghostImage.add(new Image("/resource/iceman_1.png"));
						Ghost ghost = new Ghost(pos, new Position(24,24), ghostImage, Ability.ICE);
						ghosts.add(ghost);
					}
					//initialise ninja ghost
					else if (id == '5') {
						pos = new Position(25*i, 25*j+66);
						ArrayList<Image> ghostImage = new ArrayList<Image>();
						ghostImage.add(new Image("/resource/ninja_0.png"));
						ghostImage.add(new Image("/resource/ninja_1.png"));
						Ghost ghost = new Ghost(pos, new Position(24,24), ghostImage, Ability.NINJA);
						ghosts.add(ghost);
					}
				}
				j++;
			}
			bufferedReader.close();
			getPro().setIterator(0);
			Image ghostDie = new Image("/resource/ghostDie.png");
			for (int i = 0; i < ghosts.size(); i++) { ghosts.get(i).addImage(ghostDie); }
			objects.addAll(ghosts);
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + path + "'");                
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	//initiallise level
	public Level(int world, int stage) {	
		this.world = world - 1;
		this.stage = stage - 1;
		String path = "./src/level/" + world + "-" + stage + ".lvl";
		String line = null;
		//read file
		try {
			File file = new File(path);
			if (!file.exists()) {
				System.out.println("file does not exist");
			}
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			//make new array lists
			objects = new ArrayList<Object>();
			walls = new ArrayList<Wall>();
			pellets = new ArrayList<Pellet>();
			ghosts = new ArrayList<Ghost>();
			items = new ArrayList<Item>();
			int j = 0;
			while ((line = bufferedReader.readLine()) != null) {
				for (int i = 0; i < line.length(); i++) {
					Position pos = new Position(25*i, 25*j+66);
					char id = line.charAt(i);
					//make walls
					if (id >= 'a' && id <= 'p') {
						String impath = "/resource/Wall_" + world + "_" + id + ".png";
						Wall wall = new Wall(pos, new Image(impath));
						walls.add(wall);
					}
					//make protagonist
					else if (id == '0') {
						pos = new Position(25*i, 25*j+66);
						ArrayList<Image> proImage = new ArrayList<Image>();
						proImage.add(new Image("/resource/pro_0.png"));
						proImage.add(new Image("/resource/pro_1.png"));
						proImage.add(new Image("/resource/pro_2.png"));
						proImage.add(new Image("/resource/pro_3.png"));
						proImage.add(new Image("/resource/proDied_0.png"));
						proImage.add(new Image("/resource/proDied_1.png"));
						proImage.add(new Image("/resource/shadow_pro_0.png"));
						proImage.add(new Image("/resource/shadow_pro_1.png"));
						proImage.add(new Image("/resource/shadow_pro_2.png"));
						proImage.add(new Image("/resource/shadow_pro_3.png"));
						pro = new Protagonist(pos, new Position(24,24), proImage);
					}
					//make rainbow_star ghost
					else if (id == '1') {
						pos = new Position(25*i, 25*j+66);
						ArrayList<Image> ghostImage = new ArrayList<Image>();
						ghostImage.add(new Image("/resource/rainbow_0.png"));
						ghostImage.add(new Image("/resource/rainbow_1.png"));
						Ghost ghost = new Ghost(pos, new Position(24,24), ghostImage, Ability.RAINBOW_STAR);
						ghosts.add(ghost);
					}
					//make nurse ghost
					else if (id == '2') {
						pos = new Position(25*i, 25*j+66);
						ArrayList<Image> ghostImage = new ArrayList<Image>();
						ghostImage.add(new Image("/resource/nurse_0.png"));
						ghostImage.add(new Image("/resource/nurse_1.png"));
						Ghost ghost = new Ghost(pos, new Position(24,24), ghostImage, Ability.NURSE);
						ghosts.add(ghost);
					}
					//make wizard ghost
					else if (id == '3') {
						pos = new Position(25*i , 25*j+66);
						ArrayList<Image> ghostImage = new ArrayList<Image>();
						ghostImage.add(new Image("/resource/wizard_0.png"));
						ghostImage.add(new Image("/resource/wizard_1.png"));
						Ghost ghost = new Ghost(pos, new Position(24,24), ghostImage, Ability.WIZARD);
						ghosts.add(ghost);
					}
					//make ice ghost
					else if (id == '4') {
						pos = new Position(25*i, 25*j+66);
						ArrayList<Image> ghostImage = new ArrayList<Image>();
						ghostImage.add(new Image("/resource/iceman_0.png"));
						ghostImage.add(new Image("/resource/iceman_1.png"));
						Ghost ghost = new Ghost(pos, new Position(24,24), ghostImage, Ability.ICE);
						ghosts.add(ghost);
					}
					//make ninja ghost
					else if (id == '5') {
						pos = new Position(25*i, 25*j+66);
						ArrayList<Image> ghostImage = new ArrayList<Image>();
						ghostImage.add(new Image("/resource/ninja_0.png"));
						ghostImage.add(new Image("/resource/ninja_1.png"));
						Ghost ghost = new Ghost(pos, new Position(24,24), ghostImage, Ability.NINJA);
						ghosts.add(ghost);
					}
					//make pellets
					else if (id == '*') {
						pos = new Position(25*i + 10, 25*j+76);
						Pellet pallet = new Pellet(pos, new Position(5,5), new Image("/resource/pellet.png"), 10);
						pellets.add(pallet);
						
					}
					//make item
					else if (id == '#') {
						pos = new Position(25*i + 2.5, 25*j+66+2.5);
						Item item = new Item(pos, new Position(20,20), new Image("/resource/item.png"), 100);
						items.add(item);
					}
				}
				j++;
			}
			bufferedReader.close();
			background = new Object(new Position(0,66), new Image("/resource/Background_" + world + ".png"));
			top = new Object(new Position(0,0), new Image("/resource/test_InGameTop.png"));
			side = new Object(new Position(1150, 66), new Image("resource/test_InGameRightSide.png"));
			Image ghostDie = new Image("/resource/ghostDie.png");
			for (int i = 0; i < ghosts.size(); i++) { ghosts.get(i).addImage(ghostDie); }
			//show all objects
			objects.add(background);
			objects.add(side);
			objects.add(top);
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
