package main.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class GameModelHandler {
	private ArrayList<Object> objects;
	private ArrayList<Text> texts;
	public Start start = null;
	public SingleStageSelect singleStageSel = null;
	public SingleInGame singleInGame = null;
	
	
	@SuppressWarnings("serial")
	ArrayList<Integer> maxLevel = new ArrayList<Integer>() {
		{
			add(5);
			add(5);
			add(5);
			add(4);
		}
	};
	
	public void deleteStates() {
		start = null;
		singleStageSel = null;
		singleInGame = null;
		System.gc();
	}
	public ArrayList<Object> getObjects() { return objects; }
	private void addObject(Object obj) { objects.add(obj); }
	private void addObject(ArrayList<Object> obj) { objects.addAll(obj); }
	private void removeObject(Object obj) {{objects.remove(obj); obj = null;}}
	private void removeObject(ArrayList<Object> obj) {objects.removeAll(obj); obj.clear();}
	public ArrayList<Text> getTexts() { return texts; }
	private void addText(Text text) { texts.add(text); }
	private void addText(ArrayList<Text> text) { texts.addAll(text); }
	private void removeText(Text text) { texts.remove(text); text = null;}
	private void removeText(ArrayList<Text> text) {texts.removeAll(text); text.clear();}
	
	//init classes
	public void initStart() { start = new Start(); }
	public void initSingleStageSelect() { singleStageSel = new SingleStageSelect(); }
	public void initSingleInGame(int world, int stage) {singleInGame = new SingleInGame(world, stage);}
	
	public class SingleInGame {
		Object top;
		Object side;
		Level level;
		
		boolean inCountdown;
		boolean inPause;
		boolean gameWin;
		boolean gameFinish;
		double speed = 2.5;
		Random rand = new Random();

		PlayerInfo info;
		Text playerScore = null;
		Text count = null;
		Text gameCount = null;
		Text pause = null;
		Text gameFinishText = null;
		
		Object background;
		Object menu_button;
		Object retry_button;
		Object next_round_button;
		
		MovePressed movePressed = new MovePressed();
		
				
		
		public void update() {
			proAlive();
			if (!gameFinish) { 
				gameFinish();
			}
			protagonistMove();
			if(!gameFinish) {
				ghostAi();
			}
			isProCollideGhost();
			
		}
		
		public void ghostAi() {
			double range = 250 + level.getWorld()*50;
			double speed;
			double xDiff;
			double yDiff;
			for(int i = 0; i < level.getGhosts().size(); i ++) {
				if(level.getGhosts().get(i).getAbility() == Ability.RAINBOW_STAR || level.getGhosts().get(i).getAbility() == Ability.NINJA) {
				int count = 0;
				speed = 1 + level.getWorld()*0.5;
				xDiff = level.getPro().getPosition().getX() - level.getGhosts().get(i).getPosition().getX();
				yDiff = level.getPro().getPosition().getY() - level.getGhosts().get(i).getPosition().getY();
				if(xDiff == 0 && yDiff < 0) {
					Object block = new Object(new Position(level.getPro().getPosition().getX(),level.getPro().getSize().getY() + level.getPro().getPosition().getY()), 
							new Position (level.getPro().getSize().getX(),-yDiff - level.getPro().getSize().getY())); 
					for(int j = 0; j < level.getWalls().size(); j++ ) {
						if(!block.isCollideObject(level.getWalls().get(j))) {
							count++;
						}
					}
					if(count == level.getWalls().size()) {
						level.getGhosts().get(i).changeDirection(Direction.UP);
					}
				}
				else if(xDiff == 0 && yDiff > 0) {
					Object block = new Object(new Position(level.getPro().getPosition().getX(),level.getGhosts().get(i).getSize().getY() + level.getGhosts().get(i).getPosition().getY()), 
							new Position (level.getGhosts().get(i).getSize().getX(),yDiff - level.getGhosts().get(i).getSize().getY())); 
					for(int j = 0; j < level.getWalls().size(); j++ ) {
						if(!block.isCollideObject(level.getWalls().get(j))) {
							count++;
						}
					}
					if(count == level.getWalls().size()) {
						level.getGhosts().get(i).changeDirection(Direction.DOWN);
					}
				}
				else if(yDiff == 0 && xDiff < 0) {
					Object block = new Object(new Position(level.getPro().getPosition().getX() + level.getPro().getSize().getX(),level.getPro().getPosition().getY()), 
							new Position (-xDiff - level.getPro().getSize().getX(), level.getPro().getSize().getY())); 
					for(int j = 0; j < level.getWalls().size(); j++ ) {
						if(!block.isCollideObject(level.getWalls().get(j))) {
							count++;
						}
					}
					if(count == level.getWalls().size()) {
						level.getGhosts().get(i).changeDirection(Direction.LEFT);
					}
				}
				else if(yDiff == 0 && xDiff > 0) {
					Object block = new Object(new Position(level.getGhosts().get(i).getPosition().getX() + level.getGhosts().get(i).getSize().getX(),level.getPro().getPosition().getY()), 
							new Position (xDiff - level.getGhosts().get(i).getSize().getX(), level.getGhosts().get(i).getSize().getY())); 
					for(int j = 0; j < level.getWalls().size(); j++ ) {
						if(!block.isCollideObject(level.getWalls().get(j))) {
							count++;
						}
					}
					if(count == level.getWalls().size()) {
						level.getGhosts().get(i).changeDirection(Direction.RIGHT);
					}

				}
				ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
				}
				if(level.getGhosts().get(i).getAbility() == Ability.WIZARD || level.getGhosts().get(i).getAbility() == Ability.NURSE || level.getGhosts().get(i).getAbility() == Ability.ICE) {
					speed = 1 + level.getWorld()*0.25;
					xDiff = level.getPro().getPosition().getX() - level.getGhosts().get(i).getPosition().getX();
				yDiff = level.getPro().getPosition().getY() - level.getGhosts().get(i).getPosition().getY();
				if(xDiff > -range && xDiff < range && yDiff > -range && yDiff < range) {
					if(xDiff < 0 && xDiff > -range){
						level.getGhosts().get(i).changeDirection(Direction.LEFT);
						ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
					}
					else if(xDiff > 0 && xDiff < range){
						level.getGhosts().get(i).changeDirection(Direction.RIGHT);
						ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
					}
				
					
					if(yDiff < 0 && yDiff > -range){
						level.getGhosts().get(i).changeDirection(Direction.UP);
						ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
					}
					else if(yDiff > 0 && yDiff < range){
						level.getGhosts().get(i).changeDirection(Direction.DOWN);
						ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
					}
				}

				else
					ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
				
				}
			}

		}
		
		public Direction getRandomDirection() {
			int direction = rand.nextInt(4)+1;
			if(direction == 1) {
				return Direction.UP;
			}
			if(direction == 2) {
				return Direction.DOWN;
			}
			if(direction == 3) {
				return Direction.RIGHT;
			}
			else {
				return Direction.LEFT;
			}
		}
		
		
		
		public void ghostMove(int i, Direction direction, double speed) {
			
			if(direction == Direction.UP) {
				level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX(), level.getGhosts().get(i).getPosition().getY() - speed);
				for(int j = 0; j < level.getWalls().size(); j++ )
					if(level.getWalls().get(j).isCollideTop(level.getGhosts().get(i))) {
						level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX(), level.getGhosts().get(i).getPosition().getY() + speed);
						level.getGhosts().get(i).changeDirection(getRandomDirection());
					}
			}
			if(direction == Direction.DOWN) {
				level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX(), level.getGhosts().get(i).getPosition().getY() + speed);
				for(int j = 0; j < level.getWalls().size(); j++ )
					if(level.getWalls().get(j).isCollideBottom(level.getGhosts().get(i))) {
						level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX(), level.getGhosts().get(i).getPosition().getY() - speed);
						level.getGhosts().get(i).changeDirection(getRandomDirection());
					}
			}
			if(direction == Direction.RIGHT) {
				level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX() + speed, level.getGhosts().get(i).getPosition().getY());
				for(int j = 0; j < level.getWalls().size(); j++ )
					if(level.getWalls().get(j).isCollideRight(level.getGhosts().get(i))) {
						level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX() - speed, level.getGhosts().get(i).getPosition().getY());
						level.getGhosts().get(i).changeDirection(getRandomDirection());
					}
			}
			if(direction == Direction.LEFT) {
				level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX() - speed, level.getGhosts().get(i).getPosition().getY());
				for(int j = 0; j < level.getWalls().size(); j++ )
					if(level.getWalls().get(j).isCollideLeft(level.getGhosts().get(i))) {
						level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX() + speed, level.getGhosts().get(i).getPosition().getY());
						level.getGhosts().get(i).changeDirection(getRandomDirection());
					}
			}
		}
		
		public void gameFinish() {
			if(!level.getPro().alive) {
				gameWin = false;
				gameFinish = true;

				showFinish(gameWin);
			}
			else if(level.getPellets().isEmpty() && level.getItem().isEmpty()) {
				gameWin = true;
				gameFinish = true;

				showFinish(gameWin);
			} else if (gameCount.getString() == "0:00") {
				gameWin = false;
				gameFinish = true;

				showFinish(gameWin);
			}
		}
		
		public void showFinish(boolean win) {

			ArrayList<Object> buttons = new ArrayList<Object>();
			if (win) {
				System.out.println("Game Won");
				gameFinishText = new Text(new Position(450, 200), 250, "Game Won", 50, Color.BLACK);
				addText(gameFinishText);
				
				background = new Object(new Position (450, 300), new Position (250, 150), new Image("resource/test_LevelSelectWindow_" + (level.world + 1) +".png"));
				menu_button = new Object(new Position (600, 350), new Position (50,50), new Image("resource/test_ReturnButton.png"));
				next_round_button = new Object(new Position (500, 350), new Position (50,50), new Image("resource/test_Arrow.png"));

				buttons.add(background);
				buttons.add(menu_button);
				buttons.add(next_round_button);
			
				addObject(buttons);				
			} else {
				System.out.println("Game Lost");
				gameFinishText = new Text(new Position(450, 200), 250, "Game Lost", 50, Color.BLACK);
				addText(gameFinishText);
				
				background = new Object(new Position (450, 300), new Position (250, 150), new Image("resource/test_LevelSelectWindow_" + (level.world + 1) +".png"));
				menu_button = new Object(new Position (600, 350), new Position (50,50), new Image("resource/test_ReturnButton.png"));
				retry_button = new Object(new Position (500, 350), new Position (50,50), new Image("resource/test_BackButton.png"));
		
				buttons.add(background);
				buttons.add(menu_button);
				buttons.add(retry_button);
		
				addObject(buttons);
			}
			
		}
		
		public boolean getGameFinish() {
			return gameFinish;
		}
		
		public int nextRound(double x, double y) {
			
			if(gameWin) {
				if(menu_button.isInsideObject(x, y)) {
				return 1;
				}
				if(next_round_button.isInsideObject(x, y)) {
					return 3;
				}
				else
					return 0;
			}
			else {
				if(menu_button.isInsideObject(x, y)) {
					return 1;
					}
				if(retry_button.isInsideObject(x, y)) {
					return 2;
				}
				else return 0;
			}
				
		}
		
		public void initLevel() {
			singleInGame = new SingleInGame(level.getWorld(), level.getStage());
		}
		
		public void initNextLevel() {
			if((level.getStage()+1) < maxLevel.get(level.getWorld()))
				singleInGame = new SingleInGame(level.getWorld(), (level.getStage()+1));
			else
				singleInGame = new SingleInGame((level.getWorld() +1), 0);
		}
		
		public void proAlive() {
			if(level.getPro().getLife() <= 0)
				level.getPro().alive = false;
		}
		
		public void proDied() {
			if(!level.getPro().alive) {
				level.removePro();
			}
		}
		
		public void protagonistMove() {
			if (movePressed.getMovePressed(Direction.UP)) {
				level.getPro().setDirection(Direction.UP);
				moveUp();
			}
			if (movePressed.getMovePressed(Direction.DOWN)) {
				level.getPro().setDirection(Direction.DOWN);
				moveDown();
			}
			if (movePressed.getMovePressed(Direction.RIGHT)) {
				level.getPro().setDirection(Direction.RIGHT);
				moveRight();
			}
			if (movePressed.getMovePressed(Direction.LEFT)) {
				level.getPro().setDirection(Direction.LEFT);
				moveLeft();
			}
			checkPelletsAndItems();
		}
		
		public void pressMove(KeyCode code) {
			switch(code) {
			case UP:
				movePressed.setMovePressed(Direction.UP, true);
				break;
			case DOWN:
				movePressed.setMovePressed(Direction.DOWN, true);
				break;
			case LEFT:
				movePressed.setMovePressed(Direction.LEFT, true);
				break;
			case RIGHT:
				movePressed.setMovePressed(Direction.RIGHT, true);
				break;
			}
		}
		public void releaseMove(KeyCode code) {
			switch(code) {
			case UP:
				movePressed.setMovePressed(Direction.UP, false);
				break;
			case DOWN:
				movePressed.setMovePressed(Direction.DOWN, false);
				break;
			case LEFT:
				movePressed.setMovePressed(Direction.LEFT, false);
				break;
			case RIGHT:
				movePressed.setMovePressed(Direction.RIGHT, false);
				break;
			case P:
				movePressed.setMovePressed(Direction.ALL, false);
				break;
			}
		}

		public void isProCollideGhost() {
			for(int i = 0; i < level.getGhosts().size(); i++ ) {
				if(level.getGhosts().get(i).isCollideObject(level.getPro()) && !level.getPro().untouchable) {
					if(level.getPro().item) {
						level.getPro().setStoredAbility(level.getGhosts().get(i).getAbility()); 
						removeObject(level.getGhosts().get(i));
						level.removeGhosts(level.getGhosts().get(i));
						level.getPro().usableAbility = true;
						level.getPro().item = false;
						System.out.println("item consumed");
					}
					else {
						level.getPro().decreaseLife();
						removeObject(level.getPro());				
					}
				}
			}
		}
		
		public void useAbility() {
			Object newPosition = new Object(new Position (0,0), new Position(20,20));
			int count = 0;
			if(level.getPro().usableAbility) {
				Ability ability = level.getPro().getStoredAbility();
				if(ability == Ability.RAINBOW_STAR) {
					speed = speed * 2;
					level.getPro().usableAbility = false;
				}
				else if(ability == Ability.NURSE) {
					level.getPro().increaseLife();
					level.getPro().usableAbility = false;
				}
				else if(ability == Ability.WIZARD) {
					Direction direction = level.getPro().getDirection();
					
					if (direction == Direction.UP) {
						newPosition.setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY() - level.getWalls().get(0).getSize().getY()*2);
					}
					else if (direction == Direction.DOWN) {
						newPosition.setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY() + level.getWalls().get(0).getSize().getY()*2);
					}
					else if (direction == Direction.RIGHT) {
						newPosition.setPosition(level.getPro().getPosition().getX() + level.getWalls().get(0).getSize().getY()*2, level.getPro().getPosition().getY());
					}
					else if (direction == Direction.LEFT) {
						newPosition.setPosition(level.getPro().getPosition().getX() - level.getWalls().get(0).getSize().getY()*2, level.getPro().getPosition().getY());
					}
					for(int i = 0; i < level.getWalls().size(); i++ ) {
						if(!level.getWalls().get(i).isCollideObject(newPosition)) {
							count++;
						}
							
					}
					if (count == level.getWalls().size()) {
						level.getPro().setPosition(newPosition.getPosition().getX(), newPosition.getPosition().getY());
						level.getPro().usableAbility = false;
					}
				}
				else if(ability == Ability.ICE) {
					newPosition.setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY());
					Object ice = new Object(newPosition.getPosition(), new Position(20,20), new Image("/resource/ice.png"));
					level.objects.add(ice);
					level.getPro().usableAbility = false;
				}
				else if(ability == Ability.NINJA) {
					level.getPro().beUntouchable();
					level.getPro().usableAbility = false;
				}
				
			}
			
		}
		
		private void moveUp() {
			level.getPro().setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY() - speed);
			for(int i = 0; i < level.getWalls().size(); i++ )
				if(level.getWalls().get(i).isCollideTop(level.getPro()))
					level.getPro().setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY() + speed);
		}
		private void moveDown() {
			level.getPro().setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY() + speed);
			for(int i = 0; i < level.getWalls().size(); i++ )
				if(level.getWalls().get(i).isCollideBottom(level.getPro()))
					level.getPro().setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY() - speed);
		}
		private void moveRight() {
			level.getPro().setPosition(level.getPro().getPosition().getX() + speed, level.getPro().getPosition().getY());
			for(int i = 0; i < level.getWalls().size(); i++ )
				if(level.getWalls().get(i).isCollideRight(level.getPro()))
					level.getPro().setPosition(level.getPro().getPosition().getX() - speed, level.getPro().getPosition().getY());
		}
		private void moveLeft() {
			level.getPro().setPosition(level.getPro().getPosition().getX() - speed, level.getPro().getPosition().getY());
			for(int i = 0; i < level.getWalls().size(); i++ )
				if(level.getWalls().get(i).isCollideLeft(level.getPro()))
					level.getPro().setPosition(level.getPro().getPosition().getX() + speed, level.getPro().getPosition().getY());
		}
		private void checkPelletsAndItems() {
			for(int i = 0; i < level.getPellets().size(); i++ ) {
				if(level.getPro().isCollideLeft(level.getPellets().get(i))) {
					eatPellet(level.getPellets().get(i));
				}
			}

			for(int i = 0; i < level.getItem().size(); i++ ) {
				if(level.getPro().isCollideLeft(level.getItem().get(i))) {
					eatItem(level.getItem().get(i));
				}
			}
		}
		private void eatItem(Item item) {
			removeObject(item);	
			level.getPro().item = true;
			info.addScore(item);
			playerScore.setString(info.getScore());
			level.removeItems(item);
		}
		private void eatPellet(Pellet pellet) {
			removeObject(pellet);
			info.addScore(pellet);
			playerScore.setString(info.getScore());
			level.removePellets(pellet);
		}
		
		public class MovePressed {
			boolean up = false;
			boolean down = false;
			boolean left = false;
			boolean right = false;
			
			public boolean getMovePressed(Direction d) {
				switch(d) {
				case UP:
					return up;
				case DOWN:
					return down;
				case LEFT:
					return left;
				case RIGHT:
					return right;
				default:
					return false;
				}
			}
			
			public void setMovePressed(Direction d, boolean b) {
				switch(d) {
				case UP: 
					up = b;
					break;
				case DOWN: 
					down = b;
					break;
				case LEFT:
					left = b;
					break;
				case RIGHT:
					right = b;
					break;
				case ALL:
					up = b;
					down = b;
					left = b;
					right = b;
					break;
				}
			}
		}
		public boolean getCountdownFlag() {
			return inCountdown;
		}
		public boolean getPauseFlag() {
			return inPause;
		}
		public void setTimerTo_0() {
			gameCount.setString("0:00");
		}
		public void setPause(boolean b) {
			if (b) {
				if (count != null) removeText(count);
				pause = new Text(new Position(575, 325), 200, "Paused", 24, Color.BLACK);
				addText(pause);
				inPause = b;
			} else {
				if (count != null) addText(count);
				removeText(pause);
				inPause = b;
			}
		}
		
		public SingleInGame(int world, int stage) {
			objects.clear();
			texts.clear();

			top = new Object(new Position(0,0), new Image("/resource/test_InGameTop.png"));
			addObject(top);
			side = new Object(new Position(1150, 70), new Image("resource/test_InGameRightSide.png"));
			addObject(side);
			level = new Level(world + 1, stage + 1);
			addObject(level.getObjectList());
			
			info = new PlayerInfo();
			playerScore = new Text(new Position(1190, 425), 80, info.getScore(), 24, Color.BLACK);
			addText(playerScore);
			count = new Text(new Position(575, 325), 99, 3, 24, Color.BLACK);
			addText(count);
			gameCount = new Text(new Position(1190, 325), 80, "2:00", 24, Color.BLACK);
			addText(gameCount);
			inCountdown = true;
			inPause = false;
			Timer timer = new Timer();
			TimerTask gameTimerTask = new TimerTask() {
				@Override
				public void run() {
					String timerString[] = gameCount.getString().split(":");
					int min = Integer.valueOf(timerString[0]);
					int sec = Integer.valueOf(timerString[1]);
					if (!inPause) {
						if (gameFinish) {
							this.cancel();
						}
						else if (min == 0 && sec == 0) {
							gameFinish();
							this.cancel();
						} else if (sec == 0 && min != 0) {
								min--;
								sec = 59;
						} else {
							sec--;
						}
						String minStr = String.valueOf(min);
						String secStr = String.valueOf(sec);
						if (secStr.length() == 1) {
							secStr = 0 + secStr;
						}
						
						gameCount.setString(minStr + ":" + secStr);
					}
				}
			};
			TimerTask counter = new TimerTask() {
				@Override
				public void run() {
					System.out.println(count.getString());
					if (count.getString().equals("1")) {
						System.out.println("timertask canceled!");
						count.setString("Start!");
						inCountdown = false;
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								removeText(count);
								timer.scheduleAtFixedRate(gameTimerTask, 0l, 1000l);
							}
						}, 1000l);
						this.cancel();
					} else {
						if (!inPause)
							count.setString(Integer.valueOf(count.getString()) - 1);
					}
				}
			};
			timer.scheduleAtFixedRate(counter, 1000l, 1000l);
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

		Object pointer = null;
		
		boolean sel = false;
		int world = 0;
		int stage = 0;
		
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
			for (int i = 0; i < maxLevel.get(world); i++) {
				Object stage = new Object(
						new Position(window.getPosition().getX() + (window.getSize().getX() - maxLevel.get(world) * 120) / (maxLevel.get(world)+1) * (i+1) + 120*i,
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
				if (stage >= maxLevel.get(world) - 1);
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
			backButton = new Object(new Position(20,20), new Image("/resource/BackButton.png"));
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
