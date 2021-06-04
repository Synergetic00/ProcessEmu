package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyEvent;
import utils.Colours;
import utils.Constants;
import utils.Maths;

@SuppressWarnings("unused")
public class AppBase {

    private GraphicsContext gc;

    private int width;
    private int height;

    public AppBase(GraphicsContext gc) {
        this.gc = gc;
        fullScreen();
        background(10);
        size(100, 100);
    }

    public void size(int width, int height) {
        this.width = width;
        this.height = height;

        Constants.offsetW((Constants.screenW() - width) / 2);
        Constants.offsetH((Constants.screenH() - height) / 2);
    }

    public void fullScreen() {
        size(Constants.screenW(), Constants.screenH());
    }

    // Background

    public void background(int gray) {
        background(gray, gray, gray, 255);
    }

    public void background(int gray, int alpha) {
        background(gray, gray, gray, alpha);
    }

    public void background(int rh, int gs, int bv) {
        background(rh, gs, bv, 255);
    }

    public void background(int rh, int gs, int bv, int ao) {
        setBackground(Colours.encodeColour(rh, gs, bv, ao));
    }

    private void setBackground(int encodedValue) {
        gc.save();
        gc.setFill(Colours.decodeColour(encodedValue));
        gc.fillRect(Constants.offsetW(), Constants.offsetH(), width, height);
        gc.restore();
    }

    // Fill

    private boolean hasFill;

    public void noFill() {
        hasFill = false;
    }

    public void fill(int gray) {
        fill(gray, gray, gray, 255);
    }

    public void fill(int gray, int alpha) {
        fill(gray, gray, gray, alpha);
    }

    public void fill(int rh, int gs, int bv) {
        fill(rh, gs, bv, 255);
    }

    public void fill(int rh, int gs, int bv, int ao) {
        hasFill = true;
        setFill(Colours.encodeColour(rh, gs, bv, ao));
    }

    private void setFill(int encodedValue) {
        gc.setFill(Colours.decodeColour(encodedValue));
    }

    // Stroke

    private boolean hasStroke;

    public void noStroke() {
        hasStroke = false;
    }

    public void stroke(int gray) {
        stroke(gray, gray, gray, 255);
    }

    public void stroke(int gray, int alpha) {
        stroke(gray, gray, gray, alpha);
    }

    public void stroke(int rh, int gs, int bv) {
        stroke(rh, gs, bv, 255);
    }

    public void stroke(int rh, int gs, int bv, int ao) {
        hasStroke = true;
        setStroke(Colours.encodeColour(rh, gs, bv, ao));
    }

    private void setStroke(int encodedValue) {
        gc.setStroke(Colours.decodeColour(encodedValue));
    }

    // Shapes

    public void rect(int x, int y, int w, int h) {
        if (hasFill) gc.fillRect(Constants.offsetW() + x, Constants.offsetH() + y, w, h);
        if (hasStroke) gc.strokeRect(Constants.offsetW() + x, Constants.offsetH() + y, w, h);
    }

    // Other

    public void exit() {
        Loader.launchHomeScreen();
    }

    public void handleSettings() {
        settings();
    }

    public void handleSetup() {
        setup();
    }

    public void handleDraw() {
        draw();
        coverEdges();
    }

    private void coverEdges() {
        gc.save();
        fill(20);
        gc.fillRect(0, 0, Constants.screenW(), Constants.offsetH());                            // Top
        gc.fillRect(0, Constants.offsetH() + height, Constants.screenW(), Constants.offsetH()); // Bottom
        gc.fillRect(0, 0, Constants.offsetW(), Constants.screenH());                            // Left
        gc.fillRect(Constants.offsetW() + width, 0, Constants.offsetW(), Constants.screenH());  // Right
        gc.restore();
    }

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
    
    public void handleKeyPressed(KeyEvent event) {
        keyPressed();
    }

}