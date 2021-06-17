package utils;

import javafx.scene.paint.Color;

import static utils.Constants.*;
import static utils.Maths.*;

public class Colours {

    public static int colorToInt(Color color) {
        System.out.println(color.getRed());
        return 0;
    }

    public static int encodeColour(double rh, double gs, double bv, double ao) {
        int encodedAO = (int)(ao) << 24;
        int encodedRH = (int)(rh) << 16;
        int encodedGS = (int)(gs) << 8;
        int encodedBV = (int)(bv);
        int encodedValue = encodedAO | encodedRH | encodedGS | encodedBV;
        return encodedValue;
    }

    public static Color decodeColour(int mode, int encodedValue) {
        switch (mode) {
            case RGB: {
                return Color.rgb((int)red(encodedValue), (int)green(encodedValue), (int)blue(encodedValue), alpha(encodedValue)/255);
            }

            case HSB: {
                double hue = map(red(encodedValue), 0, 255, 0, 360);
                double saturation = map(green(encodedValue), 0, 255, 0, 1);
                double brightness = map(blue(encodedValue), 0, 255, 0, 1);
                double opacity = map(alpha(encodedValue), 0, 255, 0, 1);
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

    public static double hue(int encodedValue) {
        return map(red(encodedValue), 0, 255, 0, 360);
    }

    public static double saturation(int encodedValue) {
        return map(green(encodedValue), 0, 255, 0, 1);
    }

    public static double brightness(int encodedValue) {
        return map(blue(encodedValue), 0, 255, 0, 1);
    }

}