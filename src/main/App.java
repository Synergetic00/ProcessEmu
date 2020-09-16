package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;
import utils.PVector;
import static utils.PVector.*;
import static utils.FXUtils.*;

public class App extends FXApp {

    public App(GraphicsContext gc) {
        super(gc);
    }

    int[] angles = { 30, 10, 45, 35, 60, 38, 75, 67 };

    public void setup() {
        size(1920, 1080);
        arc(50, 55, 50, 50, 0, HALF_PI);
        noFill();
        arc(50, 55, 60, 60, HALF_PI, PI);
        arc(50, 55, 70, 70, PI, PI+QUARTER_PI);
        arc(50, 55, 80, 80, PI+QUARTER_PI, TWO_PI);
        //pieChart(300, angles);
    }

    public void draw() {
    }

    void pieChart(double diameter, int[] data) {
        double lastAngle = 0;
        for (int i = 0; i < data.length; i++) {
            double gray = map(i, 0, data.length, 0, 255);
            fill((int) gray);
            arc(width/2, height/2, diameter, diameter, lastAngle, lastAngle+radians(data[i]));
            lastAngle += radians(data[i]);
        }
    } 
}
