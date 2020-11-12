package utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import main.FXApp;

import static utils.PConstants.*;
import static utils.MathUtils.*;

public class PGraphics {

    GraphicsContext gc;
    FXApp parent;

	public PGraphics(GraphicsContext gc, FXApp parent) {
        this.gc = gc;
        this.parent = parent;
    }
    
    // [Color | Setting]

    int colourMode;
    double maxRH;
    double maxGS;
    double maxBB;
    double maxAL;

    boolean hasFill;
    boolean hasStroke;

    // background()

    public void background(int gray) {
        background(gray, gray, gray, 255);
    }

    public void background(int gray, int alpha) {
        background(gray, gray, gray, alpha);
    }
    
    public void background(int v1, int v2, int v3) {
        background(v1, v2, v3, 255);
    }
    
    public void background(int v1, int v2, int v3, int alpha) {
        gc.save();
        fill(v1, v2, v3, alpha);
        gc.fillRect(offsetX,offsetY,width,height);
        gc.restore();
    }

    // colorMode()

    public void colorMode(int mode) {
        colorMode(mode, 255, 255, 255, 255);
    }

    public void colorMode(int mode, double max) {
        colorMode(mode, max, max, max, 255);
    }

    public void colorMode(int mode, double max1, double max2, double max3) {
        colorMode(mode, max1, max2, max3, 255);
    }
    
    public void colorMode(int mode, double max1, double max2, double max3, double maxA) {
        colourMode = mode;
        maxRH = max1;
        maxGS = max2;
        maxBB = max3;
        maxAL = maxA;
    }

    // fill()

    public void fill(int gray) {
        fill(gray, gray, gray, 255);
    }

    public void fill(int gray, int alpha) {
        fill(gray, gray, gray, alpha);
    }
    
    public void fill(int v1, int v2, int v3) {
        fill(v1, v2, v3, 255);
    }
    
    public void fill(int v1, int v2, int v3, int alpha) {
        hasFill = true;
        gc.setFill(getColor(v1, v2, v3, alpha));
    }

    // noFill()

    public void noFill() {
        hasFill = false;
    }

    // noStroke()

    public void noStroke() {
        hasStroke = false;
    }

    // stroke()

    public void stroke(int gray) {
        stroke(gray, gray, gray, 255);
    }

    public void stroke(int gray, int alpha) {
        stroke(gray, gray, gray, alpha);
    }
    
    public void stroke(int v1, int v2, int v3) {
        stroke(v1, v2, v3, 255);
    }
    
    public void stroke(int v1, int v2, int v3, int alpha) {
        hasStroke = true;
        gc.setStroke(getColor(v1, v2, v3, alpha));
    }

    public Color getColor(int v1, int v2, int v3, int alpha) {
        switch (colourMode) {
            case RGB: {
                int red = (int) map(v1, 0, maxRH, 0, 255);
                int green = (int) map(v2, 0, maxGS, 0, 255);
                int blue = (int) map(v3, 0, maxBB, 0, 255);
                double opacity = map(alpha, 0, maxAL, 0, 1);
                return Color.rgb(red, green, blue, opacity);
            }

            case HSB: {
                double hue = map(v1, 0, maxRH, 0, 359);
                double saturation = map(v2, 0, maxGS, 0, 1);
                double brightness = map(v3, 0, maxBB, 0, 1);
                double opacity = map(alpha, 0, maxAL, 0, 1);
                return Color.hsb(hue, saturation, brightness, opacity);
            }

            default: {
                return Color.WHITE;
            }
        }
    }
    
    // [Transform]

    final int MATRIX_STACK_DEPTH = 32;
    int transformCount;
    Affine[] transformStack = new Affine[MATRIX_STACK_DEPTH];

    public void applyMatrix() {
        // Not yet implemented
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
        // Not yet implemented
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

    public void rotate(double angle) {
        gc.rotate(degrees(angle));
    }

    public void rotateX() {
        // Not yet implemented
    }

    public void rotateY() {
        // Not yet implemented
    }

    public void rotateZ() {
        // Not yet implemented
    }

    public void scale(double s) {
        scale(s, s);
    }

    public void scale(double x, double y) {
        gc.scale(x, y);
    }

    public void shearX(double angle) {
        Affine temp = new Affine();
        temp.appendShear(tan(angle), 0);
        gc.transform(temp);

    }

    public void shearY(double angle) {
        Affine temp = new Affine();
        temp.appendShear(0, tan(angle));
        gc.transform(temp);

    }

    public void translate(double x, double y) {
        gc.translate(x, y);
    }

}