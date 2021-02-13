package misc;

import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.TextAlignment;

public class CommandNode {

    String type, text;
    TextAlignment hAlign;
    VPos vAlign;
    ArcType arcMode;

    // Common: 0 = red, 1 = green, 2 = blue, 3 = alpha
    int[] intVals = new int[4];
    // Common: 0 = x, 1 = y, 2 = w, 3 = h, 4/5 = ranges
    double[] dblVals = new double[6];

    public Color backgroundColour, fillColour, strokeColour;

    //////////////////
    // Constructors //
    //////////////////

    public CommandNode(String s) {
        type = s;
	}

    // Integers

    public CommandNode(String s, int valA) { // 1
        type = s;
        intVals[0] = valA;
	}

    public CommandNode(String s, int valA, int valB, int valC, int valD) { // 4
        type = s;
        intVals[0] = valA;
        intVals[1] = valB;
        intVals[2] = valC;
        intVals[3] = valD;
    }

    // Doubles

    public CommandNode(String s, double valA) { // 1
        type = s;
        dblVals[0] = valA;
	}

    public CommandNode(String s, double valA, double valB) { // 2
        type = s;
        dblVals[0] = valA;
        dblVals[1] = valB;
	}

    public CommandNode(String s, double valA, double valB, double valC, double valD) { // 4
        type = s;
        dblVals[0] = valA;
        dblVals[1] = valB;
        dblVals[2] = valC;
        dblVals[3] = valD;
    }

    // Arc constructor (w/o mode)
	public CommandNode(String s, double valA, double valB, double valC, double valD, double valE, double valF) { // 6
        dblVals[0] = valA;
        dblVals[1] = valB;
        dblVals[2] = valC;
        dblVals[3] = valD;
        dblVals[4] = valE;
        dblVals[5] = valF;
	}

    // Other

    // Textual element constructor
	public CommandNode(String s, String t, double x, double y) {
        type = s;
        text = t;
        dblVals[0] = x;
        dblVals[1] = y;
	}

    // Color constructor
	public CommandNode(String string, Color colour) {
        type = string;
        switch (string) {
            case "background": {
                backgroundColour = colour;
                break;
            }
            
            case "fill": {
                fillColour = colour;
                break;
            }
            
            case "stroke": {
                strokeColour = colour;
                break;
            }
        }
	}

    // TextAlign constructor
	public CommandNode(String s, TextAlignment alignH, VPos alignV) {
        type = s;
        hAlign = alignH;
        vAlign = alignV;
	}

    // Arc constructor (w/ mode)
	public CommandNode(String s, double valA, double valB, double valC, double valD, double valE, double valF, ArcType mode) {
        dblVals[0] = valA;
        dblVals[1] = valB;
        dblVals[2] = valC;
        dblVals[3] = valD;
        dblVals[4] = valE;
        dblVals[5] = valF;
        arcMode = mode;
	}

    // Main execution stack    
	public void execute(Renderer r, double x, double y) {
        r.renderPos(x, y);
        switch (type) {
            case "background": {
                r.background(backgroundColour);
                break;
            }
            
            case "fill": {
                r.fill(fillColour);
                break;
            }
            
            case "stroke": {
                r.stroke(strokeColour);
                break;
            }
            
            case "arc": {
                r.arc(dblVals[0], dblVals[1], dblVals[2], dblVals[3], dblVals[4], dblVals[5]);
                break;
            }
            
            case "arcM": {
                r.arc(dblVals[0], dblVals[1], dblVals[2], dblVals[3], dblVals[4], dblVals[5], arcMode);
                break;
            }
            
            case "circle": {
                break;
            }
            
            case "ellipse": {
                r.ellipse(dblVals[0]+x, dblVals[1]+y, dblVals[2], dblVals[3]);
                break;
            }
            
            case "line": {
                break;
            }
            
            case "point": {
                break;
            }
            
            case "quad": {
                break;
            }
            
            case "rect": {
                r.rect(dblVals[0]+x, dblVals[1]+y, dblVals[2], dblVals[3]);
                break;
            }
            
            case "square": {
                break;
            }
            
            case "triangle": {
                break;
            }
            
            case "text": {
                r.text(text, dblVals[0]+x, dblVals[1]+y);
                break;
            }
            
            case "textAlign": {
                r.textAlign(hAlign, vAlign);
                break;
            }
            
            case "textSize": {
                r.textSize(dblVals[0]);
                break;
            }
            
            case "pushMatrix": {
                r.pushMatrix();
                break;
            }
            
            case "popMatrix": {
                r.popMatrix();
                break;
            }
            
            case "resetMatrix": {
                r.resetMatrix();
                break;
            }
            
            case "rotate": {
                r.rotate(dblVals[0]);
                break;
            }
            
            case "scale": {
                r.scale(dblVals[0], dblVals[1]);
                break;
            }
            
            case "translate": {
                r.translate(dblVals[0], dblVals[1]);
                break;
            }
            
            case "strokeWeight": {
                r.strokeWeight(dblVals[0]);
                break;
            }
            
            case "vertex": {
                r.vertex(dblVals[0], dblVals[1]);
                break;
            }
            
            case "endShape": {
                r.endShape();
                break;
            }
        }
	}
    
}