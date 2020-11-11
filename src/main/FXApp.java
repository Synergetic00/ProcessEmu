package main;

import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import javafx.scene.shape.ArcType;

import static utils.PConstants.*;

public class FXApp {

    GraphicsContext gc;
    double offsetX, offsetY;
    double screenW, screenH;

    public FXApp(GraphicsContext gc) {
        this.gc = gc;
        screenW = gc.getCanvas().getWidth();
        screenH = gc.getCanvas().getHeight();
        defaultSettings();
    }

    private void defaultSettings() {
        colorMode(RGB, 255, 255, 255, 255);
        size(100,100);
        fill(255);
        stroke(0);
    }

    // [Environment]

    public double width, height;

    // size()

    public void size(int width, int height) {
        this.width = width;
        this.height = height;
        offsetX = (screenW - width) / 2;
        offsetY = (screenH - height) / 2;

        gc.save();
        gc.setFill(Color.rgb(20,20,20));
        gc.fillRect(0, 0, screenW, screenH);
        background(204);
        gc.restore();
    }

    public void size(int width, int height, int renderer) {
        size(width, height);
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

    ///////////////////////////

    // arc()

    public void arc(double x, double y, double width, double height, double start, double stop) {
        double degStart = -degrees(start);
        double degStop = -degrees(stop);
        degStop -= degStart;

        double nx = x - width/2;
        double ny = y - height/2;

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

        double nx = x - width/2;
        double ny = y - height/2;

        if (hasFill) gc.fillArc(offsetX+nx, offsetY+ny, width, height, degStart, degStop, arcMode);
        if (hasStroke) gc.strokeArc(offsetX+nx, offsetY+ny, width, height, degStart, degStop, arcMode);
    }

    // circle()

    public void circle(double x, double y, double size) {
        ellipse(x, y, size, size);
    }

    // ellipse()
    
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

    // line()
    
    public void line(double x1, double y1, double x2, double y2) {
        gc.strokeLine(offsetX+x1, offsetY+y1, offsetX+x2, offsetY+y2);
    }

    // point() - maybe drawing the point using either circle() or square()
    public void point(double x, double y) {
        line(x, y, x, y);
    }

    // quad()

    public void quad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double[] xPoints = {offsetX+x1, offsetX+x2, offsetX+x3, offsetX+x4};
        double[] yPoints = {offsetY+y1, offsetY+y2, offsetY+y3, offsetY+y4};
        if (hasFill) gc.fillPolygon(xPoints, yPoints, 4);
        if (hasStroke) gc.strokePolygon(xPoints, yPoints, 4);
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

    // square()

    public void square(double x, double y, double size) {
        rect(x, y, size, size);
    }

    // triangle()

    public void triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        double[] xPoints = {offsetX+x1, offsetX+x2, offsetX+x3};
        double[] yPoints = {offsetY+y1, offsetY+y2, offsetY+y3};
        if (hasFill) gc.fillPolygon(xPoints, yPoints, 3);
        if (hasStroke) gc.strokePolygon(xPoints, yPoints, 3);
    }

    public int rectMode = CORNER, ellipseMode = CENTER;

    public void rectMode(int newMode) {
        rectMode = clamp(newMode, CORNER, CENTER);
    }

    public void ellipseMode(int newMode) {
        ellipseMode = clamp(newMode, CORNER, CENTER);
    }

    public double degrees(double a) {
        return Math.toDegrees(a);
    }

    public double radians(double a) {
        return Math.toRadians(a);
    }

    public int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    // Converts value from original range [1] to new range [2]
    public double map(double value, double start1, double stop1, double start2, double stop2) {
        return ((1d/((stop1-start1)/(stop2-start2)))*(value-stop1)+stop2);
    }

    // Utility Functions

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

    public void settings() {}
    public void setup() {}
    public void draw() {}

    public void handleSettings() {
        settings();
    }
    
    public void handleSetup() {
        setup();
    }

    public void handleDraw() {
        draw();
    }
    
    public void handleKeyPressed(KeyEvent keyEvent) {}
    
    public void handleKeyReleased(KeyEvent keyEvent) {}
    
    public void handleKeyTyped(KeyEvent keyEvent) {}
    
    public void handleMouseClicked(MouseEvent mouseEvent) {}
    
    public void handleMouseDragged(MouseEvent mouseEvent) {}
    
    public void handleMouseMoved(MouseEvent mouseEvent) {}
    
    public void handleMousePressed(MouseEvent mouseEvent) {}
    
    public void handleMouseReleased(MouseEvent mouseEvent) {}
    
    public void handleMouseWheel(ScrollEvent scrollEvent) {}

}