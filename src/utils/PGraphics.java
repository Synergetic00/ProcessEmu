package utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.FXApp;

public class PGraphics {

    GraphicsContext gc;
    FXApp parent;
    public GraphicState gs;

	public PGraphics(GraphicsContext gc, FXApp parent) {
        this.gc = gc;
        this.parent = parent;
        gs.screenW = gc.getCanvas().getWidth();
        gs.screenH = gc.getCanvas().getHeight();
    }

    public void hello(String text) {
        System.out.println(""+text+" "+gs.isPrimary+" "+gs.width);
    }

    public void size(int w, int h) {
        gs.width = w;
        gs.height = h;

        setOffset(((gs.screenW - gs.width) / 2), ((gs.screenH - gs.height) / 2));
    }

    public void setOffset(double x, double y) {
        gs.offsetX = x;
        gs.offsetY = y;
    }

    public void fill(int red, int green, int blue) {
        gc.setFill(Color.rgb(red, green, blue));
    }

    public void background(int gray) {
        gc.save();
        gc.setFill(Color.rgb(gray, gray, gray));
        gc.fillRect(gs.offsetX, gs.offsetY, gs.width, gs.height);
        gc.restore();
    }

	public void rect(double x, double y, double w, double h) {
        if (gs.isPrimary) {
            gc.setFill(Color.RED);
        } else {
            gc.setFill(Color.BLUE);
        }
        gc.fillRect(gs.offsetX+x, gs.offsetY+y, w, h);
    }
    
    public void beginDraw() {
        // Doesn't need implementation
    }
    
    public void endDraw() {
        // Doesn't need implementation
    }

}