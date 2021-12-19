package main;

import jgraphics.canvas.Canvas;
import jgraphics.canvas.Graphics;
import jgraphics.utils.Colour;

public class Main {

    public static void main(String[] args) {
        Canvas cv = new Canvas();
        cv.setupCanvas(1280, 720);
        Graphics gc = cv.getGraphics();
        gc.setFill(Colour.RED);
        gc.fillRect(100, 100, 300, 150);
    }

}