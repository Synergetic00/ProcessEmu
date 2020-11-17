package main;

import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import misc.*;
import utils.*;

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

    public color color(int r, int g, int b) {
        return color.getColour(r, g, b);
    }

    public void background(int gray) {
        
    }

    public void background(int gray, int alpha) {

    }

    public void background(int red, int green, int blue) {

    }

    public void background(int red, int green, int blue, int alpha) {

    }

    public void fill(int gray) {
        fill(gray, gray, gray, (int)g.maxAL);
    }

    public void fill(int gray, int alpha) {
        fill(gray, gray, gray, alpha);
    }

    public void fill(int rh, int gs, int bb) {
        fill(rh, gs, bb, (int)g.maxAL);
    }

    public void fill(int rh, int gs, int bb, int alpha) {
        g.fill(rh, gs, bb, alpha);
    }

    public void stroke(int gray) {

    }

    public void stroke(int gray, int alpha) {

    }

    public void stroke(int red, int green, int blue) {

    }

    public void stroke(int red, int green, int blue, int alpha) {

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