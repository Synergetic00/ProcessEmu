package utils;

import main.Main;
import ptypes.PGraphics;

public class Action {

    private Actions type;
    private PGraphics pg;
    private int[] iv;
    private double[] dv;

    public Action(PGraphics pg, Actions type, int... iparams) {
        this.pg = pg;
        this.type = type;
        this.iv = iparams;
    }

    public Action(PGraphics pg, Actions type, double... dparams) {
        this.pg = pg;
        this.type = type;
        this.dv = dparams;
    }

    public void act(double x, double y) {
        switch (type) {
            case ARC:
                break;
            case CIRCLE:
                break;
            case ELLIPSE:
                break;
            case LINE:
                break;
            case POINT:
                break;
            case QUAD:
                break;
            case RECT:
                Main.renderer.rect(dv[0] + x, dv[1] + y, dv[2], dv[3], pg);
                break;
            case SQUARE:
                break;
            case TRIANGLE:
                break;
            default:
                break;
        }
    }
    
}