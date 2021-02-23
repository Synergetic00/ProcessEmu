package misc;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
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
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import main.*;

public class DynLoader {

    // Take the string and run the compiled program and setup the methods

    // Java object variables
    static Class<?> programClass;
    static Constructor<?> programConstructor;
    static Object programObject;

    // Rendering methods
    static Method settingsMethod;
    static Method setupMethod;
    static Method drawMethod;
    public static void handleDraw() {
        try {
            if (drawMethod != null) {
                drawMethod.invoke(programObject);
            }
        } catch (Exception e) {}
    }

    // Keyboard input methods
    static Method keyPressedMethod;
    public static void handleKeyPressed(KeyEvent event) {
        try {
            keyPressedMethod.invoke(programObject, event);
        } catch (Exception e) {}
    }

    static Method keyReleasedMethod;
    public static void handleKeyReleased(KeyEvent event) {
        try {
            keyReleasedMethod.invoke(programObject, event);
        } catch (Exception e) {}
    }

    static Method keyTypedMethod;
    public static void handleKeyTyped(KeyEvent event) {
        try {
            keyTypedMethod.invoke(programObject, event);
        } catch (Exception e) {}
    }

    // Mouse input methods
    static Method mouseClickedMethod;
    public static void handleMouseClicked(MouseEvent event) {
        try {
            mouseClickedMethod.invoke(programObject, event);
        } catch (Exception e) {}
    }

    static Method mouseDraggedMethod;
    public static void handleMouseDragged(MouseEvent event) {
        try {
            mouseDraggedMethod.invoke(programObject, event);
        } catch (Exception e) {}
    }

    static Method mouseMovedMethod;
    public static void handleMouseMoved(MouseEvent event) {
        try {
            mouseMovedMethod.invoke(programObject, event);
        } catch (Exception e) {}
    }
    
    static Method mousePressedMethod;
    public static void handleMousePressed(MouseEvent event) {
        try {
            mousePressedMethod.invoke(programObject, event);
        } catch (Exception e) {}
    }

    static Method mouseReleasedMethod;
    public static void handleMouseReleased(MouseEvent event) {
        try {
            mouseReleasedMethod.invoke(programObject, event);
        } catch (Exception e) {}
    }

    static Method mouseWheelMethod;
    public static void handleMouseWheel(ScrollEvent event) {
        try {
            mouseWheelMethod.invoke(programObject, event);
        } catch (Exception e) {}
    }
    
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
        programObject = programConstructor.newInstance(Main.gc);

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

    // Load the processing code into a string of java code to be run
    public static String loadFile(String path) throws IOException {

        // Read the contents of the desired file into a complete java 'file'
        byte[] header = Files.readAllBytes(Paths.get("src/misc/Header.txt"));
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        Charset charset = Charset.forName("UTF-8");
        String program = new String(header, charset).concat(new String(bytes, charset)).concat("\n\n}");

        // Change all the incompatiable code from Processing
        program = program.replace("public void ", "void ");
        program = program.replace("void ", "public void ");
        program = program.replace("float", "double");
        program = program.replace("Float", "Double");
        program = program.replace("FLOAT", "DOUBLE");
        program = program.replace("color ", "int ");
        program = program.replace("boolean(", "parseBoolean(");
        program = program.replace("byte(", "parseByte(");
        program = program.replace("char(", "parseChar(");
        program = program.replace("double(", "parseDouble(");
        program = program.replace("int(", "parseInt(");
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
        System.out.println("Loaded "+Main.apps.size()+" app(s)");
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
        Main.apps.add(new AppTemplate(appTitle, appAuthour, appDescription));
    }
    
    public static class StringJavaFileObject extends SimpleJavaFileObject {
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

    public static class ClassJavaFileObject extends SimpleJavaFileObject {
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

    public static class SimpleJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
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

    public static class CompiledClassLoader extends ClassLoader {
        private final List<ClassJavaFileObject> files;

        CompiledClassLoader(List<ClassJavaFileObject> files) {
            this.files = files;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            Iterator<ClassJavaFileObject> itr = files.iterator();
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