package utils;

public class FXUtils {

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
    
    public static float constrain(float val, float low, float high) {
		return (val < low) ? low : ((val > high) ? high : val);
    }
    
    public static float dist(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(abs(x2 - x1) * abs(x2 - x1) + abs(y2 - y1) * abs(y2 - y1));
    }

    public static float exp(float n) {
        return (float) Math.exp(n);
    }

    public static int floor(float val) {
		return (int) ((val > 0) ? val - (val % 1) : val - (val % 1) - 1) ;
    }

    public static int floor(double val) {
		return (int) ((val > 0) ? val - (val % 1) : val - (val % 1) - 1) ;
    }

    public static float lerp(float start, float stop, float amt) {
        return start + amt * (stop - amt);
    }

    public static float log(float n) {
        return (float) Math.log(n);
    }
    
    // Constants

    public static final float HALF_PI = 1.5707964f;
    public static final float PI = 3.1415927f;
    public static final float QUARTER_PI = 0.7853982f;
    public static final float TAU = 6.2831855f;
    public static final float TWO_PI = 6.2831855f;
    
}