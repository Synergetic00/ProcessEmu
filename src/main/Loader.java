package main;

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

import jgraphics.canvas.Graphics;

import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

@SuppressWarnings("unused")
public class Loader {

    private static Class<?> programClass;
    private static Constructor<?> programConstructor;
    private static Object programObject;

    public static void loadFolder(String folder) {
        try {
            Loader.searchFolder(new File(folder));
            System.out.println(String.format("Loaded %d app(s)", Main.apps.size()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void searchFolder(File path) throws IOException {
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

        String fileName = file.getName();
        if (fileName.equals(".DS_Store")) {
            br.close();
            return;
        }
        String title = fileName.substring(0, fileName.length() - 4);
        String authour;
        String desc;


        try {
            String input = br.readLine();
            if (input.substring(0, 2).equals("//")) {
                authour = input.substring(2);
            } else {
                authour = "Unknown Authour";
            }
        } catch (StringIndexOutOfBoundsException e) {
            authour = "Unknown Authour";
        }

        try {
            String input = br.readLine();
            if (input.substring(0, 2).equals("//")) {
                desc = input.substring(2);
            } else {
                desc = "Default description";
            }
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
        String headerClass = "public class ProcessingApp extends AppBase {\n\tpublic ProcessingApp(Graphics gc) { super(gc); }\n";
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
        program = program.replace("color[", "int[");

        program = program.replace(" boolean(", "parseBoolean(");
        program = program.replace(" byte(", "parseByte(");
        program = program.replace(" char(", "parseChar(");
        program = program.replace(" double(", "parseDouble(");
        program = program.replace(" int(", "parseInt(");

        program = program.replace("=boolean(", "parseBoolean(");
        program = program.replace("=byte(", "parseByte(");
        program = program.replace("=char(", "parseChar(");
        program = program.replace("=double(", "parseDouble(");
        program = program.replace("=int(", "parseInt(");

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
        programConstructor = programClass.getConstructor(Graphics.class);
        programObject = programConstructor.newInstance(Main.gc);

        methodSettings = programClass.getMethod("handleSettings");
        methodSetup = programClass.getMethod("handleSetup");
        methodDraw = programClass.getMethod("handleDraw");

        // methodKeyPressed = programClass.getMethod("handleKeyPressed");
        // methodKeyReleased = programClass.getMethod("handleKeyReleased");
        // methodKeyTyped = programClass.getMethod("handleKeyTyped");
        // methodMouseClicked = programClass.getMethod("handleMouseClicked");
        // methodMouseDragged = programClass.getMethod("handleMouseDragged");
        // methodMouseMoved = programClass.getMethod("handleMouseMoved");
        // methodMousePressed = programClass.getMethod("handleMousePressed");
        // methodMouseReleased = programClass.getMethod("handleMouseReleased");
        // methodMouseWheel = programClass.getMethod("handleMouseWheel");

        methodSettings.invoke(programObject);
        methodSetup.invoke(programObject);
    }

    /////////////////////
    // Handled Methods //
    /////////////////////

    private static Method methodSettings;
    public static void handleSettings() {

    }

    private static Method methodSetup;
    public static void handleSetup() {

    }

    private static Method methodDraw;
    public static void handleDraw() {

    }

    private static Method methodKeyPressed;
    public static void handleKeyPressed() {
        
    }

    private static Method methodKeyReleased;
    public static void handleKeyReleased() {
        
    }

    private static Method methodKeyTyped;
    public static void handleKeyTyped() {
        
    }

    
    private static Method methodMouseClicked;
    public static void handleMouseClicked() {
        
    }

    private static Method methodMouseDragged;
    public static void handleMouseDragged() {
        
    }

    private static Method methodMouseMoved;
    public static void handleMouseMoved() {
        
    }

    private static Method methodMousePressed;
    public static void handleMousePressed() {
        
    }

    private static Method methodMouseReleased;
    public static void handleMouseReleased() {
        
    }

    private static Method methodMouseWheel;
    public static void handleMouseWheel() {
        
    }

    ////////////////////
    // Object Loading //
    ////////////////////

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