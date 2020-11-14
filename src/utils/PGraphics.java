package utils;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import main.FXApp;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Shape;

import static utils.Constants.*;
import static utils.MathUtils.*;

import java.util.Calendar;

public class PGraphics {

    GraphicsContext gc;
    FXApp parent;
    boolean isPrimary;

    public double offsetX, offsetY;
    public double screenW, screenH;
    public int width, height;

	public PGraphics(GraphicsContext gc, FXApp parent) {
        this.gc = gc;
        this.parent = parent;
        screenW = gc.getCanvas().getWidth();
        screenH = gc.getCanvas().getHeight();
        setPrimary(true);
    }

    public PGraphics createGraphics(int w, int h) {
        PGraphics newPGraphics = new PGraphics(gc, parent);
        newPGraphics.setPrimary(false);
        newPGraphics.size(w, h);
        return newPGraphics;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public void size(int w, int h) {
        width = w;
        height = h;
        if (isPrimary) {
            setOffset();
        }
    }

    public void setOffset() {
        setOffset(((screenW - width) / 2), ((screenH - height) / 2));
    }

    public void setOffset(double x, double y) {
        offsetX = x;
        offsetY = y;
    }

    public void beginDraw() {
        gc.save();
    }

    public void endDraw() {
        gc.restore();
    }

    public int rectMode = CORNER, ellipseMode = CENTER;

    public void rectMode(int newMode) {
        rectMode = clamp(newMode, CORNER, CENTER);
    }

    public void ellipseMode(int newMode) {
        ellipseMode = clamp(newMode, CORNER, CENTER);
    }

    public int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    // rect()

    public void rect(double x, double y, double width, double height) {
        double nx = x, ny = y, nwidth = width, nheight = height;
        switch (rectMode) {
            case CORNER: {
                break;
            }
            case CORNERS: {
                nwidth /= 2;
                nheight /= 2;
                break;
            }
            case RADIUS: {
                nwidth *= 2;
                nheight *= 2;
                nx -= nwidth / 2;
                ny -= nheight / 2;
                break;
            }
            case CENTER: {
                nx -= nwidth / 2;
                ny -= nheight / 2;
                break;
            }
        }
        if (hasFill) gc.fillRect(offsetX+nx, offsetY+ny, nwidth, nheight);
        if (hasStroke) gc.strokeRect(offsetX+nx, offsetY+ny, nwidth, nheight);
    }

    // [Color]

    //// [Setting]

    int colourMode;
    double maxRH;
    double maxGS;
    double maxBB;
    double maxAL;

    boolean hasFill;
    boolean hasStroke;

    ////// background()

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
    
    ////// colorMode()

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

    ////// fill()

    public void fill(int gray) {
        fill(gray, gray, gray, (int) maxAL);
    }

    public void fill(int gray, int alpha) {
        fill(gray, gray, gray, alpha);
    }
    
    public void fill(int v1, int v2, int v3) {
        fill(v1, v2, v3, (int) maxAL);
    }
    
    public void fill(int v1, int v2, int v3, int alpha) {
        hasFill = true;
        gc.setFill(getColor(v1, v2, v3, alpha));
    }

    ////// noFill()

    public void noFill() {
        hasFill = false;
    }

    ////// noStroke()

    public void noStroke() {
        hasStroke = false;
    }

    ////// stroke()

    public void stroke(int gray) {
        stroke(gray, gray, gray, (int) maxAL);
    }

    public void stroke(int gray, int alpha) {
        stroke(gray, gray, gray, alpha);
    }
    
    public void stroke(int v1, int v2, int v3) {
        stroke(v1, v2, v3, (int) maxAL);
    }
    
    public void stroke(int v1, int v2, int v3, int alpha) {
        hasStroke = true;
        gc.setStroke(getColor(v1, v2, v3, alpha));
    }

    public void ellipse(double x, double y, double width, double height) {
        double nx = x, ny = y, nwidth = width, nheight = height;
        switch (ellipseMode) {
            case CORNER: {
                nwidth *= 2;
                nheight *= 2;
                break;
            }
            case CORNERS: {
                break;
            }
            case RADIUS: {
                nwidth *= 2;
                nheight *= 2;
                nx -= nwidth / 2;
                ny -= nheight / 2;
                break;
            }
            case CENTER: {
                nx -= nwidth / 2;
                ny -= nheight / 2;
                break;
            }
        }
        if (hasFill) gc.fillOval(offsetX+nx, offsetY+ny, nwidth, nheight);
        if (hasStroke) gc.strokeOval(offsetX+nx, offsetY+ny, nwidth, nheight);
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

}