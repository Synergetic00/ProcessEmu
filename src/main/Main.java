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

import utils.FXUtils;
import utils.PVector;
import static utils.FXUtils.*;

@SuppressWarnings("unused")
public class Main extends Application {

	public static Group root;
	public static Scene scene;
	public static Canvas canvas;
	public static GraphicsContext g;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		stage.setTitle("RaspberryPiFX");
        root = new Group();
        scene = new Scene(root, 1280, 720, Color.LIGHTGRAY);
        canvas = new Canvas(scene.getWidth(), scene.getHeight());
        root.getChildren().add(canvas);
		g = canvas.getGraphicsContext2D();

		App app = new App(Group root)

		new AnimationTimer(){
			public void handle(long now) {

			}
		}.start();

		stage.setScene(scene);
		stage.show();

    }
}