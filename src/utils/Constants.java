package utils;

public class Constants {

    private static int screenW;
    private static int screenH;

    public static int screenW() {
        return screenW;
    }

    public static int screenH() {
        return screenH;
    }

    public static void screenW(int screenW) {
        Constants.screenW = screenW;
    }

    public static void screenH(int screenH) {
        Constants.screenH = screenH;
    }

    public static int offsetW;
    public static int offsetH;

    public static int offsetW() {
        return offsetW;
    }

    public static int offsetH() {
        return offsetH;
    }

    public static void offsetW(int offsetW) {
        Constants.offsetW = offsetW;
    }

    public static void offsetH(int offsetH) {
        Constants.offsetH = offsetH;
    }
    
}