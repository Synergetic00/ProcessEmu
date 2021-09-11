package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

public class Loader {

    final static KeyCombination quitComb = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
    final static KeyCombination scaleComb = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    final static KeyCombination reloadComb = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);

    // Java object variables
    static Class<?> programClass;
    static Constructor<?> programConstructor;
    static Object programObject;

    // Rendering methods
    static Method settingsMethod;
    static Method setupMethod;
    static Method drawMethod;

    public static void handleSetup() {
        try {
            if (setupMethod != null) {
                setupMethod.invoke(programObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in setup()");
        }
    }

    public static void handleDraw() {
        try {
            if (drawMethod != null) {
                drawMethod.invoke(programObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in draw()");
        }
    }

    // Keyboard input methods
    static Method keyPressedMethod;
    public static void handleKeyPressed(KeyEvent event) {
        try {
            keyPressedMethod.invoke(programObject, event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (quitComb.match(event)) {
            launchHomeScreen();
        }

        if (scaleComb.match(event)) {
            Main.scaled = !Main.scaled;
        }

        if (reloadComb.match(event)) {
            launchProgram(Main.appIndex);
        }

        event.consume();
    }

    static Method keyReleasedMethod;
    public static void handleKeyReleased(KeyEvent event) {
        try {
            keyReleasedMethod.invoke(programObject, event);
        } catch (Exception e) {}
        event.consume();
    }

    static Method keyTypedMethod;
    public static void handleKeyTyped(KeyEvent event) {
        try {
            keyTypedMethod.invoke(programObject, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        event.consume();
    }

    // Mouse input methods
    static Method mouseClickedMethod;
    public static void handleMouseClicked(MouseEvent event) {
        try {
            mouseClickedMethod.invoke(programObject, event);
        } catch (Exception e) {}
        event.consume();
    }

    static Method mouseDraggedMethod;
    public static void handleMouseDragged(MouseEvent event) {
        try {
            mouseDraggedMethod.invoke(programObject, event);
        } catch (Exception e) {}
        event.consume();
    }

    static Method mouseMovedMethod;
    public static void handleMouseMoved(MouseEvent event) {
        try {
            mouseMovedMethod.invoke(programObject, event);
        } catch (Exception e) {}
        event.consume();
    }

    static Method mousePressedMethod;
    public static void handleMousePressed(MouseEvent event) {
        try {
            mousePressedMethod.invoke(programObject, event);
        } catch (Exception e) {}
        event.consume();
    }

    static Method mouseReleasedMethod;
    public static void handleMouseReleased(MouseEvent event) {
        try {
            mouseReleasedMethod.invoke(programObject, event);
        } catch (Exception e) {}
        event.consume();
    }

    static Method mouseWheelMethod;
    public static void handleMouseWheel(ScrollEvent event) {
        try {
            mouseWheelMethod.invoke(programObject, event);
        } catch (Exception e) {}
        event.consume();
    }

    public static void searchFolder(File path) throws IOException {
        for (File entry : path.listFiles()) {
            if (entry.isDirectory()) {
                searchFolder(entry);
            } else {
                addAppEntry(entry);
            }
        }

        Collections.sort(Main.apps);
    }

    private static void addAppEntry(File file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String title = file.getName().substring(0, file.getName().length() - 4);
        String authour;
        String desc;

        try {
            authour = br.readLine().substring(2);
        } catch (StringIndexOutOfBoundsException e) {
            authour = "Unknown Authour";
        }

        try {
            desc = br.readLine().substring(2);
        } catch (StringIndexOutOfBoundsException e) {
            desc = "Default description";
        }
        Main.apps.add(new AppEntry(title, authour, desc, file.toString()));

        br.close();
        fr.close();
    }

    public static String getFileContents(String path) throws IOException {

        // Read the contents of the desired file into a complete java 'file'
        Charset utf8 = Charset.forName("UTF-8");

        String headerPackage = "package sketches;";
        String headerClass = "public class ProcessingApp extends AppBase {\n\tpublic ProcessingApp(GraphicsContext gc) { super(gc); }\n";
        String headerImports = new String(Files.readAllBytes(Paths.get("src/Imports.txt")), utf8);

        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String program = new String(bytes, utf8);

        Pattern p = Pattern.compile("import .*;");
        Matcher m = p.matcher(program);

        while (m.find()) {
            String newImport = m.group(0);
            program = program.replace(newImport, "");
            headerImports += newImport + '\n';
        }

        String header = headerPackage + '\n' + headerImports + '\n' + headerClass;

        if (!(program.contains("void setup()") || program.contains("void draw()"))) {
            program = new String("void setup() {\n".concat(program).concat("\n}"));
        }

        program = header.concat(program).concat("\n\n}");

        program = program.replace("public void ", "void ");
        program = program.replace("void ", "public void ");
        program = program.replace("float", "double");
        program = program.replace("Float", "Double");
        program = program.replace("FLOAT", "DOUBLE");
        program = program.replace("color ", "int ");

        program = program.replaceAll("([ \\+\\*-/=\\(\\)\\[\\]])boolean\\(", "$1parseBoolean(");
        program = program.replaceAll("([ \\+\\*-/=\\(\\)\\[\\]])byte\\(", "$1parseByte(");
        program = program.replaceAll("([ \\+\\*-/=\\(\\)\\[\\]])char\\(", "$1parseChar(");
        program = program.replaceAll("([ \\+\\*-/=\\(\\)\\[\\]])double\\(", "$1parseDouble(");
        program = program.replaceAll("([ \\+\\*-/=\\(\\)\\[\\]])int\\(", "$1parseInt(");

        return program;
    }

    public static void launchHomeScreen() {
        // Go back to home screen
        try {
            launchProgram("src/Home.pde");
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("Failed to load home screen");
        }
    }

    public static void launchProgram(int id) {
        try {
            Main.apps.get(id).launch();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No application exists at index: "+id);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void launchProgram(String path) throws Exception {
        String program = getFileContents(path);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaFileObject compilationUnit = new StringJavaFileObject("ProcessingApp", program);
        SimpleJavaFileManager fileManager = new SimpleJavaFileManager(compiler.getStandardFileManager(null, null, null));
        CompilationTask compilationTask = compiler.getTask(null, fileManager, null, null, null, Arrays.asList(compilationUnit));
        compilationTask.call();
        CompiledClassLoader classLoader = new CompiledClassLoader(fileManager.getGeneratedOutputFiles());

        programClass = classLoader.loadClass("sketches.ProcessingApp");
        programConstructor = programClass.getConstructor(GraphicsContext.class);
        programObject = programConstructor.newInstance(Main.gc);

        settingsMethod = programClass.getMethod("handleSettings");
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

        settingsMethod.invoke(programObject);
        setupMethod.invoke(programObject);
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