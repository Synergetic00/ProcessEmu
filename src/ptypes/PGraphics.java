package ptypes;

import java.util.LinkedList;

import javafx.scene.transform.Affine;
import main.Main;
import utils.Action;
import utils.Actions;
import utils.Colours;

import static utils.Maths.*;
import static utils.Constants.*;

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

    public void defaultSettings() {
        colorMode(RGB, 255, 255, 255, 255);
    }
    
    public PGraphics(int w, int h) {
        width = w;
        height = h;
    }

    public void beginDraw() {
        actions = new LinkedList<Action>();
        defaultSettings();
        System.out.println("HERE 1");
    }
    
    public void endDraw() {

    }

    public void colorMode(int mode) {
        colorMode(mode, (int)maxRH, (int)maxGS, (int)maxBB, maxAO);
    }

    public void colorMode(int mode, double max) {
        colorMode(mode, max, max, max, maxAO);
    }

    public void colorMode(int mode, double rh, double gs, double bb) {
        colorMode(mode, rh, gs, bb, maxAO);
    }

    public void colorMode(int mode, double rh, double gs, double bb, double alpha) {
        colorMode = mode;
        maxRH = rh;
        maxGS = gs;
        maxBB = bb;
        maxAO = alpha;
    }

    public void background(double gray) {
        System.out.println("HERE 2");
        setBackground(gray, gray, gray, maxAO);
    }

    public void background(double gray, double alpha) {
        setBackground(gray, gray, gray, alpha);
    }

    public void background(double rh, double gs, double bv) {
        setBackground(rh, gs, bv, maxAO);
    }

    public void background(double rh, double gs, double bv, double ao) {
        setBackground(rh, gs, bv, ao);
    }

    public void setBackground(double rh, double gs, double bv, double ao) {
        double mappedRH = clamp(map(rh, 0, maxRH, 0, 255), 0, 255);
        double mappedGS = clamp(map(gs, 0, maxGS, 0, 255), 0, 255);
        double mappedBV = clamp(map(bv, 0, maxBB, 0, 255), 0, 255);
        double mappedAO = clamp(map(ao, 0, maxAO, 0, 255), 0, 255);
        int encodedValue = Colours.encodeColour(mappedRH, mappedGS, mappedBV, mappedAO);
        actions.add(new Action(this, Actions.BACKGROUND, encodedValue));
    }

    public void fill(double gray) {
        actions.add(new Action(null, Actions.FILL, gray, gray, gray, maxAO));
    }

    public void fill(double gray, double alpha) {
        actions.add(new Action(null, Actions.FILL, gray, gray, gray, alpha));
    }

    public void fill(double rh, double gs, double bv) {
        actions.add(new Action(null, Actions.FILL, rh, gs, bv, maxAO));
    }

    public void fill(double rh, double gs, double bv, double ao) {
        actions.add(new Action(null, Actions.FILL, rh, gs, bv, ao));
    }

    public void text(String str, double x, double y) {
        actions.add(new Action(str, x, y));
    }

    public void textSize(double size) {
        actions.add(new Action(null, Actions.TEXTSIZE, size));
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