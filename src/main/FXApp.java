package main;

//import utils.PVector;
//import static utils.PVector.*;
import static utils.FXUtils.*;

import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

public class FXApp {

    public int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public final int CORNER = 0;
    public final int CORNERS = 1;
    public final int RADIUS = 2;
    public final int CENTER = 3;

    public int rectMode = CORNER, ellipseMode = CENTER;

    public void rectMode(int newMode) {
        rectMode = clamp(newMode, CORNER, CENTER);
    }

    public void ellipseMode(int newMode) {
        ellipseMode = clamp(newMode, CORNER, CENTER);
    }

    GraphicsContext gc;

    public int width = 100, height = 100;
    double mouseX, mouseY, pmouseX, pmouseY;
    
    protected FXApp(GraphicsContext gc){
        this.gc = gc;

        //Default colour objects
        gc.setFill(Color.WHITE);

        //Default background
        gc.save();
        gc.setFill(Color.rgb(204, 204, 204));
        gc.fillRect(0,0,width,height);
        gc.restore();
    }

    protected void size(int w, int h){
        width = w;
        height = h;
    }

    public void setup() {}
    public void draw() {}

    public boolean hasFill = true;
    public boolean hasStroke = true;

    public void background(int gray) {
        background(gray, gray, gray);
    }

    public void background(int r, int g, int b) {
        gc.save();
        gc.setFill(Color.rgb(r, g, b));
        gc.fillRect(0,0,width,height);
        gc.restore();
    }

    public void noStroke() {
        hasStroke = false;
    }

    public void stroke(int gray) {
        stroke(gray,gray,gray);
    }

    public void stroke(int r, int g, int b) {
        hasStroke = true;
        gc.setStroke(Color.rgb(r,g,b));
    }

    public void strokeWeight(int w) {
        gc.setLineWidth(w);
    }

    public void noFill() {
        hasFill = false;
    }
    
    public void fill(int gray){
        fill(gray, gray, gray);
    }
    public void fill(int r, int g, int b){
        hasFill = true;
        gc.setFill(Color.rgb(r,g,b));
    }

    public final int OPEN = 0;
    public final int CHORD = 1;
    public final int PIE = 2;

    ///////////////////////////////////
    // Shape // 2D Primitives // Arc //
    ///////////////////////////////////

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

        if (hasFill) gc.fillArc(nx, ny, width, height, degStart, degStop, arcMode);
        if (hasStroke) gc.strokeArc(nx, ny, width, height, degStart, degStop, arcMode);
    }

    //////////////////////////////////////
    // Shape // 2D Primitives // Circle //
    //////////////////////////////////////

    public void circle(double x, double y, double size) {
        ellipse(x, y, size, size);
    }

    ///////////////////////////////////////
    // Shape // 2D Primitives // Ellipse //
    ///////////////////////////////////////

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
        if (hasFill) gc.fillOval(nx, ny, nwidth, nheight);
        if (hasStroke) gc.strokeOval(nx, ny, nwidth, nheight);
    }

    ////////////////////////////////////
    // Shape // 2D Primitives // Line //
    ////////////////////////////////////

    public void line(double x1, double y1, double x2, double y2) {
        gc.strokeLine(x1, y1, x2, y2);
    }

    /////////////////////////////////////
    // Shape // 2D Primitives // Point //
    /////////////////////////////////////

    // maybe drawing the point using either circle() or square()
    public void point(double x, double y) {
        line(x, y, x, y);
    }

    ////////////////////////////////////
    // Shape // 2D Primitives // Quad //
    ////////////////////////////////////

    public void quad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double[] xPoints = {x1, x2, x3, x4};
        double[] yPoints = {y1, y2, y3, y4};
        if (hasFill) gc.fillPolygon(xPoints, yPoints, 4);
        if (hasStroke) gc.strokePolygon(xPoints, yPoints, 4);
    }

    ////////////////////////////////////
    // Shape // 2D Primitives // Rect //
    ////////////////////////////////////

    public void rect(double x, double y, double width, double height) {
        double nx = x, ny = y, nwidth = width, nheight = height;
        switch (rectMode) {
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
        if (hasFill) gc.fillRect(nx, ny, nwidth, nheight);
        if (hasStroke) gc.strokeRect(nx, ny, nwidth, nheight);
    }

    //////////////////////////////////////
    // Shape // 2D Primitives // Square //
    //////////////////////////////////////

    public void square(double x, double y, double size) {
        rect(x, y, size, size);
    }

    ////////////////////////////////////////
    // Shape // 2D Primitives // Triangle //
    ////////////////////////////////////////

    public void triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        double[] xPoints = {x1, x2, x3};
        double[] yPoints = {y1, y2, y3};
        if (hasFill) gc.fillPolygon(xPoints, yPoints, 3);
        if (hasStroke) gc.strokePolygon(xPoints, yPoints, 3);
    }

    public void setMousePos(double x, double y) {
        pmouseX = mouseX;
        pmouseY = mouseY;
        mouseX = x;
        mouseY = y;
    }
    
}