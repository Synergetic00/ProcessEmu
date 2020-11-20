package main;

import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import misc.*;

import static utils.Constants.*;
import static utils.MathUtils.*;

import event.MouseEvent;

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
        g.size(100,100);
    }

    public PGraphics createGraphics(int w, int h) {
        PGraphics newPGraphics = new PGraphics(gc, this);
        newPGraphics.isPrimary = false;
        newPGraphics.size(w, h);
        return newPGraphics;
    }

    public void size(int w, int h) {
        g.size(w, h);
        width = g.gs.width;
        height = g.gs.height;
        g.background(204);
    }
    
    public void image(PGraphics pg, double x, double y) {
        pg.render(x, y);
    }
    
    public void image(PGraphics pg, double x, double y, double w, double h) {
        pg.render(x, y, w, h);
    }

    public void rect(double x, double y, double w, double h) {
        g.rect(x, y, w, h);
    }

    public void ellipse(double x, double y, double w, double h) {
        g.ellipse(x, y, w, h);
    }

    public void background(Color color) {
        g.background(color);
    }

    public void background(double gray) {
        g.background(gray);
    }

    public void background(double gray, double alpha) {
        g.background(gray, alpha);
    }

    public void background(double rh, double gs, double bb) {
        g.background(rh, gs, bb);
    }

    public void background(double rh, double gs, double bb, double alpha) {
        g.background(rh, gs, bb, alpha);
    }

    public void noFill() {
        g.noFill();
    }

    public void fill(Color color) {
        g.fill(color);
    }

    public void fill(double gray) {
        g.fill(gray, gray, gray, (int)g.maxAL);
    }

    public void fill(double gray, double alpha) {
        g.fill(gray, gray, gray, alpha);
    }

    public void fill(double rh, double gs, double bb) {
        g.fill(rh, gs, bb, (int)g.maxAL);
    }

    public void fill(double rh, double gs, double bb, double alpha) {
        g.fill(rh, gs, bb, alpha);
    }

    public void noStroke() {
        g.noStroke();
    }

    public void stroke(Color color) {
        g.stroke(color);
    }

    public void stroke(double gray) {
        g.stroke(gray);
    }

    public void stroke(double gray, double alpha) {
        g.stroke(gray, alpha);
    }

    public void stroke(double rh, double gs, double bb) {
        g.stroke(rh, gs, bb);
    }

    public void stroke(double rh, double gs, double bb, double alpha) {
        g.stroke(rh, gs, bb, alpha);
    }

    public void textAlign(int newAlignX) {
        g.textAlign(newAlignX);
    }

    public void textAlign(int newAlignX, int newAlignY) {
        g.textAlign(newAlignX, newAlignY);
    }

    public void textSize(double newSize) {
        g.textSize(newSize);
    }

    public void text(int value, double x, double y) {
        g.text(value, x, y);
    }

    public void text(double value, double x, double y) {
        g.text(value, x, y);
    }

    public void text(String value, double x, double y) {
        g.text(value, x, y);
    }

    public void strokeWeight(double weight) {
        g.strokeWeight(weight);
    }

    public void line(double startX, double startY, double endX, double endY) {
        g.line(startX, startY, endX, endY);
    }

    public void triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        g.triangle(x1, y1, x2, y2, x3, y3);
    }

    public void beginShape() {
        g.beginShape(CLOSE);
    }

    public void beginShape(int kind) {
        g.beginShape(kind);
    }

    public void endShape() {
        g.endShape();
    }

    public void endShape(int mode) {
        g.endShape(mode);
    }

    public void vertex(double x, double y) {
        g.vertex(x, y);
    }

    public void colorMode(int mode) {
        g.colorMode(mode);
    }

    public void colorMode(int mode, double max) {
        g.colorMode(mode, max);
    }

    public void colorMode(int mode, double rh, double gs, double bb) {
        g.colorMode(mode, rh, gs, bb);
    }

    public void colorMode(int mode, double rh, double gs, double bb, double alpha) {
        g.colorMode(mode, rh, gs, bb, alpha);
    }

    public int color(double rh, double gs, double bb) {
        return 0;
    }

    public void pushMatrix() {
        g.pushMatrix();
    }

    public void popMatrix() {
        g.popMatrix();
    }

    public void ellipseMode(int mode) {
        g.ellipseMode(mode);
    }

    public void rectMode(int mode) {
        g.rectMode(mode);
    }
    
    public void scale(double amt) {
        g.scale(amt);
    }
    
    public void scale(double amtX, double amtY) {
        g.scale(amtX, amtY);
    }

    public void translate(double amtX, double amtY) {
        g.translate(amtX, amtY);
    }

    public void frameRate(int fps) {
        // What the shit, not atm, thanks
    }























    public double mouseX, mouseY, pmouseX, pmouseY;

    public void updateMouse(javafx.scene.input.MouseEvent event) {
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
        updateMouse(event);
        mouseClicked();
    }

    public void handleMouseDragged(javafx.scene.input.MouseEvent event) {
        mouseDragged();
    }

    public void handleMouseMoved(javafx.scene.input.MouseEvent event) {
        updateMouse(event);
        mousedMoved();
    }

    public void handleMousePressed(javafx.scene.input.MouseEvent event) {
        updateMouse(event);
        mousePressed();
    }

    public void handleMouseReleased(javafx.scene.input.MouseEvent event) {
        updateMouse(event);
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