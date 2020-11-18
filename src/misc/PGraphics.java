package misc;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.*;

import static utils.Constants.*;
import static utils.MathUtils.*;

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
        background(204, 204, 204, 255);
        fill(255, 255, 255, 255);
        stroke(0, 0, 0, 255);
        rectMode = CORNER;
        ellipseMode = CENTER;
        commands = new ArrayList<CommandNode>();
    }

    void updateVars() {
        r.background(backgroundColour);
        r.fill(fillColour);
        r.stroke(strokeColour);
    }

    public void size(int w, int h) {
        gs.width = w;
        gs.height = h;

        if (isPrimary) {
            GraphicState.setOffset(((gs.screenW - gs.width) / 2), ((gs.screenH - gs.height) / 2));
        }

    }

    public void beginDraw() {
        if (commands == null) {
            commands = new ArrayList<CommandNode>();
        } else {
            commands.clear();
        }
    }
    
    public void endDraw() {
    }

	public void render(double x, double y) {
        r.renderPos(x, y);
        for (CommandNode command : commands) {
            command.execute(r, x, y);
        }
	}

    public void background(int gray) {
        background(gray, gray, gray, (int)maxAL);
    }

    public void background(int gray, int alpha) {
        background(gray, gray, gray, alpha);
    }

    public void background(int rh, int gs, int bb) {
        background(rh, gs, bb, (int)maxAL);
    }

    public void background(int rh, int gs, int bb, int alpha) {
        backgroundColour = getColor(rh, gs, bb, alpha);

        if (isPrimary) {
            r.background(backgroundColour);
        } else {
            commands.add(new CommandNode("background", backgroundColour));
        }
    }

    public void fill(int gray) {
        fill(gray, gray, gray, (int)maxAL);
    }

    public void fill(int gray, int alpha) {
        fill(gray, gray, gray, alpha);
    }

    public void fill(int rh, int gs, int bb) {
        fill(rh, gs, bb, (int)maxAL);
    }

    public void fill(int rh, int gs, int bb, int alpha) {
        fillColour = getColor(rh, gs, bb, alpha);
        hasFill = true;

        if (isPrimary) {
            r.background(fillColour);
        } else {
            commands.add(new CommandNode("fill", fillColour));
        }
    }

    public void stroke(int rh, int gs, int bb, int alpha) {
        strokeColour = getColor(rh, gs, bb, alpha);
        hasStroke = true;

        if (isPrimary) {
            r.stroke(strokeColour);
        } else {
            commands.add(new CommandNode("stroke", strokeColour));
        }
    }

    public void colorMode(int mode, int rh, int gs, int bb, int alpha) {
        colorMode = mode;
        maxRH = rh;
        maxGS = gs;
        maxBB = bb;
        maxAL = alpha;
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
            r.rect(nx, ny, nw, nh);
        } else {
            commands.add(new CommandNode("rect", nx, ny, nw, nh));
        }
    }

    // Utils

    public Color getColor(int rh, int gs, int bb, int alpha) {
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


}
