package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import misc.GraphicState;

public class Renderer {

    GraphicsContext gc;
    PGraphics pg;
    public GraphicState gs;
    double rdrX, rdrY;

	public Renderer(GraphicsContext gc, GraphicState gs, PGraphics pg) {
        this.gc = gc;
        this.gs = gs;
        this.pg = pg;
    }

    public void setBackground(Color color) {
        gc.save();
        gc.setFill(color);
        gc.fillRect(GraphicState.offsetX+rdrX, GraphicState.offsetY+rdrY, gs.width, gs.height);
        gc.restore();
    }

    public void setFill(Color color) {
        gc.setFill(color);
    }

    public void setStroke(Color color) {
        gc.setStroke(color);
    }

}