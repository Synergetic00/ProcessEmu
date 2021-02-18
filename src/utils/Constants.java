package utils;

import java.awt.Cursor;
import java.awt.event.KeyEvent;

public interface Constants {

	static public final int WIDTH = 1280;
	static public final int HEIGHT = 720;

	static public final int X = 0;
	static public final int Y = 1;
	static public final int Z = 2;

	static final String JAVA2D = "processing.awt.PGraphicsJava2D";
	static final String P2D = "processing.opengl.PGraphics2D";
	static final String P3D = "processing.opengl.PGraphics3D";
    //static final String OPENGL = P3D;
    static final String FX2D = "processing.javafx.PGraphicsFX2D";
	static final String PDF = "processing.pdf.PGraphicsPDF";
	static final String SVG = "processing.svg.PGraphicsSVG";
	static final String DXF = "processing.dxf.RawDXF";

	// platform IDs for PApplet.platform
	static final int OTHER   = 0;
	static final int WINDOWS = 1;
	static final int MACOSX  = 2;
	static final int LINUX   = 3;

	static final String[] platformNames = {
			"other", "windows", "macosx", "linux"
	};

	static final float EPSILON = 0.0001f;

	// max/min values for numbers
    static final float MAX_FLOAT = Float.MAX_VALUE;
	static final float MIN_FLOAT = -Float.MAX_VALUE;
	static final int MAX_INT = Integer.MAX_VALUE;
	static final int MIN_INT = Integer.MIN_VALUE;

	// shapes
	static public final int VERTEX = 0;
	static public final int BEZIER_VERTEX = 1;
	static public final int QUADRATIC_VERTEX = 2;
	static public final int CURVE_VERTEX = 3;
	static public final int BREAK = 4;
    //static public final int QUAD_BEZIER_VERTEX = 2;

	// useful goodness
    static final float PI = (float) Math.PI;
	static final float HALF_PI = (float) (Math.PI / 2.0);
	static final float THIRD_PI = (float) (Math.PI / 3.0);
	static final float QUARTER_PI = (float) (Math.PI / 4.0);
	static final float TWO_PI = (float) (2.0 * Math.PI);
	static final float TAU = (float) (2.0 * Math.PI);
	static final float DEG_TO_RAD = PI/180.0f;
	static final float RAD_TO_DEG = 180.0f/PI;

	// angle modes
	//static final int RADIANS = 0;
	//static final int DEGREES = 1;

	// used by split, all the standard whitespace chars
	// (also includes unicode nbsp, that little bostage)
	static final String WHITESPACE = " \t\n\r\f\u00A0";

	// for colors and/or images
	static final int RGB   = 1;  // image & color
	static final int ARGB  = 2;  // image
	static final int HSB   = 3;  // color
	static final int ALPHA = 4;  // image
	//  static final int CMYK  = 5;  // image & color (someday)

	// image file types
	static final int TIFF  = 0;
	static final int TARGA = 1;
	static final int JPEG  = 2;
	static final int GIF   = 3;

	// filter/convert types
	static final int BLUR      = 11;
	static final int GRAY      = 12;
	static final int INVERT    = 13;
	static final int OPAQUE    = 14;
	static final int POSTERIZE = 15;
	static final int THRESHOLD = 16;
	static final int ERODE     = 17;
	static final int DILATE    = 18;

	// blend mode keyword definitions
	public final static int REPLACE    = 0;
	public final static int BLEND      = 1 << 0;
	public final static int ADD        = 1 << 1;
	public final static int SUBTRACT   = 1 << 2;
	public final static int LIGHTEST   = 1 << 3;
	public final static int DARKEST    = 1 << 4;
	public final static int DIFFERENCE = 1 << 5;
	public final static int EXCLUSION  = 1 << 6;
	public final static int MULTIPLY   = 1 << 7;
	public final static int SCREEN     = 1 << 8;
	public final static int OVERLAY    = 1 << 9;
	public final static int HARD_LIGHT = 1 << 10;
	public final static int SOFT_LIGHT = 1 << 11;
	public final static int DODGE      = 1 << 12;
	public final static int BURN       = 1 << 13;

	// for messages
	static final int CHATTER   = 0;
	static final int COMPLAINT = 1;
	static final int PROBLEM   = 2;

	// types of transformation matrices 
    static final int PROJECTION = 0;
	static final int MODELVIEW  = 1;

	// types of projection matrices
	static final int CUSTOM       = 0; // user-specified fanciness
	static final int ORTHOGRAPHIC = 2; // 2D isometric projection
	static final int PERSPECTIVE  = 3; // perspective matrix

	// shapes - the low four bits set the variety, higher bits set the specific shape type
	static final int GROUP           = 0;   // createShape()
	static final int POINT           = 2;   // primitive
	static final int POINTS          = 3;   // vertices
	static final int LINE            = 4;   // primitive
	static final int LINES           = 5;   // beginShape(), createShape()
	static final int LINE_STRIP      = 50;  // beginShape()
	static final int LINE_LOOP       = 51;
	static final int TRIANGLE        = 8;   // primitive
	static final int TRIANGLES       = 9;   // vertices
	static final int TRIANGLE_STRIP  = 10;  // vertices
	static final int TRIANGLE_FAN    = 11;  // vertices
	static final int QUAD            = 16;  // primitive
	static final int QUADS           = 17;  // vertices
	static final int QUAD_STRIP      = 18;  // vertices
	static final int POLYGON         = 20;  // in the end, probably cannot
	static final int PATH            = 21;  // separate these two
	static final int RECT            = 30;  // primitive
	static final int ELLIPSE         = 31;  // primitive
	static final int ARC             = 32;  // primitive
	static final int SPHERE          = 40;  // primitive
	static final int BOX             = 41;  // primitive
	//static public final int POINT_SPRITES = 52;
	//static public final int NON_STROKED_SHAPE = 60;
	//static public final int STROKED_SHAPE     = 61;

	// shape closing modes
	static final int OPEN = 1;
	static final int CLOSE = 2;

	// shape drawing modes
    static final int CORNER   = 0;
	static final int CORNERS  = 1;
	static final int RADIUS   = 2;
	static final int CENTER   = 3;
	static final int DIAMETER = 3;

	// other arc drawing modes (not open)
	static final int CHORD  = 2;
	static final int PIE    = 3;

	// vertically alignment modes for text
    static final int BASELINE = 0;
	static final int TOP = 101;
	static final int BOTTOM = 102;

	// uv texture orientation modes
	static final int NORMAL     = 1; // texture coordinates in 0..1 range
	static final int IMAGE      = 2; // texture coordinates based on image width/height

	// texture wrapping modes
	public static final int CLAMP = 0; // textures are clamped to their edges
	public static final int REPEAT = 1; // textures wrap around when uv values go outside 0..1 range

	// text placement modes
    static final int MODEL = 4;
    static final int SHAPE = 5;

	// stroke modes
	static final int SQUARE   = 1 << 0;  // called 'butt' in the svg spec
	static final int ROUND    = 1 << 1;
	static final int PROJECT  = 1 << 2;  // called 'square' in the svg spec
	static final int MITER    = 1 << 3;
	static final int BEVEL    = 1 << 5;

	// lighting
	static final int AMBIENT = 0;
	static final int DIRECTIONAL  = 1;
	//static final int POINT  = 2;  // shared with shape feature
	static final int SPOT = 3;

	// key constants
	static final char BACKSPACE = 8;
	static final char TAB       = 9;
	static final char ENTER     = 10;
	static final char RETURN    = 13;
	static final char ESC       = 27;
	static final char DELETE    = 127;

	// i.e. if ((key == CODED) && (keyCode == UP))
	static final int CODED     = 0xffff;

	// key will be CODED and keyCode will be this value
	static final int UP        = KeyEvent.VK_UP;
	static final int DOWN      = KeyEvent.VK_DOWN;
	static final int LEFT      = KeyEvent.VK_LEFT;
	static final int RIGHT     = KeyEvent.VK_RIGHT;

	// key will be CODED and keyCode will be this value
	static final int ALT       = KeyEvent.VK_ALT;
	static final int CONTROL   = KeyEvent.VK_CONTROL;
	static final int SHIFT     = KeyEvent.VK_SHIFT;

	// orientations (only used on Android, ignored on desktop)
	static final int PORTRAIT = 1; // Screen orientation constant for portrait (the hamburger way).
	static final int LANDSCAPE = 2; // Screen orientation constant for landscape (the hot dog way).
	static final int SPAN = 0; // Use with fullScreen() to indicate all available displays.

	// cursor types
	static final int ARROW = Cursor.DEFAULT_CURSOR;
	static final int CROSS = Cursor.CROSSHAIR_CURSOR;
	static final int HAND  = Cursor.HAND_CURSOR;
	static final int MOVE  = Cursor.MOVE_CURSOR;
	static final int TEXT  = Cursor.TEXT_CURSOR;
	static final int WAIT  = Cursor.WAIT_CURSOR;

	static final int ENABLE_NATIVE_FONTS        =  1;
	static final int DISABLE_NATIVE_FONTS       = -1;
	static final int DISABLE_DEPTH_TEST         =  2;
	static final int ENABLE_DEPTH_TEST          = -2;
	static final int ENABLE_DEPTH_SORT          =  3;
	static final int DISABLE_DEPTH_SORT         = -3;
	static final int DISABLE_OPENGL_ERRORS      =  4;
	static final int ENABLE_OPENGL_ERRORS       = -4;
	static final int DISABLE_DEPTH_MASK         =  5;
	static final int ENABLE_DEPTH_MASK          = -5;
	static final int DISABLE_OPTIMIZED_STROKE   =  6;
	static final int ENABLE_OPTIMIZED_STROKE    = -6;
	static final int ENABLE_STROKE_PERSPECTIVE  =  7;
	static final int DISABLE_STROKE_PERSPECTIVE = -7;
	static final int DISABLE_TEXTURE_MIPMAPS    =  8;
	static final int ENABLE_TEXTURE_MIPMAPS     = -8;
	static final int ENABLE_STROKE_PURE         =  9;
	static final int DISABLE_STROKE_PURE        = -9;
	static final int ENABLE_BUFFER_READING      =  10;
	static final int DISABLE_BUFFER_READING     = -10;
	static final int DISABLE_KEY_REPEAT         =  11;
	static final int ENABLE_KEY_REPEAT          = -11;
	static final int DISABLE_ASYNC_SAVEFRAME    =  12;
	static final int ENABLE_ASYNC_SAVEFRAME     = -12;
	static final int HINT_COUNT                 =  13;
}