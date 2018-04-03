package main.model;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.image.Image;

public class GameModelHandler {
	private ArrayList<Object> objects;
	public Start start = null;
	public SingleStageSelect singleStageSel = null;
	
	
	
	public ArrayList<Object> getObjects() {
		return objects;
	}
	
	public void initStart() {
		start = new Start();
	}
	public void initSingleStageSelect() {
		singleStageSel = new SingleStageSelect();
	}
	
	private void addObject(Object obj) {
		objects.add(obj);
	}
	public GameModelHandler() {
		objects = new ArrayList<Object>();
	}
	public class SingleStageSelect {
		Object world_1;
		Object world_2;
		Object world_3;
		Object world_4;
		Object backButton;
		int sel = 1;
		
		int world;
		public void showStages() {
			
		}
		public void loadStages() {
			
		}
		
		public SingleStageSelect() {
			objects.clear();
			world_1 = new Object(new Position(245, 300), new Image("/resource/test_World1.png"));
			addObject(world_1);
			world_2 = new Object(new Position(565, 100), new Image("/resource/test_World2.png"));
			addObject(world_2);
			world_3 = new Object(new Position(885, 285), new Image("/resource/test_World3.png"));
			addObject(world_3);
			world_4 = new Object(new Position(565, 500), new Image("/resource/test_World4.png"));
			addObject(world_4);
			backButton = new Object(new Position(20,20), new Image("/resource/test_BackButton.png"));
			addObject(backButton);
			
			
		}
		public void setWorld(int n) {
			world = n;
		}
		
		public int selectMouse(double x, double y) {
			if (world_1.isInsideObject(x, y)) return 1;
			else if (world_2.isInsideObject(x, y)) return 2;
			else if (world_3.isInsideObject(x, y)) return 3;
			else if (world_3.isInsideObject(x, y)) return 4;
			else if (backButton.isInsideObject(x, y)) return 5;
			else return 0;
		}
	}
	
	public class Start {
		Object option_1;
		Object option_2;
		Object option_3;
		Object arrow;
		int sel;
		
		public int getSelected() {
			return sel;
		}
		
		public Start() {
			objects.clear();
			
			addObject(new Object(new Position(390, 100), new Image("/resource/test_Planet.png")));
			addObject(new Object(new Position(520, 210), new Image("/resource/test_Escape.png")));
			option_1 = new Object(new Position(493, 425), new Image("/resource/test_SinglePlayer.png"));
			addObject(option_1);
			option_2 = new Object(new Position(435, 500), new Image("/resource/test_MultiPlayer.png"));
			addObject(option_2);
			option_3 = new Object (new Position(572, 575), new Image("/resource/test_Credit.png"));
			addObject(option_3);
			arrow = new Object(new Position(425, 430), new Image("/resource/test_Arrow.png"));
			addObject(arrow);
			sel = 1;
		}
		public void selectDown() {
			if (sel == 1) {
				sel = 2;
			} else if (sel == 2) {
				sel = 3;
			} else if (sel == 3) {
				sel = 1;
			}
		}
		public void selectUp() {
			if (sel == 1) {
				sel = 3;
			} else if (sel == 2) {
				sel = 1;
			} else if (sel == 3) {
				sel = 2;
			}
		}
		public int selectMouse(double x, double y) {
			if (option_1.isInsideObject(x, y)) return 1;
			else if (option_2.isInsideObject(x, y)) return 2;
			else if (option_3.isInsideObject(x, y)) return 3;
			else return 0;
		}
		
		public void update() {
			if (sel == 1) {
				arrow.setPosition(425, 430);
			} else if (sel == 2) {
				arrow.setPosition(435 - 68, 505);
			} else if (sel == 3) {
				arrow.setPosition(572 - 68, 580);
			}
		}
	}

}
