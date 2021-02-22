package main;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.zip.*;

import javax.imageio.ImageIO;

import event.*;
import types.*;

import static utils.Constants.*;

public class FXApp {

    public PGraphics pg, recorder;
    public int displayWidth, displayHeight;
    public String[] args;
    public boolean keyRepeatEnabled = false;
    public boolean focused = false;
    protected PSurface surface;
    public static int platform;
    private String sketchPath;
	public int pixelDensity;

    public static final int DEFAULT_WIDTH = 100;
    public static final int DEFAULT_HEIGHT = 100;
    public int[] pixels;
    public int width = DEFAULT_WIDTH;
    public int height = DEFAULT_HEIGHT;

    long millisOffset = System.currentTimeMillis();
    public double frameRate = 60;
    public int frameCount;
    protected boolean looping = true;
    protected boolean redraw = true;
    public volatile boolean finished;
    static Throwable uncaughtThrowable;
    protected boolean exitCalled;

    protected void printStackTrace(Throwable t) {
        t.printStackTrace();
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
    // noLoop()
    // pop()
    // popStyle()
    // push()
    // pushStyle()
    // redraw()
    // setLocation()
    // setResizable()
    // setTitle()
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
    // delay()
    // displayDensity()
    // frameRate()
    // fullScreen()
    // noCursor()
    // noSmooth()
    // pixelDensity()
    // settings()
    // size()
    // smooth()

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
    List<Long> pressedKeys = new ArrayList<>(6);

    // keyPressed()
    // keyReleased()
    // keyTyped()

    ///////////
    // Files //
    ///////////

    static protected String calcSketchPath() {
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

    static public InputStream createInput(File file) {
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

    static public BufferedReader createReader(File file) {
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

    static public BufferedReader createReader(InputStream input) {
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
    // loadBytes()
    // loadJSONArray()
    // loadJSONObject()
    // loadStrings()

    static public String[] loadStrings(File file) {
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

    static public String[] loadStrings(InputStream input) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            return loadStrings(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static public String[] loadStrings(BufferedReader reader) {
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
    // loadXML()
    // parseJSONArray()
    // parseJSONObject()
    // parseXML()
    // selectFolder()
    // selectInput()

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

    static public void createPath(String path) {
        createPath(new File(path));
    }

    static public void createPath(File file) {
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

    // save()
    // saveFrame()
    
    ///////////
    // Files //
    ///////////

    // beginRaw()
    // beginRecord()
    // createOutput()

    public OutputStream createOutput(String filename) {
        return createOutput(saveFile(filename));
    }

    static public OutputStream createOutput(File file) {
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

    static public PrintWriter createWriter(File file) {
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

    static public PrintWriter createWriter(OutputStream output) {
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
    // endRecord()
    // saveBytes()
    // saveJSONArray()
    // saveJSONObject()
    // saveStream()
    // saveStrings()
    // saveTable()
    // saveXML()
    // selectOutput()

    ///////////
    // Image //
    ///////////

    // createImage()

    //////////////////////////
    // Loading & Displaying //
    //////////////////////////

    static private final String REQUEST_IMAGE_THREAD_PREFIX = "requestImage";
    protected String[] loadImageFormats;

    // image()
    // imageMode()
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
            // if (params != null) {
            // image.setParams(g, params);
            // }
            return image;
        }

        try {
            if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("gif")
                    || extension.equals("png") || extension.equals("unknown")) {
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
                            /*
                             * // wishful thinking, appears to not be supported //
                             * https://community.oracle.com/thread/1272045?start=0&tstart=0 BufferedImage
                             * destImage = new BufferedImage(buffImage.getWidth(), buffImage.getHeight(),
                             * BufferedImage.TYPE_3BYTE_BGR); ColorConvertOp op = new ColorConvertOp(null);
                             * op.filter(buffImage, destImage); image = new PImage(destImage);
                             */
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
                            // (is.read() << 16) | (is.read() << 8) | is.read();
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
                                // (is.read() << 16) | (is.read() << 8) | is.read();
                            }
                            break;
                        case ARGB:
                            for (int i = 0; i < num; i++) {
                                px[index++] = is.read() | // (is.read() << 24) |
                                        (is.read() << 8) | (is.read() << 16) | (is.read() << 24);
                                // (is.read() << 16) | (is.read() << 8) | is.read();
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

    // noTint()
    // requestImage()
    // tint()

}