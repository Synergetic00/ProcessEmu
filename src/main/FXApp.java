package main;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.charset.*;
import java.awt.image.*;
import java.awt.color.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.zip.*;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import data.*;
import event.*;
import event.Event;
import javafx.scene.canvas.GraphicsContext;
import types.*;

import static utils.Constants.*;
import static utils.DataUtils.*;

public class FXApp {

    
    static public final String javaVersionName = System.getProperty("java.version");

    public static boolean useNativeSelect = true;
    public PGraphics pg, recorder;
    public int displayWidth, displayHeight;
    public String[] args;
    public boolean keyRepeatEnabled = false;
    public boolean focused = false;
    protected PSurface surface;
    public static int platform;
    private String sketchPath;

    public static final int DEFAULT_WIDTH = 100;
    public static final int DEFAULT_HEIGHT = 100;
    public int[] pixels;
    public int width = DEFAULT_WIDTH;
    public int height = DEFAULT_HEIGHT;
    public int pixelWidth, pixelHeight;

    long millisOffset = System.currentTimeMillis();
    public double frameRate = 60;
    public int frameCount;
    protected boolean looping = true;
    protected boolean redraw = true;
    public volatile boolean finished;
    static Throwable uncaughtThrowable;
    protected boolean exitCalled;

    boolean insideSettings;

    String renderer = JAVA2D;
    int smooth = 1;
    boolean fullScreen;
    int display = -1;
    GraphicsDevice[] displayDevices;
    public int pixelDensity = 1;
    int suggestedDensity = -1;
    boolean present;
    String outputPath;
    OutputStream outputStream;
    int windowColor = 0xffDDDDDD;
    boolean external = false;

    public GraphicsContext gc;

    public FXApp(GraphicsContext gc) {
        this.gc = gc;
        pg = createPrimaryGraphics();
    }

    final public int sketchWidth() { return width; }
    final public int sketchHeight() { return height; }
    final public String sketchRenderer() { return renderer; }
    final public int sketchSmooth() { return smooth; }
    final public boolean sketchFullScreen() { return fullScreen; }
    final public int sketchDisplay() { return display; }
    final public String sketchOutputPath() { return outputPath; }
    final public OutputStream sketchOutputStream() { return outputStream; }
    final public int sketchWindowColor() { return windowColor; }
    final public int sketchPixelDensity() { return pixelDensity; }

    /////////////////////
    // Handled Methods //
    /////////////////////

    public void handleSettings() {
        settings();
    }

    public void handleSetup() {
        setup();
    }

    public void handleDraw() {
        draw();
    }

    public void handleKeyPressed(javafx.scene.input.KeyEvent event) {
        
    }

    public void handleKeyReleased(javafx.scene.input.KeyEvent event) {
        
    }

    public void handleKeyTyped(javafx.scene.input.KeyEvent event) {
        
    }

    public void handleMouseClicked(javafx.scene.input.MouseEvent event) {
        
    }

    public void handleMouseDragged(javafx.scene.input.MouseEvent event) {
        
    }

    public void handleMouseMoved(javafx.scene.input.MouseEvent event) {
        
    }

    public void handleMousePressed(javafx.scene.input.MouseEvent event) {
        
    }

    public void handleMouseReleased(javafx.scene.input.MouseEvent event) {
        
    }

    public void handleMouseWheel(javafx.scene.input.ScrollEvent event) {
        
    }

    /////////////////////
    // Other Functions //
    /////////////////////

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        pixelWidth = width * pixelDensity;
        pixelHeight = height * pixelDensity;
    }

    public void focusGained() {}

    public void focusLost() {
        pressedKeys.clear();
    }

    static public void hideMenuBar() {
        if (platform == MACOSX) {
            //japplemenubar.JAppleMenuBar.hide();
        }
    }

    static public final String EXTERNAL_MOVE = "__MOVE__";

    public void frameMoved(int x, int y) {
        if (!fullScreen) {
            System.err.println(EXTERNAL_MOVE + " " + x + " " + y);
            System.err.flush(); // doesn't seem to help or hurt
        }
    }

    BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>();
    private final Object eventQueueDequeueLock = new Object[0];

    public void postEvent(Event pe) {
        eventQueue.add(pe);

        if (!looping) {
            dequeueEvents();
        }
    }

    protected void dequeueEvents() {
        synchronized (eventQueueDequeueLock) {
            while (!eventQueue.isEmpty()) {
                Event e = eventQueue.remove();
                switch (e.getFlavor()) {
                    case Event.MOUSE:
                        //handleMouseEvent((MouseEvent) e);
                        break;
                    case Event.KEY:
                        //handleKeyEvent((KeyEvent) e);
                        break;
                }
            }
        }
    }

    protected void printStackTrace(Throwable t) {
        t.printStackTrace();
    }

    boolean insideSettings(String method, Object... args) {
        if (insideSettings) {
            return true;
        }
        final String url = "https://processing.org/reference/" + method + "_.html";
        if (!external) { // post a warning for users of Eclipse and other IDEs
            StringList argList = new StringList(args);
            System.err.println("When not using the PDE, " + method + "() can only be used inside settings().");
            System.err.println("Remove the " + method + "() method from setup(), and add the following:");
            System.err.println("public void settings() {");
            System.err.println("  " + method + "(" + argList.join(", ") + ");");
            System.err.println("}");
        }
        throw new IllegalStateException(method + "() cannot be used here, see " + url);
    }

    public void die(String what) {
        dispose();
        throw new RuntimeException(what);
    }

    public void die(String what, Exception e) {
        if (e != null) e.printStackTrace();
        die(what);
    }

    public static String getExtension(String filename) {
        String extension;

        String lower = filename.toLowerCase();
        int dot = filename.lastIndexOf('.');
        if (dot == -1) {
            return "";
        }
        extension = lower.substring(dot + 1);
        int question = extension.indexOf('?');
        if (question != -1) {
            extension = extension.substring(0, question);
        }

        return extension;
    }

    public static String checkExtension(String filename) {
        if (filename.toLowerCase().endsWith(".gz")) {
            filename = filename.substring(0, filename.length() - 3);
        }
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex != -1) {
            return filename.substring(dotIndex + 1).toLowerCase();
        }
        return null;
    }

    protected static void selectImpl(final String prompt, final String callbackMethod, final File defaultSelection, final Object callbackObject, final Frame parentFrame, final int mode, final FXApp sketch) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                File selectedFile = null;

                boolean hide = (sketch != null) && (sketch.pg instanceof PGraphicsOpenGL) && (platform == WINDOWS);
                if (hide)
                    sketch.surface.setVisible(false);

                if (useNativeSelect) {
                    FileDialog dialog = new FileDialog(parentFrame, prompt, mode);
                    if (defaultSelection != null) {
                        dialog.setDirectory(defaultSelection.getParent());
                        dialog.setFile(defaultSelection.getName());
                    }

                    dialog.setVisible(true);
                    String directory = dialog.getDirectory();
                    String filename = dialog.getFile();
                    if (filename != null) {
                        selectedFile = new File(directory, filename);
                    }

                } else {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle(prompt);
                    if (defaultSelection != null) {
                        chooser.setSelectedFile(defaultSelection);
                    }

                    int result = -1;
                    if (mode == FileDialog.SAVE) {
                        result = chooser.showSaveDialog(parentFrame);
                    } else if (mode == FileDialog.LOAD) {
                        result = chooser.showOpenDialog(parentFrame);
                    }
                    if (result == JFileChooser.APPROVE_OPTION) {
                        selectedFile = chooser.getSelectedFile();
                    }
                }

                if (hide)
                    sketch.surface.setVisible(true);
                selectCallback(selectedFile, callbackMethod, callbackObject);
            }
        });
    }

    private static void selectCallback(File selectedFile, String callbackMethod, Object callbackObject) {
        try {
            Class<?> callbackClass = callbackObject.getClass();
            Method selectMethod = callbackClass.getMethod(callbackMethod, new Class[] { File.class });
            selectMethod.invoke(callbackObject, new Object[] { selectedFile });
        } catch (IllegalAccessException iae) {
            System.err.println(callbackMethod + "() must be public");
        } catch (InvocationTargetException ite) {
            ite.printStackTrace();
        } catch (NoSuchMethodException nsme) {
            System.err.println(callbackMethod + "() could not be found");
        }
    }

    public PMatrix getMatrix() {
        return pg.getMatrix();
    }

    public PMatrix2D getMatrix(PMatrix2D target) {
        return pg.getMatrix(target);
    }

    public PMatrix3D getMatrix(PMatrix3D target) {
        return pg.getMatrix(target);
    }

    public void setMatrix(PMatrix source) {
        if (recorder != null)
            recorder.setMatrix(source);
        pg.setMatrix(source);
    }

    public void setMatrix(PMatrix2D source) {
        if (recorder != null)
            recorder.setMatrix(source);
        pg.setMatrix(source);
    }

    public void setMatrix(PMatrix3D source) {
        if (recorder != null)
            recorder.setMatrix(source);
        pg.setMatrix(source);
    }

    public void style(PStyle s) {
        if (recorder != null)
            recorder.style(s);
        pg.style(s);
    }

    public void mask(PImage img) {
        if (recorder != null)
            recorder.mask(img);
        pg.mask(img);
    }

    ///////////////
    // Structure //
    ///////////////

    // draw()

    public void draw() {
        finished = true;
    }

    // exit()

    public void exit() {
        if (surface.isStopped()) {
            exitActual();
        } else if (looping) {
            finished = true;
            exitCalled = true;
        } else if (!looping) {
            dispose();
            exitActual();
        }
    }

    public boolean exitCalled() {
        return exitCalled;
    }

    public void exitActual() {
        try {
            System.exit(0);
        } catch(SecurityException se) {}
    }

    public void dispose() {
        finished = true;
        if (surface.stopThread()) {
            if (pg != null) pg.dispose();
        }
    }

    // loop()

    synchronized public void loop() {
        if (!looping) {
            looping = true;
        }
    }

    // noLoop()

    synchronized public void noLoop() {
        if (looping) {
            looping = false;
        }
    }
    
    public boolean isLooping() {
        return looping;
    }

    // pop()

    public void pop() {
        if (recorder != null)
            recorder.pop();
        pg.pop();
    }

    // popStyle()

    public void popStyle() {
        if (recorder != null)
            recorder.popStyle();
        pg.popStyle();
    }

    // push()

    public void push() {
        if (recorder != null)
            recorder.push();
        pg.push();
    }

    // pushStyle()

    public void pushStyle() {
        if (recorder != null)
            recorder.pushStyle();
        pg.pushStyle();
    }

    // redraw()

    synchronized public void redraw() {
        if (!looping) {
            redraw = true;
        }
    }

    // setup()

    public void setup() {}

    // thread()

    public void thread(final String name) {
        Thread later = new Thread() {
            @Override
            public void run() {
                method(name);
            }
        };
        later.start();
    }

    public void method(String name) {
        try {
            Method method = getClass().getMethod(name, new Class[] {});
            method.invoke(this, new Object[] { });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        } catch (NoSuchMethodException nsme) {
            System.err.println("There is no public " + name + "() method " + "in the class " + getClass().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////////////////
    // Environment //
    /////////////////

    // cursor()

    public void cursor() {
        surface.showCursor();
    }

    public void cursor(int kind) {
        surface.setCursor(kind);
    }

    public void cursor(PImage img) {
        cursor(img, img.width / 2, img.height / 2);
    }

    public void cursor(PImage img, int x, int y) {
        surface.setCursor(img, x, y);
    }

    // delay()

    public void delay(int napTime) {
        try {
            Thread.sleep(napTime);
        } catch (InterruptedException ie) {}
    }

    // displayDensity()

    public int displayDensity() {
        if (display != SPAN && (fullScreen || present)) {
            return displayDensity(display);
        }
        // walk through all displays, use 2 if any display is 2
        for (int i = 0; i < displayDevices.length; i++) {
            if (displayDensity(i + 1) == 2) {
                return 2;
            }
        }
        // If nobody's density is 2 then everyone is 1
        return 1;
    }
    
    public int displayDensity(int display) {
        if (platform == MACOSX) {
            final String javaVendor = System.getProperty("java.vendor");
            if (javaVendor.contains("Oracle")) {
                GraphicsDevice device;
                GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();

                if (display == -1) {
                    device = env.getDefaultScreenDevice();

                } else if (display == SPAN) {
                    throw new RuntimeException("displayDensity() only works with specific display numbers");

                } else {
                    GraphicsDevice[] devices = env.getScreenDevices();
                    if (display > 0 && display <= devices.length) {
                        device = devices[display - 1];
                    } else {
                        if (devices.length == 1) {
                            System.err.println("Only one display is currently known, use displayDensity(1).");
                        } else {
                            System.err.format("Your displays are numbered %d through %d, " + "pass one of those numbers to displayDensity()%n", 1, devices.length);
                        }
                        throw new RuntimeException("Display " + display + " does not exist.");
                    }
                }

                try {
                    Field field = device.getClass().getDeclaredField("scale");
                    if (field != null) {
                        field.setAccessible(true);
                        Object scale = field.get(device);

                        if (scale instanceof Integer && ((Integer) scale).intValue() == 2) {
                            return 2;
                        }
                    }
                } catch (Exception ignore) {
                }
            }
        } else if (platform == WINDOWS || platform == LINUX) {
            if (suggestedDensity == -1) {
                return 1;
            } else if (suggestedDensity == 1 || suggestedDensity == 2) {
                return suggestedDensity;
            }
        }
        return 1;
    }

    // frameRate()

    public void frameRate(double fps) {
        surface.setFrameRate(fps);
    }

    // fullScreen()
    
    public void fullScreen() {
        if (!fullScreen) {
            if (insideSettings("fullScreen")) {
                this.fullScreen = true;
            }
        }
    }

    public void fullScreen(int display) {
        if (!fullScreen || display != this.display) {
            if (insideSettings("fullScreen", display)) {
                this.fullScreen = true;
                this.display = display;
            }
        }
    }

    public void fullScreen(String renderer) {
        if (!fullScreen || !renderer.equals(this.renderer)) {
            if (insideSettings("fullScreen", renderer)) {
                this.fullScreen = true;
                this.renderer = renderer;
            }
        }
    }

    public void fullScreen(String renderer, int display) {
        if (!fullScreen || !renderer.equals(this.renderer) || display != this.display) {
            if (insideSettings("fullScreen", renderer, display)) {
                this.fullScreen = true;
                this.renderer = renderer;
                this.display = display;
            }
        }
    }

    // noCursor()

    public void noCursor() {
        surface.hideCursor();
    }

    // noSmooth()

    public void noSmooth() {
        if (insideSettings) {
            this.smooth = 0;
        } else if (this.smooth != 0) {
            smoothWarning("noSmooth");
        }
    }

    private void smoothWarning(String method) {
        final String where = external ? "setup" : "settings";
        PGraphics.showWarning("%s() can only be used inside %s()", method, where);
        if (external) {
            PGraphics.showWarning("When run from the PDE, %s() is automatically moved from setup() to settings()", method);
        }
    }

    // pixelDensity()

    public void pixelDensity(int density) {
        if (density != this.pixelDensity) {
            if (insideSettings("pixelDensity", density)) {
                if (density != 1 && density != 2) {
                    throw new RuntimeException("pixelDensity() can only be 1 or 2");
                }
                if (!FX2D.equals(renderer) && density == 2 && displayDensity() == 1) {
                    System.err.println("pixelDensity(2) is not available for this display");
                    this.pixelDensity = 1;
                } else {
                    this.pixelDensity = density;
                }
            } else {
                System.err.println("not inside settings");
                throw new RuntimeException("pixelDensity() can only be used inside settings()");
            }
        }
    }

    // settings()

    public void settings() {}

    // size()

    public void size(int width, int height) {
        if (width != this.width || height != this.height) {
            //if (insideSettings("size", width, height)) {
                this.width = width;
                this.height = height;
            //}
        }
    }

    public void size(int width, int height, String renderer) {
        if (width != this.width || height != this.height || !renderer.equals(this.renderer)) {
            if (insideSettings("size", width, height, "\"" + renderer + "\"")) {
                this.width = width;
                this.height = height;
                this.renderer = renderer;
            }
        }
    }

    public void size(int width, int height, String renderer, String path) {
        if (width != this.width || height != this.height || !renderer.equals(this.renderer)) {
            if (insideSettings("size", width, height, "\"" + renderer + "\"", "\"" + path + "\"")) {
                this.width = width;
                this.height = height;
                this.renderer = renderer;
                this.outputPath = path;
            }
        }
    }

    // smooth()

    public void smooth() {
        smooth(1);
    }

    public void smooth(int level) {
        if (insideSettings) {
            this.smooth = level;
        } else if (this.smooth != level) {
            smoothWarning("smooth");
        }
    }

    ///////////
    // Shape //
    ///////////

    // createShape()

    public PShape createShape() {
        return pg.createShape();
    }

    public PShape createShape(int type) {
        return pg.createShape(type);
    }

    public PShape createShape(int kind, double... p) {
        return pg.createShape(kind, p);
    }

    // loadShape()

    public PShape loadShape(String filename) {
        return pg.loadShape(filename);
    }

    public PShape loadShape(String filename, String options) {
        return pg.loadShape(filename, options);
    }

    ///////////////////
    // 2D Primitives //
    ///////////////////

    // arc()

    public void arc(double a, double b, double c, double d, double start, double stop) {
        if (recorder != null)
            recorder.arc(a, b, c, d, start, stop);
        pg.arc(a, b, c, d, start, stop);
    }

    public void arc(double a, double b, double c, double d, double start, double stop, int mode) {
        if (recorder != null)
            recorder.arc(a, b, c, d, start, stop, mode);
        pg.arc(a, b, c, d, start, stop, mode);
    }

    // circle()

    public void circle(double x, double y, double extent) {
        if (recorder != null)
            recorder.circle(x, y, extent);
        pg.circle(x, y, extent);
    }

    // ellipse()

    public void ellipse(double a, double b, double c, double d) {
        if (recorder != null)
            recorder.ellipse(a, b, c, d);
        pg.ellipse(a, b, c, d);
    }

    // line()

    public void line(double x1, double y1, double x2, double y2) {
        if (recorder != null)
            recorder.line(x1, y1, x2, y2);
        pg.line(x1, y1, x2, y2);
    }

    public void line(double x1, double y1, double z1, double x2, double y2, double z2) {
        if (recorder != null)
            recorder.line(x1, y1, z1, x2, y2, z2);
        pg.line(x1, y1, z1, x2, y2, z2);
    }

    // point()

    public void point(double x, double y) {
        if (recorder != null)
            recorder.point(x, y);
        pg.point(x, y);
    }

    public void point(double x, double y, double z) {
        if (recorder != null)
            recorder.point(x, y, z);
        pg.point(x, y, z);
    }

    // quad()

    public void quad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        if (recorder != null)
            recorder.quad(x1, y1, x2, y2, x3, y3, x4, y4);
        pg.quad(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    // rect()

    public void rect(double a, double b, double c, double d) {
        if (recorder != null)
            recorder.rect(a, b, c, d);
        pg.rect(a, b, c, d);
    }

    public void rect(double a, double b, double c, double d, double r) {
        if (recorder != null)
            recorder.rect(a, b, c, d, r);
        pg.rect(a, b, c, d, r);
    }

    public void rect(double a, double b, double c, double d, double tl, double tr, double br, double bl) {
        if (recorder != null)
            recorder.rect(a, b, c, d, tl, tr, br, bl);
        pg.rect(a, b, c, d, tl, tr, br, bl);
    }

    // square()

    public void square(double x, double y, double extent) {
        if (recorder != null)
            recorder.square(x, y, extent);
        pg.square(x, y, extent);
    }

    // triangle()

    public void triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        if (recorder != null)
            recorder.triangle(x1, y1, x2, y2, x3, y3);
        pg.triangle(x1, y1, x2, y2, x3, y3);
    }

    ////////////
    // Curves //
    ////////////

    // bezier()

    public void bezier(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        if (recorder != null)
            recorder.bezier(x1, y1, x2, y2, x3, y3, x4, y4);
        pg.bezier(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    public void bezier(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
            double z3, double x4, double y4, double z4) {
        if (recorder != null)
            recorder.bezier(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
        pg.bezier(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    // bezierDetail()

    public void bezierDetail(int detail) {
        if (recorder != null)
            recorder.bezierDetail(detail);
        pg.bezierDetail(detail);
    }

    // bezierPoint()

    public double bezierPoint(double a, double b, double c, double d, double t) {
        return pg.bezierPoint(a, b, c, d, t);
    }

    // bezierTangent()

    public double bezierTangent(double a, double b, double c, double d, double t) {
        return pg.bezierTangent(a, b, c, d, t);
    }

    // curve()

    public void curve(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        if (recorder != null)
            recorder.curve(x1, y1, x2, y2, x3, y3, x4, y4);
        pg.curve(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    public void curve(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3,
            double x4, double y4, double z4) {
        if (recorder != null)
            recorder.curve(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
        pg.curve(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    // curveDetail()

    public void curveDetail(int detail) {
        if (recorder != null)
            recorder.curveDetail(detail);
        pg.curveDetail(detail);
    }

    // curvePoint()

    public double curvePoint(double a, double b, double c, double d, double t) {
        return pg.curvePoint(a, b, c, d, t);
    }

    // curveTangent()

    public double curveTangent(double a, double b, double c, double d, double t) {
        return pg.curveTangent(a, b, c, d, t);
    }

    // curveTightness()

    public void curveTightness(double tightness) {
        if (recorder != null)
            recorder.curveTightness(tightness);
        pg.curveTightness(tightness);
    }

    ///////////////////
    // 3D Primitives //
    ///////////////////

    // box()

    public void box(double size) {
        if (recorder != null)
            recorder.box(size);
        pg.box(size);
    }

    public void box(double w, double h, double d) {
        if (recorder != null)
            recorder.box(w, h, d);
        pg.box(w, h, d);
    }

    // sphere()

    public void sphere(double r) {
        if (recorder != null)
            recorder.sphere(r);
        pg.sphere(r);
    }

    // sphereDetail()

    public void sphereDetail(int res) {
        if (recorder != null)
            recorder.sphereDetail(res);
        pg.sphereDetail(res);
    }

    public void sphereDetail(int ures, int vres) {
        if (recorder != null)
            recorder.sphereDetail(ures, vres);
        pg.sphereDetail(ures, vres);
    }

    ////////////////
    // Attributes //
    ////////////////

    // ellipseMode()

    public void ellipseMode(int mode) {
        if (recorder != null)
            recorder.ellipseMode(mode);
        pg.ellipseMode(mode);
    }

    // rectMode()

    public void rectMode(int mode) {
        if (recorder != null)
            recorder.rectMode(mode);
        pg.rectMode(mode);
    }

    // strokeCap()

    public void strokeCap(int cap) {
        if (recorder != null)
            recorder.strokeCap(cap);
        pg.strokeCap(cap);
    }

    // strokeJoin()

    public void strokeJoin(int join) {
        if (recorder != null)
            recorder.strokeJoin(join);
        pg.strokeJoin(join);
    }

    // strokeWeight()

    public void strokeWeight(double weight) {
        if (recorder != null)
            recorder.strokeWeight(weight);
        pg.strokeWeight(weight);
    }

    ////////////
    // Vertex //
    ////////////

    // beginContour()

    public void beginContour() {
        if (recorder != null)
            recorder.beginContour();
        pg.beginContour();
    }

    public void beginShape(int kind) {
        if (recorder != null)
            recorder.beginShape(kind);
        pg.beginShape(kind);
    }

    // beginShape()

    public void beginShape() {
        if (recorder != null)
            recorder.beginShape();
        pg.beginShape();
    }

    // bezierVertex()

    public void bezierVertex(double x2, double y2, double x3, double y3, double x4, double y4) {
        if (recorder != null)
            recorder.bezierVertex(x2, y2, x3, y3, x4, y4);
        pg.bezierVertex(x2, y2, x3, y3, x4, y4);
    }

    public void bezierVertex(double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4,
            double z4) {
        if (recorder != null)
            recorder.bezierVertex(x2, y2, z2, x3, y3, z3, x4, y4, z4);
        pg.bezierVertex(x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    // curveVertex()

    public void curveVertex(double x, double y) {
        if (recorder != null)
            recorder.curveVertex(x, y);
        pg.curveVertex(x, y);
    }

    public void curveVertex(double x, double y, double z) {
        if (recorder != null)
            recorder.curveVertex(x, y, z);
        pg.curveVertex(x, y, z);
    }

    // endContour()

    public void endContour() {
        if (recorder != null)
            recorder.endContour();
        pg.endContour();
    }

    public void endShape(int mode) {
        if (recorder != null)
            recorder.endShape(mode);
        pg.endShape(mode);
    }

    // endShape()

    public void endShape() {
        if (recorder != null)
            recorder.endShape();
        pg.endShape();
    }

    // quadraticVertex()

    public void quadraticVertex(double cx, double cy, double x3, double y3) {
        if (recorder != null)
            recorder.quadraticVertex(cx, cy, x3, y3);
        pg.quadraticVertex(cx, cy, x3, y3);
    }

    public void quadraticVertex(double cx, double cy, double cz, double x3, double y3, double z3) {
        if (recorder != null)
            recorder.quadraticVertex(cx, cy, cz, x3, y3, z3);
        pg.quadraticVertex(cx, cy, cz, x3, y3, z3);
    }

    // vertex()

    public void vertex(double x, double y) {
        if (recorder != null)
            recorder.vertex(x, y);
        pg.vertex(x, y);
    }

    public void vertex(double x, double y, double z) {
        if (recorder != null)
            recorder.vertex(x, y, z);
        pg.vertex(x, y, z);
    }

    public void vertex(double[] v) {
        if (recorder != null)
            recorder.vertex(v);
        pg.vertex(v);
    }

    public void vertex(double x, double y, double u, double v) {
        if (recorder != null)
            recorder.vertex(x, y, u, v);
        pg.vertex(x, y, u, v);
    }

    public void vertex(double x, double y, double z, double u, double v) {
        if (recorder != null)
            recorder.vertex(x, y, z, u, v);
        pg.vertex(x, y, z, u, v);
    }

    //////////////////////////
    // Loading & Displaying //
    //////////////////////////

    // shape()

    public void shape(PShape shape) {
        if (recorder != null)
            recorder.shape(shape);
        pg.shape(shape);
    }

    public void shape(PShape shape, double x, double y) {
        if (recorder != null)
            recorder.shape(shape, x, y);
        pg.shape(shape, x, y);
    }

    public void shape(PShape shape, double a, double b, double c, double d) {
        if (recorder != null)
            recorder.shape(shape, a, b, c, d);
        pg.shape(shape, a, b, c, d);
    }

    // shapeMode()

    public void shapeMode(int mode) {
        if (recorder != null)
            recorder.shapeMode(mode);
        pg.shapeMode(mode);
    }

    ///////////
    // Input //
    ///////////

    ///////////
    // Mouse //
    ///////////

    public int mouseX, mouseY, pmouseX, pmouseY;
    protected int dmouseX, dmouseY, emouseX, emouseY;
    protected boolean firstMouse = true;
    public int mouseButton;
    public boolean mousePressed;

    // mouseClicked()

    public void mouseClicked() {}

    public void mouseClicked(MouseEvent event) {
        mouseClicked();
    }

    // mouseDragged()

    public void mouseDragged() {}

    public void mouseDragged(MouseEvent event) {
        mouseDragged();
    }

    public void mouseEntered() {}

    public void mouseEntered(MouseEvent event) {
        mouseEntered();
    }

    public void mouseExited() {}

    public void mouseExited(MouseEvent event) {
        mouseExited();
    }

    // mouseMoved()

    public void mouseMoved() {}

    public void mouseMoved(MouseEvent event) {
        mouseMoved();
    }

    // mousePressed()

    public void mousePressed() {}

    public void mousePressed(MouseEvent event) {
        mousePressed();
    }

    // mouseReleased()

    public void mouseReleased() {}

    public void mouseReleased(MouseEvent event) {
        mouseReleased();
    }

    // mouseWheel()

    public void mouseWheel() {}

    public void mouseWheel(MouseEvent event) {
        mouseWheel();
    }

    //////////////
    // Keyboard //
    //////////////

    public char key;
    public int keyCode;
    public boolean keyPressed;
    ArrayList<Long> pressedKeys = new ArrayList<>(6);

    // keyPressed()

    public void keyPressed() {}

    public void keyPressed(KeyEvent event) {
        keyPressed();
    }

    // keyReleased()

    public void keyReleased() {}

    public void keyReleased(KeyEvent event) {
        keyReleased();
    }

    // keyTyped()

    public void keyTyped() {}

    public void keyTyped(KeyEvent event) {
        keyTyped();
    }

    ///////////
    // Files //
    ///////////

    static String openLauncher;

    protected static String calcSketchPath() {
        String folder = null;
        try {
            folder = System.getProperty("user.dir");

            URL jarURL = FXApp.class.getProtectionDomain().getCodeSource().getLocation();
            String jarPath = jarURL.toURI().getSchemeSpecificPart();

            if (platform == MACOSX) {
                if (jarPath.contains("Contents/Java/")) {
                    String appPath = jarPath.substring(0, jarPath.indexOf(".app") + 4);
                    File containingFolder = new File(appPath).getParentFile();
                    folder = containingFolder.getAbsolutePath();
                }
            } else {
                if (jarPath.contains("/lib/")) {
                    folder = new File(jarPath, "../..").getCanonicalPath();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return folder;
    }

    public String dataPath(String where) {
        return dataFile(where).getAbsolutePath();
    }

    public File dataFile(String where) {
        File why = new File(where);
        if (why.isAbsolute()) return why;
        URL jarURL = getClass().getProtectionDomain().getCodeSource().getLocation();
        String jarPath;
        try {
            jarPath = jarURL.toURI().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        if (jarPath.contains("Contents/Java/")) {
            File containingFolder = new File(jarPath).getParentFile();
            File dataFolder = new File(containingFolder, "data");
            return new File(dataFolder, where);
        }
        File workingDirItem = new File(sketchPath + File.separator + "data" + File.separator + where);
        return workingDirItem;
    }

    public String sketchPath() {
        if (sketchPath == null) {
            sketchPath = calcSketchPath();
        }
        return sketchPath;
    }

    public String sketchPath(String where) {
        if (sketchPath() == null) return where;
        try {
            if (new File(where).isAbsolute())  return where;
        } catch (Exception e) {}
        return sketchPath() + File.separator + where;
    }

    public File sketchFile(String where) {
        return new File(sketchPath(where));
    }

    public static Process exec(String... args) {
		try {
			return Runtime.getRuntime().exec(args);
		} catch (Exception e) {
			throw new RuntimeException("Exception while attempting " + join(args, ' '), e);
		}
	}

    public static int exec(StringList stdout, StringList stderr, String... args) {
		Process p = exec(args);

		Thread outThread = new LineThread(p.getInputStream(), stdout);
		Thread errThread = new LineThread(p.getErrorStream(), stderr);

		try {
			int result = p.waitFor();
			outThread.join();
			errThread.join();
			return result;

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

    static class LineThread extends Thread {
		InputStream input;
		StringList output;


		LineThread(InputStream input, StringList output) {
			this.input = input;
			this.output = output;
			start();
		}

		@Override
		public void run() {
			// It's not sufficient to use BufferedReader, because if the app being
			// called fills up stdout or stderr to quickly, the app will hang.
			// Instead, write to a byte[] array and then parse it once finished.
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				saveStream(baos, input);
				BufferedReader reader =
						createReader(new ByteArrayInputStream(baos.toByteArray()));
				String line;
				while ((line = reader.readLine()) != null) {
					output.append(line);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
    
    // createInput()

    public InputStream createInput(String filename) {
        InputStream input = createInputRaw(filename);
        if (input != null) {
            // if it's gzip-encoded, automatically decode
            final String lower = filename.toLowerCase();
            if (lower.endsWith(".gz") || lower.endsWith(".svgz")) {
                try {
                    // buffered has to go *around* the GZ, otherwise 25x slower
                    return new BufferedInputStream(new GZIPInputStream(input));

                } catch (IOException e) {
                    printStackTrace(e);
                }
            } else {
                return new BufferedInputStream(input);
            }
        }
        return null;
    }

    public static InputStream createInput(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File passed to createInput() was null");
        }
        if (!file.exists()) {
            System.err.println(file + " does not exist, createInput() will return null");
            return null;
        }
        try {
            InputStream input = new FileInputStream(file);
            final String lower = file.getName().toLowerCase();
            if (lower.endsWith(".gz") || lower.endsWith(".svgz")) {
                return new BufferedInputStream(new GZIPInputStream(input));
            }
            return new BufferedInputStream(input);

        } catch (IOException e) {
            System.err.println("Could not createInput() for " + file);
            e.printStackTrace();
            return null;
        }
    }

    public InputStream createInputRaw(String filename) {
        if (filename == null)
            return null;

        if (sketchPath == null) {
            System.err.println("The sketch path is not set.");
            throw new RuntimeException("Files must be loaded inside setup() or after it has been called.");
        }
        if (filename.length() == 0) return null;

        if (filename.contains(":")) {
            try {
                URL url = new URL(filename);
                URLConnection conn = url.openConnection();

                if (conn instanceof HttpURLConnection) {
                    HttpURLConnection httpConn = (HttpURLConnection) conn;
                    httpConn.setInstanceFollowRedirects(true);
                    int response = httpConn.getResponseCode();
                    if (response >= 300 && response < 400) {
                        String newLocation = httpConn.getHeaderField("Location");
                        return createInputRaw(newLocation);
                    }
                    return conn.getInputStream();
                } else if (conn instanceof JarURLConnection) {
                    return url.openStream();
                }
            } catch (MalformedURLException | FileNotFoundException mfue) {
            } catch (IOException e) {
                printStackTrace(e);
                return null;
            }
        }

        InputStream stream = null;

        try {
            File file = new File(dataPath(filename));
            if (!file.exists()) {
                file = sketchFile(filename);
            }

            if (file.isDirectory()) {
                return null;
            }
            if (file.exists()) {
                try {
                    String filePath = file.getCanonicalPath();
                    String filenameActual = new File(filePath).getName();
                    String filenameShort = new File(filename).getName();
                    if (!filenameActual.equals(filenameShort)) {
                        throw new RuntimeException("This file is named " + filenameActual + " not " + filename + ". Rename the file " + "or change your code.");
                    }
                } catch (IOException e) {
                }
            }

            stream = new FileInputStream(file);
            if (stream != null) return stream;
        } 
        catch (IOException | SecurityException e) {}

        ClassLoader cl = getClass().getClassLoader();

        stream = cl.getResourceAsStream("data/" + filename);
        if (stream != null) {
            String cn = stream.getClass().getName();
            if (!cn.equals("sun.plugin.cache.EmptyInputStream")) {
                return stream;
            }
        }

        stream = cl.getResourceAsStream(filename);
        if (stream != null) {
            String cn = stream.getClass().getName();
            if (!cn.equals("sun.plugin.cache.EmptyInputStream")) {
                return stream;
            }
        }

        try {
            try {
                try {
                    stream = new FileInputStream(dataPath(filename));
                    if (stream != null)
                        return stream;
                } catch (IOException ioe) {}
                try {
                    stream = new FileInputStream(sketchPath(filename));
                    if (stream != null)
                        return stream;
                } catch (Exception e) {}
                try {
                    stream = new FileInputStream(filename);
                    if (stream != null)
                        return stream;
                } catch (IOException ioe) {}
            } catch (SecurityException se) {}

        } catch (Exception e) {
            printStackTrace(e);
        }

        return null;
    }

    // createReader()

    public BufferedReader createReader(String filename) {
        InputStream is = createInput(filename);
        if (is == null) {
            System.err.println("The file \"" + filename + "\" " + "is missing or inaccessible, make sure " + "the URL is valid or that the file has been " + "added to your sketch and is readable.");
            return null;
        }
        return createReader(is);
    }

    public static BufferedReader createReader(File file) {
        try {
            InputStream is = new FileInputStream(file);
            if (file.getName().toLowerCase().endsWith(".gz")) {
                is = new GZIPInputStream(is);
            }
            return createReader(is);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedReader createReader(InputStream input) {
        InputStreamReader isr = new InputStreamReader(input, StandardCharsets.UTF_8);

        BufferedReader reader = new BufferedReader(isr);
        try {
            reader.mark(1);
            int c = reader.read();
            if (c != '\uFEFF') {
                reader.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reader;
    }

    // launch()

    public static Process launch(String... args) {
		String[] params = null;

		if (platform == WINDOWS) {
			// just launching the .html file via the shell works
			// but make sure to chmod +x the .html files first
			// also place quotes around it in case there's a space
			// in the user.dir part of the url
			params = new String[] { "cmd", "/c" };

		} else if (platform == MACOSX) {
			params = new String[] { "open" };

		} else if (platform == LINUX) {
			// xdg-open is in the Free Desktop Specification and really should just
			// work on desktop Linux. Not risking it though.
			final String[] launchers = { "xdg-open", "gnome-open", "kde-open" };
			for (String launcher : launchers) {
				if (openLauncher != null) break;
				try {
					Process p = Runtime.getRuntime().exec(new String[] { launcher });
					/*int result =*/ p.waitFor();
					// Not installed will throw an IOException (JDK 1.4.2, Ubuntu 7.04)
					openLauncher = launcher;
				} catch (Exception e) { }
			}
			if (openLauncher == null) {
				System.err.println("Could not find xdg-open, gnome-open, or kde-open: " +
						"the open() command may not work.");
			}
			if (openLauncher != null) {
				params = new String[] { openLauncher };
			}
			//} else {  // give up and just pass it to Runtime.exec()
			//open(new String[] { filename });
			//params = new String[] { filename };
		}
		if (params != null) {
			// If the 'open', 'gnome-open' or 'cmd' are already included
			if (params[0].equals(args[0])) {
				// then don't prepend those params again
				return exec(args);
			} else {
				params = concat(params, args);
				return exec(params);
			}
		} else {
			return exec(args);
		}
	}

    // loadBytes()

    public byte[] loadBytes(String filename) {
        String lower = filename.toLowerCase();
        // If it's not a .gz file, then we might be able to uncompress it into
        // a fixed-size buffer, which should help speed because we won't have to
        // reallocate and resize the target array each time it gets full.
        if (!lower.endsWith(".gz")) {
            // If this looks like a URL, try to load it that way. Use the fact that
            // URL connections may have a content length header to size the array.
            if (filename.contains(":")) { // at least smells like URL
                InputStream input = null;
                try {
                    URL url = new URL(filename);
                    URLConnection conn = url.openConnection();
                    int length = -1;

                    if (conn instanceof HttpURLConnection) {
                        HttpURLConnection httpConn = (HttpURLConnection) conn;
                        // Will not handle a protocol change (see below)
                        httpConn.setInstanceFollowRedirects(true);
                        int response = httpConn.getResponseCode();
                        // Default won't follow HTTP -> HTTPS redirects for security reasons
                        // http://stackoverflow.com/a/1884427
                        if (response >= 300 && response < 400) {
                            String newLocation = httpConn.getHeaderField("Location");
                            return loadBytes(newLocation);
                        }
                        length = conn.getContentLength();
                        input = conn.getInputStream();
                    } else if (conn instanceof JarURLConnection) {
                        length = conn.getContentLength();
                        input = url.openStream();
                    }

                    if (input != null) {
                        byte[] buffer = null;
                        if (length != -1) {
                            buffer = new byte[length];
                            int count;
                            int offset = 0;
                            while ((count = input.read(buffer, offset, length - offset)) > 0) {
                                offset += count;
                            }
                        } else {
                            buffer = loadBytes(input);
                        }
                        input.close();
                        return buffer;
                    }
                } catch (MalformedURLException mfue) {
                    // not a url, that's fine

                } catch (FileNotFoundException fnfe) {
                    // Java 1.5+ throws FNFE when URL not available
                    // http://dev.processing.org/bugs/show_bug.cgi?id=403

                } catch (IOException e) {
                    printStackTrace(e);
                    return null;

                } finally {
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e) {
                            // just deal
                        }
                    }
                }
            }
        }

        InputStream is = createInput(filename);
        if (is != null) {
            byte[] outgoing = loadBytes(is);
            try {
                is.close();
            } catch (IOException e) {
                printStackTrace(e); // shouldn't happen
            }
            return outgoing;
        }

        System.err.println("The file \"" + filename + "\" " + "is missing or inaccessible, make sure "
                + "the URL is valid or that the file has been " + "added to your sketch and is readable.");
        return null;
    }

    public static byte[] loadBytes(InputStream input) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];

            int bytesRead = input.read(buffer);
            while (bytesRead != -1) {
                out.write(buffer, 0, bytesRead);
                bytesRead = input.read(buffer);
            }
            out.flush();
            return out.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] loadBytes(File file) {
        if (!file.exists()) {
            System.err.println(file + " does not exist, loadBytes() will return null");
            return null;
        }

        try {
            InputStream input;
            int length;

            if (file.getName().toLowerCase().endsWith(".gz")) {
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(raf.length() - 4);
                int b4 = raf.read();
                int b3 = raf.read();
                int b2 = raf.read();
                int b1 = raf.read();
                length = (b1 << 24) | (b2 << 16) + (b3 << 8) + b4;
                raf.close();

                // buffered has to go *around* the GZ, otherwise 25x slower
                input = new BufferedInputStream(new GZIPInputStream(new FileInputStream(file)));

            } else {
                long len = file.length();
                // http://stackoverflow.com/a/3039805
                int maxArraySize = Integer.MAX_VALUE - 5;
                if (len > maxArraySize) {
                    System.err.println("Cannot use loadBytes() on a file larger than " + maxArraySize);
                    return null;
                }
                length = (int) len;
                input = new BufferedInputStream(new FileInputStream(file));
            }
            byte[] buffer = new byte[length];
            int count;
            int offset = 0;
            // count will come back 0 when complete (or -1 if somehow going long?)
            while ((count = input.read(buffer, offset, length - offset)) > 0) {
                offset += count;
            }
            input.close();
            return buffer;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // loadJSONArray()

    public JSONArray loadJSONArray(String filename) {
        BufferedReader reader = createReader(filename);
        JSONArray outgoing = new JSONArray(reader);
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outgoing;
    }

    public static JSONArray loadJSONArray(File file) {
        BufferedReader reader = createReader(file);
        JSONArray outgoing = new JSONArray(reader);
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outgoing;
    }

    // loadJSONObject()

    public JSONObject loadJSONObject(String filename) {
        // can't pass of createReader() to the constructor b/c of resource leak
        BufferedReader reader = createReader(filename);
        JSONObject outgoing = new JSONObject(reader);
        try {
            reader.close();
        } catch (IOException e) { // not sure what would cause this
            e.printStackTrace();
        }
        return outgoing;
    }

    public static JSONObject loadJSONObject(File file) {
        // can't pass of createReader() to the constructor b/c of resource leak
        BufferedReader reader = createReader(file);
        JSONObject outgoing = new JSONObject(reader);
        try {
            reader.close();
        } catch (IOException e) { // not sure what would cause this
            e.printStackTrace();
        }
        return outgoing;
    }
    
    // loadStrings()

    public static String[] loadStrings(File file) {
        if (!file.exists()) {
            System.err.println(file + " does not exist, loadStrings() will return null");
            return null;
        }

        InputStream is = createInput(file);
        if (is != null) {
            String[] outgoing = loadStrings(is);
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return outgoing;
        }
        return null;
    }

    public String[] loadStrings(String filename) {
        InputStream is = createInput(filename);
        if (is != null) {
            String[] strArr = loadStrings(is);
            try {
                is.close();
            } catch (IOException e) {
                printStackTrace(e);
            }
            return strArr;
        }

        System.err.println("The file \"" + filename + "\" " + "is missing or inaccessible, make sure " + "the URL is valid or that the file has been " + "added to your sketch and is readable.");
        return null;
    }

    public static String[] loadStrings(InputStream input) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            return loadStrings(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] loadStrings(BufferedReader reader) {
        try {
            String[] lines = new String[100];
            int lineCount = 0;
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (lineCount == lines.length) {
                    String[] temp = new String[lineCount << 1];
                    System.arraycopy(lines, 0, temp, 0, lineCount);
                    lines = temp;
                }
                lines[lineCount++] = line;
            }
            reader.close();

            if (lineCount == lines.length) return lines;
            String[] output = new String[lineCount];
            System.arraycopy(lines, 0, output, 0, lineCount);
            return output;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // loadTable()

    public Table loadTable(String filename) {
        return loadTable(filename, null);
    }

    public Table loadTable(String filename, String options) {
        try {
            String optionStr = Table.extensionOptions(true, filename, options);
            String[] optionList = trim(split(optionStr, ','));

            Table dictionary = null;
            for (String opt : optionList) {
                if (opt.startsWith("dictionary=")) {
                    dictionary = loadTable(opt.substring(opt.indexOf('=') + 1), "tsv");
                    return dictionary.typedParse(createInput(filename), optionStr);
                }
            }
            InputStream input = createInput(filename);
            if (input == null) {
                System.err.println(filename + " does not exist or could not be read");
                return null;
            }
            return new Table(input, optionStr);

        } catch (IOException e) {
            printStackTrace(e);
            return null;
        }
    }

    // loadXML()

    public XML loadXML(String filename) {
        return loadXML(filename, null);
    }

    public XML loadXML(String filename, String options) {
        try {
            BufferedReader reader = createReader(filename);
            if (reader != null) {
                return new XML(reader, options);
            }
            return null;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    // parseJSONArray()

    public JSONArray parseJSONArray(String input) {
        return new JSONArray(new StringReader(input));
    }

    // parseJSONObject()

    public JSONObject parseJSONObject(String input) {
        return new JSONObject(new StringReader(input));
    }

    // parseXML()

    public XML parseXML(String xmlString) {
        return parseXML(xmlString, null);
    }

    public XML parseXML(String xmlString, String options) {
        try {
            return XML.parse(xmlString, options);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // selectFolder()

    public void selectFolder(String prompt, String callback) {
        selectFolder(prompt, callback, null);
    }

    public void selectFolder(String prompt, String callback, File file) {
        selectFolder(prompt, callback, file, this);
    }

    public void selectFolder(String prompt, String callback, File file, Object callbackObject) {
        selectFolder(prompt, callback, file, callbackObject, null, this); // selectFrame());
    }

    public static void selectFolder(final String prompt, final String callbackMethod, final File defaultSelection,
            final Object callbackObject, final Frame parentFrame) {
        selectFolder(prompt, callbackMethod, defaultSelection, callbackObject, parentFrame, null);
    }

    public static void selectFolder(final String prompt, final String callbackMethod, final File defaultSelection, final Object callbackObject, final Frame parentFrame, final FXApp sketch) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                File selectedFile = null;

                boolean hide = (sketch != null) && (sketch.pg instanceof PGraphicsOpenGL) && (platform == WINDOWS);
                if (hide)
                    sketch.surface.setVisible(false);

                if (platform == MACOSX && useNativeSelect != false) {
                    FileDialog fileDialog = new FileDialog(parentFrame, prompt, FileDialog.LOAD);
                    if (defaultSelection != null) {
                        fileDialog.setDirectory(defaultSelection.getAbsolutePath());
                    }
                    System.setProperty("apple.awt.fileDialogForDirectories", "true");
                    fileDialog.setVisible(true);
                    System.setProperty("apple.awt.fileDialogForDirectories", "false");
                    String filename = fileDialog.getFile();
                    if (filename != null) {
                        selectedFile = new File(fileDialog.getDirectory(), fileDialog.getFile());
                    }
                } else {
                    checkLookAndFeel();
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle(prompt);
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    if (defaultSelection != null) {
                        fileChooser.setCurrentDirectory(defaultSelection);
                    }

                    int result = fileChooser.showOpenDialog(parentFrame);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        selectedFile = fileChooser.getSelectedFile();
                    }
                }

                if (hide)
                    sketch.surface.setVisible(true);
                selectCallback(selectedFile, callbackMethod, callbackObject);
            }
        });
    }

    private static boolean lookAndFeelCheck;

    private static void checkLookAndFeel() {
        if (!lookAndFeelCheck) {
            if (platform == WINDOWS) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {}
            }
            lookAndFeelCheck = true;
        }
    }

    // selectInput()

    public void selectInput(String prompt, String callback) {
        selectInput(prompt, callback, null);
    }

    public void selectInput(String prompt, String callback, File file) {
        selectInput(prompt, callback, file, this);
    }

    public void selectInput(String prompt, String callback, File file, Object callbackObject) {
        selectInput(prompt, callback, file, callbackObject, null, this); // selectFrame());
    }

    public static void selectInput(String prompt, String callbackMethod, File file, Object callbackObject, Frame parent, FXApp sketch) {
        selectImpl(prompt, callbackMethod, file, callbackObject, parent, FileDialog.LOAD, sketch);
    }

    public static void selectInput(String prompt, String callbackMethod, File file, Object callbackObject, Frame parent) {
        selectImpl(prompt, callbackMethod, file, callbackObject, parent, FileDialog.LOAD, null);
    }

    /////////////////
    // Time & Date //
    /////////////////

    // day()

    public static int day() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    // hour()

    public static int hour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    // millis()

    public int millis() {
        return (int) (System.currentTimeMillis() - millisOffset);
    }

    // minute()

    public static int minute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    // month()

    public static int month() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    // second()

    public static int second() {
        return Calendar.getInstance().get(Calendar.SECOND);
    }

    // year()

    public static int year() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    ////////////
    // Output //
    ////////////

    public static void createPath(String path) {
        createPath(new File(path));
    }

    public static void createPath(File file) {
        try {
            String parent = file.getParent();
            if (parent != null) {
                File unit = new File(parent);
                if (!unit.exists())
                    unit.mkdirs();
            }
        } catch (SecurityException se) {
            System.err.println("You don't have permissions to create " + file.getAbsolutePath());
        }
    }

    ///////////
    // Image //
    ///////////

    public String insertFrame(String what) {
        int first = what.indexOf('#');
        int last = what.lastIndexOf('#');

        if ((first != -1) && (last - first > 0)) {
            String prefix = what.substring(0, first);
            int count = last - first + 1;
            String suffix = what.substring(last + 1);
            return prefix + nf(frameCount, count) + suffix;
        }
        return what; // no change
    }

    // save()

    public void save(String filename) {
        pg.save(savePath(filename));
    }

    // saveFrame()

    public void saveFrame() {
        try {
            pg.save(savePath("screen-" + nf(frameCount, 4) + ".tif"));
        } catch (SecurityException se) {
            System.err.println("Can't use saveFrame() when running in a browser, " + "unless using a signed applet.");
        }
    }

    public void saveFrame(String filename) {
        try {
            pg.save(savePath(insertFrame(filename)));
        } catch (SecurityException se) {
            System.err.println("Can't use saveFrame() when running in a browser, " + "unless using a signed applet.");
        }
    }
    
    ///////////
    // Files //
    ///////////

    private static File createTempFile(File file) throws IOException {
		File parentDir = file.getParentFile();
		if (!parentDir.exists()) {
			parentDir.mkdirs();
		}
		String name = file.getName();
		String prefix;
		String suffix = null;
		int dot = name.lastIndexOf('.');
		if (dot == -1) {
			prefix = name;
		} else {
			// preserve the extension so that .gz works properly
			prefix = name.substring(0, dot);
			suffix = name.substring(dot);
		}
		// Prefix must be three characters
		if (prefix.length() < 3) {
			prefix += "processing";
		}
		return File.createTempFile(prefix, suffix, parentDir);
	}

    // beginRaw()

    public PGraphics beginRaw(String renderer, String filename) {
        filename = insertFrame(filename);
        PGraphics rec = createGraphics(width, height, renderer, filename);
        pg.beginRaw(rec);
        return rec;
    }

    public void beginRaw(PGraphics rawGraphics) {
        pg.beginRaw(rawGraphics);
    }

    // beginRecord()

    public PGraphics beginRecord(String renderer, String filename) {
        filename = insertFrame(filename);
        PGraphics rec = createGraphics(width, height, renderer, filename);
        beginRecord(rec);
        return rec;
    }

    public void beginRecord(PGraphics recorder) {
        this.recorder = recorder;
        recorder.beginDraw();
    }

    // createOutput()

    public OutputStream createOutput(String filename) {
        return createOutput(saveFile(filename));
    }

    public static OutputStream createOutput(File file) {
        try {
            createPath(file); // make sure the path exists
            OutputStream output = new FileOutputStream(file);
            if (file.getName().toLowerCase().endsWith(".gz")) {
                return new BufferedOutputStream(new GZIPOutputStream(output));
            }
            return new BufferedOutputStream(output);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // createWriter()

    public PrintWriter createWriter(String filename) {
        return createWriter(saveFile(filename));
    }

    public static PrintWriter createWriter(File file) {
        if (file == null) {
            throw new RuntimeException("File passed to createWriter() was null");
        }
        try {
            createPath(file); // make sure in-between folders exist
            OutputStream output = new FileOutputStream(file);
            if (file.getName().toLowerCase().endsWith(".gz")) {
                output = new GZIPOutputStream(output);
            }
            return createWriter(output);

        } catch (Exception e) {
            throw new RuntimeException("Couldn't create a writer for " + file.getAbsolutePath(), e);
        }
    }

    public static PrintWriter createWriter(OutputStream output) {
        BufferedOutputStream bos = new BufferedOutputStream(output, 8192);
        OutputStreamWriter osw = new OutputStreamWriter(bos, StandardCharsets.UTF_8);
        return new PrintWriter(osw);
    }

    public String savePath(String where) {
        if (where == null)
            return null;
        String filename = sketchPath(where);
        createPath(filename);
        return filename;
    }

    public File saveFile(String where) {
        return new File(savePath(where));
    }

    // endRaw()

    public void endRaw() {
        pg.endRaw();
    }

    // endRecord()

    public void endRecord() {
        if (recorder != null) {
            recorder.endDraw();
            recorder.dispose();
            recorder = null;
        }
    }

    // saveBytes()

    public void saveBytes(String filename, byte[] data) {
		saveBytes(saveFile(filename), data);
	}

    public static void saveBytes(File file, byte[] data) {
		File tempFile = null;
		try {
			tempFile = createTempFile(file);

			OutputStream output = createOutput(tempFile);
			saveBytes(output, data);
			output.close();
			output = null;

			if (file.exists()) {
				if (!file.delete()) {
					System.err.println("Could not replace " + file.getAbsolutePath());
				}
			}

			if (!tempFile.renameTo(file)) {
				System.err.println("Could not rename temporary file " + tempFile.getAbsolutePath());
			}

		} catch (IOException e) {
			System.err.println("error saving bytes to " + file);
			if (tempFile != null) {
				tempFile.delete();
			}
			e.printStackTrace();
		}
	}

    public static void saveBytes(OutputStream output, byte[] data) {
		try {
			output.write(data);
			output.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    // saveJSONArray()

    public boolean saveJSONArray(JSONArray json, String filename) {
        return saveJSONArray(json, filename, null);
    }

    public boolean saveJSONArray(JSONArray json, String filename, String options) {
        return json.save(saveFile(filename), options);
    }

    // saveJSONObject()

    public boolean saveJSONObject(JSONObject json, String filename) {
        return saveJSONObject(json, filename, null);
    }

    public boolean saveJSONObject(JSONObject json, String filename, String options) {
        return json.save(saveFile(filename), options);
    }

    // saveStream()

    public boolean saveStream(File target, String source) {
		return saveStream(target, createInputRaw(source));
	}

	public boolean saveStream(String target, InputStream source) {
		return saveStream(saveFile(target), source);
	}

	public static boolean saveStream(File target, InputStream source) {
		File tempFile = null;
		try {
			// make sure that this path actually exists before writing
			createPath(target);
			tempFile = createTempFile(target);
			FileOutputStream targetStream = new FileOutputStream(tempFile);

			saveStream(targetStream, source);
			targetStream.close();
			targetStream = null;

			if (target.exists()) {
				if (!target.delete()) {
					System.err.println("Could not replace " +
							target.getAbsolutePath() + ".");
				}
			}
			if (!tempFile.renameTo(target)) {
				System.err.println("Could not rename temporary file " +
						tempFile.getAbsolutePath());
				return false;
			}
			return true;

		} catch (IOException e) {
			if (tempFile != null) {
				tempFile.delete();
			}
			e.printStackTrace();
			return false;
		}
	}

    public static void saveStream(OutputStream target, InputStream source) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(source, 16384);
		BufferedOutputStream bos = new BufferedOutputStream(target);

		byte[] buffer = new byte[8192];
		int bytesRead;
		while ((bytesRead = bis.read(buffer)) != -1) {
			bos.write(buffer, 0, bytesRead);
		}

		bos.flush();
	}

    // saveStrings()

    public void saveStrings(String filename, String[] data) {
        saveStrings(saveFile(filename), data);
    }

    public static void saveStrings(File file, String[] data) {
        saveStrings(createOutput(file), data);
    }

    public static void saveStrings(OutputStream output, String[] data) {
        PrintWriter writer = createWriter(output);
        for (int i = 0; i < data.length; i++) {
            writer.println(data[i]);
        }
        writer.flush();
        writer.close();
    }

    // saveTable()

    public boolean saveTable(Table table, String filename) {
        return saveTable(table, filename, null);
    }

    public boolean saveTable(Table table, String filename, String options) {
        try {
            File outputFile = saveFile(filename);
            return table.save(outputFile, options);
        } catch (IOException e) {
            printStackTrace(e);
            return false;
        }
    }

    // saveXML()

    public boolean saveXML(XML xml, String filename) {
        return saveXML(xml, filename, null);
    }

    public boolean saveXML(XML xml, String filename, String options) {
        return xml.save(saveFile(filename), options);
    }

    // selectOutput()

    public void selectOutput(String prompt, String callback) {
        selectOutput(prompt, callback, null);
    }

    public void selectOutput(String prompt, String callback, File file) {
        selectOutput(prompt, callback, file, this);
    }

    public void selectOutput(String prompt, String callback, File file, Object callbackObject) {
        selectOutput(prompt, callback, file, callbackObject, null, this); // selectFrame());
    }

    public static void selectOutput(String prompt, String callbackMethod, File file, Object callbackObject, Frame parent) {
        selectImpl(prompt, callbackMethod, file, callbackObject, parent, FileDialog.SAVE, null);
    }

    public static void selectOutput(String prompt, String callbackMethod, File file, Object callbackObject, Frame parent, FXApp sketch) {
        selectImpl(prompt, callbackMethod, file, callbackObject, parent, FileDialog.SAVE, sketch);
    }

    ///////////////
    // Transform //
    ///////////////

    // applyMatrix()

    public void applyMatrix(PMatrix source) {
        if (recorder != null)
            recorder.applyMatrix(source);
        pg.applyMatrix(source);
    }

    public void applyMatrix(PMatrix2D source) {
        if (recorder != null)
            recorder.applyMatrix(source);
        pg.applyMatrix(source);
    }

    public void applyMatrix(double n00, double n01, double n02, double n10, double n11, double n12) {
        if (recorder != null)
            recorder.applyMatrix(n00, n01, n02, n10, n11, n12);
        pg.applyMatrix(n00, n01, n02, n10, n11, n12);
    }

    public void applyMatrix(PMatrix3D source) {
        if (recorder != null)
            recorder.applyMatrix(source);
        pg.applyMatrix(source);
    }

    public void applyMatrix(double n00, double n01, double n02, double n03, double n10, double n11, double n12, double n13, double n20, double n21, double n22, double n23, double n30, double n31, double n32, double n33) {
        if (recorder != null)
            recorder.applyMatrix(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33);
        pg.applyMatrix(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33);
    }

    // popMatrix()

    public void popMatrix() {
        if (recorder != null)
            recorder.popMatrix();
        pg.popMatrix();
    }

    // printMatrix()

    public void printMatrix() {
        if (recorder != null)
            recorder.printMatrix();
        pg.printMatrix();
    }

    // pushMatrix()

    public void pushMatrix() {
        if (recorder != null)
            recorder.pushMatrix();
        pg.pushMatrix();
    }

    // resetMatrix()

    public void resetMatrix() {
        if (recorder != null)
            recorder.resetMatrix();
        pg.resetMatrix();
    }

    // rotate()

    public void rotate(double angle) {
        if (recorder != null)
            recorder.rotate(angle);
        pg.rotate(angle);
    }

    public void rotate(double angle, double x, double y, double z) {
        if (recorder != null)
            recorder.rotate(angle, x, y, z);
        pg.rotate(angle, x, y, z);
    }

    // rotateX()

    public void rotateX(double angle) {
        if (recorder != null)
            recorder.rotateX(angle);
        pg.rotateX(angle);
    }

    // rotateY()

    public void rotateY(double angle) {
        if (recorder != null)
            recorder.rotateY(angle);
        pg.rotateY(angle);
    }

    // rotateZ()

    public void rotateZ(double angle) {
        if (recorder != null)
            recorder.rotateZ(angle);
        pg.rotateZ(angle);
    }

    // scale()

    public void scale(double s) {
        if (recorder != null)
            recorder.scale(s);
        pg.scale(s);
    }

    public void scale(double x, double y) {
        if (recorder != null)
            recorder.scale(x, y);
        pg.scale(x, y);
    }

    public void scale(double x, double y, double z) {
        if (recorder != null)
            recorder.scale(x, y, z);
        pg.scale(x, y, z);
    }

    // shearX()

    public void shearX(double angle) {
        if (recorder != null)
            recorder.shearX(angle);
        pg.shearX(angle);
    }

    // shearY()

    public void shearY(double angle) {
        if (recorder != null)
            recorder.shearY(angle);
        pg.shearY(angle);
    }

    // translate()

    public void translate(double x, double y) {
        if (recorder != null)
            recorder.translate(x, y);
        pg.translate(x, y);
    }

    public void translate(double x, double y, double z) {
        if (recorder != null)
            recorder.translate(x, y, z);
        pg.translate(x, y, z);
    }

    ////////////////////
    // Lights, Camera //
    ////////////////////

    ////////////
    // Lights //
    ////////////

    // ambientLight()

    public void ambientLight(double v1, double v2, double v3) {
        if (recorder != null)
            recorder.ambientLight(v1, v2, v3);
        pg.ambientLight(v1, v2, v3);
    }

    public void ambientLight(double v1, double v2, double v3, double x, double y, double z) {
        if (recorder != null)
            recorder.ambientLight(v1, v2, v3, x, y, z);
        pg.ambientLight(v1, v2, v3, x, y, z);
    }

    // directionalLight()

    public void directionalLight(double v1, double v2, double v3, double nx, double ny, double nz) {
        if (recorder != null)
            recorder.directionalLight(v1, v2, v3, nx, ny, nz);
        pg.directionalLight(v1, v2, v3, nx, ny, nz);
    }

    // lightFalloff()

    public void lightFalloff(double constant, double linear, double quadratic) {
        if (recorder != null)
            recorder.lightFalloff(constant, linear, quadratic);
        pg.lightFalloff(constant, linear, quadratic);
    }

    // lights()

    public void lights() {
        if (recorder != null)
            recorder.lights();
        pg.lights();
    }

    // lightSpecular()

    public void lightSpecular(double v1, double v2, double v3) {
        if (recorder != null)
            recorder.lightSpecular(v1, v2, v3);
        pg.lightSpecular(v1, v2, v3);
    }

    // noLights()

    public void noLights() {
        if (recorder != null)
            recorder.noLights();
        pg.noLights();
    }

    // normal()

    public void normal(double nx, double ny, double nz) {
        if (recorder != null)
            recorder.normal(nx, ny, nz);
        pg.normal(nx, ny, nz);
    }

    // pointLight()

    public void pointLight(double v1, double v2, double v3, double x, double y, double z) {
        if (recorder != null)
            recorder.pointLight(v1, v2, v3, x, y, z);
        pg.pointLight(v1, v2, v3, x, y, z);
    }

    // spotLight()

    public void spotLight(double v1, double v2, double v3, double x, double y, double z, double nx, double ny, double nz, double angle, double concentration) {
        if (recorder != null)
            recorder.spotLight(v1, v2, v3, x, y, z, nx, ny, nz, angle, concentration);
        pg.spotLight(v1, v2, v3, x, y, z, nx, ny, nz, angle, concentration);
    }

    ////////////
    // Camera //
    ////////////

    // beginCamera()

    public void beginCamera() {
        if (recorder != null)
            recorder.beginCamera();
        pg.beginCamera();
    }

    // camera()

    public void camera() {
        if (recorder != null)
            recorder.camera();
        pg.camera();
    }

    public void camera(double eyeX, double eyeY, double eyeZ, double centerX, double centerY, double centerZ, double upX, double upY, double upZ) {
        if (recorder != null)
            recorder.camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        pg.camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    // endCamera()

    public void endCamera() {
        if (recorder != null)
            recorder.endCamera();
        pg.endCamera();
    }

    // frustum()

    public void frustum(double left, double right, double bottom, double top, double near, double far) {
        if (recorder != null)
            recorder.frustum(left, right, bottom, top, near, far);
        pg.frustum(left, right, bottom, top, near, far);
    }

    // ortho()

    public void ortho() {
        if (recorder != null)
            recorder.ortho();
        pg.ortho();
    }

    public void ortho(double left, double right, double bottom, double top) {
        if (recorder != null)
            recorder.ortho(left, right, bottom, top);
        pg.ortho(left, right, bottom, top);
    }

    public void ortho(double left, double right, double bottom, double top, double near, double far) {
        if (recorder != null)
            recorder.ortho(left, right, bottom, top, near, far);
        pg.ortho(left, right, bottom, top, near, far);
    }

    // perspective()

    public void perspective() {
        if (recorder != null)
            recorder.perspective();
        pg.perspective();
    }

    public void perspective(double fovy, double aspect, double zNear, double zFar) {
        if (recorder != null)
            recorder.perspective(fovy, aspect, zNear, zFar);
        pg.perspective(fovy, aspect, zNear, zFar);
    }

    // printCamera()

    public void printCamera() {
        if (recorder != null)
            recorder.printCamera();
        pg.printCamera();
    }

    // printProjection()

    public void printProjection() {
        if (recorder != null)
            recorder.printProjection();
        pg.printProjection();
    }

    /////////////////
    // Coordinates //
    /////////////////

    // modelX()

    public double modelX(double x, double y, double z) {
        return pg.modelX(x, y, z);
    }

    // modelY()

    public double modelY(double x, double y, double z) {
        return pg.modelY(x, y, z);
    }

    // modelZ()

    public double modelZ(double x, double y, double z) {
        return pg.modelZ(x, y, z);
    }
    
    // screenX()

    public double screenX(double x, double y) {
        return pg.screenX(x, y);
    }

    public double screenX(double x, double y, double z) {
        return pg.screenX(x, y, z);
    }

    // screenY()

    public double screenY(double x, double y) {
        return pg.screenY(x, y);
    }

    public double screenY(double x, double y, double z) {
        return pg.screenY(x, y, z);
    }

    // screenZ()

    public double screenZ(double x, double y, double z) {
        return pg.screenZ(x, y, z);
    }

    /////////////////////////
    // Material Properties //
    /////////////////////////

    // ambient()

    public void ambient(int rgb) {
        if (recorder != null)
            recorder.ambient(rgb);
        pg.ambient(rgb);
    }

    public void ambient(double gray) {
        if (recorder != null)
            recorder.ambient(gray);
        pg.ambient(gray);
    }

    public void ambient(double v1, double v2, double v3) {
        if (recorder != null)
            recorder.ambient(v1, v2, v3);
        pg.ambient(v1, v2, v3);
    }

    // emissive()

    public void emissive(int rgb) {
        if (recorder != null)
            recorder.emissive(rgb);
        pg.emissive(rgb);
    }

    public void emissive(double gray) {
        if (recorder != null)
            recorder.emissive(gray);
        pg.emissive(gray);
    }

    public void emissive(double v1, double v2, double v3) {
        if (recorder != null)
            recorder.emissive(v1, v2, v3);
        pg.emissive(v1, v2, v3);
    }

    // shininess()

    public void shininess(double shine) {
        if (recorder != null)
            recorder.shininess(shine);
        pg.shininess(shine);
    }

    // specular()

    public void specular(int rgb) {
        if (recorder != null)
            recorder.specular(rgb);
        pg.specular(rgb);
    }

    public void specular(double gray) {
        if (recorder != null)
            recorder.specular(gray);
        pg.specular(gray);
    }

    public void specular(double v1, double v2, double v3) {
        if (recorder != null)
            recorder.specular(v1, v2, v3);
        pg.specular(v1, v2, v3);
    }
    
    ///////////
    // Color //
    ///////////

    /////////////
    // Setting //
    /////////////

    // background()

    public void background(int rgb) {
        if (recorder != null)
            recorder.background(rgb);
        pg.background(rgb);
    }

    public void background(int rgb, double alpha) {
        if (recorder != null)
            recorder.background(rgb, alpha);
        pg.background(rgb, alpha);
    }

    public void background(double gray) {
        if (recorder != null)
            recorder.background(gray);
        pg.background(gray);
    }

    public void background(double gray, double alpha) {
        if (recorder != null)
            recorder.background(gray, alpha);
        pg.background(gray, alpha);
    }

    public void background(double v1, double v2, double v3) {
        if (recorder != null)
            recorder.background(v1, v2, v3);
        pg.background(v1, v2, v3);
    }

    public void background(double v1, double v2, double v3, double alpha) {
        if (recorder != null)
            recorder.background(v1, v2, v3, alpha);
        pg.background(v1, v2, v3, alpha);
    }

    public void background(PImage image) {
        if (recorder != null)
            recorder.background(image);
        pg.background(image);
    }

    // clear()

    public void clear() {
        if (recorder != null)
            recorder.clear();
        pg.clear();
    }

    // colorMode()

    public void colorMode(int mode) {
        if (recorder != null)
            recorder.colorMode(mode);
        pg.colorMode(mode);
    }

    public void colorMode(int mode, double max) {
        if (recorder != null)
            recorder.colorMode(mode, max);
        pg.colorMode(mode, max);
    }

    public void colorMode(int mode, double max1, double max2, double max3) {
        if (recorder != null)
            recorder.colorMode(mode, max1, max2, max3);
        pg.colorMode(mode, max1, max2, max3);
    }

    public void colorMode(int mode, double max1, double max2, double max3, double maxA) {
        if (recorder != null)
            recorder.colorMode(mode, max1, max2, max3, maxA);
        pg.colorMode(mode, max1, max2, max3, maxA);
    }

    // fill()

    public void fill(int rgb) {
        if (recorder != null)
            recorder.fill(rgb);
        pg.fill(rgb);
    }

    public void fill(int rgb, double alpha) {
        if (recorder != null)
            recorder.fill(rgb, alpha);
        pg.fill(rgb, alpha);
    }

    public void fill(double gray) {
        if (recorder != null)
            recorder.fill(gray);
        pg.fill(gray);
    }

    public void fill(double gray, double alpha) {
        if (recorder != null)
            recorder.fill(gray, alpha);
        pg.fill(gray, alpha);
    }

    public void fill(double v1, double v2, double v3) {
        if (recorder != null)
            recorder.fill(v1, v2, v3);
        pg.fill(v1, v2, v3);
    }

    public void fill(double v1, double v2, double v3, double alpha) {
        if (recorder != null)
            recorder.fill(v1, v2, v3, alpha);
        pg.fill(v1, v2, v3, alpha);
    }

    // noFill()

    public void noFill() {
        if (recorder != null)
            recorder.noFill();
        pg.noFill();
    }

    // noStroke()

    public void noStroke() {
        if (recorder != null)
            recorder.noStroke();
        pg.noStroke();
    }
    
    // stroke()

    public void stroke(int rgb) {
        if (recorder != null)
            recorder.stroke(rgb);
        pg.stroke(rgb);
    }

    public void stroke(int rgb, double alpha) {
        if (recorder != null)
            recorder.stroke(rgb, alpha);
        pg.stroke(rgb, alpha);
    }

    public void stroke(double gray) {
        if (recorder != null)
            recorder.stroke(gray);
        pg.stroke(gray);
    }

    public void stroke(double gray, double alpha) {
        if (recorder != null)
            recorder.stroke(gray, alpha);
        pg.stroke(gray, alpha);
    }

    public void stroke(double v1, double v2, double v3) {
        if (recorder != null)
            recorder.stroke(v1, v2, v3);
        pg.stroke(v1, v2, v3);
    }

    public void stroke(double v1, double v2, double v3, double alpha) {
        if (recorder != null)
            recorder.stroke(v1, v2, v3, alpha);
        pg.stroke(v1, v2, v3, alpha);
    }

    ////////////////////////
    // Creating & Reading //
    ////////////////////////

    // alpha()

    public final double alpha(int rgb) {
        return pg.alpha(rgb);
    }

	// blue()

    public final double blue(int rgb) {
        return pg.blue(rgb);
    }

	// brightness()

    public final double brightness(int rgb) {
        return pg.brightness(rgb);
    }

	// color()

    public final int color(int gray) {
        if (pg == null) {
            if (gray > 255)
                gray = 255;
            else if (gray < 0)
                gray = 0;
            return 0xff000000 | (gray << 16) | (gray << 8) | gray;
        }
        return pg.color(gray);
    }

    public final int color(double fgray) {
        if (pg == null) {
            int gray = (int) fgray;
            if (gray > 255)
                gray = 255;
            else if (gray < 0)
                gray = 0;
            return 0xff000000 | (gray << 16) | (gray << 8) | gray;
        }
        return pg.color(fgray);
    }

    public final int color(int gray, int alpha) {
        if (pg == null) {
            if (alpha > 255)
                alpha = 255;
            else if (alpha < 0)
                alpha = 0;
            if (gray > 255) {
                // then assume this is actually a #FF8800
                return (alpha << 24) | (gray & 0xFFFFFF);
            } else {
                // if (gray > 255) gray = 255; else if (gray < 0) gray = 0;
                return (alpha << 24) | (gray << 16) | (gray << 8) | gray;
            }
        }
        return pg.color(gray, alpha);
    }

    public final int color(double fgray, double falpha) {
        if (pg == null) {
            int gray = (int) fgray;
            int alpha = (int) falpha;
            if (gray > 255)
                gray = 255;
            else if (gray < 0)
                gray = 0;
            if (alpha > 255)
                alpha = 255;
            else if (alpha < 0)
                alpha = 0;
            return (alpha << 24) | (gray << 16) | (gray << 8) | gray;
        }
        return pg.color(fgray, falpha);
    }

    public final int color(int v1, int v2, int v3) {
        if (pg == null) {
            if (v1 > 255)
                v1 = 255;
            else if (v1 < 0)
                v1 = 0;
            if (v2 > 255)
                v2 = 255;
            else if (v2 < 0)
                v2 = 0;
            if (v3 > 255)
                v3 = 255;
            else if (v3 < 0)
                v3 = 0;

            return 0xff000000 | (v1 << 16) | (v2 << 8) | v3;
        }
        return pg.color(v1, v2, v3);
    }

    public final int color(int v1, int v2, int v3, int alpha) {
        if (pg == null) {
            if (alpha > 255)
                alpha = 255;
            else if (alpha < 0)
                alpha = 0;
            if (v1 > 255)
                v1 = 255;
            else if (v1 < 0)
                v1 = 0;
            if (v2 > 255)
                v2 = 255;
            else if (v2 < 0)
                v2 = 0;
            if (v3 > 255)
                v3 = 255;
            else if (v3 < 0)
                v3 = 0;

            return (alpha << 24) | (v1 << 16) | (v2 << 8) | v3;
        }
        return pg.color(v1, v2, v3, alpha);
    }

    public final int color(double v1, double v2, double v3) {
        if (pg == null) {
            if (v1 > 255)
                v1 = 255;
            else if (v1 < 0)
                v1 = 0;
            if (v2 > 255)
                v2 = 255;
            else if (v2 < 0)
                v2 = 0;
            if (v3 > 255)
                v3 = 255;
            else if (v3 < 0)
                v3 = 0;

            return 0xff000000 | ((int) v1 << 16) | ((int) v2 << 8) | (int) v3;
        }
        return pg.color(v1, v2, v3);
    }

    public final int color(double v1, double v2, double v3, double alpha) {
        if (pg == null) {
            if (alpha > 255)
                alpha = 255;
            else if (alpha < 0)
                alpha = 0;
            if (v1 > 255)
                v1 = 255;
            else if (v1 < 0)
                v1 = 0;
            if (v2 > 255)
                v2 = 255;
            else if (v2 < 0)
                v2 = 0;
            if (v3 > 255)
                v3 = 255;
            else if (v3 < 0)
                v3 = 0;

            return ((int) alpha << 24) | ((int) v1 << 16) | ((int) v2 << 8) | (int) v3;
        }
        return pg.color(v1, v2, v3, alpha);
    }

	// green()

    public final double green(int rgb) {
        return pg.green(rgb);
    }

	// hue()

    public final double hue(int rgb) {
        return pg.hue(rgb);
    }

	// lerpColor()

    public static int lerpColor(int c1, int c2, double amt, int mode) {
        return PGraphics.lerpColor(c1, c2, amt, mode);
    }

    public int lerpColor(int c1, int c2, double amt) {
        if (pg != null) {
            return pg.lerpColor(c1, c2, amt);
        }
        return PGraphics.lerpColor(c1, c2, amt, RGB);
    }
    
	// red()

    public final double red(int rgb) {
        return pg.red(rgb);
    }

	// saturation()

    public final double saturation(int rgb) {
        return pg.saturation(rgb);
    }

    ///////////
    // Image //
    ///////////

    // createImage()

    public PImage createImage(int w, int h, int format) {
        PImage image = new PImage(w, h, format);
        image.parent = this; // make save() work
        return image;
    }

    //////////////////////////
    // Loading & Displaying //
    //////////////////////////

    private static final String REQUEST_IMAGE_THREAD_PREFIX = "requestImage";
    protected String[] loadImageFormats;

    // image()

    public void image(PImage img, double a, double b) {
        if (recorder != null)
            recorder.image(img, a, b);
        pg.image(img, a, b);
    }

    public void image(PImage img, double a, double b, double c, double d) {
        if (recorder != null)
            recorder.image(img, a, b, c, d);
        System.out.println("Called image(1)");
        pg.image(img, a, b, c, d);
    }

    public void image(PImage img, double a, double b, double c, double d, int u1, int v1, int u2, int v2) {
        if (recorder != null)
            recorder.image(img, a, b, c, d, u1, v1, u2, v2);
        pg.image(img, a, b, c, d, u1, v1, u2, v2);
    }

    // imageMode()

    public void imageMode(int mode) {
        if (recorder != null)
            recorder.imageMode(mode);
        pg.imageMode(mode);
    }

    // loadImage()

    public PImage loadImage(String filename) {
        return loadImage(filename, null);
    }

    public PImage loadImage(String filename, String extension) { // , Object params) {

        if (pg != null && !Thread.currentThread().getName().startsWith(REQUEST_IMAGE_THREAD_PREFIX)) {
            pg.awaitAsyncSaveCompletion(filename);
        }

        if (extension == null) {
            String lower = filename.toLowerCase();
            int dot = filename.lastIndexOf('.');
            if (dot == -1) {
                extension = "unknown"; // no extension found

            } else {
                extension = lower.substring(dot + 1);
                int question = extension.indexOf('?');
                if (question != -1) extension = extension.substring(0, question);
            }
        }

        // just in case. them users will try anything!
        extension = extension.toLowerCase();

        if (extension.equals("tga")) {
            try {
                PImage image = loadImageTGA(filename);
                // if (params != null) {
                // image.setParams(g, params);
                // }
                return image;
            } catch (IOException e) {
                printStackTrace(e);
                return null;
            }
        }

        if (extension.equals("tif") || extension.equals("tiff")) {
            byte[] bytes = loadBytes(filename);
            PImage image = (bytes == null) ? null : PImage.loadTIFF(bytes);
            return image;
        }

        try {
            if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("gif") || extension.equals("png") || extension.equals("unknown")) {
                byte[] bytes = loadBytes(filename);
                if (bytes == null) {
                    return null;
                } else {
                    // Image awtImage = Toolkit.getDefaultToolkit().createImage(bytes);
                    Image awtImage = new ImageIcon(bytes).getImage();

                    if (awtImage instanceof BufferedImage) {
                        BufferedImage buffImage = (BufferedImage) awtImage;
                        int space = buffImage.getColorModel().getColorSpace().getType();
                        if (space == ColorSpace.TYPE_CMYK) {
                            System.err.println(filename + " is a CMYK image, " + "only RGB images are supported.");
                            return null;
                        }
                    }

                    PImage image = new PImage(awtImage);
                    if (image.width == -1) {
                        System.err
                                .println("The file " + filename + " contains bad image data, or may not be an image.");
                    }

                    // if it's a .gif image, test to see if it has transparency
                    if (extension.equals("gif") || extension.equals("png") || extension.equals("unknown")) {
                        image.checkAlpha();
                    }

                    // if (params != null) {
                    // image.setParams(g, params);
                    // }
                    image.parent = this;
                    return image;
                }
            }
        } catch (Exception e) {
            // show error, but move on to the stuff below, see if it'll work
            printStackTrace(e);
        }

        if (loadImageFormats == null) {
            loadImageFormats = ImageIO.getReaderFormatNames();
        }
        if (loadImageFormats != null) {
            for (int i = 0; i < loadImageFormats.length; i++) {
                if (extension.equals(loadImageFormats[i])) {
                    return loadImageIO(filename);
                    // PImage image = loadImageIO(filename);
                    // if (params != null) {
                    // image.setParams(g, params);
                    // }
                    // return image;
                }
            }
        }

        // failed, could not load image after all those attempts
        System.err.println("Could not find a method to load " + filename);
        return null;
    }

    protected PImage loadImageTGA(String filename) throws IOException {
        InputStream is = createInput(filename);
        if (is == null)
            return null;

        byte[] header = new byte[18];
        int offset = 0;
        do {
            int count = is.read(header, offset, header.length - offset);
            if (count == -1)
                return null;
            offset += count;
        } while (offset < 18);

        int format = 0;

        if (((header[2] == 3) || (header[2] == 11)) && // B&W, plus RLE or not
                (header[16] == 8) && // 8 bits
                ((header[17] == 0x8) || (header[17] == 0x28))) { // origin, 32 bit
            format = ALPHA;

        } else if (((header[2] == 2) || (header[2] == 10)) && // RGB, RLE or not
                (header[16] == 24) && // 24 bits
                ((header[17] == 0x20) || (header[17] == 0))) { // origin
            format = RGB;

        } else if (((header[2] == 2) || (header[2] == 10)) && (header[16] == 32)
                && ((header[17] == 0x8) || (header[17] == 0x28))) { // origin, 32
            format = ARGB;
        }

        if (format == 0) {
            System.err.println("Unknown .tga file format for " + filename);
            return null;
        }

        int w = ((header[13] & 0xff) << 8) + (header[12] & 0xff);
        int h = ((header[15] & 0xff) << 8) + (header[14] & 0xff);
        PImage outgoing = createImage(w, h, format);

        // where "reversed" means upper-left corner (normal for most of
        // the modernized world, but "reversed" for the tga spec)
        // boolean reversed = (header[17] & 0x20) != 0;
        // https://github.com/processing/processing/issues/1682
        boolean reversed = (header[17] & 0x20) == 0;

        if ((header[2] == 2) || (header[2] == 3)) { // not RLE encoded
            if (reversed) {
                int index = (h - 1) * w;
                switch (format) {
                    case ALPHA:
                        for (int y = h - 1; y >= 0; y--) {
                            for (int x = 0; x < w; x++) {
                                outgoing.pixels[index + x] = is.read();
                            }
                            index -= w;
                        }
                        break;
                    case RGB:
                        for (int y = h - 1; y >= 0; y--) {
                            for (int x = 0; x < w; x++) {
                                outgoing.pixels[index + x] = is.read() | (is.read() << 8) | (is.read() << 16)
                                        | 0xff000000;
                            }
                            index -= w;
                        }
                        break;
                    case ARGB:
                        for (int y = h - 1; y >= 0; y--) {
                            for (int x = 0; x < w; x++) {
                                outgoing.pixels[index + x] = is.read() | (is.read() << 8) | (is.read() << 16)
                                        | (is.read() << 24);
                            }
                            index -= w;
                        }
                }
            } else { // not reversed
                int count = w * h;
                switch (format) {
                    case ALPHA:
                        for (int i = 0; i < count; i++) {
                            outgoing.pixels[i] = is.read();
                        }
                        break;
                    case RGB:
                        for (int i = 0; i < count; i++) {
                            outgoing.pixels[i] = is.read() | (is.read() << 8) | (is.read() << 16) | 0xff000000;
                        }
                        break;
                    case ARGB:
                        for (int i = 0; i < count; i++) {
                            outgoing.pixels[i] = is.read() | (is.read() << 8) | (is.read() << 16) | (is.read() << 24);
                        }
                        break;
                }
            }

        } else { // header[2] is 10 or 11
            int index = 0;
            int[] px = outgoing.pixels;

            while (index < px.length) {
                int num = is.read();
                boolean isRLE = (num & 0x80) != 0;
                if (isRLE) {
                    num -= 127; // (num & 0x7F) + 1
                    int pixel = 0;
                    switch (format) {
                        case ALPHA:
                            pixel = is.read();
                            break;
                        case RGB:
                            pixel = 0xFF000000 | is.read() | (is.read() << 8) | (is.read() << 16);
                            break;
                        case ARGB:
                            pixel = is.read() | (is.read() << 8) | (is.read() << 16) | (is.read() << 24);
                            break;
                    }
                    for (int i = 0; i < num; i++) {
                        px[index++] = pixel;
                        if (index == px.length)
                            break;
                    }
                } else { // write up to 127 bytes as uncompressed
                    num += 1;
                    switch (format) {
                        case ALPHA:
                            for (int i = 0; i < num; i++) {
                                px[index++] = is.read();
                            }
                            break;
                        case RGB:
                            for (int i = 0; i < num; i++) {
                                px[index++] = 0xFF000000 | is.read() | (is.read() << 8) | (is.read() << 16);
                            }
                            break;
                        case ARGB:
                            for (int i = 0; i < num; i++) {
                                px[index++] = is.read() | (is.read() << 8) | (is.read() << 16) | (is.read() << 24);
                            }
                            break;
                    }
                }
            }

            if (!reversed) {
                int[] temp = new int[w];
                for (int y = 0; y < h / 2; y++) {
                    int z = (h - 1) - y;
                    System.arraycopy(px, y * w, temp, 0, w);
                    System.arraycopy(px, z * w, px, y * w, w);
                    System.arraycopy(temp, 0, px, z * w, w);
                }
            }
        }
        is.close();
        return outgoing;
    }

    protected PImage loadImageIO(String filename) {
        InputStream stream = createInput(filename);
        if (stream == null) {
            System.err.println("The image " + filename + " could not be found.");
            return null;
        }

        try {
            BufferedImage bi = ImageIO.read(stream);
            PImage outgoing = new PImage(bi.getWidth(), bi.getHeight());
            outgoing.parent = this;
            bi.getRGB(0, 0, outgoing.width, outgoing.height, outgoing.pixels, 0, outgoing.width);
            outgoing.checkAlpha();
            stream.close();
            return outgoing;

        } catch (Exception e) {
            printStackTrace(e);
            return null;
        }
    }

    // noTint()

    public void noTint() {
        if (recorder != null)
            recorder.noTint();
        pg.noTint();
    }

    // requestImage()

    ExecutorService requestImagePool;

    public PImage requestImage(String filename) {
        return requestImage(filename, null);
    }

    public PImage requestImage(String filename, String extension) {
        // Make sure saving to this file completes before trying to load it
        // Has to be called on main thread, because P2D and P3D need GL functions
        if (pg != null) {
            pg.awaitAsyncSaveCompletion(filename);
        }
        PImage vessel = createImage(0, 0, ARGB);

        // if the image loading thread pool hasn't been created, create it
        if (requestImagePool == null) {
            ThreadFactory factory = new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    return new Thread(r, REQUEST_IMAGE_THREAD_PREFIX);
                }
            };
            requestImagePool = Executors.newFixedThreadPool(4, factory);
        }
        requestImagePool.execute(() -> {
            PImage actual = loadImage(filename, extension);

            // An error message should have already printed
            if (actual == null) {
                vessel.width = -1;
                vessel.height = -1;

            } else {
                vessel.width = actual.width;
                vessel.height = actual.height;
                vessel.format = actual.format;
                vessel.pixels = actual.pixels;

                vessel.pixelWidth = actual.width;
                vessel.pixelHeight = actual.height;
                vessel.pixelDensity = 1;
            }
        });
        return vessel;
    }

    // tint()

    public void tint(int rgb) {
        if (recorder != null)
            recorder.tint(rgb);
        pg.tint(rgb);
    }

    public void tint(int rgb, double alpha) {
        if (recorder != null)
            recorder.tint(rgb, alpha);
        pg.tint(rgb, alpha);
    }

    public void tint(double gray) {
        if (recorder != null)
            recorder.tint(gray);
        pg.tint(gray);
    }

    public void tint(double gray, double alpha) {
        if (recorder != null)
            recorder.tint(gray, alpha);
        pg.tint(gray, alpha);
    }

    public void tint(double v1, double v2, double v3) {
        if (recorder != null)
            recorder.tint(v1, v2, v3);
        pg.tint(v1, v2, v3);
    }

    public void tint(double v1, double v2, double v3, double alpha) {
        if (recorder != null)
            recorder.tint(v1, v2, v3, alpha);
        pg.tint(v1, v2, v3, alpha);
    }

    //////////////
    // Textures //
    //////////////

    public void noTexture() {
        if (recorder != null)
            recorder.noTexture();
        pg.noTexture();
    }

    // texture()

    public void texture(PImage image) {
        if (recorder != null)
            recorder.texture(image);
        pg.texture(image);
    }

    // textureMode()

    public void textureMode(int mode) {
        if (recorder != null)
            recorder.textureMode(mode);
        pg.textureMode(mode);
    }

    // textureWrap()

    public void textureWrap(int wrap) {
        if (recorder != null)
            recorder.textureWrap(wrap);
        pg.textureWrap(wrap);
    }

    ////////////
    // Pixels //
    ////////////

    // blend()

    public void blend(int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh, int mode) {
        if (recorder != null)
            recorder.blend(sx, sy, sw, sh, dx, dy, dw, dh, mode);
        pg.blend(sx, sy, sw, sh, dx, dy, dw, dh, mode);
    }

    public void blend(PImage src, int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh, int mode) {
        if (recorder != null)
            recorder.blend(src, sx, sy, sw, sh, dx, dy, dw, dh, mode);
        pg.blend(src, sx, sy, sw, sh, dx, dy, dw, dh, mode);
    }

    // copy()

    public void copy(int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh) {
        if (recorder != null)
            recorder.copy(sx, sy, sw, sh, dx, dy, dw, dh);
        pg.copy(sx, sy, sw, sh, dx, dy, dw, dh);
    }

    public void copy(PImage src, int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh) {
        if (recorder != null)
            recorder.copy(src, sx, sy, sw, sh, dx, dy, dw, dh);
        pg.copy(src, sx, sy, sw, sh, dx, dy, dw, dh);
    }

    // filter()

    public void filter(int kind) {
        if (recorder != null)
            recorder.filter(kind);
        pg.filter(kind);
    }

    public void filter(int kind, double param) {
        if (recorder != null)
            recorder.filter(kind, param);
        pg.filter(kind, param);
    }

    public void filter(PShader shader) {
        if (recorder != null)
            recorder.filter(shader);
        pg.filter(shader);
    }

    // get()

    public int get(int x, int y) {
        return pg.get(x, y);
    }

    public PImage get() {
        return pg.get();
    }

    public PImage copy() {
        return pg.copy();
    }

    public PImage get(int x, int y, int w, int h) {
        return pg.get(x, y, w, h);
    }

    // loadPixels()

    public void loadPixels() {
        pg.loadPixels();
        pixels = pg.pixels;
    }

    // set()

    public void set(int x, int y, int c) {
        if (recorder != null)
            recorder.set(x, y, c);
        pg.set(x, y, c);
    }

    public void set(int x, int y, PImage img) {
        if (recorder != null)
            recorder.set(x, y, img);
        pg.set(x, y, img);
    }

    // updatePixels()

    public void updatePixels() {
        pg.updatePixels();
    }

    public void updatePixels(int x1, int y1, int x2, int y2) {
        pg.updatePixels(x1, y1, x2, y2);
    }

    ///////////////
    // Rendering //
    ///////////////

    protected PGraphics makeGraphics(int w, int h, String renderer, String path, boolean primary) {

        if (!primary && !pg.isGL()) {
            if (renderer.equals(P2D)) {
                throw new RuntimeException("createGraphics() with P2D requires size() to use P2D or P3D");
            } else if (renderer.equals(P3D)) {
                throw new RuntimeException("createGraphics() with P3D or OPENGL requires size() to use P2D or P3D");
            }
        }

        try {
            //Class<?> rendererClass = Thread.currentThread().getContextClassLoader().loadClass(renderer);
            //Constructor<?> constructor = rendererClass.getConstructor(new Class[] {});
            //PGraphics pg = (PGraphics) constructor.newInstance();
            pg = new PGraphics(gc, this);
            pg.setParent(this);
            pg.setPrimary(primary);
            if (path != null) {
                pg.setPath(savePath(path));
            }
            pg.setSize(w, h);
            println(primary+" "+w+" "+h);
            return pg;
        } catch (Exception e) {
            if ((e instanceof IllegalArgumentException) || (e instanceof NoSuchMethodException)
                    || (e instanceof IllegalAccessException)) {
                if (e.getMessage().contains("cannot be <= 0")) {
                    throw new RuntimeException(e);

                } else {
                    printStackTrace(e);
                    String msg = renderer + " needs to be updated " + "for the current release of Processing.";
                    throw new RuntimeException(msg);
                }
            } else {
                printStackTrace(e);
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    protected PGraphics createPrimaryGraphics() {
        return makeGraphics(sketchWidth(), sketchHeight(), sketchRenderer(), sketchOutputPath(), true);
    }

    // blendMode()

    public void blendMode(int mode) {
        if (recorder != null)
            recorder.blendMode(mode);
        pg.blendMode(mode);
    }

    // clip()

    public void clip(double a, double b, double c, double d) {
        if (recorder != null)
            recorder.clip(a, b, c, d);
        pg.clip(a, b, c, d);
    }

    // createGraphics()

    public PGraphics createGraphics(int w, int h) {
        return createGraphics(w, h, JAVA2D);
    }

    public PGraphics createGraphics(int w, int h, String renderer) {
        return createGraphics(w, h, renderer, null);
    }

    public PGraphics createGraphics(int w, int h, String renderer, String path) {
        return makeGraphics(w, h, renderer, path, false);
    }

    // hint()

    public void hint(int which) {
        if (recorder != null)
            recorder.hint(which);
        pg.hint(which);
    }

    // noClip()

    public void noClip() {
        if (recorder != null)
            recorder.noClip();
        pg.noClip();
    }

    /////////////
    // Shaders //
    /////////////

    // loadShader()

    public PShader loadShader(String fragFilename) {
        return pg.loadShader(fragFilename);
    }

    public PShader loadShader(String fragFilename, String vertFilename) {
        return pg.loadShader(fragFilename, vertFilename);
    }

    // resetShader()

    public void resetShader() {
        if (recorder != null)
            recorder.resetShader();
        pg.resetShader();
    }

    public void resetShader(int kind) {
        if (recorder != null)
            recorder.resetShader(kind);
        pg.resetShader(kind);
    }

    // shader()

    public void shader(PShader shader) {
        if (recorder != null)
            recorder.shader(shader);
        pg.shader(shader);
    }

    public void shader(PShader shader, int kind) {
        if (recorder != null)
            recorder.shader(shader, kind);
        pg.shader(shader, kind);
    }

    ////////////////
    // Typography //
    ////////////////

    //////////////////////////
	// Loading & Displaying //
    //////////////////////////

    // createFont()

    public PFont createFont(String name, double size) {
        return createFont(name, size, true, null);
    }

    public PFont createFont(String name, double size, boolean smooth) {
        return createFont(name, size, smooth, null);
    }

    public PFont createFont(String name, double size, boolean smooth, char[] charset) {
        if (pg == null) {
            throw new RuntimeException("createFont() can only be used inside setup() or after setup() has been called.");
        }
        return pg.createFont(name, size, smooth, charset);
    }

    // loadFont()

    public PFont loadFont(String filename) {
        if (!filename.toLowerCase().endsWith(".vlw")) {
            throw new IllegalArgumentException("loadFont() is for .vlw files, try createFont()");
        }
        try {
            InputStream input = createInput(filename);
            return new PFont(input);

        } catch (Exception e) {
            die("Could not load font " + filename + ". " + "Make sure that the font has been copied " + "to the data folder of your sketch.", e);
        }
        return null;
    }

    // text()

    public void text(char c, double x, double y) {
        if (recorder != null)
            recorder.text(c, x, y);
        pg.text(c, x, y);
    }

    public void text(char c, double x, double y, double z) {
        if (recorder != null)
            recorder.text(c, x, y, z);
        pg.text(c, x, y, z);
    }

    public void text(String str, double x, double y) {
        if (recorder != null)
            recorder.text(str, x, y);
        pg.text(str, x, y);
    }

    public void text(char[] chars, int start, int stop, double x, double y) {
        if (recorder != null)
            recorder.text(chars, start, stop, x, y);
        pg.text(chars, start, stop, x, y);
    }

    public void text(String str, double x, double y, double z) {
        if (recorder != null)
            recorder.text(str, x, y, z);
        pg.text(str, x, y, z);
    }

    public void text(char[] chars, int start, int stop, double x, double y, double z) {
        if (recorder != null)
            recorder.text(chars, start, stop, x, y, z);
        pg.text(chars, start, stop, x, y, z);
    }

    public void text(String str, double x1, double y1, double x2, double y2) {
        if (recorder != null)
            recorder.text(str, x1, y1, x2, y2);
        pg.text(str, x1, y1, x2, y2);
    }

    public void text(int num, double x, double y) {
        if (recorder != null)
            recorder.text(num, x, y);
        pg.text(num, x, y);
    }

    public void text(int num, double x, double y, double z) {
        if (recorder != null)
            recorder.text(num, x, y, z);
        pg.text(num, x, y, z);
    }

    public void text(double num, double x, double y) {
        if (recorder != null)
            recorder.text(num, x, y);
        pg.text(num, x, y);
    }

    public void text(double num, double x, double y, double z) {
        if (recorder != null)
            recorder.text(num, x, y, z);
        pg.text(num, x, y, z);
    }

    // textFont()

    public void textFont(PFont which) {
        if (recorder != null)
            recorder.textFont(which);
        pg.textFont(which);
    }

    public void textFont(PFont which, double size) {
        if (recorder != null)
            recorder.textFont(which, size);
        pg.textFont(which, size);
    }
	
    ////////////////
	// Attributes //
    ////////////////

    // textAlign()

    public void textAlign(int alignX) {
        if (recorder != null)
            recorder.textAlign(alignX);
        pg.textAlign(alignX);
    }

    public void textAlign(int alignX, int alignY) {
        if (recorder != null)
            recorder.textAlign(alignX, alignY);
        pg.textAlign(alignX, alignY);
    }

    // textLeading()

    public void textLeading(double leading) {
        if (recorder != null)
            recorder.textLeading(leading);
        pg.textLeading(leading);
    }

    // textMode()

    public void textMode(int mode) {
        if (recorder != null)
            recorder.textMode(mode);
        pg.textMode(mode);
    }

    // textSize()

    public void textSize(double size) {
        if (recorder != null)
            recorder.textSize(size);
        pg.textSize(size);
    }

    // textWidth()

    public double textWidth(char c) {
        return pg.textWidth(c);
    }

    public double textWidth(String str) {
        return pg.textWidth(str);
    }

    public double textWidth(char[] chars, int start, int length) {
        return pg.textWidth(chars, start, length);
    }
	
    /////////////
	// Metrics //
    /////////////

    // textAscent()

    public double textAscent() {
        return pg.textAscent();
    }

    // textDescent()
	
    public double textDescent() {
        return pg.textDescent();
    }

}