package main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jgraphics.canvas.Graphics;
import jgraphics.utils.Colour;
import ptypes.PImage;
import ptypes.PSurface;

public class AppBase {

    private Graphics gc;
    private boolean looping;
    private PSurface surface;

    public AppBase(Graphics gc) {
        this.gc = gc;
        this.looping = true;
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

    //////////////////////
    // Color // Setting //
    //////////////////////

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
        gc.fillRect(x, y, w, h);
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
    }
    
}