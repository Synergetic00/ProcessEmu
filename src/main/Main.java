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
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;
import main.DynLoader.CompiledClassLoader;
import main.DynLoader.SimpleJavaFileManager;
import main.DynLoader.StringJavaFileObject;

import static utils.Constants.*;

public class Main extends Application {

    public static GraphicsContext gc;
    public static ArrayList<App> apps;
    public static Stage stageObj;

    static int appIndex;
    static boolean onHomeScreen = true;
    FXApp dApp;

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
        System.out.println("Loaded "+apps.size()+" apps");

        appIndex = 0;
        dApp = new FXApp(gc);
        launchHomeScreen();
        drawDefaultApp(gc);

        //apps.get(0).launch();

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                try {
                    mouseClickedMethod.invoke(programObject, event);
                } catch (Exception e) {}
            }
        });

        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                try {
                    mouseDraggedMethod.invoke(programObject, event);
                } catch (Exception e) {}
            }
        });

        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                try {
                    mouseMovedMethod.invoke(programObject, event);
                } catch (Exception e) {}
            }
        });

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                try {
                    mousePressedMethod.invoke(programObject, event);
                } catch (Exception e) {}
            }
        });

        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                try {
                    mouseReleasedMethod.invoke(programObject, event);
                } catch (Exception e) {}
            }
        });

        scene.setOnScroll(new EventHandler<ScrollEvent>() {
            public void handle(ScrollEvent event) {
                try {
                    mouseWheelMethod.invoke(programObject, event);
                } catch (Exception e) {}
            }
        });

        scene.setOnKeyPressed(event -> {

            try {
                keyPressedMethod.invoke(programObject, event);
            } catch (Exception e) {}

            KeyCode keyCode = event.getCode();
            switch (keyCode) {
                case Q: {
                    if (!onHomeScreen) {
                        onHomeScreen = true;
                        launchHomeScreen();
                    }
                    break;
                }

                case R: {
                    if (!onHomeScreen) {
                        try {
                            apps.get(appIndex).launch(gc);
                        } catch (Exception e) {
                        }
                    }
                    break;
                }

                case UP: {
                    if (onHomeScreen) {
                        appIndex--;
                    }
                    break;
                }

                case DOWN: {
                    if (onHomeScreen) {
                        appIndex++;
                    }
                    break;
                }

                case ENTER: {
                    if (onHomeScreen) {
                        try {
                            onHomeScreen = false;
                            apps.get(appIndex).launch(gc);
                        } catch (Exception e) {
                        }
                    }
                    break;
                }

                default: {
                    break;
                }

            }

            if (appIndex < 0) {
                appIndex = apps.size() - 1;
            }

            if (appIndex >= apps.size()) {
                appIndex = 0;
            }
        });

        scene.setOnKeyReleased(event -> {
            try {
                keyReleasedMethod.invoke(programObject, event);
            } catch (Exception e) {}
        });

        scene.setOnKeyTyped(event -> {
            try {
                keyTypedMethod.invoke(programObject, event);
            } catch (Exception e) {}
        });

        // Call the draw method once every frame
        new AnimationTimer() {
            public void handle(long now) {
                if (onHomeScreen) {
                    drawDefaultApp(gc);
                } else {
                    try {
                        if (drawMethod != null) {
                            drawMethod.invoke(programObject);
                        }
                    } catch (Exception e) {}
                }
            }
        }.start();

        stage.setOnCloseRequest(value -> {
            System.exit(0);
        });
        stage.setTitle("RaspberryPiFX");
        Image icon = new Image("file:RPFXLogo.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void launchHomeScreen() {
        gc.save();
        dApp.fullScreen();
        gc.restore();
    }

    private void drawDefaultApp(GraphicsContext gc) {

        gc.save();
        dApp.g.resetMatrix();
        dApp.updateTime();
        dApp.background(0);
        int dispNum = 5;
        int pageNum = (int) (appIndex / dispNum);
        int pageTtl = (int) Math.ceil(apps.size() / (double) dispNum);
        int tailNum = apps.size() % dispNum;
        int calcNum = apps.size() - (pageNum * dispNum);
        int toRender = (calcNum == tailNum) ? tailNum : dispNum;

        int height = (int) gc.getCanvas().getHeight();
        int spacing = 30;
        double rectH = (height - (spacing * (dispNum+1)))/dispNum;
        double rectX = 600;

        int actualIndex = 0;

        dApp.textAlign(LEFT, CENTER);

        for (int i = 0; i < toRender; i++) {
            actualIndex = i + (pageNum * dispNum);

            double yPos = i*(rectH+spacing)+spacing;
            dApp.fill(0);
            dApp.strokeWeight(5);
            if (i == appIndex % dispNum) {
                dApp.stroke(54, 205, 255);
            } else {
                dApp.stroke(255);
            }
            dApp.rect(rectX, yPos, dApp.width - rectX - spacing, rectH);
            if (i == appIndex % dispNum) {
                dApp.fill(54, 205, 255);
            } else {
                dApp.fill(255);
            }
            dApp.textSize(30);
            dApp.text(apps.get(actualIndex).title, rectX + spacing, yPos+(30));
            dApp.textSize(15);
            dApp.text(apps.get(actualIndex).authour, rectX + spacing, yPos+(rectH/2)+(3));
            dApp.textSize(20);
            dApp.text(apps.get(actualIndex).description, rectX + spacing, yPos+(rectH)-(30));
        }

        for (int i = 0; i < pageTtl; i++) {
            dApp.stroke(255);
            if (i == pageNum) dApp.fill(255);
            else dApp.fill(0);
            dApp.ellipse((rectX/2)-(25*pageTtl)+(50*i)+25, height - 50, 25, 25);
        }

        String day = String.format("%02d", dApp.day());
        String month = String.format("%02d", dApp.month());
        String year = String.format("%04d", dApp.year());
        String hour = String.format("%02d", dApp.hour());
        String minute = String.format("%02d", dApp.minute());
        String second = String.format("%02d", dApp.second());

        String dateNow = day+"/"+month+"/"+year;
        String timeNow = hour+":"+minute+":"+second;

        dApp.fill(255);
        dApp.textSize(30);
        dApp.textAlign(CENTER, CENTER);
        dApp.text(dateNow, (rectX/2), dApp.height - 200);
        dApp.text(timeNow, (rectX/2), dApp.height - 150);

        dApp.fill(54, 205, 255);
        dApp.textSize(70);
        dApp.text("RaspberryPiFX",(rectX/2),100);
        dApp.textSize(40);
        dApp.text("V2.4.7",(rectX/2),200);
        gc.restore();
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
        program = program.replace("Float", "Double");
        program = program.replace("color ", "int ");
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
        SimpleJavaFileManager fileManager = new SimpleJavaFileManager(compiler.getStandardFileManager(null, null, null));
        CompilationTask compilationTask = compiler.getTask(null, fileManager, null, null, null, Arrays.asList(compilationUnit));
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

    public void launch(GraphicsContext gc) throws Exception {
        gc.setTransform(new Affine());
        Main.loadProgram("src/programs/"+title+".pde");
    }

}