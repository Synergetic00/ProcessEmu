package utils;

import java.util.Random;
import ptypes.PVector;

public class Maths {

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

    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public static double clamp(double val, double min, double max) {
        if (val < min) return min;
        if (val > max) return max;
        return val;
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

    public static int round(double a) {
        return (int) Math.round(a);
    }

    public static double sq(double a) {
        return pow(a,2);
    }

    public static double sqrt(double a) {
        return Math.sqrt(a);
    }

    // Trigonometry

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

    // Random

    static Random internalRandom;
    static Random perlinRandom;

    static final int PERLIN_YWRAPB = 4;
    static final int PERLIN_YWRAP = 1 << PERLIN_YWRAPB;
    static final int PERLIN_ZWRAPB = 8;
    static final int PERLIN_ZWRAP = 1 << PERLIN_YWRAPB;
	static final int PERLIN_SIZE = 4095;

    static int perlinOctaves = 4;
    static double perlinAmpFalloff = 0.5;

    static int perlinTwoPi, perlinPi;
    static double[] perlinCosTable;
    static double[] perlin;

    private static double noiseFsc(double i) {
        return 0.5 * (1 - perlinCosTable[(int)(i * perlinPi) % perlinTwoPi]);
    }

    // noise()

    public static double noise(double x) {
		return noise(x, 0, 0);
	}

	public static double noise(double x, double y) {
		return noise(x, y, 0);
	}

	public static double noise(double x, double y, double z) {
		if (perlinRandom == null) perlinRandom = new Random();
        if (perlin == null) {
            perlin = new double[PERLIN_SIZE + 1];
            for (int i = 0; i < PERLIN_SIZE + 1; i++) {
                perlin[i] = perlinRandom.nextDouble();
            }
            perlinCosTable = new double[0]; //PGraphics.cosLUT;
            perlinTwoPi = perlinPi = 0; //PGraphics.SINCOS_LENGTH;
            perlinPi >>= 1;
        }

        abs(x); abs(y); abs(z);

        int xi = (int) x, yi = (int) y, zi = (int) z;
        double xd = x - xi;
        double yd = y - yi;
        double zd = z - zi;
        double rxd, ryd;
        double r = 0, ampl = 0.5;
        double n1, n2, n3;

        for (int i = 0; i < perlinOctaves; i++) {
            int of = xi + (yi << PERLIN_YWRAPB) + (zi << PERLIN_ZWRAPB);

            rxd = noiseFsc(xd);
            ryd = noiseFsc(yd);

            n1  = perlin[of & PERLIN_SIZE];
            n1 += rxd * (perlin[(of + 1) & PERLIN_SIZE] - n1);
            n2  = perlin[(of + PERLIN_YWRAP) & PERLIN_SIZE];
            n2 += rxd * (perlin[(of + PERLIN_YWRAP + 1) & PERLIN_SIZE] - n2);
            n1 += ryd * (n2 - n1);

            of += PERLIN_ZWRAP;
            n2  = perlin[of&PERLIN_SIZE];
            n2 += rxd * (perlin[(of + 1) & PERLIN_SIZE] - n2);
            n3  = perlin[(of + PERLIN_YWRAP) & PERLIN_SIZE];
            n3 += rxd * (perlin[(of + PERLIN_YWRAP + 1) & PERLIN_SIZE] - n3);
            n2 += ryd * (n3 - n2);

            n1 += noiseFsc(zd) * (n2 - n1);
            r += n1 * ampl;
            ampl *= perlinAmpFalloff;
            xi <<= 1; xd *= 2;
            yi <<= 1; yd *= 2;
            zi <<= 1; zd *= 2;

            if (xd>=1.0f) { xi++; xd--; }
            if (yd>=1.0f) { yi++; yd--; }
            if (zd>=1.0f) { zi++; zd--; }
        }

        return r;

	}

    // noiseDetail()

    public void noiseDetail(int lod) {
        if (lod>0) perlinOctaves=lod;
    }

    public void noiseDetail(int lod, float falloff) {
        if (lod > 0) perlinOctaves = lod;
        if (falloff > 0) perlinAmpFalloff = falloff;
    }

    // noiseSeed()

    public void noiseSeed(long seed) {
        if (perlinRandom == null) perlinRandom = new Random();
        perlinRandom.setSeed(seed);
        perlin = null;
    }

    // random()

    public static final double random(double high) {
        return random(0, high);
    }

    public static final double random(double low, double high) {
        if (internalRandom == null) internalRandom = new Random();
        return (low + (high - low) * internalRandom.nextDouble());
    }

    // randomGaussian()

    public static final double randomGaussian() {
        if (internalRandom == null) internalRandom = new Random();
        return internalRandom.nextGaussian();
    }

    // randomSeed()

    public static final void randomSeed(long seed) {
        if (internalRandom == null) internalRandom = new Random();
        internalRandom.setSeed(seed);
    }

    // Custom

    public static boolean between(double val, double min, double max) {
        if (val >= min && val <= max) return true;
        return false;
    }

    public static boolean inside(double valX, double valY, double x, double y, double w, double h) {
        if (between(valX, x, x + w)) {
            if (between(valY, y, y + h)) {
                return true;
            }
        }
        return false;
    }

    public static PVector centre(double x1, double y1, double x2, double y2) {
        double avgx = (x1 + x2) / 2d;
        double avgy = (y1 + y2) / 2d;
        return new PVector(avgx, avgy);
    }

    public static PVector centre(double x1, double y1, double x2, double y2, double x3, double y3) {
        double avgx = (x1 + x2 + x3) / 3d;
        double avgy = (y1 + y2 + y3) / 3d;
        return new PVector(avgx, avgy);
    }

    public static PVector centre(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double avgx = (x1 + x2 + x3 + x4) / 4d;
        double avgy = (y1 + y2 + y3 + y4) / 4d;
        return new PVector(avgx, avgy);
    }

}