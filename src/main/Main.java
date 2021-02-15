package main;

import static main.DynLoader.*;
import static utils.Constants.*;

import java.io.File;
import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;

public class Main extends Application {

    public static GraphicsContext gc;
    public static ArrayList<App> apps;
    public static Stage stageObj;
    public static Animation animation;
    public static float frameRate = 60;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stageObj = stage;
        Group root = new Group();
        Scene scene = new Scene(root, Color.BLACK);
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();
        apps = new ArrayList<App>();
        loadFolder(new File("src/programs"));

        scene.setOnKeyPressed(event -> { handleKeyPressed(event); });
        scene.setOnKeyReleased(event -> { handleKeyReleased(event); });
        scene.setOnKeyTyped(event -> { handleKeyTyped(event); });
        scene.setOnMouseClicked(event -> { handleMouseClicked(event); });
        scene.setOnMouseDragged(event -> { handleMouseDragged(event); });
        scene.setOnMouseMoved(event -> { handleMouseMoved(event); });
        scene.setOnMousePressed(event -> { handleMousePressed(event); });
        scene.setOnMouseReleased(event -> { handleMouseReleased(event); });
        scene.setOnScroll(event -> { handleMouseWheel(event); });

        new AnimationTimer() { public void handle(long now) { handleDraw(); }}.start();

        stage.setOnCloseRequest(value -> { System.exit(0); });
        stage.setTitle("RaspberryPiFX");
        Image icon = new Image("file:RPFXLogo.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}

class App {

    String title, authour, description;

    public App(String t, String a, String d) {
        title = t; authour = a; description = d;
    }

    public void launch(GraphicsContext gc) throws Exception {
        gc.setTransform(new Affine());
        loadProgram("src/programs/"+title+".pde");
    }

}