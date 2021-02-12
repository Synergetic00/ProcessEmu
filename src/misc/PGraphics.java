package misc;

import java.util.ArrayList;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import main.*;

import static utils.Constants.*;
import static utils.MathUtils.*;
import static utils.DataUtils.*;
import static utils.ColourUtils.*;

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

    public int bColour, fColour, sColour;

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

    public void setBackground(int encodedValue) {
        if (isPrimary) {
            r.setBackground(decodeColour(encodedValue));
        } else {
            commands.add(new CommandNode("background", decodeColour(encodedValue)));
        }
    }

    public void setFill(int encodedValue) {
        if (isPrimary) {
            r.setFill(decodeColour(encodedValue));
        } else {
            commands.add(new CommandNode("fill", decodeColour(encodedValue)));
        }
    }

    public void setStroke(int encodedValue) {
        if (isPrimary) {
            r.setStroke(decodeColour(encodedValue));
        } else {
            commands.add(new CommandNode("stroke", decodeColour(encodedValue)));
        }
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

    public void noFill() { hasFill = false; }
    public void noStroke() { hasStroke = false; }

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
        updateVars();
        if (isPrimary) {
            r.line(GraphicState.offsetX+startX, GraphicState.offsetY+startY, GraphicState.offsetX+endX, GraphicState.offsetY+endY);
        } else {
            commands.add(new CommandNode("line", GraphicState.offsetX+startX, GraphicState.offsetY+startY, GraphicState.offsetX+endX, GraphicState.offsetY+endY));
        }
    }

	public void beginShape(int type) {
        if (isPrimary) {
            r.beginShape(type);
        }
	}

    public void beginShape() {

    }

    public void vertex(double x, double y) {
        updateVars();
        if (isPrimary) {
            r.vertex(GraphicState.offsetX+x, GraphicState.offsetY+y);
        } else {
            commands.add(new CommandNode("vertex", GraphicState.offsetX+x, GraphicState.offsetY+y));
        }
    }

    public void endShape() {
        if (isPrimary) {
            r.endShape();
        } else {
            commands.add(new CommandNode("endShape"));
        }
    }

	public void endShape(int mode) {
        if (isPrimary) {
            //r.endShape(mode);
        } else {
            //commands.add(new CommandNode("endShape", mode));
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

	public void triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        if (isPrimary) {
            r.triangle(GraphicState.offsetX+x1, GraphicState.offsetY+y1, GraphicState.offsetX+x2, GraphicState.offsetY+y2, GraphicState.offsetX+x3, GraphicState.offsetY+y3);
        } else {
            //commands.add(new CommandNode("translate", amtX, amtY));
        }
    }

}