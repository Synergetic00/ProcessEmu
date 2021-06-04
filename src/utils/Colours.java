package utils;

import javafx.scene.paint.Color;

public class Colours {

    public static int encodeColour(double rh, double gs, double bv, double ao) {
        int encodedAO = (int)(ao) << 24;
        int encodedRH = (int)(rh) << 16;
        int encodedGS = (int)(gs) << 8;
        int encodedBV = (int)(bv);
        int encodedValue = encodedAO | encodedRH | encodedGS | encodedBV;
        return encodedValue;
    }

    public static Color decodeColour(int encodedValue) {

        return Color.rgb((int)red(encodedValue), (int)green(encodedValue), (int)blue(encodedValue), alpha(encodedValue)/255);
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