package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class QuitController {
	WindowEvent e;
	Stage s;
	@FXML
	private Button yesButton;
	@FXML
	private Button noButton;
	
	@FXML
	private void pressNo() {
		e.consume();
		s.close();
	}
	@FXML
	private void pressYes() {
		s.close();
	}
	public void setEvent(WindowEvent e) {
		this.e = e;
	}
	public void setStage(Stage s) {
		this.s = s;
	}
}
