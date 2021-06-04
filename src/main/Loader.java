package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;

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

public class Loader {

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
            System.out.println("Error in setup()");
        }
    }

    public static void handleDraw() {
        try {
            if (drawMethod != null) {
                drawMethod.invoke(programObject);
            }
        } catch (Exception e) {
            System.out.println("Error in draw()");
        }
    }

    // Keyboard input methods
    static Method keyPressedMethod;
    public static void handleKeyPressed(KeyEvent event) {
        try {
            keyPressedMethod.invoke(programObject, event);
        } catch (Exception e) {}
    }

    public static void searchFolder(File path) throws IOException {
        for (File entry : path.listFiles()) {
            if (entry.isDirectory()) {
                searchFolder(entry);
            } else {
                addAppEntry(entry.getName());
                System.out.println(entry.getName());
            }
        }
        System.out.println("Loaded "+Main.apps.size()+" app(s)");
    }

    private static void addAppEntry(String name) throws IOException {
        FileReader fr = new FileReader("src/sketches/" + name);
        BufferedReader br = new BufferedReader(fr);

        String title = name.substring(0, name.length() - 4);
        String authour = br.readLine().substring(2);
        String desc = br.readLine().substring(2);
        Main.apps.add(new AppEntry(title, authour, desc));

        br.close();
        fr.close();
    }

    public static String getFileContents(String path) throws IOException {

        // Read the contents of the desired file into a complete java 'file'
        byte[] header = Files.readAllBytes(Paths.get("src/Header.txt"));
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        Charset charset = Charset.forName("UTF-8");
        String program = new String(header, charset).concat(new String(bytes, charset)).concat("\n\n}");

        program = program.replace("public void ", "void ");
        program = program.replace("void ", "public void ");

        return program;
    }

    public static void launchHomeScreen() {
        // Go back to home screen
        try {
            launchProgram("src/Home.pde");
        } catch (Exception e) {
            System.out.println("Failed to load home screen");
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
        keyPressedMethod    = programClass.getMethod("handleKeyPressed", KeyEvent.class);

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