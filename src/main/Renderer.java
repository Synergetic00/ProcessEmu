package main;

import static utils.Maths.*;
import static utils.Constants.*;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;

public class Renderer {

    GraphicsContext gc;
    static InternalState state;
    
    public Renderer(GraphicsContext gc, InternalState state) {
        this.gc = gc;
        this.state = state;
    }

    ////////////////////////////
    // Shape // 2D Primatives //
    ////////////////////////////

    /*public void arc(double x, double y, double width, double height, double start, double stop) {
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
    }*/

    public static void line(double x1, double y1, double x2, double y2) {
        double sx = x1 + state.offsetW() + state.renderW();
        double sy = y1 + state.offsetW() + state.renderW();
        double ex = x2 + state.offsetW() + state.renderW();
        double ey = y2 + state.offsetW() + state.renderW();

        Main.gc.strokeLine(sx, sy, ex, ey);
    }

    /*public void point(double x, double y) {
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
    }*/
    
}