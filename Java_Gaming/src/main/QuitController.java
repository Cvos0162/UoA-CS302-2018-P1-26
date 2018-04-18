package main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class QuitController {
	//init connector
	WindowEvent e;
	Stage s;
	//init buttons
	@FXML
	private Button yesButton;
	@FXML
	private Button noButton;
	
	//no button press
	@FXML
	private void pressNo() {
		e.consume();
		s.close();
	}
	//yes button press
	@FXML
	private void pressYes() {
		System.exit(0);
		Platform.exit();
		s.close();
	}
	
	//setter (connection of stage and event)
	public void setEvent(WindowEvent e) {
		this.e = e;
	}
	public void setStage(Stage s) {
		this.s = s;
	}
}
