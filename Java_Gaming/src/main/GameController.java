package main;

import java.io.File;
import java.util.Random;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.model.GameModelHandler;
import main.model.Level;
import main.model.Position;
import main.model.Text;
import main.view.Colour;
import main.view.GameViewer;

public class GameController {
	//static enum State for FSM
	static enum State {
		START,
		SINGLE_STAGE_SEL,
		SINGLE_IN_GAME,
		STORY,
		MULTI_SEL,
		MULTI_IN_GAME
	};
	//initialise connection
	private Stage stage;
	private GameViewer view;
	private GameModelHandler model;
	
	//initialise game state and level
	private State gState;
	private Level level;
	
	//take mouse position when clicked
	public void takeMouseClicked(double x, double y) {
		switch(gState) {
		case START:
			//when model tells selected change game state to corresponding
			if (model.start.selectMouse(x, y) == 1) {
				model.start.stopMedia();
				gState = State.STORY;
				initState();
			} else if (model.start.selectMouse(x, y) == 2) {
				model.start.stopMedia();
				gState = State.MULTI_SEL;
				initState();
			}
			break;
		case SINGLE_STAGE_SEL:
			//when stage selection gets option selected and init game
			//when world selection gets show stage selection with corresponding world selected
			if (model.singleStageSel.getSel()) {
				switch(model.singleStageSel.selectMouse_stage(x, y)) {
				case -1:
					break;
				case -2:
					gState = State.START;
					model.singleStageSel.stopMedia();
					initState();
					break;
				case -3:
					model.singleStageSel.hideStages();
					break;
				default:
					model.singleStageSel.setStage(model.singleStageSel.selectMouse_stage(x, y));
					model.singleStageSel.playSelectSound();
					model.singleStageSel.stopMedia();
					gState = State.SINGLE_IN_GAME;
					initState();
					break;
				}
			}
			else {
				switch(model.singleStageSel.selectMouse_world(x, y)) {
				case 1:
					model.singleStageSel.setWorld(0);
					model.singleStageSel.showStages();
					break;
				case 2:
					model.singleStageSel.setWorld(1);
					model.singleStageSel.showStages();
					break;
				case 3:
					model.singleStageSel.setWorld(2);
					model.singleStageSel.showStages();
					break;
				case 4:
					model.singleStageSel.setWorld(3);
					model.singleStageSel.showStages();
					break;			
				case 5:
					model.singleStageSel.stopMedia();
					gState = State.START;
					initState();
					break;
				default:	
					break;
				}
			}
			break;
		case SINGLE_IN_GAME:
			//After game has been finished, get option selected from model and do correspond
			if(model.singleInGame.getGameFinish()) {
				switch(model.singleInGame.nextRound(x, y)) {
				case 0:
					break;
				case 1:
					gState = State.SINGLE_STAGE_SEL;
					initState();
					break;
				case 2:
					model.singleInGame.initLevel();
					break;
				case 3:
					model.singleInGame.initNextLevel();
					break;
					

				}
			}
			break;
		case STORY:
			//if story is at end of the slide change stage to single stage selection else show next scene
			if (model.story.getSel() == 5) {
				gState = State.SINGLE_STAGE_SEL;
				initState();
			} else 
				model.story.showNextScene();
			break;
		case MULTI_SEL:
			//when ghost is selected change state to multiplayer game or backbutton to go back to start
			switch(model.multiSelect.selectMouse_ghostTeam(x, y)) {
				case -1:
					break;
				case -2:
					gState = State.START;
					initState();
					break;
				default:		
					gState = State.MULTI_IN_GAME;
					initState();
					break;	
			}
			break;
		case MULTI_IN_GAME:
			//after finishing the game decide to either replay or back to start
			if(model.multiInGame.getGameFinish())
				switch(model.multiInGame.nextRound(x, y)){
				case 0:
					break;
				case 1:
					gState = State.START;
					initState();
					break;
				case 2:
					model.multiInGame.initLevel();
			}
			break;
		}
	}
	//takes movement of the mouse and let model react
	public void takeMouseMoved(double x, double y) {
		switch(gState) {
		case START:
			model.start.moveMouse(x,y);
			break;
		case SINGLE_STAGE_SEL:
			model.singleStageSel.moveMouse(x, y);
			break;
		case SINGLE_IN_GAME:
			break;
		case MULTI_SEL:
			model.multiSelect.moveMouse(x, y);
			break;
		case MULTI_IN_GAME:
			break;
		}
	}
	//take Key Pressed
	public void takeKeyPressed(KeyCode code) {
		switch(gState) {
		case START:
			switch (code) {
			case ESCAPE:
				//quit confirmation fires
				stage.fireEvent(
                        new WindowEvent(
                                stage,
                                WindowEvent.WINDOW_CLOSE_REQUEST
                        )
                );
				break;
			case UP:
				//update selected option
				model.start.selectUp();
				break;
			case DOWN:
				//update selected option
				model.start.selectDown();
				break;
			case ENTER:
				//get selected option, 1 to single game mode, 2 multi player game mode
				int sel = model.start.getSelected();
				if (sel == 1) {
					gState = State.STORY;
					model.start.stopMedia();
					initState();
				} else if (sel == 2) {
					gState = State.MULTI_SEL;
					model.start.stopMedia();
					initState();
				}
				break;
			default:
				break;
			}
			break;
		case SINGLE_STAGE_SEL:
			if (model.singleStageSel.getSel()) {
				switch(code) {
				case ESCAPE:
					model.singleStageSel.hideStages();
					break;
				case UP:
					model.singleStageSel.selectUp();
					break;
				case DOWN:
					model.singleStageSel.selectdown();
					break;
				case ENTER:
					model.singleStageSel.playSelectSound();
					model.singleStageSel.stopMedia();
					gState = State.SINGLE_IN_GAME;
					initState();
					break;
				default:
					break;
				}
			}
			else {
				switch(code) {
				case ESCAPE:
					gState = State.START;
					initState();
					break;
				case ENTER:
					model.singleStageSel.showStages();
					break;
				case UP:
					model.singleStageSel.selectUp();
					break;
				case DOWN:
					model.singleStageSel.selectdown();
					break;
				default:
					break;
				}
			}
			break;
		case SINGLE_IN_GAME:
			switch(code) {
			case ESCAPE:
				model.singleInGame.stopMedia();
				gState = State.SINGLE_STAGE_SEL;
				initState();
				break;
			case UP:
			case DOWN:
			case RIGHT:
			case LEFT:
				if (!model.singleInGame.getCountdownFlag() && !model.singleInGame.getPauseFlag() && !model.singleInGame.getGameFinish())
					model.singleInGame.pressMove(code);
				break;
			case SPACE:
			case ENTER:
				if (!model.singleInGame.getCountdownFlag() && !model.singleInGame.getPauseFlag() && !model.singleInGame.getGameFinish())
					model.singleInGame.useAbility();
				break;
			case P:
				model.singleInGame.setPause(!model.singleInGame.getPauseFlag());
				model.singleInGame.releaseMove(code);
				break;
			default:
				break;
			}
			break;
		case STORY:
			switch(code) {
			case ESCAPE:
				gState = State.START;
				initState();
				break;
			default:
				if (model.story.getSel() == 5) {
					gState = State.SINGLE_STAGE_SEL;
					initState();
				} else 
					model.story.showNextScene();
				break;
			}
			break;
		case MULTI_SEL:
			switch(code) {
			case ESCAPE:
				gState = State.START;
				initState();
				break;
			case UP:
			case RIGHT:
				model.multiSelect.selectUp();
				break;
			case DOWN:
			case LEFT:
				model.multiSelect.selectDown();
				break;
			case ENTER:
				model.multiSelect.setGhostTeam();
				gState = State.MULTI_IN_GAME;
				initState();
				break;
			}
			break;
		case MULTI_IN_GAME:
			switch(code) {
			case ESCAPE:
				gState = State.MULTI_SEL;
				initState();
				break;
			case UP:
			case DOWN:
			case RIGHT:
			case LEFT:
				if (!model.multiInGame.getCountdownFlag() && !model.multiInGame.getPauseFlag() && !model.multiInGame.getGameFinish())
					model.multiInGame.pressMove(code);
				break;
			case W:
			case A:
			case S:
			case D:
				if (!model.multiInGame.getCountdownFlag() && !model.multiInGame.getPauseFlag() && !model.multiInGame.getGameFinish())
					model.multiInGame.pressGhostMove(code);
				break;
			case SPACE:
			case ENTER:
				if (!model.multiInGame.getCountdownFlag() && !model.multiInGame.getPauseFlag() && !model.multiInGame.getGameFinish())
					model.multiInGame.useAbility();
				break;
			case P:
				model.multiInGame.setPause(!model.multiInGame.getPauseFlag());
				model.multiInGame.releaseMove(code);
				break;
			default:
				break;
			}
			break;
		}
	}
	
	public void takeKeyReleased(KeyCode code) {
		switch(gState) {
		case SINGLE_IN_GAME:
			switch(code) {
			case UP:
			case DOWN:
			case RIGHT:
			case LEFT:
				if (!model.singleInGame.getCountdownFlag() && !model.singleInGame.getPauseFlag())
					model.singleInGame.releaseMove(code);
				break;
			case PAGE_DOWN:
				model.singleInGame.setTimerTo_0();
				break;
			default:
				break;
			}
			break;
		case MULTI_IN_GAME:
			switch(code) {
			case UP:
			case DOWN:
			case RIGHT:
			case LEFT:
				if (!model.multiInGame.getCountdownFlag() && !model.multiInGame.getPauseFlag())
					model.multiInGame.releaseMove(code);
			case W:
			case A:
			case S:
			case D:
				if (!model.multiInGame.getCountdownFlag() && !model.multiInGame.getPauseFlag())
					model.multiInGame.releaseGhostMove(code);
				break;
			case PAGE_DOWN:
				model.multiInGame.setTimerTo_0();
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		
	}
	
	public void gameUpdate() {
		switch(gState) {
		case START:
			model.start.update();
			break;
		case SINGLE_STAGE_SEL:
			model.singleStageSel.update();
			break;
		case SINGLE_IN_GAME:
			if (!model.singleInGame.getCountdownFlag() && !model.singleInGame.getPauseFlag())
				model.singleInGame.update();
			break;
		case MULTI_SEL:
			model.multiSelect.showSelected();
			break;
		case MULTI_IN_GAME:
			if (!model.multiInGame.getCountdownFlag() && !model.multiInGame.getPauseFlag())
				model.multiInGame.update();
			break;
		}
		view.draw(model.getObjects());
		view.write(model.getTexts());
	}
	
	//if there is State change we initialise them
	private void initState() {
		switch(gState) {
		case START:
			model.deleteStates();
			view.setColour(Colour.WHITE);
			model.initStart();
			break;
		case SINGLE_STAGE_SEL:
			model.deleteStates();
			view.setColour(Colour.BLACK);
			model.initSingleStageSelect();
			break;
		case SINGLE_IN_GAME:
			int world = model.singleStageSel.getWorld();
			int stage = model.singleStageSel.getStage();
			model.deleteStates();
			view.setColour(Colour.WHITE);
			model.initSingleInGame(world, stage);
			break;
		case STORY:
			model.deleteStates();
			view.setColour(Colour.BLACK);
			model.initStory();
			break;
		case MULTI_SEL:
			model.deleteStates();
			view.setColour(Colour.BLACK);
			model.initMultiSelect();

			break;
		case MULTI_IN_GAME:
			Random rand = new Random();
			world = rand.nextInt(4);
			stage = 4;
			model.deleteStates();
			view.setColour(Colour.WHITE);
			model.initMultiInGame(world, stage);

			break;

		}
	}
	
	GameController(GraphicsContext graphic, Stage stage) {
		this.stage = stage;
		view = new GameViewer(graphic);
		model = new GameModelHandler();
		gState = State.START;
		initState();
	}
	
	
}
