package types;

import static utils.ColourUtils.*;
import static utils.Constants.*;
import static utils.DataUtils.*;
import static utils.MathUtils.*;

// Used for the 'image' object that's been here forever
import java.awt.Image;
import java.io.File;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Shape;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import main.FXApp;

public class PGraphics extends PImage {

    // Variables --> JavaFX
    GraphicsContext context;
    static final WritablePixelFormat<IntBuffer> argbFormat = PixelFormat.getIntArgbInstance();
    WritableImage snapshotImage;

    Path2D workPath = new Path2D();
    Path2D auxPath = new Path2D();
    boolean openContour;
    boolean adjustedForThinLines;
    boolean breakShape;

    private double[] pathCoordsBuffer = new double[6];

    double[] curveCoordX;
    double[] curveCoordY;
    double[] curveDrawX;
    double[] curveDrawY;

    int transformCount;
    Affine[] transformStack = new Affine[MATRIX_STACK_DEPTH];

    // Variables --> General
    public int pixelCount;
    public int smooth;
    protected boolean settingsInited;
    protected boolean reapplySettings;
    protected PGraphics raw;
    protected String path;
    protected boolean primaryGraphics;
    protected boolean[] hints = new boolean[HINT_COUNT];
    protected WeakHashMap<PImage, Object> cacheMap = new WeakHashMap<>();

    public int colorMode; // = RGB;
    public double colorModeX; // = 255;
    public double colorModeY; // = 255;
    public double colorModeZ; // = 255;
    public double colorModeA; // = 255;
    boolean colorModeScale; // = true;
    boolean colorModeDefault = true;

    public boolean tint;
    public int tintColor;
    protected boolean tintAlpha;
    protected double tintR, tintG, tintB, tintA;
    protected int tintRi, tintGi, tintBi, tintAi;

    public boolean fill;
    public int fillColor = 0xffFFFFFF;
    protected boolean fillAlpha;
    protected double fillR, fillG, fillB, fillA;
    protected int fillRi, fillGi, fillBi, fillAi;

    public boolean stroke;
    public int strokeColor = 0xff000000;
    protected boolean strokeAlpha;
    protected double strokeR, strokeG, strokeB, strokeA;
    protected int strokeRi, strokeGi, strokeBi, strokeAi;

    static protected final double DEFAULT_STROKE_WEIGHT = 1;
    static protected final int DEFAULT_STROKE_JOIN = MITER;
    static protected final int DEFAULT_STROKE_CAP = ROUND;

    public double strokeWeight = DEFAULT_STROKE_WEIGHT;
    public int strokeJoin = DEFAULT_STROKE_JOIN;
    public int strokeCap = DEFAULT_STROKE_CAP;

    public int rectMode;
    public int ellipseMode;
    public int shapeMode;
    public int imageMode = CORNER;

    boolean hasFill;

    public PFont textFont;
    public int textAlign = LEFT;
    public int textAlignY = BASELINE;
    public int textMode = MODEL;
    public double textSize;
    public double textLeading;
    static final protected String ERROR_TEXTFONT_NULL_PFONT = "A null PFont was passed to textFont()";

    public int ambientColor;
    public double ambientR, ambientG, ambientB;
    public boolean setAmbient;
    public int specularColor;
    public double specularR, specularG, specularB;
    public int emissiveColor;
    public double emissiveR, emissiveG, emissiveB;
    public double shininess;

    static final int STYLE_STACK_DEPTH = 64;
    PStyle[] styleStack = new PStyle[STYLE_STACK_DEPTH];
    int styleStackDepth;

    public int backgroundColor = 0xffCCCCCC;
    protected boolean backgroundAlpha;
    protected double backgroundR, backgroundG, backgroundB, backgroundA;
    protected int backgroundRi, backgroundGi, backgroundBi, backgroundAi;
    static final protected String ERROR_BACKGROUND_IMAGE_SIZE = "background image must be the same size as your application";
    static final protected String ERROR_BACKGROUND_IMAGE_FORMAT = "background images should be RGB or ARGB";

    protected int blendMode;

    static final protected int MATRIX_STACK_DEPTH = 32;
    static final protected String ERROR_PUSHMATRIX_OVERFLOW = "Too many calls to pushMatrix().";
    static final protected String ERROR_PUSHMATRIX_UNDERFLOW = "Too many calls to popMatrix(), and not enough to pushMatrix().";

    public Image image;
    protected PSurface surface;

    protected double calcR, calcG, calcB, calcA;
    protected int calcRi, calcGi, calcBi, calcAi;
    protected int calcColor;
    protected boolean calcAlpha;

    int cacheHsbKey; // The last RGB value converted to HSB
    double[] cacheHsbValue = new double[3]; // Result of the last conversion to HSB

    protected int shape;
    public static final int DEFAULT_VERTICES = 512;
    protected double[][] vertices = new double[DEFAULT_VERTICES][VERTEX_FIELD_COUNT];
    protected int vertexCount; // total number of vertices

    protected boolean bezierInited = false;
    public int bezierDetail = 20;
    protected PMatrix3D bezierBasisMatrix = new PMatrix3D(-1, 3, -3, 1, 3, -6, 3, 0, -3, 3, 0, 0, 1, 0, 0, 0);
    protected PMatrix3D bezierDrawMatrix;

    protected boolean curveInited = false;
    public int curveDetail = 20;
    public double curveTightness = 0;
    protected PMatrix3D curveBasisMatrix;
    protected PMatrix3D curveDrawMatrix;
    protected PMatrix3D bezierBasisInverse;
    protected PMatrix3D curveToBezierMatrix;
    protected double[][] curveVertices;
    protected int curveVertexCount;

    public static final double[] sinLUT;
    public static final double[] cosLUT;
    static final protected double SINCOS_PRECISION = 0.5f;
    public static final int SINCOS_LENGTH = (int) (360f / SINCOS_PRECISION);
    static {
        sinLUT = new double[SINCOS_LENGTH];
        cosLUT = new double[SINCOS_LENGTH];
        for (int i = 0; i < SINCOS_LENGTH; i++) {
            sinLUT[i] = Math.sin(i * DEG_TO_RAD * SINCOS_PRECISION);
            cosLUT[i] = Math.cos(i * DEG_TO_RAD * SINCOS_PRECISION);
        }
    }

    protected char[] textBuffer = new char[8 * 1024];
    protected char[] textWidthBuffer = new char[8 * 1024];

    protected int textBreakCount;
    protected int[] textBreakStart;
    protected int[] textBreakStop;

    public boolean edge = true;

    static protected final int NORMAL_MODE_AUTO = 0;
    static protected final int NORMAL_MODE_SHAPE = 1;
    static protected final int NORMAL_MODE_VERTEX = 2;
    protected int normalMode;
    protected boolean autoNormal;
    public double normalX, normalY, normalZ;

    public int textureMode = IMAGE;
    public double textureU;
    public double textureV;
    public PImage textureImage;

    protected double[] sphereX;
    protected double[] sphereY;
    protected double[] sphereZ;
    public int sphereDetailU = 0;
    public int sphereDetailV = 0;

    static public final int R = 3; // actual rgb, after lighting
    static public final int G = 4; // fill stored here, transform in place
    static public final int B = 5;
    static public final int A = 6;

    static public final int U = 7; // texture
    static public final int V = 8;

    static public final int NX = 9; // normal
    static public final int NY = 10;
    static public final int NZ = 11;

    static public final int EDGE = 12;

    // stroke

    /** stroke argb values */
    static public final int SR = 13;
    static public final int SG = 14;
    static public final int SB = 15;
    static public final int SA = 16;

    /** stroke weight */
    static public final int SW = 17;

    // transformations (2D and 3D)

    static public final int TX = 18; // transformed xyzw
    static public final int TY = 19;
    static public final int TZ = 20;

    static public final int VX = 21; // view space coords
    static public final int VY = 22;
    static public final int VZ = 23;
    static public final int VW = 24;

    // material properties

    // Ambient color (usually to be kept the same as diffuse)
    // fill(_) sets both ambient and diffuse.
    static public final int AR = 25;
    static public final int AG = 26;
    static public final int AB = 27;

    // Diffuse is shared with fill.
    static public final int DR = 3;
    static public final int DG = 4;
    static public final int DB = 5;
    static public final int DA = 6;

    // specular (by default kept white)
    static public final int SPR = 28;
    static public final int SPG = 29;
    static public final int SPB = 30;

    static public final int SHINE = 31;

    // emissive (by default kept black)
    static public final int ER = 32;
    static public final int EG = 33;
    static public final int EB = 34;

    // has this vertex been lit yet
    static public final int BEEN_LIT = 35;

    // has this vertex been assigned a normal yet
    static public final int HAS_NORMAL = 36;

    static public final int VERTEX_FIELD_COUNT = 37;

    // End Variables

    public PGraphics(GraphicsContext gc, FXApp parent) {
        context = gc;
        this.parent = parent;
        defaultSettings();
    }

    public void setParent(FXApp parent) { // ignore
        this.parent = parent;
        smooth = parent.sketchSmooth();
        pixelDensity = parent.sketchPixelDensity();
    }

    public void setPrimary(boolean primary) { // ignore
        this.primaryGraphics = primary;

        if (primaryGraphics) {
            format = RGB;
        }

        surface = new PSurface(this);
    }

    public void setContext(GraphicsContext context) { // ignore
        this.context = context;
    }

    public void setPath(String path) { // ignore
        this.path = path;
    }

    public void setSize(int w, int h) { // ignore
        width = w;
        height = h;

        pixelWidth = width * pixelDensity;
        pixelHeight = height * pixelDensity;

        reapplySettings = true;
    }

    public void dispose() { // ignore
        if (primaryGraphics && asyncImageSaver != null) {
            asyncImageSaver.dispose();
            asyncImageSaver = null;
        }
    }

    public PSurface createSurface() {
        return surface = new PSurface(this);
    }

    public Object getNative() {
        return context;
    }

    public void setCache(PImage image, Object storage) { // ignore
        cacheMap.put(image, storage);
    }

    public Object getCache(PImage image) { // ignore
        return cacheMap.get(image);
    }

    public void removeCache(PImage image) { // ignore
        cacheMap.remove(image);
    }

    public void beginDraw() {
        checkSettings();
        resetMatrix(); // reset model matrix
        vertexCount = 0;
    }

    public void endDraw() {
        flush();

        if (!primaryGraphics) {
            loadPixels(); // Hereeeee
        }
    }

    public PGL beginPGL() {
        showMethodWarning("beginGL");
        return null;
    }

    public void endPGL() {
        showMethodWarning("endGL");
    }

    public void flush() {
        flushPixels();
    }

    protected void flushPixels() {
        boolean hasPixels = modified && pixels != null;
        if (hasPixels) {
            int mx1 = getModifiedX1();
            int mx2 = getModifiedX2();
            int my1 = getModifiedY1();
            int my2 = getModifiedY2();
            int mw = mx2 - mx1;
            int mh = my2 - my1;

            if (pixelDensity == 1) {
                PixelWriter pw = context.getPixelWriter();
                pw.setPixels(mx1, my1, mw, mh, argbFormat, pixels, mx1 + my1 * pixelWidth, pixelWidth);
            } else {
                // The only way to push all the pixels is to draw a scaled-down image
                if (snapshotImage == null || snapshotImage.getWidth() != pixelWidth
                        || snapshotImage.getHeight() != pixelHeight) {
                    snapshotImage = new WritableImage(pixelWidth, pixelHeight);
                }

                PixelWriter pw = snapshotImage.getPixelWriter();
                pw.setPixels(mx1, my1, mw, mh, argbFormat, pixels, mx1 + my1 * pixelWidth, pixelWidth);
                context.save();
                resetMatrix();
                context.scale(1d / pixelDensity, 1d / pixelDensity);
                context.drawImage(snapshotImage, mx1, my1, mw, mh, mx1, my1, mw, mh);
                context.restore();
            }
        }

        modified = false;
    }

    protected void beforeContextDraw() {
        flushPixels();
        loaded = false;
    }

    protected void checkSettings() {
        if (!settingsInited)
            defaultSettings();
        if (reapplySettings)
            reapplySettings();
    }

    protected void defaultSettings() { // ignore
        colorMode(RGB, 255);
        fill = true;
        stroke = true;
        fill(255);
        stroke(0);

        strokeWeight(DEFAULT_STROKE_WEIGHT);
        strokeJoin(DEFAULT_STROKE_JOIN);
        strokeCap(DEFAULT_STROKE_CAP);

        shape = 0;

        rectMode(CORNER);
        ellipseMode(DIAMETER);

        autoNormal = true;
        textFont = null;
        textSize = 12;
        textLeading = 14;
        textAlign = LEFT;
        textMode = MODEL;

        if (primaryGraphics) {
            background(backgroundColor);
        }

        blendMode(BLEND);
        settingsInited = true;
        reapplySettings = false;
    }

    protected void reapplySettings() {
        if (!settingsInited)
            return;

        colorMode(colorMode, colorModeX, colorModeY, colorModeZ);
        if (fill) {
            fill(fillColor);
        } else {
            noFill();
        }
        if (stroke) {
            stroke(strokeColor);
            strokeWeight(strokeWeight);
            strokeCap(strokeCap);
            strokeJoin(strokeJoin);
        } else {
            noStroke();
        }
        if (tint) {
            tint(tintColor);
        } else {
            noTint();
        }

        if (textFont != null) {
            double saveLeading = textLeading;
            textFont(textFont, textSize);
            textLeading(saveLeading);
        }
        textMode(textMode);
        textAlign(textAlign, textAlignY);
        background(backgroundColor);

        blendMode(blendMode);

        reapplySettings = false;
    }

    public void hint(int which) {
        if (which == ENABLE_NATIVE_FONTS || which == DISABLE_NATIVE_FONTS) {
            showWarning("hint(ENABLE_NATIVE_FONTS) no longer supported. " + "Use createFont() instead.");
        }
        if (which == ENABLE_KEY_REPEAT) {
            parent.keyRepeatEnabled = true;
        } else if (which == DISABLE_KEY_REPEAT) {
            parent.keyRepeatEnabled = false;
        }
        if (which > 0) {
            hints[which] = true;
        } else {
            hints[-which] = false;
        }
    }

    public void beginShape() {
        beginShape(POLYGON);
    }

    public void beginShape(int kind) {
        shape = kind;
        vertexCount = 0;
        curveVertexCount = 0;

        workPath.reset();
        auxPath.reset();

        flushPixels();

        if (drawingThinLines()) {
            adjustedForThinLines = true;
            translate(0.5f, 0.5f);
        }
    }

    public void edge(boolean edge) {
        this.edge = edge;
    }

    public void normal(double nx, double ny, double nz) {
        normalX = nx;
        normalY = ny;
        normalZ = nz;

        if (shape != 0) {
            if (normalMode == NORMAL_MODE_AUTO) {
                normalMode = NORMAL_MODE_SHAPE;
            } else if (normalMode == NORMAL_MODE_SHAPE) {
                normalMode = NORMAL_MODE_VERTEX;
            }
        }
    }

    public void attribPosition(String name, double x, double y, double z) {
        showMissingWarning("attrib");
    }

    public void attribNormal(String name, double nx, double ny, double nz) {
        showMissingWarning("attrib");
    }

    public void attribColor(String name, int color) {
        showMissingWarning("attrib");
    }

    public void attrib(String name, double... values) {
        showMissingWarning("attrib");
    }

    public void attrib(String name, int... values) {
        showMissingWarning("attrib");
    }

    public void attrib(String name, boolean... values) {
        showMissingWarning("attrib");
    }

    public void textureMode(int mode) {
        if (mode != IMAGE && mode != NORMAL) {
            throw new RuntimeException("textureMode() only supports IMAGE and NORMAL");
        }
        this.textureMode = mode;
    }

    public void textureWrap(int wrap) {
        showMissingWarning("textureWrap");
    }

    public void texture(PImage image) {
        showMethodWarning("texture");
    }

    public void noTexture() {
        textureImage = null;
    }

    protected void vertexCheck() {
        if (vertexCount == vertices.length) {
            double[][] temp = new double[vertexCount << 1][VERTEX_FIELD_COUNT];
            System.arraycopy(vertices, 0, temp, 0, vertexCount);
            vertices = temp;
        }
    }

    public void vertex(double x, double y) {
        if (vertexCount == vertices.length) {
            double[][] temp = new double[vertexCount << 1][VERTEX_FIELD_COUNT];
            System.arraycopy(vertices, 0, temp, 0, vertexCount);
            vertices = temp;
        }
        vertices[vertexCount][X] = x;
        vertices[vertexCount][Y] = y;
        vertexCount++;

        switch (shape) {

            case POINTS:
                point(x, y);
                break;

            case LINES:
                if ((vertexCount % 2) == 0) {
                    line(vertices[vertexCount - 2][X], vertices[vertexCount - 2][Y], x, y);
                }
                break;

            case TRIANGLES:
                if ((vertexCount % 3) == 0) {
                    triangle(vertices[vertexCount - 3][X], vertices[vertexCount - 3][Y], vertices[vertexCount - 2][X],
                            vertices[vertexCount - 2][Y], x, y);
                }
                break;

            case TRIANGLE_STRIP:
                if (vertexCount >= 3) {
                    triangle(vertices[vertexCount - 2][X], vertices[vertexCount - 2][Y], vertices[vertexCount - 1][X],
                            vertices[vertexCount - 1][Y], vertices[vertexCount - 3][X], vertices[vertexCount - 3][Y]);
                }
                break;

            case TRIANGLE_FAN:
                if (vertexCount >= 3) {
                    triangle(vertices[0][X], vertices[0][Y], vertices[vertexCount - 2][X], vertices[vertexCount - 2][Y],
                            x, y);
                }
                break;

            case QUAD:
            case QUADS:
                if ((vertexCount % 4) == 0) {
                    quad(vertices[vertexCount - 4][X], vertices[vertexCount - 4][Y], vertices[vertexCount - 3][X],
                            vertices[vertexCount - 3][Y], vertices[vertexCount - 2][X], vertices[vertexCount - 2][Y], x,
                            y);
                }
                break;

            case QUAD_STRIP:
                // 0---2---4
                // | | |
                // 1---3---5
                if ((vertexCount >= 4) && ((vertexCount % 2) == 0)) {
                    quad(vertices[vertexCount - 4][X], vertices[vertexCount - 4][Y], vertices[vertexCount - 2][X],
                            vertices[vertexCount - 2][Y], x, y, vertices[vertexCount - 3][X],
                            vertices[vertexCount - 3][Y]);
                }
                break;

            case POLYGON:
                if (workPath.getNumCommands() == 0 || breakShape) {
                    workPath.moveTo((float)x, (float)y);
                    breakShape = false;
                } else {
                    workPath.lineTo((float)x, (float)y);
                }
                break;
        }
    }

    public void vertex(double x, double y, double z) {
        showDepthWarningXYZ("vertex");
    }

    public void vertex(double[] v) {
        vertex(v[X], v[Y]);
    }

    public void vertex(double x, double y, double u, double v) {
        showVariationWarning("vertex(x, y, u, v)");
    }

    public void vertex(double x, double y, double z, double u, double v) {
        showDepthWarningXYZ("vertex");
    }

    protected void vertexTexture(double u, double v) {
        if (textureImage == null) {
            throw new RuntimeException(
                    "You must first call texture() before " + "using u and v coordinates with vertex()");
        }
        if (textureMode == IMAGE) {
            u /= textureImage.width;
            v /= textureImage.height;
        }

        textureU = u;
        textureV = v;

        if (textureU < 0)
            textureU = 0;
        else if (textureU > 1)
            textureU = 1;

        if (textureV < 0)
            textureV = 0;
        else if (textureV > 1)
            textureV = 1;
    }

    public void beginContour() {
        if (openContour) {
            PGraphics.showWarning("Already called beginContour()");
            return;
        }

        Path2D contourPath = auxPath;
        auxPath = workPath;
        workPath = contourPath;

        if (contourPath.getNumCommands() > 0) {
            breakShape = true;
        }

        openContour = true;
    }

    public void endContour() {
        if (!openContour) {
            PGraphics.showWarning("Need to call beginContour() first");
            return;
        }

        if (workPath.getNumCommands() > 0)
            workPath.closePath();

        Path2D temp = workPath;
        workPath = auxPath;
        auxPath = temp;

        openContour = false;
    }

    public void endShape() {
        endShape(OPEN);
    }

    public void endShape(int mode) {
        if (openContour) {
            endContour();
            PGraphics.showWarning("Missing endContour() before endShape()");
        }
        if (workPath.getNumCommands() > 0) {
            if (shape == POLYGON) {
                if (mode == CLOSE) {
                    workPath.closePath();
                }
                if (auxPath.getNumCommands() > 0) {
                    workPath.append(auxPath, false);
                }
                drawShape(workPath);
            }
        }
        shape = 0;
        if (adjustedForThinLines) {
            adjustedForThinLines = false;
            translate(-0.5f, -0.5f);
        }
        loaded = false;
    }

    private float[] fpathCoordsBuffer = new float[pathCoordsBuffer.length];

    private void copyCoordsBuffer() {
        for (int i = 0; i < pathCoordsBuffer.length; i++) {
            fpathCoordsBuffer[i] = (float) pathCoordsBuffer[i];
        }
    }

    private void drawShape(Shape s) {
        context.beginPath();
        PathIterator pi = s.getPathIterator(null);
        copyCoordsBuffer();
        while (!pi.isDone()) {

            int pitype = pi.currentSegment(fpathCoordsBuffer);
            switch (pitype) {
                case PathIterator.SEG_MOVETO:
                    context.moveTo(pathCoordsBuffer[0], pathCoordsBuffer[1]);
                    break;
                case PathIterator.SEG_LINETO:
                    context.lineTo(pathCoordsBuffer[0], pathCoordsBuffer[1]);
                    break;
                case PathIterator.SEG_QUADTO:
                    context.quadraticCurveTo(pathCoordsBuffer[0], pathCoordsBuffer[1], pathCoordsBuffer[2],
                            pathCoordsBuffer[3]);
                    break;
                case PathIterator.SEG_CUBICTO:
                    context.bezierCurveTo(pathCoordsBuffer[0], pathCoordsBuffer[1], pathCoordsBuffer[2],
                            pathCoordsBuffer[3], pathCoordsBuffer[4], pathCoordsBuffer[5]);
                    break;
                case PathIterator.SEG_CLOSE:
                    context.closePath();
                    break;
                default:
                    showWarning("Unknown segment type " + pitype);
            }
            pi.next();
        }
        if (fill)
            context.fill();
        if (stroke)
            context.stroke();
    }

    public PShape loadShape(String filename) {
        return loadShape(filename, null);
    }

    public PShape loadShape(String filename, String options) {
        String extension = FXApp.getExtension(filename);
        if (extension.equals("svg") || extension.equals("svgz")) {
            return new PShapeSVG(parent.loadXML(filename));
        }
        PGraphics.showWarning("Unsupported format: " + filename);
        return null;
    }

    public PShape createShape() {
        return createShape(PShape.GEOMETRY);
    }

    public PShape createShape(int type) {
        if (type == GROUP || type == PShape.PATH || type == PShape.GEOMETRY) {
            return createShapeFamily(type);
        }
        final String msg = "Only GROUP, PShape.PATH, and PShape.GEOMETRY work with createShape()";
        throw new IllegalArgumentException(msg);
    }

    protected PShape createShapeFamily(int type) {
        return new PShape(this, type);
        // showMethodWarning("createShape()");
        // return null;
    }

    public PShape createShape(int kind, double... p) {
        int len = p.length;

        if (kind == POINT) {
            if (is3D() && len != 2 && len != 3) {
                throw new IllegalArgumentException("Use createShape(POINT, x, y) or createShape(POINT, x, y, z)");
            } else if (is2D() && len != 2) {
                throw new IllegalArgumentException("Use createShape(POINT, x, y)");
            }
            return createShapePrimitive(kind, p);

        } else if (kind == LINE) {
            if (is3D() && len != 4 && len != 6) {
                throw new IllegalArgumentException(
                        "Use createShape(LINE, x1, y1, x2, y2) or createShape(LINE, x1, y1, z1, x2, y2, z1)");
            } else if (is2D() && len != 4) {
                throw new IllegalArgumentException("Use createShape(LINE, x1, y1, x2, y2)");
            }
            return createShapePrimitive(kind, p);

        } else if (kind == TRIANGLE) {
            if (len != 6) {
                throw new IllegalArgumentException("Use createShape(TRIANGLE, x1, y1, x2, y2, x3, y3)");
            }
            return createShapePrimitive(kind, p);

        } else if (kind == QUAD) {
            if (len != 8) {
                throw new IllegalArgumentException("Use createShape(QUAD, x1, y1, x2, y2, x3, y3, x4, y4)");
            }
            return createShapePrimitive(kind, p);

        } else if (kind == RECT) {
            if (len != 4 && len != 5 && len != 8) {
                throw new IllegalArgumentException(
                        "Wrong number of parameters for createShape(RECT), see the reference");
            }
            return createShapePrimitive(kind, p);

        } else if (kind == ELLIPSE) {
            if (len != 4) {
                throw new IllegalArgumentException("Use createShape(ELLIPSE, x, y, w, h)");
            }
            return createShapePrimitive(kind, p);

        } else if (kind == ARC) {
            if (len != 6 && len != 7) {
                throw new IllegalArgumentException(
                        "Use createShape(ARC, x, y, w, h, start, stop) or createShape(ARC, x, y, w, h, start, stop, arcMode)");
            }
            return createShapePrimitive(kind, p);

        } else if (kind == BOX) {
            if (!is3D()) {
                throw new IllegalArgumentException("createShape(BOX) is not supported in 2D");
            } else if (len != 1 && len != 3) {
                throw new IllegalArgumentException(
                        "Use createShape(BOX, size) or createShape(BOX, width, height, depth)");
            }
            return createShapePrimitive(kind, p);

        } else if (kind == SPHERE) {
            if (!is3D()) {
                throw new IllegalArgumentException("createShape(SPHERE) is not supported in 2D");
            } else if (len != 1) {
                throw new IllegalArgumentException("Use createShape(SPHERE, radius)");
            }
            return createShapePrimitive(kind, p);
        }
        throw new IllegalArgumentException("Unknown shape type passed to createShape()");
    }

    protected PShape createShapePrimitive(int kind, double... p) {
        // showMethodWarning("createShape()");
        // return null;
        return new PShape(this, kind, p);
    }

    public PShader loadShader(String fragFilename) {
        showMissingWarning("loadShader");
        return null;
    }

    public PShader loadShader(String fragFilename, String vertFilename) {
        showMissingWarning("loadShader");
        return null;
    }

    public void shader(PShader shader) {
        showMissingWarning("shader");
    }

    public void shader(PShader shader, int kind) {
        showMissingWarning("shader");
    }

    public void resetShader() {
        showMissingWarning("resetShader");
    }

    public void resetShader(int kind) {
        showMissingWarning("resetShader");
    }

    public void filter(PShader shader) {
        showMissingWarning("filter");
    }

    public void clip(double a, double b, double c, double d) {
        if (imageMode == CORNER) {
            if (c < 0) { // reset a negative width
                a += c;
                c = -c;
            }
            if (d < 0) { // reset a negative height
                b += d;
                d = -d;
            }

            clipImpl(a, b, a + c, b + d);

        } else if (imageMode == CORNERS) {
            if (c < a) { // reverse because x2 < x1
                double temp = a;
                a = c;
                c = temp;
            }
            if (d < b) { // reverse because y2 < y1
                double temp = b;
                b = d;
                d = temp;
            }

            clipImpl(a, b, c, d);

        } else if (imageMode == CENTER) {
            // c and d are width/height
            if (c < 0)
                c = -c;
            if (d < 0)
                d = -d;
            double x1 = a - c / 2;
            double y1 = b - d / 2;

            clipImpl(x1, y1, x1 + c, y1 + d);
        }
    }

    protected void clipImpl(double x1, double y1, double x2, double y2) {
        showTodoWarning("clip()", 3274);
    }

    public void noClip() {
        showTodoWarning("noClip()", 3274);
    }

    public void blendMode(int mode) {
        this.blendMode = mode;
        blendModeImpl();
    }

    protected void blendModeImpl() {
        BlendMode mode = BlendMode.SRC_OVER;
        switch (blendMode) {
            case REPLACE:
                showWarning("blendMode(REPLACE) is not supported");
                break;
            case BLEND:
                break; // this is SRC_OVER, the default
            case ADD:
                mode = BlendMode.ADD;
                break; // everyone's favorite
            case SUBTRACT:
                showWarning("blendMode(SUBTRACT) is not supported");
                break;
            case LIGHTEST:
                mode = BlendMode.LIGHTEN;
                break;
            case DARKEST:
                mode = BlendMode.DARKEN;
                break;
            case DIFFERENCE:
                mode = BlendMode.DIFFERENCE;
                break;
            case EXCLUSION:
                mode = BlendMode.EXCLUSION;
                break;
            case MULTIPLY:
                mode = BlendMode.MULTIPLY;
                break;
            case SCREEN:
                mode = BlendMode.SCREEN;
                break;
            case OVERLAY:
                mode = BlendMode.OVERLAY;
                break;
            case HARD_LIGHT:
                mode = BlendMode.HARD_LIGHT;
                break;
            case SOFT_LIGHT:
                mode = BlendMode.SOFT_LIGHT;
                break;
            case DODGE:
                mode = BlendMode.COLOR_DODGE;
                break;
            case BURN:
                mode = BlendMode.COLOR_BURN;
                break;
        }
        context.setGlobalBlendMode(mode);
    }

    protected void bezierVertexCheck() {
        if (shape == 0 || shape != POLYGON) {
            throw new RuntimeException(
                    "beginShape() or beginShape(POLYGON) " + "must be used before bezierVertex() or quadraticVertex()");
        }
        if (workPath.getNumCommands() == 0) {
            throw new RuntimeException(
                    "vertex() must be used at least once " + "before bezierVertex() or quadraticVertex()");
        }
    }

    protected void bezierVertexCheck(int shape, int vertexCount) {
        if (shape == 0 || shape != POLYGON) {
            throw new RuntimeException("beginShape() or beginShape(POLYGON) " + "must be used before bezierVertex() or quadraticVertex()");
        }
        if (vertexCount == 0) {
            throw new RuntimeException("vertex() must be used at least once " + "before bezierVertex() or quadraticVertex()");
        }
    }

    public void bezierVertex(double x1, double y1, double x2, double y2, double x3, double y3) {
        bezierVertexCheck();
        workPath.curveTo((float) x1, (float) y1, (float) x2, (float) y2, (float) x3, (float) y3);
    }

    public void bezierVertex(double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
        showDepthWarningXYZ("bezierVertex");
    }

    public void quadraticVertex(double ctrlX, double ctrlY, double endX, double endY) {
        bezierVertexCheck();
        workPath.quadTo((float) ctrlX, (float) ctrlY, (float) endX, (float) endY);
    }

    public void quadraticVertex(double x2, double y2, double z2, double x4, double y4, double z4) {
        showDepthWarningXYZ("quadVertex");
    }

    protected void curveVertexCheck() {
        curveVertexCheck(shape);
    }

    protected void curveVertexCheck(int shape) {
        if (shape != POLYGON) {
            throw new RuntimeException("You must use beginShape() or " + "beginShape(POLYGON) before curveVertex()");
        }
        // to improve code init time, allocate on first use.
        if (curveVertices == null) {
            curveVertices = new double[128][3];
        }

        if (curveVertexCount == curveVertices.length) {
            // Can't use FXApp.expand() cuz it doesn't do the copy properly
            double[][] temp = new double[curveVertexCount << 1][3];
            System.arraycopy(curveVertices, 0, temp, 0, curveVertexCount);
            curveVertices = temp;
        }
        curveInitCheck();
    }

    public void curveVertex(double x, double y) {
        curveVertexCheck();
        double[] v = curveVertices[curveVertexCount];
        v[X] = x;
        v[Y] = y;
        curveVertexCount++;

        // draw a segment if there are enough points
        if (curveVertexCount > 3) {
            curveVertexSegment(curveVertices[curveVertexCount - 4][X], curveVertices[curveVertexCount - 4][Y],
                    curveVertices[curveVertexCount - 3][X], curveVertices[curveVertexCount - 3][Y],
                    curveVertices[curveVertexCount - 2][X], curveVertices[curveVertexCount - 2][Y],
                    curveVertices[curveVertexCount - 1][X], curveVertices[curveVertexCount - 1][Y]);
        }
    }

    public void curveVertex(double x, double y, double z) {
        showDepthWarningXYZ("curveVertex");
    }

    protected void curveVertexSegment(double x1, double y1, double x2, double y2, double x3, double y3, double x4,
            double y4) {
        if (curveCoordX == null) {
            curveCoordX = new double[4];
            curveCoordY = new double[4];
            curveDrawX = new double[4];
            curveDrawY = new double[4];
        }

        curveCoordX[0] = x1;
        curveCoordY[0] = y1;

        curveCoordX[1] = x2;
        curveCoordY[1] = y2;

        curveCoordX[2] = x3;
        curveCoordY[2] = y3;

        curveCoordX[3] = x4;
        curveCoordY[3] = y4;

        curveToBezierMatrix.mult(curveCoordX, curveDrawX);
        curveToBezierMatrix.mult(curveCoordY, curveDrawY);

        if (workPath.getNumCommands() == 0) {
            workPath.moveTo((float) curveDrawX[0], (float) curveDrawY[0]);
            breakShape = false;
        }

        workPath.curveTo((float) curveDrawX[1], (float) curveDrawY[1], (float) curveDrawX[2], (float) curveDrawY[2], (float) curveDrawX[3], (float) curveDrawY[3]);
    }

    protected void curveVertexSegment(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
        double x0 = x2;
        double y0 = y2;
        double z0 = z2;

        PMatrix3D draw = curveDrawMatrix;

        double xplot1 = draw.m10 * x1 + draw.m11 * x2 + draw.m12 * x3 + draw.m13 * x4;
        double xplot2 = draw.m20 * x1 + draw.m21 * x2 + draw.m22 * x3 + draw.m23 * x4;
        double xplot3 = draw.m30 * x1 + draw.m31 * x2 + draw.m32 * x3 + draw.m33 * x4;

        double yplot1 = draw.m10 * y1 + draw.m11 * y2 + draw.m12 * y3 + draw.m13 * y4;
        double yplot2 = draw.m20 * y1 + draw.m21 * y2 + draw.m22 * y3 + draw.m23 * y4;
        double yplot3 = draw.m30 * y1 + draw.m31 * y2 + draw.m32 * y3 + draw.m33 * y4;

        // vertex() will reset splineVertexCount, so save it
        int savedCount = curveVertexCount;

        double zplot1 = draw.m10 * z1 + draw.m11 * z2 + draw.m12 * z3 + draw.m13 * z4;
        double zplot2 = draw.m20 * z1 + draw.m21 * z2 + draw.m22 * z3 + draw.m23 * z4;
        double zplot3 = draw.m30 * z1 + draw.m31 * z2 + draw.m32 * z3 + draw.m33 * z4;

        vertex(x0, y0, z0);
        for (int j = 0; j < curveDetail; j++) {
            x0 += xplot1;
            xplot1 += xplot2;
            xplot2 += xplot3;
            y0 += yplot1;
            yplot1 += yplot2;
            yplot2 += yplot3;
            z0 += zplot1;
            zplot1 += zplot2;
            zplot2 += zplot3;
            vertex(x0, y0, z0);
        }
        curveVertexCount = savedCount;
    }

    public void point(double x, double y) {
        if (stroke) {
            line(x, y, x + EPSILON, y + EPSILON);
        }
    }

    public void point(double x, double y, double z) {
        beginShape(POINTS);
        vertex(x, y, z);
        endShape();
    }

    public void line(double x1, double y1, double x2, double y2) {
        beforeContextDraw();
        if (drawingThinLines()) {
            x1 += 0.5f;
            x2 += 0.5f;
            y1 += 0.5f;
            y2 += 0.5f;
        }
        context.strokeLine(x1, y1, x2, y2);
    }

    public void line(double x1, double y1, double z1, double x2, double y2, double z2) {
        beginShape(LINES);
        vertex(x1, y1, z1);
        vertex(x2, y2, z2);
        endShape();
    }

    public void triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        beforeContextDraw();
        if (drawingThinLines()) {
            x1 += 0.5f;
            x2 += 0.5f;
            x3 += 0.5f;
            y1 += 0.5f;
            y2 += 0.5f;
            y3 += 0.5f;
        }
        context.beginPath();
        context.moveTo(x1, y1);
        context.lineTo(x2, y2);
        context.lineTo(x3, y3);
        context.closePath();
        if (fill)
            context.fill();
        if (stroke)
            context.stroke();
    }

    public void quad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        beforeContextDraw();
        if (drawingThinLines()) {
            x1 += 0.5f;
            x2 += 0.5f;
            x3 += 0.5f;
            x4 += 0.5f;
            y1 += 0.5f;
            y2 += 0.5f;
            y3 += 0.5f;
            y4 += 0.5f;
        }
        context.beginPath();
        context.moveTo(x1, y1);
        context.lineTo(x2, y2);
        context.lineTo(x3, y3);
        context.lineTo(x4, y4);
        context.closePath();
        if (fill)
            context.fill();
        if (stroke)
            context.stroke();
    }

    public void rectMode(int mode) {
        rectMode = mode;
    }

    public void rect(double a, double b, double c, double d) {
        double hradius, vradius;
        switch (rectMode) {
            case CORNERS:
                break;
            case CORNER:
                c += a;
                d += b;
                break;
            case RADIUS:
                hradius = c;
                vradius = d;
                c = a + hradius;
                d = b + vradius;
                a -= hradius;
                b -= vradius;
                break;
            case CENTER:
                hradius = c / 2.0f;
                vradius = d / 2.0f;
                c = a + hradius;
                d = b + vradius;
                a -= hradius;
                b -= vradius;
        }

        if (a > c) {
            double temp = a;
            a = c;
            c = temp;
        }

        if (b > d) {
            double temp = b;
            b = d;
            d = temp;
        }

        rectImpl(a, b, c, d);
    }

    protected void rectImpl(double x1, double y1, double x2, double y2) {
        System.out.println(x1 + " " + y1 + " " + (x2 - x1) + " " + (y2 - y1) + " ");
        /*
         * beforeContextDraw(); if (drawingThinLines()) { x1 += 0.5f; x2 += 0.5f; y1 +=
         * 0.5f; y2 += 0.5f; }
         */
        System.out.println(fill);
        if (fill)
            context.fillRect(x1, y1, x2 - x1, y2 - y1);
        if (stroke)
            context.strokeRect(x1, y1, x2 - x1, y2 - y1);
    }

    public void rect(double a, double b, double c, double d, double r) {
        rect(a, b, c, d, r, r, r, r);
    }

    public void rect(double a, double b, double c, double d, double tl, double tr, double br, double bl) {
        double hradius, vradius;
        switch (rectMode) {
            case CORNERS:
                break;
            case CORNER:
                c += a;
                d += b;
                break;
            case RADIUS:
                hradius = c;
                vradius = d;
                c = a + hradius;
                d = b + vradius;
                a -= hradius;
                b -= vradius;
                break;
            case CENTER:
                hradius = c / 2.0f;
                vradius = d / 2.0f;
                c = a + hradius;
                d = b + vradius;
                a -= hradius;
                b -= vradius;
        }

        if (a > c) {
            double temp = a;
            a = c;
            c = temp;
        }

        if (b > d) {
            double temp = b;
            b = d;
            d = temp;
        }

        double maxRounding = (double) min((c - a) / 2, (d - b) / 2);
        if (tl > maxRounding)
            tl = maxRounding;
        if (tr > maxRounding)
            tr = maxRounding;
        if (br > maxRounding)
            br = maxRounding;
        if (bl > maxRounding)
            bl = maxRounding;

        rectImpl(a, b, c, d, tl, tr, br, bl);
    }

    protected void rectImpl(double x1, double y1, double x2, double y2, double tl, double tr, double br, double bl) {
        beginShape();
        // vertex(x1+tl, y1);
        if (tr != 0) {
            vertex(x2 - tr, y1);
            quadraticVertex(x2, y1, x2, y1 + tr);
        } else {
            vertex(x2, y1);
        }
        if (br != 0) {
            vertex(x2, y2 - br);
            quadraticVertex(x2, y2, x2 - br, y2);
        } else {
            vertex(x2, y2);
        }
        if (bl != 0) {
            vertex(x1 + bl, y2);
            quadraticVertex(x1, y2, x1, y2 - bl);
        } else {
            vertex(x1, y2);
        }
        if (tl != 0) {
            vertex(x1, y1 + tl);
            quadraticVertex(x1, y1, x1 + tl, y1);
        } else {
            vertex(x1, y1);
        }
        endShape(CLOSE);
    }

    public void square(double x, double y, double extent) {
        rect(x, y, extent, extent);
    }

    public void ellipseMode(int mode) {
        ellipseMode = mode;
    }

    public void ellipse(double a, double b, double c, double d) {
        double x = a;
        double y = b;
        double w = c;
        double h = d;

        if (ellipseMode == CORNERS) {
            w = c - a;
            h = d - b;

        } else if (ellipseMode == RADIUS) {
            x = a - c;
            y = b - d;
            w = c * 2;
            h = d * 2;

        } else if (ellipseMode == DIAMETER) {
            x = a - c / 2f;
            y = b - d / 2f;
        }

        if (w < 0) { // undo negative width
            x += w;
            w = -w;
        }

        if (h < 0) { // undo negative height
            y += h;
            h = -h;
        }

        ellipseImpl(x, y, w, h);
    }

    protected void ellipseImpl(double x, double y, double w, double h) {
        beforeContextDraw();
        if (drawingThinLines()) {
            x += 0.5f;
            y += 0.5f;
        }
        if (fill)
            context.fillOval(x, y, w, h);
        if (stroke)
            context.strokeOval(x, y, w, h);
    }

    public void arc(double a, double b, double c, double d, double start, double stop) {
        arc(a, b, c, d, start, stop, 0);
    }

    public void arc(double a, double b, double c, double d,
            double start, double stop, int mode) {
        double x = a;
        double y = b;
        double w = c;
        double h = d;

        if (ellipseMode == CORNERS) {
            w = c - a;
            h = d - b;

        } else if (ellipseMode == RADIUS) {
            x = a - c;
            y = b - d;
            w = c * 2;
            h = d * 2;

        } else if (ellipseMode == CENTER) {
            x = a - c/2f;
            y = b - d/2f;
        }

        // make sure the loop will exit before starting while
        if (!Double.isInfinite(start) && !Double.isInfinite(stop)) {
            // ignore equal and degenerate cases
            if (stop > start) {
                // make sure that we're starting at a useful point
                while (start < 0) {
                    start += TWO_PI;
                    stop += TWO_PI;
                }

                if (stop - start > TWO_PI) {
                    // don't change start, it is visible in PIE mode
                    stop = start + TWO_PI;
                }
                arcImpl(x, y, w, h, start, stop, mode);
            }
        }
    }

    protected void arcImpl(double x, double y, double w, double h, double start, double stop, int mode) {
        beforeContextDraw();

        if (drawingThinLines()) {
            x += 0.5f;
            y += 0.5f;
        }

        start = -start;
        stop = -stop;

        double sweep = stop - start;
        ArcType fillMode = ArcType.ROUND; // Arc2D.PIE
        ArcType strokeMode = ArcType.OPEN;

        if (mode == OPEN) {
            fillMode = ArcType.OPEN;

        } else if (mode == PIE) {
            strokeMode = ArcType.ROUND; // PIE

        } else if (mode == CHORD) {
            fillMode = ArcType.CHORD;
            strokeMode = ArcType.CHORD;
        }

        if (fill) {
            context.fillArc(x, y, w, h, degrees(start), degrees(sweep), fillMode);
        }
        if (stroke) {
            context.strokeArc(x, y, w, h, degrees(start), degrees(sweep), strokeMode);
        }
    }

    public void circle(double x, double y, double extent) {
        ellipse(x, y, extent, extent);
    }

    public void box(double w, double h, double d) {
        showMethodWarning("box");
    }

    public void sphereDetail(int res) {
        sphereDetail(res, res);
    }

    public void sphereDetail(int ures, int vres) {
        if (ures < 3)
            ures = 3; // force a minimum res
        if (vres < 2)
            vres = 2; // force a minimum res
        if ((ures == sphereDetailU) && (vres == sphereDetailV))
            return;

        double delta = (double) SINCOS_LENGTH / ures;
        double[] cx = new double[ures];
        double[] cz = new double[ures];
        // calc unit circle in XZ plane
        for (int i = 0; i < ures; i++) {
            cx[i] = cosLUT[(int) (i * delta) % SINCOS_LENGTH];
            cz[i] = sinLUT[(int) (i * delta) % SINCOS_LENGTH];
        }
        // computing vertexlist
        // vertexlist starts at south pole
        int vertCount = ures * (vres - 1) + 2;
        int currVert = 0;

        // re-init arrays to store vertices
        sphereX = new double[vertCount];
        sphereY = new double[vertCount];
        sphereZ = new double[vertCount];

        double angle_step = (SINCOS_LENGTH * 0.5f) / vres;
        double angle = angle_step;

        // step along Y axis
        for (int i = 1; i < vres; i++) {
            double curradius = sinLUT[(int) angle % SINCOS_LENGTH];
            double currY = cosLUT[(int) angle % SINCOS_LENGTH];
            for (int j = 0; j < ures; j++) {
                sphereX[currVert] = cx[j] * curradius;
                sphereY[currVert] = currY;
                sphereZ[currVert++] = cz[j] * curradius;
            }
            angle += angle_step;
        }
        sphereDetailU = ures;
        sphereDetailV = vres;
    }

    public void sphere(double r) {
        showMethodWarning("sphere");
    }

    public double bezierPoint(double a, double b, double c, double d, double t) {
        double t1 = 1.0f - t;
        return (a * t1 + 3 * b * t) * t1 * t1 + (3 * c * t1 + d * t) * t * t;
    }

    public double bezierTangent(double a, double b, double c, double d, double t) {
        return (3 * t * t * (-a + 3 * b - 3 * c + d) + 6 * t * (a - 2 * b + c) + 3 * (-a + b));
    }

    protected void bezierInitCheck() {
        if (!bezierInited) {
            bezierInit();
        }
    }

    protected void bezierInit() {
        // overkill to be broken out, but better parity with the curve stuff below
        bezierDetail(bezierDetail);
        bezierInited = true;
    }

    public void bezierDetail(int detail) {
    }

    public void bezier(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        beginShape();
        vertex(x1, y1);
        bezierVertex(x2, y2, x3, y3, x4, y4);
        endShape();
    }

    public void bezier(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
            double z3, double x4, double y4, double z4) {
        beginShape();
        vertex(x1, y1, z1);
        bezierVertex(x2, y2, z2, x3, y3, z3, x4, y4, z4);
        endShape();
    }

    public double curvePoint(double a, double b, double c, double d, double t) {
        curveInitCheck();

        double tt = t * t;
        double ttt = t * tt;
        PMatrix3D cb = curveBasisMatrix;

        // not optimized (and probably need not be)
        return (a * (ttt * cb.m00 + tt * cb.m10 + t * cb.m20 + cb.m30)
                + b * (ttt * cb.m01 + tt * cb.m11 + t * cb.m21 + cb.m31)
                + c * (ttt * cb.m02 + tt * cb.m12 + t * cb.m22 + cb.m32)
                + d * (ttt * cb.m03 + tt * cb.m13 + t * cb.m23 + cb.m33));
    }

    public double curveTangent(double a, double b, double c, double d, double t) {
        curveInitCheck();

        double tt3 = t * t * 3;
        double t2 = t * 2;
        PMatrix3D cb = curveBasisMatrix;

        // not optimized (and probably need not be)
        return (a * (tt3 * cb.m00 + t2 * cb.m10 + cb.m20) + b * (tt3 * cb.m01 + t2 * cb.m11 + cb.m21)
                + c * (tt3 * cb.m02 + t2 * cb.m12 + cb.m22) + d * (tt3 * cb.m03 + t2 * cb.m13 + cb.m23));
    }

    public void curveDetail(int detail) {
    }

    public void curveTightness(double tightness) {
        curveTightness = tightness;
        curveInit();
    }

    protected void curveInitCheck() {
        if (!curveInited) {
            curveInit();
        }
    }

    protected void curveInit() {
        if (curveDrawMatrix == null) {
            curveBasisMatrix = new PMatrix3D();
            curveDrawMatrix = new PMatrix3D();
            curveInited = true;
        }

        double s = curveTightness;
        curveBasisMatrix.set((s - 1) / 2f, (s + 3) / 2f, (-3 - s) / 2f, (1 - s) / 2f, (1 - s), (-5 - s) / 2f, (s + 2),
                (s - 1) / 2f, (s - 1) / 2f, 0, (1 - s) / 2f, 0, 0, 1, 0, 0);

        splineForward(curveDetail, curveDrawMatrix);

        if (bezierBasisInverse == null) {
            bezierBasisInverse = bezierBasisMatrix.get();
            bezierBasisInverse.invert();
            curveToBezierMatrix = new PMatrix3D();
        }

        curveToBezierMatrix.set(curveBasisMatrix);
        curveToBezierMatrix.preApply(bezierBasisInverse);
        curveDrawMatrix.apply(curveBasisMatrix);
    }

    public void curve(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        beginShape();
        curveVertex(x1, y1);
        curveVertex(x2, y2);
        curveVertex(x3, y3);
        curveVertex(x4, y4);
        endShape();
    }

    public void curve(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3,
            double x4, double y4, double z4) {
        beginShape();
        curveVertex(x1, y1, z1);
        curveVertex(x2, y2, z2);
        curveVertex(x3, y3, z3);
        curveVertex(x4, y4, z4);
        endShape();
    }

    protected void splineForward(int segments, PMatrix3D matrix) {
        double f = 1.0f / segments;
        double ff = f * f;
        double fff = ff * f;

        matrix.set(0, 0, 0, 1, fff, ff, f, 0, 6 * fff, 2 * ff, 0, 0, 6 * fff, 0, 0, 0);
    }

    //////////////////////////////////////////////////////////////

    // SMOOTHING

    public void smooth() { // ignore
        smooth(1);
    }

    public void smooth(int quality) { // ignore
        if (primaryGraphics) {
            parent.smooth(quality);
        } else {
            // for createGraphics(), make sure beginDraw() not called yet
            if (settingsInited) {
                // ignore if it's just a repeat of the current state
                if (this.smooth != quality) {
                    smoothWarning("smooth");
                }
            } else {
                this.smooth = quality;
            }
        }
    }

    public void noSmooth() { // ignore
        smooth(0);
    }

    private void smoothWarning(String method) {
        PGraphics.showWarning("%s() can only be used before beginDraw()", method);
    }

    public void imageMode(int mode) {
        if ((mode == CORNER) || (mode == CORNERS) || (mode == CENTER)) {
            imageMode = mode;
        } else {
            String msg = "imageMode() only works with CORNER, CORNERS, or CENTER";
            throw new RuntimeException(msg);
        }
    }

    public void image(PImage img, double a, double b) {
        if (img.width == -1 || img.height == -1)
            return;

        if (imageMode == CORNER || imageMode == CORNERS) {
            imageImpl(img, a, b, a + img.width, b + img.height, 0, 0, img.width, img.height);

        } else if (imageMode == CENTER) {
            double x1 = a - img.width / 2;
            double y1 = b - img.height / 2;
            imageImpl(img, x1, y1, x1 + img.width, y1 + img.height, 0, 0, img.width, img.height);
        }
    }

    public void image(PImage img, double a, double b, double c, double d) {
        image(img, a, b, c, d, 0, 0, img.width, img.height);
    }

    public void image(PImage img, double a, double b, double c, double d, int u1, int v1, int u2, int v2) {
        // Starting in release 0144, image errors are simply ignored.
        // loadImageAsync() sets width and height to -1 when loading fails.
        if (img.width == -1 || img.height == -1)
            return;

        if (imageMode == CORNER) {
            if (c < 0) { // reset a negative width
                a += c;
                c = -c;
            }
            if (d < 0) { // reset a negative height
                b += d;
                d = -d;
            }

            imageImpl(img, a, b, a + c, b + d, u1, v1, u2, v2);

        } else if (imageMode == CORNERS) {
            if (c < a) { // reverse because x2 < x1
                double temp = a;
                a = c;
                c = temp;
            }
            if (d < b) { // reverse because y2 < y1
                double temp = b;
                b = d;
                d = temp;
            }

            imageImpl(img, a, b, c, d, u1, v1, u2, v2);

        } else if (imageMode == CENTER) {
            // c and d are width/height
            if (c < 0)
                c = -c;
            if (d < 0)
                d = -d;
            double x1 = a - c / 2;
            double y1 = b - d / 2;

            imageImpl(img, x1, y1, x1 + c, y1 + d, u1, v1, u2, v2);
        }
    }

    protected void imageImpl(PImage who, double x1, double y1, double x2, double y2, int u1, int v1, int u2, int v2) {
        if (who.width <= 0 || who.height <= 0)
            return;

        ImageCache cash = (ImageCache) getCache(who);

        if (cash != null) {
            if (who.pixelWidth != cash.image.getWidth() || who.pixelHeight != cash.image.getHeight()) {
                cash = null;
            }
        }

        if (cash == null) {
            cash = new ImageCache(); // who);
            setCache(who, cash);
            who.updatePixels();
            who.setModified();
        }

        if ((tint && !cash.tinted) || (tint && (cash.tintedColor != tintColor)) || (!tint && cash.tinted)) {
            who.updatePixels();
        }

        if (who.isModified()) {
            if (who.pixels == null) {
                who.pixels = new int[who.pixelWidth * who.pixelHeight];
            }
            cash.update(who, tint, tintColor);
            who.setModified(false);
        }

        u1 *= who.pixelDensity;
        v1 *= who.pixelDensity;
        u2 *= who.pixelDensity;
        v2 *= who.pixelDensity;

        context.drawImage(((ImageCache) getCache(who)).image, u1, v1, u2 - u1, v2 - v1, x1, y1, x2 - x1, y2 - y1);
    }

    static class ImageCache {
        boolean tinted;
        int tintedColor;
        int[] tintedTemp;
        WritableImage image;

        public void update(PImage source, boolean tint, int tintColor) {
            int targetType = ARGB;
            boolean opaque = (tintColor & 0xFF000000) == 0xFF000000;
            if (source.format == RGB) {
                if (!tint || (tint && opaque)) {
                    targetType = RGB;
                }
            }

            if (image == null) {
                image = new WritableImage(source.pixelWidth, source.pixelHeight);
            }

            PixelWriter pw = image.getPixelWriter();
            if (tint) {
                if (tintedTemp == null || tintedTemp.length != source.pixelWidth) {
                    tintedTemp = new int[source.pixelWidth];
                }
                int a2 = (tintColor >> 24) & 0xff;
                int r2 = (tintColor >> 16) & 0xff;
                int g2 = (tintColor >> 8) & 0xff;
                int b2 = (tintColor) & 0xff;

                if (targetType == RGB) {

                    int index = 0;
                    for (int y = 0; y < source.pixelHeight; y++) {
                        for (int x = 0; x < source.pixelWidth; x++) {
                            int argb1 = source.pixels[index++];
                            int r1 = (argb1 >> 16) & 0xff;
                            int g1 = (argb1 >> 8) & 0xff;
                            int b1 = (argb1) & 0xff;

                            tintedTemp[x] = 0xFF000000 | (((r2 * r1) & 0xff00) << 8) | ((g2 * g1) & 0xff00)
                                    | (((b2 * b1) & 0xff00) >> 8);
                        }
                        pw.setPixels(0, y, source.pixelWidth, 1, argbFormat, tintedTemp, 0, source.pixelWidth);
                    }
                } else if (targetType == ARGB) {
                    if (source.format == RGB && (tintColor & 0xffffff) == 0xffffff) {
                        int hi = tintColor & 0xff000000;
                        int index = 0;
                        for (int y = 0; y < source.pixelHeight; y++) {
                            for (int x = 0; x < source.pixelWidth; x++) {
                                tintedTemp[x] = hi | (source.pixels[index++] & 0xFFFFFF);
                            }
                            pw.setPixels(0, y, source.pixelWidth, 1, argbFormat, tintedTemp, 0, source.pixelWidth);
                        }
                    } else {
                        int index = 0;
                        for (int y = 0; y < source.pixelHeight; y++) {
                            if (source.format == RGB) {
                                int alpha = tintColor & 0xFF000000;
                                for (int x = 0; x < source.pixelWidth; x++) {
                                    int argb1 = source.pixels[index++];
                                    int r1 = (argb1 >> 16) & 0xff;
                                    int g1 = (argb1 >> 8) & 0xff;
                                    int b1 = (argb1) & 0xff;
                                    tintedTemp[x] = alpha | (((r2 * r1) & 0xff00) << 8) | ((g2 * g1) & 0xff00)
                                            | (((b2 * b1) & 0xff00) >> 8);
                                }
                            } else if (source.format == ARGB) {
                                for (int x = 0; x < source.pixelWidth; x++) {
                                    int argb1 = source.pixels[index++];
                                    int a1 = (argb1 >> 24) & 0xff;
                                    int r1 = (argb1 >> 16) & 0xff;
                                    int g1 = (argb1 >> 8) & 0xff;
                                    int b1 = (argb1) & 0xff;
                                    tintedTemp[x] = (((a2 * a1) & 0xff00) << 16) | (((r2 * r1) & 0xff00) << 8)
                                            | ((g2 * g1) & 0xff00) | (((b2 * b1) & 0xff00) >> 8);
                                }
                            } else if (source.format == ALPHA) {
                                int lower = tintColor & 0xFFFFFF;
                                for (int x = 0; x < source.pixelWidth; x++) {
                                    int a1 = source.pixels[index++];
                                    tintedTemp[x] = (((a2 * a1) & 0xff00) << 16) | lower;
                                }
                            }
                            // wr.setDataElements(0, y, source.width, 1, tintedTemp);
                            pw.setPixels(0, y, source.pixelWidth, 1, argbFormat, tintedTemp, 0, source.pixelWidth);
                        }
                    }
                }
            } else { // !tint
                if (targetType == RGB && (source.pixels[0] >> 24 == 0)) {
                    source.filter(OPAQUE);
                }

                pw.setPixels(0, 0, source.pixelWidth, source.pixelHeight, argbFormat, source.pixels, 0,
                        source.pixelWidth);
            }
            this.tinted = tint;
            this.tintedColor = tintColor;
        }
    }

    public void shapeMode(int mode) {
        this.shapeMode = mode;
    }

    public void shape(PShape shape) {
        if (shape.isVisible()) { // don't do expensive matrix ops if invisible
            // Flushing any remaining geometry generated in the immediate mode
            // to avoid depth-sorting issues.
            flush();

            if (shapeMode == CENTER) {
                pushMatrix();
                translate(-shape.getWidth() / 2, -shape.getHeight() / 2);
            }

            shape.draw(this); // needs to handle recorder too

            if (shapeMode == CENTER) {
                popMatrix();
            }
        }
    }

    public void shape(PShape shape, double x, double y) {
        if (shape.isVisible()) { // don't do expensive matrix ops if invisible
            flush();

            pushMatrix();

            if (shapeMode == CENTER) {
                translate(x - shape.getWidth() / 2, y - shape.getHeight() / 2);

            } else if ((shapeMode == CORNER) || (shapeMode == CORNERS)) {
                translate(x, y);
            }
            shape.draw(this);

            popMatrix();
        }
    }

    protected void shape(PShape shape, double x, double y, double z) {
        showMissingWarning("shape");
    }

    public void shape(PShape shape, double a, double b, double c, double d) {
        if (shape.isVisible()) { // don't do expensive matrix ops if invisible
            flush();

            pushMatrix();

            if (shapeMode == CENTER) {
                // x and y are center, c and d refer to a diameter
                translate(a - c / 2f, b - d / 2f);
                scale(c / shape.getWidth(), d / shape.getHeight());

            } else if (shapeMode == CORNER) {
                translate(a, b);
                scale(c / shape.getWidth(), d / shape.getHeight());

            } else if (shapeMode == CORNERS) {
                // c and d are x2/y2, make them into width/height
                c -= a;
                d -= b;
                // then same as above
                translate(a, b);
                scale(c / shape.getWidth(), d / shape.getHeight());
            }
            shape.draw(this);

            popMatrix();
        }
    }

    protected void shape(PShape shape, double x, double y, double z, double c, double d, double e) {
        showMissingWarning("shape");
    }

    protected PFont createDefaultFont(double size) {
        java.awt.Font baseFont = new java.awt.Font("Lucida Sans", java.awt.Font.PLAIN, 1);
        return createFont(baseFont, size, true, null, false);
    }

    protected FontCache fontCache = new FontCache();

    protected FontInfo textFontInfo;

    static final class FontInfo {
        static final int MAX_CACHED_COLORS_PER_FONT = 1 << 16;
        javafx.scene.text.Font font;
        double ascent;
        double descent;
        Map<Integer, PImage[]> tintCache;
    }

    static final class FontCache {
        static final int MAX_CACHE_SIZE = 512;

        Map<String, String> nameToFilename = new HashMap<>();
        final HashSet<String> nonNativeNames = new HashSet<>();

        final LinkedHashMap<Key, FontInfo> cache = new LinkedHashMap<Key, FontInfo>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Key, FontInfo> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };

        final Key retrievingKey = new Key();
        final Text measuringText = new Text();

        FontInfo get(String name, double size) {
            if (nonNativeNames.contains(name)) {
                size = 0;
            }
            retrievingKey.name = name;
            retrievingKey.size = (float) size;
            return cache.get(retrievingKey);
        }

        void put(String name, double size, FontInfo fontInfo) {
            if (fontInfo.font == null) {
                nonNativeNames.add(name);
                size = 0;
            }
            Key key = new Key();
            key.name = name;
            key.size = (float) size;
            cache.put(key, fontInfo);
        }

        FontInfo createFontInfo(javafx.scene.text.Font font) {
            FontInfo result = new FontInfo();
            result.font = font;
            if (font != null) {
                // measure ascent and descent
                measuringText.setFont(result.font);
                measuringText.setText(" ");
                double lineHeight = (double) measuringText.getLayoutBounds().getHeight();
                result.ascent = (double) measuringText.getBaselineOffset();
                result.descent = lineHeight - result.ascent;
            }
            return result;
        }

        static final class Key {
            String name;
            float size;

            @Override
			public boolean equals(Object o) {
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;
				Key that = (Key) o;
				if (Double.compare(that.size, size) != 0) return false;
				return name.equals(that.name);
			}

            @Override
			public int hashCode() {
				int result = name.hashCode();
                result = 31 * result + (size != +0.0f ? Float.floatToIntBits(size) : 0);
                return result;
			}
        }
    }

    public PFont createFont(String name, double size, boolean smooth, char[] charset) {
        PFont font = createFontSuper(name, size, smooth, charset);
        if (font.isStream()) {
            fontCache.nameToFilename.put(font.getName(), name);
        }
        return font;
    }

    protected PFont createFontSuper(String name, double size, boolean smooth, char[] charset) {
        String lowerName = name.toLowerCase();
        java.awt.Font baseFont = null;

        try {
            InputStream stream = null;
            if (lowerName.endsWith(".otf") || lowerName.endsWith(".ttf")) {
                stream = parent.createInput(name);
                if (stream == null) {
                    System.err.println("The font \"" + name + "\" " + "is missing or inaccessible, make sure "
                            + "the URL is valid or that the file has been " + "added to your sketch and is readable.");
                    return null;
                }
                baseFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, parent.createInput(name));

            } else {
                baseFont = PFont.findFont(name);
            }
            return createFont(baseFont, size, smooth, charset, stream != null);

        } catch (Exception e) {
            System.err.println("Problem with createFont(\"" + name + "\")");
            e.printStackTrace();
            return null;
        }
    }

    private PFont createFont(java.awt.Font baseFont, double size, boolean smooth, char[] charset, boolean stream) {
        return new PFont(baseFont.deriveFont((float) (size * parent.pixelDensity)), smooth, charset, stream, parent.pixelDensity);
    }

    public void textAlign(int alignX) {
        textAlign(alignX, BASELINE);
    }

    public void textAlign(int alignX, int alignY) {
        textAlign = alignX;
        textAlignY = alignY;
    }

    public double textAscent() {
        if (textFont == null) {
            defaultFontOrDeath("textAscent");
        }
        if (textFontInfo.font == null) {
            return textAscentSuper();
        }
        return textFontInfo.ascent;
    }

    public double textAscentSuper() {
        if (textFont == null) {
            defaultFontOrDeath("textAscent");
        }
        return textFont.ascent() * textSize;
    }

    public double textDescent() {
        if (textFont == null) {
            defaultFontOrDeath("textDescent");
        }
        if (textFontInfo.font == null) {
            return textDescentSuper();
        }
        return textFontInfo.descent;
    }

    public double textDescentSuper() {
        if (textFont == null) {
            defaultFontOrDeath("textDescent");
        }
        return textFont.descent() * textSize;
    }

    public void textFont(PFont which) {
        if (which == null) {
            throw new RuntimeException(ERROR_TEXTFONT_NULL_PFONT);
        }
        textFontImpl(which, which.getDefaultSize());
    }

    public void textFont(PFont which, double size) {
        if (which == null) {
            throw new RuntimeException(ERROR_TEXTFONT_NULL_PFONT);
        }
        if (size <= 0) {
            System.err.println("textFont: ignoring size " + size + " px:" + "the text size must be larger than zero");
            size = textSize;
        }
        textFontImpl(which, size);
    }

    protected void textFontImpl(PFont which, double size) {
        handleTextFont(which, size);
        handleTextSize(size);
    }

    public void textLeading(double leading) {
        textLeading = leading;
    }

    public void textMode(int mode) {
        if ((mode == LEFT) || (mode == RIGHT)) {
            showWarning("Since Processing 1.0 beta, textMode() is now textAlign().");
            return;
        }
        if (mode == SCREEN) {
            showWarning("textMode(SCREEN) has been removed from Processing 2.0.");
            return;
        }

        if (textModeCheck(mode)) {
            textMode = mode;
        } else {
            String modeStr = String.valueOf(mode);
            switch (mode) {
                case MODEL:
                    modeStr = "MODEL";
                    break;
                case SHAPE:
                    modeStr = "SHAPE";
                    break;
            }
            showWarning("textMode(" + modeStr + ") is not supported by this renderer.");
        }
    }

    protected boolean textModeCheck(int mode) {
        return mode == MODEL;
    }

    public void textSize(double size) {
        if (size <= 0) {
            System.err.println("textSize(" + size + ") ignored: " + "the text size must be larger than zero");
            return;
        }
        if (textFont == null) {
            defaultFontOrDeath("textSize", size);
        }
        textSizeImpl(size);
    }

    protected void textSizeImpl(double size) {
        handleTextFont(textFont, size);
        handleTextSize(size);
    }

    protected void handleTextFont(PFont which, double size) {
        textFont = which;

        String fontName = which.getName();
        String fontPsName = which.getPostScriptName();

        textFontInfo = fontCache.get(fontName, size);
        if (textFontInfo == null) {
            javafx.scene.text.Font font = null;

            if (which.isStream()) {
                String filename = fontCache.nameToFilename.get(fontName);
                font = javafx.scene.text.Font.loadFont(parent.createInput(filename), size);
            }

            if (font == null) {
                // Look up font name
                font = new javafx.scene.text.Font(fontName, size);
                if (!fontName.equalsIgnoreCase(font.getName())) {
                    font = new javafx.scene.text.Font(fontPsName, size);
                    if (!fontPsName.equalsIgnoreCase(font.getName())) {
                        font = null; // Done with it
                    }
                }
            }

            if (font == null && which.getNative() != null) {
                font = new javafx.scene.text.Font(size);
            }

            textFontInfo = fontCache.createFontInfo(font);
            fontCache.put(fontName, size, textFontInfo);
        }

        context.setFont(textFontInfo.font);
    }

    protected void handleTextSize(double size) {
        textSize = size;
        textLeading = (textAscent() + textDescent()) * 1.275f;
    }

    public double textWidth(char c) {
        textWidthBuffer[0] = c;
        return textWidthImpl(textWidthBuffer, 0, 1);
    }

    public double textWidth(String str) {
        if (textFont == null) {
            defaultFontOrDeath("textWidth");
        }

        int length = str.length();
        if (length > textWidthBuffer.length) {
            textWidthBuffer = new char[length + 10];
        }
        str.getChars(0, length, textWidthBuffer, 0);

        double wide = 0;
        int index = 0;
        int start = 0;

        while (index < length) {
            if (textWidthBuffer[index] == '\n') {
                wide = Math.max(wide, textWidthImpl(textWidthBuffer, start, index));
                start = index + 1;
            }
            index++;
        }
        if (start < length) {
            wide = Math.max(wide, textWidthImpl(textWidthBuffer, start, index));
        }
        return wide;
    }

    public double textWidth(char[] chars, int start, int length) {
        return textWidthImpl(chars, start, start + length);
    }

    protected double textWidthImpl(char[] buffer, int start, int stop) {
        if (textFont == null) {
            defaultFontOrDeath("textWidth");
        }

        if (textFontInfo.font == null) {
            return textWidthImplSuper(buffer, start, stop);
        }

        fontCache.measuringText.setFont(textFontInfo.font);
        fontCache.measuringText.setText(new String(buffer, start, stop - start));
        return (double) fontCache.measuringText.getLayoutBounds().getWidth();
    }

    protected double textWidthImplSuper(char[] buffer, int start, int stop) {
        double wide = 0;
        for (int i = start; i < stop; i++) {
            // could add kerning here, but it just ain't implemented
            wide += textFont.width(buffer[i]) * textSize;
        }
        return wide;
    }

    public void text(char c, double x, double y) {
        if (textFont == null) {
            defaultFontOrDeath("text");
        }

        if (textAlignY == CENTER) {
            y += textAscent() / 2;
        } else if (textAlignY == TOP) {
            y += textAscent();
        } else if (textAlignY == BOTTOM) {
            y -= textDescent();
        }

        textBuffer[0] = c;
        textLineAlignImpl(textBuffer, 0, 1, x, y);
    }

    public void text(char c, double x, double y, double z) {
        if (z != 0)
            translate(0, 0, z); // slowness, badness
        text(c, x, y);
        if (z != 0)
            translate(0, 0, -z);
    }

    public void text(String str, double x, double y) {
        if (textFont == null) {
            defaultFontOrDeath("text");
        }

        int length = str.length();
        if (length > textBuffer.length) {
            textBuffer = new char[length + 10];
        }
        str.getChars(0, length, textBuffer, 0);
        text(textBuffer, 0, length, x, y);
    }

    public void text(char[] chars, int start, int stop, double x, double y) {
        double high = 0; // -textAscent();
        for (int i = start; i < stop; i++) {
            if (chars[i] == '\n') {
                high += textLeading;
            }
        }
        if (textAlignY == CENTER) {
            y += (textAscent() - high) / 2;
        } else if (textAlignY == TOP) {
            y += textAscent();
        } else if (textAlignY == BOTTOM) {
            y -= textDescent() + high;
        }

        int index = 0;
        while (index < stop) {
            if (chars[index] == '\n') {
                textLineAlignImpl(chars, start, index, x, y);
                start = index + 1;
                y += textLeading;
            }
            index++;
        }
        if (start < stop) {
            textLineAlignImpl(chars, start, index, x, y);
        }
    }

    public void text(String str, double x, double y, double z) {
        if (z != 0)
            translate(0, 0, z); // slow!
        text(str, x, y);
        if (z != 0)
            translate(0, 0, -z); // inaccurate!
    }

    public void text(char[] chars, int start, int stop, double x, double y, double z) {
        if (z != 0)
            translate(0, 0, z); // slow!
        text(chars, start, stop, x, y);
        if (z != 0)
            translate(0, 0, -z); // inaccurate!
    }

    public void text(String str, double x1, double y1, double x2, double y2) {
        if (textFont == null) {
            defaultFontOrDeath("text");
        }

        double hradius, vradius;
        switch (rectMode) {
            case CORNER:
                x2 += x1;
                y2 += y1;
                break;
            case RADIUS:
                hradius = x2;
                vradius = y2;
                x2 = x1 + hradius;
                y2 = y1 + vradius;
                x1 -= hradius;
                y1 -= vradius;
                break;
            case CENTER:
                hradius = x2 / 2.0f;
                vradius = y2 / 2.0f;
                x2 = x1 + hradius;
                y2 = y1 + vradius;
                x1 -= hradius;
                y1 -= vradius;
        }
        if (x2 < x1) {
            double temp = x1;
            x1 = x2;
            x2 = temp;
        }
        if (y2 < y1) {
            double temp = y1;
            y1 = y2;
            y2 = temp;
        }

        double boxWidth = x2 - x1;

        double spaceWidth = textWidth(' ');

        if (textBreakStart == null) {
            textBreakStart = new int[20];
            textBreakStop = new int[20];
        }
        textBreakCount = 0;

        int length = str.length();
        if (length + 1 > textBuffer.length) {
            textBuffer = new char[length + 1];
        }
        str.getChars(0, length, textBuffer, 0);
        // add a fake newline to simplify calculations
        textBuffer[length++] = '\n';

        int sentenceStart = 0;
        for (int i = 0; i < length; i++) {
            if (textBuffer[i] == '\n') {
                boolean legit = textSentence(textBuffer, sentenceStart, i, boxWidth, spaceWidth);
                if (!legit)
                    break;
                sentenceStart = i + 1;
            }
        }

        double lineX = x1; // boxX1;
        if (textAlign == CENTER) {
            lineX = lineX + boxWidth / 2f;
        } else if (textAlign == RIGHT) {
            lineX = x2; // boxX2;
        }

        double boxHeight = y2 - y1;
        double topAndBottom = textAscent() + textDescent();
        int lineFitCount = 1 + floor((boxHeight - topAndBottom) / textLeading);
        int lineCount = Math.min(textBreakCount, lineFitCount);

        if (textAlignY == CENTER) {
            double lineHigh = textAscent() + textLeading * (lineCount - 1);
            double y = y1 + textAscent() + (boxHeight - lineHigh) / 2;
            for (int i = 0; i < lineCount; i++) {
                textLineAlignImpl(textBuffer, textBreakStart[i], textBreakStop[i], lineX, y);
                y += textLeading;
            }

        } else if (textAlignY == BOTTOM) {
            double y = y2 - textDescent() - textLeading * (lineCount - 1);
            for (int i = 0; i < lineCount; i++) {
                textLineAlignImpl(textBuffer, textBreakStart[i], textBreakStop[i], lineX, y);
                y += textLeading;
            }

        } else { // TOP or BASELINE just go to the default
            double y = y1 + textAscent();
            for (int i = 0; i < lineCount; i++) {
                textLineAlignImpl(textBuffer, textBreakStart[i], textBreakStop[i], lineX, y);
                y += textLeading;
            }
        }
    }

    protected boolean textSentence(char[] buffer, int start, int stop, double boxWidth, double spaceWidth) {
        double runningX = 0;

        int lineStart = start;
        int wordStart = start;
        int index = start;
        while (index <= stop) {
            if ((buffer[index] == ' ') || (index == stop)) {
                // System.out.println((index == stop) + " " + wordStart + " " + index);
                double wordWidth = 0;
                if (index > wordStart) {
                    wordWidth = textWidthImpl(buffer, wordStart, index);
                }

                if (runningX + wordWidth >= boxWidth) {
                    if (runningX != 0) {

                        index = wordStart;
                        textSentenceBreak(lineStart, index);

                        while ((index < stop) && (buffer[index] == ' ')) {
                            index++;
                        }
                    } else { // (runningX == 0)

                        if (index - wordStart < 25) {
                            do {
                                index--;
                                if (index == wordStart) {
                                    // Not a single char will fit on this line. screw 'em.
                                    return false;
                                }
                                wordWidth = textWidthImpl(buffer, wordStart, index);
                            } while (wordWidth > boxWidth);
                        } else {

                            int lastIndex = index;
                            index = wordStart + 1;

                            while ((wordWidth = textWidthImpl(buffer, wordStart, index)) < boxWidth) {
                                index++;
                                if (index > lastIndex) { // Unreachable?
                                    break;
                                }
                            }
                            index--;
                            if (index == wordStart) {
                                return false; // nothing fits
                            }
                        }

                        textSentenceBreak(lineStart, index);
                    }
                    lineStart = index;
                    wordStart = index;
                    runningX = 0;

                } else if (index == stop) {

                    textSentenceBreak(lineStart, index);

                    index++;

                } else { // this word will fit, just add it to the line
                    runningX += wordWidth;
                    wordStart = index; // move on to the next word including the space before the word
                    index++;
                }
            } else {
                index++;
            }
        }
        return true;
    }

    protected void textSentenceBreak(int start, int stop) {
        if (textBreakCount == textBreakStart.length) {
            textBreakStart = expand(textBreakStart);
            textBreakStop = expand(textBreakStop);
        }
        textBreakStart[textBreakCount] = start;
        textBreakStop[textBreakCount] = stop;
        textBreakCount++;
    }

    public void text(int num, double x, double y) {
        text(String.valueOf(num), x, y);
    }

    public void text(int num, double x, double y, double z) {
        text(String.valueOf(num), x, y, z);
    }

    public void text(double num, double x, double y) {
        text(nfs(num, 0, 3), x, y);
    }

    public void text(double num, double x, double y, double z) {
        text(nfs(num, 0, 3), x, y, z);
    }

    protected void textLineAlignImpl(char[] buffer, int start, int stop, double x, double y) {
        if (textAlign == CENTER) {
            x -= textWidthImpl(buffer, start, stop) / 2f;

        } else if (textAlign == RIGHT) {
            x -= textWidthImpl(buffer, start, stop);
        }

        textLineImpl(buffer, start, stop, x, y);
    }

    protected void textLineImpl(char[] buffer, int start, int stop, double x, double y) {
        if (textFontInfo.font == null) {
            textLineImplSuper(buffer, start, stop, x, y);
        } else {
            context.fillText(new String(buffer, start, stop - start), x, y);
        }
    }

    protected void textLineImplSuper(char[] buffer, int start, int stop, double x, double y) {
        for (int index = start; index < stop; index++) {
            textCharImpl(buffer[index], x, y);
            x += textWidth(buffer[index]);
        }
    }

    protected PImage getTintedGlyphImage(PFont.Glyph glyph, int tintColor) {
        if (textFontInfo.tintCache == null) {
            textFontInfo.tintCache = new LinkedHashMap<Integer, PImage[]>(16, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<Integer, PImage[]> eldest) {
                    return size() > FontInfo.MAX_CACHED_COLORS_PER_FONT;
                }
            };
        }
        PImage[] tintedGlyphs = textFontInfo.tintCache.get(tintColor);
        int index = glyph.index;
        if (tintedGlyphs == null || tintedGlyphs.length <= index) {
            PImage[] newArray = new PImage[textFont.getGlyphCount()];
            if (tintedGlyphs != null) {
                System.arraycopy(tintedGlyphs, 0, newArray, 0, tintedGlyphs.length);
            }
            tintedGlyphs = newArray;
            textFontInfo.tintCache.put(tintColor, tintedGlyphs);
        }
        PImage tintedGlyph = tintedGlyphs[index];
        if (tintedGlyph == null) {
            tintedGlyph = glyph.image.copy();
            tintedGlyphs[index] = tintedGlyph;
        }
        return tintedGlyph;
    }

    protected void textCharImpl(char ch, double x, double y) { // , double z) {
        PFont.Glyph glyph = textFont.getGlyph(ch);
        if (glyph != null) {
            if (textMode == MODEL) {
                double high = glyph.height / (double) textFont.getSize();
                double bwidth = glyph.width / (double) textFont.getSize();
                double lextent = glyph.leftExtent / (double) textFont.getSize();
                double textent = glyph.topExtent / (double) textFont.getSize();

                double x1 = x + lextent * textSize;
                double y1 = y - textent * textSize;
                double x2 = x1 + bwidth * textSize;
                double y2 = y1 + high * textSize;

                PImage glyphImage = (fillColor == 0xFFFFFFFF) ? glyph.image : getTintedGlyphImage(glyph, fillColor);

                textCharModelImpl(glyphImage, x1, y1, x2, y2, glyph.width, glyph.height);
            }
        } else if (ch != ' ' && ch != 127) {
            showWarning("No glyph found for the " + ch + " (\\u" + hex(ch, 4) + ") character");
        }
    }

    protected void textCharModelImpl(PImage glyph, double x1, double y1, // double z1,
            double x2, double y2, // double z2,
            int u2, int v2) {
        boolean savedTint = tint;
        int savedTintColor = tintColor;
        double savedTintR = tintR;
        double savedTintG = tintG;
        double savedTintB = tintB;
        double savedTintA = tintA;
        boolean savedTintAlpha = tintAlpha;

        tint = true;
        tintColor = fillColor;
        tintR = fillR;
        tintG = fillG;
        tintB = fillB;
        tintA = fillA;
        tintAlpha = fillAlpha;

        imageImpl(glyph, x1, y1, x2, y2, 0, 0, u2, v2);

        tint = savedTint;
        tintColor = savedTintColor;
        tintR = savedTintR;
        tintG = savedTintG;
        tintB = savedTintB;
        tintA = savedTintA;
        tintAlpha = savedTintAlpha;
    }

	public void push() {
        pushStyle();
        pushMatrix();
    }

	public void pop() {
        popStyle();
        popMatrix();
    }

    public void pushMatrix() {
        if (transformCount == transformStack.length) {
            throw new RuntimeException("pushMatrix() cannot use push more than " + transformStack.length + " times");
        }
        transformStack[transformCount] = context.getTransform(transformStack[transformCount]);
        transformCount++;
    }

    public void popMatrix() {
        if (transformCount == 0) {
            throw new RuntimeException("missing a pushMatrix() " + "to go with that popMatrix()");
        }
        transformCount--;
        context.setTransform(transformStack[transformCount]);
    }

    public void translate(double tx, double ty) {
        context.translate(tx, ty);
    }

    public void translate(double x, double y, double z) {
        showMissingWarning("translate");
    }

    public void rotate(double angle) {
        context.rotate(degrees(angle));
    }

    public void rotateX(double angle) {
        showDepthWarning("rotateX");
    }

    public void rotateY(double angle) {
        showDepthWarning("rotateY");
    }

    public void rotateZ(double angle) {
        showDepthWarning("rotateZ");
    }

    public void rotate(double angle, double vx, double vy, double vz) {
        showVariationWarning("rotate");
    }

    public void scale(double s) {
        context.scale(s, s);
    }

    public void scale(double sx, double sy) {
        context.scale(sx, sy);
    }

    public void scale(double sx, double sy, double sz) {
        showDepthWarningXYZ("scale");
    }

    public void shearX(double angle) {
        Affine temp = new Affine();
        temp.appendShear(Math.tan(angle), 0);
        context.transform(temp);
    }

    public void shearY(double angle) {
        Affine temp = new Affine();
        temp.appendShear(0, Math.tan(angle));
        context.transform(temp);
    }

    public void resetMatrix() {
        context.setTransform(new Affine());
    }

    public void applyMatrix(PMatrix source) {
        if (source instanceof PMatrix2D) {
            applyMatrix((PMatrix2D) source);
        } else if (source instanceof PMatrix3D) {
            applyMatrix((PMatrix3D) source);
        }
    }

    public void applyMatrix(double n00, double n01, double n02, double n10, double n11, double n12) {
        context.transform(n00, n10, n01, n11, n02, n12);
    }

    public void applyMatrix(double n00, double n01, double n02, double n03, double n10, double n11, double n12,
            double n13, double n20, double n21, double n22, double n23, double n30, double n31, double n32,
            double n33) {
        showVariationWarning("applyMatrix");
    }

    public void applyMatrix(PMatrix2D source) {
        applyMatrix(source.m00, source.m01, source.m02, source.m10, source.m11, source.m12);
    }

    public void applyMatrix(PMatrix3D source) {
        applyMatrix(source.m00, source.m01, source.m02, source.m03, source.m10, source.m11, source.m12, source.m13,
                source.m20, source.m21, source.m22, source.m23, source.m30, source.m31, source.m32, source.m33);
    }

    public PMatrix getMatrix() {
        return getMatrix((PMatrix2D) null);
    }

    public PMatrix2D getMatrix(PMatrix2D target) {
        if (target == null) {
            target = new PMatrix2D();
        }
        Affine t = context.getTransform();
        target.set((double) t.getMxx(), (double) t.getMxy(), (double) t.getTx(), (double) t.getMyx(),
                (double) t.getMyy(), (double) t.getTy());
        return target;
    }

    public PMatrix3D getMatrix(PMatrix3D target) {
        showVariationWarning("getMatrix");
        return target;
    }

    public void setMatrix(PMatrix source) {
        if (source instanceof PMatrix2D) {
            setMatrix((PMatrix2D) source);
        } else if (source instanceof PMatrix3D) {
            setMatrix((PMatrix3D) source);
        }
    }

    public void setMatrix(PMatrix2D source) {
        context.setTransform(source.m00, source.m10, source.m01, source.m11, source.m02, source.m12);
    }

    public void setMatrix(PMatrix3D source) {
        showVariationWarning("setMatrix");
    }

    public void printMatrix() {
        getMatrix((PMatrix2D) null).print();
    }

    public void beginCamera() {
        showMethodWarning("beginCamera");
    }

    public void endCamera() {
        showMethodWarning("endCamera");
    }

    public void camera() {
        showMissingWarning("camera");
    }

    public void camera(double eyeX, double eyeY, double eyeZ, double centerX, double centerY, double centerZ,
            double upX, double upY, double upZ) {
        showMissingWarning("camera");
    }

    public void printCamera() {
        showMethodWarning("printCamera");
    }

    public void ortho() {
        showMissingWarning("ortho");
    }

    public void ortho(double left, double right, double bottom, double top) {
        showMissingWarning("ortho");
    }

    public void ortho(double left, double right, double bottom, double top, double near, double far) {
        showMissingWarning("ortho");
    }

    public void perspective() {
        showMissingWarning("perspective");
    }

    public void perspective(double fovy, double aspect, double zNear, double zFar) {
        showMissingWarning("perspective");
    }

    public void frustum(double left, double right, double bottom, double top, double near, double far) {
        showMethodWarning("frustum");
    }

    public void printProjection() {
        showMethodWarning("printProjection");
    }

    public double screenX(double x, double y) {
        return (double) context.getTransform().transform(x, y).getX();
    }

    public double screenY(double x, double y) {
        return (double) context.getTransform().transform(x, y).getY();
    }

    public double screenX(double x, double y, double z) {
        showDepthWarningXYZ("screenX");
        return 0;
    }

    public double screenY(double x, double y, double z) {
        showDepthWarningXYZ("screenY");
        return 0;
    }

    public double screenZ(double x, double y, double z) {
        showDepthWarningXYZ("screenZ");
        return 0;
    }

    public double modelX(double x, double y, double z) {
        showMissingWarning("modelX");
        return 0;
    }

    public double modelY(double x, double y, double z) {
        showMissingWarning("modelY");
        return 0;
    }

    public double modelZ(double x, double y, double z) {
        showMissingWarning("modelZ");
        return 0;
    }

    public void pushStyle() {
        if (styleStackDepth == styleStack.length) {
            styleStack = (PStyle[]) expand(styleStack);
        }
        if (styleStack[styleStackDepth] == null) {
            styleStack[styleStackDepth] = new PStyle();
        }
        PStyle s = styleStack[styleStackDepth++];
        getStyle(s);
    }

    public void popStyle() {
        if (styleStackDepth == 0) {
            throw new RuntimeException("Too many popStyle() without enough pushStyle()");
        }
        styleStackDepth--;
        style(styleStack[styleStackDepth]);
    }

    public void style(PStyle s) {
        imageMode(s.imageMode);
        rectMode(s.rectMode);
        ellipseMode(s.ellipseMode);
        shapeMode(s.shapeMode);

        if (blendMode != s.blendMode) {
            blendMode(s.blendMode);
        }

        if (s.tint) {
            tint(s.tintColor);
        } else {
            noTint();
        }
        if (s.fill) {
            fill(s.fillColor);
        } else {
            noFill();
        }
        if (s.stroke) {
            stroke(s.strokeColor);
        } else {
            noStroke();
        }
        strokeWeight(s.strokeWeight);
        strokeCap(s.strokeCap);
        strokeJoin(s.strokeJoin);

        colorMode(RGB, 1);
        ambient(s.ambientR, s.ambientG, s.ambientB);
        emissive(s.emissiveR, s.emissiveG, s.emissiveB);
        specular(s.specularR, s.specularG, s.specularB);
        shininess(s.shininess);

        colorMode(s.colorMode, s.colorModeX, s.colorModeY, s.colorModeZ, s.colorModeA);

        if (s.textFont != null) {
            textFont(s.textFont, s.textSize);
            textLeading(s.textLeading);
        }
        textAlign(s.textAlign, s.textAlignY);
        textMode(s.textMode);
    }

    public PStyle getStyle() { // ignore
        return getStyle(null);
    }

    public PStyle getStyle(PStyle s) { // ignore
        if (s == null) {
            s = new PStyle();
        }

        s.imageMode = imageMode;
        s.rectMode = rectMode;
        s.ellipseMode = ellipseMode;
        s.shapeMode = shapeMode;

        s.blendMode = blendMode;

        s.colorMode = colorMode;
        s.colorModeX = colorModeX;
        s.colorModeY = colorModeY;
        s.colorModeZ = colorModeZ;
        s.colorModeA = colorModeA;

        s.tint = tint;
        s.tintColor = tintColor;
        s.fill = fill;
        s.fillColor = fillColor;
        s.stroke = stroke;
        s.strokeColor = strokeColor;
        s.strokeWeight = strokeWeight;
        s.strokeCap = strokeCap;
        s.strokeJoin = strokeJoin;

        s.ambientR = ambientR;
        s.ambientG = ambientG;
        s.ambientB = ambientB;
        s.specularR = specularR;
        s.specularG = specularG;
        s.specularB = specularB;
        s.emissiveR = emissiveR;
        s.emissiveG = emissiveG;
        s.emissiveB = emissiveB;
        s.shininess = shininess;

        s.textFont = textFont;
        s.textAlign = textAlign;
        s.textAlignY = textAlignY;
        s.textMode = textMode;
        s.textSize = textSize;
        s.textLeading = textLeading;

        return s;
    }

    public void strokeWeight(double weight) {
        strokeWeightSuper(weight);
        context.setLineWidth(weight);
    }

    public void strokeWeightSuper(double weight) {
        strokeWeight = weight;
    }

    public void strokeJoin(int join) {
        strokeJoinSuper(join);
        if (strokeJoin == MITER) {
            context.setLineJoin(StrokeLineJoin.MITER);
        } else if (strokeJoin == ROUND) {
            context.setLineJoin(StrokeLineJoin.ROUND);
        } else {
            context.setLineJoin(StrokeLineJoin.BEVEL);
        }
    }

    public void strokeJoinSuper(int join) {
        strokeJoin = join;
    }

    public void strokeCap(int cap) {
        strokeCapSuper(cap);
        if (strokeCap == ROUND) {
            context.setLineCap(StrokeLineCap.ROUND);
        } else if (strokeCap == PROJECT) {
            context.setLineCap(StrokeLineCap.SQUARE);
        } else {
            context.setLineCap(StrokeLineCap.BUTT);
        }
    }

    public void strokeCapSuper(int cap) {
        strokeCap = cap;
    }

    public void noStroke() {
        stroke = false;
    }

    public void stroke(int rgb) {
        colorCalc(rgb);
        strokeFromCalc();
    }

    public void stroke(int rgb, double alpha) {
        colorCalc(rgb, alpha);
        strokeFromCalc();
    }

    public void stroke(double gray) {
        colorCalc(gray);
        strokeFromCalc();
    }

    public void stroke(double gray, double alpha) {
        colorCalc(gray, alpha);
        strokeFromCalc();
    }

    public void stroke(double v1, double v2, double v3) {
        colorCalc(v1, v2, v3);
        strokeFromCalc();
    }

    public void stroke(double v1, double v2, double v3, double alpha) {
        colorCalc(v1, v2, v3, alpha);
        strokeFromCalc();
    }

    protected void strokeFromCalc() {
        stroke = true;
        strokeFromCalcSuper();
        context.setStroke(new Color(strokeR, strokeG, strokeB, strokeA));
    }

    protected void strokeFromCalcSuper() {
        stroke = true;
        strokeR = calcR;
        strokeG = calcG;
        strokeB = calcB;
        strokeA = calcA;
        strokeRi = calcRi;
        strokeGi = calcGi;
        strokeBi = calcBi;
        strokeAi = calcAi;
        strokeColor = calcColor;
        strokeAlpha = calcAlpha;
    }

    protected boolean drawingThinLines() {
        return stroke && strokeWeight == 1;
    }

    public void noTint() {
        tint = false;
    }

    public void tint(int rgb) {
        colorCalc(rgb);
        tintFromCalc();
    }

    public void tint(int rgb, double alpha) {
        colorCalc(rgb, alpha);
        tintFromCalc();
    }

    public void tint(double gray) {
        colorCalc(gray);
        tintFromCalc();
    }

    public void tint(double gray, double alpha) {
        colorCalc(gray, alpha);
        tintFromCalc();
    }

    public void tint(double v1, double v2, double v3) {
        colorCalc(v1, v2, v3);
        tintFromCalc();
    }

    public void tint(double v1, double v2, double v3, double alpha) {
        colorCalc(v1, v2, v3, alpha);
        tintFromCalc();
    }

    protected void tintFromCalc() {
        tint = true;
        tintR = calcR;
        tintG = calcG;
        tintB = calcB;
        tintA = calcA;
        tintRi = calcRi;
        tintGi = calcGi;
        tintBi = calcBi;
        tintAi = calcAi;
        tintColor = calcColor;
        tintAlpha = calcAlpha;
    }

    public void noFill() {
        fill = false;
    }

    public void fill(int rgb) {
        fill = true;
        colorCalc(rgb);
        fillFromCalc();
    }

    public void fill(int rgb, double alpha) {
        colorCalc(rgb, alpha);
        fillFromCalc();
    }

    public void fill(double gray) {
        colorCalc(gray);
        fillFromCalc();
    }

    public void fill(double gray, double alpha) {
        colorCalc(gray, alpha);
        fillFromCalc();
    }

    public void fill(double v1, double v2, double v3) {
        colorCalc(v1, v2, v3);
        fillFromCalc();
    }

    public void fill(double v1, double v2, double v3, double alpha) {
        colorCalc(v1, v2, v3, alpha);
        fillFromCalc();
    }

    protected void fillFromCalc() {
        fill = true;
        fillFromCalcSuper();
        context.setFill(new Color(fillR, fillG, fillB, fillA));
    }

    protected void fillFromCalcSuper() {
        fill = true;
        fillR = calcR;
        fillG = calcG;
        fillB = calcB;
        fillA = calcA;
        fillRi = calcRi;
        fillGi = calcGi;
        fillBi = calcBi;
        fillAi = calcAi;
        fillColor = calcColor;
        fillAlpha = calcAlpha;
    }

    public void ambient(int rgb) {
        colorCalc(rgb);
        ambientFromCalc();
    }

    public void ambient(double gray) {
        colorCalc(gray);
        ambientFromCalc();
    }

    public void ambient(double v1, double v2, double v3) {
        colorCalc(v1, v2, v3);
        ambientFromCalc();
    }

    protected void ambientFromCalc() {
        ambientColor = calcColor;
        ambientR = calcR;
        ambientG = calcG;
        ambientB = calcB;
        setAmbient = true;
    }

    public void specular(int rgb) {
        colorCalc(rgb);
        specularFromCalc();
    }

    public void specular(double gray) {
        colorCalc(gray);
        specularFromCalc();
    }

    public void specular(double v1, double v2, double v3) {
        colorCalc(v1, v2, v3);
        specularFromCalc();
    }

    protected void specularFromCalc() {
        specularColor = calcColor;
        specularR = calcR;
        specularG = calcG;
        specularB = calcB;
    }

    public void shininess(double shine) {
        shininess = shine;
    }

    public void emissive(int rgb) {
        colorCalc(rgb);
        emissiveFromCalc();
    }

    public void emissive(double gray) {
        colorCalc(gray);
        emissiveFromCalc();
    }

    public void emissive(double v1, double v2, double v3) {
        colorCalc(v1, v2, v3);
        emissiveFromCalc();
    }

    protected void emissiveFromCalc() {
        emissiveColor = calcColor;
        emissiveR = calcR;
        emissiveG = calcG;
        emissiveB = calcB;
    }

    public void lights() {
        showMethodWarning("lights");
    }

    public void noLights() {
        showMethodWarning("noLights");
    }

    public void ambientLight(double v1, double v2, double v3) {
        showMethodWarning("ambientLight");
    }

    public void ambientLight(double v1, double v2, double v3, double x, double y, double z) {
        showMethodWarning("ambientLight");
    }

    public void directionalLight(double v1, double v2, double v3, double nx, double ny, double nz) {
        showMethodWarning("directionalLight");
    }

    public void pointLight(double v1, double v2, double v3, double x, double y, double z) {
        showMethodWarning("pointLight");
    }

    public void spotLight(double v1, double v2, double v3, double x, double y, double z, double nx, double ny,
            double nz, double angle, double concentration) {
        showMethodWarning("spotLight");
    }

    public void lightFalloff(double constant, double linear, double quadratic) {
        showMethodWarning("lightFalloff");
    }

    public void lightSpecular(double v1, double v2, double v3) {
        showMethodWarning("lightSpecular");
    }

    public void background(int rgb) {
        colorCalc(rgb);
        backgroundFromCalc();
    }

    public void background(int rgb, double alpha) {
        colorCalc(rgb, alpha);
        backgroundFromCalc();
    }

    public void background(double gray) {
        colorCalc(gray);
        backgroundFromCalc();
    }

    public void background(double gray, double alpha) {
        if (format == RGB) {
            background(gray);
        } else {
            colorCalc(gray, alpha);
            backgroundFromCalc();
        }
    }

    public void background(double v1, double v2, double v3) {
        colorCalc(v1, v2, v3);
        backgroundFromCalc();
    }

    public void background(double v1, double v2, double v3, double alpha) {
        colorCalc(v1, v2, v3, alpha);
        backgroundFromCalc();
    }

    public void clear() {
        background(0, 0, 0, 0);
    }

    protected void backgroundFromCalc() {
        backgroundR = calcR;
        backgroundG = calcG;
        backgroundB = calcB;
        backgroundA = (format == RGB) ? 1 : calcA;
        backgroundRi = calcRi;
        backgroundGi = calcGi;
        backgroundBi = calcBi;
        backgroundAi = (format == RGB) ? 255 : calcAi;
        backgroundAlpha = (format == RGB) ? false : calcAlpha;
        backgroundColor = calcColor;

        backgroundImpl();
    }

    public void background(PImage image) {
        if ((image.pixelWidth != pixelWidth) || (image.pixelHeight != pixelHeight)) {
            throw new RuntimeException(ERROR_BACKGROUND_IMAGE_SIZE);
        }
        if ((image.format != RGB) && (image.format != ARGB)) {
            throw new RuntimeException(ERROR_BACKGROUND_IMAGE_FORMAT);
        }
        backgroundColor = 0;
        backgroundImpl(image);
    }

    protected void backgroundImpl(PImage image) {
        set(0, 0, image);
    }

    public void backgroundImpl() {
        modified = false;
        loaded = false;
        context.save();
        context.setTransform(new Affine());
        context.setFill(new Color(backgroundR, backgroundG, backgroundB, backgroundA));
        context.setGlobalBlendMode(BlendMode.SRC_OVER);
        context.fillRect(0, 0, width, height);
        context.restore();
    }

    public void loadPixels() {
        if ((pixels == null) || (pixels.length != pixelWidth * pixelHeight)) {
            pixels = new int[pixelWidth * pixelHeight];
            loaded = false;
        }

        if (!loaded) {
            if (snapshotImage == null || snapshotImage.getWidth() != pixelWidth
                    || snapshotImage.getHeight() != pixelHeight) {
                snapshotImage = new WritableImage(pixelWidth, pixelHeight);
            }

            SnapshotParameters sp = null;
            if (pixelDensity != 1) {
                sp = new SnapshotParameters();
                sp.setTransform(Transform.scale(pixelDensity, pixelDensity));
            }
            snapshotImage = surface.canvas.snapshot(sp, snapshotImage);
            PixelReader pr = snapshotImage.getPixelReader();
            pr.getPixels(0, 0, pixelWidth, pixelHeight, argbFormat, pixels, 0, pixelWidth);

            loaded = true;
            modified = false;
        }
    }

    public int get(int x, int y) {
        loadPixels();
        return super.get(x, y);
    }

    protected void getImpl(int sourceX, int sourceY, int sourceWidth, int sourceHeight, PImage target, int targetX,
            int targetY) {
        loadPixels();
        super.getImpl(sourceX, sourceY, sourceWidth, sourceHeight, target, targetX, targetY);
    }

    public void set(int x, int y, int argb) {
        loadPixels();
        super.set(x, y, argb);
    }

    protected void setImpl(PImage sourceImage, int sourceX, int sourceY, int sourceWidth, int sourceHeight, int targetX,
            int targetY) {
        sourceImage.loadPixels();

        int sourceOffset = sourceX + sourceImage.pixelWidth * sourceY;

        PixelWriter pw = context.getPixelWriter();
        pw.setPixels(targetX, targetY, sourceWidth, sourceHeight, argbFormat, sourceImage.pixels, sourceOffset,
                sourceImage.pixelWidth);

        // Let's keep them loaded
        if (loaded) {
            int sourceStride = sourceImage.pixelWidth;
            int targetStride = pixelWidth;
            int targetOffset = targetX + targetY * targetStride;
            for (int i = 0; i < sourceHeight; i++) {
                System.arraycopy(sourceImage.pixels, sourceOffset + i * sourceStride, pixels,
                        targetOffset + i * targetStride, sourceWidth);
            }
        }
    }

    static final String MASK_WARNING = "mask() cannot be used on the main drawing surface";

    public void colorMode(int mode) {
        colorMode(mode, colorModeX, colorModeY, colorModeZ, colorModeA);
    }

    public void colorMode(int mode, double max) {
        colorMode(mode, max, max, max, max);
    }

    public void colorMode(int mode, double max1, double max2, double max3) {
        colorMode(mode, max1, max2, max3, colorModeA);
    }

    public void colorMode(int mode, double max1, double max2, double max3, double maxA) {
        colorMode = mode;

        colorModeX = max1;
        colorModeY = max2;
        colorModeZ = max3;
        colorModeA = maxA;

        colorModeScale = ((maxA != 1) || (max1 != max2) || (max2 != max3) || (max3 != maxA));

        colorModeDefault = (colorMode == RGB) && (colorModeA == 255) && (colorModeX == 255) && (colorModeY == 255)
                && (colorModeZ == 255);
    }

    protected void colorCalc(int rgb) {
        if (((rgb & 0xff000000) == 0) && (rgb <= colorModeX)) {
            colorCalc((double) rgb);

        } else {
            colorCalcARGB(rgb, colorModeA);
        }
    }

    protected void colorCalc(int rgb, double alpha) {
        if (((rgb & 0xff000000) == 0) && (rgb <= colorModeX)) { // see above
            colorCalc((double) rgb, alpha);

        } else {
            colorCalcARGB(rgb, alpha);
        }
    }

    protected void colorCalc(double gray) {
        colorCalc(gray, colorModeA);
    }

    protected void colorCalc(double gray, double alpha) {
        if (gray > colorModeX)
            gray = colorModeX;
        if (alpha > colorModeA)
            alpha = colorModeA;

        if (gray < 0)
            gray = 0;
        if (alpha < 0)
            alpha = 0;

        calcR = colorModeScale ? (gray / colorModeX) : gray;
        calcG = calcR;
        calcB = calcR;
        calcA = colorModeScale ? (alpha / colorModeA) : alpha;

        calcRi = (int) (calcR * 255);
        calcGi = (int) (calcG * 255);
        calcBi = (int) (calcB * 255);
        calcAi = (int) (calcA * 255);
        calcColor = (calcAi << 24) | (calcRi << 16) | (calcGi << 8) | calcBi;
        calcAlpha = (calcAi != 255);
    }

    protected void colorCalc(double x, double y, double z) {
        colorCalc(x, y, z, colorModeA);
    }

    protected void colorCalc(double x, double y, double z, double a) {
        if (x > colorModeX)
            x = colorModeX;
        if (y > colorModeY)
            y = colorModeY;
        if (z > colorModeZ)
            z = colorModeZ;
        if (a > colorModeA)
            a = colorModeA;

        if (x < 0)
            x = 0;
        if (y < 0)
            y = 0;
        if (z < 0)
            z = 0;
        if (a < 0)
            a = 0;

        switch (colorMode) {
            case RGB:
                if (colorModeScale) {
                    calcR = x / colorModeX;
                    calcG = y / colorModeY;
                    calcB = z / colorModeZ;
                    calcA = a / colorModeA;
                } else {
                    calcR = x;
                    calcG = y;
                    calcB = z;
                    calcA = a;
                }
                break;

            case HSB:
                x /= colorModeX; // h
                y /= colorModeY; // s
                z /= colorModeZ; // b

                calcA = colorModeScale ? (a / colorModeA) : a;

                if (y == 0) { // saturation == 0
                    calcR = calcG = calcB = z;

                } else {
                    double which = (x - (int) x) * 6.0f;
                    double f = which - (int) which;
                    double p = z * (1.0f - y);
                    double q = z * (1.0f - y * f);
                    double t = z * (1.0f - (y * (1.0f - f)));

                    switch ((int) which) {
                        case 0:
                            calcR = z;
                            calcG = t;
                            calcB = p;
                            break;
                        case 1:
                            calcR = q;
                            calcG = z;
                            calcB = p;
                            break;
                        case 2:
                            calcR = p;
                            calcG = z;
                            calcB = t;
                            break;
                        case 3:
                            calcR = p;
                            calcG = q;
                            calcB = z;
                            break;
                        case 4:
                            calcR = t;
                            calcG = p;
                            calcB = z;
                            break;
                        case 5:
                            calcR = z;
                            calcG = p;
                            calcB = q;
                            break;
                    }
                }
                break;
        }
        calcRi = (int) (255 * calcR);
        calcGi = (int) (255 * calcG);
        calcBi = (int) (255 * calcB);
        calcAi = (int) (255 * calcA);
        calcColor = (calcAi << 24) | (calcRi << 16) | (calcGi << 8) | calcBi;
        calcAlpha = (calcAi != 255);
    }

    protected void colorCalcARGB(int argb, double alpha) {
        if (alpha == colorModeA) {
            calcAi = (argb >> 24) & 0xff;
            calcColor = argb;
        } else {
            calcAi = (int) (((argb >> 24) & 0xff) * constrain((alpha / colorModeA), 0, 1));
            calcColor = (calcAi << 24) | (argb & 0xFFFFFF);
        }
        calcRi = (argb >> 16) & 0xff;
        calcGi = (argb >> 8) & 0xff;
        calcBi = argb & 0xff;
        calcA = calcAi / 255.0f;
        calcR = calcRi / 255.0f;
        calcG = calcGi / 255.0f;
        calcB = calcBi / 255.0f;
        calcAlpha = (calcAi != 255);
    }

    public final int color(int c) {
        colorCalc(c);
        return calcColor;
    }

    public final int color(double gray) {
        colorCalc(gray);
        return calcColor;
    }

    public final int color(int c, int alpha) {
        colorCalc(c, alpha);
        return calcColor;
    }

    public final int color(int c, double alpha) {
        colorCalc(c, alpha);
        return calcColor;
    }

    public final int color(double gray, double alpha) { // ignore
        colorCalc(gray, alpha);
        return calcColor;
    }

    public final int color(int v1, int v2, int v3) { // ignore
        colorCalc(v1, v2, v3);
        return calcColor;
    }

    public final int color(double v1, double v2, double v3) { // ignore
        colorCalc(v1, v2, v3);
        return calcColor;
    }

    public final int color(int v1, int v2, int v3, int a) { // ignore
        colorCalc(v1, v2, v3, a);
        return calcColor;
    }

    public final int color(double v1, double v2, double v3, double a) { // ignore
        colorCalc(v1, v2, v3, a);
        return calcColor;
    }

    public final double alpha(int rgb) {
        double outgoing = (rgb >> 24) & 0xff;
        if (colorModeA == 255)
            return outgoing;
        return (outgoing / 255.0f) * colorModeA;
    }

    public final double red(int rgb) {
        double c = (rgb >> 16) & 0xff;
        if (colorModeDefault)
            return c;
        return (c / 255.0f) * colorModeX;
    }

    public final double green(int rgb) {
        double c = (rgb >> 8) & 0xff;
        if (colorModeDefault)
            return c;
        return (c / 255.0f) * colorModeY;
    }

    public final double blue(int rgb) {
        double c = (rgb) & 0xff;
        if (colorModeDefault)
            return c;
        return (c / 255.0f) * colorModeZ;
    }

    public final double hue(int rgb) {
        if (rgb != cacheHsbKey) {
            RGBtoHSB((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff, cacheHsbValue);
            cacheHsbKey = rgb;
        }
        return cacheHsbValue[0] * colorModeX;
    }

    public final double saturation(int rgb) {
        if (rgb != cacheHsbKey) {
            RGBtoHSB((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff, cacheHsbValue);
            cacheHsbKey = rgb;
        }
        return cacheHsbValue[1] * colorModeY;
    }

    public final double brightness(int rgb) {
        if (rgb != cacheHsbKey) {
            RGBtoHSB((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff, cacheHsbValue);
            cacheHsbKey = rgb;
        }
        return cacheHsbValue[2] * colorModeZ;
    }

    public int lerpColor(int c1, int c2, double amt) { // ignore
        return lerpColor(c1, c2, amt, colorMode);
    }

    static double[] lerpColorHSB1;
    static double[] lerpColorHSB2;

    static public int lerpColor(int c1, int c2, double amt, int mode) {
        if (amt < 0)
            amt = 0;
        if (amt > 1)
            amt = 1;

        if (mode == RGB) {
            double a1 = ((c1 >> 24) & 0xff);
            double r1 = (c1 >> 16) & 0xff;
            double g1 = (c1 >> 8) & 0xff;
            double b1 = c1 & 0xff;
            double a2 = (c2 >> 24) & 0xff;
            double r2 = (c2 >> 16) & 0xff;
            double g2 = (c2 >> 8) & 0xff;
            double b2 = c2 & 0xff;

            return ((round(a1 + (a2 - a1) * amt) << 24) | (round(r1 + (r2 - r1) * amt) << 16)
                    | (round(g1 + (g2 - g1) * amt) << 8) | (round(b1 + (b2 - b1) * amt)));

        } else if (mode == HSB) {
            if (lerpColorHSB1 == null) {
                lerpColorHSB1 = new double[3];
                lerpColorHSB2 = new double[3];
            }

            double a1 = (c1 >> 24) & 0xff;
            double a2 = (c2 >> 24) & 0xff;
            int alfa = (round(a1 + (a2 - a1) * amt)) << 24;

            RGBtoHSB((c1 >> 16) & 0xff, (c1 >> 8) & 0xff, c1 & 0xff, lerpColorHSB1);
            RGBtoHSB((c2 >> 16) & 0xff, (c2 >> 8) & 0xff, c2 & 0xff, lerpColorHSB2);

            double ho = (double) lerp(lerpColorHSB1[0], lerpColorHSB2[0], amt);
            double so = (double) lerp(lerpColorHSB1[1], lerpColorHSB2[1], amt);
            double bo = (double) lerp(lerpColorHSB1[2], lerpColorHSB2[2], amt);

            return alfa | (HSBtoRGB(ho, so, bo) & 0xFFFFFF);
        }
        return 0;
    }

    public void beginRaw(PGraphics rawGraphics) { // ignore
        this.raw = rawGraphics;
        rawGraphics.beginDraw();
    }

    public void endRaw() { // ignore
        if (raw != null) {
            // for 3D, need to flush any geometry that's been stored for sorting
            // (particularly if the ENABLE_DEPTH_SORT hint is set)
            flush();

            // just like beginDraw, this will have to be called because
            // endDraw() will be happening outside of draw()
            raw.endDraw();
            raw.dispose();
            raw = null;
        }
    }

    public boolean haveRaw() { // ignore
        return raw != null;
    }

    public PGraphics getRaw() { // ignore
        return raw;
    }

    static protected Map<String, Object> warnings;

    static public void showWarning(String msg) { // ignore
        if (warnings == null) {
            warnings = new HashMap<>();
        }
        if (!warnings.containsKey(msg)) {
            System.err.println(msg);
            warnings.put(msg, new Object());
        }
    }

    static public void showWarning(String msg, Object... args) { // ignore
        showWarning(String.format(msg, args));
    }

    static public void showDepthWarning(String method) {
        showWarning(method + "() can only be used with a renderer that " + "supports 3D, such as P3D.");
    }

    static public void showDepthWarningXYZ(String method) {
        showWarning(method + "() with x, y, and z coordinates " + "can only be used with a renderer that "
                + "supports 3D, such as P3D. " + "Use a version without a z-coordinate instead.");
    }

    static public void showMethodWarning(String method) {
        showWarning(method + "() is not available with this renderer.");
    }

    static public void showVariationWarning(String str) {
        showWarning(str + " is not available with this renderer.");
    }

    static public void showMissingWarning(String method) {
        showWarning(method + "(), or this particular variation of it, " + "is not available with this renderer.");
    }

    public void mask(PImage alpha) {
        showWarning(MASK_WARNING);
    }

    static public void showTodoWarning(String method, int issue) {
        showWarning(method + "() is not yet available: " + "https://github.com/processing/processing/issues/" + issue);
    }

    static public void showException(String msg) { // ignore
        throw new RuntimeException(msg);
    }

    protected void defaultFontOrDeath(String method) {
        defaultFontOrDeath(method, 12);
    }

    protected void defaultFontOrDeath(String method, double size) {
        defaultFontOrDeathSuper(method, size);
        handleTextFont(textFont, size);
    }

    protected void defaultFontOrDeathSuper(String method, double size) {
        if (parent != null) {
            textFont = createDefaultFont(size);
        } else {
            throw new RuntimeException("Use textFont() before " + method + "()");
        }
    }

    public boolean displayable() { // ignore
        return true;
    }

    public boolean is2D() { // ignore
        return true;
    }

    public boolean is3D() { // ignore
        return false;
    }

    public boolean isGL() { // ignore
        return false;
    }

    public boolean is2X() {
        return pixelDensity == 2;
    }

    @Override
    public boolean save(String filename) { // ignore

        if (hints[DISABLE_ASYNC_SAVEFRAME]) {
            return super.save(filename);
        }

        if (asyncImageSaver == null) {
            asyncImageSaver = new AsyncImageSaver();
        }

        if (!loaded)
            loadPixels();
        PImage target = asyncImageSaver.getAvailableTarget(pixelWidth, pixelHeight, format);
        if (target == null)
            return false;
        int count = min(pixels.length, target.pixels.length);
        System.arraycopy(pixels, 0, target.pixels, 0, count);
        asyncImageSaver.saveTargetAsync(this, target, parent.sketchFile(filename));

        return true;
    }

    protected void processImageBeforeAsyncSave(PImage image) {
    }

    public void awaitAsyncSaveCompletion(String filename) {
        if (asyncImageSaver != null) {
            asyncImageSaver.awaitAsyncSaveCompletion(parent.sketchFile(filename));
        }
    }

    protected static AsyncImageSaver asyncImageSaver;

    protected static class AsyncImageSaver {

        static final int TARGET_COUNT = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);

        BlockingQueue<PImage> targetPool = new ArrayBlockingQueue<>(TARGET_COUNT);
        ExecutorService saveExecutor = Executors.newFixedThreadPool(TARGET_COUNT);

        int targetsCreated = 0;

        Map<File, Future<?>> runningTasks = new HashMap<>();
        final Object runningTasksLock = new Object();

        static final int TIME_AVG_FACTOR = 32;

        volatile long avgNanos = 0;
        long lastTime = 0;
        int lastFrameCount = 0;

        public AsyncImageSaver() {
        } // ignore

        public void dispose() { // ignore
            saveExecutor.shutdown();
            try {
                saveExecutor.awaitTermination(5000, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
            }
        }

        public boolean hasAvailableTarget() { // ignore
            return targetsCreated < TARGET_COUNT || targetPool.isEmpty();
        }

        public PImage getAvailableTarget(int requestedWidth, int requestedHeight, // ignore
                int format) {
            try {
                PImage target;
                if (targetsCreated < TARGET_COUNT && targetPool.isEmpty()) {
                    target = new PImage(requestedWidth, requestedHeight);
                    targetsCreated++;
                } else {
                    target = targetPool.take();
                    if (target.pixelWidth != requestedWidth || target.pixelHeight != requestedHeight) {

                        target = new PImage(requestedWidth, requestedHeight);
                    }
                }
                target.format = format;
                return target;
            } catch (InterruptedException e) {
                return null;
            }
        }

        public void returnUnusedTarget(PImage target) { // ignore
            targetPool.offer(target);
        }

        public void saveTargetAsync(final PGraphics renderer, final PImage target, // ignore
                final File file) {
            target.parent = renderer.parent;

            // if running every frame, smooth the framerate
            if (target.parent.frameCount - 1 == lastFrameCount && TARGET_COUNT > 1) {

                // count with one less thread to reduce jitter
                // 2 cores - 1 save thread - no wait
                // 4 cores - 3 save threads - wait 1/2 of save time
                // 8 cores - 7 save threads - wait 1/6 of save time
                long avgTimePerFrame = avgNanos / (Math.max(1, TARGET_COUNT - 1));
                long now = System.nanoTime();
                long delay = round((lastTime + avgTimePerFrame - now) / 1e6f);
                try {
                    if (delay > 0)
                        Thread.sleep(delay);
                } catch (InterruptedException e) {
                }
            }

            lastFrameCount = target.parent.frameCount;
            lastTime = System.nanoTime();

            awaitAsyncSaveCompletion(file);

            // Explicit lock, because submitting a task and putting it into map
            // has to be atomic (and happen before task tries to remove itself)
            synchronized (runningTasksLock) {
                try {
                    Future<?> task = saveExecutor.submit(() -> {
                        try {
                            long startTime = System.nanoTime();
                            renderer.processImageBeforeAsyncSave(target);
                            target.save(file.getAbsolutePath());
                            long saveNanos = System.nanoTime() - startTime;
                            synchronized (AsyncImageSaver.this) {
                                if (avgNanos == 0) {
                                    avgNanos = saveNanos;
                                } else if (saveNanos < avgNanos) {
                                    avgNanos = (avgNanos * (TIME_AVG_FACTOR - 1) + saveNanos) / (TIME_AVG_FACTOR);
                                } else {
                                    avgNanos = saveNanos;
                                }
                            }
                        } finally {
                            targetPool.offer(target);
                            synchronized (runningTasksLock) {
                                runningTasks.remove(file);
                            }
                        }
                    });
                    runningTasks.put(file, task);
                } catch (RejectedExecutionException e) {
                    // the executor service was probably shut down, no more saving for us
                }
            }
        }

        public void awaitAsyncSaveCompletion(final File file) { // ignore
            Future<?> taskWithSameFilename;
            synchronized (runningTasksLock) {
                taskWithSameFilename = runningTasks.get(file);
            }

            if (taskWithSameFilename != null) {
                try {
                    taskWithSameFilename.get();
                } catch (InterruptedException | ExecutionException e) {
                }
            }
        }
    }

    public void box(double f) {
    }
}