package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
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
	
	static enum State {
		START,
		SINGLE_STAGE_SEL,
		SINGLE_IN_GAME,
		MULTI_SEL,
		MULTI_IN_GAME
	};
	
	private Stage stage;
	
	private GameViewer view;
	private GameModelHandler model;
	private State gState;
	private Level level;
	
	public void takeMouseClicked(double x, double y) {
		switch(gState) {
		case START:
			if (model.start.selectMouse(x, y) == 1) {
				gState = State.SINGLE_STAGE_SEL;
				initState();
			} else if (model.start.selectMouse(x, y) == 2) {
				gState = State.MULTI_SEL;
				initState();
			}
			break;
		case SINGLE_STAGE_SEL:
					
			
			if (model.singleStageSel.getSel()) {
				
				switch(model.singleStageSel.selectMouse_stage(x, y)) {
				case -1:
					break;
				case -2:
					gState = State.START;
					initState();
					break;
				case -3:
					model.singleStageSel.hideStages();
					break;
				default:
					model.singleStageSel.setStage(model.singleStageSel.selectMouse_stage(x, y));
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
					gState = State.START;
					initState();
					break;
				default:	
					break;
				}
			}
			break;
		case SINGLE_IN_GAME:
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
		case MULTI_SEL:

			switch(model.multiSelect.selectMouse_ghostTeam(x, y)) {
				case -1:
					break;
				case -2:
					gState = State.START;
					initState();
					break;
				default:
					model.multiSelect.setGhostTeam(model.multiSelect.selectMouse_ghostTeam(x, y));
					gState = State.MULTI_IN_GAME;
					initState();
					break;	
			}
			break;
		case MULTI_IN_GAME:
			break;
		}
	}
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
			break;
		case MULTI_IN_GAME:
			break;
		}
	}
	public void takeKeyPressed(KeyCode code) {
		switch(gState) {
		case START:
			switch (code) {
			case ESCAPE:
				stage.fireEvent(
                        new WindowEvent(
                                stage,
                                WindowEvent.WINDOW_CLOSE_REQUEST
                        )
                );
				break;
			case UP:
				model.start.selectUp();
				break;
			case DOWN:
				model.start.selectDown();
				break;
			case ENTER:
				int sel = model.start.getSelected();
				if (sel == 1) {
					gState = State.SINGLE_STAGE_SEL;
					initState();
				} else if (sel == 2) {
					gState = State.MULTI_SEL;
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
		case MULTI_SEL:
			switch(code) {
			case ESCAPE:
				gState = State.START;
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
			break;
		case MULTI_IN_GAME:
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
		case MULTI_SEL:
			model.deleteStates();
			view.setColour(Colour.BLACK);
			model.initMultiSelect();

			break;
		case MULTI_IN_GAME:
			model.deleteStates();
			view.setColour(Colour.WHITE);

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
