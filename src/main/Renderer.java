package main;

import javafx.scene.canvas.GraphicsContext;
import ptypes.PGraphics;

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

    public void fill(double rh, double gs, double bv, double ao) {

    }

    public void stroke(double rh, double gs, double bv, double ao) {

    }

    public void text(String str, double x, double y) {
        gc.fillText(str, x, y);
    }
    
}