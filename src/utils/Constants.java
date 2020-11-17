package utils;

public class Constants {

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

    public static final int GROUP           = 0;   // createShape()
  
    public static final int POINT           = 2;   // primitive
    public static final int POINTS          = 3;   // vertices
  
    public static final int LINE            = 4;   // primitive
    public static final int LINES           = 5;   // beginShape(), createShape()
    public static final int LINE_STRIP      = 50;  // beginShape()
    public static final int LINE_LOOP       = 51;
  
    public static final int TRIANGLE        = 8;   // primitive
    public static final int TRIANGLES       = 9;   // vertices
    public static final int TRIANGLE_STRIP  = 10;  // vertices
    public static final int TRIANGLE_FAN    = 11;  // vertices
  
    public static final int QUAD            = 16;  // primitive
    public static final int QUADS           = 17;  // vertices
    public static final int QUAD_STRIP      = 18;  // vertices
  
    public static final int POLYGON         = 20;  // in the end, probably cannot
    public static final int PATH            = 21;  // separate these two
  
    public static final int RECT            = 30;  // primitive
    public static final int ELLIPSE         = 31;  // primitive
    public static final int ARC             = 32;  // primitive
  
    public static final int SPHERE          = 40;  // primitive
    public static final int BOX             = 41;  // primitive
    
}