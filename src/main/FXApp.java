package main;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.*;
import javafx.scene.input.*;
import utils.PGraphics;

public class FXApp {

    GraphicsContext gc;
    PGraphics g;

    public int width, height;

    public FXApp(GraphicsContext gc) {
        this.gc = gc;
        g = new PGraphics(gc, this);
    }

    public void setOffset(double x, double y) {
        g.setOffset(x, y);
    }

    // [Structure]

    // [Environment]

    ////// fullScreen()

    public void fullScreen() {
        g.fullScreen();
    }

    // [Data]

    // [Control]

    // [Shape]

    public void strokeWeight(int w) {
        g.strokeWeight(w);
    }

    // [Input]

    public int day() {
        return g.day();
    }

    public int hour() {
        return g.hour();
    }

    public int millis() {
        return g.millis();
    }

    public int minute() {
        return g.minute();
    }

    public int month() {
        return g.month();
    }

    ////// second()

    public int second() {
        return g.second();
    }

    public int year() {
        return g.year();
    }

    // [Output]

    // [Transform]

    // [Lights, Camera]

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

    // [Image]

    // [Rendering]

    // [Typography]

    ////// text()

    public void text(int textVar, double x, double y) {
        g.text(textVar, x, y);
    }

    public void text(double textVar, double x, double y) {
        g.text(textVar, x, y);
    }

    public void text(String text, double x, double y) {
        g.text(text, x, y);
    }

    public void textAlign(int newAlignX) {
        g.textAlign(newAlignX);
    }

    public void textAlign(int newAlignX, int newAlignY) {
        g.textAlign(newAlignX, newAlignY);
    }

    public void textSize(double size) {
        g.textSize(size);
    }

    // Unsorted Functions










    public void size(int width, int height) {
        g.size(width, height);
    }

    public void arc(double x, double y, double width, double height, double start, double stop) {
        g.arc(x, y, width, height, start, stop);
    }

    public void arc(double x, double y, double width, double height, double start, double stop, int mode) {
        g.arc(x, y, width, height, start, stop, mode);
    }

    public void circle(double x, double y, double size) {
        g.circle(x, y, size);
    }
    
    public void ellipse(double x, double y, double width, double height) {
        g.ellipse(x, y, width, height);
    }
    
    public void line(double x1, double y1, double x2, double y2) {
        g.line(x1, y1, x2, y2);
    }

    public void point(double x, double y) {
        g.point(x, y);
    }

    public void quad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        g.quad(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    public void rect(double x, double y, double width, double height) {
        g.rect(x, y, width, height);
    }

    public void square(double x, double y, double size) {
        g.square(x, y, size);
    }

    public void triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        g.triangle(x1, y1, x2, y2, x3, y3);
    }

    public void rectMode(int newMode) {
        g.rectMode(newMode);
    }

    public void ellipseMode(int newMode) {
        g.ellipseMode(newMode);
    }

    public void popMatrix() {
        g.popMatrix();
    }

    public void pushMatrix() {
        g.pushMatrix();
    }

    public void resetMatrix() {
        g.resetMatrix();
    }

    public void rotate(double angle) {
        g.rotate(angle);
    }

    public void scale(double s) {
        g.scale(s);
    }

    public void scale(double x, double y) {
        g.scale(x, y);
    }

    public void shearX(double angle) {
        g.shearX(angle);

    }

    public void shearY(double angle) {
        g.shearY(angle);

    }

    public void translate(double x, double y) {
        g.translate(x, y);
    }

    public void updateTime() {
        g.updateTime();
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
    }public double mouseX, mouseY, pmouseX, pmouseY;
    public boolean mousePressed;

    public void updateMouse(MouseEvent event) {
        pmouseX = mouseX;
        pmouseY = mouseY;

        mouseX = g.clamp((int)(event.getSceneX()-g.offsetX), 0, width);
        mouseY = g.clamp((int)(event.getSceneY()-g.offsetY), 0, width);
    }

    public void handleMouseClicked(MouseEvent event) {
        updateMouse(event);
        mousePressed = true;
        mouseClicked();
    }

    public void mouseClicked() {}

    public void handleMouseDragged(MouseEvent event) {
        mouseDragged();
    }

    public void mouseDragged() {}

    public void handleMouseMoved(MouseEvent event) {
        updateMouse(event);
        mousedMoved();
    }

    public void mousedMoved() {}

    public void handleMousePressed(MouseEvent event) {
        updateMouse(event);
        mousePressed();
    }

    public void mousePressed() {}

    public void handleMouseReleased(MouseEvent event) {
        updateMouse(event);
        mousePressed = false;
        mouseReleased();
    }

    public void mouseReleased() {}

    public void handleMouseWheel(ScrollEvent event) {
        mouseWheel(event);
    }

    public void mouseWheel(ScrollEvent event) {}

    //////////////////////
    // Input / Keyboard //
    //////////////////////

    public void keyPressed() {}
    public void keyReleased() {}
    public void keyTyped() {}

    public char key;
    public KeyCode keyCode;
    public boolean keyPressed;
    List<KeyCode> pressedKeys = new ArrayList<>(20);

    public void handleKeyPressed(KeyEvent event) {
        updateKeys(event);
        if (!pressedKeys.contains(keyCode)) pressedKeys.add(keyCode);
        keyPressed = true;
        keyPressed();
    }

    public void handleKeyReleased(KeyEvent event) {
        updateKeys(event);
        pressedKeys.remove(keyCode);
        keyPressed = !pressedKeys.isEmpty();
        keyReleased();
    }

    public void handleKeyTyped(KeyEvent event) {
        updateKeys(event);
        keyTyped();
    }

    void updateKeys(KeyEvent event) {
        keyCode = event.getCode();
        key = event.getCharacter().charAt(0);
    }


}