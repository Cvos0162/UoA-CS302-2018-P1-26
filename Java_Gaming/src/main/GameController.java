package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import main.model.GameModelHandler;
import main.view.GameViewer;

public class GameController {
	
	static enum State {
		START,
		SINGLE_STAGE_SEL,
		SINGLE_IN_GAME,
		EXIT
	};
	
	private GameViewer view;
	private GameModelHandler model;
	private State gState;
	
	public void takeMouseInput(double x, double y) {
		switch(gState) {
		case START:
			if (model.start.selectMouse(x, y) == 1) {
				gState = State.SINGLE_STAGE_SEL;
				initState();
			}
			break;
		case SINGLE_STAGE_SEL:
			switch(model.singleStageSel.selectMouse(x, y)) {
			case 1:
				model.singleStageSel.setWorld(1);
				break;
			case 2:
				model.singleStageSel.setWorld(2);
				break;
			case 3:
				model.singleStageSel.setWorld(3);
				break;
			case 4:
				model.singleStageSel.setWorld(4);
				break;			
			case 5:
				gState = State.START;
				initState();
				break;
			}
			break;
		case SINGLE_IN_GAME:
			break;
		case EXIT:
			break;
		}
	}
	public void takeKeyInput(KeyCode code) {
		switch(gState) {
		case START:
			switch (code) {
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
				}
				break;
			default:
				break;
			}
			break;
		case SINGLE_STAGE_SEL:
			switch(code) {
			case ESCAPE:
				gState = State.START;
				initState();
			default:
				break;
			}
			break;
		case SINGLE_IN_GAME:
			break;
		case EXIT:
			break;
		}
	}
	
	public void gameUpdate() {
		switch(gState) {
		case START:
			model.start.update();
			break;
		case SINGLE_STAGE_SEL:
			break;
		case SINGLE_IN_GAME:
			break;
		case EXIT:
			break;
		}
		view.draw(model.getObjects());
	}
	
	//if there is State change we initialise them
	private void initState() {
		switch(gState) {
		case START:
			model.initStart();
			break;
		case SINGLE_STAGE_SEL:
			model.initSingleStageSelect();
			break;
		case SINGLE_IN_GAME:
			break;
		case EXIT:
			break;

		}
	}
	
	GameController(GraphicsContext graphic) {
		view = new GameViewer(graphic);
		model = new GameModelHandler();
		gState = State.START;
		initState();
	}
	
	
}
