package main;

public class AppState {

    // window = the interactable application box
    // screen = what is displayed inside the window
    // offset = how far to move the processing window to centre
    // display = your actual monitor

    private static int windowW, windowH;
    private static int screenW, screenH;
    private static int offsetW, offsetH;
    private static int displayW, displayH;
    private static int renderW, renderH;

    public static int screenW() {
        return screenW;
    }

    public static int screenH() {
        return screenH;
    }

    public static void screenW(int screenW) {
        AppState.screenW = screenW;
    }

    public static void screenH(int screenH) {
        AppState.screenH = screenH;
    }

    public static int offsetW() {
        return offsetW;
    }

    public static int offsetH() {
        return offsetH;
    }

    public static void offsetW(int offsetW) {
        AppState.offsetW = offsetW;
    }

    public static void offsetH(int offsetH) {
        AppState.offsetH = offsetH;
    }

    public static int displayW() {
        return displayW;
    }

    public static int displayH() {
        return displayH;
    }

    public static void displayW(int displayW) {
        AppState.displayW = displayW;
    }

    public static void displayH(int displayH) {
        AppState.displayH = displayH;
    }

    public static int windowW() {
        return windowW;
    }

    public static int windowH() {
        return windowH;
    }

    public static void windowW(int windowW) {
        AppState.windowW = windowW;
    }

    public static void windowH(int windowH) {
        AppState.windowH = windowH;
    }

    public static int renderW() {
        return renderW;
    }

    public static int renderH() {
        return renderH;
    }

    public static void renderW(int renderW) {
        AppState.renderW = renderW;
    }

    public static void renderH(int renderH) {
        AppState.renderH = renderH;
    }
    
}