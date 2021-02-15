package utils;

import javafx.scene.paint.Color;

import static utils.Constants.*;
import static utils.MathUtils.*;

public class ColourUtils {

    public static int encodeColour(double rh, double gs, double bb, double ao) {
        int encodedAO = (int)(ao) << 24;
        int encodedRH = (int)(rh) << 16;
        int encodedGS = (int)(gs) << 8;
        int encodedBB = (int)(bb);
        int encodedValue = encodedAO | encodedRH | encodedGS | encodedBB;
        return encodedValue;
    }

    public static Color decodeColour(int mode, int value) {

        switch (mode) {
            case RGB: {
                return Color.rgb((int)red(value), (int)green(value), (int)blue(value), alpha(value)/255);
            }

            case HSB: {
                double hue = map(red(value), 0, 255, 0, 360);
                double saturation = map(green(value), 0, 255, 0, 1);
                double brightness = map(blue(value), 0, 255, 0, 1);
                double opacity = map(alpha(value), 0, 255, 0, 1);
                return Color.hsb(hue, saturation, brightness, opacity);
            }

            default: {
                return Color.WHITE;
            }
        }
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