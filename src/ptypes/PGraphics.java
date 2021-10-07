package ptypes;

import java.util.LinkedList;
import java.util.ArrayList;

import javafx.scene.transform.Affine;
import main.Main;
import utils.Action;
import utils.Actions;
import utils.Colours;
import utils.ModeState;
import utils.StyleState;

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
        fill(255);
        stroke(0);
        //strokeWeight(1);
        rectMode = CORNER;
        ellipseMode = CENTER;
    }
    
    public PGraphics(int w, int h) {
        width = w;
        height = h;
    }

    ///////////////
    // Structure //
    ///////////////

    public ArrayList<StyleState> styleStates = new ArrayList<>();
    public ArrayList<ModeState> modeStates = new ArrayList<>();

    public void pop() {
        actions.add(new Action(this, Actions.POP));
    }

    public void popStyle() {
        actions.add(new Action(this, Actions.POPSTYLE));
    }

    public void push() {
        actions.add(new Action(this, Actions.PUSH));
    }

    public void pushStyle() {
        actions.add(new Action(this, Actions.PUSHSTYLE));
    }












    public void beginDraw() {
        actions = new LinkedList<Action>();
        defaultSettings();
    }
    
    public void endDraw() {}

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

    public void stroke(double gray) {
        actions.add(new Action(this, Actions.STROKE, gray, gray, gray, maxAO));
    }

    public void stroke(double gray, double alpha) {
        actions.add(new Action(this, Actions.STROKE, gray, gray, gray, alpha));
    }

    public void stroke(double rh, double gs, double bv) {
        actions.add(new Action(this, Actions.STROKE, rh, gs, bv, maxAO));
    }

    public void stroke(double rh, double gs, double bv, double ao) {
        actions.add(new Action(this, Actions.STROKE, rh, gs, bv, ao));
    }

    public void text(String str, double x, double y) {
        actions.add(new Action(str, x, y));
    }

    public void textSize(double size) {
        actions.add(new Action(null, Actions.TEXTSIZE, size));
    }

    // Shapes

    public void arc(double x, double y, double width, double height, double start, double stop) {
        actions.add(new Action(this, Actions.ARC, x, y, width, height, start, stop));
    }

    public void arc(double x, double y, double width, double height, double start, double stop, int mode) {
        actions.add(new Action(this, Actions.ARC, mode, x, y, width, height, start, stop));
    }

    public void circle(double x, double y, double s) {
        actions.add(new Action(this, Actions.CIRCLE, x, y, s));
    }

    public void ellipse(double x, double y, double w, double h) {
        actions.add(new Action(this, Actions.ELLIPSE, x, y, w, h));
    }

    public void line(double startX, double startY, double endX, double endY) {
        actions.add(new Action(this, Actions.LINE, startX, startY, endX, endY));
    }

    public void point(double x, double y) {
        actions.add(new Action(this, Actions.POINT, x, y));
    }

    public void quad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        quad(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    public void rect(double x, double y, double w, double h) {
        actions.add(new Action(this, Actions.RECT, x, y, w, h));
    }

    public void square(double x, double y, double extent) {
        actions.add(new Action(this, Actions.SQUARE, x, y, extent));
    }

    public void triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        actions.add(new Action(this, Actions.TRIANGLE, x1, y1, x2, y2, x3, y3));
    }

    /////////////////////////
    // Shape // Attributes //
    /////////////////////////

    public void ellipseMode(int mode) {
        actions.add(new Action(this, Actions.ELLIPSEMODE, mode));
    }

    public void rectMode(int mode) {
        actions.add(new Action(this, Actions.RECTMODE, mode));
    }

    public void strokeCap(int type) {
        actions.add(new Action(this, Actions.STROKECAP, type));
    }

    public void strokeJoin(int type) {
        actions.add(new Action(this, Actions.STROKEJOIN, type));
    }

    public void strokeWeight(double weight) {
        actions.add(new Action(this, Actions.STROKEWEIGHT, weight));
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