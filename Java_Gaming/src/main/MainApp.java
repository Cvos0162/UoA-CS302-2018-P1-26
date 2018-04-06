package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
		control = new GameController(graphic);
		root.getChildren().add(canvas);
		canvas.widthProperty().bind(primaryStage.widthProperty());
		canvas.heightProperty().bind(primaryStage.heightProperty());
		Scene scene = new Scene(root);
		this.primaryStage.setScene(scene);
		this.primaryStage.setWidth(1280);
		this.primaryStage.setHeight(720);
		this.primaryStage.setResizable(false);
		this.primaryStage.show();
	}
	
	public void initEvent(Canvas canvas, GameController control) {
		canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		    public void handle(MouseEvent event) {
		        System.out.println("Handling event " + event.getEventType()
		        		+ "\tMouse Position x: "+ event.getX() + "y: " + event.getY());
		        control.takeMouseInput(event.getX(),event.getY());
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