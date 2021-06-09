package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    public static ArrayList<AppEntry> apps;
    public static GraphicsContext gc;
    public static Stage stage;
    public static int appIndex;

    @Override
    public void start(Stage stage) throws Exception {
        Constants.screenW(1280);
        Constants.screenH(720);

        Main.stage = stage;
        Group root = new Group();
        Scene scene = new Scene(root, Color.BLACK);
        Canvas canvas = new Canvas(Constants.screenW(), Constants.screenH());
        root.getChildren().add(canvas);
        Main.gc = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(event -> { Loader.handleKeyPressed(event); });
        scene.setOnKeyReleased(event -> { Loader.handleKeyReleased(event); });
        scene.setOnKeyTyped(event -> { Loader.handleKeyTyped(event); });
        scene.setOnMouseClicked(event -> { Loader.handleMouseClicked(event); });
        scene.setOnMouseDragged(event -> { Loader.handleMouseDragged(event); });
        scene.setOnMouseMoved(event -> { Loader.handleMouseMoved(event); });
        scene.setOnMousePressed(event -> { Loader.handleMousePressed(event); });
        scene.setOnMouseReleased(event -> { Loader.handleMouseReleased(event); });
        scene.setOnScroll(event -> { Loader.handleMouseWheel(event); });

        new AnimationTimer() { public void handle(long now) { updateApplication(); }}.start();

        Main.stage.setOnCloseRequest(value -> closeApplication());
        Main.stage.setTitle("RaspberryPiFX v4");
        Main.stage.getIcons().add(new Image("file:RPFXLogo.png"));
        Main.stage.setScene(scene);
        Main.stage.setResizable(false);
        Main.stage.show();

        /*try {
            URL iconURL = new File("RPFXLogo.png").toURI().toURL();
            java.awt.Image image = new ImageIcon(iconURL).getImage();
            com.apple.eawt.Application.getApplication().setDockIconImage(image);
        } catch (Exception e) {}*/

        Main.apps = new ArrayList<AppEntry>();
        
        try {
            Loader.searchFolder(new File("src/sketches"));
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