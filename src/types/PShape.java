package types;

import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.xml.bind.DatatypeConverter;

import main.FXApp;

import static utils.Constants.*;
import static utils.MathUtils.*;
import static utils.DataUtils.*;

public class PShape {
	protected String name;
	protected Map<String,PShape> nameTable;

	//  /** Generic, only draws its child objects. */
	//  static public final int GROUP = 0;
	// GROUP now inherited from PConstants, and is still zero

	// These constants were updated in 3.0b6 so that they could be distinguished
	// from others in PConstants and improve how some typos were handled.
	// https://github.com/processing/processing/issues/3776
	/** A line, ellipse, arc, image, etc. */
	static public final int PRIMITIVE = 101;
	/** A series of vertex, curveVertex, and bezierVertex calls. */
	static public final int PATH = 102;
	/** Collections of vertices created with beginShape(). */
	static public final int GEOMETRY = 103;
	/** The shape type, one of GROUP, PRIMITIVE, PATH, or GEOMETRY. */
	protected int family;

	/** ELLIPSE, LINE, QUAD; TRIANGLE_FAN, QUAD_STRIP; etc. */
	protected int kind;

	protected PMatrix matrix;

	protected int textureMode;

	/** Texture or image data associated with this shape. */
	protected PImage image;
	protected String imagePath = null;

	public static final String OUTSIDE_BEGIN_END_ERROR =
			"%1$s can only be called between beginShape() and endShape()";

	public static final String INSIDE_BEGIN_END_ERROR =
			"%1$s can only be called outside beginShape() and endShape()";

	public static final String NO_SUCH_VERTEX_ERROR =
			"%1$s vertex index does not exist";

	static public final String NO_VERTICES_ERROR =
			"getVertexCount() only works with PATH or GEOMETRY shapes";

	public static final String NOT_A_SIMPLE_VERTEX =
			"%1$s can not be called on quadratic or bezier vertices";

	static public final String PER_VERTEX_UNSUPPORTED =
			"This renderer does not support %1$s for individual vertices";

	/**
	 * ( begin auto-generated from PShape_width.xml )
	 *
	 * The width of the PShape document.
	 *
	 * ( end auto-generated )
	 * @webref pshape:field
	 * @usage web_application
	 * @brief     Shape document width
	 * @see PShape#height
	 */
	public double width;
	/**
	 * ( begin auto-generated from PShape_height.xml )
	 *
	 * The height of the PShape document.
	 *
	 * ( end auto-generated )
	 * @webref pshape:field
	 * @usage web_application
	 * @brief     Shape document height
	 * @see PShape#width
	 */
	public double height;

	public double depth;

	PGraphics g;

	// set to false if the object is hidden in the layers palette
	protected boolean visible = true;

	/** Retained shape being created with beginShape/endShape */
	protected boolean openShape = false;

	protected boolean openContour = false;

	protected boolean stroke;
	protected int strokeColor;
	protected double strokeWeight; // default is 1
	protected int strokeCap;
	protected int strokeJoin;

	protected boolean fill;
	protected int fillColor;

	protected boolean tint;
	protected int tintColor;

	protected int ambientColor;
	protected boolean setAmbient;
	protected int specularColor;
	protected int emissiveColor;
	protected double shininess;

	protected int sphereDetailU, sphereDetailV;
	protected int rectMode;
	protected int ellipseMode;

	/** Temporary toggle for whether styles should be honored. */
	protected boolean style = true;

	/** For primitive shapes in particular, params like x/y/w/h or x1/y1/x2/y2. */
	protected double[] params;

	protected int vertexCount;
	/**
	 * When drawing POLYGON shapes, the second param is an array of length
	 * VERTEX_FIELD_COUNT. When drawing PATH shapes, the second param has only
	 * two variables.
	 */
	protected double[][] vertices;

	protected PShape parent;
	protected int childCount;
	protected PShape[] children;


	/** Array of VERTEX, BEZIER_VERTEX, and CURVE_VERTEX calls. */
	protected int vertexCodeCount;
	protected int[] vertexCodes;
	/** True if this is a closed path. */
	protected boolean close;

	// ........................................................

	// internal color for setting/calculating
	protected double calcR, calcG, calcB, calcA;
	protected int calcRi, calcGi, calcBi, calcAi;
	protected int calcColor;
	protected boolean calcAlpha;

	/** The current colorMode */
	public int colorMode; // = RGB;

	/** Max value for red (or hue) set by colorMode */
	public double colorModeX; // = 255;

	/** Max value for green (or saturation) set by colorMode */
	public double colorModeY; // = 255;

	/** Max value for blue (or value) set by colorMode */
	public double colorModeZ; // = 255;

	/** Max value for alpha set by colorMode */
	public double colorModeA; // = 255;

	/** True if colors are not in the range 0..1 */
	boolean colorModeScale; // = true;

	/** True if colorMode(RGB, 255) */
	boolean colorModeDefault; // = true;

	/** True if contains 3D data */
	protected boolean is3D = false;

	protected boolean perVertexStyles = false;

	// should this be called vertices (consistent with PGraphics internals)
	// or does that hurt flexibility?


	// POINTS, LINES, xLINE_STRIP, xLINE_LOOP
	// TRIANGLES, TRIANGLE_STRIP, TRIANGLE_FAN
	// QUADS, QUAD_STRIP
	// xPOLYGON
	//  static final int PATH = 1;  // POLYGON, LINE_LOOP, LINE_STRIP
	//  static final int GROUP = 2;

	// how to handle rectmode/ellipsemode?
	// are they bitshifted into the constant?
	// CORNER, CORNERS, CENTER, (CENTER_RADIUS?)
	//  static final int RECT = 3; // could just be QUAD, but would be x1/y1/x2/y2
	//  static final int ELLIPSE = 4;
	//
	//  static final int VERTEX = 7;
	//  static final int CURVE = 5;
	//  static final int BEZIER = 6;


	// fill and stroke functions will need a pointer to the parent
	// PGraphics object.. may need some kind of createShape() fxn
	// or maybe the values are stored until draw() is called?

	// attaching images is very tricky.. it's a different type of data

	// material parameters will be thrown out,
	// except those currently supported (kinds of lights)

	// pivot point for transformations
	//  public double px;
	//  public double py;


	/**
	 * @nowebref
	 */
	public PShape() {
		this.family = GROUP;
	}


	/**
	 * @nowebref
	 */
	public PShape(int family) {
		this.family = family;
	}


	/**
	 * @nowebref
	 */
	public PShape(PGraphics g, int family) {
		this.g = g;
		this.family = family;

		// Style parameters are retrieved from the current values in the renderer.
		textureMode = g.textureMode;

		colorMode(g.colorMode,
				g.colorModeX, g.colorModeY, g.colorModeZ, g.colorModeA);

		// Initial values for fill, stroke and tint colors are also imported from
		// the renderer. This is particular relevant for primitive shapes, since is
		// not possible to set their color separately when creating them, and their
		// input vertices are actually generated at rendering time, by which the
		// color configuration of the renderer might have changed.
		fill = g.fill;
		fillColor = g.fillColor;

		stroke = g.stroke;
		strokeColor = g.strokeColor;
		strokeWeight = g.strokeWeight;
		strokeCap = g.strokeCap;
		strokeJoin = g.strokeJoin;

		tint = g.tint;
		tintColor = g.tintColor;

		setAmbient = g.setAmbient;
		ambientColor = g.ambientColor;
		specularColor = g.specularColor;
		emissiveColor = g.emissiveColor;
		shininess = g.shininess;

		sphereDetailU = g.sphereDetailU;
		sphereDetailV = g.sphereDetailV;

		//    bezierDetail = pg.bezierDetail;
		//    curveDetail = pg.curveDetail;
		//    curveTightness = pg.curveTightness;

		rectMode = g.rectMode;
		ellipseMode = g.ellipseMode;

		//    normalX = normalY = 0;
		//    normalZ = 1;
		//
		//    normalMode = NORMAL_MODE_AUTO;

		// To make sure that the first vertex is marked as a break.
		// Same behavior as in the immediate mode.
		//    breakShape = false;

		if (family == GROUP) {
			// GROUP shapes are always marked as ended.
			//      shapeCreated = true;

		}
	}


	public PShape(PGraphics g, int kind, double... params) {
		this(g, PRIMITIVE);
		setKind(kind);
		setParams(params);
	}


	public void setFamily(int family) {
		this.family = family;
	}


	public void setKind(int kind) {
		this.kind = kind;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}

	/**
	 * ( begin auto-generated from PShape_isVisible.xml )
	 *
	 * Returns a boolean value "true" if the image is set to be visible,
	 * "false" if not. This is modified with the <b>setVisible()</b> parameter.
	 * <br/> <br/>
	 * The visibility of a shape is usually controlled by whatever program
	 * created the SVG file. For instance, this parameter is controlled by
	 * showing or hiding the shape in the layers palette in Adobe Illustrator.
	 *
	 * ( end auto-generated )
	 * @webref pshape:method
	 * @usage web_application
	 * @brief Returns a boolean value "true" if the image is set to be visible, "false" if not
	 * @see PShape#setVisible(boolean)
	 */
	public boolean isVisible() {
		return visible;
	}


	/**
	 * ( begin auto-generated from PShape_setVisible.xml )
	 *
	 * Sets the shape to be visible or invisible. This is determined by the
	 * value of the <b>visible</b> parameter.
	 * <br/> <br/>
	 * The visibility of a shape is usually controlled by whatever program
	 * created the SVG file. For instance, this parameter is controlled by
	 * showing or hiding the shape in the layers palette in Adobe Illustrator.
	 *
	 * ( end auto-generated )
	 * @webref pshape:mathod
	 * @usage web_application
	 * @brief Sets the shape to be visible or invisible
	 * @param visible "false" makes the shape invisible and "true" makes it visible
	 * @see PShape#isVisible()
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}


	/**
	 * ( begin auto-generated from PShape_disableStyle.xml )
	 *
	 * Disables the shape's style data and uses Processing's current styles.
	 * Styles include attributes such as colors, stroke weight, and stroke
	 * joints.
	 *
	 * ( end auto-generated )
	 *  <h3>Advanced</h3>
	 * Overrides this shape's style information and uses PGraphics styles and
	 * colors. Identical to ignoreStyles(true). Also disables styles for all
	 * child shapes.
	 * @webref pshape:method
	 * @usage web_application
	 * @brief     Disables the shape's style data and uses Processing styles
	 * @see PShape#enableStyle()
	 */
	public void disableStyle() {
		style = false;

		for (int i = 0; i < childCount; i++) {
			children[i].disableStyle();
		}
	}


	/**
	 * ( begin auto-generated from PShape_enableStyle.xml )
	 *
	 * Enables the shape's style data and ignores Processing's current styles.
	 * Styles include attributes such as colors, stroke weight, and stroke
	 * joints.
	 *
	 * ( end auto-generated )
	 *
	 * @webref pshape:method
	 * @usage web_application
	 * @brief Enables the shape's style data and ignores the Processing styles
	 * @see PShape#disableStyle()
	 */
	public void enableStyle() {
		style = true;

		for (int i = 0; i < childCount; i++) {
			children[i].enableStyle();
		}
	}


	// . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .


	//  protected void checkBounds() {
	//    if (width == 0 || height == 0) {
	//      // calculate bounds here (also take kids into account)
	//      width = 1;
	//      height = 1;
	//    }
	//  }


	/**
	 * Get the width of the drawing area (not necessarily the shape boundary).
	 */
	public double getWidth() {
		//checkBounds();
		return width;
	}


	/**
	 * Get the height of the drawing area (not necessarily the shape boundary).
	 */
	public double getHeight() {
		//checkBounds();
		return height;
	}


	/**
	 * Get the depth of the shape area (not necessarily the shape boundary). Only makes sense for 3D PShape subclasses,
	 * such as PShape3D.
	 */
	public double getDepth() {
		//checkBounds();
		return depth;
	}



	/*

  protected PVector getTop() {
    return getTop(null);
  }


  protected PVector getTop(PVector top) {
    if (top == null) {
      top = new PVector();
    }
    return top;
  }


  protected PVector getBottom() {
    return getBottom(null);
  }


  protected PVector getBottom(PVector bottom) {
    if (bottom == null) {
      bottom = new PVector();
    }
    return bottom;
  }
	 */


	/**
	 * Return true if this shape is 2D. Defaults to true.
	 */
	public boolean is2D() {
		return !is3D;
	}


	/**
	 * Return true if this shape is 3D. Defaults to false.
	 */
	public boolean is3D() {
		return is3D;
	}


	public void set3D(boolean val) {
		is3D = val;
	}


	//  /**
	//   * Return true if this shape requires rendering through OpenGL. Defaults to false.
	//   */
	//  
	//  public boolean isGL() {
	//    return false;
	//  }


	///////////////////////////////////////////////////////////

	//

	// Drawing methods

	public void textureMode(int mode) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "textureMode()");
			return;
		}

		textureMode = mode;
	}

	public void texture(PImage tex) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "texture()");
			return;
		}

		image = tex;
	}

	public void noTexture() {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "noTexture()");
			return;
		}

		image = null;
	}



	protected void solid(boolean solid) {
	}


	/**
	 * @webref shape:vertex
	 * @brief Starts a new contour
	 * @see PShape#endContour()
	 */
	public void beginContour() {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "beginContour()");
			return;
		}

		if (family == GROUP) {
			PGraphics.showWarning("Cannot begin contour in GROUP shapes");
			return;
		}

		if (openContour) {
			PGraphics.showWarning("Already called beginContour().");
			return;
		}
		openContour = true;
		beginContourImpl();
	}


	protected void beginContourImpl() {
		if (vertexCodes == null) {
			vertexCodes = new int[10];
		} else if (vertexCodes.length == vertexCodeCount) {
			vertexCodes = expand(vertexCodes);
		}
		vertexCodes[vertexCodeCount++] = BREAK;
	}


	/**
	 * @webref shape:vertex
	 * @brief Ends a contour
	 * @see PShape#beginContour()
	 */
	public void endContour() {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "endContour()");
			return;
		}

		if (family == GROUP) {
			PGraphics.showWarning("Cannot end contour in GROUP shapes");
			return;
		}

		if (!openContour) {
			PGraphics.showWarning("Need to call beginContour() first.");
			return;
		}
		endContourImpl();
		openContour = false;
	}


	protected void endContourImpl() {
	}


	public void vertex(double x, double y) {
		if (vertices == null) {
			vertices = new double[10][2];
		} else if (vertices.length == vertexCount) {
			vertices = (double[][]) expand(vertices);
		}
		vertices[vertexCount++] = new double[] { x, y };

		if (vertexCodes == null) {
			vertexCodes = new int[10];
		} else if (vertexCodes.length == vertexCodeCount) {
			vertexCodes = expand(vertexCodes);
		}
		vertexCodes[vertexCodeCount++] = VERTEX;

		if (x > width) {
			width = x;
		}
		if (y > height) {
			height = y;
		}
	}


	public void vertex(double x, double y, double u, double v) {
	}


	public void vertex(double x, double y, double z) {
		vertex(x, y);  // maybe? maybe not?
	}


	public void vertex(double x, double y, double z, double u, double v) {
	}


	public void normal(double nx, double ny, double nz) {
	}


	public void attribPosition(String name, double x, double y, double z) {
	}

	public void attribNormal(String name, double nx, double ny, double nz) {
	}


	public void attribColor(String name, int color) {
	}


	public void attrib(String name, double... values) {
	}


	public void attrib(String name, int... values) {
	}


	public void attrib(String name, boolean... values) {
	}


	/**
	 * @webref pshape:method
	 * @brief Starts the creation of a new PShape
	 * @see FXApp#endShape()
	 */
	public void beginShape() {
		beginShape(POLYGON);
	}


	public void beginShape(int kind) {
		this.kind = kind;
		openShape = true;
	}

	/**
	 * @webref pshape:method
	 * @brief Finishes the creation of a new PShape
	 * @see FXApp#beginShape()
	 */
	public void endShape() {
		endShape(OPEN);
	}


	public void endShape(int mode) {
		if (family == GROUP) {
			PGraphics.showWarning("Cannot end GROUP shape");
			return;
		}

		if (!openShape) {
			PGraphics.showWarning("Need to call beginShape() first");
			return;
		}

		close = (mode==CLOSE);

		// this is the state of the shape
		openShape = false;
	}


	//////////////////////////////////////////////////////////////

	// STROKE CAP/JOIN/WEIGHT


	public void strokeWeight(double weight) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "strokeWeight()");
			return;
		}

		strokeWeight = weight;
	}

	public void strokeJoin(int join) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "strokeJoin()");
			return;
		}

		strokeJoin = join;
	}

	public void strokeCap(int cap) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "strokeCap()");
			return;
		}

		strokeCap = cap;
	}


	//////////////////////////////////////////////////////////////

	// FILL COLOR


	public void noFill() {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "noFill()");
			return;
		}

		fill = false;
		fillColor = 0x0;

		if (!setAmbient) {
			ambientColor = fillColor;
		}
	}


	public void fill(int rgb) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "fill()");
			return;
		}

		fill = true;
		colorCalc(rgb);
		fillColor = calcColor;

		if (!setAmbient) {
			ambientColor = fillColor;
		}
	}


	public void fill(int rgb, double alpha) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "fill()");
			return;
		}

		fill = true;
		colorCalc(rgb, alpha);
		fillColor = calcColor;

		if (!setAmbient) {
			ambientColor = fillColor;
		}
	}


	public void fill(double gray) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "fill()");
			return;
		}

		fill = true;
		colorCalc(gray);
		fillColor = calcColor;

		if (!setAmbient) {
			ambientColor = fillColor;
		}
	}


	public void fill(double gray, double alpha) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "fill()");
			return;
		}

		fill = true;
		colorCalc(gray, alpha);
		fillColor = calcColor;

		if (!setAmbient) {
			ambient(fillColor);
			setAmbient = false;
		}

		if (!setAmbient) {
			ambientColor = fillColor;
		}
	}


	public void fill(double x, double y, double z) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "fill()");
			return;
		}

		fill = true;
		colorCalc(x, y, z);
		fillColor = calcColor;

		if (!setAmbient) {
			ambientColor = fillColor;
		}
	}


	public void fill(double x, double y, double z, double a) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "fill()");
			return;
		}

		fill = true;
		colorCalc(x, y, z, a);
		fillColor = calcColor;

		if (!setAmbient) {
			ambientColor = fillColor;
		}
	}


	//////////////////////////////////////////////////////////////

	// STROKE COLOR


	public void noStroke() {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "noStroke()");
			return;
		}

		stroke = false;
	}


	public void stroke(int rgb) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "stroke()");
			return;
		}

		stroke = true;
		colorCalc(rgb);
		strokeColor = calcColor;
	}


	public void stroke(int rgb, double alpha) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "stroke()");
			return;
		}

		stroke = true;
		colorCalc(rgb, alpha);
		strokeColor = calcColor;
	}


	public void stroke(double gray) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "stroke()");
			return;
		}

		stroke = true;
		colorCalc(gray);
		strokeColor = calcColor;
	}


	public void stroke(double gray, double alpha) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "stroke()");
			return;
		}

		stroke = true;
		colorCalc(gray, alpha);
		strokeColor = calcColor;
	}


	public void stroke(double x, double y, double z) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "stroke()");
			return;
		}

		stroke = true;
		colorCalc(x, y, z);
		strokeColor = calcColor;
	}


	public void stroke(double x, double y, double z, double alpha) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "stroke()");
			return;
		}

		stroke = true;
		colorCalc(x, y, z, alpha);
		strokeColor = calcColor;
	}


	//////////////////////////////////////////////////////////////

	// TINT COLOR


	public void noTint() {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "noTint()");
			return;
		}

		tint = false;
	}


	public void tint(int rgb) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "tint()");
			return;
		}

		tint = true;
		colorCalc(rgb);
		tintColor = calcColor;
	}


	public void tint(int rgb, double alpha) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "tint()");
			return;
		}

		tint = true;
		colorCalc(rgb, alpha);
		tintColor = calcColor;
	}


	public void tint(double gray) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "tint()");
			return;
		}

		tint = true;
		colorCalc(gray);
		tintColor = calcColor;
	}


	public void tint(double gray, double alpha) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "tint()");
			return;
		}

		tint = true;
		colorCalc(gray, alpha);
		tintColor = calcColor;
	}


	public void tint(double x, double y, double z) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "tint()");
			return;
		}

		tint = true;
		colorCalc(x, y, z);
		tintColor = calcColor;
	}


	public void tint(double x, double y, double z, double alpha) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "tint()");
			return;
		}

		tint = true;
		colorCalc(x, y, z, alpha);
		tintColor = calcColor;
	}


	//////////////////////////////////////////////////////////////

	// Ambient set/update

	public void ambient(int rgb) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "ambient()");
			return;
		}

		setAmbient = true;
		colorCalc(rgb);
		ambientColor = calcColor;
	}


	public void ambient(double gray) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "ambient()");
			return;
		}

		setAmbient = true;
		colorCalc(gray);
		ambientColor = calcColor;
	}


	public void ambient(double x, double y, double z) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "ambient()");
			return;
		}

		setAmbient = true;
		colorCalc(x, y, z);
		ambientColor = calcColor;
	}


	//////////////////////////////////////////////////////////////

	// Specular set/update

	public void specular(int rgb) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "specular()");
			return;
		}

		colorCalc(rgb);
		specularColor = calcColor;
	}


	public void specular(double gray) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "specular()");
			return;
		}

		colorCalc(gray);
		specularColor = calcColor;
	}


	public void specular(double x, double y, double z) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "specular()");
			return;
		}

		colorCalc(x, y, z);
		specularColor = calcColor;
	}


	//////////////////////////////////////////////////////////////

	// Emissive set/update

	public void emissive(int rgb) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "emissive()");
			return;
		}

		colorCalc(rgb);
		emissiveColor = calcColor;
	}


	public void emissive(double gray) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "emissive()");
			return;
		}

		colorCalc(gray);
		emissiveColor = calcColor;
	}


	public void emissive(double x, double y, double z) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "emissive()");
			return;
		}

		colorCalc(x, y, z);
		emissiveColor = calcColor;
	}


	//////////////////////////////////////////////////////////////

	// Shininess set/update

	public void shininess(double shine) {
		if (!openShape) {
			PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "shininess()");
			return;
		}

		shininess = shine;
	}

	///////////////////////////////////////////////////////////

	//

	// Bezier curves


	public void bezierDetail(int detail) {
	}


	public void bezierVertex(double x2, double y2,
			double x3, double y3,
			double x4, double y4) {
		if (vertices == null) {
			vertices = new double[10][];
		} else if (vertexCount + 2 >= vertices.length) {
			vertices = (double[][]) expand(vertices);
		}
		vertices[vertexCount++] = new double[] { x2, y2 };
		vertices[vertexCount++] = new double[] { x3, y3 };
		vertices[vertexCount++] = new double[] { x4, y4 };

		// vertexCodes must be allocated because a vertex() call is required
		if (vertexCodes.length == vertexCodeCount) {
			vertexCodes = expand(vertexCodes);
		}
		vertexCodes[vertexCodeCount++] = BEZIER_VERTEX;

		if (x4 > width) {
			width = x4;
		}
		if (y4 > height) {
			height = y4;
		}
	}


	public void bezierVertex(double x2, double y2, double z2,
			double x3, double y3, double z3,
			double x4, double y4, double z4) {
	}


	public void quadraticVertex(double cx, double cy,
			double x3, double y3) {
		if (vertices == null) {
			vertices = new double[10][];
		} else if (vertexCount + 1 >= vertices.length) {
			vertices = (double[][]) expand(vertices);
		}
		vertices[vertexCount++] = new double[] { cx, cy };
		vertices[vertexCount++] = new double[] { x3, y3 };

		// vertexCodes must be allocated because a vertex() call is required
		if (vertexCodes.length == vertexCodeCount) {
			vertexCodes = expand(vertexCodes);
		}
		vertexCodes[vertexCodeCount++] = QUADRATIC_VERTEX;

		if (x3 > width) {
			width = x3;
		}
		if (y3 > height) {
			height = y3;
		}
	}


	public void quadraticVertex(double cx, double cy, double cz,
			double x3, double y3, double z3) {
	}


	///////////////////////////////////////////////////////////

	//

	// Catmull-Rom curves

	public void curveDetail(int detail) {
	}

	public void curveTightness(double tightness) {
	}

	public void curveVertex(double x, double y) {
	}

	public void curveVertex(double x, double y, double z) {
	}



	// . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .


	/*
  boolean strokeSaved;
  int strokeColorSaved;
  double strokeWeightSaved;
  int strokeCapSaved;
  int strokeJoinSaved;

  boolean fillSaved;
  int fillColorSaved;

  int rectModeSaved;
  int ellipseModeSaved;
  int shapeModeSaved;
	 */


	protected void pre(PGraphics g) {
		if (matrix != null) {
			g.pushMatrix();
			g.applyMatrix(matrix);
		}

		/*
    strokeSaved = g.stroke;
    strokeColorSaved = g.strokeColor;
    strokeWeightSaved = g.strokeWeight;
    strokeCapSaved = g.strokeCap;
    strokeJoinSaved = g.strokeJoin;

    fillSaved = g.fill;
    fillColorSaved = g.fillColor;

    rectModeSaved = g.rectMode;
    ellipseModeSaved = g.ellipseMode;
    shapeModeSaved = g.shapeMode;
		 */
		if (style) {
			g.pushStyle();
			styles(g);
		}
	}


	protected void styles(PGraphics g) {
		// should not be necessary because using only the int version of color
		//parent.colorMode(PConstants.RGB, 255);

		if (stroke) {
			g.stroke(strokeColor);
			g.strokeWeight(strokeWeight);
			g.strokeCap(strokeCap);
			g.strokeJoin(strokeJoin);
		} else {
			g.noStroke();
		}

		if (fill) {
			//System.out.println("filling " + FXApp.hex(fillColor));
			g.fill(fillColor);
		} else {
			g.noFill();
		}
	}


	protected void post(PGraphics g) {
		//    for (int i = 0; i < childCount; i++) {
		//      children[i].draw(g);
		//    }

		/*

    g.stroke = strokeSaved;
    g.strokeColor = strokeColorSaved;
    g.strokeWeight = strokeWeightSaved;
    g.strokeCap = strokeCapSaved;
    g.strokeJoin = strokeJoinSaved;

    g.fill = fillSaved;
    g.fillColor = fillColorSaved;

    g.ellipseMode = ellipseModeSaved;
		 */

		if (matrix != null) {
			g.popMatrix();
		}

		if (style) {
			g.popStyle();
		}
	}


	////////////////////////////////////////////////////////////////////////
	//
	// Shape copy



	static protected PShape createShape(FXApp parent, PShape src) {
		PShape dest = null;
		if (src.family == GROUP) {
			dest = parent.createShape(GROUP);
			PShape.copyGroup(parent, src, dest);
		} else if (src.family == PRIMITIVE) {
			dest = parent.createShape(src.kind, src.params);
			PShape.copyPrimitive(src, dest);
		} else if (src.family == GEOMETRY) {
			dest = parent.createShape(src.kind);
			PShape.copyGeometry(src, dest);
		} else if (src.family == PATH) {
			dest = parent.createShape(PATH);
			PShape.copyPath(src, dest);
		}
		dest.setName(src.name);
		return dest;
	}



	static protected void copyGroup(FXApp parent, PShape src, PShape dest) {
		copyMatrix(src, dest);
		copyStyles(src, dest);
		copyImage(src, dest);
		for (int i = 0; i < src.childCount; i++) {
			PShape c = PShape.createShape(parent, src.children[i]);
			dest.addChild(c);
		}
	}



	static protected void copyPrimitive(PShape src, PShape dest) {
		copyMatrix(src, dest);
		copyStyles(src, dest);
		copyImage(src, dest);
	}



	static protected void copyGeometry(PShape src, PShape dest) {
		dest.beginShape(src.getKind());

		copyMatrix(src, dest);
		copyStyles(src, dest);
		copyImage(src, dest);

		if (src.style) {
			for (int i = 0; i < src.vertexCount; i++) {
				double[] vert = src.vertices[i];

				dest.fill((int)(vert[PGraphics.A] * 255) << 24 |
						(int)(vert[PGraphics.R] * 255) << 16 |
						(int)(vert[PGraphics.G] * 255) <<  8 |
						(int)(vert[PGraphics.B] * 255));

				// Do we need to copy these as well?
						//        dest.ambient(vert[PGraphics.AR] * 255, vert[PGraphics.AG] * 255, vert[PGraphics.AB] * 255);
				//        dest.specular(vert[PGraphics.SPR] * 255, vert[PGraphics.SPG] * 255, vert[PGraphics.SPB] * 255);
				//        dest.emissive(vert[PGraphics.ER] * 255, vert[PGraphics.EG] * 255, vert[PGraphics.EB] * 255);
				//        dest.shininess(vert[PGraphics.SHINE]);

				if (0 < dist(vert[PGraphics.NX],
						vert[PGraphics.NY],
						vert[PGraphics.NZ], 0, 0, 0)) {
					dest.normal(vert[PGraphics.NX],
							vert[PGraphics.NY],
							vert[PGraphics.NZ]);
				}
				dest.vertex(vert[X], vert[Y], vert[Z],
						vert[PGraphics.U],
						vert[PGraphics.V]);
			}
		} else {
			for (int i = 0; i < src.vertexCount; i++) {
				double[] vert = src.vertices[i];
				if (vert[Z] == 0) {
					dest.vertex(vert[X], vert[Y]);
				} else {
					dest.vertex(vert[X], vert[Y], vert[Z]);
				}
			}
		}

		dest.endShape();
	}



	static protected void copyPath(PShape src, PShape dest) {
		copyMatrix(src, dest);
		copyStyles(src, dest);
		copyImage(src, dest);
		dest.close = src.close;
		dest.setPath(src.vertexCount, src.vertices, src.vertexCodeCount, src.vertexCodes);
	}



	static protected void copyMatrix(PShape src, PShape dest) {
		if (src.matrix != null) {
			dest.applyMatrix(src.matrix);
		}
	}



	static protected void copyStyles(PShape src, PShape dest) {
		dest.ellipseMode = src.ellipseMode;
		dest.rectMode = src.rectMode;

		if (src.stroke) {
			dest.stroke = true;
			dest.strokeColor = src.strokeColor;
			dest.strokeWeight = src.strokeWeight;
			dest.strokeCap = src.strokeCap;
			dest.strokeJoin = src.strokeJoin;
		} else {
			dest.stroke = false;
		}

		if (src.fill) {
			dest.fill = true;
			dest.fillColor = src.fillColor;
		} else {
			dest.fill = false;
		}
	}



	static protected void copyImage(PShape src, PShape dest) {
		if (src.image != null) {
			dest.texture(src.image);
		}
	}



	////////////////////////////////////////////////////////////////////////


	/**
	 * Called by the following (the shape() command adds the g)
	 * PShape s = loadShape("blah.svg");
	 * shape(s);
	 */
	public void draw(PGraphics g) {
		if (visible) {
			pre(g);
			drawImpl(g);
			post(g);
		}
	}


	/**
	 * Draws the SVG document.
	 */
	protected void drawImpl(PGraphics g) {
		if (family == GROUP) {
			drawGroup(g);
		} else if (family == PRIMITIVE) {
			drawPrimitive(g);
		} else if (family == GEOMETRY) {
			// Not same as path: `kind` matters.
			//      drawPath(g);
			drawGeometry(g);
		} else if (family == PATH) {
			drawPath(g);
		}
	}


	protected void drawGroup(PGraphics g) {
		for (int i = 0; i < childCount; i++) {
			children[i].draw(g);
		}
	}


	protected void drawPrimitive(PGraphics g) {
		if (kind == POINT) {
			g.point(params[0], params[1]);

		} else if (kind == LINE) {
			if (params.length == 4) {  // 2D
				g.line(params[0], params[1],
						params[2], params[3]);
			} else {  // 3D
				g.line(params[0], params[1], params[2],
						params[3], params[4], params[5]);
			}

		} else if (kind == TRIANGLE) {
			g.triangle(params[0], params[1],
					params[2], params[3],
					params[4], params[5]);

		} else if (kind == QUAD) {
			g.quad(params[0], params[1],
					params[2], params[3],
					params[4], params[5],
					params[6], params[7]);

		} else if (kind == RECT) {

			if (imagePath != null){
				loadImage(g);
			}
			if (image != null) {
				int oldMode = g.imageMode;
				g.imageMode(CORNER);
				g.image(image, params[0], params[1], params[2], params[3]);
				g.imageMode(oldMode);
			} else {
				int oldMode = g.rectMode;
				g.rectMode(rectMode);
				if (params.length == 4) {
					g.rect(params[0], params[1],
							params[2], params[3]);
				} else if (params.length == 5) {
					g.rect(params[0], params[1],
							params[2], params[3],
							params[4]);
				} else if (params.length == 8) {
					g.rect(params[0], params[1],
							params[2], params[3],
							params[4], params[5],
							params[6], params[7]);
				}
				g.rectMode(oldMode);
			}
		} else if (kind == ELLIPSE) {
			int oldMode = g.ellipseMode;
			g.ellipseMode(ellipseMode);
			g.ellipse(params[0], params[1],
					params[2], params[3]);
			g.ellipseMode(oldMode);

		} else if (kind == ARC) {
			int oldMode = g.ellipseMode;
			g.ellipseMode(ellipseMode);
			if (params.length == 6) {
				g.arc(params[0], params[1],
						params[2], params[3],
						params[4], params[5]);
			} else if (params.length == 7) {
				g.arc(params[0], params[1],
						params[2], params[3],
						params[4], params[5],
						(int) params[6]);
			}
			g.ellipseMode(oldMode);

		} else if (kind == BOX) {
			if (params.length == 1) {
				g.box(params[0]);
			} else {
				g.box(params[0], params[1], params[2]);
			}

		} else if (kind == SPHERE) {
			g.sphere(params[0]);
		}
	}


	protected void drawGeometry(PGraphics g) {
		// get cache object using g.
		g.beginShape(kind);
		if (style) {
			for (int i = 0; i < vertexCount; i++) {
				g.vertex(vertices[i]);
			}
		} else {
			for (int i = 0; i < vertexCount; i++) {
				double[] vert = vertices[i];
				if (vert[Z] == 0) {
					g.vertex(vert[X], vert[Y]);
				} else {
					g.vertex(vert[X], vert[Y], vert[Z]);
				}
			}
		}
		g.endShape(close ? CLOSE : OPEN);
	}


	/*
  protected void drawPath(PGraphics g) {
    g.beginShape();
    for (int j = 0; j < childCount; j++) {
      if (j > 0) g.breakShape();
      int count = children[j].vertexCount;
      double[][] vert = children[j].vertices;
      int[] code = children[j].vertexCodes;

      for (int i = 0; i < count; i++) {
        if (style) {
          if (children[j].fill) {
            g.fill(vert[i][R], vert[i][G], vert[i][B]);
          } else {
            g.noFill();
          }
          if (children[j].stroke) {
            g.stroke(vert[i][R], vert[i][G], vert[i][B]);
          } else {
            g.noStroke();
          }
        }
        g.edge(vert[i][EDGE] == 1);

        if (code[i] == VERTEX) {
          g.vertex(vert[i]);

        } else if (code[i] == BEZIER_VERTEX) {
          double z0 = vert[i+0][Z];
          double z1 = vert[i+1][Z];
          double z2 = vert[i+2][Z];
          if (z0 == 0 && z1 == 0 && z2 == 0) {
            g.bezierVertex(vert[i+0][X], vert[i+0][Y], z0,
                           vert[i+1][X], vert[i+1][Y], z1,
                           vert[i+2][X], vert[i+2][Y], z2);
          } else {
            g.bezierVertex(vert[i+0][X], vert[i+0][Y],
                           vert[i+1][X], vert[i+1][Y],
                           vert[i+2][X], vert[i+2][Y]);
          }
        } else if (code[i] == CURVE_VERTEX) {
          double z = vert[i][Z];
          if (z == 0) {
            g.curveVertex(vert[i][X], vert[i][Y]);
          } else {
            g.curveVertex(vert[i][X], vert[i][Y], z);
          }
        }
      }
    }
    g.endShape();
  }
	 */

	protected void drawPath(PGraphics g) {
		// Paths might be empty (go figure)
		// http://dev.processing.org/bugs/show_bug.cgi?id=982
		if (vertices == null) return;

		boolean insideContour = false;
		g.beginShape();

		if (vertexCodeCount == 0) {  // each point is a simple vertex
			if (vertices[0].length == 2) {  // drawing 2D vertices
				for (int i = 0; i < vertexCount; i++) {
					g.vertex(vertices[i][X], vertices[i][Y]);
				}
			} else {  // drawing 3D vertices
				for (int i = 0; i < vertexCount; i++) {
					g.vertex(vertices[i][X], vertices[i][Y], vertices[i][Z]);
				}
			}

		} else {  // coded set of vertices
			int index = 0;

			if (vertices[0].length == 2) {  // drawing a 2D path
				for (int j = 0; j < vertexCodeCount; j++) {
					switch (vertexCodes[j]) {

					case VERTEX:
						g.vertex(vertices[index][X], vertices[index][Y]);
						index++;
						break;

					case QUADRATIC_VERTEX:
						g.quadraticVertex(vertices[index+0][X], vertices[index+0][Y],
								vertices[index+1][X], vertices[index+1][Y]);
						index += 2;
						break;

					case BEZIER_VERTEX:
						g.bezierVertex(vertices[index+0][X], vertices[index+0][Y],
								vertices[index+1][X], vertices[index+1][Y],
								vertices[index+2][X], vertices[index+2][Y]);
						index += 3;
						break;

					case CURVE_VERTEX:
						g.curveVertex(vertices[index][X], vertices[index][Y]);
						index++;
						break;

					case BREAK:
						if (insideContour) {
							g.endContour();
						}
						g.beginContour();
						insideContour = true;
					}
				}
			} else {  // drawing a 3D path
				for (int j = 0; j < vertexCodeCount; j++) {
					switch (vertexCodes[j]) {

					case VERTEX:
						g.vertex(vertices[index][X], vertices[index][Y], vertices[index][Z]);
						index++;
						break;

					case QUADRATIC_VERTEX:
						g.quadraticVertex(vertices[index+0][X], vertices[index+0][Y], vertices[index+0][Z],
								vertices[index+1][X], vertices[index+1][Y], vertices[index+0][Z]);
						index += 2;
						break;


					case BEZIER_VERTEX:
						g.bezierVertex(vertices[index+0][X], vertices[index+0][Y], vertices[index+0][Z],
								vertices[index+1][X], vertices[index+1][Y], vertices[index+1][Z],
								vertices[index+2][X], vertices[index+2][Y], vertices[index+2][Z]);
						index += 3;
						break;

					case CURVE_VERTEX:
						g.curveVertex(vertices[index][X], vertices[index][Y], vertices[index][Z]);
						index++;
						break;

					case BREAK:
						if (insideContour) {
							g.endContour();
						}
						g.beginContour();
						insideContour = true;
					}
				}
			}
		}
		if (insideContour) {
			g.endContour();
		}
		g.endShape(close ? CLOSE : OPEN);
	}

	private void loadImage(PGraphics g){

		if(this.imagePath.startsWith("data:image")){
			loadBase64Image();
		}

		if(this.imagePath.startsWith("file://")){
			loadFileSystemImage(g);
		}
		this.imagePath = null;
	}

	private void loadFileSystemImage(PGraphics g){
		imagePath = imagePath.substring(7);
		PImage loadedImage = g.parent.loadImage(imagePath);
		if(loadedImage == null){
			System.err.println("Error loading image file: " + imagePath);
		}else{
			setTexture(loadedImage);
		}
	}

	private void loadBase64Image(){
		String[] parts = this.imagePath.split(";base64,");
		String extension = parts[0].substring(11);
		String encodedData = parts[1];

		byte[] decodedBytes = DatatypeConverter.parseBase64Binary(encodedData);

		if(decodedBytes == null){
			System.err.println("Decode Error on image: " + imagePath.substring(0, 20));
			return;
		}

		Image awtImage = new ImageIcon(decodedBytes).getImage();

		if (awtImage instanceof BufferedImage) {
			BufferedImage buffImage = (BufferedImage) awtImage;
			int space = buffImage.getColorModel().getColorSpace().getType();
			if (space == ColorSpace.TYPE_CMYK) {
				return;
			}
		}

		PImage loadedImage = new PImage(awtImage);
		if (loadedImage.width == -1) {
			// error...
		}

		// if it's a .gif image, test to see if it has transparency
		if (extension.equals("gif") || extension.equals("png") ||
				extension.equals("unknown")) {
			loadedImage.checkAlpha();
		}

		setTexture(loadedImage);
	}

	// . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .


	public PShape getParent() {
		return parent;
	}

	/**
	 * @webref
	 * @brief Returns the number of children
	 */
	public int getChildCount() {
		return childCount;
	}


	/** Resize the children[] array to be in line with childCount */
	protected void crop() {
		// https://github.com/processing/processing/issues/3347
		if (children.length != childCount) {
			children = (PShape[]) subset(children, 0, childCount);
		}
	}


	public PShape[] getChildren() {
		crop();
		return children;
	}

	/**
	 * ( begin auto-generated from PShape_getChild.xml )
	 *
	 * Extracts a child shape from a parent shape. Specify the name of the
	 * shape with the <b>target</b> parameter. The shape is returned as a
	 * <b>PShape</b> object, or <b>null</b> is returned if there is an error.
	 *
	 * ( end auto-generated )
	 * @webref pshape:method
	 * @usage web_application
	 * @brief Returns a child element of a shape as a PShape object
	 * @param index the layer position of the shape to get
	 * @see PShape#addChild(PShape)
	 */
	public PShape getChild(int index) {
		crop();
		return children[index];
	}

	/**
	 * @param target the name of the shape to get
	 */
	public PShape getChild(String target) {
		if (name != null && name.equals(target)) {
			return this;
		}
		if (nameTable != null) {
			PShape found = nameTable.get(target);
			if (found != null) return found;
		}
		for (int i = 0; i < childCount; i++) {
			PShape found = children[i].getChild(target);
			if (found != null) return found;
		}
		return null;
	}


	/**
	 * Same as getChild(name), except that it first walks all the way up the
	 * hierarchy to the eldest grandparent, so that children can be found anywhere.
	 */
	public PShape findChild(String target) {
		if (parent == null) {
			return getChild(target);

		} else {
			return parent.findChild(target);
		}
	}


	// can't be just 'add' because that suggests additive geometry
	/**
	 * @webref pshape:method
	 * @brief Adds a new child
	 * @param who any variable of type PShape
	 * @see PShape#getChild(int)
	 */
	public void addChild(PShape who) {
		if (children == null) {
			children = new PShape[1];
		}
		if (childCount == children.length) {
			children = (PShape[]) expand(children);
		}
		children[childCount++] = who;
		who.parent = this;

		if (who.getName() != null) {
			addName(who.getName(), who);
		}
	}


	// adds child who exactly at position idx in the array of children.
	/**
	 * @param idx the layer position in which to insert the new child
	 */
	public void addChild(PShape who, int idx) {
		if (idx < childCount) {
			if (childCount == children.length) {
				children = (PShape[]) expand(children);
			}

			// Copy [idx, childCount - 1] to [idx + 1, childCount]
			for (int i = childCount - 1; i >= idx; i--) {
				children[i + 1] = children[i];
			}
			childCount++;

			children[idx] = who;

			who.parent = this;

			if (who.getName() != null) {
				addName(who.getName(), who);
			}
		}
	}


	/**
	 * Remove the child shape with index idx.
	 */
	public void removeChild(int idx) {
		if (idx < childCount) {
			PShape child = children[idx];

			// Copy [idx + 1, childCount - 1] to [idx, childCount - 2]
			for (int i = idx; i < childCount - 1; i++) {
				children[i] = children[i + 1];
			}
			childCount--;

			if (child.getName() != null && nameTable != null) {
				nameTable.remove(child.getName());
			}
		}
	}


	/**
	 * Add a shape to the name lookup table.
	 */
	public void addName(String nom, PShape shape) {
		if (parent != null) {
			parent.addName(nom, shape);
		} else {
			if (nameTable == null) {
				nameTable = new HashMap<>();
			}
			nameTable.put(nom, shape);
		}
	}


	/**
	 * Returns the index of child who.
	 */
	public int getChildIndex(PShape who) {
		for (int i = 0; i < childCount; i++) {
			if (children[i] == who) {
				return i;
			}
		}
		return -1;
	}


	public PShape getTessellation() {
		return null;
	}


	// . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .


	/** The shape type, one of GROUP, PRIMITIVE, PATH, or GEOMETRY. */
	public int getFamily() {
		return family;
	}


	public int getKind() {
		return kind;
	}


	public double[] getParams() {
		return getParams(null);
	}


	public double[] getParams(double[] target) {
		if (target == null || target.length != params.length) {
			target = new double[params.length];
		}
		arrayCopy(params, target);
		return target;
	}


	public double getParam(int index) {
		return params[index];
	}


	protected void setParams(double[] source) {
		if (params == null) {
			params = new double[source.length];
		}
		if (source.length != params.length) {
			PGraphics.showWarning("Wrong number of parameters");
			return;
		}
		arrayCopy(source, params);
	}


	public void setPath(int vcount, double[][] verts) {
		setPath(vcount, verts, 0, null);
	}


	protected void setPath(int vcount, double[][] verts, int ccount, int[] codes) {
		if (verts == null || verts.length < vcount) return;
		if (0 < ccount && (codes == null || codes.length < ccount)) return;

		int ndim = verts[0].length;
		vertexCount = vcount;
		vertices = new double[vertexCount][ndim];
		for (int i = 0; i < vertexCount; i++) {
			arrayCopy(verts[i], vertices[i]);
		}

		vertexCodeCount = ccount;
		if (0 < vertexCodeCount) {
			vertexCodes = new int[vertexCodeCount];
			arrayCopy(codes, vertexCodes, vertexCodeCount);
		}
	}

	/**
	 * @webref pshape:method
	 * @brief Returns the total number of vertices as an int
	 * @see PShape#getVertex(int)
	 * @see PShape#setVertex(int, double, double)
	 */
	public int getVertexCount() {
		if (family == GROUP || family == PRIMITIVE) {
			PGraphics.showWarning(NO_VERTICES_ERROR);
		}
		return vertexCount;
	}


	/**
	 * @webref pshape:method
	 * @brief Returns the vertex at the index position
	 * @param index the location of the vertex
	 * @see PShape#setVertex(int, double, double)
	 * @see PShape#getVertexCount()
	 */
	public PVector getVertex(int index) {
		return getVertex(index, null);
	}


	/**
	 * @param vec PVector to assign the data to
	 */
	public PVector getVertex(int index, PVector vec) {
		if (vec == null) {
			vec = new PVector();
		}
		double[] vert = vertices[index];
		vec.x = vert[X];
		vec.y = vert[Y];
		if (vert.length > 2) {
			vec.z = vert[Z];
		} else {
			vec.z = 0;  // in case this isn't a new vector
		}
		return vec;
	}


	public double getVertexX(int index) {
		return vertices[index][X];
	}


	public double getVertexY(int index) {
		return vertices[index][Y];
	}


	public double getVertexZ(int index) {
		return vertices[index][Z];
	}


	/**
	 * @webref pshape:method
	 * @brief Sets the vertex at the index position
	 * @param index the location of the vertex
	 * @param x the x value for the vertex
	 * @param y the y value for the vertex
	 * @see PShape#getVertex(int)
	 * @see PShape#getVertexCount()
	 */
	public void setVertex(int index, double x, double y) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setVertex()");
			return;
		}

		vertices[index][X] = x;
		vertices[index][Y] = y;
	}


	/**
	 * @param z the z value for the vertex
	 */
	public void setVertex(int index, double x, double y, double z) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setVertex()");
			return;
		}

		vertices[index][X] = x;
		vertices[index][Y] = y;
		vertices[index][Z] = z;
	}


	/**
	 * @param vec the PVector to define the x, y, z coordinates
	 */
	public void setVertex(int index, PVector vec) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setVertex()");
			return;
		}

		vertices[index][X] = (double) vec.x;
		vertices[index][Y] = (double) vec.y;

		if (vertices[index].length > 2) {
			vertices[index][Z] = (double) vec.z;
		} else if (vec.z != 0 && vec.z == vec.z) {
			throw new IllegalArgumentException("Cannot set a z-coordinate on a 2D shape");
		}
	}


	public PVector getNormal(int index) {
		return getNormal(index, null);
	}


	public PVector getNormal(int index, PVector vec) {
		if (vec == null) {
			vec = new PVector();
		}
		vec.x = vertices[index][PGraphics.NX];
		vec.y = vertices[index][PGraphics.NY];
		vec.z = vertices[index][PGraphics.NZ];
		return vec;
	}


	public double getNormalX(int index) {
		return vertices[index][PGraphics.NX];
	}


	public double getNormalY(int index) {
		return vertices[index][PGraphics.NY];
	}


	public double getNormalZ(int index) {
		return vertices[index][PGraphics.NZ];
	}


	public void setNormal(int index, double nx, double ny, double nz) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setNormal()");
			return;
		}

		vertices[index][PGraphics.NX] = nx;
		vertices[index][PGraphics.NY] = ny;
		vertices[index][PGraphics.NZ] = nz;
	}



	public void setAttrib(String name, int index, double... values) {
	}


	public void setAttrib(String name, int index, int... values) {
	}


	public void setAttrib(String name, int index, boolean... values) {
	}


	public double getTextureU(int index) {
		return vertices[index][PGraphics.U];
	}


	public double getTextureV(int index) {
		return vertices[index][PGraphics.V];
	}


	public void setTextureUV(int index, double u, double v) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setTextureUV()");
			return;
		}

		// make sure we allocated the vertices array and that vertex exists
		if (vertices == null ||
				index >= vertices.length) {
			PGraphics.showWarning(NO_SUCH_VERTEX_ERROR + " (" + index + ")", "setTextureUV()");
			return;
		}


		vertices[index][PGraphics.U] = u;
		vertices[index][PGraphics.V] = v;
	}


	public void setTextureMode(int mode) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setTextureMode()");
			return;
		}

		textureMode = mode;
	}


	public void setTexture(PImage tex) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setTexture()");
			return;
		}

		image = tex;
	}


	public int getFill(int index) {
		// make sure we allocated the vertices array and that vertex exists
		if (vertices == null ||
				index >= vertices.length) {
			PGraphics.showWarning(NO_SUCH_VERTEX_ERROR + " (" + index + ")", "getFill()");
			return fillColor;
		}

		if (image == null) {
			int a = (int) (vertices[index][PGraphics.A] * 255);
			int r = (int) (vertices[index][PGraphics.R] * 255);
			int g = (int) (vertices[index][PGraphics.G] * 255);
			int b = (int) (vertices[index][PGraphics.B] * 255);
			return (a << 24) | (r << 16) | (g << 8) | b;
		} else {
			return 0;
		}
	}

	/**
	 * @nowebref
	 */
	public void setFill(boolean fill) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setFill()");
			return;
		}

		this.fill = fill;
	}

	/**
	 * ( begin auto-generated from PShape_setFill.xml )
	 *
	 * The <b>setFill()</b> method defines the fill color of a <b>PShape</b>.
	 * This method is used after shapes are created or when a shape is defined explicitly
	 * (e.g. <b>createShape(RECT, 20, 20, 80, 80)</b>) as shown in the above example.
	 * When a shape is created with <b>beginShape()</b> and <b>endShape()</b>, its
	 * attributes may be changed with <b>fill()</b> and <b>stroke()</b> within
	 * <b>beginShape()</b> and <b>endShape()</b>. However, after the shape is
	 * created, only the <b>setFill()</b> method can define a new fill value for
	 * the <b>PShape</b>.
	 *
	 * ( end auto-generated )
	 *
	 * @webref
	 * @param fill
	 * @brief Set the fill value
	 */
	public void setFill(int fill) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setFill()");
			return;
		}

		this.fillColor = fill;

		if (vertices != null && perVertexStyles) {
			for (int i = 0; i < vertexCount; i++) {
				setFill(i, fill);
			}
		}
	}

	/**
	 * @nowebref
	 */
	public void setFill(int index, int fill) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setFill()");
			return;
		}

		if (!perVertexStyles) {
			PGraphics.showWarning(PER_VERTEX_UNSUPPORTED, "setFill()");
			return;
		}

		// make sure we allocated the vertices array and that vertex exists
		if (vertices == null || index >= vertices.length) {
			PGraphics.showWarning(NO_SUCH_VERTEX_ERROR + " (" + index + ")", "getFill()");
			return;
		}

		if (image == null) {
			vertices[index][PGraphics.A] = ((fill >> 24) & 0xFF) / 255.0f;
			vertices[index][PGraphics.R] = ((fill >> 16) & 0xFF) / 255.0f;
			vertices[index][PGraphics.G] = ((fill >>  8) & 0xFF) / 255.0f;
			vertices[index][PGraphics.B] = ((fill >>  0) & 0xFF) / 255.0f;
		}
	}


	public int getTint(int index) {
		// make sure we allocated the vertices array and that vertex exists
		if (vertices == null || index >= vertices.length) {
			PGraphics.showWarning(NO_SUCH_VERTEX_ERROR + " (" + index + ")", "getTint()");
			return this.tintColor;
		}

		if (image != null) {
			int a = (int) (vertices[index][PGraphics.A] * 255);
			int r = (int) (vertices[index][PGraphics.R] * 255);
			int g = (int) (vertices[index][PGraphics.G] * 255);
			int b = (int) (vertices[index][PGraphics.B] * 255);
			return (a << 24) | (r << 16) | (g << 8) | b;
		} else {
			return 0;
		}
	}


	public void setTint(boolean tint) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setTint()");
			return;
		}

		this.tint = tint;
	}


	public void setTint(int fill) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setTint()");
			return;
		}

		tintColor = fill;

		if (vertices != null) {
			for  (int i = 0; i < vertices.length; i++) {
				setFill(i, fill);
			}
		}
	}


	public void setTint(int index, int tint) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setTint()");
			return;
		}

		// make sure we allocated the vertices array and that vertex exists
		if (vertices == null ||
				index >= vertices.length) {
			PGraphics.showWarning(NO_SUCH_VERTEX_ERROR + " (" + index + ")", "setTint()");
			return;
		}

		if (image != null) {
			vertices[index][PGraphics.A] = ((tint >> 24) & 0xFF) / 255.0f;
			vertices[index][PGraphics.R] = ((tint >> 16) & 0xFF) / 255.0f;
			vertices[index][PGraphics.G] = ((tint >>  8) & 0xFF) / 255.0f;
			vertices[index][PGraphics.B] = ((tint >>  0) & 0xFF) / 255.0f;
		}
	}


	public int getStroke(int index) {
		// make sure we allocated the vertices array and that vertex exists
		if (vertices == null ||
				index >= vertices.length) {
			PGraphics.showWarning(NO_SUCH_VERTEX_ERROR + " (" + index + ")", "getStroke()");
			return strokeColor;
		}

		int a = (int) (vertices[index][PGraphics.SA] * 255);
		int r = (int) (vertices[index][PGraphics.SR] * 255);
		int g = (int) (vertices[index][PGraphics.SG] * 255);
		int b = (int) (vertices[index][PGraphics.SB] * 255);
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	/**
	 * @nowebref
	 */
	public void setStroke(boolean stroke) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setStroke()");
			return;
		}

		this.stroke = stroke;
	}

	/**
	 * ( begin auto-generated from PShape_setStroke.xml )
	 *
	 * The <b>setStroke()</b> method defines the outline color of a <b>PShape</b>.
	 * This method is used after shapes are created or when a shape is defined
	 * explicitly (e.g. <b>createShape(RECT, 20, 20, 80, 80)</b>) as shown in
	 * the above example. When a shape is created with <b>beginShape()</b> and
	 * <b>endShape()</b>, its attributes may be changed with <b>fill()</b> and
	 * <b>stroke()</b> within <b>beginShape()</b> and <b>endShape()</b>.
	 * However, after the shape is created, only the <b>setStroke()</b> method
	 * can define a new stroke value for the <b>PShape</b>.
	 *
	 * ( end auto-generated )
	 *
	 * @webref
	 * @param stroke
	 * @brief Set the stroke value
	 */
	public void setStroke(int stroke) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setStroke()");
			return;
		}

		strokeColor = stroke;

		if (vertices != null && perVertexStyles) {
			for (int i = 0; i < vertices.length; i++) {
				setStroke(i, stroke);
			}
		}
	}

	/**
	 * @nowebref
	 */
	public void setStroke(int index, int stroke) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setStroke()");
			return;
		}

		if (!perVertexStyles) {
			PGraphics.showWarning(PER_VERTEX_UNSUPPORTED, "setStroke()");
			return;
		}

		// make sure we allocated the vertices array and that vertex exists
		if (vertices == null || index >= vertices.length) {
			PGraphics.showWarning(NO_SUCH_VERTEX_ERROR + " (" + index + ")", "setStroke()");
			return;
		}

		vertices[index][PGraphics.SA] = ((stroke >> 24) & 0xFF) / 255.0f;
		vertices[index][PGraphics.SR] = ((stroke >> 16) & 0xFF) / 255.0f;
		vertices[index][PGraphics.SG] = ((stroke >>  8) & 0xFF) / 255.0f;
		vertices[index][PGraphics.SB] = ((stroke >>  0) & 0xFF) / 255.0f;
	}


	public double getStrokeWeight(int index) {
		// make sure we allocated the vertices array and that vertex exists
		if (vertices == null || index >= vertices.length) {
			PGraphics.showWarning(NO_SUCH_VERTEX_ERROR + " (" + index + ")", "getStrokeWeight()");
			return strokeWeight;
		}

		return vertices[index][PGraphics.SW];
	}


	public void setStrokeWeight(double weight) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setStrokeWeight()");
			return;
		}

		strokeWeight = weight;

		if (vertices != null && perVertexStyles) {
			for (int i = 0; i < vertexCount; i++) {
				setStrokeWeight(i, weight);
			}
		}
	}


	public void setStrokeWeight(int index, double weight) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setStrokeWeight()");
			return;
		}

		if (!perVertexStyles) {
			PGraphics.showWarning(PER_VERTEX_UNSUPPORTED, "setStrokeWeight()");
			return;
		}

		// make sure we allocated the vertices array and that vertex exists
		if (vertices == null || index >= vertices.length) {
			PGraphics.showWarning(NO_SUCH_VERTEX_ERROR + " (" + index + ")", "setStrokeWeight()");
			return;
		}

		vertices[index][PGraphics.SW] = weight;
	}


	public void setStrokeJoin(int join) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setStrokeJoin()");
			return;
		}

		strokeJoin = join;
	}


	public void setStrokeCap(int cap) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setStrokeCap()");
			return;
		}

		strokeCap = cap;
	}


	public int getAmbient(int index) {
		// make sure we allocated the vertices array and that vertex exists
		if (vertices == null || index >= vertices.length) {
			PGraphics.showWarning(NO_SUCH_VERTEX_ERROR + " (" + index + ")", "getAmbient()");
			return ambientColor;
		}

		int r = (int) (vertices[index][PGraphics.AR] * 255);
		int g = (int) (vertices[index][PGraphics.AG] * 255);
		int b = (int) (vertices[index][PGraphics.AB] * 255);
		return 0xff000000 | (r << 16) | (g << 8) | b;
	}


	public void setAmbient(int ambient) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setAmbient()");
			return;
		}

		ambientColor = ambient;

		if (vertices != null) {
			for  (int i = 0; i < vertices.length; i++) {
				setAmbient(i, ambient);
			}
		}
	}


	public void setAmbient(int index, int ambient) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setAmbient()");
			return;
		}

		// make sure we allocated the vertices array and that vertex exists
		if (vertices == null || index >= vertices.length) {
			PGraphics.showWarning(NO_SUCH_VERTEX_ERROR + " (" + index + ")", "setAmbient()");
			return;
		}

		vertices[index][PGraphics.AR] = ((ambient >> 16) & 0xFF) / 255.0f;
		vertices[index][PGraphics.AG] = ((ambient >>  8) & 0xFF) / 255.0f;
		vertices[index][PGraphics.AB] = ((ambient >>  0) & 0xFF) / 255.0f;
	}


	public int getSpecular(int index) {
		// make sure we allocated the vertices array and that vertex exists
		if (vertices == null || index >= vertices.length) {
			PGraphics.showWarning(NO_SUCH_VERTEX_ERROR + " (" + index + ")", "getSpecular()");
			return specularColor;
		}

		int r = (int) (vertices[index][PGraphics.SPR] * 255);
		int g = (int) (vertices[index][PGraphics.SPG] * 255);
		int b = (int) (vertices[index][PGraphics.SPB] * 255);
		return 0xff000000 | (r << 16) | (g << 8) | b;
	}


	public void setSpecular(int specular) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setSpecular()");
			return;
		}

		specularColor = specular;

		if (vertices != null) {
			for  (int i = 0; i < vertices.length; i++) {
				setSpecular(i, specular);
			}
		}
	}


	public void setSpecular(int index, int specular) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setSpecular()");
			return;
		}

		// make sure we allocated the vertices array and that vertex exists
		if (vertices == null || index >= vertices.length) {
			PGraphics.showWarning(NO_SUCH_VERTEX_ERROR + " (" + index + ")", "setSpecular()");
			return;
		}

		vertices[index][PGraphics.SPR] = ((specular >> 16) & 0xFF) / 255.0f;
		vertices[index][PGraphics.SPG] = ((specular >>  8) & 0xFF) / 255.0f;
		vertices[index][PGraphics.SPB] = ((specular >>  0) & 0xFF) / 255.0f;
	}


	public int getEmissive(int index) {
		// make sure we allocated the vertices array and that vertex exists
		if (vertices == null || index >= vertices.length) {
			PGraphics.showWarning(NO_SUCH_VERTEX_ERROR + " (" + index + ")", "getEmissive()");
			return emissiveColor;
		}

		int r = (int) (vertices[index][PGraphics.ER] * 255);
		int g = (int) (vertices[index][PGraphics.EG] * 255);
		int b = (int) (vertices[index][PGraphics.EB] * 255);
		return 0xff000000 | (r << 16) | (g << 8) | b;
	}


	public void setEmissive(int emissive) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setEmissive()");
			return;
		}

		emissiveColor = emissive;

		if (vertices != null) {
			for  (int i = 0; i < vertices.length; i++) {
				setEmissive(i, emissive);
			}
		}
	}


	public void setEmissive(int index, int emissive) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setEmissive()");
			return;
		}

		// make sure we allocated the vertices array and that vertex exists
		if (vertices == null ||
				index >= vertices.length) {
			PGraphics.showWarning(NO_SUCH_VERTEX_ERROR + " (" + index + ")", "setEmissive()");
			return;
		}

		vertices[index][PGraphics.ER] = ((emissive >> 16) & 0xFF) / 255.0f;
		vertices[index][PGraphics.EG] = ((emissive >>  8) & 0xFF) / 255.0f;
		vertices[index][PGraphics.EB] = ((emissive >>  0) & 0xFF) / 255.0f;
	}


	public double getShininess(int index) {
		// make sure we allocated the vertices array and that vertex exists
		if (vertices == null ||
				index >= vertices.length) {
			PGraphics.showWarning(NO_SUCH_VERTEX_ERROR + " (" + index + ")", "getShininess()");
			return shininess;
		}

		return vertices[index][PGraphics.SHINE];
	}


	public void setShininess(double shine) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setShininess()");
			return;
		}

		shininess = shine;

		if (vertices != null) {
			for  (int i = 0; i < vertices.length; i++) {
				setShininess(i, shine);
			}
		}
	}


	public void setShininess(int index, double shine) {
		if (openShape) {
			PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setShininess()");
			return;
		}

		// make sure we allocated the vertices array and that vertex exists
		if (vertices == null ||
				index >= vertices.length) {
			PGraphics.showWarning(NO_SUCH_VERTEX_ERROR + " (" + index + ")", "setShininess()");
			return;
		}


		vertices[index][PGraphics.SHINE] = shine;
	}


	public int[] getVertexCodes() {
		if (vertexCodes == null) {
			return null;
		}
		if (vertexCodes.length != vertexCodeCount) {
			vertexCodes = subset(vertexCodes, 0, vertexCodeCount);
		}
		return vertexCodes;
	}


	public int getVertexCodeCount() {
		return vertexCodeCount;
	}


	/**
	 * One of VERTEX, BEZIER_VERTEX, CURVE_VERTEX, or BREAK.
	 */
	public int getVertexCode(int index) {
		return vertexCodes[index];
	}


	public boolean isClosed() {
		return close;
	}


	// . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .


	/**
	 * Return true if this x, y coordinate is part of this shape. Only works
	 * with PATH shapes or GROUP shapes that contain other GROUPs or PATHs.
	 */
	public boolean contains(double x, double y) {
		if (family == PATH) {
			PVector p = new PVector(x, y);
			if (matrix != null) {
				// apply the inverse transformation matrix to the point coordinates
				PMatrix inverseCoords = matrix.get();

				// commit was https://github.com/processing/processing/commit/027fc7a4f8e8d0a435366eae754304eea282512a
				inverseCoords.invert();  // maybe cache this?
				inverseCoords.invert();  // maybe cache this?
				inverseCoords.mult(new PVector(x, y), p);
			}

			// http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
			boolean c = false;
			for (int i = 0, j = vertexCount-1; i < vertexCount; j = i++) {
				if (((vertices[i][Y] > p.y) != (vertices[j][Y] > p.y)) &&
						(p.x <
								(vertices[j][X]-vertices[i][X]) *
								(y-vertices[i][Y]) /
								(vertices[j][1]-vertices[i][Y]) +
								vertices[i][X])) {
					c = !c;
				}
			}
			return c;

		} else if (family == GROUP) {
			// If this is a group, loop through children until we find one that
			// contains the supplied coordinates. If a child does not support
			// contains() throw a warning and continue.
			for (int i = 0; i < childCount; i++) {
				if (children[i].contains(x, y)) return true;
			}
			return false;

		} else {
			// https://github.com/processing/processing/issues/1280
			throw new IllegalArgumentException("The contains() method is only implemented for paths.");
		}
	}


	// . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .


	// translate, rotate, scale, apply (no push/pop)
	//   these each call matrix.translate, etc
	// if matrix is null when one is called,
	//   it is created and set to identity


	/**
	 * ( begin auto-generated from PShape_translate.xml )
	 *
	 * Specifies an amount to displace the shape. The <b>x</b> parameter
	 * specifies left/right translation, the <b>y</b> parameter specifies
	 * up/down translation, and the <b>z</b> parameter specifies translations
	 * toward/away from the screen. Subsequent calls to the method accumulates
	 * the effect. For example, calling <b>translate(50, 0)</b> and then
	 * <b>translate(20, 0)</b> is the same as <b>translate(70, 0)</b>. This
	 * transformation is applied directly to the shape, it's not refreshed each
	 * time <b>draw()</b> is run.
	 * <br /><br />
	 * Using this method with the <b>z</b> parameter requires using the P3D
	 * parameter in combination with size.
	 *
	 * ( end auto-generated )
	 * @webref pshape:method
	 * @usage web_application
	 * @brief Displaces the shape
	 * @param x left/right translation
	 * @param y up/down translation
	 * @see PShape#rotate(double)
	 * @see PShape#scale(double)
	 * @see PShape#resetMatrix()
	 */
	public void translate(double x, double y) {
		checkMatrix(2);
		matrix.translate(x, y);
	}

	/**
	 * @param z forward/back translation
	 */
	public void translate(double x, double y, double z) {
		checkMatrix(3);
		matrix.translate(x, y, z);
	}

	/**
	 * ( begin auto-generated from PShape_rotateX.xml )
	 *
	 * Rotates a shape around the x-axis the amount specified by the
	 * <b>angle</b> parameter. Angles should be specified in radians (values
	 * from 0 to TWO_PI) or converted to radians with the <b>radians()</b> method.
	 * <br /><br />
	 * Shapes are always rotated around the upper-left corner of their bounding
	 * box. Positive numbers rotate objects in a clockwise direction.
	 * Subsequent calls to the method accumulates the effect. For example,
	 * calling <b>rotateX(HALF_PI)</b> and then <b>rotateX(HALF_PI)</b> is the
	 * same as <b>rotateX(PI)</b>. This transformation is applied directly to
	 * the shape, it's not refreshed each time <b>draw()</b> is run.
	 * <br /><br />
	 * This method requires a 3D renderer. You need to use P3D as a third
	 * parameter for the <b>size()</b> function as shown in the example above.
	 *
	 * ( end auto-generated )
	 * @webref pshape:method
	 * @usage web_application
	 * @brief Rotates the shape around the x-axis
	 * @param angle angle of rotation specified in radians
	 * @see PShape#rotate(double)
	 * @see PShape#rotateY(double)
	 * @see PShape#rotateZ(double)
	 * @see PShape#scale(double)
	 * @see PShape#translate(double, double)
	 * @see PShape#resetMatrix()
	 */
	public void rotateX(double angle) {
		rotate(angle, 1, 0, 0);
	}

	/**
	 * ( begin auto-generated from PShape_rotateY.xml )
	 *
	 * Rotates a shape around the y-axis the amount specified by the
	 * <b>angle</b> parameter. Angles should be specified in radians (values
	 * from 0 to TWO_PI) or converted to radians with the <b>radians()</b> method.
	 * <br /><br />
	 * Shapes are always rotated around the upper-left corner of their bounding
	 * box. Positive numbers rotate objects in a clockwise direction.
	 * Subsequent calls to the method accumulates the effect. For example,
	 * calling <b>rotateY(HALF_PI)</b> and then <b>rotateY(HALF_PI)</b> is the
	 * same as <b>rotateY(PI)</b>. This transformation is applied directly to
	 * the shape, it's not refreshed each time <b>draw()</b> is run.
	 * <br /><br />
	 * This method requires a 3D renderer. You need to use P3D as a third
	 * parameter for the <b>size()</b> function as shown in the example above.
	 *
	 * ( end auto-generated )
	 *
	 * @webref pshape:method
	 * @usage web_application
	 * @brief Rotates the shape around the y-axis
	 * @param angle angle of rotation specified in radians
	 * @see PShape#rotate(double)
	 * @see PShape#rotateX(double)
	 * @see PShape#rotateZ(double)
	 * @see PShape#scale(double)
	 * @see PShape#translate(double, double)
	 * @see PShape#resetMatrix()
	 */
	public void rotateY(double angle) {
		rotate(angle, 0, 1, 0);
	}


	/**
	 * ( begin auto-generated from PShape_rotateZ.xml )
	 *
	 * Rotates a shape around the z-axis the amount specified by the
	 * <b>angle</b> parameter. Angles should be specified in radians (values
	 * from 0 to TWO_PI) or converted to radians with the <b>radians()</b> method.
	 * <br /><br />
	 * Shapes are always rotated around the upper-left corner of their bounding
	 * box. Positive numbers rotate objects in a clockwise direction.
	 * Subsequent calls to the method accumulates the effect. For example,
	 * calling <b>rotateZ(HALF_PI)</b> and then <b>rotateZ(HALF_PI)</b> is the
	 * same as <b>rotateZ(PI)</b>. This transformation is applied directly to
	 * the shape, it's not refreshed each time <b>draw()</b> is run.
	 * <br /><br />
	 * This method requires a 3D renderer. You need to use P3D as a third
	 * parameter for the <b>size()</b> function as shown in the example above.
	 *
	 * ( end auto-generated )
	 * @webref pshape:method
	 * @usage web_application
	 * @brief Rotates the shape around the z-axis
	 * @param angle angle of rotation specified in radians
	 * @see PShape#rotate(double)
	 * @see PShape#rotateX(double)
	 * @see PShape#rotateY(double)
	 * @see PShape#scale(double)
	 * @see PShape#translate(double, double)
	 * @see PShape#resetMatrix()
	 */
	public void rotateZ(double angle) {
		rotate(angle, 0, 0, 1);
	}

	/**
	 * ( begin auto-generated from PShape_rotate.xml )
	 *
	 * Rotates a shape the amount specified by the <b>angle</b> parameter.
	 * Angles should be specified in radians (values from 0 to TWO_PI) or
	 * converted to radians with the <b>radians()</b> method.
	 * <br /><br />
	 * Shapes are always rotated around the upper-left corner of their bounding
	 * box. Positive numbers rotate objects in a clockwise direction.
	 * Transformations apply to everything that happens after and subsequent
	 * calls to the method accumulates the effect. For example, calling
	 * <b>rotate(HALF_PI)</b> and then <b>rotate(HALF_PI)</b> is the same as
	 * <b>rotate(PI)</b>. This transformation is applied directly to the shape,
	 * it's not refreshed each time <b>draw()</b> is run.
	 *
	 * ( end auto-generated )
	 * @webref pshape:method
	 * @usage web_application
	 * @brief Rotates the shape
	 * @param angle angle of rotation specified in radians
	 * @see PShape#rotateX(double)
	 * @see PShape#rotateY(double)
	 * @see PShape#rotateZ(double)
	 * @see PShape#scale(double)
	 * @see PShape#translate(double, double)
	 * @see PShape#resetMatrix()
	 */
	public void rotate(double angle) {
		checkMatrix(2);  // at least 2...
		matrix.rotate(angle);
	}

	/**
	 * @nowebref
	 */
	public void rotate(double angle, double v0, double v1, double v2) {
		checkMatrix(3);
		double norm2 = v0 * v0 + v1 * v1 + v2 * v2;
		if (Math.abs(norm2 - 1) > EPSILON) {
			// The rotation vector is not normalized.
			double norm = (double) sqrt(norm2);
			v0 /= norm;
			v1 /= norm;
			v2 /= norm;
		}
		matrix.rotate(angle, v0, v1, v2);
	}


	//

	/**
	 * ( begin auto-generated from PShape_scale.xml )
	 *
	 * Increases or decreases the size of a shape by expanding and contracting
	 * vertices. Shapes always scale from the relative origin of their bounding
	 * box. Scale values are specified as decimal percentages. For example, the
	 * method call <b>scale(2.0)</b> increases the dimension of a shape by
	 * 200%. Subsequent calls to the method multiply the effect. For example,
	 * calling <b>scale(2.0)</b> and then <b>scale(1.5)</b> is the same as
	 * <b>scale(3.0)</b>. This transformation is applied directly to the shape,
	 * it's not refreshed each time <b>draw()</b> is run.
	 * <br /><br />
	 * Using this method with the <b>z</b> parameter requires using the P3D
	 * parameter in combination with size.
	 *
	 * ( end auto-generated )
	 * @webref pshape:method
	 * @usage web_application
	 * @brief Increases and decreases the size of a shape
	 * @param s percentate to scale the object
	 * @see PShape#rotate(double)
	 * @see PShape#translate(double, double)
	 * @see PShape#resetMatrix()
	 */
	public void scale(double s) {
		checkMatrix(2);  // at least 2...
		matrix.scale(s);
	}


	public void scale(double x, double y) {
		checkMatrix(2);
		matrix.scale(x, y);
	}

	/**
	 * @param x percentage to scale the object in the x-axis
	 * @param y percentage to scale the object in the y-axis
	 * @param z percentage to scale the object in the z-axis
	 */
	public void scale(double x, double y, double z) {
		checkMatrix(3);
		matrix.scale(x, y, z);
	}


	//

	/**
	 * ( begin auto-generated from PShape_resetMatrix.xml )
	 *
	 * Replaces the current matrix of a shape with the identity matrix. The
	 * equivalent function in OpenGL is glLoadIdentity().
	 *
	 * ( end auto-generated )
	 * @webref pshape:method
	 * @brief Replaces the current matrix of a shape with the identity matrix
	 * @usage web_application
	 * @see PShape#rotate(double)
	 * @see PShape#scale(double)
	 * @see PShape#translate(double, double)
	 */
	public void resetMatrix() {
		checkMatrix(2);
		matrix.reset();
	}


	public void applyMatrix(PMatrix source) {
		if (source instanceof PMatrix2D) {
			applyMatrix((PMatrix2D) source);
		} else if (source instanceof PMatrix3D) {
			applyMatrix((PMatrix3D) source);
		}
	}


	public void applyMatrix(PMatrix2D source) {
		applyMatrix(source.m00, source.m01, 0, source.m02,
				source.m10, source.m11, 0, source.m12,
				0, 0, 1, 0,
				0, 0, 0, 1);
	}


	public void applyMatrix(double n00, double n01, double n02,
			double n10, double n11, double n12) {
		checkMatrix(2);
		matrix.apply(n00, n01, n02,
				n10, n11, n12);
	}


	public void applyMatrix(PMatrix3D source) {
		applyMatrix(source.m00, source.m01, source.m02, source.m03,
				source.m10, source.m11, source.m12, source.m13,
				source.m20, source.m21, source.m22, source.m23,
				source.m30, source.m31, source.m32, source.m33);
	}


	public void applyMatrix(double n00, double n01, double n02, double n03,
			double n10, double n11, double n12, double n13,
			double n20, double n21, double n22, double n23,
			double n30, double n31, double n32, double n33) {
		checkMatrix(3);
		matrix.apply(n00, n01, n02, n03,
				n10, n11, n12, n13,
				n20, n21, n22, n23,
				n30, n31, n32, n33);
	}


	//


	/**
	 * Make sure that the shape's matrix is 1) not null, and 2) has a matrix
	 * that can handle <em>at least</em> the specified number of dimensions.
	 */
	protected void checkMatrix(int dimensions) {
		if (matrix == null) {
			if (dimensions == 2) {
				matrix = new PMatrix2D();
			} else {
				matrix = new PMatrix3D();
			}
		} else if (dimensions == 3 && (matrix instanceof PMatrix2D)) {
			// time for an upgrayedd for a double dose of my pimpin'
			matrix = new PMatrix3D(matrix);
		}
	}


	// . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .


	/**
	 * Center the shape based on its bounding box. Can't assume
	 * that the bounding box is 0, 0, width, height. Common case will be
	 * opening a letter size document in Illustrator, and drawing something
	 * in the middle, then reading it in as an svg file.
	 * This will also need to flip the y axis (scale(1, -1)) in cases
	 * like Adobe Illustrator where the coordinates start at the bottom.
	 */
	//  public void center() {
	//  }


	/**
	 * Set the pivot point for all transformations.
	 */
	//  public void pivot(double x, double y) {
	//    px = x;
	//    py = y;
	//  }


	// . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .


	public void colorMode(int mode) {
		colorMode(mode, colorModeX, colorModeY, colorModeZ, colorModeA);
	}

	/**
	 * @param max range for all color elements
	 */
	public void colorMode(int mode, double max) {
		colorMode(mode, max, max, max, max);
	}


	/**
	 * @param maxX range for the red or hue depending on the current color mode
	 * @param maxY range for the green or saturation depending on the current color mode
	 * @param maxZ range for the blue or brightness depending on the current color mode
	 */
	public void colorMode(int mode, double maxX, double maxY, double maxZ) {
		colorMode(mode, maxX, maxY, maxZ, colorModeA);
	}

	/**
	 * @param maxA range for the alpha
	 */
	public void colorMode(int mode,
			double maxX, double maxY, double maxZ, double maxA) {
		colorMode = mode;

		colorModeX = maxX;  // still needs to be set for hsb
		colorModeY = maxY;
		colorModeZ = maxZ;
		colorModeA = maxA;

		// if color max values are all 1, then no need to scale
		colorModeScale =
				((maxA != 1) || (maxX != maxY) || (maxY != maxZ) || (maxZ != maxA));

		// if color is rgb/0..255 this will make it easier for the
		// red() green() etc functions
		colorModeDefault = (colorMode == RGB) &&
				(colorModeA == 255) && (colorModeX == 255) &&
				(colorModeY == 255) && (colorModeZ == 255);
	}


	protected void colorCalc(int rgb) {
		if (((rgb & 0xff000000) == 0) && (rgb <= colorModeX)) {
			colorCalc((double) rgb);

		} else {
			colorCalcARGB(rgb, colorModeA);
		}
	}


	protected void colorCalc(int rgb, double alpha) {
		if (((rgb & 0xff000000) == 0) && (rgb <= colorModeX)) {  // see above
			colorCalc((double) rgb, alpha);

		} else {
			colorCalcARGB(rgb, alpha);
		}
	}


	protected void colorCalc(double gray) {
		colorCalc(gray, colorModeA);
	}


	protected void colorCalc(double gray, double alpha) {
		if (gray > colorModeX) gray = colorModeX;
		if (alpha > colorModeA) alpha = colorModeA;

		if (gray < 0) gray = 0;
		if (alpha < 0) alpha = 0;

		calcR = colorModeScale ? (gray / colorModeX) : gray;
		calcG = calcR;
		calcB = calcR;
		calcA = colorModeScale ? (alpha / colorModeA) : alpha;

		calcRi = (int)(calcR*255); calcGi = (int)(calcG*255);
		calcBi = (int)(calcB*255); calcAi = (int)(calcA*255);
		calcColor = (calcAi << 24) | (calcRi << 16) | (calcGi << 8) | calcBi;
		calcAlpha = (calcAi != 255);
	}


	protected void colorCalc(double x, double y, double z) {
		colorCalc(x, y, z, colorModeA);
	}


	protected void colorCalc(double x, double y, double z, double a) {
		if (x > colorModeX) x = colorModeX;
		if (y > colorModeY) y = colorModeY;
		if (z > colorModeZ) z = colorModeZ;
		if (a > colorModeA) a = colorModeA;

		if (x < 0) x = 0;
		if (y < 0) y = 0;
		if (z < 0) z = 0;
		if (a < 0) a = 0;

		switch (colorMode) {
		case RGB:
			if (colorModeScale) {
				calcR = x / colorModeX;
				calcG = y / colorModeY;
				calcB = z / colorModeZ;
				calcA = a / colorModeA;
			} else {
				calcR = x; calcG = y; calcB = z; calcA = a;
			}
			break;

		case HSB:
			x /= colorModeX; // h
			y /= colorModeY; // s
			z /= colorModeZ; // b

			calcA = colorModeScale ? (a/colorModeA) : a;

			if (y == 0) {  // saturation == 0
				calcR = calcG = calcB = z;

			} else {
				double which = (x - (int)x) * 6.0f;
				double f = which - (int)which;
				double p = z * (1.0f - y);
				double q = z * (1.0f - y * f);
				double t = z * (1.0f - (y * (1.0f - f)));

				switch ((int)which) {
				case 0: calcR = z; calcG = t; calcB = p; break;
				case 1: calcR = q; calcG = z; calcB = p; break;
				case 2: calcR = p; calcG = z; calcB = t; break;
				case 3: calcR = p; calcG = q; calcB = z; break;
				case 4: calcR = t; calcG = p; calcB = z; break;
				case 5: calcR = z; calcG = p; calcB = q; break;
				}
			}
			break;
		}
		calcRi = (int)(255*calcR); calcGi = (int)(255*calcG);
		calcBi = (int)(255*calcB); calcAi = (int)(255*calcA);
		calcColor = (calcAi << 24) | (calcRi << 16) | (calcGi << 8) | calcBi;
		calcAlpha = (calcAi != 255);
	}


	protected void colorCalcARGB(int argb, double alpha) {
		if (alpha == colorModeA) {
			calcAi = (argb >> 24) & 0xff;
			calcColor = argb;
		} else {
			calcAi = (int) (((argb >> 24) & 0xff) * (alpha / colorModeA));
			calcColor = (calcAi << 24) | (argb & 0xFFFFFF);
		}
		calcRi = (argb >> 16) & 0xff;
		calcGi = (argb >> 8) & 0xff;
		calcBi = argb & 0xff;
		calcA = calcAi / 255.0f;
		calcR = calcRi / 255.0f;
		calcG = calcGi / 255.0f;
		calcB = calcBi / 255.0f;
		calcAlpha = (calcAi != 255);
	}

}