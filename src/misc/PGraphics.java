package misc;

import java.util.ArrayList;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import main.*;

import com.sun.javafx.geom.*;

import static utils.Constants.*;
import static utils.MathUtils.*;
import static utils.DataUtils.*;

public class PGraphics {
    GraphicsContext gc;
    FXApp parent;
    Renderer r;
    public GraphicState gs;
    ArrayList<CommandNode> commands;
    public boolean isPrimary;

    public Color backgroundColour;
    public Color fillColour;
    public Color strokeColour;

    public boolean hasFill;
    public boolean hasStroke;
    public double strokeWidth;

    public int rectMode;
    public int ellipseMode;
    public int colorMode;

    public double maxRH;
    public double maxGS;
    public double maxBB;
    public double maxAL;

    public PGraphics(GraphicsContext gc, FXApp parent) {
        this.gc = gc;
        this.parent = parent;
        gs = new GraphicState();
        r = new Renderer(gc, gs, this);
        gs.screenW = gc.getCanvas().getWidth();
        gs.screenH = gc.getCanvas().getHeight();
        defaultSettings();
    }

    void defaultSettings() {
        colorMode(RGB, 255, 255, 255, 255);
        rectMode = CORNER;
        ellipseMode = CENTER;
    }

    void updateVars() {
        r.fill(fillColour);
        r.stroke(strokeColour);
        r.textAlign(alignH, alignV);
    }

    public void colorMode(int mode) {
        colorMode(mode, (int)maxRH, (int)maxGS, (int)maxBB, (int)maxAL);
    }

    public void colorMode(int mode, double max) {
        colorMode(mode, max, max, max, (int)maxAL);
    }

    public void colorMode(int mode, double rh, double gs, double bb) {
        colorMode(mode, rh, gs, bb, (int)maxAL);
    }

    public void colorMode(int mode, double rh, double gs, double bb, double alpha) {
        colorMode = mode;
        maxRH = rh;
        maxGS = gs;
        maxBB = bb;
        maxAL = alpha;
    }

    public void size(int w, int h) {
        gs.width = w;
        gs.height = h;

        if (isPrimary) {
            GraphicState.setOffset(((gs.screenW - gs.width) / 2), ((gs.screenH - gs.height) / 2));
        }

    }

    public void beginDraw() {
        System.out.println("Begun drawing");
        if (commands == null) {
            System.out.println("Created list");
            commands = new ArrayList<CommandNode>();
        } else {
            commands.clear();
        }
    }
    
    public void endDraw() {}

    public void rectMode(int mode) {
        rectMode = mode;
    }

    public void ellipseMode(int mode) {
        ellipseMode = mode;
    }

    public void rect(double x, double y, double w, double h) {
        updateVars();
        
        double nx = x;
        double ny = y;
        double nw = w;
        double nh = h;

        switch (rectMode) {
            case CORNERS: {
                nw /= 2;
                nh /= 2;
                break;
            }
            case RADIUS: {
                nw *= 2;
                nh *= 2;
                nx -= nw / 2;
                ny -= nh / 2;
                break;
            }
            case CENTER: {
                nx -= nw / 2;
                ny -= nh / 2;
            }
        }

        if (isPrimary) {
            r.rect(GraphicState.offsetX+nx, GraphicState.offsetY+ny, nw, nh);
        } else {
            commands.add(new CommandNode("rect", GraphicState.offsetX+nx, GraphicState.offsetY+ny, nw, nh));
        }
    }

	public void ellipse(double x, double y, double w, double h) {
        updateVars();
        
        double nx = x;
        double ny = y;
        double nw = w;
        double nh = h;

        switch (ellipseMode) {
            case CORNER: {
                nw *= 2;
                nh *= 2;
                break;
            }
            case RADIUS: {
                nw *= 2;
                nh *= 2;
                nx -= nw / 2;
                ny -= nh / 2;
                break;
            }
            case CENTER: {
                nx -= nw / 2;
                ny -= nh / 2;
                break;
            }
        }

        if (isPrimary) {
            r.ellipse(GraphicState.offsetX+nx, GraphicState.offsetY+ny, nw, nh);
        } else {
            commands.add(new CommandNode("ellipse", GraphicState.offsetX+nx, GraphicState.offsetY+ny, nw, nh));
        }
	}

    public void background(Color color) {
        backgroundColour = color;

        if (isPrimary) {
            r.background(backgroundColour);
        } else {
            commands.add(new CommandNode("background", backgroundColour));
        }
    }

    public void background(double gray) {
        background(gray, gray, gray, maxAL);
    }

    public void background(double gray, double alpha) {
        background(gray, gray, gray, alpha);
    }

    public void background(double rh, double gs, double bb) {
        background(rh, gs, bb, maxAL);
    }

    public void background(double rh, double gs, double bb, double alpha) {
        backgroundColour = getColor(rh, gs, bb, alpha);

        if (isPrimary) {
            r.background(backgroundColour);
        } else {
            commands.add(new CommandNode("background", backgroundColour));
        }
    }

    public void noFill() {
        hasFill = false;
    }

    public void fill(Color color) {
        this.fillColour = color;
        hasFill = true;

        if (isPrimary) {
            r.fill(fillColour);
        } else {
            commands.add(new CommandNode("fill", fillColour));
        }
    }

    public void fill(double gray) {
        fill(gray, gray, gray, (int)maxAL);
    }

    public void fill(double gray, double alpha) {
        fill(gray, gray, gray, alpha);
    }

    public void fill(double rh, double gs, double bb) {
        fill(rh, gs, bb, maxAL);
    }

    public void fill(double rh, double gs, double bb, double alpha) {
        this.fillColour = getColor(rh, gs, bb, alpha);
        hasFill = true;

        if (isPrimary) {
            r.fill(fillColour);
        } else {
            commands.add(new CommandNode("fill", fillColour));
        }
    }

    public void noStroke() {
        hasStroke = false;
    }

    public void stroke(Color color) {
        this.strokeColour = color;
        hasStroke = true;

        if (isPrimary) {
            r.stroke(strokeColour);
        } else {
            commands.add(new CommandNode("stroke", strokeColour));
        }
    }

    public void stroke(double gray) {
        stroke(gray, gray, gray, (int)maxAL);
    }

    public void stroke(double gray, double alpha) {
        stroke(gray, gray, gray, alpha);
    }

    public void stroke(double rh, double gs, double bb) {
        stroke(rh, gs, bb, (int)maxAL);
    }

    public void stroke(double rh, double gs, double bb, double alpha) {
        this.strokeColour = getColor(rh, gs, bb, alpha);
        hasStroke = true;

        if (isPrimary) {
            r.stroke(strokeColour);
        } else {
            commands.add(new CommandNode("stroke", strokeColour));
        }
    }

    TextAlignment alignH = TextAlignment.LEFT;
    VPos alignV = VPos.BASELINE;

    public void textAlign(int newAlignX) {

        switch (newAlignX) {
            case LEFT: {
                alignH = TextAlignment.LEFT;
                break;
            }
            case CENTER: {
                alignH = TextAlignment.CENTER;
                break;
            }
            case RIGHT: {
                alignH = TextAlignment.RIGHT;
                break;
            }
        }

        if (isPrimary) {
            r.textAlign(alignH, alignV);
        } else {
            commands.add(new CommandNode("textAlign", alignH, alignV));
        }
    }

    public void textAlign(int newAlignX, int newAlignY) {
        textAlign(newAlignX);

        switch (newAlignY) {
            case TOP: {
                alignV = VPos.TOP;
                break;
            }
            case BOTTOM: {
                alignV = VPos.BOTTOM;
                break;
            }
            case CENTER: {
                alignV = VPos.CENTER;
                break;
            }
            case BASELINE: {
                alignV = VPos.BASELINE;
                break;
            }
        }

        if (isPrimary) {
            r.textAlign(alignH, alignV);
        } else {
            commands.add(new CommandNode("textAlign", alignH, alignV));
        }
    }

    public void textSize(double newSize) {

        if (isPrimary) {
            r.textSize(newSize);
        } else {
            commands.add(new CommandNode("textSize", newSize));
        }
    }

    public void text(int value, double x, double y) {
        text(nfs(value, 3), x, y);
    }

    public void text(double value, double x, double y) {
        text(value+"", x, y);
    }

    public void text(String value, double x, double y) {
        updateVars();

        if (isPrimary) {
            r.text(value, GraphicState.offsetX+x, GraphicState.offsetY+y);
        } else {
            commands.add(new CommandNode("text", value, GraphicState.offsetX+x, GraphicState.offsetY+y));
        }
    }

    public void strokeWeight(double weight) {
        if (isPrimary) {
            r.strokeWeight(weight);
        } else {
            commands.add(new CommandNode("strokeWeight", weight));
        }
    }

    public void line(double startX, double startY, double endX, double endY) {
        if (isPrimary) {
            r.line(startX, startY, endX, endY);
        } else {
            commands.add(new CommandNode("line", GraphicState.offsetX+startX, GraphicState.offsetY+startY, GraphicState.offsetX+endX, GraphicState.offsetY+endY));
        }
    }

    int shape;
    final int DEFAULT_VERTICES = 512;
    final int VERTEX_FIELD_COUNT = 37;
    double[][] vertices = new double[DEFAULT_VERTICES][VERTEX_FIELD_COUNT];
    int vertexCount;
    boolean openContour;
    boolean adjustedForThinLines;
    /// break the shape at the next vertex (next vertex() call is a moveto())
    boolean breakShape;
    float[] pathCoordsBuffer = new float[6];
    Path2D workPath = new Path2D();
    Path2D auxPath = new Path2D();

    double[][] curveVertices;
    int curveVertexCount;

	public void beginShape(int type) {
        shape = type;
        vertexCount = 0;
        curveVertexCount = 0;
    
        workPath.reset();
        auxPath.reset();  
	}

    public void beginShape() {

    }

    public void vertex(double x, double y) {
        if (isPrimary) {
            r.vertex(x, y);
        } else {
            commands.add(new CommandNode("vertex", GraphicState.offsetX+x, GraphicState.offsetY+y));
        }
    }

    public void endShape() {
        endShape(CLOSE);
    }

	public void endShape(int mode) {
        if (isPrimary) {
            r.endShape(mode);
        } else {
            commands.add(new CommandNode("endShape", mode));
        }
	}

    final int MATRIX_STACK_DEPTH = 32;
    int transformCount;
    Affine[] transformStack = new Affine[MATRIX_STACK_DEPTH];

    public void pushMatrix() {
        if (isPrimary) {
            r.pushMatrix();
        } else {
            commands.add(new CommandNode("pushMatrix"));
        }
    }

    public void popMatrix() {
        if (isPrimary) {
            r.popMatrix();
        } else {
            commands.add(new CommandNode("popMatrix"));
        }
    }

    public void resetMatrix() {
        if (isPrimary) {
            r.resetMatrix();
        } else {
            commands.add(new CommandNode("resetMatrix"));
        }
    }
    
    public void scale(double amt) {
        scale(amt, amt);
    }
    
    public void scale(double amtX, double amtY) {
        if (isPrimary) {
            r.scale(amtX, amtY);
        } else {
            commands.add(new CommandNode("scale", amtX, amtY));
        }
    }

    public void translate(double amtX, double amtY) {
        if (isPrimary) {
            r.translate(amtX, amtY);
        } else {
            commands.add(new CommandNode("translate", amtX, amtY));
        }
    }

    public Color getColor(double rh, double gs, double bb, double alpha) {
        switch (colorMode) {
            case RGB: {
                int red = (int) map(rh, 0, maxRH, 0, 255);
                int green = (int) map(gs, 0, maxGS, 0, 255);
                int blue = (int) map(bb, 0, maxBB, 0, 255);
                double opacity = map(alpha, 0, maxAL, 0, 1);
                return Color.rgb(red, green, blue, opacity);
            }

            case HSB: {
                double hue = map(rh, 0, maxRH, 0, 359);
                double saturation = map(gs, 0, maxGS, 0, 1);
                double brightness = map(bb, 0, maxBB, 0, 1);
                double opacity = map(alpha, 0, maxAL, 0, 1);
                return Color.hsb(hue, saturation, brightness, opacity);
            }

            default: {
                return Color.WHITE;
            }
        }
    }

	public void render(double x, double y) {
        System.out.println("Renderering");
        r.renderPos(x, y);
        for (CommandNode command : commands) {
            command.execute(r, x, y);
        }
	}

	public void render(double x, double y, double w, double h) {
        double scaleX = w / gs.width;
        double scaleY = h / gs.height;
        r.pushMatrix();
        r.scale(scaleX, scaleY);
        double transX = GraphicState.offsetX * (1-scaleX);
        double transY = GraphicState.offsetY * (1-scaleY);
        transX *= (1/scaleX);
        transY *= (1/scaleY);
        transX += x * ((1/scaleX)-1);
        transY += y * ((1/scaleY)-1);
        r.translate(transX, transY);
        r.renderPos(x, y);
        for (CommandNode command : commands) {
            command.execute(r, x, y);
        }
        r.popMatrix();
	}

}