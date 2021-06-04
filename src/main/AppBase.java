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