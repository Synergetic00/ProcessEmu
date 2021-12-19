package main;

import jgraphics.canvas.Graphics;
import jgraphics.utils.Colour;

public class AppBase {

    private Graphics gc;

    public AppBase(Graphics gc) {
        this.gc = gc;
    }

    public void size(int width, int height) {

    }

    public void fill(double rh, double gs, double bv) {
        gc.setFill(new Colour((int)rh, (int)gs, (int)bv, 255));
    }

    public void rect(double x, double y, double w, double h) {
        gc.fillRect(x, y, w, h);
    }

    ///////////////////
    // Other Methods //
    ///////////////////

    public void settings() {}

    public void setup() {}

    public void draw() {}

    public void handleSettings() {
        settings();
    }

    public void handleSetup() {
        setup();
        render();
    }

    public void handleDraw() {
        //updateTime();
        render();
    }

    private void render() {
        draw();
    }
    
}