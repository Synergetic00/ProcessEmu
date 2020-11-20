package misc;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.transform.Affine;
import com.sun.javafx.geom.*;

import static utils.Constants.*;

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
        if (pg.hasFill) gc.fillOval(nx, ny, nw, nh);
        if (pg.hasStroke) gc.strokeOval(nx, ny, nw, nh);
    }

    public void line(double startX, double startY, double endX, double endY) {
        gc.strokeLine(startX, startY, endX, endY);
    }

    private void point(double x, double y) {
        line(x, y, x, y);
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

	public void strokeWeight(double weight) {
        gc.setLineWidth(weight);
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

    public void beginShape(int kind) {
        shape = kind;
        vertexCount = 0;
        curveVertexCount = 0;

        workPath.reset();
        auxPath.reset();
    }

	public void endShape() {

    }
    
    public void vertex(double x, double y) {
        if (vertexCount == vertices.length) {
          double[][] temp = new double[vertexCount<<1][VERTEX_FIELD_COUNT];
          System.arraycopy(vertices, 0, temp, 0, vertexCount);
          vertices = temp;
        }

        vertices[vertexCount][X] = x;
        vertices[vertexCount][Y] = y;
        vertexCount++;
    
        switch (shape) {
            case POINTS: {
                point(x, y);
                break;
            }

            case LINES: {
                if ((vertexCount % 2) == 0) {
                    line(vertices[vertexCount-2][X],
                        vertices[vertexCount-2][Y], x, y);
                }
                break;
            }

            case TRIANGLES: {
                if ((vertexCount % 3) == 0) {
                    triangle(vertices[vertexCount - 3][X],
                        vertices[vertexCount - 3][Y],
                        vertices[vertexCount - 2][X],
                        vertices[vertexCount - 2][Y],
                        x, y);
                }
                break;
            }

            case TRIANGLE_STRIP: {
                System.out.println("Started triangle strip");
                if (vertexCount >= 3) {
                    triangle(vertices[vertexCount - 2][X],
                        vertices[vertexCount - 2][Y],
                        vertices[vertexCount - 1][X],
                        vertices[vertexCount - 1][Y],
                        vertices[vertexCount - 3][X],
                        vertices[vertexCount - 3][Y]);
                }
                System.out.println("Finished triangle strip");
                break;
            }

            case TRIANGLE_FAN: {
                if (vertexCount >= 3) {
                    triangle(vertices[0][X],
                        vertices[0][Y],
                        vertices[vertexCount - 2][X],
                        vertices[vertexCount - 2][Y],
                        x, y);
                }
                break;
            }

            case QUAD: {
                break;
            }

            case QUADS: {
                if ((vertexCount % 4) == 0) {
                    quad(vertices[vertexCount - 4][X],
                        vertices[vertexCount - 4][Y],
                        vertices[vertexCount - 3][X],
                        vertices[vertexCount - 3][Y],
                        vertices[vertexCount - 2][X],
                        vertices[vertexCount - 2][Y],
                        x, y);
                }
                break;
            }

            case QUAD_STRIP: {
                if ((vertexCount >= 4) && ((vertexCount % 2) == 0)) {
                    quad(vertices[vertexCount - 4][X],
                        vertices[vertexCount - 4][Y],
                        vertices[vertexCount - 2][X],
                        vertices[vertexCount - 2][Y],
                        x, y,
                        vertices[vertexCount - 3][X],
                        vertices[vertexCount - 3][Y]);
                }
                break;
            }

            case POLYGON: {
                if (workPath.getNumCommands() == 0 || breakShape) {
                    workPath.moveTo((float) x, (float) y);
                    breakShape = false;
                } else {
                    workPath.lineTo((float) x, (float) y);
                }
                break;
            }
        }
    }

    public void triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        double[] xPoints = {x1, x2, x3};
        double[] yPoints = {y1, y2, y3};
        if (pg.hasFill) gc.fillPolygon(xPoints, yPoints, 3);
        if (pg.hasStroke) gc.strokePolygon(xPoints, yPoints, 3);
        System.out.println("Drew triangle");
    }

    public void quad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double[] xPoints = {x1, x2, x3, x4};
        double[] yPoints = {y1, y2, y3, y4};
        if (pg.hasFill) gc.fillPolygon(xPoints, yPoints, 4);
        if (pg.hasStroke) gc.strokePolygon(xPoints, yPoints, 4);
    }
}