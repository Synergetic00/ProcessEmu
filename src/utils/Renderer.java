package utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer {

    GraphicsContext gc;
    GraphicState gs;
    double renderX, renderY;

	public Renderer(GraphicsContext gc, GraphicState gs) {
        this.gc = gc;
        this.gs = gs;
    }
    
    public void renderPos(double x, double y) {
        renderX = x;
        renderY = y;
    }

    public void background(Color backgroundColour) {
        gc.save();
        gc.setFill(backgroundColour);
        gc.fillRect(GraphicState.offsetX+renderX, GraphicState.offsetY+renderY, gs.width, gs.height);
        gc.restore();
    }

	public void rect(double x, double y, double w, double h) {
        gc.fillRect(GraphicState.offsetX+x, GraphicState.offsetY+y, w, h);
    }

	public void fill(Color fillColour) {
        gc.setFill(fillColour);
	}
    
}