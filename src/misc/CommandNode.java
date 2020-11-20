package misc;

import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class CommandNode {

    String commandString;
    String textString;
    int value1;
    int value2;
    int value3;
    int valueA;
    double valueX;
    double valueY;
    double valueW;
    double valueH;
    TextAlignment textAlignment;
    VPos vPos;

    public Color backgroundColour;
    public Color fillColour;
    public Color strokeColour;

    public CommandNode(String s, int v1, int v2, int v3, int vA) {
        commandString = s;
        value1 = v1;
        value2 = v2;
        value3 = v3;
        valueA = vA;
    }

    public CommandNode(String s, double x, double y, double w, double h) {
        commandString = s;
        valueX = x;
        valueY = y;
        valueW = w;
        valueH = h;
    }

	public CommandNode(String s, String t, double x, double y) {
        commandString = s;
        textString = t;
        valueX = x;
        valueY = y;
	}

	public CommandNode(String string, Color colour) {
        commandString = string;
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

	public CommandNode(String s, TextAlignment alignH, VPos alignV) {
        commandString = s;
        textAlignment = alignH;
        vPos = alignV;
	}

	public CommandNode(String s, double newSize) {
        commandString = s;
        valueW = newSize;
	}


	public CommandNode(String s, int value) {
        commandString = s;
        value1 = value;
	}
	public CommandNode(String s) {
        commandString = s;
	}

	public CommandNode(String s, double x, double y) {
        commandString = s;
        valueX = x;
        valueY = y;
	}

	public void execute(Renderer r, double x, double y) {
        r.renderPos(x, y);
        switch (commandString) {
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
            
            case "rect": {
                r.rect(valueX+x, valueY+y, valueW, valueH);
                break;
            }
            
            case "ellipse": {
                r.ellipse(valueX+x, valueY+y, valueW, valueH);
                break;
            }
            
            case "text": {
                r.text(textString, valueX+x, valueY+y);
                break;
            }
            
            case "textAlign": {
                r.textAlign(textAlignment, vPos);
                break;
            }
            
            case "textSize": {
                r.textSize(valueW);
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
            
            case "scale": {
                r.scale(valueX, valueY);
                break;
            }
            
            case "translate": {
                r.translate(valueX, valueY);
                break;
            }
            
            case "line": {
                r.line(valueX, valueY, valueW, valueH);
                break;
            }
            
            case "strokeWeight": {
                r.strokeWeight(valueW);
                break;
            }
            
            case "vertex": {
                r.vertex(valueX, valueY);
                break;
            }
            
            case "endShape": {
                r.endShape();
                break;
            }
        }
	}
    
}