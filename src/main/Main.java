package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jgraphics.canvas.Canvas;
import jgraphics.canvas.Graphics;

public class Main {

    public static ArrayList<AppEntry> apps;
    public static Canvas cv;
    public static Graphics gc;

    public static void main(String[] args) {
        cv = new Canvas();
        cv.setupCanvas(1280, 720);
        gc = cv.getGraphics();
        apps = new ArrayList<AppEntry>();

        try {
            Loader.searchFolder(new File("sketches"));
            System.out.println(String.format("Loaded %d app(s)", Main.apps.size()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Loader.launchProgram(0);
    }

}