package misc;

public class GraphicState {
    
    public static double offsetX, offsetY;
    public double screenW, screenH;
    public int width, height;

    public GraphicState() {}

    public static void setOffset(double x, double y) {
        offsetX = x;
        offsetY = y;
    }

}