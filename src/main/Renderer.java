package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.text.Font;
import ptypes.PGraphics;
import utils.Colours;

import static utils.Maths.*;
import static utils.Constants.*;

public class Renderer {

    private GraphicsContext gc;

    public Renderer(GraphicsContext gc) {
        this.gc = gc;
    }

    public void rect(double x, double y, double w, double h, PGraphics pg) {

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
    
}