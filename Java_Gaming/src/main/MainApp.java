package main;

import java.util.Optional;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApp extends Application {
	
	private Group root;
	private Canvas canvas;
	private Stage primaryStage;
	private GameController control;
	private GraphicsContext graphic;
	
	public void init(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Planet Escape (Java Gaming - InJae)");

		root = new Group();
		
		canvas = new Canvas();
		graphic = canvas.getGraphicsContext2D();
		control = new GameController(graphic, this.primaryStage);
		root.getChildren().add(canvas);
		canvas.widthProperty().bind(this.primaryStage.widthProperty());
		canvas.heightProperty().bind(this.primaryStage.heightProperty());
		Scene scene = new Scene(root);
		this.primaryStage.setScene(scene);
		this.primaryStage.setWidth(1280);
		this.primaryStage.setHeight(720);
		this.primaryStage.setResizable(false);
		this.primaryStage.show();
		this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
		    public void handle(WindowEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Quit Confirmation");
				alert.setHeaderText("You are about to quit");
				alert.setContentText("Do you really want to quit?");
				alert.initModality(Modality.APPLICATION_MODAL);
				alert.initOwner(primaryStage);
				
				ButtonType yes = new ButtonType("Yes", ButtonData.YES);
				ButtonType no = new ButtonType("No", ButtonData.NO);

				alert.getButtonTypes().setAll(yes, no);
				
				Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == no) {
						event.consume();
					}
		        }
			});
	}
	
	public void initEvent(Canvas canvas, GameController control) {
		canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		    public void handle(MouseEvent event) {
		        System.out.println("Handling event " + event.getEventType()
		        		+ "\tMouse Position x: "+ event.getX() + "y: " + event.getY());
		        control.takeMouseClicked(event.getX(),event.getY());
		        event.consume();
		    }
		    });
		canvas.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
		    public void handle(MouseEvent event) {
		        control.takeMouseMoved(event.getX(),event.getY());
		        event.consume();
		    }
		    });
		canvas.setFocusTraversable(true);
		canvas.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
		    public void handle(KeyEvent event) {
		        System.out.println("Handling event " + event.getEventType()
		        		+ "\tKey: " + event.getText());
		        control.takeKeyInput(event.getCode());
		        event.consume();
		    }
		    });
		
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		init(primaryStage);
		
		initEvent(canvas, control);
		
		AnimationTimer gameLoop = new AnimationTimer() {
			
			@Override
			public void handle(long now) {	
				
				control.gameUpdate();
			}
		};
		gameLoop.start();
	}

	public static void main(String[] args) {
		launch(args);
	}
}