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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Main extends Application {

    public static ArrayList<AppEntry> apps;
    public static GraphicsContext gc;
    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        Main.stage = stage;
        Group root = new Group();
        Scene scene = new Scene(root, Color.BLACK);
        Canvas canvas = new Canvas(1280, 720);
        root.getChildren().add(canvas);
        Main.gc = canvas.getGraphicsContext2D();

        new AnimationTimer() { public void handle(long now) { updateApplication(); }}.start();

        Main.stage.setOnCloseRequest(value -> closeApplication());
        Main.stage.setTitle("RaspberryPiFX v4");
        Main.stage.getIcons().add(new Image("file:RPFXLogo.png"));
        Main.stage.setScene(scene);
        Main.stage.setResizable(false);
        Main.stage.show();

        try {
            URL iconURL = new File("RPFXLogo.png").toURI().toURL();
            java.awt.Image image = new ImageIcon(iconURL).getImage();
            com.apple.eawt.Application.getApplication().setDockIconImage(image);
        } catch (Exception e) {
            System.out.println("Not running MacOS");
        }

        Main.apps = new ArrayList<AppEntry>();
        
        try {
            Loader.searchFolder(new File("src/sketches"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            apps.get(0).launch();
        } catch (Exception e) {
            e.printStackTrace();
        }
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