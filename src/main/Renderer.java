package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.Affine;
import javafx.scene.text.Font;
import ptypes.PGraphics;
import utils.Colours;
import utils.ModeState;
import utils.StyleState;

import static utils.Maths.*;
import static utils.Constants.*;

public class Renderer {

    private GraphicsContext gc;

    public Renderer(GraphicsContext gc) {
        this.gc = gc;
    }

    ///////////////
    // Structure //
    ///////////////

    public void pop(PGraphics pg) {
        //popMatrix();
        popStyle(pg);
    }

    public void popStyle(PGraphics pg) {
        if (pg.styleStates.size() > 0) {
            AppBase.setState(pg.styleStates.remove(pg.styleStates.size()-1));
        } else {
            throw new RuntimeException("popStyle() needs corresponding pushStyle() statement");
        }

        if (pg.styleStates.size() > 0) {
            ModeState ms = pg.modeStates.remove(pg.modeStates.size()-1);
            pg.rectMode = ms.rectMode;
            pg.ellipseMode = ms.ellipseMode;
            pg.colorMode = ms.colorMode;
        }
    }

    public void push(PGraphics pg) {
        //pushMatrix();
        pushStyle(pg);
    }

    public void pushStyle(PGraphics pg) {
        pg.modeStates.add(new ModeState(0, pg.rectMode, pg.ellipseMode, pg.colorMode));
        pg.styleStates.add(new StyleState(gc));
    }



    ////////////
    // Shapes //
    ////////////

    // arc()

    public void arc(PGraphics pg, double x, double y, double width, double height, double start, double stop) {
        double degStart = -degrees(start);
        double degStop = -degrees(stop);
        degStop -= degStart;

        double nx = AppState.offsetW() + x - width/2;
        double ny = AppState.offsetH() + y - height/2;

        if (pg.hasFill) gc.fillArc(nx, ny, width, height, degStart, degStop, ArcType.ROUND);
        if (pg.hasStroke) gc.strokeArc(nx, ny, width, height, degStart, degStop, ArcType.OPEN);
    }

    public void arc(PGraphics pg, double x, double y, double width, double height, double start, double stop, int mode) {
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

        if (pg.hasFill) gc.fillArc(nx, ny, width, height, degStart, degStop, arcMode);
        if (pg.hasStroke) gc.strokeArc(nx, ny, width, height, degStart, degStop, arcMode);
    }

    // circle()

    public void circle(PGraphics pg, double x, double y, double s) {
        ellipse(pg, x, y, s, s);
    }

    // ellipse()

    public void ellipse(PGraphics pg, double x, double y, double w, double h) {

        double nx = x;
        double ny = y;
        double nw = w;
        double nh = h;

        switch (pg.ellipseMode) {
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

        if (pg.hasFill) gc.fillOval(nx, ny, nw, nh);
        if (pg.hasStroke) gc.strokeOval(nx, ny, nw, nh);
    }

    // line()

    public void line(PGraphics pg, double startX, double startY, double endX, double endY) {
        double sx = AppState.offsetW() + startX;
        double sy = AppState.offsetH() + startY;
        double ex = AppState.offsetW() + endX;
        double ey = AppState.offsetH() + endY;

        gc.strokeLine(sx, sy, ex, ey);
    }

    // point()

    public void point(PGraphics pg, double x, double y) {
        pg.rect(x, y, 1, 1);
    }

    // quad()

    public void quad(PGraphics pg, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double[] xPoints = {x1, x2, x3, x4};
        for (int i = 0; i < 4; i++) xPoints[i] += AppState.offsetW();
        double[] yPoints = {y1, y2, y3, y4};
        for (int i = 0; i < 4; i++) yPoints[i] += AppState.offsetH();
        if (pg.hasFill) gc.fillPolygon(xPoints, yPoints, 4);
        if (pg.hasStroke) gc.strokePolygon(xPoints, yPoints, 4);
    }

    // rect()

    public void rect(PGraphics pg, double x, double y, double w, double h) {

        double nx = x;
        double ny = y;
        double nw = w;
        double nh = h;

        switch (pg.rectMode) {
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

        if (pg.hasFill) gc.fillRect(nx, ny, nw, nh);
        if (pg.hasStroke) gc.strokeRect(nx, ny, nw, nh);
    }

    // square()

    public void square(PGraphics pg, double x, double y, double extent) {
        rect(pg, x, y, extent, extent);
    }

    // triangle()

    public void triangle(PGraphics pg, double x1, double y1, double x2, double y2, double x3, double y3) {
        double[] xPoints = {x1, x2, x3};
        for (int i = 0; i < 3; i++) xPoints[i] += AppState.offsetW();
        double[] yPoints = {y1, y2, y3};
        for (int i = 0; i < 3; i++) yPoints[i] += AppState.offsetH();
        if (pg.hasFill) gc.fillPolygon(xPoints, yPoints, 3);
        if (pg.hasStroke) gc.strokePolygon(xPoints, yPoints, 3);
    }

    public void pushMatrix(PGraphics pg) {
        if (pg.transformCount == pg.transformStack.length) {
            throw new RuntimeException("StackOverflow: Reached the maximum amount of pushed matrixes");
        } else {
            pg.transformStack[pg.transformCount] = gc.getTransform(pg.transformStack[pg.transformCount]);
            pg.transformCount++;
        }
    }
    
    public void resetMatrix() {
        gc.setTransform(new Affine());
    }

    public void popMatrix(PGraphics pg) {
        if (pg.transformCount == 0) {
            throw new RuntimeException("popMatrix() needs corresponding pushMatrix() statement");
        } else {
            pg.transformCount--;
            gc.setTransform(pg.transformStack[pg.transformCount]);
        }
    }

    public void scale(double scaleX, double scaleY, PGraphics pg) {
    }

    public void fill(PGraphics pg, double rh, double gs, double bv, double ao) {
        pg.hasFill = true;
        double mappedRH = clamp(map(rh, 0, pg.maxRH, 0, 255), 0, 255);
        double mappedGS = clamp(map(gs, 0, pg.maxGS, 0, 255), 0, 255);
        double mappedBV = clamp(map(bv, 0, pg.maxBB, 0, 255), 0, 255);
        double mappedAO = clamp(map(ao, 0, pg.maxAO, 0, 255), 0, 255);
        int encodedValue = Colours.encodeColour(mappedRH, mappedGS, mappedBV, mappedAO);
        gc.setFill(Colours.decodeColour(pg.colorMode, encodedValue));
    }

    public void stroke(PGraphics pg, double rh, double gs, double bv, double ao) {
        pg.hasStroke = true;
        double mappedRH = clamp(map(rh, 0, pg.maxRH, 0, 255), 0, 255);
        double mappedGS = clamp(map(gs, 0, pg.maxGS, 0, 255), 0, 255);
        double mappedBV = clamp(map(bv, 0, pg.maxBB, 0, 255), 0, 255);
        double mappedAO = clamp(map(ao, 0, pg.maxAO, 0, 255), 0, 255);
        int encodedValue = Colours.encodeColour(mappedRH, mappedGS, mappedBV, mappedAO);
        gc.setStroke(Colours.decodeColour(pg.colorMode, encodedValue));
    }

    public void text(String str, double x, double y) {
        gc.fillText(str, AppState.offsetW() + x, AppState.offsetH() + y);
    }

    public void background(PGraphics pg, int encodedValue, double x, double y) {
        pushMatrix(pg);
        resetMatrix();
        gc.save();
        gc.setFill(Colours.decodeColour(pg.colorMode, encodedValue));
        gc.fillRect(AppState.offsetW() + x, AppState.offsetH() + y, pg.width, pg.height);
        gc.restore();
        popMatrix(pg);
    }

    public void textSize(double size) {
        gc.setFont(new Font(size));
    }

    /////////////////////////
    // Shape // Attributes //
    /////////////////////////

    public void strokeWeight(double weight) {
        gc.setLineWidth(weight);
    }
    
}