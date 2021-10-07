package utils;

import main.Main;
import ptypes.PGraphics;

public class Action {

    private Actions type;
    private PGraphics pg;
    private double[] values;
    private String str;
    private int value = -1;

    public Action(PGraphics pg, Actions type) {
        this.pg = pg;
        this.type = type;
    }

    public Action(PGraphics pg, Actions type, double... params) {
        this.pg = pg;
        this.type = type;
        this.values = params;
    }

    public Action(PGraphics pg, Actions type, int param, double... params) {
        this.pg = pg;
        this.type = type;
        this.values = params;
        this.value = param;
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
            case POP:
                Main.renderer.pop(pg);
                break;
            case POPSTYLE:
                Main.renderer.popStyle(pg);
                break;
            case PUSH:
                Main.renderer.push(pg);
                break;
            case PUSHSTYLE:
                Main.renderer.pushStyle(pg);
                break;
            case STROKEWEIGHT:
                Main.renderer.strokeWeight(values[0]);
                break;

            case BACKGROUND:
                Main.renderer.background(pg, value, x, y);
                break;
            case QUAD:
                Main.renderer.quad(pg, values[0] + x, values[1] + y, values[2] + x, values[3] + y, values[4] + x, values[5] + y, values[6] + x, values[7] + y);
                break;
            case RECT:
                Main.renderer.rect(pg, values[0] + x, values[1] + y, values[2], values[3]);
                break;
            case SQUARE:
                Main.renderer.square(pg, values[0] + x, values[1] + y, values[2]);
                break;
            case TRIANGLE:
                Main.renderer.triangle(pg, values[0] + x, values[1] + y, values[2] + x, values[3] + y, values[4] + x, values[5] + y);
                break;
            case ARC:
                if (value == -1) {
                    Main.renderer.arc(pg, values[0] + x, values[1] + y, values[2], values[3], values[4], values[5]);
                } else {
                    Main.renderer.arc(pg, values[0] + x, values[1] + y, values[2], values[3], values[4], values[5], value);
                }
                break;
            case CIRCLE:
                Main.renderer.circle(pg, values[0] + x, values[1] + y, values[2]);
                break;
            case ELLIPSE:
                Main.renderer.ellipse(pg, values[0] + x, values[1] + y, values[2], values[3]);
                break;
            case FILL:
                Main.renderer.fill(pg, values[0], values[1], values[2], values[3]);
                break;
            case LINE:
                Main.renderer.line(pg, values[0] + x, values[1] + y, values[2] + x, values[3] + y);
                break;
            case POINT:
                Main.renderer.point(pg, values[0] + x, values[1] + y);
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
            case ELLIPSEMODE:
                Main.renderer.ellipseMode(pg, value);
                break;
            case RECTMODE:
                Main.renderer.rectMode(pg, value);
                break;
            case STROKECAP:
                Main.renderer.strokeCap(value);
                break;
            case STROKEJOIN:
                Main.renderer.strokeJoin(value);
                break;
            default:
                break;
        }
    }
    
}