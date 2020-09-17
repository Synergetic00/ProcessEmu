package main;

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
            writer.write("package programs;\n\n");
            writer.write("import javafx.scene.canvas.GraphicsContext;\n");
            writer.write("import main.FXApp;\n");
            writer.write("public class Test extends FXApp{\n");
            writer.write("public Test(GraphicsContext gc) { super(gc); }\n");
            String reader="";
            System.out.println("start");
            while((reader=(br.readLine()))!=null){
                reader.replace("void setup()", "public void setup()");
                reader.replace("void draw()", "public void draw()");
                writer.write(reader+"\n");
            }
            writer.write("\n}");
    
            br.close();
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        //System.out.println(compiler);
        int b = compiler.run(null, null, null,"src/programs/Test.java");

        Test testme=new Test(gc);
        testme.setup();
        //System.out.println(compiler);
        b = compiler.run(null, null, null,"src/programs/Test.java");

        Test test2=new Test(gc);
        test2.setup();

        new AnimationTimer() {
            public void handle(long now) {
                test2.draw();
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