package main;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    public static ArrayList<AppEntry> apps;
    public static GraphicsContext gc;
    public static Canvas canvas;
    public static Animation animation;
    public static Stage stage;
    public static int appIndex;
    public static String title;
    public static String version;
    public static boolean scaled;

    @Override
    public void start(Stage stage) throws Exception {
        Constants.displayW((int) Screen.getPrimary().getBounds().getWidth());
        Constants.displayH((int) Screen.getPrimary().getBounds().getHeight());
        Constants.screenW(1280);
        Constants.screenH(720);

        Group root = new Group();
        Scene scene = new Scene(root, Color.BLACK);
        canvas = new Canvas(Constants.screenW(), Constants.screenH());
        root.getChildren().add(canvas);

        Main.title = "RaspberryPiFX";
        Main.version = "v4.1.4";
        Main.stage = stage;
        Main.gc = canvas.getGraphicsContext2D();

        //ctrl r reload
        //ctrl f scale app view

        scene.setOnKeyPressed(event -> { Loader.handleKeyPressed(event); });
        scene.setOnKeyReleased(event -> { Loader.handleKeyReleased(event); });
        scene.setOnKeyTyped(event -> { Loader.handleKeyTyped(event); });
        scene.setOnMouseClicked(event -> { Loader.handleMouseClicked(event); });
        scene.setOnMouseDragged(event -> { Loader.handleMouseDragged(event); });
        scene.setOnMouseMoved(event -> { Loader.handleMouseMoved(event); });
        scene.setOnMousePressed(event -> { Loader.handleMousePressed(event); });
        scene.setOnMouseReleased(event -> { Loader.handleMouseReleased(event); });
        scene.setOnScroll(event -> { Loader.handleMouseWheel(event); });

        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                updateApplication();
            }
        });

        animation = new Timeline(keyFrame);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.setRate(-60);
        animation.play();

        //new AnimationTimer() { public void handle(long now) { updateApplication(); }}.start();

        Main.stage.setOnCloseRequest(value -> closeApplication());
        Main.stage.setTitle(Main.title);
        Main.stage.getIcons().add(new Image("file:RPFXLogo.png"));
        Main.stage.setScene(scene);
        Main.stage.setResizable(false);
        Main.stage.show();

        Constants.windowW((int) Main.stage.getWidth());
        Constants.windowH((int) Main.stage.getHeight());

        /*try { // Logo for MacOS Window
            URL iconURL = new File("RPFXLogo.png").toURI().toURL();
            java.awt.Image image = new ImageIcon(iconURL).getImage();
            com.apple.eawt.Application.getApplication().setDockIconImage(image);
        } catch (Exception e) {}*/

        Main.apps = new ArrayList<AppEntry>();
        Main.scaled = false;
        
        try {
            Loader.searchFolder(new File("sketches"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Loader.launchHomeScreen();
    }

    protected void updateApplication() {
        Loader.handleDraw();
    }

    private void closeApplication() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}