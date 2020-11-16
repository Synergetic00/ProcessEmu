package utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer {

    GraphicsContext gc;
    GraphicState gs;
    double renderX, renderY;

    public Color backgroundColour;
    public Color fillColour;
    public Color strokeColour;

	public Renderer(GraphicsContext gc, GraphicState gs) {
        this.gc = gc;
        this.gs = gs;
    }
    
    public void renderPos(double x, double y) {
        renderX = x;
        renderY = y;
    }

    public void fill(int red, int green, int blue) {
        gc.setFill(Color.rgb(red, green, blue));
    }

    public void background(int gray) {
        gc.save();
        gc.setFill(Color.rgb(gray, gray, gray));
        gc.fillRect(GraphicState.offsetX+renderX, GraphicState.offsetY+renderY, gs.width, gs.height);
        gc.restore();
    }

	public void rect(double x, double y, double w, double h) {
        gc.fillRect(GraphicState.offsetX+x, GraphicState.offsetY+y, w, h);
    }
    
}