package main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jgraphics.canvas.Graphics;
import jgraphics.utils.Colour;
import ptypes.PImage;
import ptypes.PSurface;

import static utils.Maths.*;

@SuppressWarnings("unused")
public class AppBase {

    private Graphics gc;
    private boolean looping;
    private PSurface surface;

    private boolean hasFill;
    private boolean hasStroke;

    public AppBase(Graphics gc) {
        this.gc = gc;
        this.looping = true;
        this.hasFill = true;
        this.hasStroke = true;
    }

    ///////////////
    // Structure //
    ///////////////

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
        popMatrix();
        popStyle();
    }

    public void popStyle() {
        
    }

    public void push() {
        pushMatrix();
        pushStyle();
    }

    public void pushStyle() {
        
    }

    public void redraw() {
        render();
    }

    public void setup() {}

    public void thread(final String name) {
        Thread later = new Thread() {
            @Override
            public void run() {
                method(name);
            }
        };
        later.start();
    }

    /////////////////
    // Environment //
    /////////////////

    public void cursor() {
        surface.showCursor();
    }

    public void cursor(int kind) {
        surface.setCursor(kind);
    }

    public void cursor(PImage img) {
        cursor(img, img.width / 2, img.height / 2);
    }

    public void cursor(PImage img, int x, int y) {
        surface.setCursor(img, x, y);
    }

    public void delay(int napTime) {
        try {
            Thread.sleep(napTime);
        } catch (InterruptedException ie) {}
    }

    public int displayDensity() {
        return 1;
    }

    public int displayDensity(int display) {
        return 1;
    }

    public boolean focused;

    public int frameCount;

    public void frameRate(double fps) {
        if (fps > 0) {
            frameRate = fps;
            Main.cv.setFrameRate(frameRate);
        }
    }

    public double frameRate;

    public void fullScreen() {
        size(AppState.screenW(), AppState.screenH());
    }

    public int height;

    public void noCursor() {
        surface.hideCursor();
    }

    public void noSmooth() {}

    public void pixelDensity(int density) {}

    public int pixelHeight;

    public int pixelWidth;

    public void settings() {}

    public void size(int width, int height) {
        double widthMul = min(1d, AppState.screenW() / (double)width);
        double heightMul = min(1d, AppState.screenH() / (double)height);

        this.width = width;
        this.height = height;

        double minMul = min(widthMul, heightMul);

        if (minMul < 1) {
            gc.translate(0, 320);
            gc.scale(minMul, minMul);
        }

        AppState.offsetW((AppState.screenW() - width) / 2);
        AppState.offsetH((AppState.screenH() - height) / 2);

        background(204);
    }

    public void smooth(int level) {}

    public int width;

    ///////////
    // Shape //
    ///////////

    ////////////////////////////
    // Shape // 2D Primatives //
    ////////////////////////////

    /////////////////////
    // Shape // Curves //
    /////////////////////

    ////////////////////////////
    // Shape // 3D Primitives //
    ////////////////////////////

    /////////////////////////
    // Shape // Attributes //
    /////////////////////////

    ///////////
    // Input //
    ///////////

    ////////////////////
    // Input // Mouse //
    ////////////////////

    ///////////////////////
    // Input // Keyboard //
    ///////////////////////

    ////////////////////
    // Input // Files //
    ////////////////////

    //////////////////////////
    // Input // Time & Date //
    //////////////////////////

    ////////////
    // Output //
    ////////////

    /////////////////////
    // Output // Image //
    /////////////////////

    ///////////////
    // Transform //
    ///////////////

    public void popMatrix() {
        
    }

    public void pushMatrix() {
        
    }

    public void resetMatrix() {
        
    }

    ////////////////////
    // Lights, Camera //
    ////////////////////

    //////////////////////////////
    // Lights, Camera // Lights //
    //////////////////////////////

    //////////////////////////////
    // Lights, Camera // Camera //
    //////////////////////////////

    ///////////////////////////////////
    // Lights, Camera // Coordinates //
    ///////////////////////////////////

    ///////////////////////////////////////////
    // Lights, Camera // Material Properties //
    ///////////////////////////////////////////

    ///////////
    // Color //
    ///////////

    private double maxRH;
    private double maxGS;
    private double maxBB;
    private double maxAO;

    //////////////////////
    // Color // Setting //
    //////////////////////

    public void background(double gray) {
        if (between(gray, 0, maxRH)) {
            background(gray, gray, gray, maxAO);
        } else {
            setBackground((int) gray);
        }
    }

    public void background(double gray, double alpha) {
        background(gray, gray, gray, alpha);
    }

    public void background(double rh, double gs, double bv) {
        background(rh, gs, bv, maxAO);
    }

    public void background(double rh, double gs, double bv, double ao) {
        int mappedRH = (int) clamp(map(rh, 0, maxRH, 0, 255), 0, 255);
        int mappedGS = (int) clamp(map(gs, 0, maxGS, 0, 255), 0, 255);
        int mappedBV = (int) clamp(map(bv, 0, maxBB, 0, 255), 0, 255);
        int mappedAO = (int) clamp(map(ao, 0, maxAO, 0, 255), 0, 255);
        setBackground(new Colour(mappedRH, mappedGS, mappedBV, mappedAO).getARGB());
    }

    private void setBackground(int encodedValue) {
        pushMatrix();
        resetMatrix();
        gc.save();
        gc.setFill(encodedValue);
        gc.fillRect(AppState.offsetW(), AppState.offsetH(), width, height);
        gc.restore();
        popMatrix();
    }

    ///////////
    // Image //
    ///////////

    ///////////////
    // Rendering //
    ///////////////

    ////////////////
    // Typography //
    ////////////////

    ////////////////////////////////////////
    // Typography // Loading & Displaying //
    ////////////////////////////////////////

    //////////////////////////////
    // Typography // Attributes //
    //////////////////////////////

    ///////////////////////////
    // Typography // Metrics //
    ///////////////////////////

    /////////////////////
    // Shape // Vertex //
    /////////////////////

    ////////////
    // Pixels //
    ////////////

    //////////////////////
    // Internal Methods //
    //////////////////////

    private void method(String name) {
        try {
            Method method = getClass().getMethod(name, new Class[] {});
            method.invoke(this, new Object[] { });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        } catch (NoSuchMethodException nsme) {
            System.err.println("There is no public " + name + "() method " + "in the class " + getClass().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ///////////////////
    // Other Methods //
    ///////////////////

    public void fill(double rh, double gs, double bv) {
        gc.setFill(new Colour((int)rh, (int)gs, (int)bv, 255));
    }

    public void rect(double x, double y, double w, double h) {

        double nx = x;
        double ny = y;
        double nw = w;
        double nh = h;

        // switch (rectMode) {
        //     case CORNERS: {
        //         nw /= 2;
        //         nh /= 2;
        //         break;
        //     }
        //     case RADIUS: {
        //         nw *= 2;
        //         nh *= 2;
        //         nx -= nw / 2;
        //         ny -= nh / 2;
        //         break;
        //     }
        //     case CENTER: {
        //         nx -= nw / 2;
        //         ny -= nh / 2;
        //     }
        // }

        nx += AppState.offsetW();
        ny += AppState.offsetH();

        if (hasFill) gc.fillRect(nx, ny, nw, nh);
        if (hasStroke) gc.strokeRect(nx, ny, nw, nh);
    }

    public void handleSettings() {
        settings();
    }

    public void handleSetup() {
        setup();
        render();
    }

    public void handleDraw() {
        //updateTime();
        render();
    }

    private void render() {
        draw();
        coverEdges();
    }

    private void coverEdges() {
        pushMatrix();
        resetMatrix();
        gc.save();
        gc.setFill(new Colour(40, 40, 40, 255));
        gc.fillRect(0, 0, AppState.screenW(), AppState.offsetH());                            // Top
        gc.fillRect(0, AppState.offsetH() + height, AppState.screenW(), AppState.offsetH()); // Bottom
        gc.fillRect(0, 0, AppState.offsetW(), AppState.screenH());                            // Left
        gc.fillRect(AppState.offsetW() + width, 0, AppState.offsetW(), AppState.screenH());  // Right
        gc.restore();
        popMatrix();
    }
    
}