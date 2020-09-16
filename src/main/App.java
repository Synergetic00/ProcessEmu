package main;

import javafx.scene.canvas.GraphicsContext;

public class App extends FXApp {

    public App(GraphicsContext gc) {
        super(gc);
    }

    public void setup() {
        size(500,500);
        background(0,200,200);
    }

    public void draw() {
        background(0,200,200);
        rect(50,50,50,50);
        fill(0, 0, 150);
        stroke(0, 0, 250);
        rect(pmouseX,pmouseY,10,10);
        fill(150, 0, 0);
        stroke(250, 0, 0);
        rect(mouseX,mouseY,10,10);
    }
    
}
