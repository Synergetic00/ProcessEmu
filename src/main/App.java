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
        rect(mouseX,mouseY,10,10);
    }
    
}
