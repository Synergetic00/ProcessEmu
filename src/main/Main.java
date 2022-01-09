package main;

import java.util.ArrayList;

import jgraphics.canvas.Canvas;
import jgraphics.canvas.Display;
import jgraphics.canvas.Graphics;
import utils.Constants;

public class Main {

    public static ArrayList<AppEntry> apps;
    public static Display dp;
    public static Canvas cv;
    public static Graphics gc;

    public static void main(String[] args) {
        cv = new Canvas();

        createWindow();

        apps = new ArrayList<AppEntry>();
        Loader.loadFolder("sketches");
        Loader.launchProgram(0);
    }

    private static void createWindow() {
        AppState.screenW(Constants.WIDTH);
        AppState.screenH(Constants.HEIGHT);

        cv.setupCanvas(AppState.screenW(), AppState.screenH());

        gc = cv.getGraphics();
        dp = cv.getDisplay();

        AppState.displayW(1920);
        AppState.displayH(1080);

        AppState.windowW(Constants.WIDTH + 16);
        AppState.windowH(Constants.HEIGHT + 39);

        dp.setIcon("ProcessEmuLogo.png");
        dp.setTitle("ProcessEmu");
    }

}