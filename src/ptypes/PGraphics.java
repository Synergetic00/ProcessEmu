package ptypes;

import java.util.LinkedList;

import javafx.scene.transform.Affine;
import main.Main;
import utils.Action;
import utils.Actions;

public class PGraphics {

    private LinkedList<Action> actions;

    public int width;
    public int height;

    public boolean hasFill;
    public boolean hasStroke;

    public int rectMode;
    public int ellipseMode;
    public int colorMode;

    public final int MATRIX_STACK_DEPTH = 32;
    public int transformCount;
    public Affine[] transformStack = new Affine[MATRIX_STACK_DEPTH];

    public double maxRH;
    public double maxGS;
    public double maxBB;
    public double maxAO;
    
    public PGraphics(int w, int h) {
        width = w;
        height = h;
    }

    public void beginDraw() {
        actions = new LinkedList<Action>();
    }
    
    public void endDraw() {

    }

    public void background(double gray) {

    }

    public void fill(double gray) {
        actions.add(new Action(this, Actions.FILL, gray, gray, gray, maxAO));
    }

    public void fill(double gray, double alpha) {
        actions.add(new Action(this, Actions.FILL, gray, gray, gray, alpha));
    }

    public void fill(double rh, double gs, double bv) {
        actions.add(new Action(this, Actions.FILL, rh, gs, bv, maxAO));
    }

    public void fill(double rh, double gs, double bv, double ao) {
        actions.add(new Action(this, Actions.FILL, rh, gs, bv, ao));
    }

    public void text(String str, double x, double y) {
        actions.add(new Action(str, x, y));
    }

    public void textSize(double size) {

    }

    public void rect(double x, double y, double w, double h) {
        actions.add(new Action(this, Actions.RECT, x, y, w, h));
    }

    public void render(double x, double y) {
        for (Action action : actions) {
            action.act(x, y);
        }
    }

    public void render(double x, double y, double w, double h) {
        double scaleX = w / width;
        double scaleY = h / height;
        Main.renderer.pushMatrix(this);
        Main.renderer.scale(scaleX, scaleY, this);
        for (Action action : actions) {
            action.act(x, y);
        }
    }

}