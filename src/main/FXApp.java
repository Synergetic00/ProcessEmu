package main;

import utils.PVector;
import static utils.PVector.*;
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

    int width, height;
    double mouseX, mouseY, pmouseX, pmouseY;
    
    protected FXApp(GraphicsContext gc){
        this.gc = gc;
        gc.setFill(Color.WHITE);
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

    public void square(double x, double y, double size) {
        rect(x, y, size, size);
    }

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

    public void circle(double x, double y, double size) {
        ellipse(x, y, size, size);
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
        if (hasFill) gc.fillOval(nx, ny, nwidth, nheight);
        if (hasStroke) gc.strokeOval(nx, ny, nwidth, nheight);
    }

    public final int OPEN = 0;
    public final int CHORD = 1;
    public final int PIE = 2;

    public void arc(double x, double y, double width, double height, double start, double stop) {
        double nstart = degrees(start);
        double nstop = degrees(stop);
        if (hasFill) gc.fillArc(x, y, width, height, nstart, nstop, ArcType.ROUND);
        if (hasStroke) gc.strokeArc(x, y, width, height, nstart, nstop, ArcType.OPEN);
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
        double nstart = degrees(start)/2;
        double nstop = degrees(stop)/2;
        if (hasFill) gc.fillArc(x, y, width, height, nstart, nstop, arcMode);
        if (hasStroke) gc.strokeArc(x, y, width, height, nstart, nstop, arcMode);
    }

    public void setMousePos(double x, double y) {
        pmouseX = mouseX;
        pmouseY = mouseY;
        mouseX = x;
        mouseY = y;
    }
    
}