package main;

import java.util.Vector;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root);
        Canvas canvas = new Canvas(1920, 1080);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        App testApp = new App(gc);
        testApp.setup();

        new AnimationTimer() {
            public void handle(long now) {
                testApp.draw();
            }
        }.start();

        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				testApp.setMousePos(event.getSceneX(),event.getSceneY());
			}
		});

        stage.setScene(scene);
		stage.show();
    }
    
}