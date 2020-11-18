package main;

import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import misc.*;

import static utils.MathUtils.*;

public class FXApp {

    GraphicsContext gc;
    public PGraphics g;

    double screenW, screenH;
    public int width, height;

    public FXApp(GraphicsContext gc) {
        this.gc = gc;
        screenW = gc.getCanvas().getWidth();
        screenH = gc.getCanvas().getHeight();

        gc.save();
        gc.setFill(Color.rgb(40, 40, 40));
        gc.fillRect(0, 0, screenW, screenH);
        gc.restore();

        g = new PGraphics(gc, this);
        g.isPrimary = true;
    }

    public PGraphics createGraphics(int w, int h) {
        PGraphics newPGraphics = new PGraphics(gc, this);
        newPGraphics.isPrimary = false;
        newPGraphics.size(w, h);
        return newPGraphics;
    }

    public void size(int w, int h) {
        g.size(w, h);
    }

    public void background(int gray) {
        g.background(gray);
    }

    public void background(int gray, int alpha) {
        g.background(gray, alpha);
    }

    public void background(int rh, int gs, int bb) {
        g.background(rh, gs, bb);
    }

    public void background(int rh, int gs, int bb, int alpha) {
        g.background(rh, gs, bb, alpha);
    }

    public void fill(int gray) {
        g.fill(gray, gray, gray, (int)g.maxAL);
    }

    public void fill(int gray, int alpha) {
        g.fill(gray, gray, gray, alpha);
    }

    public void fill(int rh, int gs, int bb) {
        g.fill(rh, gs, bb, (int)g.maxAL);
    }

    public void fill(int rh, int gs, int bb, int alpha) {
        g.fill(rh, gs, bb, alpha);
    }

    public void stroke(int gray) {
        stroke(gray, gray, gray, (int)g.maxAL);
    }

    public void stroke(int gray, int alpha) {
        stroke(gray, gray, gray, alpha);
    }

    public void stroke(int rh, int gs, int bb) {
        stroke(rh, gs, bb, (int)g.maxAL);
    }

    public void stroke(int rh, int gs, int bb, int alpha) {
        g.stroke(rh, gs, bb, alpha);
    }

    public void rect(double x, double y, double w, double h) {
        g.rect(x, y, w, h);
    }
    
    public void image(PGraphics pg, double x, double y) {
        pg.render(x, y);
    }





























    public double mouseX, mouseY, pmouseX, pmouseY;

    public void updateMouse(MouseEvent event) {
        pmouseX = mouseX;
        pmouseY = mouseY;

        mouseX = clamp((int)(event.getSceneX()-GraphicState.offsetX), 0, g.gs.width);
        mouseY = clamp((int)(event.getSceneY()-GraphicState.offsetY), 0, g.gs.height);
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

}