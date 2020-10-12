package main;

//import utils.PVector;
//import static utils.PVector.*;
import static utils.FXUtils.*;

import java.util.*;

import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;

public class FXApp {

    protected FXApp(GraphicsContext gc){
        this.gc = gc;

        date = new Calendar.Builder().build();
        startX = 0; startY = 0;
        keyPressed = false;
        trueWidth = gc.getCanvas().getWidth();
        trueHeight = gc.getCanvas().getHeight();

        defaultSettings();
    }

    protected void defaultSettings() {
        stroke(0);
        fill(255);
        size(100,100);
    }

    ///////////////
    // Structure //
    ///////////////

    public void handleSetup() {
        setup();
    }

    public void setup() {}

    public void handleDraw() {
        updateTime();
        draw();
        //rect(-startX, -startY, (trueWidth-width)/2, trueHeight);
        //rect(trueWidth-startX, -startY, (trueWidth-width)/2, trueHeight);
        //rect(-startX, -startY, trueWidth, (trueHeight-height)/2);
        //rect(-startX, trueHeight-startY, trueWidth, (trueHeight-height)/2);
    }

    public void draw() {}

    /////////////////
    // Environment //
    /////////////////

    ///////////////////////
    // Data / Conversion //
    ///////////////////////

    // binary()

    // boolean()

    // byte()

    // char()

    // float()
    
    // hex()

    // int()
    
    // str()

    // unbinary()

    // unhex()

    ///////////
    // Shape //
    ///////////

    ///////////////////////////
    // Shape / 2D Primitives //
    ///////////////////////////

    // arc()

    public void arc(double x, double y, double width, double height, double start, double stop) {
        double degStart = -degrees(start);
        double degStop = -degrees(stop);
        degStop -= degStart;

        double nx = x - width/2;
        double ny = y - height/2;

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

        double nx = x - width/2;
        double ny = y - height/2;

        if (hasFill) gc.fillArc(startX+nx, startY+ny, width, height, degStart, degStop, arcMode);
        if (hasStroke) gc.strokeArc(startX+nx, startY+ny, width, height, degStart, degStop, arcMode);
    }

    // circle()

    public void circle(double x, double y, double size) {
        ellipse(x, y, size, size);
    }

    // ellipse()
    
    public void ellipse(double x, double y, double width, double height) {
        double nx = x, ny = y, nwidth = width, nheight = height;
        switch (ellipseMode) {
            case CORNER: {
                nwidth *= 2;
                nheight *= 2;
                break;
            }
            case CORNERS: {
                break;
            }
            case RADIUS: {
                nwidth *= 2;
                nheight *= 2;
                nx -= nwidth / 2;
                ny -= nheight / 2;
                break;
            }
            case CENTER: {
                nx -= nwidth / 2;
                ny -= nheight / 2;
                break;
            }
        }
        if (hasFill) gc.fillOval(startX+nx, startY+ny, nwidth, nheight);
        if (hasStroke) gc.strokeOval(startX+nx, startY+ny, nwidth, nheight);
    }

    // line()
    
    public void line(double x1, double y1, double x2, double y2) {
        gc.strokeLine(startX+x1, startY+y1, startX+x2, startY+y2);
    }

    // point() - maybe drawing the point using either circle() or square()
    public void point(double x, double y) {
        line(x, y, x, y);
    }

    // quad()

    public void quad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double[] xPoints = {startX+x1, startX+x2, startX+x3, startX+x4};
        double[] yPoints = {startY+y1, startY+y2, startY+y3, startY+y4};
        if (hasFill) gc.fillPolygon(xPoints, yPoints, 4);
        if (hasStroke) gc.strokePolygon(xPoints, yPoints, 4);
    }

    // rect()

    public void rect(double x, double y, double width, double height) {
        double nx = x, ny = y, nwidth = width, nheight = height;
        switch (rectMode) {
            case CORNER: {
                break;
            }
            case CORNERS: {
                nwidth /= 2;
                nheight /= 2;
                break;
            }
            case RADIUS: {
                nwidth *= 2;
                nheight *= 2;
                nx -= nwidth / 2;
                ny -= nheight / 2;
                break;
            }
            case CENTER: {
                nx -= nwidth / 2;
                ny -= nheight / 2;
                break;
            }
        }
        if (hasFill) gc.fillRect(startX+nx, startY+ny, nwidth, nheight);
        if (hasStroke) gc.strokeRect(startX+nx, startY+ny, nwidth, nheight);
    }

    // square()

    public void square(double x, double y, double size) {
        rect(x, y, size, size);
    }

    // triangle()

    public void triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        double[] xPoints = {startX+x1, startX+x2, startX+x3};
        double[] yPoints = {startY+y1, startY+y2, startY+y3};
        if (hasFill) gc.fillPolygon(xPoints, yPoints, 3);
        if (hasStroke) gc.strokePolygon(xPoints, yPoints, 3);
    }

    ///////////
    // Input //
    ///////////

    ///////////////////
    // Input / Mouse //
    ///////////////////
    
    public double mouseX, mouseY, pmouseX, pmouseY;
    public boolean mousePressed;

    public void updateMouse(MouseEvent event) {
        pmouseX = mouseX;
        pmouseY = mouseY;

        mouseX = clamp((int)(event.getSceneX()-startX), 0, width);
        mouseY = clamp((int)(event.getSceneY()-startY), 0, width);
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

    ///////////////////
    // Input / Files //
    ///////////////////

    /////////////////////////
    // Input / Time & Date //
    /////////////////////////

    Calendar date;

    public void updateTime() {
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

    ////////////
    // Output //
    ////////////

    ///////////////
    // Transform //
    ///////////////

    ////////////////////
    // Lights, Camera //
    ////////////////////

    ///////////
    // Color //
    ///////////

    /////////////////////
    // Color / Setting //
    /////////////////////

    ////////////////////////////////
    // Color / Creating & Reading //
    ////////////////////////////////
























    public int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public int rectMode = CORNER, ellipseMode = CENTER;

    public void rectMode(int newMode) {
        rectMode = clamp(newMode, CORNER, CENTER);
    }

    public void ellipseMode(int newMode) {
        ellipseMode = clamp(newMode, CORNER, CENTER);
    }

    GraphicsContext gc;

    public int width = 100, height = 100;
    double trueWidth, trueHeight;
    double startX, startY;

    
    

    protected void fullScreen() {
        width = (int) trueWidth;
        height = (int) trueHeight;
        startX = 0;
        startY = 0;

        gc.save();
        gc.setFill(Color.rgb(20,20,20));
        gc.fillRect(0, 0, trueWidth, trueHeight);
        gc.restore();
    }

    protected void size(int w, int h){
        width = w;
        height = h;
        startX = (trueWidth-w)/2;
        startY = (trueHeight-h)/2;

        gc.save();
        gc.setFill(Color.rgb(20,20,20));
        gc.fillRect(0, 0, trueWidth, trueHeight);
        gc.setFill(Color.rgb(204, 204, 204));
        gc.fillRect(startX,startY,width,height);
        gc.restore();
    }

    public void textSize(double size) {
        gc.setFont(new Font(size));
    }

    public void text(String text, double x, double y) {
        gc.fillText(text, x, y);
    }

    ////////////
    // Output //
    ////////////

    public void keyPressed() {}
    public void keyReleased() {}
    public void keyTyped() {}

    public boolean hasFill = true;
    public boolean hasStroke = true;

    public void background(int gray) {
        background(gray, gray, gray);
    }

    public void background(int r, int g, int b) {
        gc.save();
        gc.setFill(Color.rgb(20,20,20));
        gc.fillRect(0, 0, trueWidth, trueHeight);
        gc.setFill(Color.rgb(r, g, b));
        gc.fillRect(startX,startY,width,height);
        gc.restore();
    }

    public void noStroke() {
        hasStroke = false;
    }

    public void stroke(int gray) {
        stroke(gray,gray,gray);
    }

    public void stroke(int r, int g, int b) {
        hasStroke = true;
        gc.setStroke(Color.rgb(r,g,b));
    }

    public void strokeWeight(int w) {
        gc.setLineWidth(w);
    }

    public void noFill() {
        hasFill = false;
    }
    
    public void fill(int gray){
        fill(gray, gray, gray);
    }
    public void fill(int r, int g, int b){
        hasFill = true;
        gc.setFill(Color.rgb(r,g,b));
    }


    ////////////////////////////////////
    // Shape // 2D Primitives // Line //
    ////////////////////////////////////

    

    /////////////////////////////////////
    // Shape // 2D Primitives // Point //
    /////////////////////////////////////

    

    ////////////////////////////////////
    // Shape // 2D Primitives // Quad //
    ////////////////////////////////////

    

    ////////////////////////////////////
    // Shape // 2D Primitives // Rect //
    ////////////////////////////////////

    

    public void setMousePos(double x, double y) {
        pmouseX = mouseX;
        pmouseY = mouseY;
        mouseX = x;
        mouseY = y;
    }
    
}