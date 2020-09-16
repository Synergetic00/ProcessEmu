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
        //size(640, 360);
        point(30, 20);
point(85, 20);
point(85, 75);
point(30, 75);
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
