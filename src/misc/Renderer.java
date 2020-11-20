package misc;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;

public class Renderer {

    GraphicsContext gc;
    PGraphics pg;
    public GraphicState gs;
    double renderX, renderY;

    public Renderer(GraphicsContext gc, GraphicState gs, PGraphics pg) {
        this.gc = gc;
        this.gs = gs;
        this.pg = pg;
        pg.hasFill = true;
        pg.hasStroke = true;
        fill(Color.WHITE);
    }
    
    public void renderPos(double x, double y) {
        renderX = x;
        renderY = y;
    }

    public void background(Color backgroundColour) {
        gc.save();
        gc.setFill(backgroundColour);
        gc.fillRect(GraphicState.offsetX+renderX, GraphicState.offsetY+renderY, gs.width, gs.height);
        gc.restore();
    }
    
    public void fill(Color fillColour) {
        gc.setFill(fillColour);
}
    
    public void stroke(Color strokeColour) {
        gc.setStroke(strokeColour);
    }

    public void rect(double nx, double ny, double nw, double nh) {
        if (pg.hasFill) gc.fillRect(nx, ny, nw, nh);
        if (pg.hasStroke) gc.strokeRect(nx, ny, nw, nh);
    }

    public void ellipse(double nx, double ny, double nw, double nh) {

    }

    public void text(String value, double x, double y) {
        gc.fillText(value, x, y);
    }

    public void textAlign(TextAlignment alignH, VPos alignV) {
        gc.setTextAlign(alignH);
        gc.setTextBaseline(alignV);
    }

    public void textSize(double newSize) {
         gc.setFont(new Font(newSize));
    }

    public void pushMatrix() {
        if (pg.transformCount == pg.transformStack.length) {
            throw new RuntimeException("StackOverflow: Reached the maximum amount of pushed matrixes");
        } else {
            pg.transformStack[pg.transformCount] = gc.getTransform(pg.transformStack[pg.transformCount]);
            pg.transformCount++;
        }
    }

    public void popMatrix() {
        if (pg.transformCount == 0) {
            throw new RuntimeException("popMatrix() needs corresponding pushMatrix() statement");
        } else {
            pg.transformCount--;
            gc.setTransform(pg.transformStack[pg.transformCount]);
        }
    }

	public void resetMatrix() {
        gc.setTransform(new Affine());
	}

	public void scale(double amtX, double amtY) {
        gc.scale(amtX, amtY);
	}

	public void translate(double amtX, double amtY) {
        gc.translate(amtX, amtY);
	}
}