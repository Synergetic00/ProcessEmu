package main;

import java.math.RoundingMode;
import java.text.NumberFormat;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import utils.*;
//import static utils.PConstants.*;

public class FXApp {

    GraphicsContext gc;
    PSurface surface;

    public FXApp(GraphicsContext gc) {
        this.gc = gc;
        defaultSettings();
    }

    private void defaultSettings() {
        //settings();
        loop();
        isTerminating = false;
    }
    
    // [Structure]

    boolean isLooping;
    boolean isTerminating;

    public void draw() {}

    public void exit() {
        isTerminating = true;
    }

    public void loop() {
        isLooping = true;
    }

    public void noLoop() {
        isLooping = false;
    }

    public void pop() {}

    public void popStyle() {}

    public void push() {}

    public void pushStyle() {}

    public void redraw() {
        draw();
    }

    public void setup() {}

    public void thread(String name) {
        //new Thread(name);
    }

    // [Environment]

    public int frameCount;

    public void cursor(int kind) {
        surface.setCursor(kind);
    }

    public void cursor(PImage image) {
       
    }

    public void cursor(PImage image, int x, int y) {
       
    }

    public void cursor() {
       surface.showCursor();
    }

    public void noCursor() {
       surface.hideCursor();
    }

    public void settings() {}

    // [Data / Conversion]

    // [Data / String Functions]

    //// join()

    public String join(String[] list, char separator) {
        return join(list, String.valueOf(separator));
    }

    public String join(String[] list, String separator) {
        String output = "";
        for (int i = 0; i < list.length; i++) {
            output += list[i];
            if (i != list.length-1) output += separator;
        }
        return output;
    }

    //// nf()

    static NumberFormat numberFormat;

    public String nf(double num) {
        return nf(num, 0, 8 - String.valueOf((int)num).length());
    }

    public String nf(int num) {
        return nf(num, 0, 0);
    }

    public String nf(double num, int digits) {
        return nf(num, 0, digits);
    }

    public String nf(int num, int digits) {
        return nf(num, 0, digits);
    }

    public String nf(double num, int left, int right) {
        numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        numberFormat.setMinimumIntegerDigits(left);
        numberFormat.setMinimumFractionDigits(right);
        numberFormat.setMaximumFractionDigits(right);
        numberFormat.setRoundingMode(RoundingMode.HALF_DOWN);
        return numberFormat.format(num);
    }

    public String[] nf(double[] nums) {
        return null;
    }

    public String[] nf(int[] nums) {
        return null;
    }

    public String[] nf(double[] nums, int digits) {
        return null;
    }

    public String[] nf(int[] nums, int digits) {
        return null;
    }

    public String[] nf(double[] nums, int left, int right) {
        return null;
    }

    public String[] nf(int[] nums, int left, int right) {
        return null;
    }

    // [Data / Array Functions]

    // [Shape / 2D Primatives]

















    // Utility Functions (not in Processing)

    public void handleSettings() {
        settings();
    }
    
    public void handleSetup() {
        setup();
        if (isTerminating) forceExit(0);
    }

    public void handleDraw() {
        if (isLooping && !isTerminating) {
            draw();
            frameCount++;
        }
        if (isTerminating) forceExit(0);
    }
    
    public void handleKeyPressed(KeyEvent keyEvent) {}
    
    public void handleKeyReleased(KeyEvent keyEvent) {}
    
    public void handleKeyTyped(KeyEvent keyEvent) {}
    
    public void handleMouseClicked(MouseEvent mouseEvent) {}
    
    public void handleMouseDragged(MouseEvent mouseEvent) {}
    
    public void handleMouseMoved(MouseEvent mouseEvent) {}
    
    public void handleMousePressed(MouseEvent mouseEvent) {}
    
    public void handleMouseReleased(MouseEvent mouseEvent) {}
    
    public void handleMouseWheel(ScrollEvent scrollEvent) {}

    public void size(int a, int b) {}

    public void forceExit(int status) {
        System.exit(status);
    }
    
    public void rect(int a, int b, int c, int d) {
        gc.fillRect(a, b, c, d);
    }

}