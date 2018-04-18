package main.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class GameModelHandler {
	Ability ghostTeam;
	
	//Rendering List
	private ArrayList<Object> objects;
	private ArrayList<Text> texts;
	
	//Structure Class
	public Start start = null;
	public SingleStageSelect singleStageSel = null;
	public SingleInGame singleInGame = null;
	public MultiSelect multiSelect = null;
	public MultiInGame multiInGame = null;
	public Story story = null;
	
	//maximum level stages and world can have
	@SuppressWarnings("serial")
	ArrayList<Integer> maxLevel = new ArrayList<Integer>() {
		{
			add(5);
			add(5);
			add(5);
			add(5);
		}
	};
	
	//garbage collector
	public void deleteStates() {
		start = null;
		singleStageSel = null;
		singleInGame = null;
		story = null;
		multiSelect = null;
		multiInGame = null;
		System.gc();
	}
	//getter adder remover of rendering list
	public ArrayList<Object> getObjects() {
		return objects; 
	}
	private void addObject(Object obj) { 
		objects.add(obj);
	}
	private void addObject(ArrayList<Object> obj) { 
		objects.addAll(obj); 
	}
	private void removeObject(Object obj) {
		objects.remove(obj); obj = null;
	}
	private void removeObject(ArrayList<Object> obj) {
		objects.removeAll(obj); obj.clear();
	}
	public ArrayList<Text> getTexts() { 
		return texts; 
	}
	private void addText(Text text) { 
		texts.add(text); 
	}
	private void addText(ArrayList<Text> text) { 
		texts.addAll(text); 
	}
	private void removeText(Text text) { 
		texts.remove(text); text = null;
	}
	private void removeText(ArrayList<Text> text) {
		texts.removeAll(text); text.clear();
	}
	
	//init classes
	public void initStart() { 
		start = new Start(); 
	}
	
	public void initSingleStageSelect() { 
		singleStageSel = new SingleStageSelect(); 
	}
	
	public void initSingleInGame(int world, int stage) { 
		singleInGame = new SingleInGame(world, stage); 
	}
	public void initMultiSelect() { 
		multiSelect = new MultiSelect(); 
	}
	public void initMultiInGame(int world, int stage) { 
		multiInGame = new MultiInGame(world, stage); 
	}
	public void initStory() { 
		story = new Story();
	}
	
	// class and function during story scene
	public class Story {
		
		//call Mediaplayer
		MediaPlayer player;
		
		//set selection integer
		int sel;
		
		//get selection
		public int getSel() {
			return sel;
		}
		
		//function that is showing next scene
		public void showNextScene() {
			
			//clear objects and texts
			objects.clear();
			texts.clear();
			//change selection to go over next scene
			sel++;
			//call scene
			addObject(new Object(new Position(0,0), new Image("/resource/storyScene_" + sel + ".png")));
			//make text box
			Object textBox = new Object(new Position(0, 495), new Position(1280,225), new Image("/resource/storyTextBox.png"));
			addObject(textBox);
			//helping text
			Text key = new Text(new Position(950, 680), 330, "Press any key or mouse for next.", 24, Color.WHITE);
			addText(key);
			switch (sel) {
			//first scene of story
			case 1:
				addText(new Text(new Position(30,540), 1200, "Hero from Magic Planet", 28, Color.WHITE));
				addText(new Text(new Position(30,580), 1200, "On a journey to find the super star", 28, Color.WHITE));
				addText(new Text(new Position(30,620), 1200, "To save the world.", 28, Color.WHITE));
				player = new MediaPlayer(new Media(new File("./src/resource/select.wav").toURI().toString()));
				break;
			//second scene of story
			case 2:
				addText(new Text(new Position(30,580), 1200, "BOOM!!!", 56, Color.WHITE));
				player = new MediaPlayer(new Media(new File("./src/resource/explosion.wav").toURI().toString()));
				break;
			//third scene of story
			case 3:
				addText(new Text(new Position(30,540), 1200, "The Ghosts! The Space Pirates appeared!", 28, Color.WHITE));
				player = new MediaPlayer(new Media(new File("./src/resource/chased.wav").toURI().toString()));
				break;
			//fourth scene of story
			case 4:
				removeObject(textBox);
				player.stop();
				player = new MediaPlayer(new Media(new File("./src/resource/die.wav").toURI().toString()));
				break;
			//last scene of story
			case 5:
				addText(new Text(new Position(30,540), 1200, "How is he going to escape?", 28, Color.WHITE));
				addText(new Text(new Position(30,580), 1200, "We need to collect moon dust and stardust!", 28, Color.WHITE));
				player = new MediaPlayer(new Media(new File("./src/resource/gameOver.wav").toURI().toString()));
				break;
			};
			player.play();
		}
		
		//when story function is called, all objects and texts are cleared
		public Story() {
			objects.clear();
			texts.clear();
			sel = 0;
			showNextScene();
		}
	}
	
	//class of when player play multi-play game
	public class MultiInGame {
		
		//initiate classes 		
		MediaPlayer mediaPlayer;
		Media background_sound;
		MediaPlayer backPlayer;
		Level level;
		PlayerInfo info;
		Random rand = new Random();
		MovePressed movePressed = new MovePressed();
		
		//initiate flags
		boolean iceAppear;
		boolean inCountdown;
		boolean inPause;
		boolean dying = false;
		boolean gameWin;
		boolean gameFinish;
		
		//declare protagonist speed and initiate playerGhost
		double speed = 2.5;
		int playerGhost;

		//initiate texts
		Text ability;
		Text playerScore = null;
		Text count = null;
		Text gameCount = null;
		Text pause = null;
		Text gameFinishText = null;
		Text itemTime = new Text(new Position(1240,650), 80, 5, 24, Color.BLACK);
	
		//initiate array lists
		ArrayList<Object> icedGhost = new ArrayList<Object>();
		ArrayList<Object> lifes;
		
		//initiate objects		
		Object highlight;
		Object background;
		Object menu_button;
		Object retry_button;
		Object next_round_button;
		Object ice;
		
		//function that updating game
		public void update() {
			//if protagonist died, nothing is going to work
			if (!dying) {
				if (gameFinish || dying) { 
					//declare protagonist's direction of moving
					movePressed.setMovePressed(Direction.ALL, false);
				}
				//find ghost that second player want to play
				findPlayerGhost();
				//check whether protagonist is alive or not
				proAlive();
				//make protagonist move
				protagonistMove();
				//ghost move
				ghostMove();
				if(!gameFinish) {
					//game finish window
					gameFinish();
					//declare ghosts direction of moving
					ghostAi();
					if(iceAppear)
						//if ghosts collide to ice, they stop moving
						isGhostCollideIce();
				}
				//if protagonist collide ghost, life is decreased
				isProCollideGhost();

			}
		}
		
		//declare direction of ghosts that is not controlled by player
		public void ghostAi() {
			
			//declare variables
			double range = 300;
			double speed;
			double xDiff;
			double yDiff;
			for(int i = 0; i < level.getGhosts().size(); i ++) {
				if(level.getGhosts().get(i).alive && !level.getGhosts().get(i).freeze && i != playerGhost) {
					//if ghosts ability is rainbow_star or ninja
					if(level.getGhosts().get(i).getAbility() == Ability.RAINBOW_STAR || level.getGhosts().get(i).getAbility() == Ability.NINJA) {
						int count = 0;
						speed = 2;
						//get difference in x and y axis
						xDiff = level.getPro().getPosition().getX() - level.getGhosts().get(i).getPosition().getX();
						yDiff = level.getPro().getPosition().getY() - level.getGhosts().get(i).getPosition().getY();
							//if there is no walls between protagonist and ghosts, ghosts go forward to protagonist
							if(xDiff == 0 && yDiff < 0 && !level.getPro().untouchable) {
								Object block = new Object(new Position(level.getPro().getPosition().getX(),level.getPro().getSize().getY() + level.getPro().getPosition().getY()), 
								new Position (level.getPro().getSize().getX(),-yDiff - level.getPro().getSize().getY())); 
								for(int j = 0; j < level.getWalls().size(); j++ ) {
									if(!block.isCollideObject(level.getWalls().get(j))) {
										count++;
									}
								}
								if(count == level.getWalls().size()) {
									level.getGhosts().get(i).setDirection(Direction.UP);
								}
							}
							else if(xDiff == 0 && yDiff > 0 && !level.getPro().untouchable) {
								Object block = new Object(new Position(level.getPro().getPosition().getX(),level.getGhosts().get(i).getSize().getY() + level.getGhosts().get(i).getPosition().getY()), 
										new Position (level.getGhosts().get(i).getSize().getX(),yDiff - level.getGhosts().get(i).getSize().getY())); 
								for(int j = 0; j < level.getWalls().size(); j++ ) {
									if(!block.isCollideObject(level.getWalls().get(j))) {
										count++;
									}
								}
								if(count == level.getWalls().size()) {
									level.getGhosts().get(i).setDirection(Direction.DOWN);
								}
							}
							else if(yDiff == 0 && xDiff < 0 && !level.getPro().untouchable) {
								Object block = new Object(new Position(level.getPro().getPosition().getX() + level.getPro().getSize().getX(),level.getPro().getPosition().getY()), 
										new Position (-xDiff - level.getPro().getSize().getX(), level.getPro().getSize().getY())); 
								for(int j = 0; j < level.getWalls().size(); j++ ) {
									if(!block.isCollideObject(level.getWalls().get(j))) {
										count++;
									}
								}
								if(count == level.getWalls().size()) {
									level.getGhosts().get(i).setDirection(Direction.LEFT);
								}
							}
							else if(yDiff == 0 && xDiff > 0 && !level.getPro().untouchable) {
								Object block = new Object(new Position(level.getGhosts().get(i).getPosition().getX() + level.getGhosts().get(i).getSize().getX(),level.getPro().getPosition().getY()), 
										new Position (xDiff - level.getGhosts().get(i).getSize().getX(), level.getGhosts().get(i).getSize().getY())); 
								for(int j = 0; j < level.getWalls().size(); j++ ) {
									if(!block.isCollideObject(level.getWalls().get(j))) {
										count++;
									}
								}
								if(count == level.getWalls().size()) {
									level.getGhosts().get(i).setDirection(Direction.RIGHT);
								}
							}
							ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
					}
					//if ghost ability is nurse
					else if (level.getGhosts().get(i).getAbility() == Ability.NURSE) {
						//declare ghost speed
						speed = 1.25;
						//find difference in x and y axis
						xDiff = level.getPro().getPosition().getX() - level.getGhosts().get(i).getPosition().getX();
						yDiff = level.getPro().getPosition().getY() - level.getGhosts().get(i).getPosition().getY();
						//if protagonist is in range of ghost detecting, this ghost is going to protagonist
						if((xDiff > -100 && xDiff < 100 && yDiff > -100 && yDiff < 100) && (yDiff == 0 || xDiff == 0 && !level.getPro().untouchable)) {
							if(xDiff < 0 && xDiff > -100 && yDiff == 0){
								level.getGhosts().get(i).setDirection(Direction.RIGHT);
								ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
							}
							else if(xDiff > 0 && xDiff < 100 && yDiff == 0){
								level.getGhosts().get(i).setDirection(Direction.LEFT);
								ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
							}	
							if(yDiff < 0 && yDiff > -100 && xDiff == 0){
								level.getGhosts().get(i).setDirection(Direction.DOWN);
								ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
							}
							else if(yDiff > 0 && yDiff < 100 && xDiff == 0){
								level.getGhosts().get(i).setDirection(Direction.UP);
								ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
							}
						}
						else if(xDiff > -range && xDiff < range && yDiff > -range && yDiff < range && !level.getPro().untouchable) {
							if(xDiff < 0 && xDiff > -range){
								level.getGhosts().get(i).setDirection(Direction.LEFT);
								ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
							}
							else if(xDiff > 0 && xDiff < range){
								level.getGhosts().get(i).setDirection(Direction.RIGHT);
								ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
							}								
							if(yDiff < 0 && yDiff > -range){
								level.getGhosts().get(i).setDirection(Direction.UP);
								ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
							}
							else if(yDiff > 0 && yDiff < range){
								level.getGhosts().get(i).setDirection(Direction.DOWN);
								ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
							}
						}
						else {
							ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
						}
					}
					else if(level.getGhosts().get(i).getAbility() == Ability.WIZARD || level.getGhosts().get(i).getAbility() == Ability.ICE) {
						speed = 1.25;
						boolean moved = false;
						for (int j = 0; j < level.getGhosts().size(); j++) {
							if (i != j && !moved) {
								speed = 1 + level.getWorld()*0.25;
								double ghrange = 50;
								double ghxDiff = level.getGhosts().get(j).getPosition().getX() - level.getGhosts().get(i).getPosition().getX();
								double ghyDiff = level.getGhosts().get(j).getPosition().getY() - level.getGhosts().get(i).getPosition().getY();
								//if protagonist is in range of ghost detecting, this ghost is going forward to protagonist
								if(ghxDiff > -ghrange && ghxDiff < ghrange && ghyDiff > -ghrange && ghyDiff < ghrange && !level.getPro().untouchable) {
									if(ghxDiff < 0 && ghxDiff > -ghrange){
										level.getGhosts().get(i).setDirection(Direction.RIGHT);
										ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
										moved = true;
									}
									else if(ghxDiff > 0 && ghxDiff < ghrange){
										level.getGhosts().get(i).setDirection(Direction.LEFT);
										ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
										moved = true;
									}
									if(ghyDiff < 0 && ghyDiff > -ghrange){
										level.getGhosts().get(i).setDirection(Direction.DOWN);
										ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
										moved = true;
									}
									else if(ghyDiff > 0 && ghyDiff < ghrange){
										level.getGhosts().get(i).setDirection(Direction.UP);
										ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
										moved = true;
									}
								}
							}	
						}
						if (!moved) {
							xDiff = level.getPro().getPosition().getX() - level.getGhosts().get(i).getPosition().getX();
							yDiff = level.getPro().getPosition().getY() - level.getGhosts().get(i).getPosition().getY();
							//if protagonist is in range of ghost detecting, this ghost is going forward to protagonist
							if(xDiff > -range && xDiff < range && yDiff > -range && yDiff < range && !level.getPro().untouchable) {
								if(xDiff < 0 && xDiff > -range){
									level.getGhosts().get(i).setDirection(Direction.LEFT);
									ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
								}
								else if(xDiff > 0 && xDiff < range){
									level.getGhosts().get(i).setDirection(Direction.RIGHT);
									ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
								}	
								if(yDiff < 0 && yDiff > -range){
									level.getGhosts().get(i).setDirection(Direction.UP);
									ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
								}
								else if(yDiff > 0 && yDiff < range){
									level.getGhosts().get(i).setDirection(Direction.DOWN);
									ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
								}
							}
							else
								//ghost just going to same as previous direction
								ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
						}
					}
				}
			}

		}
		
		//get random direction
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
		
		
		//make ghost that is not controlled by player move
		public void ghostMove(int i, Direction direction, double speed) {
			if(direction == Direction.UP) {
				//ghost move up
				level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX(), level.getGhosts().get(i).getPosition().getY() - speed);
				//if ghosts collide to wall change their direction and set their position to previous position
				for(int j = 0; j < level.getWalls().size(); j++ )
					if(level.getWalls().get(j).isCollideTop(level.getGhosts().get(i))) {
						level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX(), level.getWalls().get(j).getPosition().getY() + level.getWalls().get(j).getSize().getY() + 1);
						level.getGhosts().get(i).setDirection(getRandomDirection());
					}
			}
			else if(direction == Direction.DOWN) {
				//ghost move down
				level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX(), level.getGhosts().get(i).getPosition().getY() + speed);
				//if ghosts collide to wall change their direction and set their position to previous position
				for(int j = 0; j < level.getWalls().size(); j++ )
					if(level.getWalls().get(j).isCollideBottom(level.getGhosts().get(i))) {
						level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX(), level.getWalls().get(j).getPosition().getY() - level.getGhosts().get(i).getSize().getY() - 1);
						level.getGhosts().get(i).setDirection(getRandomDirection());
					}
			}
			else if(direction == Direction.RIGHT) {
				//ghost move right
				level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX() + speed, level.getGhosts().get(i).getPosition().getY());
				//if ghosts collide to wall change their direction and set their position to previous position
				for(int j = 0; j < level.getWalls().size(); j++ )
					if(level.getWalls().get(j).isCollideRight(level.getGhosts().get(i))) {
						level.getGhosts().get(i).setPosition(level.getWalls().get(j).getPosition().getX() - level.getGhosts().get(i).getSize().getX() - 1, level.getGhosts().get(i).getPosition().getY());
						level.getGhosts().get(i).setDirection(getRandomDirection());
					}
			}
			else if(direction == Direction.LEFT) {
				//ghost move left
				level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX() - speed, level.getGhosts().get(i).getPosition().getY());
				//if ghosts collide to wall change their direction and set their position to previous position
				for(int j = 0; j < level.getWalls().size(); j++ )
					if(level.getWalls().get(j).isCollideLeft(level.getGhosts().get(i))) {
						level.getGhosts().get(i).setPosition(level.getWalls().get(j).getPosition().getX() +  level.getWalls().get(j).getSize().getX() + 1, level.getGhosts().get(i).getPosition().getY());
						level.getGhosts().get(i).setDirection(getRandomDirection());
					}
			}
		}
		
		//when game is finished
		public void gameFinish() {
			//when protagonist lost game
			if(!level.getPro().alive) {
				gameWin = false;
				gameFinish = true;
				showFinish(gameWin);
			}
			//when protagonist won game
			else if(level.getPellets().isEmpty() && level.getItem().isEmpty()) {
				gameWin = true;
				gameFinish = true;
				showFinish(gameWin);
			} 
			//when time is up
			else if (gameCount.getString() == "0:00") {
				gameWin = false;
				gameFinish = true;
				showFinish(gameWin);
			}
		}
		
		//show game finish text and window
		public void showFinish(boolean win) {

			ArrayList<Object> buttons = new ArrayList<Object>();
			//if protagonist won the game
			if (win) {
				gameFinishText = new Text(new Position(500, 225), 250, "Player Won", 26, Color.BLACK);	
			}
			//if ghost win the game
			else {
				gameFinishText = new Text(new Position(500, 225), 250, "Ghost Won", 28, Color.BLACK);
			}
			
			//show scores
			Text score = new Text(new Position(515, 290), 200, info.getScore(), 36, Color.BLACK);	
			addText(gameFinishText);
			addText(score);
			
			//buttons created
			background = new Object(new Position (450, 150), new Position (250, 300), new Image("resource/gameFinishWindow_" + (level.world + 1) +".png"));
			menu_button = new Object(new Position (600, 350), new Position (50,50), new Image("resource/menuButton.png"));
			retry_button = new Object(new Position (500, 350), new Position (50,50), new Image("resource/retryButton.png"));
			buttons.add(background);
			buttons.add(menu_button);
			buttons.add(retry_button);
			addObject(buttons);
			
			//highlight buttons
			Object textHighlighter = new Object(new Position(gameFinishText.getPosition().getX() - 20,gameFinishText.getPosition().getY() - 34),new Position(190, 41),new Image("resource/Text_Highlight.png"));
			addObject(textHighlighter);
			Object scoreHighlighter = new Object(new Position(score.getPosition().getX() - 12,score.getPosition().getY() - 34),new Position(140, 41),new Image("resource/Text_Highlight.png"));
			addObject(scoreHighlighter);
			
			//stop meida
			stopMedia();
			Media gameWinSound = new Media(new File("./src/resource/gameWin.wav").toURI().toString());
			mediaPlayer = new MediaPlayer(gameWinSound);
			mediaPlayer.play();
		}
		
		//can know that whether game is finished or not
		public boolean getGameFinish() {
			return gameFinish;
		}
		
		//when game is finished, player can choose their next round
		public int nextRound(double x, double y) {
			
			//go back to menu
			if(menu_button.isInsideObject(x, y)) {
				//go back to menu
				return 1;
			}
			//play once again
			else if(retry_button.isInsideObject(x, y)) {
				//play one more game
				return 2;
			}
			else 
				return 0;
		}
		
		//initiate map level
		public void initLevel() {
			multiInGame = new MultiInGame(rand.nextInt(4), 4);
		}
		
		//reset character when it collide to ghost
		public void resetCharacter() {
			objects.removeAll(level.getGhosts());
			level.resetCharacter();
			objects.addAll(level.getGhosts());
		}

		//check protagonist is alive or not
		public void proAlive() {
			if(level.getPro().getLife() <= 0)
				level.getPro().alive = false;
		}
		
		//when protagonist is died
		public void proDied() {
			if(!level.getPro().alive) {
				removeObject(level.getPro());
			} 
			else {
				Media reviveSound = new Media(new File("./src/resource/revived_InGame.wav").toURI().toString());
				mediaPlayer = new MediaPlayer(reviveSound);
				mediaPlayer.play();
				resetCharacter();
			}
		}
		
		//make protagonist character move
		public void protagonistMove() {
			if (movePressed.getMovePressed(Direction.UP)) {
				level.getPro().setDirection(Direction.UP);
				moveUp();
			}
			else if (movePressed.getMovePressed(Direction.DOWN)) {
				level.getPro().setDirection(Direction.DOWN);
				moveDown();
			}
			if (movePressed.getMovePressed(Direction.RIGHT)) {
				level.getPro().setDirection(Direction.RIGHT);
				moveRight();
			}
			else if (movePressed.getMovePressed(Direction.LEFT)) {
				level.getPro().setDirection(Direction.LEFT);
				moveLeft();
			}
			//check that whether protagonist is colliding pellets or items
			checkPelletsAndItems();
		}
		
		//make ghost that is controlled by player move
		public void ghostMove() {
			if (level.getGhosts().get(playerGhost).getAlive() && !level.getGhosts().get(playerGhost).freeze) {
				if (movePressed.getGhostMovePressed(Direction.UP)) {
					level.getGhosts().get(playerGhost).setDirection(Direction.UP);
					moveGhostUp();
				}
				else if (movePressed.getGhostMovePressed(Direction.DOWN)) {
					level.getGhosts().get(playerGhost).setDirection(Direction.DOWN);
					moveGhostDown();
				}
				if (movePressed.getGhostMovePressed(Direction.RIGHT)) {
					level.getGhosts().get(playerGhost).setDirection(Direction.RIGHT);
					moveGhostRight();
				}
				else if (movePressed.getGhostMovePressed(Direction.LEFT)) {
					level.getGhosts().get(playerGhost).setDirection(Direction.LEFT);
					moveGhostLeft();
				}
			}
		}
		
		//if key that is used for control protagonist is pressed
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
		//if key that is used for control ghost is pressed
		public void pressGhostMove(KeyCode code) {//n
		switch(code) {
			case W:
				movePressed.setGhostMovePressed(Direction.UP, true);
				break;
			case S:
				movePressed.setGhostMovePressed(Direction.DOWN, true);
				break;
			case A:
				movePressed.setGhostMovePressed(Direction.LEFT, true);
				break;
			case D:
				movePressed.setGhostMovePressed(Direction.RIGHT, true);
				break;
			}
		}
		//if key that is used for control protagonist is released
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
		//if key that is used for control ghost is released
		public void releaseGhostMove(KeyCode code) {
			switch(code) {
			case W:
				movePressed.setGhostMovePressed(Direction.UP, false);
				break;
			case S:
				movePressed.setGhostMovePressed(Direction.DOWN, false);
				break;
			case A:
				movePressed.setGhostMovePressed(Direction.LEFT, false);
				break;
			case D:
				movePressed.setGhostMovePressed(Direction.RIGHT, false);
				break;
			case P:
				movePressed.setGhostMovePressed(Direction.ALL, false);
				break;
			}
		}

		//if protagonist collide ghost
		public void isProCollideGhost() {
			//check which ghost is collided
			for(int i = 0; i < level.getGhosts().size(); i++ ) {
				if(level.getGhosts().get(i).isCollideObject(level.getPro()) && !level.getPro().untouchable && level.getGhosts().get(i).alive && !level.getGhosts().get(i).freeze) {
					//if protagonist had item before
					if(level.getPro().item) {
						//ghost died
						level.getGhosts().get(i).setIterator(2);
						//protagonist copy ghsot ability
						level.getPro().setStoredAbility(level.getGhosts().get(i).getAbility());
						drawAbility();
						level.getGhosts().get(i).alive = false;
						int diedGhost = i;
						Timer t = new Timer();
						t.schedule(new TimerTask() {
							@Override
							public void run() {
								//revive died ghost
								level.getGhosts().get(diedGhost).alive = true;
								level.getGhosts().get(diedGhost).setIterator(0);
								cancel();
							}
						}, 2000l);
						//sound play
						Media startSound = new Media(new File("./src/resource/kill.wav").toURI().toString());
						mediaPlayer = new MediaPlayer(startSound);
						mediaPlayer.play();
						level.getPro().usableAbility = true;
						removeText(itemTime);
						level.getPro().item = false;
					}
					else {
						//sound play
						stopMedia();
						Media startSound = new Media(new File("./src/resource/die.wav").toURI().toString());
						mediaPlayer = new MediaPlayer(startSound);
						mediaPlayer.play();
						Timer stop = new Timer();
						//protagonist died
						dying = true;
						level.getPro().setIterator(4);
						stop.schedule(new TimerTask() {
							@Override
							public void run() {
								if (multiInGame != null) {
									//decrease life
									level.getPro().setIterator(5);
									level.getPro().decreaseLife();
									drawLife();
									//stop sound
									stopMedia();
									Media startSound = new Media(new File("./src/resource/die.wav").toURI().toString());
									mediaPlayer = new MediaPlayer(startSound);
									mediaPlayer.play();
								}
							}
						}, 1500l);
						stop.schedule(new TimerTask() {
							@Override
							public void run() {
								if (multiInGame != null) {
									proAlive();
									proDied();
								}
							}
						}, 3000l);
						stop.schedule(new TimerTask() {
							@Override
							public void run() {
								//revive protagonist if life is left
								dying = false;
							}
						}, 4500l);
						
						
					}
				}
			}
		}
		
		//if ghost collide ice
		public void isGhostCollideIce() {
			for(int i = 0; i < level.getGhosts().size(); i++ ) {
				if(ice.isCollideObject(level.getGhosts().get(i))) {
					//ghost change to ice
					Object ice = new Object(level.getGhosts().get(i).getPosition(), new Image("/resource/iced.png"));
					icedGhost.add(ice);
					addObject(ice);
					//ghost unfreezed
					if (level.getGhosts().get(i).freeze == false) {
						Media startSound = new Media(new File("./src/resource/iced.wav").toURI().toString());
						mediaPlayer = new MediaPlayer(startSound);
						mediaPlayer.play();
					}
					level.getGhosts().get(i).freeze = true;
				}
			}
		}
		
		//if protagonist use ability
		public void useAbility() {
			Object newPosition = new Object(new Position (0,0), new Position(20,20));
			int count = 0;
			if(level.getPro().usableAbility) {
				Ability ability = level.getPro().getStoredAbility();
				if(ability == Ability.RAINBOW_STAR) {
					//speed is doubled
					speed = speed * 2;
					abilityTimer();
					Timer t = new Timer();
					t.schedule(new TimerTask() {
						@Override
						public void run() {

							speed = 2.5;
							cancel();
						}
					}, 3000l);
					level.getPro().usableAbility = false;
					level.getPro().setStoredAbility(Ability.DEFAULT);
					drawAbility();
					Media startSound = new Media(new File("./src/resource/rainbow.wav").toURI().toString());
					mediaPlayer = new MediaPlayer(startSound);
					mediaPlayer.play();
				}
				else if(ability == Ability.NURSE) {
					//life is increased
					level.getPro().increaseLife();
					drawLife();
					level.getPro().usableAbility = false;
					level.getPro().setStoredAbility(Ability.DEFAULT);
					drawAbility();
					Media startSound = new Media(new File("./src/resource/nurse.wav").toURI().toString());
					mediaPlayer = new MediaPlayer(startSound);
					mediaPlayer.play();
				}
				else if(ability == Ability.WIZARD) {
					Direction direction = level.getPro().getDirection();
					//check what is on next pixel
					if (direction == Direction.UP) {
						newPosition.setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY() - level.getWalls().get(0).getSize().getY()*2);
						if (level.getPro().untouchable) 
							level.getPro().setIterator(7);
						else 
							level.getPro().setIterator(1);
					}
					else if (direction == Direction.DOWN) {
						newPosition.setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY() + (level.getWalls().get(0).getSize().getY()+1)*2);
						if (level.getPro().untouchable) 
							level.getPro().setIterator(6);
						else 
							level.getPro().setIterator(0);
					}
					else if (direction == Direction.RIGHT) {
						newPosition.setPosition(level.getPro().getPosition().getX() + (level.getWalls().get(0).getSize().getY()+1)*2, level.getPro().getPosition().getY());
						if (level.getPro().untouchable) 
							level.getPro().setIterator(8);
						else 
							level.getPro().setIterator(2);
					}
					else if (direction == Direction.LEFT) {
						newPosition.setPosition(level.getPro().getPosition().getX() - level.getWalls().get(0).getSize().getY()*2, level.getPro().getPosition().getY());
						if (level.getPro().untouchable) 
							level.getPro().setIterator(9);
						else 
							level.getPro().setIterator(3);
					}
					for(int i = 0; i < level.getWalls().size(); i++ ) {
						if(!level.getWalls().get(i).isCollideObject(newPosition)) {
							count++;
						}
							
					}
					//if protagonist use wizard ability it can teleport through the wall
					if (count == level.getWalls().size()) {
						level.getPro().setPosition(newPosition.getPosition().getX(), newPosition.getPosition().getY());
						level.getPro().usableAbility = false;
						level.getPro().setStoredAbility(Ability.DEFAULT);
						drawAbility();
						Media startSound = new Media(new File("./src/resource/wizard.wav").toURI().toString());
						mediaPlayer = new MediaPlayer(startSound);
						mediaPlayer.play();
					}
				}
				else if(ability == Ability.ICE) {
					//make ice on the map that can freeze ghosts
					newPosition.setPosition(level.getPro().getPosition().getX()-level.getPro().getSize().getX()/2, level.getPro().getPosition().getY()-level.getPro().getSize().getY()/2);
					ice = new Object(newPosition.getPosition(), new Position(50,50), new Image("/resource/ice.png"));
					objects.add(ice);
					iceAppear = true;
					abilityTimer();
					Media startSound = new Media(new File("./src/resource/iceAbility.wav").toURI().toString());
					mediaPlayer = new MediaPlayer(startSound);
					mediaPlayer.play();
					Timer t = new Timer();
					t.schedule(new TimerTask() {
						@Override
						public void run() {
							iceAppear = false;
							removeObject(ice);
							removeObject(icedGhost);
							icedGhost.clear();
							for(int i = 0; i < level.getGhosts().size(); i++ ) {
								if(level.getGhosts().get(i).freeze = true) {
									level.getGhosts().get(i).freeze = false;
								}
							}
							cancel();
						}
					}, 3000l);
					level.getPro().usableAbility = false;
					level.getPro().setStoredAbility(Ability.DEFAULT);
					drawAbility();
				}
				else if(ability == Ability.NINJA) {
					//protagonist is being untouchable
					level.getPro().setUntouchable(true);
					switch(level.getPro().getDirection()) {
					case UP:
						level.getPro().setIterator(7);
						break;
					case DOWN:
						level.getPro().setIterator(6);
						break;
					case RIGHT:
						level.getPro().setIterator(8);
						break;
					case LEFT:
						level.getPro().setIterator(9);
						break;
					}
					abilityTimer();
					Timer t = new Timer();
					t.schedule(new TimerTask() {
						@Override
						public void run() {
							switch(level.getPro().getDirection()) {
							case UP:
								level.getPro().setIterator(1);
								break;
							case DOWN:
								level.getPro().setIterator(0);
								break;
							case RIGHT:
								level.getPro().setIterator(2);
								break;
							case LEFT:
								level.getPro().setIterator(3);
								break;
							}
							level.getPro().setUntouchable(false);
							cancel();
						}
					}, 3000l);
					level.getPro().usableAbility = false;
					level.getPro().setStoredAbility(Ability.DEFAULT);
					drawAbility();
					Media startSound = new Media(new File("./src/resource/ninja.wav").toURI().toString());
					mediaPlayer = new MediaPlayer(startSound);
					mediaPlayer.play();
				}
			}
		}
		
		//stop media play
		public void stopMedia() {
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}
			if (backPlayer != null) {
				backPlayer.stop();
			}
		}
		
		//ability has timer
		private void abilityTimer() {
			Text abilityTime = new Text(new Position(1200,650), 80, 3, 24, Color.BLACK);
			addText(abilityTime);
			Timer textTimer = new Timer();
			textTimer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					if (abilityTime.getString().equals("0")) {
						removeText(abilityTime);
						cancel();
					}
					else {
						abilityTime.setString( Integer.valueOf(abilityTime.getString()) - 1);
					}
				}
			}, 1000l, 1000l);
		}
		
		//find players ghost that player want to play
		public void findPlayerGhost() {
			for(int i=0; i <level.getGhosts().size(); i++) {
				if(level.getGhosts().get(i).getAbility() == ghostTeam) {
					playerGhost = i;

				}
			}
		}
		
		//player's ghost moving up
		private void moveGhostUp() {
			level.getGhosts().get(playerGhost).setPosition(level.getGhosts().get(playerGhost).getPosition().getX(), level.getGhosts().get(playerGhost).getPosition().getY() - speed);
			for(int i = 0; i < level.getWalls().size(); i++ )
				if(level.getWalls().get(i).isCollideTop(level.getGhosts().get(playerGhost))) {
					level.getGhosts().get(playerGhost).setPosition(level.getGhosts().get(playerGhost).getPosition().getX(), level.getWalls().get(i).getPosition().getY() + level.getWalls().get(i).getSize().getY() + 1);
				}
		}
		//player's ghost moving down
		private void moveGhostDown() {
			level.getGhosts().get(playerGhost).setPosition(level.getGhosts().get(playerGhost).getPosition().getX(), level.getGhosts().get(playerGhost).getPosition().getY() + speed);
			for(int i = 0; i < level.getWalls().size(); i++ )
				if(level.getWalls().get(i).isCollideBottom(level.getGhosts().get(playerGhost))) {
					level.getGhosts().get(playerGhost).setPosition(level.getGhosts().get(playerGhost).getPosition().getX(), level.getWalls().get(i).getPosition().getY() - level.getGhosts().get(playerGhost).getSize().getY() - 1);
				}
		}
		//player's ghost moving right
		private void moveGhostRight() {
			boolean moving = true;
			level.getGhosts().get(playerGhost).setPosition(level.getGhosts().get(playerGhost).getPosition().getX() + speed, level.getGhosts().get(playerGhost).getPosition().getY());
			for(int i = 0; i < level.getWalls().size(); i++ )
				if(level.getWalls().get(i).isCollideRight(level.getGhosts().get(playerGhost))) {
					level.getGhosts().get(playerGhost).setPosition(level.getWalls().get(i).getPosition().getX() - level.getGhosts().get(playerGhost).getSize().getX() - 1, level.getGhosts().get(playerGhost).getPosition().getY());
					moving = false;
				}
			if (moving) {
				level.getGhosts().get(playerGhost).setIterator(0);
			}
		}
		//player's ghost moving left
		private void moveGhostLeft() {
			boolean moving = true;
			level.getGhosts().get(playerGhost).setPosition(level.getGhosts().get(playerGhost).getPosition().getX() - speed, level.getGhosts().get(playerGhost).getPosition().getY());
			for(int i = 0; i < level.getWalls().size(); i++ )
				if(level.getWalls().get(i).isCollideLeft(level.getGhosts().get(playerGhost))) {
					level.getGhosts().get(playerGhost).setPosition(level.getWalls().get(i).getPosition().getX() +  level.getWalls().get(i).getSize().getX() + 1, level.getGhosts().get(playerGhost).getPosition().getY());
					moving = false;
				}
			if (moving) {
				level.getGhosts().get(playerGhost).setIterator(1);
			}
		}
		
		//protagonist moving up
		private void moveUp() {
			boolean moving = true;
			level.getPro().setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY() - speed);
			for(int i = 0; i < level.getWalls().size(); i++ )
				//if wall is located on top side of protagonist
				if(level.getWalls().get(i).isCollideTop(level.getPro())) {
					//protagonist cannot move go through the wall
					level.getPro().setPosition(level.getPro().getPosition().getX(), level.getWalls().get(i).getPosition().getY() + level.getWalls().get(i).getSize().getY() + 1);
					moving = false;
				}
			if (moving) {
				//if protagonist is untouchable, image of protagonist is change
				if (level.getPro().untouchable) 
					level.getPro().setIterator(7);
				//if protagnost is moving up, it face top
				else 
					level.getPro().setIterator(1);
			}
		}
		//protagonist moving down
		private void moveDown() {
			boolean moving = true;
			level.getPro().setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY() + speed);
			for(int i = 0; i < level.getWalls().size(); i++ )
				//if wall is located on top side of protagonist
				if(level.getWalls().get(i).isCollideBottom(level.getPro())) {
					//protagonist cannot move go through the wall
					level.getPro().setPosition(level.getPro().getPosition().getX(), level.getWalls().get(i).getPosition().getY() - level.getPro().getSize().getY() - 1);
					moving = false;
				}
			if (moving) {
				//if protagonist is untouchable, image of protagonist is change
				if (level.getPro().untouchable) 
					level.getPro().setIterator(6);
				//if protagnost is moving down, it face bottom
				else 
					level.getPro().setIterator(0);
			}
		}
		//protagonist moving right
		private void moveRight() {
			boolean moving = true;
			level.getPro().setPosition(level.getPro().getPosition().getX() + speed, level.getPro().getPosition().getY());
			for(int i = 0; i < level.getWalls().size(); i++ )
				//if wall is located on top side of protagonist
				if(level.getWalls().get(i).isCollideRight(level.getPro())) {
					//protagonist cannot move go through the wall
					level.getPro().setPosition(level.getWalls().get(i).getPosition().getX() - level.getPro().getSize().getX() - 1, level.getPro().getPosition().getY());
					moving = false;
				}
			if (moving) {
				//if protagonist is untouchable, image of protagonist is change
				if (level.getPro().untouchable) 
					level.getPro().setIterator(8);
				//if protagnost is moving right, it face right
				else 
					level.getPro().setIterator(2);
			}
		}
		//protagonist moving left
		private void moveLeft() {
			boolean moving = true;
			level.getPro().setPosition(level.getPro().getPosition().getX() - speed, level.getPro().getPosition().getY());
			for(int i = 0; i < level.getWalls().size(); i++ )
				//if wall is located on top side of protagonist
				if(level.getWalls().get(i).isCollideLeft(level.getPro())) {
					//protagonist cannot move go through the wall
					level.getPro().setPosition(level.getWalls().get(i).getPosition().getX() +  level.getWalls().get(i).getSize().getX() + 1, level.getPro().getPosition().getY());
					moving = false;
				}
			if (moving) {
				//if protagonist is untouchable, image of protagonist is change
				if (level.getPro().untouchable) 
					level.getPro().setIterator(9);
				//if protagnost is moving right, it face right
				else 
					level.getPro().setIterator(3);
			}
		}
		
		//if protagonist collide to pellets or item
		private void checkPelletsAndItems() {
			for(int i = 0; i < level.getPellets().size(); i++ ) {
				if(level.getPro().isCollideLeft(level.getPellets().get(i))) {
					//collide pellets
					eatPellet(level.getPellets().get(i));
				}
			}

			for(int i = 0; i < level.getItem().size(); i++ ) {
				if(level.getPro().isCollideLeft(level.getItem().get(i))) {
					//collide item
					eatItem(level.getItem().get(i));
				}
			}
		}
		
		//if protagonist collide item
		private void eatItem(Item item) {
			//remove item on map
			removeObject(item);
			if (!level.getPro().item) {
				itemTime.setString(5);
				addText(itemTime);
				Timer textTimer = new Timer();
				textTimer.scheduleAtFixedRate(new TimerTask() {
					@Override
					//after timer is finished delcare that protagonist's item is gone
					public void run() {
						if (itemTime.getString().equals("0")) {
							level.getPro().item = false;
							removeText(itemTime);
							cancel();
						}
						else {
							itemTime.setString( Integer.valueOf(itemTime.getString()) - 1);
						}
					}
				}, 1000l, 1000l);
			} 
			else {
				itemTime.setString(5);
			}
			//tell that protagonist had item
			level.getPro().item = true;
			info.addScore(item);
			//score is increased
			playerScore.setString(info.getScore());
			level.removeItems(item);
			Media startSound = new Media(new File("./src/resource/item_ground.wav").toURI().toString());
			mediaPlayer = new MediaPlayer(startSound);
			mediaPlayer.play();
		}
		
		//if protagonist collide pellets
		private void eatPellet(Pellet pellet) {
			//remove pellest on map
			removeObject(pellet);
			//score is increased
			info.addScore(pellet);
			playerScore.setString(info.getScore());
			level.removePellets(pellet);
			Media startSound = new Media(new File("./src/resource/pellet.wav").toURI().toString());
			mediaPlayer = new MediaPlayer(startSound);
			mediaPlayer.play();
		}
		
		//move pressed class
		public class MovePressed {
			
			//declare flags
			boolean up = false;
			boolean down = false;
			boolean left = false;
			boolean right = false;
			boolean ghostUp = false;
			boolean ghostDown = false;
			boolean ghostLeft = false;
			boolean ghostRight = false;
			
			//get protagonist direction
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
			
			//change protagonist direction
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
			
			//get ghost move pressed
			public boolean getGhostMovePressed(Direction d) {
				switch(d) {
				case UP:
					return ghostUp;
				case DOWN:
					return ghostDown;
				case LEFT:
					return ghostLeft;
				case RIGHT:
					return ghostRight;
				default:
					return false;
				}
			}
			//change protagonist direction
			public void setGhostMovePressed(Direction d, boolean b) {
				switch(d) {
				case UP: 
					ghostUp = b;
					break;
				case DOWN: 
					ghostDown = b;
					break;
				case LEFT:
					ghostLeft = b;
					break;
				case RIGHT:
					ghostRight = b;
					break;
				case ALL:
					ghostUp = b;
					ghostDown = b;
					ghostLeft = b;
					ghostRight = b;
					break;
				}
			}
		}
		
		//get count down flag
		public boolean getCountdownFlag() {
			return inCountdown;
		}
		
		//return flag of pause
		public boolean getPauseFlag() {
			return inPause;
		}
		
		//set timer as  0:00
		public void setTimerTo_0() {
			gameCount.setString("0:00");
		}
		
		//set pause
		public void setPause(boolean b) {
			//if pause, countdown timer is not working
			if (b) {
				if (count != null) removeText(count);
				pause = new Text(new Position(575, 325), 200, "Paused", 24, Color.BLACK);
				addText(pause);
				highlight = new Object(new Position(pause.getPosition().getX() - 12, pause.getPosition().getY() - 24), new Position(100,30), new Image("/resource/Text_Highlight.png"));
				addObject(highlight);
				inPause = b;
			} 
			else {
				if (count != null) addText(count);
				removeText(pause);
				inPause = b;
				removeObject(highlight);
			}
		}
		
		//draw left lives of protagonist
		public void drawLife() {
			removeObject(lifes);
			objects.removeAll(lifes);
			int posX = 1175;
			int posY = 150;
			for(int i = 0; i<level.getPro().getMaxLife(); i++) {

				Position pos = new Position(posX + i*40, posY);
				if(pos.getX() > level.getSide().getPosition().getX() + level.getSide().getSize().getX() - 50) {
					posX = 1175 - i*40;
					posY = posY + 40;
				}
				pos = new Position(posX + i*40, posY);
				if(i<level.getPro().getLife()) {
					Object lifes = new Object(pos, new Position(25,25), new Image("/resource/life.png") );
					this.lifes.add(lifes);
				}
				else {
					Object lifes = new Object(pos, new Position(25,25), new Image("/resource/diedLife.png") );
					this.lifes.add(lifes);
				}
				
			}
			objects.addAll(lifes);
		}
		
		//draw ability of protagonist
		public void drawAbility() {
			removeText(ability);
			Position pos = new Position(1160, 600);
			if(level.getPro().getStoredAbility() == Ability.DEFAULT) {
				ability = new Text(pos, 120, "DEFAULT ", 24, Color.RED);
			}
			else if(level.getPro().getStoredAbility() == Ability.RAINBOW_STAR) {
				ability = new Text(pos, 120, "RAINBOW", 24, Color.RED);
			}
			else if(level.getPro().getStoredAbility() == Ability.NURSE) {
				ability = new Text(pos, 120, "NURSE", 24, Color.RED);
			}
			else if(level.getPro().getStoredAbility() == Ability.WIZARD) {
				ability = new Text(pos, 120, "WIZARD", 24, Color.RED);
			}
			else if(level.getPro().getStoredAbility() == Ability.ICE) {
				ability = new Text(pos, 120, "ICE", 24, Color.RED);
			}
			else if(level.getPro().getStoredAbility() == Ability.NINJA) {
				ability = new Text(pos, 120, "NINJA", 24, Color.RED);
			}
			addText(ability);
		}
		
		//when multi play game is started
		public MultiInGame(int world, int stage) {
			//clear objects and texts
			objects.clear();
			texts.clear();
			lifes = new ArrayList<Object>();
			
			level = new Level(world + 1, stage + 1);
			addObject(level.getObjectList());
			//add texts
			String worldText = (level.getWorld()+1) + " - " + (level.getStage()+1);
			Text stageText = new Text(new Position(600, 50), 120, worldText, 24, Color.BLACK);
			Text score = new Text(new Position(1170, 450), 120, "Score :", 24, Color.BLACK);
			Text time = new Text(new Position(1160, 350), 120, "Time Left :", 24, Color.BLACK);
			Text life = new Text(new Position(1175, 125), 120, "Life : ", 24, Color.BLACK);
			Text abilityText = new Text(new Position(1170, 550), 120, "Ability : ", 24, Color.BLACK);
			addText(stageText);
			addText(score);
			addText(time);
			addText(life);
			addText(abilityText);
			//draw ability
			drawAbility();
			//add all player information on game screen
			info = new PlayerInfo();
			playerScore = new Text(new Position(1190, 475), 80, info.getScore(), 24, Color.BLACK);
			addText(playerScore);
			count = new Text(new Position(575, 375), 99, 3, 24, Color.BLACK);
			addText(count);
			gameCount = new Text(new Position(1190, 375), 80, "2:00", 24, Color.BLACK);
			addText(gameCount);
			highlight = new Object(new Position(count.getPosition().getX() - 12, count.getPosition().getY() - 24), new Position(40,30), new Image("/resource/Text_Highlight.png"));
			addObject(highlight);
			drawLife();
			//count down is started
			inCountdown = true;
			inPause = false;
			//2 min countdown
			Timer timer = new Timer();
			TimerTask gameTimerTask = new TimerTask() {
				@Override
				public void run() {
					if (multiInGame == null) {
						this.cancel();
					}
					String timerString[] = gameCount.getString().split(":");
					int min = Integer.valueOf(timerString[0]);
					int sec = Integer.valueOf(timerString[1]);
					if (!inPause) {
						if (gameFinish) {
							this.cancel();
						}
						else if (min == 0 && sec == 0) {
							gameCount.setString("0:00");
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
			//3 to 0 countdown
			TimerTask counter = new TimerTask() {
				@Override
				public void run() {
					if (multiInGame == null) {
						this.cancel();
					}
					if (count.getString().equals("1")) {
						count.setString("Start!");
						highlight.setSize(80, 30);
						inCountdown = false;
						
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								removeText(count);
								removeObject(highlight);
								count = null;
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
			
			//background music
			Media startSound = new Media(new File("./src/resource/start_InGame.wav").toURI().toString());
			mediaPlayer = new MediaPlayer(startSound);
			mediaPlayer.play();
		}
	
	}
		
	//Multiplayer mode ghost selection
	public class MultiSelect {
		
		//initialise Objects
		ArrayList<Object> ghostTeams;
		Object backButton;
		Object highlight;

		//initialise MediaPlayer
		MediaPlayer background;
		MediaPlayer selectSound;
		
		//init and declare in representing selected ghost
		int sel = 1;
		
		//constructor
		public MultiSelect() {
			//render clear
			objects.clear();
			texts.clear();
			//init ArrayList
			ghostTeams = new ArrayList<Object>();
			//add Back button
			backButton = new Object(new Position(20,20), new Image("/resource/BackButton.png"));
			addObject(backButton);
			//instruction text
			Text select = new Text(new Position(125,100), 500, "Select Ghost :", 80, Color.WHITE);
			addText(select);
			//highlight
			highlight = new Object(new Position(0,0), new Position(250, 250) , new Image("/resource/test_HighLightStage.png"));
			objects.add(highlight);
			
			//add Ghost
			Object ghost = new Object(new Position(300, 150), new Position(200, 200), new Image("/resource/rainbow_sel.png"));
			ghostTeams.add(ghost);
			ghost = new Object(new Position(550, 150), new Position(200, 200), new Image("/resource/nurse_sel.png"));
			ghostTeams.add(ghost);
			ghost = new Object(new Position(800, 150), new Position(200, 200), new Image("/resource/wizard_sel.png"));
			ghostTeams.add(ghost);
			ghost = new Object(new Position(425, 400), new Position(200, 200), new Image("/resource/iceman_sel.png"));
			ghostTeams.add(ghost);
			ghost = new Object(new Position(675, 400), new Position(200, 200), new Image("/resource/ninja_sel.png"));
			ghostTeams.add(ghost);
			objects.addAll(ghostTeams);	
			
			//highlight 1st ghost
			highlight.setPosition(ghostTeams.get(sel-1).getPosition().getX() - 50, ghostTeams.get(sel-1).getPosition().getY() - 50);
		}
		
		//checks if the mouse is on the ghost and change selected ghosts to corresponding
		public void moveMouse(double x, double y) {
			for (int i = 0; i < ghostTeams.size(); i++) {
				if (ghostTeams.get(i).isInsideObject(x, y)) {
					sel = i+1;
				}
			}
		}
		
		//selecting ghost to corresponding mouse click
		public int selectMouse_ghostTeam(double x, double y) {
			for (int i = 0; i < ghostTeams.size(); i++)
				if (ghostTeams.get(i).isInsideObject(x, y)) {
					selectSound = new MediaPlayer(new Media(new File("./src/resource/select.wav").toURI().toString()));
					selectSound.play();
					setGhostTeam(i);
					return i;
				}
			if (backButton.isInsideObject(x, y)) 
				return -2;
			else return -1;
		}
		
		//set ghost team by given representation of integer
		public void setGhostTeam(int n) {
			if(n==0) {
				ghostTeam = Ability.RAINBOW_STAR;
			}
			else if(n==1) {
				ghostTeam = Ability.NURSE;
			}
			else if(n==2) {
				ghostTeam = Ability.WIZARD;
			}
			else if(n==3) {
				ghostTeam = Ability.ICE;
			}
			else if(n==4) {
				ghostTeam = Ability.NINJA;
			}
		}
		
		//set ghost team by selected
		public void setGhostTeam() {
			selectSound = new MediaPlayer(new Media(new File("./src/resource/select.wav").toURI().toString()));
			selectSound.play();
			switch(sel) {
			case 1:
				ghostTeam = Ability.RAINBOW_STAR;
				break;
			case 2:
				ghostTeam = Ability.NURSE;
				break;
			case 3:
				ghostTeam = Ability.WIZARD;
				break;
			case 4:
				ghostTeam = Ability.ICE;
				break;
			case 5:
				ghostTeam = Ability.NINJA;
				break;
			}
		}
		
		//Key up, selects next ghost
		public void selectUp() {
			if (sel >= ghostTeams.size()) sel = 1;
			else sel++;
		}
		//Key down, selects ghost 1 before
		public void selectDown() {
			if (sel <= 1) sel = ghostTeams.size();
			else sel--;
		}
		
		//change highlight to selected ghost
		public void showSelected() {
			highlight.setPosition(ghostTeams.get(sel-1).getPosition().getX() - 10, ghostTeams.get(sel-1).getPosition().getY() - 10);
		}
	}
	
	//when user play single player game
	public class SingleInGame {
		
		//initiate classes 		
		MediaPlayer mediaPlayer;
		Media background_sound;
		MediaPlayer backPlayer;
		Media eat = new Media(new File("./src/resource/pellet.wav").toURI().toString());
		MediaPlayer eatPelletPlayer = new MediaPlayer(eat);
		Level level;
		PlayerInfo info;
		Random rand = new Random();
		MovePressed movePressed = new MovePressed();
		
		//initiate flags
		boolean iceAppear;
		boolean inCountdown;
		boolean inPause;
		boolean dying = false;
		boolean gameWin;
		boolean gameFinish;
		
		//declare protagonist speed and initiate playerGhost
		double speed = 2.5;
		int playerGhost;

		//initiate texts
		Text ability;
		Text playerScore = null;
		Text count = null;
		Text gameCount = null;
		Text pause = null;
		Text gameFinishText = null;
		Text itemTime = new Text(new Position(1240,650), 80, 5, 24, Color.BLACK);
	
		//initiate array lists
		ArrayList<Object> icedGhost = new ArrayList<Object>();
		ArrayList<Object> lifes;
		
		//initiate objects		
		Object highlight;
		Object background;
		Object menu_button;
		Object retry_button;
		Object next_round_button;
		Object ice;
				
		//function that updating game
		public void update() {
			//if protagonist died, nothing is going to work
			if (!dying) {
				if (gameFinish || dying) { 
					//declare protagonist's direction of moving
					movePressed.setMovePressed(Direction.ALL, false);
				}
				//check whether protagonist is alive or not
				proAlive();
				//make protagonist move
				protagonistMove();
				if(!gameFinish) {
					//game finish window
					gameFinish();
					//declare ghosts direction of moving
					ghostAi();
					if(iceAppear)
						//if ghosts collide to ice, they stop moving
						isGhostCollideIce();
				}
				//if protagonist collide ghost, life is decreased
				isProCollideGhost();

			}
		}
		
		//stop media player
		public void stopMedia() {
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}
			if (backPlayer != null) {
				backPlayer.stop();
			}
		}
		
		//declare direction of ghosts that is not controlled by player
		public void ghostAi() {
			
			//declare variables
			double range = 300;
			double speed;
			double xDiff;
			double yDiff;
			for(int i = 0; i < level.getGhosts().size(); i ++) {
				if(level.getGhosts().get(i).alive && !level.getGhosts().get(i).freeze) {
					//if ghosts ability is rainbow_star or ninja
					if(level.getGhosts().get(i).getAbility() == Ability.RAINBOW_STAR || level.getGhosts().get(i).getAbility() == Ability.NINJA) {
						int count = 0;
						speed = 2;
						//get difference in x and y axis
						xDiff = level.getPro().getPosition().getX() - level.getGhosts().get(i).getPosition().getX();
						yDiff = level.getPro().getPosition().getY() - level.getGhosts().get(i).getPosition().getY();
							//if there is no walls between protagonist and ghosts, ghosts go forward to protagonist
							if(xDiff == 0 && yDiff < 0 && !level.getPro().untouchable) {
								Object block = new Object(new Position(level.getPro().getPosition().getX(),level.getPro().getSize().getY() + level.getPro().getPosition().getY()), 
								new Position (level.getPro().getSize().getX(),-yDiff - level.getPro().getSize().getY())); 
								for(int j = 0; j < level.getWalls().size(); j++ ) {
									if(!block.isCollideObject(level.getWalls().get(j))) {
										count++;
									}
								}
								if(count == level.getWalls().size()) {
									level.getGhosts().get(i).setDirection(Direction.UP);
								}
							}
							else if(xDiff == 0 && yDiff > 0 && !level.getPro().untouchable) {
								Object block = new Object(new Position(level.getPro().getPosition().getX(),level.getGhosts().get(i).getSize().getY() + level.getGhosts().get(i).getPosition().getY()), 
										new Position (level.getGhosts().get(i).getSize().getX(),yDiff - level.getGhosts().get(i).getSize().getY())); 
								for(int j = 0; j < level.getWalls().size(); j++ ) {
									if(!block.isCollideObject(level.getWalls().get(j))) {
										count++;
									}
								}
								if(count == level.getWalls().size()) {
									level.getGhosts().get(i).setDirection(Direction.DOWN);
								}
							}
							else if(yDiff == 0 && xDiff < 0 && !level.getPro().untouchable) {
								Object block = new Object(new Position(level.getPro().getPosition().getX() + level.getPro().getSize().getX(),level.getPro().getPosition().getY()), 
										new Position (-xDiff - level.getPro().getSize().getX(), level.getPro().getSize().getY())); 
								for(int j = 0; j < level.getWalls().size(); j++ ) {
									if(!block.isCollideObject(level.getWalls().get(j))) {
										count++;
									}
								}
								if(count == level.getWalls().size()) {
									level.getGhosts().get(i).setDirection(Direction.LEFT);
								}
							}
							else if(yDiff == 0 && xDiff > 0 && !level.getPro().untouchable) {
								Object block = new Object(new Position(level.getGhosts().get(i).getPosition().getX() + level.getGhosts().get(i).getSize().getX(),level.getPro().getPosition().getY()), 
										new Position (xDiff - level.getGhosts().get(i).getSize().getX(), level.getGhosts().get(i).getSize().getY())); 
								for(int j = 0; j < level.getWalls().size(); j++ ) {
									if(!block.isCollideObject(level.getWalls().get(j))) {
										count++;
									}
								}
								if(count == level.getWalls().size()) {
									level.getGhosts().get(i).setDirection(Direction.RIGHT);
								}
							}
							ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
					}
					//if ghost ability is nurse
					else if (level.getGhosts().get(i).getAbility() == Ability.NURSE) {
						//declare ghost speed
						speed = 1.25;
						//find difference in x and y axis
						xDiff = level.getPro().getPosition().getX() - level.getGhosts().get(i).getPosition().getX();
						yDiff = level.getPro().getPosition().getY() - level.getGhosts().get(i).getPosition().getY();
						//if protagonist is in range of ghost detecting, this ghost is going to protagonist
						if((xDiff > -100 && xDiff < 100 && yDiff > -100 && yDiff < 100) && (yDiff == 0 || xDiff == 0 && !level.getPro().untouchable)) {
							if(xDiff < 0 && xDiff > -100 && yDiff == 0){
								level.getGhosts().get(i).setDirection(Direction.RIGHT);
								ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
							}
							else if(xDiff > 0 && xDiff < 100 && yDiff == 0){
								level.getGhosts().get(i).setDirection(Direction.LEFT);
								ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
							}	
							if(yDiff < 0 && yDiff > -100 && xDiff == 0){
								level.getGhosts().get(i).setDirection(Direction.DOWN);
								ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
							}
							else if(yDiff > 0 && yDiff < 100 && xDiff == 0){
								level.getGhosts().get(i).setDirection(Direction.UP);
								ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
							}
						}
						else if(xDiff > -range && xDiff < range && yDiff > -range && yDiff < range && !level.getPro().untouchable) {
								if(xDiff < 0 && xDiff > -range){
									level.getGhosts().get(i).setDirection(Direction.LEFT);
									ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
								}
								else if(xDiff > 0 && xDiff < range){
									level.getGhosts().get(i).setDirection(Direction.RIGHT);
									ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
								}								
								if(yDiff < 0 && yDiff > -range){
									level.getGhosts().get(i).setDirection(Direction.UP);
									ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
								}
								else if(yDiff > 0 && yDiff < range){
									level.getGhosts().get(i).setDirection(Direction.DOWN);
									ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
								}
						}
						else {
							ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
						}
					}
					else if(level.getGhosts().get(i).getAbility() == Ability.WIZARD || level.getGhosts().get(i).getAbility() == Ability.ICE) {
						speed = 1.25;
						boolean moved = false;
						for (int j = 0; j < level.getGhosts().size(); j++) {
							if (i != j && !moved) {
								speed = 1 + level.getWorld()*0.25;
								double ghrange = 50;
								double ghxDiff = level.getGhosts().get(j).getPosition().getX() - level.getGhosts().get(i).getPosition().getX();
								double ghyDiff = level.getGhosts().get(j).getPosition().getY() - level.getGhosts().get(i).getPosition().getY();
								//if protagonist is in range of ghost detecting, this ghost is going forward to protagonist
								if(ghxDiff > -ghrange && ghxDiff < ghrange && ghyDiff > -ghrange && ghyDiff < ghrange && !level.getPro().untouchable) {
									if(ghxDiff < 0 && ghxDiff > -ghrange){
										level.getGhosts().get(i).setDirection(Direction.RIGHT);
										ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
										moved = true;
									}
									else if(ghxDiff > 0 && ghxDiff < ghrange){
										level.getGhosts().get(i).setDirection(Direction.LEFT);
										ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
										moved = true;
									}
									if(ghyDiff < 0 && ghyDiff > -ghrange){
										level.getGhosts().get(i).setDirection(Direction.DOWN);
										ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
										moved = true;
									}
									else if(ghyDiff > 0 && ghyDiff < ghrange){
										level.getGhosts().get(i).setDirection(Direction.UP);
										ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
										moved = true;
									}
								}
							}	
						}
						if (!moved) {
							xDiff = level.getPro().getPosition().getX() - level.getGhosts().get(i).getPosition().getX();
							yDiff = level.getPro().getPosition().getY() - level.getGhosts().get(i).getPosition().getY();
							//if protagonist is in range of ghost detecting, this ghost is going forward to protagonist
							if(xDiff > -range && xDiff < range && yDiff > -range && yDiff < range && !level.getPro().untouchable) {
								if(xDiff < 0 && xDiff > -range){
									level.getGhosts().get(i).setDirection(Direction.LEFT);
									ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
								}
								else if(xDiff > 0 && xDiff < range){
									level.getGhosts().get(i).setDirection(Direction.RIGHT);
									ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
								}	
								if(yDiff < 0 && yDiff > -range){
									level.getGhosts().get(i).setDirection(Direction.UP);
									ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
								}
								else if(yDiff > 0 && yDiff < range){
									level.getGhosts().get(i).setDirection(Direction.DOWN);
									ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
								}
							}
							else
								//ghost just going to same as previous direction
								ghostMove(i,level.getGhosts().get(i).getDirection(),speed);
						}
					}
				}
			}

		}
		
		//get random direction
		public Direction getRandomDirection() {
			int direction = rand.nextInt(4)+1;
			if(direction == 1) {
				return Direction.UP;
			}
			else if(direction == 2) {
				return Direction.DOWN;
			}
			else if(direction == 3) {
				return Direction.RIGHT;
			}
			else {
				return Direction.LEFT;
			}
		}

		
		//make ghost that is not controlled by player move
		public void ghostMove(int i, Direction direction, double speed) {
			if(direction == Direction.UP) {
				//ghost move up
				level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX(), level.getGhosts().get(i).getPosition().getY() - speed);
				//if ghosts collide to wall change their direction and set their position to previous position
				for(int j = 0; j < level.getWalls().size(); j++ )
					if(level.getWalls().get(j).isCollideTop(level.getGhosts().get(i))) {
						level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX(), level.getWalls().get(j).getPosition().getY() + level.getWalls().get(j).getSize().getY() + 1);
						level.getGhosts().get(i).setDirection(getRandomDirection());
					}
			}
			else if(direction == Direction.DOWN) {
				//ghost move down
				level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX(), level.getGhosts().get(i).getPosition().getY() + speed);
				//if ghosts collide to wall change their direction and set their position to previous position
				for(int j = 0; j < level.getWalls().size(); j++ )
					if(level.getWalls().get(j).isCollideBottom(level.getGhosts().get(i))) {
						level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX(), level.getWalls().get(j).getPosition().getY() - level.getGhosts().get(i).getSize().getY() - 1);
						level.getGhosts().get(i).setDirection(getRandomDirection());
					}
			}
			else if(direction == Direction.RIGHT) {
				//ghost move right
				level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX() + speed, level.getGhosts().get(i).getPosition().getY());
				//if ghosts collide to wall change their direction and set their position to previous position
				for(int j = 0; j < level.getWalls().size(); j++ )
					if(level.getWalls().get(j).isCollideRight(level.getGhosts().get(i))) {
						level.getGhosts().get(i).setPosition(level.getWalls().get(j).getPosition().getX() - level.getGhosts().get(i).getSize().getX() - 1, level.getGhosts().get(i).getPosition().getY());
						level.getGhosts().get(i).setDirection(getRandomDirection());
					}
			}
			else if(direction == Direction.LEFT) {
				//ghost move left
				level.getGhosts().get(i).setPosition(level.getGhosts().get(i).getPosition().getX() - speed, level.getGhosts().get(i).getPosition().getY());
				//if ghosts collide to wall change their direction and set their position to previous position
				for(int j = 0; j < level.getWalls().size(); j++ )
					if(level.getWalls().get(j).isCollideLeft(level.getGhosts().get(i))) {
						level.getGhosts().get(i).setPosition(level.getWalls().get(j).getPosition().getX() +  level.getWalls().get(j).getSize().getX() + 1, level.getGhosts().get(i).getPosition().getY());
						level.getGhosts().get(i).setDirection(getRandomDirection());
					}
			}
		}
		
		//when game is finished
		public void gameFinish() {
			//when protagonist lost game
			if(!level.getPro().alive) {
				gameWin = false;
				gameFinish = true;
				showFinish(gameWin);
			}
			//when protagonist won game
			else if(level.getPellets().isEmpty() && level.getItem().isEmpty()) {
				gameWin = true;
				gameFinish = true;
				showFinish(gameWin);
			} 
			//when time is up
			else if (gameCount.getString() == "0:00") {
				gameWin = false;
				gameFinish = true;
				showFinish(gameWin);
			}
		}
		
		//when game is finished
		public void showFinish(boolean win) {
			ArrayList<Object> buttons = new ArrayList<Object>();
			//if player won
			if (win) {
				//show score text and message that player won the game
				gameFinishText = new Text(new Position(490, 225), 250, "Game Won", 30, Color.BLACK);
				addText(gameFinishText);
				Text score = new Text(new Position(515, 290), 200, info.getScore(), 36, Color.BLACK);
				addText(score);
				
				//make buttons that can go menu or next round
				background = new Object(new Position (450, 150), new Position (250, 300), new Image("resource/gameFinishWindow_" + (level.world + 1) +".png"));
				menu_button = new Object(new Position (600, 350), new Position (50,50), new Image("resource/menuButton.png"));
				next_round_button = new Object(new Position (500, 350), new Position (50,50), new Image("resource/nextButton.png"));
				buttons.add(background);
				buttons.add(menu_button);
				buttons.add(next_round_button);
				addObject(buttons);
				
				//highlight the texts
				Object textHighlighter = new Object(new Position(gameFinishText.getPosition().getX() - 10,gameFinishText.getPosition().getY() - 34),new Position(190, 41),new Image("resource/Text_Highlight.png"));
				addObject(textHighlighter);
				Object scoreHighlighter = new Object(new Position(score.getPosition().getX() - 12,score.getPosition().getY() - 34),new Position(140, 41),new Image("resource/Text_Highlight.png"));
				addObject(scoreHighlighter);
				
				//stop media play
				stopMedia();
				Media gameWinSound = new Media(new File("./src/resource/gameWin.wav").toURI().toString());
				mediaPlayer = new MediaPlayer(gameWinSound);
				mediaPlayer.play();
			} 
			//when player lost
			else {
				//show score text and message that player lost the game
				gameFinishText = new Text(new Position(490, 225), 250, "Game Lost", 30, Color.BLACK);
				addText(gameFinishText);
				Text score = new Text(new Position(515, 290), 200, info.getScore(), 36, Color.BLACK);
				addText(score);
				
				//make buttons that can go menu or retry stage
				background = new Object(new Position (450, 150), new Position (250, 300), new Image("resource/gameFinishWindow_" + (level.world + 1) +".png"));
				menu_button = new Object(new Position (600, 350), new Position (50,50), new Image("resource/menuButton.png"));
				retry_button = new Object(new Position (500, 350), new Position (50,50), new Image("resource/retryButton.png"));
				buttons.add(background);
				buttons.add(menu_button);
				buttons.add(retry_button);
				addObject(buttons);
				
				//highlight the texts
				Object textHighlighter = new Object(new Position(gameFinishText.getPosition().getX() - 12,gameFinishText.getPosition().getY() - 34),new Position(190, 41),new Image("resource/Text_Highlight.png"));
				addObject(textHighlighter);
				Object scoreHighlighter = new Object(new Position(score.getPosition().getX() - 12,score.getPosition().getY() - 34),new Position(140, 41),new Image("resource/Text_Highlight.png"));
				addObject(scoreHighlighter);
				
				//stop media play
				stopMedia();
				Media gameOverSound = new Media(new File("./src/resource/gameOver.wav").toURI().toString());
				mediaPlayer = new MediaPlayer(gameOverSound);
				mediaPlayer.play();
			}
		}
		
		//get game finish
		public boolean getGameFinish() {
			return gameFinish;
		}
		
		//when game is finished, player can choose their next round
		public int nextRound(double x, double y) {
			if(gameWin) {
				if(menu_button.isInsideObject(x, y)) {
					//go back to menu
					return 1;
				}
				else if(next_round_button.isInsideObject(x, y)) {
					//go to next stage
					return 3;
				}
				else
					return 0;
			}
			else {
				if(menu_button.isInsideObject(x, y)) {
					//go back to menu
					return 1;
					}
				else if(retry_button.isInsideObject(x, y)) {
					//retry stage
					return 2;
				}
				else return 0;
			}
		}

		//initiate map level
		public void initLevel() {
			singleInGame = new SingleInGame(level.getWorld(),level.getStage());
		}
		
		//initiate map level
		public void initNextLevel() {
			if(level.getStage()+1 < maxLevel.get(level.getWorld()))
				singleInGame = new SingleInGame(level.getWorld(),level.getStage() +1);
			else if(level.getWorld() < 3 )
				singleInGame = new SingleInGame(level.getWorld()+1,0);
			else
				singleInGame = new SingleInGame(level.getWorld(), level.getStage());
		}
		
		
		//reset character when it collide to ghost
		public void resetCharacter() {
			objects.removeAll(level.getGhosts());
			level.resetCharacter();
			objects.addAll(level.getGhosts());
		}

		//check protagonist is alive or not
		public void proAlive() {
			if(level.getPro().getLife() <= 0)
				level.getPro().alive = false;
		}
		
		//when protagonist is died
		public void proDied() {
			if(!level.getPro().alive) {
				removeObject(level.getPro());
			} else {
				Media reviveSound = new Media(new File("./src/resource/revived_InGame.wav").toURI().toString());
				mediaPlayer = new MediaPlayer(reviveSound);
				mediaPlayer.play();
				resetCharacter();
			}
		}
		
		//make protagonist character move
		public void protagonistMove() {
			if (movePressed.getMovePressed(Direction.UP)) {
				level.getPro().setDirection(Direction.UP);
				moveUp();
			}
			else if (movePressed.getMovePressed(Direction.DOWN)) {
				level.getPro().setDirection(Direction.DOWN);
				moveDown();
			}
			if (movePressed.getMovePressed(Direction.RIGHT)) {
				level.getPro().setDirection(Direction.RIGHT);
				moveRight();
			}
			else if (movePressed.getMovePressed(Direction.LEFT)) {
				level.getPro().setDirection(Direction.LEFT);
				moveLeft();
			}
			//check that whether protagonist is colliding pellets or items
			checkPelletsAndItems();
		}
		
		//if key that is used for control protagonist is pressed
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
		
		//if key that is used for control protagonist is released
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

		//if protagonist collide ghost
		public void isProCollideGhost() {
			//check which ghost is collided
			for(int i = 0; i < level.getGhosts().size(); i++ ) {
				if(level.getGhosts().get(i).isCollideObject(level.getPro()) && !level.getPro().untouchable && level.getGhosts().get(i).alive && !level.getGhosts().get(i).freeze) {
					//if protagonist had item before
					if(level.getPro().item) {
						//ghost died
						level.getGhosts().get(i).setIterator(2);
						//protagonist copy ghsot ability
						level.getPro().setStoredAbility(level.getGhosts().get(i).getAbility());
						drawAbility();
						level.getGhosts().get(i).alive = false;
						int diedGhost = i;
						Timer t = new Timer();
						t.schedule(new TimerTask() {
							@Override
							public void run() {
								//revive died ghost
								level.getGhosts().get(diedGhost).alive = true;
								level.getGhosts().get(diedGhost).setIterator(0);
								cancel();
							}
						}, 2000l);
						//sound play
						Media startSound = new Media(new File("./src/resource/kill.wav").toURI().toString());
						mediaPlayer = new MediaPlayer(startSound);
						mediaPlayer.play();
						level.getPro().usableAbility = true;
						removeText(itemTime);
						level.getPro().item = false;
					}
					else {
						//sound play
						stopMedia();
						Media startSound = new Media(new File("./src/resource/die.wav").toURI().toString());
						mediaPlayer = new MediaPlayer(startSound);
						mediaPlayer.play();
						Timer stop = new Timer();
						//protagonist died
						dying = true;
						level.getPro().setIterator(4);
						stop.schedule(new TimerTask() {
							@Override
							public void run() {
								if (singleInGame != null) {
									//decrease life
									level.getPro().setIterator(5);
									level.getPro().decreaseLife();
									drawLife();
									//stop sound
									stopMedia();
									Media startSound = new Media(new File("./src/resource/die.wav").toURI().toString());
									mediaPlayer = new MediaPlayer(startSound);
									mediaPlayer.play();
								}
							}
						}, 1500l);
						stop.schedule(new TimerTask() {
							@Override
							public void run() {
								if (singleInGame != null) {
									proAlive();
									proDied();
								}
							}
						}, 3000l);
						stop.schedule(new TimerTask() {
							@Override
							public void run() {
								//revive protagonist if life is left
								dying = false;
							}
						}, 4500l);				
					}
				}
			}
		}
		
		//if ghost collide ice
		public void isGhostCollideIce() {
			for(int i = 0; i < level.getGhosts().size(); i++ ) {
				if(ice.isCollideObject(level.getGhosts().get(i))) {
					//ghost change to ice
					Object ice = new Object(level.getGhosts().get(i).getPosition(), new Image("/resource/iced.png"));
					icedGhost.add(ice);
					addObject(ice);
					//ghost unfreezed
					if (level.getGhosts().get(i).freeze == false) {
						Media startSound = new Media(new File("./src/resource/iced.wav").toURI().toString());
						mediaPlayer = new MediaPlayer(startSound);
						mediaPlayer.play();
					}
					level.getGhosts().get(i).freeze = true;
				}
			}
		}
		
		//if protagonist use ability
		public void useAbility() {
			Object newPosition = new Object(new Position (0,0), new Position(20,20));
			int count = 0;
			if(level.getPro().usableAbility) {
				Ability ability = level.getPro().getStoredAbility();
				if(ability == Ability.RAINBOW_STAR) {
					//speed is doubled
					speed = speed * 2;
					abilityTimer();
					Timer t = new Timer();
					t.schedule(new TimerTask() {
						@Override
						public void run() {
							speed = 2.5;
							cancel();
						}
					}, 3000l);
					level.getPro().usableAbility = false;
					level.getPro().setStoredAbility(Ability.DEFAULT);
					drawAbility();
					Media startSound = new Media(new File("./src/resource/rainbow.wav").toURI().toString());
					mediaPlayer = new MediaPlayer(startSound);
					mediaPlayer.play();
				}
				else if(ability == Ability.NURSE) {
					//life is increased
					level.getPro().increaseLife();
					drawLife();
					level.getPro().usableAbility = false;
					level.getPro().setStoredAbility(Ability.DEFAULT);
					drawAbility();
					Media startSound = new Media(new File("./src/resource/nurse.wav").toURI().toString());
					mediaPlayer = new MediaPlayer(startSound);
					mediaPlayer.play();
				}
				else if(ability == Ability.WIZARD) {
					Direction direction = level.getPro().getDirection();
					//check what is on next pixel
					if (direction == Direction.UP) {
						newPosition.setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY() - level.getWalls().get(0).getSize().getY()*2);
						if (level.getPro().untouchable) 
							level.getPro().setIterator(7);
						else 
							level.getPro().setIterator(1);
					}
					else if (direction == Direction.DOWN) {
						newPosition.setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY() + (level.getWalls().get(0).getSize().getY()+1)*2);
						if (level.getPro().untouchable) 
							level.getPro().setIterator(6);
						else 
							level.getPro().setIterator(0);
					}
					else if (direction == Direction.RIGHT) {
						newPosition.setPosition(level.getPro().getPosition().getX() + (level.getWalls().get(0).getSize().getY()+1)*2, level.getPro().getPosition().getY());
						if (level.getPro().untouchable) 
							level.getPro().setIterator(8);
						else 
							level.getPro().setIterator(2);
					}
					else if (direction == Direction.LEFT) {
						newPosition.setPosition(level.getPro().getPosition().getX() - level.getWalls().get(0).getSize().getY()*2, level.getPro().getPosition().getY());
						if (level.getPro().untouchable) 
							level.getPro().setIterator(9);
						else 
							level.getPro().setIterator(3);
					}
					//check that whether wall is at where the protagonist want to move
					for(int i = 0; i < level.getWalls().size(); i++ ) {
						if(!level.getWalls().get(i).isCollideObject(newPosition)) {
							//if there is nothing on where the protagonist want to move, count is up
							count++;
						}
					}
					//if protagonist use wizard ability it can teleport through the wall
					if (count == level.getWalls().size()) {
						level.getPro().setPosition(newPosition.getPosition().getX(), newPosition.getPosition().getY());
						level.getPro().usableAbility = false;
									level.getPro().setStoredAbility(Ability.DEFAULT);
									drawAbility();
									Media startSound = new Media(new File("./src/resource/wizard.wav").toURI().toString());
									mediaPlayer = new MediaPlayer(startSound);
									mediaPlayer.play();
					}
				}
				else if(ability == Ability.ICE) {
					//make ice on the map that can freeze ghosts
					newPosition.setPosition(level.getPro().getPosition().getX()-level.getPro().getSize().getX()/2, level.getPro().getPosition().getY()-level.getPro().getSize().getY()/2);
					ice = new Object(newPosition.getPosition(), new Position(50,50), new Image("/resource/ice.png"));
					objects.add(ice);
					iceAppear = true;
					abilityTimer();
					Media startSound = new Media(new File("./src/resource/iceAbility.wav").toURI().toString());
					mediaPlayer = new MediaPlayer(startSound);
					mediaPlayer.play();
					Timer t = new Timer();
					t.schedule(new TimerTask() {
						@Override
						public void run() {
							iceAppear = false;
							removeObject(ice);
							removeObject(icedGhost);
							icedGhost.clear();
							for(int i = 0; i < level.getGhosts().size(); i++ ) {
								if(level.getGhosts().get(i).freeze = true) {
									level.getGhosts().get(i).freeze = false;
								}
							}
							cancel();
						}
					}, 3000l);
					level.getPro().usableAbility = false;
					level.getPro().setStoredAbility(Ability.DEFAULT);
					drawAbility();
				}
				else if(ability == Ability.NINJA) {
					//protagonist is being untouchable
					level.getPro().setUntouchable(true);
					switch(level.getPro().getDirection()) {
					case UP:
						level.getPro().setIterator(7);
						break;
					case DOWN:
						level.getPro().setIterator(6);
						break;
					case RIGHT:
						level.getPro().setIterator(8);
						break;
					case LEFT:
						level.getPro().setIterator(9);
						break;
					}
					abilityTimer();
					Timer t = new Timer();
					t.schedule(new TimerTask() {
						@Override
						public void run() {
							switch(level.getPro().getDirection()) {
							case UP:
								level.getPro().setIterator(1);
								break;
							case DOWN:
								level.getPro().setIterator(0);
								break;
							case RIGHT:
								level.getPro().setIterator(2);
								break;
							case LEFT:
								level.getPro().setIterator(3);
							break;
							}
							level.getPro().setUntouchable(false);
							cancel();
						}
					}, 3000l);
					level.getPro().usableAbility = false;
					level.getPro().setStoredAbility(Ability.DEFAULT);
					drawAbility();
					Media startSound = new Media(new File("./src/resource/ninja.wav").toURI().toString());
					mediaPlayer = new MediaPlayer(startSound);
					mediaPlayer.play();
				}
			}
		}
		
		//ability has timer
		private void abilityTimer() {
			Text abilityTime = new Text(new Position(1200,650), 80, 3, 24, Color.BLACK);
			addText(abilityTime);
			Timer textTimer = new Timer();
			textTimer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					if (abilityTime.getString().equals("0")) {
						removeText(abilityTime);
						cancel();
					}
					else {
						abilityTime.setString( Integer.valueOf(abilityTime.getString()) - 1);
					}
				}
			}, 1000l, 1000l);
		}
		
		//protagonist moving up
		private void moveUp() {
			boolean moving = true;
			level.getPro().setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY() - speed);
			for(int i = 0; i < level.getWalls().size(); i++ )
				//if wall is located on top side of protagonist
				if(level.getWalls().get(i).isCollideTop(level.getPro())) {
					//protagonist cannot move go through the wall
					level.getPro().setPosition(level.getPro().getPosition().getX(), level.getWalls().get(i).getPosition().getY() + level.getWalls().get(i).getSize().getY() + 1);
					moving = false;
				}
			if (moving) {
				//if protagonist is untouchable, image of protagonist is change
				if (level.getPro().untouchable) 
					level.getPro().setIterator(7);
				//if protagnost is moving up, it face top
				else 
					level.getPro().setIterator(1);
			}
		}
		//protagonist moving down
		private void moveDown() {
			boolean moving = true;
			level.getPro().setPosition(level.getPro().getPosition().getX(), level.getPro().getPosition().getY() + speed);
			for(int i = 0; i < level.getWalls().size(); i++ )
				//if wall is located on top side of protagonist
				if(level.getWalls().get(i).isCollideBottom(level.getPro())) {
					//protagonist cannot move go through the wall
					level.getPro().setPosition(level.getPro().getPosition().getX(), level.getWalls().get(i).getPosition().getY() - level.getPro().getSize().getY() - 1);
					moving = false;
				}
			if (moving) {
				//if protagonist is untouchable, image of protagonist is change
				if (level.getPro().untouchable) 
					level.getPro().setIterator(6);
				//if protagnost is moving down, it face bottom
				else 
					level.getPro().setIterator(0);
			}
		}
		//protagonist moving right
		private void moveRight() {
			boolean moving = true;
			level.getPro().setPosition(level.getPro().getPosition().getX() + speed, level.getPro().getPosition().getY());
			for(int i = 0; i < level.getWalls().size(); i++ )
				//if wall is located on top side of protagonist
				if(level.getWalls().get(i).isCollideRight(level.getPro())) {
					//protagonist cannot move go through the wall
					level.getPro().setPosition(level.getWalls().get(i).getPosition().getX() - level.getPro().getSize().getX() - 1, level.getPro().getPosition().getY());
					moving = false;
				}
			if (moving) {
				//if protagonist is untouchable, image of protagonist is change
				if (level.getPro().untouchable) 
					level.getPro().setIterator(8);
				//if protagnost is moving right, it face right
				else 
					level.getPro().setIterator(2);
			}
		}
		//protagonist moving left
		private void moveLeft() {
			boolean moving = true;
			level.getPro().setPosition(level.getPro().getPosition().getX() - speed, level.getPro().getPosition().getY());
			for(int i = 0; i < level.getWalls().size(); i++ )
				//if wall is located on top side of protagonist
				if(level.getWalls().get(i).isCollideLeft(level.getPro())) {
					//protagonist cannot move go through the wall
					level.getPro().setPosition(level.getWalls().get(i).getPosition().getX() +  level.getWalls().get(i).getSize().getX() + 1, level.getPro().getPosition().getY());
					moving = false;
				}
			if (moving) {
				//if protagonist is untouchable, image of protagonist is change
				if (level.getPro().untouchable) 
					level.getPro().setIterator(9);
				//if protagnost is moving right, it face right
				else 
					level.getPro().setIterator(3);
			}
		}
		
		//if protagonist collide to pellets or item
		private void checkPelletsAndItems() {
			for(int i = 0; i < level.getPellets().size(); i++ ) {
				if(level.getPro().isCollideLeft(level.getPellets().get(i))) {
					//collide pellets
					eatPellet(level.getPellets().get(i));
				}
			}

			for(int i = 0; i < level.getItem().size(); i++ ) {
				if(level.getPro().isCollideLeft(level.getItem().get(i))) {
					//collide item
					eatItem(level.getItem().get(i));
				}
			}
		}
		
		//if protagonist collide item
		private void eatItem(Item item) {
			//remove item on map
			removeObject(item);
			if (!level.getPro().item) {
				itemTime.setString(5);
				addText(itemTime);
				Timer textTimer = new Timer();
				textTimer.scheduleAtFixedRate(new TimerTask() {
					@Override
					//after timer is finished delcare that protagonist's item is gone
					public void run() {
						if (itemTime.getString().equals("0")) {
							level.getPro().item = false;
							removeText(itemTime);
							cancel();
						}
						else {
							itemTime.setString( Integer.valueOf(itemTime.getString()) - 1);
						}
					}
				}, 1000l, 1000l);
			} 
			else {
				itemTime.setString(5);
			}
			//tell that protagonist had item
			level.getPro().item = true;
			info.addScore(item);
			//score is increased
			playerScore.setString(info.getScore());
			level.removeItems(item);
			Media startSound = new Media(new File("./src/resource/item_ground.wav").toURI().toString());
			mediaPlayer = new MediaPlayer(startSound);
			mediaPlayer.play();
		}
		
		//if protagonist collide pellets
		private void eatPellet(Pellet pellet) {
			//remove pellest on map
			removeObject(pellet);
			//score is increased
			info.addScore(pellet);
			playerScore.setString(info.getScore());
			level.removePellets(pellet);
			eatPelletPlayer.play();
		}

		//initiate class of move pressed
		public class MovePressed {
			//declare flags
			boolean up = false;
			boolean down = false;
			boolean left = false;
			boolean right = false;
			//get move pressed
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
			//change protagonist direction
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
		
		//get count down flag
		public boolean getCountdownFlag() {
			return inCountdown;
		}
		
		//return flag of pause
		public boolean getPauseFlag() {
			return inPause;
		}
		
		//set timer as  0:00
		public void setTimerTo_0() {
			gameCount.setString("0:00");
		}
		
		//set pause
		public void setPause(boolean b) {
			//if pause, countdown timer is not working
			if (b) {
				if (count != null) removeText(count);
				pause = new Text(new Position(575, 325), 200, "Paused", 24, Color.BLACK);
				addText(pause);
				highlight = new Object(new Position(pause.getPosition().getX() - 12, pause.getPosition().getY() - 24), new Position(100,30), new Image("/resource/Text_Highlight.png"));
				addObject(highlight);
				inPause = b;
			} 
			else {
				if (count != null) addText(count);
				removeText(pause);
				inPause = b;
				removeObject(highlight);
			}
		}
		
		//draw left lives of protagonist
		public void drawLife() {
			removeObject(lifes);
			objects.removeAll(lifes);
			int posX = 1175;
			int posY = 150;
			for(int i = 0; i<level.getPro().getMaxLife(); i++) {

				Position pos = new Position(posX + i*40, posY);
				if(pos.getX() > level.getSide().getPosition().getX() + level.getSide().getSize().getX() - 50) {
					posX = 1175 - i*40;
					posY = posY + 40;
				}
				pos = new Position(posX + i*40, posY);
				if(i<level.getPro().getLife()) {
					Object lifes = new Object(pos, new Position(25,25), new Image("/resource/life.png") );
					this.lifes.add(lifes);
				}
				else {
					Object lifes = new Object(pos, new Position(25,25), new Image("/resource/diedLife.png") );
					this.lifes.add(lifes);
				}
				
			}
			objects.addAll(lifes);
		}
		
		//draw ability of protagonist
		public void drawAbility() {
			removeText(ability);
			Position pos = new Position(1160, 600);
			if(level.getPro().getStoredAbility() == Ability.DEFAULT) {
				ability = new Text(pos, 120, "DEFAULT ", 24, Color.RED);
			}
			else if(level.getPro().getStoredAbility() == Ability.RAINBOW_STAR) {
				ability = new Text(pos, 120, "RAINBOW", 24, Color.RED);
			}
			else if(level.getPro().getStoredAbility() == Ability.NURSE) {
				ability = new Text(pos, 120, "NURSE", 24, Color.RED);
			}
			else if(level.getPro().getStoredAbility() == Ability.WIZARD) {
				ability = new Text(pos, 120, "WIZARD", 24, Color.RED);
			}
			else if(level.getPro().getStoredAbility() == Ability.ICE) {
				ability = new Text(pos, 120, "ICE", 24, Color.RED);
			}
			else if(level.getPro().getStoredAbility() == Ability.NINJA) {
				ability = new Text(pos, 120, "NINJA", 24, Color.RED);
			}
			addText(ability);
		}
		
		//when single play game is started
		public SingleInGame(int world, int stage) {
			//clear all objects and texts
			objects.clear();
			texts.clear();
			lifes = new ArrayList<Object>();
			
			//load map
			level = new Level(world + 1, stage + 1);
			addObject(level.getObjectList());
			
			int posX = 1175;
			int posY = 150;
			ArrayList<Image> im = new ArrayList<Image>();
			
			//add texts
			String worldText = (level.getWorld()+1) + " - " + (level.getStage()+1);
			Text stageText = new Text(new Position(600, 50), 120, worldText, 24, Color.BLACK);
			Text score = new Text(new Position(1170, 450), 120, "Score :", 24, Color.BLACK);
			Text time = new Text(new Position(1160, 350), 120, "Time Left :", 24, Color.BLACK);
			Text life = new Text(new Position(1175, 125), 120, "Life : ", 24, Color.BLACK);
			Text abilityText = new Text(new Position(1170, 550), 120, "Ability : ", 24, Color.BLACK);
			addText(stageText);
			addText(score);
			addText(time);
			addText(life);
			addText(abilityText);
			//show current stored ability
			drawAbility();
			//show player score and game count
			info = new PlayerInfo();
			playerScore = new Text(new Position(1190, 475), 80, info.getScore(), 24, Color.BLACK);
			addText(playerScore);
			count = new Text(new Position(575, 375), 99, 3, 24, Color.BLACK);
			addText(count);
			gameCount = new Text(new Position(1190, 375), 80, "2:00", 24, Color.BLACK);
			addText(gameCount);
			highlight = new Object(new Position(count.getPosition().getX() - 12, count.getPosition().getY() - 24), new Position(40,30), new Image("/resource/Text_Highlight.png"));
			addObject(highlight);
			//show lives
			drawLife();
			//count down timer flag
			inCountdown = true;
			//pause flag
			inPause = false;
			//timer set up
			Timer timer = new Timer();
			TimerTask gameTimerTask = new TimerTask() {
				@Override
				public void run() {
					if (singleInGame == null) {
						this.cancel();
					}
					String timerString[] = gameCount.getString().split(":");
					int min = Integer.valueOf(timerString[0]);
					int sec = Integer.valueOf(timerString[1]);
					if (!inPause) {
						if (gameFinish) {
							this.cancel();
						}
						else if (min == 0 && sec == 0) {
							gameCount.setString("0:00");
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
					if (singleInGame == null) {
						this.cancel();
					}
					if (count.getString().equals("1")) {
						count.setString("Start!");
						highlight.setSize(80, 30);
						inCountdown = false;
						
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								removeText(count);
								removeObject(highlight);
								count = null;
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

			Media startSound = new Media(new File("./src/resource/start_InGame.wav").toURI().toString());
			mediaPlayer = new MediaPlayer(startSound);
			mediaPlayer.play();
		}
	}
	
	//Stage selection in single game mode
	public class SingleStageSelect {
		
		//initialise MediaPlayer
		MediaPlayer selectSound;
		MediaPlayer backgroundPlayer;
		
		//initialise Object
		Object world_1;
		Object world_2;
		Object world_3;
		Object world_4;
		Object backButton;
		Object window;
		Object unselectButton;
		Object pointer = null;
		
		//initialise Text
		Text stageSel;
		
		//initialise ArrayList
		ArrayList<Object> stage_buttons;
		ArrayList<Text> stage_texts;
		
		//init variables
		boolean sel = false;
		int world = 0;
		int stage = 0;
		
		//init and declare Images for each world design
		ArrayList<Image> windowImages = new ArrayList<Image>() {
			{
				add(new Image("/resource/LevelSelectWindow_1.png"));
				add(new Image("/resource/LevelSelectWindow_2.png"));
				add(new Image("/resource/LevelSelectWindow_3.png"));
				add(new Image("/resource/LevelSelectWindow_4.png"));
			}
		};
		ArrayList<Image> stageImages = new ArrayList<Image>() {
			{
				add(new Image("/resource/StageButton_1.png"));
				add(new Image("/resource/StageButton_2.png"));
				add(new Image("/resource/StageButton_3.png"));
				add(new Image("/resource/StageButton_4.png"));
			}
		};
		
		//getter and setter
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
		
		//stop and dispose background music
		public void stopMedia() {	
			backgroundPlayer.stop();
			backgroundPlayer.dispose();
		}
		
		//highlights current selected object
		public void showSelected() {
			removeObject(pointer);
			Position pos;
			Position size;
			if (sel){
				pos = new Position(stage_buttons.get(stage).getPosition().getX() - 20, stage_buttons.get(stage).getPosition().getY() + stage_buttons.get(stage).getSize().getY()/2);
				size = new Position(stage_buttons.get(stage).getSize().getX() + 40, stage_buttons.get(stage).getSize().getY()/2 + 40);
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
		
		//playing sound when selected
		public void playSelectSound() {
			selectSound = new MediaPlayer(new Media(new File("./src/resource/select.wav").toURI().toString()));
			selectSound.play();
		}
		
		//add list of stages to renderer 
		public void showStages() {
			playSelectSound();
			stage = 0;
			window = new Object(new Position(240, 180), windowImages);
			window.setIterator(world);
			addObject(window);
			stage_buttons = new ArrayList<Object>();
			for (int i = 0; i < maxLevel.get(world); i++) {
				Object stage = new Object(
						new Position(window.getPosition().getX() + (window.getSize().getX() - maxLevel.get(world) * 120) / (maxLevel.get(world)+1) * (i+1) + 120*i - 5,
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
						new Position(stage_buttons.get(i).getPosition().getX() + stage_buttons.get(i).getSize().getX()/2 - 14,
								stage_buttons.get(i).getPosition().getY() + stage_buttons.get(i).getSize().getY()/2 + 14),
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
		
		//remove list from renderer
		public void hideStages() {
			removeObject(window);
			removeObject(unselectButton);
			removeObject(stage_buttons);
			removeText(stage_texts);
			sel = false;
		}
		
		//change selected stage/world upon mouse movement
		public void moveMouse(double x, double y) {
			if (sel) {
				for (int i = 0; i < stage_buttons.size(); i++) {
					if (stage_buttons.get(i).isInsideObject(x, y)) 
						stage = i;
				}
			} else {
				if (world_1.isInsideObject(x, y)) world = 0;
				else if (world_2.isInsideObject(x, y)) 
					world = 1;
				else if (world_3.isInsideObject(x, y)) 
					world = 2;
				else if (world_4.isInsideObject(x, y)) 
					world = 3;
			}
		}
		
		//returns if the user is clickling an object or not and the integer representation of it for worlds
		public int selectMouse_world(double x, double y) {
			if (world_1.isInsideObject(x, y)) 
				return 1;
			else if (world_2.isInsideObject(x, y)) 
				return 2;
			else if (world_3.isInsideObject(x, y)) 
				return 3;
			else if (world_4.isInsideObject(x, y)) 
				return 4;
			else if (backButton.isInsideObject(x, y)) 
				return 5;
			else return 0;
		}
		
		//returns if the user is clickling an object or not and ther integer represntation of it for stages
		public int selectMouse_stage(double x, double y) {
			for (int i = 0; i < stage_buttons.size(); i++)
				if (stage_buttons.get(i).isInsideObject(x, y)) 
					return i;
			if (backButton.isInsideObject(x, y)) 
				return -2;
			else if (!window.isInsideObject(x, y)) 
				return -3;
			else return -1;
		}
		
		//update selected upon Up Key press
		public void selectUp() {
			if (sel) {
				if (stage >= maxLevel.get(world) - 1);
				else 
					stage++;
			}
			else {
				if (world >= 3);
				else 
					world++;
			}
		}
		
		//update selected upon Down Key press
		public void selectdown() {
			if (sel) {
				if (stage <= 0);
				else 
					stage--;
			}
			else {
				if (world <= 0);
				else 
					world--;
			}
		}
		
		//list of function upon update of rendering
		public void update() {
			showSelected();
		}
		
		//constructor
		public SingleStageSelect() {
			//rendering clear
			objects.clear();
			texts.clear();
			
			//create world objects
			world_1 = new Object(new Position(85, 150), new Image("/resource/World_1.png"));
			addObject(world_1);
			world_2 = new Object(new Position(515, 40), new Image("/resource/World_2.png"));
			addObject(world_2);
			world_3 = new Object(new Position(900, 25), new Image("/resource/World_3.png"));
			addObject(world_3);
			world_4 = new Object(new Position(420, 390), new Image("/resource/World_4.png"));
			addObject(world_4);
			//add back button
			backButton = new Object(new Position(20,20), new Image("/resource/BackButton.png"));
			addObject(backButton);
			//add text instruction
			stageSel = new Text(
					new Position(125,60),
					300,
					"Select Level :",
					50,
					Color.WHITE
					);
			addText(stageSel);
			//Play background music repeatedly
			Media music = new Media(new File("./src/resource/choice.wav").toURI().toString());
			backgroundPlayer = new MediaPlayer(music);
			backgroundPlayer.setOnEndOfMedia(new Runnable() {
			       public void run() {
			    	   backgroundPlayer.seek(Duration.ZERO);
			       }
			   });
			backgroundPlayer.play();
		}
	}
	
	//Main Start class
	public class Start {
		//init Object
		Object option_1;
		Object option_2;
		Object option_3;
		Object arrow;
		
		//init integer representation of selected mode
		int sel;
		
		//init media player
		MediaPlayer mediaPlayer;
		
		//stop and dispose media
		public void stopMedia() {
			mediaPlayer.stop();
			mediaPlayer.dispose();
		}
		
		//getter of selected option
		public int getSelected() {
			return sel;
		}
		
		//constructor
		public Start() {
			//renderer clear
			objects.clear();
			texts.clear();
			
			//declare and add object into rendering list
			addObject(new Object(new Position(390, 100), new Image("/resource/test_Planet.png")));
			addObject(new Object(new Position(520, 210), new Image("/resource/test_Escape.png")));
			option_1 = new Object(new Position(493, 425), new Image("/resource/test_SinglePlayer.png"));
			addObject(option_1);
			option_2 = new Object(new Position(435, 500), new Image("/resource/test_MultiPlayer.png"));
			addObject(option_2);
			arrow = new Object(new Position(425, 430), new Image("/resource/test_Arrow.png"));
			addObject(arrow);
			
			//selected option declared as 1 at first
			sel = 1;
			
			//play background music
			Media music = new Media(new File("./src/resource/start.wav").toURI().toString());
			mediaPlayer = new MediaPlayer(music);
			mediaPlayer.setOnEndOfMedia(new Runnable() {
			       public void run() {
			    	   mediaPlayer.seek(Duration.ZERO);
			       }
			   });
			mediaPlayer.play();
		}
		
		//Key down pressed, option goes down
		public void selectDown() {
			if (sel == 1) {
				sel = 2;
			} else if (sel == 2) {
				sel = 1;
			}
		}
		//Key Up pressed, option goes up
		public void selectUp() {
			if (sel == 1) {
				sel = 2;
			} else if (sel == 2) {
				sel = 1;
			}
		}
		//return selected object upon mouse click
		public int selectMouse(double x, double y) {
			if (option_1.isInsideObject(x, y)) 
				return 1;
			else if (option_2.isInsideObject(x, y)) 
				return 2;

			else return 0;
		}
		//set selected option upon mouse move
		public void moveMouse(double x, double y) {
			if (option_1.isInsideObject(x, y)) 
				sel = 1;
			else if (option_2.isInsideObject(x, y)) 
				sel = 2;
			
		}
		// update renderer
		public void update() {
			if (sel == 1) {
				arrow.setPosition(425, 430);
			} else if (sel == 2) {
				arrow.setPosition(435 - 68, 505);
			}
		}
	}
	
	//Game model handler constructor
	public GameModelHandler() {
		//declare new rendering list
		objects = new ArrayList<Object>();
		texts = new ArrayList<Text>();
	}
}
