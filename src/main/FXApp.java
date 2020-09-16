package main;

import utils.PVector;
import static utils.PVector.*;
import static utils.FXUtils.*;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

public class FXApp {

    GraphicsContext gc;

    int width, height;
    double mouseX, mouseY, pmouseX, pmouseY;
    
    protected FXApp(GraphicsContext gc){
        this.gc = gc;
    }

    protected void size(int w, int h){
        width = w;
        height = h;
    }

    public void setup() {}
    public void draw() {}

    public boolean hasFill = true;
    public boolean hasStroke = true;

    public void background(int gray) {
        background(gray, gray, gray);
    }

    public void background(int r, int g, int b) {
        gc.save();
        gc.setFill(Color.rgb(r, g, b));
        gc.fillRect(0,0,width,height);
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
    
    public void fill(int gray){
        fill(gray, gray, gray);
    }
    public void fill(int r, int g, int b){
        hasFill = true;
        gc.setFill(Color.rgb(r,g,b));
    }

    public void rect(double x, double y, double width, double height) {
        if (hasFill) gc.fillRect(x, y, width, height);
        if (hasStroke) gc.strokeRect(x, y, width, height);
    }

    public void setMousePos(double x, double y) {
        pmouseX = mouseX;
        pmouseY = mouseY;
        mouseX = x;
        mouseY = y;
    }
    
}