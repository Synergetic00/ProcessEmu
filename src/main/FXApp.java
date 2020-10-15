package main;

//import utils.PVector;
//import static utils.PVector.*;
import static utils.FXUtils.*;

import java.util.*;

import javafx.geometry.VPos;
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
        gc.setTextAlign(alignH);
        gc.setTextBaseline(alignV);
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

    ////////////////////////
    // Output / Text Area //
    ////////////////////////

    // print()

    public void print(byte toPrint) {
        System.out.print(toPrint);
        System.out.flush();
    }
    
    public void print(boolean toPrint) {
        System.out.print(toPrint);
        System.out.flush();
    }
    
    public void print(char toPrint) {
        System.out.print(toPrint);
        System.out.flush();
    }
    
    public void print(int toPrint) {
        System.out.print(toPrint);
        System.out.flush();
    }
    
    public void print(long toPrint) {
        System.out.print(toPrint);
        System.out.flush();
    }
    
    public void print(float toPrint) {
        System.out.print(toPrint);
        System.out.flush();
    }
    
    public void print(double toPrint) {
        System.out.print(toPrint);
        System.out.flush();
    }
    
    public void print(String toPrint) {
        System.out.print(toPrint);
        System.out.flush();
    }
    
    public void print(Object toPrint) {
        if (toPrint == null) {
            System.out.print("null");
        } else {
            System.out.println(toPrint.toString());
        }
    }

    public void print(Object... variables) {
        StringBuilder sb = new StringBuilder();
        for (Object o : variables) {
            if (sb.length() != 0) {
                sb.append(" ");
            }
            if (o == null) {
                sb.append("null");
            } else {
                sb.append(o.toString());
            }
        }
        System.out.print(sb.toString());
    }

    // printArray()

    public void printArray(Object toPrint) {
        if (toPrint == null) {
            System.out.println("null");
        } else {
            String name = toPrint.getClass().getName();
            if (name.charAt(0) == '[') {
                switch (name.charAt(1)) {
                    case '[': { // multi-dimensional
                        System.out.println(toPrint);
                        break;
                    }

                    case 'L': { // objects
                        Object objectArr[] = (Object[]) toPrint;
                        for (int i = 0; i < objectArr.length; i++) {
                            if (objectArr[i] instanceof String) {
                                System.out.println("[" + i + "] \"" + objectArr[i] + "\"");
                            } else {
                                System.out.println("[" + i + "] " + objectArr[i]);
                            }
                        }
                        break;
                    }

                    case 'Z': { // boolean
                        boolean booleanArr[] = (boolean[]) toPrint;
                        for (int i = 0; i < booleanArr.length; i++) {
                            System.out.println("[" + i + "] " + booleanArr[i]);
                        }
                        break;
                    }

                    case 'B': { // byte
                        byte byteArr[] = (byte[]) toPrint;
                        for (int i = 0; i < byteArr.length; i++) {
                            System.out.println("[" + i + "] " + byteArr[i]);
                        }
                        break;
                    }

                    case 'C': { // char
                        char charArr[] = (char[]) toPrint;
                        for (int i = 0; i < charArr.length; i++) {
                            System.out.println("[" + i + "] '" + charArr[i] + "'");
                        }
                        break;
                    }

                    case 'I': { // int
                        int intArr[] = (int[]) toPrint;
                        for (int i = 0; i < intArr.length; i++) {
                            System.out.println("[" + i + "] " + intArr[i]);
                        }
                        break;
                    }

                    case 'J': { // long
                        long longArr[] = (long[]) toPrint;
                        for (int i = 0; i < longArr.length; i++) {
                            System.out.println("[" + i + "] " + longArr[i]);
                        }
                        break;
                    }

                    case 'F': { // float
                        float floatArr[] = (float[]) toPrint;
                        for (int i = 0; i < floatArr.length; i++) {
                            System.out.println("[" + i + "] " + floatArr[i]);
                        }
                        break;
                    }

                    case 'D': { // double
                        double doubleArr[] = (double[]) toPrint;
                        for (int i = 0; i < doubleArr.length; i++) {
                            System.out.println("[" + i + "] " + doubleArr[i]);
                        }
                        break;
                    }
                
                    default: { // other data types
                        System.out.println(toPrint);
                        break;
                    }
                }
            } else {
                System.out.println(toPrint);
            }


        }

        System.out.flush();
    }

    // println()

    public void println() {
        System.out.println();
        System.out.flush();
    }

    public void println(byte toPrint) {
        System.out.println(toPrint);
        System.out.flush();
    }
  
    public void println(boolean toPrint) {
        System.out.println(toPrint);
        System.out.flush();
    }
  
    public void println(char toPrint) {
        System.out.println(toPrint);
        System.out.flush();
    }
  
    public void println(int toPrint) {
        System.out.println(toPrint);
        System.out.flush();
    }
  
    public void println(long toPrint) {
        System.out.println(toPrint);
        System.out.flush();
    }
  
    public void println(float toPrint) {
        System.out.println(toPrint);
        System.out.flush();
    }
  
    public void println(double toPrint) {
        System.out.println(toPrint);
        System.out.flush();
    }
  
    public void println(String toPrint) {
        System.out.println(toPrint);
        System.out.flush();
    }

    public void println(Object toPrint) {
        if (toPrint == null) {
            System.out.println("null");
        } else if (toPrint.getClass().isArray()) {
            printArray(toPrint);
        } else {
            System.out.println(toPrint.toString());
            System.out.flush();
        }
    }

    public void println(Object... variables) {
        print(variables);
        println();
    }

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
    
    public void fill(int gray) {
        fill(gray, gray, gray, 255);
    }
    
    public void fill(int gray, int alpha) {
        fill(gray, gray, gray, alpha);
    }

    public void fill(int red, int green, int blue) {
        fill(red, green, blue, 255);
    }
    
    public void fill(int red, int green, int blue, int alpha) {
        hasFill = true;
        gc.setFill(new Color(map(red, 0, 255, 0, 1), map(green, 0, 255, 0, 1), map(blue, 0, 255, 0, 1), map(alpha, 0, 255, 0, 1)));
    }

    public void smooth() {
        
    }

    

    public void setMousePos(double x, double y) {
        pmouseX = mouseX;
        pmouseY = mouseY;
        mouseX = x;
        mouseY = y;
    }

    ////////////////
    // Typography //
    ////////////////

    // PFont.java

    ///////////////////////////////////////
    // Typography / Loading & Displaying //
    ///////////////////////////////////////

    // text()

    public void text(int textVar, double x, double y) { text(textVar+"", x, y); }

    public void text(double textVar, double x, double y) { text(nfs(textVar,3), x, y); }

    public void text(String text, double x, double y) {
        double nx = x;
        double ny = y;

        gc.fillText(text, startX+nx, startY+ny);
    }

    public String nfs(double num, int right) {
        return String.format("%."+right+"f", num);
    }

    /////////////////////////////
    // Typography / Attributes //
    /////////////////////////////

    TextAlignment alignH = TextAlignment.LEFT;
    VPos alignV = VPos.BASELINE;
    int alignX = LEFT, alignY = BASELINE;

    // textAlign()

    public void textAlign(int newAlignX) {
        alignX = newAlignX;
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
        
            default: { break; }
        }
        gc.setTextAlign(alignH);
    }

    public void textAlign(int newAlignX, int newAlignY) {
        alignX = newAlignX;
        alignY = newAlignY;
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
        
            default: { break; }
        }
        gc.setTextBaseline(alignV);
    }

    // textSize()

    public void textSize(double size) {
        gc.setFont(new Font(size));
    }

    //////////////////////////
    // Typography / Metrics //
    //////////////////////////
    
}