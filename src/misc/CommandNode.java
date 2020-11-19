package misc;

import javafx.scene.paint.Color;

public class CommandNode {

    String commandString;
    int value1;
    int value2;
    int value3;
    int valueA;
    double valueX;
    double valueY;
    double valueW;
    double valueH;

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
        }
	}
    
}