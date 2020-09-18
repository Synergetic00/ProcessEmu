package main;

import java.util.Scanner;
import java.util.Vector;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

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
import programs.Test;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;


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

        //App testApp = new App(gc);
        //testApp.setup();

        try {
            BufferedReader br=new BufferedReader(new FileReader("src/programs/Test.txt"));
            PrintWriter writer=new PrintWriter("src/programs/Test.java");
            writer.write("//Hello\n\n");
            writer.write("package programs;\n\n");
            writer.write("import javafx.scene.canvas.GraphicsContext;\n");
            writer.write("import main.FXApp;\n");
            writer.write("public class Test extends FXApp{\n");
            writer.write("public Test(GraphicsContext gc) { super(gc); }\n");
            String reader="";
            while((reader=(br.readLine()))!=null) {
                writer.write(reader+"\n");
            }
            writer.write("\n}");
    
            br.close();
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        

        new AnimationTimer() {
            public void handle(long now) {
            }
        }.start();

        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//testApp.setMousePos(event.getSceneX(),event.getSceneY());
			}
		});

        stage.setScene(scene);
		stage.show();
    }
    
}