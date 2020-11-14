package main;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.*;
import javafx.scene.input.*;
import utils.PGraphics;

import static utils.Constants.*;

public class FXApp {

    GraphicsContext gc;
    PGraphics g;

    public int width, height;

    public FXApp(GraphicsContext gc) {
        this.gc = gc;
        g = new PGraphics(gc, this);
    }

    public PGraphics createGraphics(int w, int h) {
        return g.createGraphics(w, h);
    }

    public void size(int w, int h) {
        g.size(w, h);
    }

    public void image(PGraphics pg, int x, int y) {
        // Not yet implemented
    }

    public void rect(double x, double y, double width, double height) {
        g.rect(x, y, width, height);
    }

    public void ellipse(double x, double y, double width, double height) {
        g.ellipse(x, y, width, height);
    }
    
    // [Color]

    public void background(int gray) {
        g.background(gray);
    }

    public void background(int gray, int alpha) {
        g.background(gray, alpha);
    }
    
    public void background(int v1, int v2, int v3) {
        g.background(v1, v2, v3);
    }
    
    public void background(int v1, int v2, int v3, int alpha) {
        g.background(v1, v2, v3, alpha);
    }

    public void colorMode(int mode) {
        g.colorMode(mode);
    }

    public void colorMode(int mode, double max) {
        g.colorMode(mode, max);
    }

    public void colorMode(int mode, double max1, double max2, double max3) {
        g.colorMode(mode, max1, max2, max3);
    }
    
    public void colorMode(int mode, double max1, double max2, double max3, double maxA) {
        g.colorMode(mode, max1, max2, max3, maxA);
    }

    public void fill(int gray) {
        g.fill(gray);
    }

    public void fill(int gray, int alpha) {
        g.fill(gray, alpha);
    }
    
    public void fill(int v1, int v2, int v3) {
        g.fill(v1, v2, v3);
    }
    
    public void fill(int v1, int v2, int v3, int alpha) {
        g.fill(v1, v2, v3, alpha);
    }

    public void noFill() {
        g.noFill();
    }

    public void noStroke() {
        g.noStroke();
    }

    public void stroke(int gray) {
        g.stroke(gray);
    }

    public void stroke(int gray, int alpha) {
        g.stroke(gray, alpha);
    }
    
    public void stroke(int v1, int v2, int v3) {
        g.stroke(v1, v2, v3);
    }
    
    public void stroke(int v1, int v2, int v3, int alpha) {
        g.stroke(v1, v2, v3, alpha);
    }

    public double mouseX, mouseY, pmouseX, pmouseY;
    public boolean mousePressed;

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
        updateMouse(event);
        mouseClicked();
    }

    public void handleMouseDragged(MouseEvent event) {
        mouseDragged();
    }

    public void handleMouseMoved(MouseEvent event) {
        updateMouse(event);
        mousedMoved();
    }

    public void handleMousePressed(MouseEvent event) {
        updateMouse(event);
        mousePressed();
    }

    public void handleMouseReleased(MouseEvent event) {
        updateMouse(event);
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

    public void updateMouse(MouseEvent event) {
        pmouseX = mouseX;
        pmouseY = mouseY;

        mouseX = g.clamp((int)(event.getSceneX()-g.offsetX), 0, g.width);
        mouseY = g.clamp((int)(event.getSceneY()-g.offsetY), 0, g.height);
    }

}