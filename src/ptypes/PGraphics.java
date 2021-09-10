package ptypes;

import java.util.LinkedList;

import javafx.scene.transform.Affine;
import main.Main;
import utils.Action;
import utils.Actions;

public class PGraphics {

    public int width;
    public int height;
    private LinkedList<Action> actions;

    public boolean hasFill;
    public boolean hasStroke;

    public int rectMode;
    public int ellipseMode;
    public int colorMode;

    public final int MATRIX_STACK_DEPTH = 32;
    public int transformCount;
    public Affine[] transformStack = new Affine[MATRIX_STACK_DEPTH];
    
    public PGraphics(int w, int h) {
        width = w;
        height = h;
    }

    public void beginDraw() {
        actions = new LinkedList<Action>();
    }
    
    public void endDraw() {

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