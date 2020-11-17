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
        r = new Renderer(gc, gs);
        gs.screenW = gc.getCanvas().getWidth();
        gs.screenH = gc.getCanvas().getHeight();
    }

    public void updateVars() {
        r.background(backgroundColour);
        r.fill(fillColour);
        r.stroke(strokeColour);
    }

    public void background(int rh, int gs, int bb, int al) {
        updateVars();
    }

    public void fill(int rh, int gs, int bb, int al) {
        updateVars();
    }

    public void stroke(int rb, int gs, int bb, int al) {
        updateVars();
    }

    public void colorMode(int mode, int rh, int gs, int bb, int al) {
        colorMode = mode;
        maxRH = rh;
        maxGS = gs;
        maxBB = bb;
        maxAL = al;
    }

    public Color getColor(int v1, int v2, int v3, int alpha) {
        switch (colorMode) {
            case RGB: {
                int red = (int) map(v1, 0, maxRH, 0, 255);
                int green = (int) map(v2, 0, maxGS, 0, 255);
                int blue = (int) map(v3, 0, maxBB, 0, 255);
                double opacity = map(alpha, 0, maxAL, 0, 1);
                return Color.rgb(red, green, blue, opacity);
            }

            case HSB: {
                double hue = map(v1, 0, maxRH, 0, 359);
                double saturation = map(v2, 0, maxGS, 0, 1);
                double brightness = map(v3, 0, maxBB, 0, 1);
                double opacity = map(alpha, 0, maxAL, 0, 1);
                return Color.hsb(hue, saturation, brightness, opacity);
            }

            default: {
                return Color.WHITE;
            }
        }
    }


}
