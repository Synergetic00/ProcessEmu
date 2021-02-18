package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import misc.GraphicState;
import types.*;

public class Renderer {

    GraphicsContext gc;
    GraphicState gs;
    PGraphics pg;
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