package utils;

import java.util.Random;

public class FXUtils {

    public static Random r = new Random();

    // Math --> Calculation

    public static int abs(int a) {
        return (a < 0) ? -a : a;
    }

    public static long abs(long a) {
        return (a < 0) ? -a : a;
    }

    public static float abs(float a) {
        return (a <= 0.0F) ? 0.0F - a : a;
    }

    public static double abs(double a) {
        return (a <= 0.0D) ? 0.0D - a : a;
    }
    
    public static int ceil(float val) {
        if (val % 1 == 0) return (int) val;
        return (int) ((val > 0) ? val - (val % 1) + 1: val - (val % 1) );
    }

    public static int ceil(double val) {
        if (val % 1 == 0) return (int) val;
        return (int) ((val > 0) ? val - (val % 1) + 1: val - (val % 1) );
    }

    public static int constrain(int val, int low, int high) {
		return (val < low) ? low : ((val > high) ? high : val);
    }
    
    public static float constrain(double val, double low, double high) {
		return (float) ((val < low) ? low : ((val > high) ? high : val));
    }
    
    public static float dist(double x1, double y1, double x2, double y2) {
        return (float) Math.sqrt(abs(x2 - x1) * abs(x2 - x1) + abs(y2 - y1) * abs(y2 - y1));
    }

    public static float exp(double n) {
        return (float) Math.exp(n);
    }

    public static int floor(float val) {
		return (int) ((val > 0) ? val - (val % 1) : val - (val % 1) - 1) ;
    }

    public static int floor(double val) {
		return (int) ((val > 0) ? val - (val % 1) : val - (val % 1) - 1) ;
    }

    public static float lerp(double start, double stop, double amt) {
        return (float) (start + amt * (stop - amt));
    }

    public static float log(double n) {
        return (float) Math.log(n);
    }

    public static float mag(double x, double y) {
        return dist(0, 0, x, y);
    }

    public static float map(double value, double start1, double stop1, double start2, double stop2) {
        return (float) ((1d/((stop1-start1)/(stop2-start2)))*(value-stop1)+stop2);
    }

    public static int max(int a, int b) {
        return (a > b) ? a : b;
    }

    public static float max(double a, double b) {
        return (float) ((a > b) ? a : b);
    }

    public static int max(int a, int b, int c) {
        return (a > b) ? ((a > c) ? a : c) : (b > c) ? b : c;
    }

    public static float max(double a, double b, double c) {
        return (float) ((a > b) ? ((a > c) ? a : c) : (b > c) ? b : c);
    }

    public static int max(int[] arr) {
        int max = arr[0];
        for (int i : arr) if (i > max) max = i;
        return max;
    }

    public static float max(double[] arr) {
        double max = arr[0];
        for (double i : arr) if (i > max) max = i;
        return (float) max;
    }

    public static int min(int a, int b) {
        return (a < b) ? a : b;
    }

    public static float min(double a, double b) {
        return (float) ((a < b) ? a : b);
    }

    public static int min(int a, int b, int c) {
        return (a < b) ? ((a < c) ? a : c) : (b < c) ? b : c;
    }

    public static float min(double a, double b, double c) {
        return (float) ((a < b) ? ((a < c) ? a : c) : (b < c) ? b : c);
    }

    public static int min(int[] arr) {
        int min = arr[0];
        for (int i : arr) if (i < min) min = i;
        return min;
    }

    public static float min(double[] arr) {
        double min = arr[0];
        for (double i : arr) if (i < min) min = i;
        return (float) min;
    }

    public static float norm(double value, double start, double stop) {
        return (float) (value / (stop - start));
    }

    public static float pow(double n, double e) {
        return (float) Math.pow(n, e);
    }

    public static float round(double a) {
        return (float) Math.round(a);
    }

    public static float sq(double a) {
        return pow(a,2);
    }

    public static float sqrt(double a) {
        return (float) Math.sqrt(a);
    }

    public static float acos(double a) {
        return (float) Math.acos(a);
    }

    public static float asin(double a) {
        return (float) Math.asin(a);
    }

    public static float atan(double a) {
        return (float) Math.atan(a);
    }

    public static float atan2(double y, double x) {
        return (float) Math.atan2(y, x);
    }

    public static float cos(double a) {
        return (float) Math.cos(a);
    }

    public static float degrees(double a) {
        return (float) Math.toDegrees(a);
    }

    public static float radians(double a) {
        return (float) Math.toRadians(a);
    }

    public static float sin(double a) {
        return (float) Math.sin(a);
    }

    public static float tan(double a) {
        return (float) Math.tan(a);
    }

    public static float random(double high) {
        return random(0, high);
    }

    public static float random(double low, double high) {
        return (float) (low + (high - low) * r.nextDouble());
    }

    public static float randomGaussian() {
        return (float) r.nextGaussian();
    }

    public static void randomSeed(int seed) {
        r = new Random(seed);
    }
    
    // Constants

    public static final float HALF_PI = 1.5707964f;
    public static final float PI = 3.1415927f;
    public static final float QUARTER_PI = 0.7853982f;
    public static final float TAU = 6.2831855f;
    public static final float TWO_PI = 6.2831855f;
    
}