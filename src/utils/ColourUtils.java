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

    public static double[] RGBtoHSB(int r, int g, int b, double[] hsbvals) {
        double hue, saturation, brightness;
        if (hsbvals == null) {
            hsbvals = new double[3];
        }
        int cmax = (r > g) ? r : g;
        if (b > cmax) cmax = b;
        int cmin = (r < g) ? r : g;
        if (b < cmin) cmin = b;

        brightness = ((double) cmax) / 255.0f;
        if (cmax != 0)
            saturation = ((double) (cmax - cmin)) / ((double) cmax);
        else
            saturation = 0;
        if (saturation == 0)
            hue = 0;
        else {
            double redc = ((double) (cmax - r)) / ((double) (cmax - cmin));
            double greenc = ((double) (cmax - g)) / ((double) (cmax - cmin));
            double bluec = ((double) (cmax - b)) / ((double) (cmax - cmin));
            if (r == cmax)
                hue = bluec - greenc;
            else if (g == cmax)
                hue = 2.0f + redc - bluec;
            else
                hue = 4.0f + greenc - redc;
            hue = hue / 6.0f;
            if (hue < 0)
                hue = hue + 1.0f;
        }
        hsbvals[0] = hue;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;
        return hsbvals;
    }

    public static int HSBtoRGB(double hue, double saturation, double brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            double h = (hue - (double)Math.floor(hue)) * 6.0f;
            double f = h - (double)java.lang.Math.floor(h);
            double p = brightness * (1.0f - saturation);
            double q = brightness * (1.0f - saturation * f);
            double t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
            case 0:
                r = (int) (brightness * 255.0f + 0.5f);
                g = (int) (t * 255.0f + 0.5f);
                b = (int) (p * 255.0f + 0.5f);
                break;
            case 1:
                r = (int) (q * 255.0f + 0.5f);
                g = (int) (brightness * 255.0f + 0.5f);
                b = (int) (p * 255.0f + 0.5f);
                break;
            case 2:
                r = (int) (p * 255.0f + 0.5f);
                g = (int) (brightness * 255.0f + 0.5f);
                b = (int) (t * 255.0f + 0.5f);
                break;
            case 3:
                r = (int) (p * 255.0f + 0.5f);
                g = (int) (q * 255.0f + 0.5f);
                b = (int) (brightness * 255.0f + 0.5f);
                break;
            case 4:
                r = (int) (t * 255.0f + 0.5f);
                g = (int) (p * 255.0f + 0.5f);
                b = (int) (brightness * 255.0f + 0.5f);
                break;
            case 5:
                r = (int) (brightness * 255.0f + 0.5f);
                g = (int) (p * 255.0f + 0.5f);
                b = (int) (q * 255.0f + 0.5f);
                break;
            }
        }
        return 0xff000000 | (r << 16) | (g << 8) | (b << 0);
    }
    
}