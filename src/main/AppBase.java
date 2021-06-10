package main;

import java.util.ArrayList;
import java.util.Calendar;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import utils.Colours;
import utils.Constants;
import utils.Maths;

import static utils.Constants.*;

@SuppressWarnings("unused")
public class AppBase {

    private GraphicsContext gc;

    public int width;
    public int height;

    private boolean looping;
    private boolean redraw;

    public int rectMode;
    public int ellipseMode;
    public int colorMode;

    public AppBase(GraphicsContext gc) {
        this.gc = gc;
        defaultSettings();
    }

    private void defaultSettings() {
        updateTime();
        size(100, 100);
        background(204);
        looping = true;
        rectMode = CORNER;
        ellipseMode = CENTER;
    }

    /////////////////////////////
    // Handled Default Methods //
    /////////////////////////////

    public void rectMode(int mode) {
        rectMode = mode;
    }

    public void ellipseMode(int mode) {
        ellipseMode = mode;
    }

    public double mouseX, mouseY, pmouseX, pmouseY;

    public void updateMouse(javafx.scene.input.MouseEvent event) {
        pmouseX = mouseX;
        pmouseY = mouseY;

        mouseX = Maths.clamp((int)(event.getSceneX()-Constants.offsetW), 0, width);
        mouseY = Maths.clamp((int)(event.getSceneY()-Constants.offsetH), 0, height);
    }

    public char key;
    public KeyCode keyCode;
    public boolean keyPressed;
    ArrayList<KeyCode> pressedKeys = new ArrayList<>(20);

    public void settings() {}
    public void setup() {}
    public void draw() {}
    public void mouseClicked() {}
    public void mouseDragged() {}
    public void mousedMoved() {}
    public void mousePressed() {}
    public void mouseReleased() {}
    //spublic void mouseWheel(MouseEvent event) {}
    public void keyPressed() {}
    public void keyReleased() {}
    public void keyTyped() {}

    public void handleSettings() {
        settings();
    }

    public void handleSetup() {
        setup();
    }

    public void handleDraw() {
        updateTime();
        if (looping) {
            draw();
            coverEdges();
        }
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

    public void handleMouseWheel(javafx.scene.input.ScrollEvent scrollEvent) {
        int count = (int) -(scrollEvent.getDeltaY() / scrollEvent.getMultiplierY());
        //MouseEvent event = new MouseEvent(count);
        //mouseWheel(event);
    }

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

    private void updateKeys(KeyEvent event) {
        keyCode = event.getCode();
        key = event.getCharacter().charAt(0);
    }

    ///////////////
    // Structure //
    ///////////////

    public void exit() {
        Loader.launchHomeScreen();
    }

    public void loop() {
        if (!looping) looping = true;
    }

    public void noLoop() {
        if (looping) looping = false;
    }

    public void redraw() {
        handleDraw();
    }

    /////////////////
    // Environment //
    /////////////////

    private TextAlignment alignH = TextAlignment.LEFT;
    private VPos alignV = VPos.BASELINE;

    public void textAlign(int newAlignX) {

        switch (newAlignX) {
            case LEFT: {
                alignH = TextAlignment.LEFT;
                break;
            }
            case CENTER: {
                alignH = TextAlignment.CENTER;
                break;
            }
            case RIGHT: {
                alignH = TextAlignment.RIGHT;
                break;
            }
        }

        textAlign(alignH, alignV);
    }

    public void textAlign(int newAlignX, int newAlignY) {
        textAlign(newAlignX);

        switch (newAlignY) {
            case TOP: {
                alignV = VPos.TOP;
                break;
            }
            case BOTTOM: {
                alignV = VPos.BOTTOM;
                break;
            }
            case CENTER: {
                alignV = VPos.CENTER;
                break;
            }
            case BASELINE: {
                alignV = VPos.BASELINE;
                break;
            }
        }

        textAlign(alignH, alignV);
    }

    public void textAlign(TextAlignment alignH, VPos alignV) {
        gc.setTextAlign(alignH);
        gc.setTextBaseline(alignV);
    }

    public void strokeWeight(double weight) {
        gc.setLineWidth(weight);
    }

    public void textSize(double size) {
        gc.setFont(new Font(size));
    }

    public void text(String text, int x, int y) {
        gc.fillText(text, x, y);
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



    public void rectImpl(double x, double y, double w, double h) {

        double nx = x;
        double ny = y;
        double nw = w;
        double nh = h;

        switch (rectMode) {
            case CORNERS: {
                nw /= 2;
                nh /= 2;
                break;
            }
            case RADIUS: {
                nw *= 2;
                nh *= 2;
                nx -= nw / 2;
                ny -= nh / 2;
                break;
            }
            case CENTER: {
                nx -= nw / 2;
                ny -= nh / 2;
            }
        }

        if (hasFill) gc.fillRect(Constants.offsetW() + nx, Constants.offsetH() + ny, nw, nh);
        if (hasStroke) gc.strokeRect(Constants.offsetW() + nx, Constants.offsetH() + ny, nw, nh);
    }

    Calendar date;

    private void updateTime() {
        date.setTimeInMillis(System.currentTimeMillis());
    }

    public int day() {
        return date.get(Calendar.DAY_OF_MONTH);
    }

    public int hour() {
        return date.get(Calendar.HOUR_OF_DAY);
    }

    public int millis() {
        return date.get(Calendar.MILLISECOND);
    }

    public int minute() {
        return date.get(Calendar.MINUTE);
    }

    public int month() {
        return date.get(Calendar.MONTH) + 1;
    }

    public int second() {
        return date.get(Calendar.SECOND);
    }

    public int year() {
        return date.get(Calendar.YEAR);
    }

    // Other

    private void coverEdges() {
        gc.save();
        fill(20);
        gc.fillRect(0, 0, Constants.screenW(), Constants.offsetH());                            // Top
        gc.fillRect(0, Constants.offsetH() + height, Constants.screenW(), Constants.offsetH()); // Bottom
        gc.fillRect(0, 0, Constants.offsetW(), Constants.screenH());                            // Left
        gc.fillRect(Constants.offsetW() + width, 0, Constants.offsetW(), Constants.screenH());  // Right
        gc.restore();
    }

}