package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import main.DynLoader.CompiledClassLoader;
import main.DynLoader.SimpleJavaFileManager;
import main.DynLoader.StringJavaFileObject;

public class Main extends Application {

    public static GraphicsContext gc;
    public static ArrayList<App> apps;
    public static Stage stageObj;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stageObj = stage;
        Group root = new Group();
        Scene scene = new Scene(root);
        Canvas canvas = new Canvas(1280, 720);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();
        apps = new ArrayList<App>();

        loadFolder(new File("src/programs"));

        apps.get(0).launch();

        // Call the draw method once every frame
        new AnimationTimer() {
            public void handle(long now) {
                try {
                    if (drawMethod != null) {
                        drawMethod.invoke(programObject);
                    }
                } catch (Exception e) {
                }
            }
        }.start();

        stage.setOnCloseRequest(value -> {
            System.exit(0);
        });
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // Load the processing code into a string of java code to be run
    public static String loadFile(String path) throws IOException {

        // Read the contents of the desired file into a complete java 'file'
        byte[] header = Files.readAllBytes(Paths.get("src/main/Header.txt"));
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        Charset charset = Charset.forName("UTF-8");
        String program = new String(header, charset).concat(new String(bytes, charset)).concat("\n\n}");

        // Change all the incompatiable code from Processing
        program = program.replace("public void ", "void ");
        program = program.replace("void ", "public void ");
        program = program.replace("float", "double");
        return program;

    }

    // Read the processing files from folder and add to an array list
    public static void loadFolder(final File folderPath) throws IOException {
        for (final File fileEntry : folderPath.listFiles()) {
            if (fileEntry.isDirectory()) {
                loadFolder(fileEntry);
            } else {
                String fileName = fileEntry.getName();
                loadApp(fileName);
            }
        }
    }

    // Load the apps metadata into array
    public static void loadApp(String appName) throws IOException {
        FileReader fr = new FileReader("src/programs/" + appName);
        BufferedReader br = new BufferedReader(fr);
        String appTitle = appName.substring(0, appName.length() - 4);
        String appAuthour = br.readLine().substring(2);
        String appDescription = br.readLine().substring(2);
        br.close();
        fr.close();
        apps.add(new App(appTitle, appAuthour, appDescription));
    }

    // Take the string and run the compiled program and setup the methods

    // Java object variables
    static Class<?> programClass;
    static Constructor<?> programConstructor;
    static Object programObject;

    // Rendering methods
    static Method settingsMethod;
    static Method setupMethod;
    static Method drawMethod;

    // Keyboard input methods
    static Method keyPressedMethod;
    static Method keyReleasedMethod;
    static Method keyTypedMethod;

    // Mouse input methods
    static Method mouseClickedMethod;
    static Method mouseDraggedMethod;
    static Method mouseMovedMethod;
    static Method mousePressedMethod;
    static Method mouseReleasedMethod;
    static Method mouseWheelMethod;

    public static void loadProgram(String path) throws Exception {

        // Grab our program as a string
        String program = loadFile(path);

        // Use boilerplate code to compile the string
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaFileObject compilationUnit = new StringJavaFileObject("ProcessingApp", program);
        SimpleJavaFileManager fileManager = new SimpleJavaFileManager(
                compiler.getStandardFileManager(null, null, null));
        CompilationTask compilationTask = compiler.getTask(null, fileManager, null, null, null,
                Arrays.asList(compilationUnit));
        compilationTask.call();
        CompiledClassLoader classLoader = new CompiledClassLoader(fileManager.getGeneratedOutputFiles());

        // Set other Java class parameters and create an instance
        programClass = classLoader.loadClass("programs.ProcessingApp");
        programConstructor = programClass.getConstructor(GraphicsContext.class);
        programObject = programConstructor.newInstance(gc);

        // Setup the methods to be used from the scripts
        settingsMethod      = programClass.getMethod("handleSettings");
        setupMethod         = programClass.getMethod("handleSetup");
        drawMethod          = programClass.getMethod("handleDraw");
        keyPressedMethod    = programClass.getMethod("handleKeyPressed", KeyEvent.class);
        keyReleasedMethod   = programClass.getMethod("handleKeyReleased", KeyEvent.class);
        keyTypedMethod      = programClass.getMethod("handleKeyTyped", KeyEvent.class);
        mouseClickedMethod  = programClass.getMethod("handleMouseClicked", MouseEvent.class);
        mouseDraggedMethod  = programClass.getMethod("handleMouseDragged", MouseEvent.class);
        mouseMovedMethod    = programClass.getMethod("handleMouseMoved", MouseEvent.class);
        mousePressedMethod  = programClass.getMethod("handleMousePressed", MouseEvent.class);
        mouseReleasedMethod = programClass.getMethod("handleMouseReleased", MouseEvent.class);
        mouseWheelMethod    = programClass.getMethod("handleMouseWheel", ScrollEvent.class);

        // Run the settings and the setup methods
        settingsMethod.invoke(programObject);
        setupMethod.invoke(programObject);

    }

}

class App {

    String title;
    String authour;
    String description;

    public App(String t, String a, String d) {
        title = t;
        authour = a;
        description = d;
    }

    public void launch() throws Exception {
        Main.loadProgram("src/programs/"+title+".pde");
    }

}