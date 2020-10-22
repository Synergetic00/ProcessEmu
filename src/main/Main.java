package main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class Main extends Application {

    public static GraphicsContext gc;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root);
        Canvas canvas = new Canvas(1280, 720);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();

        stage.setOnCloseRequest(value -> {
            System.exit(0);
        });
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static String loadFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        Charset charset = Charset.forName("UTF-8");
        String output = new String(bytes, charset);
    }

    public static void loadFolder() {

    }

    public static void loadProgram() {
        
    }

}
