package utils;

import javafx.scene.canvas.GraphicsContext;
import main.FXApp;

public class PGraphics {

    GraphicsContext gc;
    FXApp parent;

    public double offsetX, offsetY;
    public double screenW, screenH;
    public int width, height;

	public PGraphics(GraphicsContext gc, FXApp parent) {
        this.gc = gc;
        this.parent = parent;
    }

    public PGraphics createGraphics(int w, int h) {
        return new PGraphics(gc, parent);
    }

    public void hello(String text) {
        System.out.println("PG: "+text);
    }

}