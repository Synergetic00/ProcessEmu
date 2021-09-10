package utils;

import main.Main;
import ptypes.PGraphics;

@SuppressWarnings("unused")
public class Action {

    private Actions type;
    private PGraphics pg;
    private double[] values;
    private String str;

    public Action(PGraphics pg, Actions type, double... params) {
        this.pg = pg;
        this.type = type;
        this.values = params;
    }

    public Action(String str, double x, double y) {
        this.type = Actions.TEXT;
        this.values = new double[] {x, y};
        this.str = str;
    }

    public void act(double x, double y) {
        switch (type) {
            case BACKGROUND:
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
                Main.renderer.fill(values[0], values[1], values[2], values[3]);
                break;
            case LINE:
                break;
            case POINT:
                break;
            case STROKE:
                break;
            case TEXT:
                Main.renderer.text(str, values[0], values[1]);
                break;
            case TEXTSIZE:
                break;
            default:
                break;
        }
    }
    
}