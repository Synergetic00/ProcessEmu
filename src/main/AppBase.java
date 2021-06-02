package main;

import javafx.scene.canvas.GraphicsContext;

@SuppressWarnings("unused")
public class AppBase {

    private GraphicsContext gc;

    public AppBase(GraphicsContext gc) {
        this.gc = gc;
    }

    public void handleSettings() {
        settings();
    }

    public void handleSetup() {
        setup();
    }

    public void handleDraw() {
        draw();
    }

    public void settings() {}
    public void setup() {}
    public void draw() {}

}