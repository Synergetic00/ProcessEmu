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

    // key constants
	public static final char BACKSPACE = 1;
	public static final char TAB       = 2;
	public static final char ENTER     = 0;
	public static final char RETURN    = 0;
	public static final char ESC       = 10;
	public static final char DELETE    = 127;

	// i.e. if ((key == CODED) && (keyCode == UP))
	public static final int CODED     = 0xffff;

	public static final int UP = 17;
	public static final int DOWN = 19;
    public static final int LEFT = 16;
    public static final int RIGHT = 18;
	public static final int ALT = 7;
	public static final int CONTROL = 6;
	public static final int SHIFT = 5;

    public static final int SQUARE   = 1 << 0;  // called 'butt' in the svg spec
    public static final int ROUND    = 1 << 1;
    public static final int PROJECT  = 1 << 2;  // called 'square' in the svg spec
    public static final int MITER    = 1 << 3;
    public static final int BEVEL    = 1 << 5;

    public static final String WHITESPACE = " \t\n\r\f\u00A0";

}