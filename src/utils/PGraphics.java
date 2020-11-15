package utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.FXApp;

public class PGraphics {

    GraphicsContext gc;
    FXApp parent;

    public double offsetX, offsetY;
    public double screenW, screenH;
    public int width, height;

    public boolean isPrimary;

	public PGraphics(GraphicsContext gc, FXApp parent) {
        this.gc = gc;
        this.parent = parent;
        screenW = gc.getCanvas().getWidth();
        screenH = gc.getCanvas().getHeight();
    }

    public void hello(String text) {
        System.out.println(""+text+" "+isPrimary+" "+width);
    }

    public void size(int w, int h) {
        width = w;
        height = h;

        setOffset(((screenW - width) / 2), ((screenH - height) / 2));
    }

    public void setOffset(double x, double y) {
        offsetX = x;
        offsetY = y;
    }

    public void fill(int red, int green, int blue) {
        gc.setFill(Color.rgb(red, green, blue));
    }

    public void background(int gray) {
        gc.save();
        gc.setFill(Color.rgb(gray, gray, gray));
        gc.fillRect(offsetX, offsetY, width, height);
        gc.restore();
    }

	public void rect(double x, double y, double w, double h) {
        if (isPrimary) {
            gc.setFill(Color.RED);
        } else {
            gc.setFill(Color.BLUE);
        }
        gc.fillRect(offsetX+x, offsetY+y, w, h);
    }
    
    public void beginDraw() {
        // Doesn't need implementation
    }
    
    public void endDraw() {
        // Doesn't need implementation
    }

}