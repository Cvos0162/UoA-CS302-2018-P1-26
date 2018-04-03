package main;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	private BorderPane rootLayout;
	private Stage primaryStage;
	
	public void init(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Planet Escape (Java Gaming - InJae)");

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(IOException e) {
			e.printStackTrace();
		}
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

		Canvas canvas = new Canvas(1280, 720);
		GraphicsContext graphic = canvas.getGraphicsContext2D();
		GameController control = new GameController(graphic);
		rootLayout.getChildren().add(canvas);
		
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