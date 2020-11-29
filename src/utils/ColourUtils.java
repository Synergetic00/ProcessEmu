package utils;

import javafx.scene.paint.Color;

public class ColourUtils {

    public static int encodeColour(double rh, double gs, double bb, double ao) {
        int encodedAO = (int)(ao) << 24;
        int encodedRH = (int)(rh) << 16;
        int encodedGS = (int)(gs) << 8;
        int encodedBB = (int)(bb);
        int encodedValue = encodedAO | encodedRH | encodedGS | encodedBB;
        return encodedValue;
    }

    public static Color decodeColour(int value) {
        return new Color(red(value)/255, green(value)/255, blue(value)/255, alpha(value)/255);
    }

    public static double alpha(int encodedValue) {
        return (encodedValue >> 24) & 255;
    }

    public static double red(int encodedValue) {
        return (encodedValue >> 16) & 255;
    }

    public static double green(int encodedValue) {
        return (encodedValue >> 8) & 255;
    }

    public static double blue(int encodedValue) {
        return (encodedValue) & 255;
    }
    
}