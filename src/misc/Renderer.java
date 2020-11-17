package misc;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer {

    GraphicsContext gc;
    public GraphicState gs;
    double renderX, renderY;
    boolean hasFill;
    boolean hasStroke;

	public Renderer(GraphicsContext gc, GraphicState gs) {
        this.gc = gc;
        this.gs = gs;
        hasFill = true;
        hasStroke = true;
        fill(Color.WHITE);
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

	public void fill(Color fillColour) {
        gc.setFill(fillColour);
	}
    
	public void stroke(Color strokeColour) {
        gc.setStroke(strokeColour);
	}
}