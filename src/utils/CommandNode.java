package utils;

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

	public void execute(Renderer r, double x, double y) {
        r.renderPos(x, y);
        switch (commandString) {
            case "background": {
                r.background(value1);
                break;
            }
            
            case "fill": {
                r.fill(value1, value2, value3);
                break;
            }
            
            case "rect": {
                r.rect(valueX+x, valueY+y, valueW, valueH);
                break;
            }

            default: {
                break;
            }
        }
	}
    
}