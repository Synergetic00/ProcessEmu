package main;

import java.io.ByteArrayOutputStream;
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

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

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

    static int currentAppIndex;
    static boolean onHomeScreen = true;

    static ArrayList<App> apps = new ArrayList<App>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root);
        Canvas canvas = new Canvas(1440, 900);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();

        currentAppIndex = 0;

        apps.add(new App("Bounce", "Processing", "Bouncing ball without acceleration"));
        apps.add(new App("Collision", "Processing", "A physics simulation"));
        apps.add(new App("BoxCarrier", "Elise", "A game of infinite haulage"));
        apps.add(new App("Keyboard", "Processing", "Is a key being pressed"));

        drawDefaultApp(gc);

        /*
         * scene.setOnMousePressed(new EventHandler<MouseEvent>() {
         * 
         * @Override public void handle(MouseEvent event) { if (event.getButton() ==
         * MouseButton.PRIMARY) { try { apps.get(0).execute(); } catch (Exception e) {}
         * } if (event.getButton() == MouseButton.SECONDARY) { try {
         * apps.get(1).execute(); } catch (Exception e) {} } } });
         */

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
                            apps.get(currentAppIndex).execute();
                        } catch (Exception e) {
                        }
                    }
                    break;
                }

                case UP: {
                    if (onHomeScreen) {
                        currentAppIndex--;
                    }
                    break;
                }

                case DOWN: {
                    if (onHomeScreen) {
                        currentAppIndex++;
                    }
                    break;
                }

                case ENTER: {
                    if (onHomeScreen) {
                        try {
                            onHomeScreen = false;
                            apps.get(currentAppIndex).execute();
                        } catch (Exception e) {
                        }
                    }
                    break;
                }

                default: {
                    break;
                }

            }

            if (currentAppIndex < 0) {
                currentAppIndex = apps.size() - 1;
            }

            if (currentAppIndex >= apps.size()) {
                currentAppIndex = 0;
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
        int spacing = 20, toDisplay = 5;
        int height = (int) gc.getCanvas().getHeight();
        gc.save();
        FXApp defaultApp = new FXApp(gc);
        defaultApp.fullScreen();
        defaultApp.background(20);
        double rectH = (height - (spacing * (toDisplay+1)))/toDisplay;
        for (int i = 0; i < apps.size(); i++) {
            double yPos = i*(rectH+spacing)+spacing;
            defaultApp.fill(0);
            defaultApp.strokeWeight(5);
            if (i == currentAppIndex) {
                defaultApp.stroke(255,0,0);
            } else {
                defaultApp.stroke(255);
            }
            defaultApp.rect(200, yPos, defaultApp.width - 400, rectH);
            if (i == currentAppIndex) {
                defaultApp.fill(255,0,0);
            } else {
                defaultApp.fill(255);
            }
            defaultApp.textSize(35);
            defaultApp.text(apps.get(i).appName, 230, yPos+50);
            defaultApp.textSize(20);
            defaultApp.text(apps.get(i).appAuthor, 230, yPos+90);
            defaultApp.textSize(25);
            defaultApp.text(apps.get(i).appDesc, 230, yPos+130);
            //gc.fillText(apps.get(i).appName, 150, yPos+20);
            //gc.fillText(apps.get(i).appAuthor, 150, yPos+50);
        }
        gc.restore();
    }

    public static void executeProgram(String name, GraphicsContext gc) throws Exception {
        runFile("src/programs/"+name+".pde", gc);
        setupMethod = programClass.getMethod("handleSetup");
        setupMethod.invoke(programObject);
        drawMethod = programClass.getMethod("handleDraw");
        keyPressedMethod = programClass.getMethod("handleKeyPressed", KeyEvent.class);
        keyReleasedMethod = programClass.getMethod("handleKeyReleased", KeyEvent.class);
        keyTypedMethod = programClass.getMethod("handleKeyTyped", KeyEvent.class);
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private static void runFile(String path, GraphicsContext gc) throws Exception {
        String program = "";
        program += readFile("src/utils/Opening.txt", Charset.forName("UTF-8"));
        program += readFile(path, Charset.forName("UTF-8"));
        program += "}";

        program = program.replace("void setup()", "public void setup()");
        program = program.replace("void draw()", "public void draw()");
        program = program.replace("float", "double");

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaFileObject compilationUnit = new StringJavaFileObject("ProcessingApp", program);
        SimpleJavaFileManager fileManager = new SimpleJavaFileManager(compiler.getStandardFileManager(null, null, null));
        JavaCompiler.CompilationTask compilationTask = compiler.getTask(null, fileManager, null, null, null, Arrays.asList(compilationUnit));
        compilationTask.call();
        CompiledClassLoader classLoader = new CompiledClassLoader(fileManager.getGeneratedOutputFiles());

        programClass = classLoader.loadClass("programs.ProcessingApp");
        programConstructor = programClass.getConstructor(GraphicsContext.class);
        programObject = programConstructor.newInstance(gc);
    }

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