package utils;

import java.util.Random;

import types.PGraphics;

public class MathUtils {

    /////////////////
    // Calculation //
    /////////////////

    public static final int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public static final double clamp(double val, double min, double max) {
        if (val < min) return min;
        if (val > max) return max;
        return val;
    }

    // abs()

    public static final int abs(int a) {
        return (a < 0) ? -a : a;
    }

    public static final long abs(long a) {
        return (a < 0) ? -a : a;
    }

    public static final double abs(double a) {
        return (a <= 0.0) ? 0.0 - a : a;
    }

    // ceil()

    public static final int ceil(double val) {
        if (val % 1 == 0) return (int) val;
        return (int) ((val > 0) ? val - (val % 1) + 1: val - (val % 1) );
    }

    // constrain()

    public static final int constrain(int val, int low, int high) {
		return (val < low) ? low : ((val > high) ? high : val);
    }
    
    public static final double constrain(double val, double low, double high) {
		return ((val < low) ? low : ((val > high) ? high : val));
    }
    
    // dist()

    public static final double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt(sq(abs(x1 - x2)) + sq(abs(y1 - y2)));
    }
    
    public static final double dist(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.sqrt(sq(abs(x1 - x2)) + sq(abs(y1 - y2)) + sq(abs(z1 - z2)));
    }
    
    // exp()

    public static final double exp(double n) {
        return Math.exp(n);
    }
    
    // floor()

    public static final int floor(double val) {
		return (int) ((val > 0) ? val - (val % 1) : val - (val % 1) - 1) ;
    }
    
    // lerp()

    public static final double lerp(double start, double stop, double amt) {
        return (start + amt * (stop - amt));
    }
    
    // log()

    public static final double log(double n) {
        return Math.log(n);
    }
    
    // mag()

    public static final double mag(double x, double y) {
        return dist(0, 0, x, y);
    }
    
    // map()

    public static final double map(double value, double start1, double stop1, double start2, double stop2) {
        return ((1d/((stop1-start1)/(stop2-start2)))*(value-stop1)+stop2);
    }
    
    // max()

    public static final int max(int a, int b) {
        return (a > b) ? a : b;
    }

    public static final double max(double a, double b) {
        return ((a > b) ? a : b);
    }

    public static final int max(int a, int b, int c) {
        return (a > b) ? ((a > c) ? a : c) : (b > c) ? b : c;
    }

    public static final double max(double a, double b, double c) {
        return ((a > b) ? ((a > c) ? a : c) : (b > c) ? b : c);
    }

    public static final int max(int[] arr) {
        int max = arr[0];
        for (int i : arr) if (i > max) max = i;
        return max;
    }

    public static final double max(double[] arr) {
        double max = arr[0];
        for (double i : arr) if (i > max) max = i;
        return max;
    }
    
    // min()

    public static final int min(int a, int b) {
        return (a < b) ? a : b;
    }

    public static final double min(double a, double b) {
        return ((a < b) ? a : b);
    }

    public static final int min(int a, int b, int c) {
        return (a < b) ? ((a < c) ? a : c) : (b < c) ? b : c;
    }

    public static final double min(double a, double b, double c) {
        return ((a < b) ? ((a < c) ? a : c) : (b < c) ? b : c);
    }

    public static final int min(int[] arr) {
        int min = arr[0];
        for (int i : arr) if (i < min) min = i;
        return min;
    }

    public static final double min(double[] arr) {
        double min = arr[0];
        for (double i : arr) if (i < min) min = i;
        return min;
    }
    
    // norm()

    public static final double norm(double value, double start, double stop) {
        return (value / (stop - start));
    }
    
    // pow()

    public static final double pow(double n, double e) {
        return Math.pow(n, e);
    }
    
    // round()

    public static final int round(double a) {
        return (int) Math.round(a);
    }
    
    // sq()

    public static final double sq(double a) {
        return pow(a,2);
    }
    
    // sqrt()

    public static final double sqrt(double a) {
        return Math.sqrt(a);
    }

    //////////////////
    // Trigonometry //
    //////////////////

    // acos()

    public static final double acos(double a) {
        return Math.acos(a);
    }
    
    // asin()

    public static final double asin(double a) {
        return Math.asin(a);
    }
    
    // atan()

    public static final double atan(double a) {
        return Math.atan(a);
    }
    
    // atan2()

    public static final double atan2(double y, double x) {
        return Math.atan2(y, x);
    }
    
    // cos()

    public static final double cos(double a) {
        return Math.cos(a);
    }
    
    // degrees()

    public static final double degrees(double a) {
        return Math.toDegrees(a);
    }
    
    // radians()

    public static final double radians(double a) {
        return Math.toRadians(a);
    }
    
    // sin()

    public static final double sin(double a) {
        return Math.sin(a);
    }
    
    // tan()

    public static final double tan(double a) {
        return Math.tan(a);
    }

    ////////////
    // Random //
    ////////////

    static Random internalRandom;
    static Random perlinRandom;

    static final int PERLIN_YWRAPB = 4;
    static final int PERLIN_YWRAP = 1 << PERLIN_YWRAPB;
    static final int PERLIN_ZWRAPB = 8;
    static final int PERLIN_ZWRAP = 1 << PERLIN_YWRAPB;
	static final int PERLIN_SIZE = 4095;

    int perlinOctaves = 4;
    double perlinAmpFalloff = 0.5;

    int perlinTwoPi, perlinPi;
    double[] perlinCosTable;
    double[] perlin;

    private static void setupRandom(Random random) {
        if (random == null) random = new Random();
    }

    private double noiseFsc(double i) {
        return 0.5 * (1 - perlinCosTable[(int)(i * perlinPi) % perlinTwoPi]);
    }

    // noise()

    public double noise(double x) {
		return noise(x, 0, 0);
	}

	public double noise(double x, double y) {
		return noise(x, y, 0);
	}

	public double noise(double x, double y, double z) {
		setupRandom(perlinRandom);
        if (perlin == null) {
            perlin = new double[PERLIN_SIZE + 1];
            for (int i = 0; i < PERLIN_SIZE + 1; i++) {
                perlin[i] = perlinRandom.nextDouble();
            }
            perlinCosTable = PGraphics.cosLUT;
            perlinTwoPi = perlinPi = PGraphics.SINCOS_LENGTH;
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
        setupRandom(perlinRandom);
        perlinRandom.setSeed(seed);
        perlin = null;
    }
    
    // random()

    public static final double random(double high) {
        if (high == 0 || high != high) return 0;
        setupRandom(internalRandom);
        double output = 0;
        do {
			output = internalRandom.nextDouble() * high;
		} while (output == high);
        return output;
    }

    public static final double random(double low, double high) {
        if (low >= high) return low;
        double diff = high - low;
        double output = 0;
        do {
			output = random(diff) + low;
		} while (output == high);
		return output;
    }
    
    // randomGaussian()

    public static final double randomGaussian() {
        setupRandom(internalRandom);
        return internalRandom.nextGaussian();
    }

    // randomSeed()

    public static final void randomSeed(long seed) {
        setupRandom(internalRandom);
        internalRandom.setSeed(seed);
    }

    ////////////
    // Custom //
    ////////////

	public static final boolean between(double val, double min, double max) {
        if (val >= min && val <= max) return true;
        return false;
    }

}