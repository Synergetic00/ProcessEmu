package programs;

import javafx.scene.canvas.GraphicsContext;
import main.FXApp;
public class Test extends FXApp{
public Test(GraphicsContext gc) { super(gc); }
int x = 50, y = 100;

public void setup() {
    size(600,400);
    background(255,0,255);
    rect(x,y,100,250);
}

public void draw() {
    background(255,0,255);
    rect(x,y,100,250);
}

}