package main;

import event.MouseEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;

public class FXApp {

    GraphicsContext gc;
    public PGraphics g;
    public int width, height;

    public FXApp(GraphicsContext gc) {
        this.gc = gc;
        g = new PGraphics(gc, this);
        size(100,100);
        fill(255);
        stroke(0);
    }

    public void size(int w, int h) {
        g.size(w, h);
        width = g.gs.width;
        height = g.gs.height; 
        background(204);
    }

    public void fill(int rh, int gs, int bb) {
        g.fill(rh, gs, bb);
    }

    public void rect(int x, int y, int w, int h) {
        System.out.println("called rect");
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
    public void mouseWheel(MouseEvent event) {}
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

    public void handleMouseClicked(javafx.scene.input.MouseEvent event) {
        mouseClicked();
    }

    public void handleMouseDragged(javafx.scene.input.MouseEvent event) {
        mouseDragged();
    }

    public void handleMouseMoved(javafx.scene.input.MouseEvent event) {
        mousedMoved();
    }

    public void handleMousePressed(javafx.scene.input.MouseEvent event) {
        mousePressed();
    }

    public void handleMouseReleased(javafx.scene.input.MouseEvent event) {
        mouseReleased();
    }

    public void handleMouseWheel(ScrollEvent scrollEvent) {
        int count = (int) -(scrollEvent.getDeltaY() / scrollEvent.getMultiplierY());
        MouseEvent event = new MouseEvent(count);
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