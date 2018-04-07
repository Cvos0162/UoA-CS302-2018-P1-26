package main.model;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameModelHandler {
	private ArrayList<Object> objects;
	private ArrayList<Text> texts;
	public Start start = null;
	public SingleStageSelect singleStageSel = null;
	public SingleInGame singleInGame = null;
	
	public void deleteStates() {
		start = null;
		singleStageSel = null;
		singleInGame = null;
		System.gc();
	}
	public ArrayList<Object> getObjects() { return objects; }
	private void addObject(Object obj) { objects.add(obj); }
	private void addObject(ArrayList<Object> obj) { objects.addAll(obj); }
	private void removeObject(Object obj) { objects.remove(obj); }
	private void removeObject(ArrayList<Object> obj) {objects.removeAll(obj);}
	public ArrayList<Text> getTexts() { return texts; }
	private void addText(Text text) { texts.add(text); }
	private void addText(ArrayList<Text> text) { texts.addAll(text); }
	private void removeText(Text text) { texts.remove(text); }
	private void removeText(ArrayList<Text> text) {texts.removeAll(text);}
	
	//init classes
	public void initStart() { start = new Start(); }
	public void initSingleStageSelect() { singleStageSel = new SingleStageSelect(); }
	public void initSingleInGame(int world, int stage) {singleInGame = new SingleInGame(world, stage);}
	
	public class SingleInGame {
		Object top;
		Object side;
		Level level;
		public SingleInGame(int world, int stage) {
			objects.clear();
			texts.clear();

			top = new Object(new Position(0,0), new Image("/resource/test_InGameTop.png"));
			addObject(top);
			side = new Object(new Position(1150, 70), new Image("resource/test_InGameRightSide.png"));
			addObject(side);
			level = new Level(world + 1, stage + 1);
			addObject(level.getObjectList());
			
		}
		
		public void moveUp() {
			level.getPro().setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY() - 5);
			for(int i = 0; i < level.getWalls().size(); i++ )
				if(level.getWalls().get(i).isCollideTop(level.getPro()))
					level.getPro().setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY() + 5);
			
		}
		public void moveDown() {
			level.getPro().setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY() + 5);
			for(int i = 0; i < level.getWalls().size(); i++ )
				if(level.getWalls().get(i).isCollideBottom(level.getPro()))
					level.getPro().setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY() - 5);
		}
		public void moveRight() {
			level.getPro().setPosition(level.getPro().getPosition().getX() + 5, level.getPro().getPosition().getY());
			for(int i = 0; i < level.getWalls().size(); i++ )
				if(level.getWalls().get(i).isCollideRight(level.getPro()))
					level.getPro().setPosition(level.getPro().getPosition().getX() - 5, level.getPro().getPosition().getY());
		}
		public void moveLeft() {
			level.getPro().setPosition(level.getPro().getPosition().getX() - 5, level.getPro().getPosition().getY());
			for(int i = 0; i < level.getWalls().size(); i++ )
				if(level.getWalls().get(i).isCollideLeft(level.getPro()))
					level.getPro().setPosition(level.getPro().getPosition().getX() + 5, level.getPro().getPosition().getY());
		
			
		}	
		public void useAbility() {
			
		}
	
		
	
	
	
	}
	
	
	
	
	public class SingleStageSelect {
		Object world_1;
		Object world_2;
		Object world_3;
		Object world_4;
		Object backButton;
		
		Text stageSel;
		
		Object window;
		Object unselectButton;
		ArrayList<Object> stage_buttons;
		ArrayList<Text> stage_texts;

		Object pointer;
		
		boolean sel = false;
		int world = 0;
		int stage = 0;
		
		@SuppressWarnings("serial")
		ArrayList<Integer> level = new ArrayList<Integer>() {
			{
				add(5);
				add(5);
				add(5);
				add(4);
			}
		};
		@SuppressWarnings("serial")
		ArrayList<Image> windowImages = new ArrayList<Image>() {
			{
				add(new Image("/resource/test_LevelSelectWindow_1.png"));
				add(new Image("/resource/test_LevelSelectWindow_2.png"));
				add(new Image("/resource/test_LevelSelectWindow_3.png"));
				add(new Image("/resource/test_LevelSelectWindow_4.png"));
			}
		};
		@SuppressWarnings("serial")
		ArrayList<Image> stageImages = new ArrayList<Image>() {
			{
				add(new Image("/resource/test_StageButton_1.png"));
				add(new Image("/resource/test_StageButton_2.png"));
				add(new Image("/resource/test_StageButton_3.png"));
				add(new Image("/resource/test_StageButton_4.png"));
			}
		};
		
		public boolean getSel() {
			return sel;
		}
		public int getWorld() {
			return world;
		}
		public int getStage() {
			return stage;
		}
		public void setWorld(int n) {
			world = n;
		}
		public void setStage(int n) {
			stage = n;
		}
		public void showSelected() {
			removeObject(pointer);
			pointer = null;
			Position pos;
			Position size;
			if (sel){
				pos = new Position(stage_buttons.get(stage).getPosition().getX() - 20, stage_buttons.get(stage).getPosition().getY() + stage_buttons.get(stage).getSize().getY()/2);
				size = new Position(stage_buttons.get(stage).getSize().getX() + 40, stage_buttons.get(stage).getSize().getY()/2 + 40
						
						);
				pointer = new Object(pos, size, new Image("/resource/test_HighLightStage.png"));
				objects.add(objects.indexOf(stage_buttons.get(0)), pointer);
			}
			else {
				switch (world) {
				case 0:
					pos = new Position(world_1.getPosition().getX()-20, world_1.getPosition().getY()-20);
					size = new Position(world_1.getSize().getX() + 40, world_1.getSize().getY() + 40);
					break;
				case 1:
					pos = new Position(world_2.getPosition().getX()-20, world_2.getPosition().getY()-20);
					size = new Position(world_2.getSize().getX() + 40, world_2.getSize().getY() + 40);
					break;
				case 2:
					pos = new Position(world_3.getPosition().getX()-20, world_3.getPosition().getY()-20);
					size = new Position(world_3.getSize().getX() + 40, world_3.getSize().getY() + 40);
					break;
				case 3:
					pos = new Position(world_4.getPosition().getX()-20, world_4.getPosition().getY()-20);
					size = new Position(world_4.getSize().getX() + 40, world_4.getSize().getY() + 40);
					break;
				default:
					pos = new Position(0,0);
					size = new Position(0,0);
					break;				
				}
				pointer = new Object(pos, size, new Image("/resource/test_HighLightWorld.png"));
				objects.add(objects.indexOf(world_1), pointer);
			}
			
			
		}
		
		public void showStages() {
			stage = 0;
			window = new Object(new Position(240, 180), windowImages);
			window.setIterator(world);
			addObject(window);
			unselectButton = new Object(new Position(990, 200), new Image("/resource/test_ReturnButton.png"));
			addObject(unselectButton);
			stage_buttons = new ArrayList<Object>();
			for (int i = 0; i < level.get(world); i++) {
				Object stage = new Object(
						new Position(window.getPosition().getX() + (window.getSize().getX() - level.get(world) * 120) / (level.get(world)+1) * (i+1) + 120*i,
								window.getPosition().getY() + window.getSize().getY()/2 - 60),
						stageImages
						);
				stage.setIterator(world);
				stage_buttons.add(stage);
			}
			addObject(stage_buttons);
			stage_texts = new ArrayList<Text>();
			for (int i = 0; i < stage_buttons.size(); i++) {
				Text text = new Text(
						new Position(stage_buttons.get(i).getPosition().getX() + stage_buttons.get(i).getSize().getX()/2 - 15,
								stage_buttons.get(i).getPosition().getY() + stage_buttons.get(i).getSize().getY()/2 + 10),
						stage_buttons.get(i).getSize().getX(),
						i+1,
						50,
						Color.BLACK
						);
				stage_texts.add(text);
			}
			addText(stage_texts);
			sel = true;
		}
		public void hideStages() {
			removeObject(window);
			removeObject(unselectButton);
			removeObject(stage_buttons);
			removeText(stage_texts);
			sel = false;
		}
		public void moveMouse(double x, double y) {
			if (sel) {
				for (int i = 0; i < stage_buttons.size(); i++) {
					if (stage_buttons.get(i).isInsideObject(x, y)) stage = i;
				}
			} else {
				if (world_1.isInsideObject(x, y)) world = 0;
				else if (world_2.isInsideObject(x, y)) world = 1;
				else if (world_3.isInsideObject(x, y)) world = 2;
				else if (world_4.isInsideObject(x, y)) world = 3;
			}
		}
		public int selectMouse_world(double x, double y) {
			if (world_1.isInsideObject(x, y)) return 1;
			else if (world_2.isInsideObject(x, y)) return 2;
			else if (world_3.isInsideObject(x, y)) return 3;
			else if (world_4.isInsideObject(x, y)) return 4;
			else if (backButton.isInsideObject(x, y)) return 5;
			else return 0;
		}
		public int selectMouse_stage(double x, double y) {
			for (int i = 0; i < stage_buttons.size(); i++)
				if (stage_buttons.get(i).isInsideObject(x, y)) return i;
			if (backButton.isInsideObject(x, y)) return -2;
			else if (unselectButton.isInsideObject(x, y)) return -3;
			else return -1;
		}
		public void selectUp() {
			if (sel) {
				if (stage >= level.get(world) - 1);
				else stage++;
			}
			else {
				if (world >= 3);
				else world++;
			}
		}
		public void selectdown() {
			if (sel) {
				if (stage <= 0);
				else stage--;
			}
			else {
				if (world <= 0);
				else world--;
			}
		}
		
		
		public void update() {
			showSelected();
		}
		
		public SingleStageSelect() {
			objects.clear();
			texts.clear();
			world_1 = new Object(new Position(85, 150), new Image("/resource/World_1.png"));
			addObject(world_1);
			world_2 = new Object(new Position(515, 40), new Image("/resource/World_2.png"));
			addObject(world_2);
			world_3 = new Object(new Position(900, 25), new Image("/resource/World_3.png"));
			addObject(world_3);
			world_4 = new Object(new Position(420, 390), new Image("/resource/World_4.png"));
			addObject(world_4);
			backButton = new Object(new Position(20,20), new Image("/resource/test_BackButton.png"));
			addObject(backButton);
			stageSel = new Text(
					new Position(125,60),
					300,
					"Select Level :",
					50,
					Color.WHITE
					);
			addText(stageSel);
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
			texts.clear();

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
		public void moveMouse(double x, double y) {
			if (option_1.isInsideObject(x, y)) sel = 1;
			else if (option_2.isInsideObject(x, y)) sel = 2;
			else if (option_3.isInsideObject(x, y)) sel = 3;
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
	
	public GameModelHandler() {
		objects = new ArrayList<Object>();
		texts = new ArrayList<Text>();
	}
}
