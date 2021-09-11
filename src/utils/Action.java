package utils;

import main.Main;
import ptypes.PGraphics;

public class Action {

    private Actions type;
    private PGraphics pg;
    private double[] values;
    private String str;
    private int value;

    public Action(PGraphics pg, Actions type, double... params) {
        this.pg = pg;
        this.type = type;
        this.values = params;
    }

    public Action(PGraphics pg, Actions type, int value) {
        this.pg = pg;
        this.type = type;
        this.value = value;
    }

    public Action(String str, double x, double y) {
        this.type = Actions.TEXT;
        this.values = new double[2];
        this.values[0] = x;
        this.values[1] = y;
        this.str = str;
    }

    public void act(double x, double y) {
        switch (type) {
            case BACKGROUND:
                Main.renderer.background(pg, value, x, y);
                break;
            case QUAD:
                break;
            case RECT:
                Main.renderer.rect(values[0] + x, values[1] + y, values[2], values[3], pg);
                break;
            case SQUARE:
                break;
            case TRIANGLE:
                break;
            case ARC:
                break;
            case CIRCLE:
                break;
            case ELLIPSE:
                break;
            case FILL:
                Main.renderer.fill(pg, values[0], values[1], values[2], values[3]);
                break;
            case LINE:
                break;
            case POINT:
                break;
            case STROKE:
            Main.renderer.stroke(pg, values[0], values[1], values[2], values[3]);
                break;
            case TEXT:
                Main.renderer.text(str, values[0] + x, values[1] + y);
                break;
            case TEXTSIZE:
                Main.renderer.textSize(values[0]);
                break;
            default:
                break;
        }
    }
    
}