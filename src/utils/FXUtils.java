package utils;

import java.util.Random;

public class FXUtils {

    /////////////////////////
    // Output // Text Area //
    /////////////////////////

    public static void print(byte data) {
        System.out.print(data);
        System.out.flush();
    }

    public static void print(boolean data) {
        System.out.print(data);
        System.out.flush();
    }

    //////////////////////////
    // Maths // Calculation //
    //////////////////////////

    public static int abs(int a) {
        return (a < 0) ? -a : a;
    }

    public static long abs(long a) {
        return (a < 0) ? -a : a;
    }

    public static double abs(double a) {
        return (a <= 0.0) ? 0.0 - a : a;
    }
    
    public static int ceil(double val) {
        if (val % 1 == 0) return (int) val;
        return (int) ((val > 0) ? val - (val % 1) + 1: val - (val % 1) );
    }

    public static int constrain(int val, int low, int high) {
		return (val < low) ? low : ((val > high) ? high : val);
    }
    
    public static double constrain(double val, double low, double high) {
		return ((val < low) ? low : ((val > high) ? high : val));
    }
    
    public static double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt(sq(abs(x1 - x2)) + sq(abs(y1 - y2)));
    }
    
    public static double dist(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.sqrt(sq(abs(x1 - x2)) + sq(abs(y1 - y2)) + sq(abs(z1 - z2)));
    }

    public static double exp(double n) {
        return Math.exp(n);
    }

    public static int floor(double val) {
		return (int) ((val > 0) ? val - (val % 1) : val - (val % 1) - 1) ;
    }

    public static double lerp(double start, double stop, double amt) {
        return (start + amt * (stop - amt));
    }

    public static double log(double n) {
        return Math.log(n);
    }

    public static double mag(double x, double y) {
        return dist(0, 0, x, y);
    }

    public static double map(double value, double start1, double stop1, double start2, double stop2) {
        return ((1d/((stop1-start1)/(stop2-start2)))*(value-stop1)+stop2);
    }

    public static int max(int a, int b) {
        return (a > b) ? a : b;
    }

    public static double max(double a, double b) {
        return ((a > b) ? a : b);
    }

    public static int max(int a, int b, int c) {
        return (a > b) ? ((a > c) ? a : c) : (b > c) ? b : c;
    }

    public static double max(double a, double b, double c) {
        return ((a > b) ? ((a > c) ? a : c) : (b > c) ? b : c);
    }

    public static int max(int[] arr) {
        int max = arr[0];
        for (int i : arr) if (i > max) max = i;
        return max;
    }

    public static double max(double[] arr) {
        double max = arr[0];
        for (double i : arr) if (i > max) max = i;
        return max;
    }

    public static int min(int a, int b) {
        return (a < b) ? a : b;
    }

    public static double min(double a, double b) {
        return ((a < b) ? a : b);
    }

    public static int min(int a, int b, int c) {
        return (a < b) ? ((a < c) ? a : c) : (b < c) ? b : c;
    }

    public static double min(double a, double b, double c) {
        return ((a < b) ? ((a < c) ? a : c) : (b < c) ? b : c);
    }

    public static int min(int[] arr) {
        int min = arr[0];
        for (int i : arr) if (i < min) min = i;
        return min;
    }

    public static double min(double[] arr) {
        double min = arr[0];
        for (double i : arr) if (i < min) min = i;
        return min;
    }

    public static double norm(double value, double start, double stop) {
        return (value / (stop - start));
    }

    public static double pow(double n, double e) {
        return Math.pow(n, e);
    }

    public static double round(double a) {
        return Math.round(a);
    }

    public static double sq(double a) {
        return pow(a,2);
    }

    public static double sqrt(double a) {
        return Math.sqrt(a);
    }
    
    ///////////////////////////
    // Maths // Trigonometry //
    ///////////////////////////

    public static double acos(double a) {
        return Math.acos(a);
    }

    public static double asin(double a) {
        return Math.asin(a);
    }

    public static double atan(double a) {
        return Math.atan(a);
    }

    public static double atan2(double y, double x) {
        return Math.atan2(y, x);
    }

    public static double cos(double a) {
        return Math.cos(a);
    }

    public static double degrees(double a) {
        return Math.toDegrees(a);
    }

    public static double radians(double a) {
        return Math.toRadians(a);
    }

    public static double sin(double a) {
        return Math.sin(a);
    }

    public static double tan(double a) {
        return Math.tan(a);
    }
    
    /////////////////////
    // Maths // Random //
    /////////////////////
    
    static Random defaultRandom;

    public static double random(double high) {
        return random(0, high);
    }

    public static double random(double low, double high) {
        if (defaultRandom == null) defaultRandom = new Random();
        return (low + (high - low) * defaultRandom.nextDouble());
    }

    public static double randomGaussian() {
        if (defaultRandom == null) defaultRandom = new Random();
        return defaultRandom.nextGaussian();
    }

    public static void randomSeed(int seed) {
        if (defaultRandom == null) defaultRandom = new Random();
        defaultRandom.setSeed(seed);
    }

    static Random perlinRandom = new Random();

    // noise()
    // noiseDetail()

    public static void noiseSeed(int seed) {
        if (perlinRandom == null) perlinRandom = new Random();
        perlinRandom.setSeed(seed);
    }
    
    ////////////////////////
    // Maths // Constants //
    ////////////////////////

    public static final double HALF_PI = 1.5707964f;
    public static final double PI = 3.1415927f;
    public static final double QUARTER_PI = 0.7853982f;
    public static final double TAU = 6.2831855f;
    public static final double TWO_PI = 6.2831855f;

    public static final int OPEN = 0;
    public static final int CHORD = 1;
    public static final int PIE = 2;

    public static final int CORNER = 0;
    public static final int CORNERS = 1;
    public static final int RADIUS = 2;
    public static final int CENTER = 3;

    public static final int LEFT = 4;
    public static final int RIGHT = 5;
    public static final int TOP = 6;
    public static final int BOTTOM = 7;
    public static final int BASELINE = 8;

}
