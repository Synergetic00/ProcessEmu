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
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Shear;
import ptypes.PFont;
import ptypes.PImage;
import ptypes.PShape;
import ptypes.PSurface;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import utils.Colours;
import utils.Constants;
import utils.Maths;

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
        resetSurface();
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
        looping = true;
        rectMode = CORNER;
        ellipseMode = CENTER;
    }

    // Structure

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
        gc.restore();
    }

    public void popStyle() {

    }

    public void push() {
        gc.save();
    }

    public void pushStyle() {

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
        size(Constants.screenW(), Constants.screenH());
    }

    public int height;

    public void noCursor() {

    }

    public void noSmooth() {

    }

    public void pixelDensity(int density) {

    }

    public int pixelHeight;

    public int pixelWidth;

    public void size(int width, int height) {
        this.width = width;
        this.height = height;

        Constants.offsetW((Constants.screenW() - width) / 2);
        Constants.offsetH((Constants.screenH() - height) / 2);

        background(204);
    }

    public void settings() {}

    public void smooth(int level) {

    }

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

        double nx = Constants.offsetW() + x - width/2;
        double ny = Constants.offsetH() + y - height/2;

        if (hasFill) gc.fillArc(nx, ny, width, height, degStart, degStop, ArcType.ROUND);
        if (hasStroke) gc.strokeArc(nx, ny, width, height, degStart, degStop, ArcType.OPEN);
    }

    public void arc(double x, double y, double width, double height, double start, double stop, int mode) {
        clamp(mode, OPEN, PIE);
        ArcType arcMode = ArcType.OPEN;
        switch (mode) {
            case OPEN: {
                arcMode = ArcType.OPEN;
                break;
            }
            case CHORD: {
                arcMode = ArcType.CHORD;
                break;
            }
            case PIE: {
                arcMode = ArcType.ROUND;
                break;
            }
        }
        double degStart = -degrees(start);
        double degStop = -degrees(stop);
        degStop -= degStart;

        double nx = Constants.offsetW() + x - width/2;
        double ny = Constants.offsetH() + y - height/2;

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

        nx += Constants.offsetW();
        ny += Constants.offsetH();

        if (hasFill) gc.fillOval(nx, ny, nw, nh);
        if (hasStroke) gc.strokeOval(nx, ny, nw, nh);
    }

    public void line(double startX, double startY, double endX, double endY) {
        double sx = Constants.offsetW() + startX;
        double sy = Constants.offsetH() + startY;
        double ex = Constants.offsetW() + endX;
        double ey = Constants.offsetH() + endY;

        gc.strokeLine(sx, sy, ex, ey);
    }

    public void point(double x, double y) {
        rect(x, y, 1, 1);
    }

    public void quad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double[] xPoints = {x1, x2, x3, x4};
        for (int i = 0; i < 4; i++) xPoints[i] += Constants.offsetW();
        double[] yPoints = {y1, y2, y3, y4};
        for (int i = 0; i < 4; i++) yPoints[i] += Constants.offsetH();
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

        nx += Constants.offsetW();
        ny += Constants.offsetH();

        if (hasFill) gc.fillRect(nx, ny, nw, nh);
        if (hasStroke) gc.strokeRect(nx, ny, nw, nh);
    }

    public void triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        double[] xPoints = {x1, x2, x3};
        for (int i = 0; i < 3; i++) xPoints[i] += Constants.offsetW();
        double[] yPoints = {y1, y2, y3};
        for (int i = 0; i < 3; i++) yPoints[i] += Constants.offsetH();
        if (hasFill) gc.fillPolygon(xPoints, yPoints, 3);
        if (hasStroke) gc.strokePolygon(xPoints, yPoints, 3);
    }

    /////////////////////
    // Shape // Curves //
    /////////////////////

    /////////////////////////
    // Shape // Attributes //
    /////////////////////////

    public void ellipseMode(int mode) {
        ellipseMode = mode;
    }

    public void rectMode(int mode) {
        rectMode = mode;
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

    ///////////////////////////////////
    // Shape // Loading & Displaying //
    ///////////////////////////////////

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

    public KeyCode keyCode;

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

    /////////////////////
    // Output // Image //
    /////////////////////

    ///////////////
    // Transform //
    ///////////////

    private double currentRotationY = 0;
    private double currentScaleX = 1;
    private double currentScaleY = 1;
    private double currentTranslateX = 0;
    private double currentTranslateY = 0;

	public void rotate(double radians) {
        currentRotationY += radians;

        double sideA = Constants.offsetW();
        double sideB = Constants.offsetH();
        double sideC = sqrt(sq(sideA)+sq(sideB));
        double anglB = degrees(acos((sq(sideA)+sq(sideC)-sq(sideB))/(2*sideA*sideC)));
        double finalAmt = degrees(radians) + anglB;
        double rtsdA = sideC*sin(radians(finalAmt));
        double rtsdB = sideC*cos(radians(finalAmt));
        double diffX = sideA - rtsdB;
        double diffY = sideB - rtsdA;
        gc.translate(diffX, diffY);
        gc.rotate(degrees(radians));
        //gc.translate(-diffX, -diffY);
	}

	public void scale(double amount) {
        scale(amount, amount);
	}

	public void scale(double amountX, double amountY) {
        currentScaleX *= amountX;
        currentScaleY *= amountX;

        gc.scale(amountX, amountY);
	}

    public void shearX(double amount) {
        Shear shear = Affine.shear(amount, 0);
    }

    public void shearY(double amount) {
        Shear shear = Affine.shear(0, amount);
    }

	public void translate(double amountX, double amountY) {
        currentTranslateX += amountX;
        currentTranslateY += amountX;

        gc.translate(amountX, amountY);
	}

    private final int MATRIX_STACK_DEPTH = 32;
    private int transformCount;
    private Affine[] transformStack = new Affine[MATRIX_STACK_DEPTH];

    public void pushMatrix() {
        if (transformCount == transformStack.length) {
            throw new RuntimeException("StackOverflow: Reached the maximum amount of pushed matrixes");
        } else {
            transformStack[transformCount] = gc.getTransform(transformStack[transformCount]);
            transformCount++;
        }
    }

    public void popMatrix() {
        if (transformCount == 0) {
            throw new RuntimeException("popMatrix() needs corresponding pushMatrix() statement");
        } else {
            transformCount--;
            gc.setTransform(transformStack[transformCount]);
        }
    }

	public void resetMatrix() {
        gc.setTransform(new Affine());
    }

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
        gc.fillRect(Constants.offsetW(), Constants.offsetH(), width, height);
        gc.restore();
        popMatrix();
    }

    private double maxRH;
    private double maxGS;
    private double maxBB;
    private double maxAO;

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

    // Color

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
    
    ////////////////
    // Typography //
    ////////////////

    private TextAlignment alignH = TextAlignment.LEFT;
    private VPos alignV = VPos.BASELINE;

    public PFont createFont(String name, double size) {
        return null;
    }

    public PFont createFont(String name, double size, boolean smooth) {
        return null;
    }
    
    public PFont createFont(String name, double size, boolean smooth, char[] charset) {
        return null;
    }

    public PFont loadFont(String filename) {
        return null;
    }

    public void text(String text, double x, double y) {
        gc.fillText(text, x, y);
    }

    public void textFont(PFont which) {

    }

    public void textFont(PFont which, int size) {

    }

    public void textAlign(int newAlignX) {

        switch (newAlignX) {
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

        textAlign(alignH, alignV);
    }

    public void textAlign(int newAlignX, int newAlignY) {
        textAlign(newAlignX);

        switch (newAlignY) {
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

        textAlign(alignH, alignV);
    }

    public void textAlign(TextAlignment alignH, VPos alignV) {
        gc.setTextAlign(alignH);
        gc.setTextBaseline(alignV);
    }

    public void textSize(double size) {
        gc.setFont(new Font(size));
    }




























































    private void resetSurface() {
        //surface.setLocation((Constants.displayW() - Constants.windowW()) / 2, (Constants.displayH() - Constants.windowH()) / 2);
        //surface.setTitle(Main.title);
    }












    /////////////////////////////
    // Handled Default Methods //
    /////////////////////////////

    public void updateMouse(javafx.scene.input.MouseEvent event) {
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

        if (inside(event.getSceneX(), event.getSceneY(), Constants.offsetW(), Constants.offsetH(), width, height)) {
            pmouseX = mouseX;
            pmouseY = mouseY;

            mouseX = Maths.clamp((int)(event.getSceneX()-Constants.offsetW()), 0, width);
            mouseY = Maths.clamp((int)(event.getSceneY()-Constants.offsetH()), 0, height);
        }
    }

    private ArrayList<KeyCode> pressedKeys = new ArrayList<>(20);
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
        //    double scaleAmount = min(Constants.screenW() / width, Constants.screenH() / height);
        //    scale(scaleAmount);
        //}

        if (looping) {
            render();
        }

        frameCount++;
    }

    private void render() {
        pushMatrix();
        draw();
        popMatrix();
        coverEdges();
    }

    public void handleMouseClicked(javafx.scene.input.MouseEvent event) {
        updateMouse(event);
        mouseClicked();
    }

    public void handleMouseDragged(javafx.scene.input.MouseEvent event) {
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

    private void updateKeys(KeyEvent event) {
        keyCode = event.getCode();
        key = event.getCharacter().charAt(0);
    }

    ///////////////
    // Structure //
    ///////////////

    /////////////////
    // Environment //
    /////////////////




    public void strokeWeight(double weight) {
        gc.setLineWidth(weight);
    }


    // More Stuff






















    // Other

    private void updateTime() {
        date.setTimeInMillis(System.currentTimeMillis());
    }

    private void coverEdges() {
        pushMatrix();
        resetMatrix();
        gc.save();
        gc.setFill(Color.gray(0.08));
        gc.fillRect(0, 0, Constants.screenW(), Constants.offsetH());                            // Top
        gc.fillRect(0, Constants.offsetH() + height, Constants.screenW(), Constants.offsetH()); // Bottom
        gc.fillRect(0, 0, Constants.offsetW(), Constants.screenH());                            // Left
        gc.fillRect(Constants.offsetW() + width, 0, Constants.offsetW(), Constants.screenH());  // Right
        gc.restore();
        popMatrix();
    }

}