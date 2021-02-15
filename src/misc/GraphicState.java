package misc;

public class GraphicState {
    
    public static double offsetX, offsetY;
    public int width, height;

    public GraphicState() {}

    public static void setOffset(double x, double y) {
        offsetX = x;
        offsetY = y;
    }

	public void setSize(int w, int h) {
        width = w;
        height = h;
	}

}