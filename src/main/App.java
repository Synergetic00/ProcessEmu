package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.*;

import utils.PVector;
import static utils.FXUtils.*;

@SuppressWarnings("unused")
public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		stage.setTitle("RaspberryPiFX");
        Group root = new Group();
        Scene scene = new Scene(root, 1280, 720, Color.LIGHTGRAY);
        Canvas canvas = new Canvas(scene.getWidth(), scene.getHeight());
        root.getChildren().add(canvas);
		GraphicsContext g = canvas.getGraphicsContext2D();
		
		PVector p = new PVector(30,10);
		System.out.print(Arrays.toString(p.array()));

		scene.setOnKeyTyped(event -> {
			char keyPressed = event.getCharacter().charAt(0);
		});

		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

			}
		});

		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
			}
		});

		new AnimationTimer(){
			public void handle(long now) {

			}
		}.start();

		stage.setScene(scene);
		stage.show();

    }
}