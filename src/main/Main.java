package main;

import java.util.ArrayList;

import jgraphics.canvas.Canvas;
import jgraphics.canvas.Display;
import jgraphics.canvas.Graphics;

public class Main {

    public static ArrayList<AppEntry> apps;
    public static Display dp;
    public static Canvas cv;
    public static Graphics gc;

    public static void main(String[] args) {
        cv = new Canvas();
        cv.setupCanvas(1280, 720);
        gc = cv.getGraphics();
        dp = cv.getDisplay();
        dp.setIcon("ProcessEmuLogo.png");
        dp.setTitle("ProcessEmu");
        apps = new ArrayList<AppEntry>();
        Loader.loadFolder("sketches");
        Loader.launchProgram(0);
    }

}