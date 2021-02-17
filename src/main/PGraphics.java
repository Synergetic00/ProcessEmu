package main;

import javafx.scene.canvas.GraphicsContext;
import misc.GraphicState;

import static utils.Constants.*;
import static utils.ColourUtils.*;

public class PGraphics {

    GraphicsContext gc;
    FXApp parent;
    Renderer r;
	GraphicState gs;
    int colorMode;

    public PGraphics(GraphicsContext gc, FXApp parent) {
        this.gc = gc;
        this.parent = parent;
        gs = new GraphicState();
        r = new Renderer(gc, gs, this);
        colorMode = RGB;
	}
    
    public void setBackground(int argb) {
        r.setBackground(decodeColour(colorMode, argb));
    }

    public void setFill(int argb) {
        r.setFill(decodeColour(colorMode, argb));
    }

    public void setStroke(int argb) {
        r.setStroke(decodeColour(colorMode, argb));
    }

    public void fill(int rh, int gs, int bb) {
        System.out.println("filled");
        setFill(encodeColour(rh, gs, bb, 255));
    }

}