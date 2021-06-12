package main;

import java.util.ArrayList;
import java.util.Calendar;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import ptypes.PSurface;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import utils.Colours;
import utils.Constants;
import utils.Maths;

import static utils.Maths.*;
import static utils.Constants.*;

@SuppressWarnings("unused")
public class AppBase {

    private GraphicsContext gc;
    private Calendar date;
    private boolean looping;

    public PSurface surface;

    private boolean hasFill;
    private boolean hasStroke;

    public int rectMode;
    public int ellipseMode;
    public int colorMode;

    public int width;
    public int height;

    // Constructor for the object and the internal variables
    public AppBase(GraphicsContext gc) {
        this.gc = gc;
        this.date = new Calendar.Builder().build();
        this.surface = new PSurface();
        resetSurface();
        defaultSettings();
    }

    // Initial internal variables in Processing
    private void defaultSettings() {
        updateTime();
        size(100, 100);
        colorMode(RGB, 255, 255, 255, 255);
        background(204);
        fill(255);
        stroke(0);
        strokeWeight(1);
        looping = true;
        rectMode = CORNER;
        ellipseMode = CENTER;
    }

    // Structure

    public void draw() {}

    public void exit() {
        Loader.launchHomeScreen();
    }

    public void loop() {
        looping = true;
    }

    public void noLoop() {
        looping = false;
    }

    public void pop() {
        gc.restore();
    }

    public void popStyle() {
        
    }

    public void push() {
        gc.save();
    }

    public void pushStyle() {

    }

    public void redraw() {
        render();
    }

    public void setup() {}

    public void thread(String name) {

    }

    // 2D Primatives

    public void arc(double x, double y, double width, double height, double start, double stop) {
        double degStart = -degrees(start);
        double degStop = -degrees(stop);
        degStop -= degStart;

        double nx = Constants.offsetW() + x - width/2;
        double ny = Constants.offsetH() + y - height/2;

        if (hasFill) gc.fillArc(nx, ny, width, height, degStart, degStop, ArcType.ROUND);
        if (hasStroke) gc.strokeArc(nx, ny, width, height, degStart, degStop, ArcType.OPEN);
    }

    public void arc(double x, double y, double width, double height, double start, double stop, int mode) {
        clamp(mode, OPEN, PIE);
        ArcType arcMode = ArcType.OPEN;
        switch (mode) {
            case OPEN: {
                arcMode = ArcType.OPEN;
                break;
            }
            case CHORD: {
                arcMode = ArcType.CHORD;
                break;
            }
            case PIE: {
                arcMode = ArcType.ROUND;
                break;
            }
        }
        double degStart = -degrees(start);
        double degStop = -degrees(stop);
        degStop -= degStart;

        double nx = Constants.offsetW() + x - width/2;
        double ny = Constants.offsetH() + y - height/2;

        if (hasFill) gc.fillArc(nx, ny, width, height, degStart, degStop, arcMode);
        if (hasStroke) gc.strokeArc(nx, ny, width, height, degStart, degStop, arcMode);
    }

    public void circle(double x, double y, double s) {
        ellipse(x, y, s, s);
    }

    public void ellipse(double x, double y, double w, double h) {

        double nx = x;
        double ny = y;
        double nw = w;
        double nh = h;

        switch (ellipseMode) {
            case CORNER: {
                nw *= 2;
                nh *= 2;
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
                break;
            }
        }

        nx += Constants.offsetW();
        ny += Constants.offsetH();

        if (hasFill) gc.fillOval(nx, ny, nw, nh);
        if (hasStroke) gc.strokeOval(nx, ny, nw, nh);
    }

    public void line(double startX, double startY, double endX, double endY) {
        double sx = Constants.offsetW() + startX;
        double sy = Constants.offsetH() + startY;
        double ex = Constants.offsetW() + endX;
        double ey = Constants.offsetH() + endY;
        
        gc.strokeLine(sx, sy, ex, ey);
    }

    public void point(double x, double y) {
        rect(x, y, 1, 1);
    }

    public void quad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double[] xPoints = {x1, x2, x3, x4};
        for (int i = 0; i < 4; i++) xPoints[i] += Constants.offsetW();
        double[] yPoints = {y1, y2, y3, y4};
        for (int i = 0; i < 4; i++) yPoints[i] += Constants.offsetH();
        if (hasFill) gc.fillPolygon(xPoints, yPoints, 4);
        if (hasStroke) gc.strokePolygon(xPoints, yPoints, 4);
    }
    
    public void rect(double x, double y, double w, double h) {

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

        nx += Constants.offsetW();
        ny += Constants.offsetH();

        if (hasFill) gc.fillRect(nx, ny, nw, nh);
        if (hasStroke) gc.strokeRect(nx, ny, nw, nh);
    }

    public void triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        double[] xPoints = {x1, x2, x3};
        for (int i = 0; i < 3; i++) xPoints[i] += Constants.offsetW();
        double[] yPoints = {y1, y2, y3};
        for (int i = 0; i < 3; i++) yPoints[i] += Constants.offsetH();
        if (hasFill) gc.fillPolygon(xPoints, yPoints, 3);
        if (hasStroke) gc.strokePolygon(xPoints, yPoints, 3);
    }

    // Color // Setting

    public void background(double gray) {
        background(gray, gray, gray, (int)maxAO);
    }

    public void background(double gray, double alpha) {
        background(gray, gray, gray, alpha);
    }

    public void background(double rh, double gs, double bv) {
        background(rh, gs, bv, (int)maxAO);
    }

    public void background(double rh, double gs, double bv, double ao) {
        double mappedRH = clamp(map(rh, 0, maxRH, 0, 255), 0, 255);
        double mappedGS = clamp(map(gs, 0, maxGS, 0, 255), 0, 255);
        double mappedBV = clamp(map(bv, 0, maxBB, 0, 255), 0, 255);
        double mappedAO = clamp(map(ao, 0, (int)maxAO, 0, 255), 0, 255);
        setBackground(Colours.encodeColour(mappedRH, mappedGS, mappedBV, mappedAO));
    }

    private void setBackground(int encodedValue) {
        gc.save();
        gc.setFill(Colours.decodeColour(colorMode, encodedValue));
        gc.fillRect(Constants.offsetW(), Constants.offsetH(), width, height);
        gc.restore();
    }

    private double maxRH;
    private double maxGS;
    private double maxBB;
    private double maxAO;

    public void colorMode(int mode) {
        colorMode(mode, (int)maxRH, (int)maxGS, (int)maxBB, (int)maxAO);
    }

    public void colorMode(int mode, double max) {
        colorMode(mode, max, max, max, (int)maxAO);
    }

    public void colorMode(int mode, double rh, double gs, double bb) {
        colorMode(mode, rh, gs, bb, (int)maxAO);
    }

    public void colorMode(int mode, double rh, double gs, double bb, double alpha) {
        colorMode = mode;
        maxRH = rh;
        maxGS = gs;
        maxBB = bb;
        maxAO = alpha;
    }

    public void fill(double gray) {
        fill(gray, gray, gray, (int)maxAO);
    }

    public void fill(double gray, double alpha) {
        fill(gray, gray, gray, alpha);
    }

    public void fill(double rh, double gs, double bv) {
        fill(rh, gs, bv, (int)maxAO);
    }

    public void fill(double rh, double gs, double bv, double ao) {
        hasFill = true;
        double mappedRH = clamp(map(rh, 0, maxRH, 0, 255), 0, 255);
        double mappedGS = clamp(map(gs, 0, maxGS, 0, 255), 0, 255);
        double mappedBV = clamp(map(bv, 0, maxBB, 0, 255), 0, 255);
        double mappedAO = clamp(map(ao, 0, (int)maxAO, 0, 255), 0, 255);
        setFill(Colours.encodeColour(mappedRH, mappedGS, mappedBV, mappedAO));
    }

    private void setFill(int encodedValue) {
        gc.setFill(Colours.decodeColour(colorMode, encodedValue));
    }

    public void noFill() {
        hasFill = false;
    }

    public void noStroke() {
        hasStroke = false;
    }

    public void stroke(double gray) {
        stroke(gray, gray, gray, (int)maxAO);
    }

    public void stroke(double gray, double alpha) {
        stroke(gray, gray, gray, alpha);
    }

    public void stroke(double rh, double gs, double bv) {
        stroke(rh, gs, bv, (int)maxAO);
    }

    public void stroke(double rh, double gs, double bv, double ao) {
        hasStroke = true;
        double mappedRH = clamp(map(rh, 0, maxRH, 0, 255), 0, 255);
        double mappedGS = clamp(map(gs, 0, maxGS, 0, 255), 0, 255);
        double mappedBV = clamp(map(bv, 0, maxBB, 0, 255), 0, 255);
        double mappedAO = clamp(map(ao, 0, (int)maxAO, 0, 255), 0, 255);
        setStroke(Colours.encodeColour(mappedRH, mappedGS, mappedBV, mappedAO));
    }

    private void setStroke(int encodedValue) {
        gc.setStroke(Colours.decodeColour(colorMode, encodedValue));
    }
































































    private void resetSurface() {
        //surface.setLocation((Constants.displayW() - Constants.windowW()) / 2, (Constants.displayH() - Constants.windowH()) / 2);
        //surface.setTitle(Main.title);
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
        if (inside(event.getSceneX(), event.getSceneY(), Constants.offsetW(), Constants.offsetH(), width, height)) {
            pmouseX = mouseX;
            pmouseY = mouseY;
    
            mouseX = Maths.clamp((int)(event.getSceneX()-Constants.offsetW()), 0, width);
            mouseY = Maths.clamp((int)(event.getSceneY()-Constants.offsetH()), 0, height);
        }
    }

    public char key;
    public KeyCode keyCode;
    public boolean keyPressed;
    ArrayList<KeyCode> pressedKeys = new ArrayList<>(20);

    public void settings() {}
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
        render();
    }

    public void handleDraw() {
        updateTime();
        if (looping) {
            render();
        }
    }

    private void render() {
        draw();
        coverEdges();
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

    public void text(String text, double x, double y) {
        gc.fillText(text, x, y);
    }

    

    

    // More Stuff






















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

    

    // Fill

    

    // Stroke

    

    // Shapes

    


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