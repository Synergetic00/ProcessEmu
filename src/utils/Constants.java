package utils;

public class Constants {

    private static int screenW;
    private static int screenH;

    public static int screenW() {
   return screenW;
    }

    public static int screenH() {
   return screenH;
    }

    public static void screenW(int screenW) {
   Constants.screenW = screenW;
    }

    public static void screenH(int screenH) {
   Constants.screenH = screenH;
    }

    public static int offsetW;
    public static int offsetH;

    public static int offsetW() {
   return offsetW;
    }

    public static int offsetH() {
   return offsetH;
    }

    public static void offsetW(int offsetW) {
   Constants.offsetW = offsetW;
    }

    public static void offsetH(int offsetH) {
   Constants.offsetH = offsetH;
    }

    public static final int RGB = 0;
	public static final int HSB = 1;

	public static final int ARROW = 0;
	public static final int CROSS = 1;
	public static final int HAND = 2;
	public static final int MOVE = 3;
	public static final int TEXT = 4;
	public static final int WAIT = 5;

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

    static public final int X = 0;
    static public final int Y = 1;
    static public final int Z = 2;

    public static final int CLOSE = 2;

    public static final int GROUP = 0;
  
    public static final int POINT = 2; 
    public static final int POINTS = 3; 
  
    public static final int LINE = 4; 
    public static final int LINES = 5;
    public static final int LINE_STRIP = 50;
    public static final int LINE_LOOP = 51;
  
    public static final int TRIANGLE = 8; 
    public static final int TRIANGLES = 9; 
    public static final int TRIANGLE_STRIP = 10;
    public static final int TRIANGLE_FAN = 11;
  
    public static final int QUAD = 16;
    public static final int QUADS = 17;
    public static final int QUAD_STRIP = 18;
  
    public static final int POLYGON = 20;
    public static final int PATH = 21;
  
    public static final int RECT = 30;
    public static final int ELLIPSE = 31;
    public static final int ARC = 32;
  
    public static final int SPHERE = 40;
    public static final int BOX = 41;
    
}