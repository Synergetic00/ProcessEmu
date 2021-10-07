package main;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Shape;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Shear;
import ptypes.PFont;
import ptypes.PGraphics;
import ptypes.PImage;
import ptypes.PMatrix2D;
import ptypes.PShape;
import ptypes.PSurface;
import ptypes.PVector;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import utils.Colours;
import utils.Maths;
import utils.ModeState;
import utils.StyleState;

import static utils.Maths.*;
import static utils.Constants.*;

@SuppressWarnings("unused")
public class AppBase {

    private GraphicsContext gc;
    private Calendar date;
    private boolean looping;

    public PSurface surface;

    private boolean hasFill;
    private boolean hasStroke;

    public int rectMode;
    public int ellipseMode;
    public int colorMode;

    // Constructor for the object and the internal variables
    public AppBase(GraphicsContext gc) {
        this.gc = gc;
        this.date = new Calendar.Builder().build();
        this.surface = new PSurface();
        defaultSettings();
    }

    // Initial internal variables in Processing
    private void defaultSettings() {
        frameCount = 0;
        focused = true;
        resetMatrix();
        updateTime();
        frameRate(60);
        size(100, 100);
        colorMode(RGB, 255, 255, 255, 255);
        background(204);
        fill(255);
        stroke(0);
        strokeWeight(1);
        strokeCap(ROUND);
        strokeJoin(MITER);
        textAlign(LEFT, BASELINE);
        looping = true;
        rectMode = CORNER;
        ellipseMode = CENTER;
        surface.reset();
    }

    ///////////////
    // Structure //
    ///////////////

    private ArrayList<StyleState> styleStates = new ArrayList<>();
    private ArrayList<ModeState> modeStates = new ArrayList<>();

    public void draw() {}

    public void exit() {
        Loader.launchHomeScreen();
    }

    public void loop() {
        looping = true;
    }

    public void noLoop() {
        looping = false;
    }

    public void pop() {
        popMatrix();
        popStyle();
    }

    public void popStyle() {
        if (styleStates.size() > 0) {
            setState(styleStates.remove(styleStates.size()-1));
        } else {
            throw new RuntimeException("popStyle() needs corresponding pushStyle() statement");
        }

        if (styleStates.size() > 0) {
            ModeState ms = modeStates.remove(modeStates.size()-1);
            rectMode = ms.rectMode;
            ellipseMode = ms.ellipseMode;
            colorMode = ms.colorMode;
        }
    }

    public void push() {
        pushMatrix();
        pushStyle();
    }

    public void pushStyle() {
        modeStates.add(new ModeState(0, rectMode, ellipseMode, colorMode));
        styleStates.add(new StyleState(gc));
    }

    public void redraw() {
        render();
    }

    public void setup() {}

    public void thread(final String name) {
        Thread later = new Thread() {
            @Override
            public void run() {
                method(name);
            }
        };
        later.start();
    }

    /////////////////
    // Environment //
    /////////////////

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

    public void delay(int napTime) {
        try {
            Thread.sleep(napTime);
        } catch (InterruptedException ie) {}
    }

    public int displayDensity() {
        return 1;
    }

    public int displayDensity(int display) {
        return 1;
    }

    public boolean focused;

    public int frameCount;

    public void frameRate(double fps) {
        if (fps > 0) {
            frameRate = fps;
            Main.animation.setRate(-frameRate);
        }
    }

    public double frameRate;

    public void fullScreen() {
        size(AppState.screenW(), AppState.screenH());
    }

    public int height;

    public void noCursor() {
        surface.hideCursor();
    }

    public void noSmooth() {}

    public void pixelDensity(int density) {}

    public int pixelHeight;

    public int pixelWidth;

    public void settings() {}

    public void size(int width, int height) {
        double widthMul = min(1d, AppState.screenW() / (double)width);
        double heightMul = min(1d, AppState.screenH() / (double)height);

        this.width = width;
        this.height = height;

        double minMul = min(widthMul, heightMul);

        if (minMul < 1) {
            gc.translate(0, 320);
            gc.scale(minMul, minMul);
        }

        AppState.offsetW((AppState.screenW() - width) / 2);
        AppState.offsetH((AppState.screenH() - height) / 2);

        background(204);
    }

    public void smooth(int level) {}

    public int width;

    ///////////
    // Shape //
    ///////////

    public PShape createShape() {
        return null;
    }

    public PShape createShape(int type) {
        return null;
    }

    public PShape createShape(int kind, double... p) {
        return null;
    }

    public PShape loadShape(String filename) {
        return null;
    }

    ////////////////////////////
    // Shape // 2D Primatives //
    ////////////////////////////

    public void arc(double x, double y, double width, double height, double start, double stop) {
        double degStart = -degrees(start);
        double degStop = -degrees(stop);
        degStop -= degStart;

        double nx = AppState.offsetW() + x - width/2;
        double ny = AppState.offsetH() + y - height/2;

        if (hasFill) gc.fillArc(nx, ny, width, height, degStart, degStop, ArcType.ROUND);
        if (hasStroke) gc.strokeArc(nx, ny, width, height, degStart, degStop, ArcType.OPEN);
    }

    public void arc(double x, double y, double width, double height, double start, double stop, int mode) {
        clamp(mode, OPEN, PIE);
        ArcType arcMode = ArcType.OPEN;
        switch (mode) {
            case OPEN: { arcMode = ArcType.OPEN; break; }
            case CHORD: { arcMode = ArcType.CHORD; break; }
            case PIE: { arcMode = ArcType.ROUND; break; }
        }
        double degStart = -degrees(start);
        double degStop = -degrees(stop);
        degStop -= degStart;

        double nx = AppState.offsetW() + x - width/2;
        double ny = AppState.offsetH() + y - height/2;

        if (hasFill) gc.fillArc(nx, ny, width, height, degStart, degStop, arcMode);
        if (hasStroke) gc.strokeArc(nx, ny, width, height, degStart, degStop, arcMode);
    }

    public void circle(double x, double y, double s) {
        ellipse(x, y, s, s);
    }

    public void ellipse(double x, double y, double w, double h) {

        double nx = x;
        double ny = y;
        double nw = w;
        double nh = h;

        switch (ellipseMode) {
            case CORNER: {
                nw *= 2;
                nh *= 2;
                break;
            }
            case RADIUS: {
                nw *= 2;
                nh *= 2;
                nx -= nw / 2;
                ny -= nh / 2;
                break;
            }
            case CENTER: {
                nx -= nw / 2;
                ny -= nh / 2;
                break;
            }
        }

        nx += AppState.offsetW();
        ny += AppState.offsetH();

        if (hasFill) gc.fillOval(nx, ny, nw, nh);
        if (hasStroke) gc.strokeOval(nx, ny, nw, nh);
    }

    public void line(double startX, double startY, double endX, double endY) {
        double sx = AppState.offsetW() + startX;
        double sy = AppState.offsetH() + startY;
        double ex = AppState.offsetW() + endX;
        double ey = AppState.offsetH() + endY;

        gc.strokeLine(sx, sy, ex, ey);
    }

    public void point(double x, double y) {
        rect(x, y, 1, 1);
    }

    public void quad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double[] xPoints = {x1, x2, x3, x4};
        for (int i = 0; i < 4; i++) xPoints[i] += AppState.offsetW();
        double[] yPoints = {y1, y2, y3, y4};
        for (int i = 0; i < 4; i++) yPoints[i] += AppState.offsetH();
        if (hasFill) gc.fillPolygon(xPoints, yPoints, 4);
        if (hasStroke) gc.strokePolygon(xPoints, yPoints, 4);
    }

    public void rect(double x, double y, double w, double h) {

        double nx = x;
        double ny = y;
        double nw = w;
        double nh = h;

        switch (rectMode) {
            case CORNERS: {
                nw /= 2;
                nh /= 2;
                break;
            }
            case RADIUS: {
                nw *= 2;
                nh *= 2;
                nx -= nw / 2;
                ny -= nh / 2;
                break;
            }
            case CENTER: {
                nx -= nw / 2;
                ny -= nh / 2;
            }
        }

        nx += AppState.offsetW();
        ny += AppState.offsetH();

        if (hasFill) gc.fillRect(nx, ny, nw, nh);
        if (hasStroke) gc.strokeRect(nx, ny, nw, nh);
    }

    public void square(double x, double y, double extent) {
        rect(x, y, extent, extent);
    }

    public void triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        double[] xPoints = {x1, x2, x3};
        for (int i = 0; i < 3; i++) xPoints[i] += AppState.offsetW();
        double[] yPoints = {y1, y2, y3};
        for (int i = 0; i < 3; i++) yPoints[i] += AppState.offsetH();
        if (hasFill) gc.fillPolygon(xPoints, yPoints, 3);
        if (hasStroke) gc.strokePolygon(xPoints, yPoints, 3);
    }
    /////////////////////
    // Shape // Curves //
    /////////////////////

    public void bezier(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        PVector centre = centre(x1, y1, x2, y2, x3, y3, x4, y4);
        PVector top = centre(x1, y1, x2, y2);
        PVector btm = centre(x3, y3, x4, y4);
        gc.save();
        gc.beginPath();
        gc.moveTo(AppState.offsetW() + x1, AppState.offsetH() + y1);
        gc.bezierCurveTo(AppState.offsetW() + x2, AppState.offsetH() + y2, AppState.offsetW() + x3, AppState.offsetH() + y3, AppState.offsetW() + x4, AppState.offsetH() + y4);
        gc.stroke();
        gc.restore();
    }

    public void bezier(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
        Main.throw3DError();
    }

    public void bezierDetail(int detail) {}

    public double bezierPoint(double a, double b, double c, double d, double t) {
        double t1 = 1.0 - t;
        return (a * t1 + 3 * b * t) * t1 * t1 + (3 * c * t1 + d * t) * t * t;
    }

    public double bezierTangent(double a, double b, double c, double d, double t) {
        return (3*t*t * (-a+3*b-3*c+d) + 6*t * (a-2*b+c) + 3 * (-a+b));
    }

    public void curve(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {}

    public void curve(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
        Main.throw3DError();
    }

    public void curveDetail(int detail) {}

    public double curvePoint(double a, double b, double c, double d, double t) {
        return 0;
    }

    public double curveTangent(double a, double b, double c, double d, double t) {
        return 0;
    }

    public void curveTightness(float tightness) {}

    ////////////////////////////
    // Shape // 3D Primitives //
    ////////////////////////////

    public void box(double size) {}

    public void box(double w, double h, double d) {}

    public void sphereDetail(double res) {}

    public void sphereDetail(double ures, double vres) {}

    public void sphere(double r) {}

    /////////////////////////
    // Shape // Attributes //
    /////////////////////////

    public void ellipseMode(int mode) {
        ellipseMode = mode;
    }

    public void rectMode(int mode) {
        rectMode = mode;
    }

    public void strokeCap(int type) {
        StrokeLineCap cap;
        switch (type) {
            case ROUND:
                cap = StrokeLineCap.ROUND;
                break;
            case SQUARE:
                cap = StrokeLineCap.BUTT;
                break;
            default:
                cap = StrokeLineCap.SQUARE;
                break;
        }
        gc.setLineCap(cap);
    }

    public void strokeJoin(int type) {
        StrokeLineJoin join;
        switch (type) {
            case BEVEL:
                join = StrokeLineJoin.BEVEL;
                break;
            case ROUND:
                join = StrokeLineJoin.ROUND;
                break;
            default:
                join = StrokeLineJoin.MITER;
                break;
        }
        gc.setLineJoin(join);
    }

    public void strokeWeight(double weight) {
        gc.setLineWidth(weight);
    }

    ///////////
    // Input //
    ///////////

    ////////////////////
    // Input // Mouse //
    ////////////////////

    public int mouseButton;

    public void mouseClicked() {}

    public void mouseDragged() {}

    public void mousedMoved() {}

    public void mousePressed() {}

    public boolean mousePressed;

    public void mouseReleased() {}

    public void mouseWheel(utils.MouseEvent event) {}

    public double mouseX;

    public double mouseY;

    public double pmouseX;

    public double pmouseY;

    ///////////////////////
    // Input // Keyboard //
    ///////////////////////

    public char key;

    public int keyCode;

    public void keyPressed() {}

    public boolean keyPressed;

    public void keyReleased() {}

    public void keyTyped() {}

    ////////////////////
    // Input // Files //
    ////////////////////

    //////////////////////////
    // Input // Time & Date //
    //////////////////////////

    public int day() {
        return date.get(Calendar.DAY_OF_MONTH);
    }

    public int hour() {
        return date.get(Calendar.HOUR_OF_DAY);
    }

    public int millis() {
        return date.get(Calendar.MILLISECOND);
    }

    public int minute() {
        return date.get(Calendar.MINUTE);
    }

    public int month() {
        return date.get(Calendar.MONTH) + 1;
    }

    public int second() {
        return date.get(Calendar.SECOND);
    }

    public int year() {
        return date.get(Calendar.YEAR);
    }

    ////////////
    // Output //
    ////////////

    /////////////////////
    // Output // Image //
    /////////////////////

    public void saveFrame() {}

    public void saveFrame(String fileName) {}

    ///////////////
    // Transform //
    ///////////////

    private final int MATRIX_STACK_DEPTH = 32;
    private int transformCount;
    private Affine[] transformStack = new Affine[MATRIX_STACK_DEPTH];

    private double currentRotationY = 0;
    private double currentScaleX = 1;
    private double currentScaleY = 1;
    private double currentShearX = 0;
    private double currentShearY = 0;
    private double currentTranslateX = 0;
    private double currentTranslateY = 0;
    
    public void applyMatrix(PMatrix2D source)	{
        gc.transform(source.m00, source.m10, source.m01, source.m11, source.m02, source.m12);
    }

    public void applyMatrix(double n00, double n01, double n02, double n10, double n11, double n12)	{
        gc.transform(n00, n10, n01, n11, n02, n12);
    }

    public void applyMatrix(double n00, double n01, double n02, double n03, double n10, double n11, double n12, double n13, double n20, double n21, double n22, double n23, double n30, double n31, double n32, double n33) {
        Main.throw3DError();
    }

    public PMatrix2D getMatrix() {
        PMatrix2D output;
        Affine t = gc.getTransform();
        output = new PMatrix2D(t.getMxx(), t.getMxy(), t.getTx(), t.getMyx(), t.getMyy(), t.getTy());
        return output;
    }

    public void popMatrix() {
        if (transformCount == 0) {
            throw new RuntimeException("popMatrix() needs corresponding pushMatrix() statement");
        } else {
            transformCount--;
            gc.setTransform(transformStack[transformCount]);
        }
    }

    public void printMatrix() {
        System.out.println(getMatrix());
    }

    public void pushMatrix() {
        if (transformCount == transformStack.length) {
            throw new RuntimeException("StackOverflow: Reached the maximum amount of pushed matrixes");
        } else {
            transformStack[transformCount] = gc.getTransform(transformStack[transformCount]);
            transformCount++;
        }
    }

    public void resetMatrix() {
        gc.setTransform(new Affine());
    }

    public void rotate(double radians) {
        currentRotationY += radians;
        double sideA = AppState.offsetW();
        double sideB = AppState.offsetH();
        double sideC = sqrt(sq(sideA)+sq(sideB));
        double anglB = degrees(acos((sq(sideA)+sq(sideC)-sq(sideB))/(2*sideA*sideC)));
        double finalAmt = degrees(radians) + anglB;
        double rtsdA = sideC*sin(radians(finalAmt));
        double rtsdB = sideC*cos(radians(finalAmt));
        double diffX = sideA - rtsdB;
        double diffY = sideB - rtsdA;
        gc.translate(diffX, diffY);
        gc.rotate(degrees(radians));
    }

    public void rotateX(double amount) { Main.throw3DError(); }

    public void rotateY(double amount) { Main.throw3DError(); }

    public void rotateZ(double amount) { Main.throw3DError(); }

    public void scale(double amount) {
        scale(amount, amount);
    }

    public void scale(double amountX, double amountY) {
        currentScaleX *= amountX;
        currentScaleY *= amountX;
        gc.scale(amountX, amountY);
    }

    public void scale(double amountX, double amountY, double amountZ) { Main.throw3DError(); }

    public void shearX(PGraphics pg, double amount) {
        currentShearX += amount;
        Affine temp = new Affine();
        temp.appendShear(Math.tan(amount), 0);
        gc.transform(temp);
    }

    public void shearY(PGraphics pg, double amount) {
        currentShearY += amount;
        Affine temp = new Affine();
        temp.appendShear(0, Math.tan(amount));
        gc.transform(temp);
    }

    public void translate(double amountX, double amountY) {
        currentTranslateX += amountX;
        currentTranslateY += amountX;
        gc.translate(amountX, amountY);
    }

    ////////////////////
    // Lights, Camera //
    ////////////////////

    //////////////////////////////
    // Lights, Camera // Lights //
    //////////////////////////////

    public void ambientLight(double v1, double v2, double v3) {}

    public void ambientLight(double v1, double v2, double v3, double x, double y, double z) {}
    
    public void directionalLight(double v1, double v2, double v3, double nx, double ny, double nz) {}
    
    public void lightFalloff(double constant, double linear, double quadratic) {}
    
    public void lights() {}
    
    public void lightSpecular(double v1, double v2, double v3) {}
    
    public void noLights() {}
    
    public void normal(double v1, double v2, double v3) {}
    
    public void pointLight(double v1, double v2, double v3, double x, double y, double z) {}
    
    public void spotLight(double v1, double v2, double v3, double x, double y, double z, double nx, double ny, double nz, double angle, double concerntration) {}

    //////////////////////////////
    // Lights, Camera // Camera //
    //////////////////////////////
    
    public void beginCamera() {}
    
    public void camera() {}

    public void camera(double eyeX, double eyeY, double eyeZ, double centerX, double centerY, double centerZ, double upX, double upY, double upZ) {}
    
    public void endCamera() {}
    
    public void frustum(double left, double right, double bottom, double top, double near, double far) {}
    
    public void ortho() {}
    
    public void ortho(double left, double right, double bottom, double top) {}
    
    public void ortho(double left, double right, double bottom, double top, double near, double far) {}
    
    public void perspective() {}
    
    public void perspective(double fovy, double aspect, double zNear, double zFar) {}
    
    public void printCamera() {}
    
    public void printProjection() {}

    ///////////////////////////////////
    // Lights, Camera // Coordinates //
    ///////////////////////////////////
    
    public double modelX(double x, double y, double z) { return 0; }
    
    public double modelY(double x, double y, double z) { return 0; }
    
    public double modelZ(double x, double y, double z) { return 0; }
    
    public double screenX(double x, double y) { return 0; }
    
    public double screenX(double x, double y, double z) { return 0; }
    
    public double screenY(double x, double y) { return 0; }
    
    public double screenY(double x, double y, double z) { return 0; }
    
    public double screenZ(double x, double y) { return 0; }
    
    public double screenZ(double x, double y, double z) { return 0; }

    ///////////////////////////////////////////
    // Lights, Camera // Material Properties //
    ///////////////////////////////////////////
    
    public void ambient(int rgb) {}
    
    public void ambient(double gray) {}
    
    public void ambient(double v1, double v2, double v3) {}
    
    public void emissive(int rgb) {}
    
    public void emissive(double gray) {}
    
    public void emissive(double v1, double v2, double v3) {}
    
    public void shininess(double shine) {}
    
    public void specular(int rgb) {}
    
    public void specular(double gray) {}
    
    public void specular(double v1, double v2, double v3) {}

    ///////////
    // Color //
    ///////////

    private double maxRH;
    private double maxGS;
    private double maxBB;
    private double maxAO;

    //////////////////////
    // Color // Setting //
    //////////////////////

    public void background(double gray) {
        if (between(gray, 0, maxRH)) {
            background(gray, gray, gray, maxAO);
        } else {
            setBackground((int) gray);
        }
    }

    public void background(double gray, double alpha) {
        background(gray, gray, gray, alpha);
    }

    public void background(double rh, double gs, double bv) {
        background(rh, gs, bv, maxAO);
    }

    public void background(double rh, double gs, double bv, double ao) {
        double mappedRH = clamp(map(rh, 0, maxRH, 0, 255), 0, 255);
        double mappedGS = clamp(map(gs, 0, maxGS, 0, 255), 0, 255);
        double mappedBV = clamp(map(bv, 0, maxBB, 0, 255), 0, 255);
        double mappedAO = clamp(map(ao, 0, maxAO, 0, 255), 0, 255);
        setBackground(Colours.encodeColour(mappedRH, mappedGS, mappedBV, mappedAO));
    }

    private void setBackground(int encodedValue) {
        pushMatrix();
        resetMatrix();
        gc.save();
        gc.setFill(Colours.decodeColour(colorMode, encodedValue));
        gc.fillRect(AppState.offsetW(), AppState.offsetH(), width, height);
        gc.restore();
        popMatrix();
    }

    public void colorMode(int mode) {
        colorMode(mode, (int)maxRH, (int)maxGS, (int)maxBB, maxAO);
    }

    public void colorMode(int mode, double max) {
        colorMode(mode, max, max, max, maxAO);
    }

    public void colorMode(int mode, double rh, double gs, double bb) {
        colorMode(mode, rh, gs, bb, maxAO);
    }

    public void colorMode(int mode, double rh, double gs, double bb, double alpha) {
        colorMode = mode;
        maxRH = rh;
        maxGS = gs;
        maxBB = bb;
        maxAO = alpha;
    }

    public void fill(double gray) {
        if (between(gray, 0, maxRH)) {
            fill(gray, gray, gray, maxAO);
        } else {
            setFill((int) gray);
        }
    }

    public void fill(double gray, double alpha) {
        fill(gray, gray, gray, alpha);
    }

    public void fill(double rh, double gs, double bv) {
        fill(rh, gs, bv, maxAO);
    }

    public void fill(double rh, double gs, double bv, double ao) {
        hasFill = true;
        double mappedRH = clamp(map(rh, 0, maxRH, 0, 255), 0, 255);
        double mappedGS = clamp(map(gs, 0, maxGS, 0, 255), 0, 255);
        double mappedBV = clamp(map(bv, 0, maxBB, 0, 255), 0, 255);
        double mappedAO = clamp(map(ao, 0, maxAO, 0, 255), 0, 255);
        setFill(Colours.encodeColour(mappedRH, mappedGS, mappedBV, mappedAO));
    }

    private void setFill(int encodedValue) {
        gc.setFill(Colours.decodeColour(colorMode, encodedValue));
    }

    public void noFill() {
        hasFill = false;
    }

    public void noStroke() {
        hasStroke = false;
    }

    public void stroke(double gray) {
        if (between(gray, 0, maxRH)) {
            stroke(gray, gray, gray, maxAO);
        } else {
            setStroke((int) gray);
        }
    }

    public void stroke(double gray, double alpha) {
        stroke(gray, gray, gray, alpha);
    }

    public void stroke(double rh, double gs, double bv) {
        stroke(rh, gs, bv, maxAO);
    }

    public void stroke(double rh, double gs, double bv, double ao) {
        hasStroke = true;
        double mappedRH = clamp(map(rh, 0, maxRH, 0, 255), 0, 255);
        double mappedGS = clamp(map(gs, 0, maxGS, 0, 255), 0, 255);
        double mappedBV = clamp(map(bv, 0, maxBB, 0, 255), 0, 255);
        double mappedAO = clamp(map(ao, 0, maxAO, 0, 255), 0, 255);
        setStroke(Colours.encodeColour(mappedRH, mappedGS, mappedBV, mappedAO));
    }

    private void setStroke(int encodedValue) {
        gc.setStroke(Colours.decodeColour(colorMode, encodedValue));
    }

    public int color(double gray) {
        if (between(gray, 0, maxRH)) {
            color(gray, gray, gray, maxAO);
        } else {
            return (int) gray;
        }

        return color(gray, gray, gray, maxAO);
    }

    public int color(double gray, double alpha) {
        return color(gray, gray, gray, alpha);
    }

    public int color(double rh, double gs, double bv) {
        return color(rh, gs, bv, maxAO);
    }

    public int color(double rh, double gs, double bv, double ao) {
        double mappedRH = clamp(map(rh, 0, maxRH, 0, 255), 0, 255);
        double mappedGS = clamp(map(gs, 0, maxGS, 0, 255), 0, 255);
        double mappedBV = clamp(map(bv, 0, maxBB, 0, 255), 0, 255);
        double mappedAO = clamp(map(ao, 0, maxAO, 0, 255), 0, 255);
        return Colours.encodeColour(mappedRH, mappedGS, mappedBV, mappedAO);
    }

    ///////////
    // Image //
    ///////////

    public void image(PGraphics pg, double x, double y) {
        gc.save();
        gc.beginPath();
        gc.rect(AppState.offsetW() + x, AppState.offsetH() + y, pg.width, pg.height);
        gc.clip();
        pg.render(x, y);
        gc.restore();
    }

    public void image(PGraphics pg, double x, double y, double w, double h) {
        gc.save();
        gc.beginPath();
        gc.rect(AppState.offsetW() + x, AppState.offsetH() + y, w, h);
        gc.clip();
        pg.render(x, y, w, h);
        gc.restore();
    }

    ///////////////
    // Rendering //
    ///////////////

    // blendMode()

    // clip()

    // createGraphics()

    public PGraphics createGraphics(int w, int h) {
        return new PGraphics(w, h);
    }

    // hint()

    // noClip()

    ////////////////
    // Typography //
    ////////////////

    ////////////////////////////////////////
    // Typography // Loading & Displaying //
    ////////////////////////////////////////

    private TextAlignment alignH = TextAlignment.LEFT;
    private VPos alignV = VPos.BASELINE;
    private Text textCache = new Text("");

    // createFont()

    public PFont createFont(String name, double size) {
        return null;
    }

    public PFont createFont(String name, double size, boolean smooth) {
        return null;
    }
    
    public PFont createFont(String name, double size, boolean smooth, char[] charset) {
        return null;
    }

    // loadFont()

    public PFont loadFont(String filename) {
        return null;
    }

    // text()

    public void text(char c, double x, double y) {
        text(Character.toString(c), x, y);
    }
    
    public void text(char c, double x, double y, double z) { Main.throw3DError(); }
    
    public void text(String str, double x, double y) {
        gc.fillText(str, AppState.offsetW() + x, AppState.offsetH() + y);
    }
    
    public void text(char[] chars, int start, int stop, double x, double y) {
        text(new String(chars), x, y);
    }
    
    public void text(String str, double x, double y, double z) { Main.throw3DError(); }
    
    public void text(char[] chars, int start, int stop, double x, double y, double z) { Main.throw3DError(); }
    
    public void text(String str, double x1, double y1, double x2, double y2) {
        
    }
    
    public void text(int num, double x, double y) {
        text(Integer.toString(num), x, y);
    }
    
    public void text(int num, double x, double y, double z) { Main.throw3DError(); }
    
    public void text(double num, double x, double y) {
        text(Double.toString(num), x, y);
    }
    
    public void text(double num, double x, double y, double z) { Main.throw3DError(); }

    public void textFont(PFont which) {}

    public void textFont(PFont which, double size) {}

    //////////////////////////////
    // Typography // Attributes //
    //////////////////////////////

    public void textAlign(int alignX) {

        switch (alignX) {
            case LEFT: {
                alignH = TextAlignment.LEFT;
                break;
            }
            case CENTER: {
                alignH = TextAlignment.CENTER;
                break;
            }
            case RIGHT: {
                alignH = TextAlignment.RIGHT;
                break;
            }
        }

        setTextAlign(alignH, alignV);
    }

    public void textAlign(int alignX, int alignY) {
        textAlign(alignX);

        switch (alignY) {
            case TOP: {
                alignV = VPos.TOP;
                break;
            }
            case BOTTOM: {
                alignV = VPos.BOTTOM;
                break;
            }
            case CENTER: {
                alignV = VPos.CENTER;
                break;
            }
            case BASELINE: {
                alignV = VPos.BASELINE;
                break;
            }
        }

        setTextAlign(alignH, alignV);
    }

    public void textLeading(double leading) {}

    public void textMode(int mode) {
        
    }

    public void textSize(double size) {
        textCache.setFont(new Font(size));
        gc.setFont(new Font(size));
    }

    public double textWidth(char c) {
        return textWidth(Character.toString(c));
    }

    public double textWidth(String str) {
        textCache.setText(str);
        return textCache.getLayoutBounds().getWidth();
    }

    ///////////////////////////
    // Typography // Metrics //
    ///////////////////////////

    public double textAscent() {
        return 0;
    }

    public double textDescent() {
        return 0;
    }

    //////////////////////
    // Internal Methods //
    //////////////////////

    private void method(String name) {
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


































































































    /////////////////////
    // Shape // Vertex //
    /////////////////////

    int shape;
    final int DEFAULT_VERTICES = 512;
    final int VERTEX_FIELD_COUNT = 37;
    double[][] vertices = new double[DEFAULT_VERTICES][VERTEX_FIELD_COUNT];
    int vertexCount;
    boolean openContour;
    boolean adjustedForThinLines;
    /// break the shape at the next vertex (next vertex() call is a moveto())
    boolean breakShape;
    float[] pathCoordsBuffer = new float[6];
    Path2D workPath = new Path2D();
    Path2D auxPath = new Path2D();
    double[][] curveVertices;
    int curveVertexCount;

    public void beginContour() {
        if (openContour) {
            System.out.println("Already called beginContour()");
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

    public void beginShape(int kind) {
        shape = kind;
        vertexCount = 0;
        curveVertexCount = 0;

        workPath.reset();
        auxPath.reset();
    }

    public void beginShape() {
        beginShape(POLYGON);
    }

    public void endContour() {
        if (!openContour) {
            System.out.println("Need to call beginContour() first");
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
            System.out.println("Missing endContour() before endShape()");
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
    }

    private float[] fpathCoordsBuffer = new float[pathCoordsBuffer.length];

    private void copyCoordsBuffer() {
        for (int i = 0; i < pathCoordsBuffer.length; i++) {
            fpathCoordsBuffer[i] = (float) pathCoordsBuffer[i];
        }
    }

    private void drawShape(Shape s) {
        gc.beginPath();
        PathIterator pi = s.getPathIterator(null);
        copyCoordsBuffer();
        while (!pi.isDone()) {

            int pitype = pi.currentSegment(fpathCoordsBuffer);
            switch (pitype) {
                case PathIterator.SEG_MOVETO:
                    gc.moveTo(pathCoordsBuffer[0], pathCoordsBuffer[1]);
                    break;
                case PathIterator.SEG_LINETO:
                    gc.lineTo(pathCoordsBuffer[0], pathCoordsBuffer[1]);
                    break;
                case PathIterator.SEG_QUADTO:
                    gc.quadraticCurveTo(pathCoordsBuffer[0], pathCoordsBuffer[1], pathCoordsBuffer[2],
                            pathCoordsBuffer[3]);
                    break;
                case PathIterator.SEG_CUBICTO:
                    gc.bezierCurveTo(pathCoordsBuffer[0], pathCoordsBuffer[1], pathCoordsBuffer[2],
                            pathCoordsBuffer[3], pathCoordsBuffer[4], pathCoordsBuffer[5]);
                    break;
                case PathIterator.SEG_CLOSE:
                    gc.closePath();
                    break;
                default:
                    System.out.println("Unknown segment type " + pitype);
            }
            pi.next();
        }
        if (hasFill) gc.fill();
        if (hasStroke) gc.stroke();
    }

    public void vertexImpl(double x, double y) {
        //vertexImpl(x + AppState.offsetW(), y + AppState.offsetH());
    }

    public void vertex(double x, double y) {
        if (vertexCount == vertices.length) {
          double[][] temp = new double[vertexCount<<1][VERTEX_FIELD_COUNT];
          System.arraycopy(vertices, 0, temp, 0, vertexCount);
          vertices = temp;
        }

        vertices[vertexCount][X] = x;
        vertices[vertexCount][Y] = y;
        vertexCount++;

        switch (shape) {
            case POINTS: {
                point(x, y);
                break;
            }

            case LINES: {
                if ((vertexCount % 2) == 0) {
                    line(vertices[vertexCount-2][X],
                        vertices[vertexCount-2][Y], x, y);
                }
                break;
            }

            case TRIANGLES: {
                if ((vertexCount % 3) == 0) {
                    triangle(vertices[vertexCount - 3][X],
                        vertices[vertexCount - 3][Y],
                        vertices[vertexCount - 2][X],
                        vertices[vertexCount - 2][Y],
                        x, y);
                }
                break;
            }

            case TRIANGLE_STRIP: {
                if (vertexCount >= 3) {
                    triangle(vertices[vertexCount - 2][X],
                        vertices[vertexCount - 2][Y],
                        vertices[vertexCount - 1][X],
                        vertices[vertexCount - 1][Y],
                        vertices[vertexCount - 3][X],
                        vertices[vertexCount - 3][Y]);
                }
                break;
            }

            case TRIANGLE_FAN: {
                if (vertexCount >= 3) {
                    triangle(vertices[0][X],
                        vertices[0][Y],
                        vertices[vertexCount - 2][X],
                        vertices[vertexCount - 2][Y],
                        x, y);
                }
                break;
            }

            case QUAD: {
                break;
            }

            case QUADS: {
                if ((vertexCount % 4) == 0) {
                    quad(vertices[vertexCount - 4][X],
                        vertices[vertexCount - 4][Y],
                        vertices[vertexCount - 3][X],
                        vertices[vertexCount - 3][Y],
                        vertices[vertexCount - 2][X],
                        vertices[vertexCount - 2][Y],
                        x, y);
                }
                break;
            }

            case QUAD_STRIP: {
                if ((vertexCount >= 4) && ((vertexCount % 2) == 0)) {
                    quad(vertices[vertexCount - 4][X],
                        vertices[vertexCount - 4][Y],
                        vertices[vertexCount - 2][X],
                        vertices[vertexCount - 2][Y],
                        x, y,
                        vertices[vertexCount - 3][X],
                        vertices[vertexCount - 3][Y]);
                }
                break;
            }

            case POLYGON: {
                if (workPath.getNumCommands() == 0 || breakShape) {
                    workPath.moveTo((float) x, (float) y);
                    breakShape = false;
                } else {
                    workPath.lineTo((float) x, (float) y);
                }
                break;
            }
        }
    }


    ////////////
    // Pixels //
    ////////////

    public int get(double x, double y) {
        WritableImage snap = gc.getCanvas().snapshot(null, null);
        snap.getPixelReader().getColor((int)x, (int)y);
        return 0;
    }

    //////////////////////
    // Internal Methods //
    //////////////////////

    private void render() {
        pushMatrix();
        draw();
        popMatrix();
        coverEdges();
        if (!mouseOnSketch) {
            surface.setCursor(ARROW);
        }
    }

    private void setTextAlign(TextAlignment alignH, VPos alignV) {
        gc.setTextAlign(alignH);
        gc.setTextBaseline(alignV);
        textCache.setTextAlignment(alignH);
        textCache.setTextOrigin(alignV);
    }

    private void updateTime() {
        date.setTimeInMillis(System.currentTimeMillis());
    }

    private void coverEdges() {
        pushMatrix();
        resetMatrix();
        gc.save();
        gc.setFill(Color.gray(0.08));
        gc.fillRect(0, 0, AppState.screenW(), AppState.offsetH());                            // Top
        gc.fillRect(0, AppState.offsetH() + height, AppState.screenW(), AppState.offsetH()); // Bottom
        gc.fillRect(0, 0, AppState.offsetW(), AppState.screenH());                            // Left
        gc.fillRect(AppState.offsetW() + width, 0, AppState.offsetW(), AppState.screenH());  // Right
        gc.restore();
        popMatrix();
    }

    private void updateMouse(javafx.scene.input.MouseEvent event) {
        updateMouseButton(event);
        updateMousePos(event);
    }

    private void updateMouseButton(javafx.scene.input.MouseEvent event) {
        switch(event.getButton()) {
            case MIDDLE: {
                mouseButton = CENTER;
                break;
            }

            case PRIMARY: {
                mouseButton = LEFT;
                break;
            }

            case SECONDARY: {
                mouseButton = RIGHT;
                break;
            }

            default: {
                mouseButton = 0;
                break;
            }
        }
    }

    boolean mouseOnSketch;

    private void updateMousePos(javafx.scene.input.MouseEvent event) {
        if (inside(event.getSceneX(), event.getSceneY(), AppState.offsetW(), AppState.offsetH(), width, height)) {
            mouseOnSketch = true;
            pmouseX = mouseX;
            pmouseY = mouseY;

            mouseX = Maths.clamp((int)(event.getSceneX()-AppState.offsetW()), 0, width);
            mouseY = Maths.clamp((int)(event.getSceneY()-AppState.offsetH()), 0, height);
        } else {
            mouseOnSketch = false;
        }
    }

    private void updateKeys(KeyEvent event) {
        keyCode = event.getCode().ordinal();
        if (event.getCode().isDigitKey() || event.getCode().isLetterKey()) {
            key = event.getCharacter().charAt(0);
        } else {
            if (event.getCode().equals(KeyCode.ENTER)) key = '\n';
            else if (event.getCode().equals(KeyCode.SPACE)) key = ' ';
            else if (event.getCode().equals(KeyCode.TAB)) key = '\t';
        }
    }

    /////////////////////////////
    // Handled Default Methods //
    /////////////////////////////

    private ArrayList<Integer> pressedKeys = new ArrayList<>(20);
    private ArrayList<MouseButton> pressedMouseBtns = new ArrayList<>(3);

    public void handleSettings() {
        settings();
    }

    public void handleSetup() {
        setup();
        render();
    }

    public void handleDraw() {
        updateTime();

        //if (Main.scaled) {
        //    double scaleAmount = min(AppState.screenW() / width, AppState.screenH() / height);
        //    scale(scaleAmount);
        //}

        if (looping) {
            render();
        }

        frameCount++;
    }

    public void handleMouseClicked(javafx.scene.input.MouseEvent event) {
        updateMouse(event);
        mouseClicked();
    }

    public void handleMouseDragged(javafx.scene.input.MouseEvent event) {
        updateMousePos(event);
        if (!mousePressed) {
            updateMouseButton(event);
        }
        mouseDragged();
    }

    public void handleMouseMoved(javafx.scene.input.MouseEvent event) {
        updateMouse(event);
        mousedMoved();
    }

    public void handleMousePressed(javafx.scene.input.MouseEvent event) {
        updateMouse(event);
        if (!pressedMouseBtns.contains(event.getButton())) pressedMouseBtns.add(event.getButton());
        mousePressed = true;
        mousePressed();
    }

    public void handleMouseReleased(javafx.scene.input.MouseEvent event) {
        updateMouse(event);
        pressedMouseBtns.remove(event.getButton());
        mousePressed = !pressedMouseBtns.isEmpty();
        mouseReleased();
    }

    public void handleMouseWheel(javafx.scene.input.ScrollEvent scrollEvent) {
        int count = (int) -(scrollEvent.getDeltaY() / scrollEvent.getMultiplierY());
        utils.MouseEvent event = new utils.MouseEvent(count);
        mouseWheel(event);
    }

    public void handleKeyPressed(KeyEvent event) {
        updateKeys(event);
        if (!pressedKeys.contains(keyCode)) pressedKeys.add(keyCode);
        keyPressed = true;
        keyPressed();
    }

    public void handleKeyReleased(KeyEvent event) {
        updateKeys(event);
        pressedKeys.remove(keyCode);
        keyPressed = !pressedKeys.isEmpty();
        keyReleased();
    }

    public void handleKeyTyped(KeyEvent event) {
        updateKeys(event);
        keyTyped();
    }

    public static void setState(StyleState ss) {
        Main.gc.setFill(ss.fill);
        Main.gc.setStroke(ss.stroke);
        Main.gc.setLineWidth(ss.linewidth);
        Main.gc.setLineCap(ss.linecap);
        Main.gc.setLineJoin(ss.linejoin);
        Main.gc.setMiterLimit(ss.miterlimit);
        Main.gc.setLineDashes(ss.dashes);
        Main.gc.setLineDashOffset(ss.dashOffset);
        Main.gc.setFont(ss.font);
        Main.gc.setTextAlign(ss.textalign);
        Main.gc.setTextBaseline(ss.textbaseline);
    }
}