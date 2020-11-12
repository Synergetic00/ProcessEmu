package main;

import javafx.scene.canvas.*;
import javafx.scene.input.*;
import utils.PGraphics;

public class FXApp {

    GraphicsContext gc;
    PGraphics g;

    double offsetX, offsetY;
    double screenW, screenH;

    public FXApp(GraphicsContext gc) {
        this.gc = gc;
        g = new PGraphics(gc, this);
        screenW = gc.getCanvas().getWidth();
        screenH = gc.getCanvas().getHeight();
        defaultSettings();
    }

    private void defaultSettings() {
        size(100,100);
    }

    public double width, height;

    public void size(int width, int height) {
        this.width = width;
        this.height = height;
        offsetX = (screenW - width) / 2;
        offsetY = (screenH - height) / 2;
    }

    public void fill(int gray) {
        g.fill(gray);
    }

    public void settings() {}
    public void setup() {}
    public void draw() {}

    public void handleSettings() {
        settings();
    }
    
    public void handleSetup() {
        setup();
    }

    public void handleDraw() {
        draw();
    }
    
    public void handleKeyPressed(KeyEvent keyEvent) {}
    
    public void handleKeyReleased(KeyEvent keyEvent) {}
    
    public void handleKeyTyped(KeyEvent keyEvent) {}
    
    public void handleMouseClicked(MouseEvent mouseEvent) {}
    
    public void handleMouseDragged(MouseEvent mouseEvent) {}
    
    public void handleMouseMoved(MouseEvent mouseEvent) {}
    
    public void handleMousePressed(MouseEvent mouseEvent) {}
    
    public void handleMouseReleased(MouseEvent mouseEvent) {}
    
    public void handleMouseWheel(ScrollEvent scrollEvent) {}

}