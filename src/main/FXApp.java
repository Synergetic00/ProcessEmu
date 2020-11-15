package main;

import javafx.scene.canvas.*;
import javafx.scene.input.*;
import utils.PGraphics;

public class FXApp {

    GraphicsContext gc;
    PGraphics g;

    public FXApp(GraphicsContext gc) {
        this.gc = gc;
        g = new PGraphics(gc, this);
    }

    public PGraphics createGraphics(int w, int h) {
        return g.createGraphics(w, h);
    }

    public void hello(String text) {
        System.out.println("FX: "+text);
    }






















    // Methods to be overwritten
    public void settings() {}
    public void setup() {}
    public void draw() {}
    public void mouseClicked() {}
    public void mouseDragged() {}
    public void mousedMoved() {}
    public void mousePressed() {}
    public void mouseReleased() {}
    public void mouseWheel(ScrollEvent event) {}
    public void keyPressed() {}
    public void keyReleased() {}
    public void keyTyped() {}

    // Handled methods by Main.java

    public void handleSettings() {
        settings();
    }
    
    public void handleSetup() {
        setup();
    }

    public void handleDraw() {
        draw();
    }

    public void handleMouseClicked(MouseEvent event) {
        mouseClicked();
    }

    public void handleMouseDragged(MouseEvent event) {
        mouseDragged();
    }

    public void handleMouseMoved(MouseEvent event) {
        mousedMoved();
    }

    public void handleMousePressed(MouseEvent event) {
        mousePressed();
    }

    public void handleMouseReleased(MouseEvent event) {
        mouseReleased();
    }

    public void handleMouseWheel(ScrollEvent event) {
        mouseWheel(event);
    }

    public void handleKeyPressed(KeyEvent event) {
        keyPressed();
    }

    public void handleKeyReleased(KeyEvent event) {
        keyReleased();
    }

    public void handleKeyTyped(KeyEvent event) {
        keyTyped();
    }

}