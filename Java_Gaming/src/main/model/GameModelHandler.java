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
		// TODO Auto-generated method stub
		singleStageSel = new SingleStageSelect();
	}
	
	private void addObject(Object obj) {
		objects.add(obj);
	}
	public GameModelHandler() {
		objects = new ArrayList<Object>();
	}
	
	public class SingleStageSelect {
		public SingleStageSelect() {
			objects.clear();
		}
	}
	
	public class Start {
		Object title;
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
			title = new Object(new Position(506, 100));
			title.addImage(new Image("/resource/test_Title.png"));
			addObject(title);
			option_1 = new Object(new Position(493, 425));
			option_1.addImage(new Image("/resource/test_SinglePlayer.png"));
			addObject(option_1);
			option_2 = new Object(new Position(435, 500));
			option_2.addImage(new Image("/resource/test_MultiPlayer.png"));
			addObject(option_2);
			option_3 = new Object (new Position(572, 575));
			option_3.addImage(new Image("/resource/test_Credit.png"));
			addObject(option_3);
			arrow = new Object(new Position(425, 430));
			arrow.addImage(new Image("/resource/test_Arrow.png"));
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
