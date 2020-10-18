package main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import utils.FXUtils;

public class Main extends Application {

    public static GraphicsContext gc;

    static Class<?> programClass;
    static Constructor<?> programConstructor;
    static Object programObject;

    static Method setupMethod;
    static Method drawMethod;

    static Method keyPressedMethod;
    static Method keyReleasedMethod;
    static Method keyTypedMethod;

    static Method mouseClickedMethod;
    static Method mouseDraggedMethod;
    static Method mouseMovedMethod;
    static Method mousePressedMethod;
    static Method mouseReleasedMethod;
    static Method mouseWheelMethod;

    static int appIndex;
    static boolean onHomeScreen = true;

    static ArrayList<App> apps = new ArrayList<App>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root);
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        Canvas canvas = new Canvas(screenBounds.getWidth()+1, screenBounds.getHeight()+1);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();

        appIndex = 0;
        final File folder = new File("src/programs");
        readFolder(folder);
        drawDefaultApp(gc);

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
                    }
                    break;
                }

                case R: {
                    if (!onHomeScreen) {
                        try {
                            apps.get(appIndex).execute();
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
                            apps.get(appIndex).execute();
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

        stage.setOnCloseRequest(value -> { System.exit(0); });
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    private void drawDefaultApp(GraphicsContext gc) {

        gc.save();
        FXApp dApp = new FXApp(gc);
        dApp.fullScreen();
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
            dApp.textSize(35);
            dApp.text(apps.get(actualIndex).appName, rectX + spacing, yPos+(35*2));
            dApp.textSize(20);
            dApp.text(apps.get(actualIndex).appAuthor, rectX + spacing, yPos+(rectH/2 + (20/2)));
            dApp.textSize(25);
            dApp.text(apps.get(actualIndex).appDesc, rectX + spacing, yPos+(rectH - (25*2)));
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
        dApp.textAlign(FXUtils.CENTER,FXUtils.CENTER);
        dApp.text(dateNow, (rectX/2), dApp.height - 200);
        dApp.text(timeNow, (rectX/2), dApp.height - 150);

        dApp.fill(54, 205, 255);
        dApp.textSize(70);
        dApp.text("RaspberryPiFX",(rectX/2),100);
        dApp.textSize(40);
        dApp.text("v1.3.6",(rectX/2),200);

        gc.restore();
    }

    // Methods for loading the files into loadable code

    public static void readFolder(final File folderPath) {
        for (final File fileEntry : folderPath.listFiles()) {
            if (fileEntry.isDirectory()) {
                readFolder(fileEntry);
            } else {
                String fileName = fileEntry.getName();
                apps.add(new App(fileName.substring(0, fileName.length()-4), "An author", "A description"));
            }
        }
    }

    public static void executeProgram(String name, GraphicsContext gc) throws Exception {
        runFile("src/programs/"+name+".pde", gc);
        setupMethod = programClass.getMethod("handleSetup");
        drawMethod = programClass.getMethod("handleDraw");

        keyPressedMethod = programClass.getMethod("handleKeyPressed", KeyEvent.class);
        keyReleasedMethod = programClass.getMethod("handleKeyReleased", KeyEvent.class);
        keyTypedMethod = programClass.getMethod("handleKeyTyped", KeyEvent.class);

        mouseClickedMethod = programClass.getMethod("handleMouseClicked", MouseEvent.class);
        mouseDraggedMethod = programClass.getMethod("handleMouseDragged", MouseEvent.class);
        mouseMovedMethod = programClass.getMethod("handleMouseMoved", MouseEvent.class);
        mousePressedMethod = programClass.getMethod("handleMousePressed", MouseEvent.class);
        mouseReleasedMethod = programClass.getMethod("handleMouseReleased", MouseEvent.class);
        mouseWheelMethod = programClass.getMethod("handleMouseWheel", ScrollEvent.class);
        
        setupMethod.invoke(programObject);
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private static void runFile(String path, GraphicsContext gc) throws Exception {
        String program = loadFile(path);
        program = editProgramString(program);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaFileObject compilationUnit = new StringJavaFileObject("ProcessingApp", program);
        SimpleJavaFileManager fileManager = new SimpleJavaFileManager(compiler.getStandardFileManager(null, null, null));
        CompilationTask compilationTask = compiler.getTask(null, fileManager, null, null, null, Arrays.asList(compilationUnit));
        compilationTask.call();
        CompiledClassLoader classLoader = new CompiledClassLoader(fileManager.getGeneratedOutputFiles());
        programClass = classLoader.loadClass("programs.ProcessingApp");
        programConstructor = programClass.getConstructor(GraphicsContext.class);
        programObject = programConstructor.newInstance(gc);
    }

    public static String loadFile(String path) throws IOException {
        String program = "";
        program += readFile("src/utils/Opening.txt", Charset.forName("UTF-8"));
        program += readFile(path, Charset.forName("UTF-8"));
        program += "}";
        return program;
    }

    public static String editProgramString(String program) {
        program = program.replace("public void ", "void ");
        program = program.replace("void ", "public void ");
        program = program.replace("float", "double");
        return program;
    }
    
    // Dynamic Java code class loader boilerplate

    private static class StringJavaFileObject extends SimpleJavaFileObject {
        private final String code;

        public StringJavaFileObject(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension),Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return code;
        }
    }

    private static class ClassJavaFileObject extends SimpleJavaFileObject {
        private final ByteArrayOutputStream outputStream;
        private final String className;

        protected ClassJavaFileObject(String className, Kind kind) {
            super(URI.create("mem:///" + className.replace('.', '/') + kind.extension), kind);
            this.className = className;
            outputStream = new ByteArrayOutputStream();
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            return outputStream;
        }

        public byte[] getBytes() {
            return outputStream.toByteArray();
        }

        public String getClassName() {
            return className;
        }
    }

    private static class SimpleJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
        private final List<ClassJavaFileObject> outputFiles;

        protected SimpleJavaFileManager(JavaFileManager fileManager) {
            super(fileManager);
            outputFiles = new ArrayList<ClassJavaFileObject> ();
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            ClassJavaFileObject file = new ClassJavaFileObject(className, kind);
            outputFiles.add(file);
            return file;
        }

        public List<ClassJavaFileObject> getGeneratedOutputFiles() {
            return outputFiles;
        }
    }

    private static class CompiledClassLoader extends ClassLoader {
        private final List<ClassJavaFileObject> files;

        private CompiledClassLoader(List<ClassJavaFileObject> files) {
            this.files = files;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            Iterator <ClassJavaFileObject> itr = files.iterator();
            while (itr.hasNext()) {
                ClassJavaFileObject file = itr.next();
                if (file.getClassName().equals(name)) {
                    itr.remove();
                    byte[] bytes = file.getBytes();
                    return super.defineClass(name, bytes, 0, bytes.length);
                }
            }
            return super.findClass(name);
        }
    }
}