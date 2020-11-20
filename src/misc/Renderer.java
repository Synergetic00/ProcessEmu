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
    
    public void vertex(double x, double y) {
        if (pg.vertexCount == pg.vertices.length) {
          double[][] temp = new double[pg.vertexCount<<1][pg.VERTEX_FIELD_COUNT];
          System.arraycopy(pg.vertices, 0, temp, 0, pg.vertexCount);
          pg.vertices = temp;
        }

        pg.vertices[pg.vertexCount][X] = x;
        pg.vertices[pg.vertexCount][Y] = y;
        pg.vertexCount++;
    
        switch (pg.shape) {
            case POINTS: {
                point(x, y);
                break;
            }

            case LINES: {
                if ((pg.vertexCount % 2) == 0) {
                    line(pg.vertices[pg.vertexCount-2][X],
                        pg.vertices[pg.vertexCount-2][Y], x, y);
                }
                break;
            }

            case TRIANGLES: {
                if ((pg.vertexCount % 3) == 0) {
                    triangle(pg.vertices[pg.vertexCount - 3][X],
                        pg.vertices[pg.vertexCount - 3][Y],
                        pg.vertices[pg.vertexCount - 2][X],
                        pg.vertices[pg.vertexCount - 2][Y],
                        x, y);
                }
                break;
            }

            case TRIANGLE_STRIP: {
                if (pg.vertexCount >= 3) {
                    triangle(pg.vertices[pg.vertexCount - 2][X],
                        pg.vertices[pg.vertexCount - 2][Y],
                        pg.vertices[pg.vertexCount - 1][X],
                        pg.vertices[pg.vertexCount - 1][Y],
                        pg.vertices[pg.vertexCount - 3][X],
                        pg.vertices[pg.vertexCount - 3][Y]);
                }
                break;
            }

            case TRIANGLE_FAN: {
                if (pg.vertexCount >= 3) {
                    triangle(pg.vertices[0][X],
                        pg.vertices[0][Y],
                        pg.vertices[pg.vertexCount - 2][X],
                        pg.vertices[pg.vertexCount - 2][Y],
                        x, y);
                }
                break;
            }

            case QUAD: {
                break;
            }

            case QUADS: {
                if ((pg.vertexCount % 4) == 0) {
                    quad(pg.vertices[pg.vertexCount - 4][X],
                        pg.vertices[pg.vertexCount - 4][Y],
                        pg.vertices[pg.vertexCount - 3][X],
                        pg.vertices[pg.vertexCount - 3][Y],
                        pg.vertices[pg.vertexCount - 2][X],
                        pg.vertices[pg.vertexCount - 2][Y],
                        x, y);
                }
                break;
            }

            case QUAD_STRIP: {
                if ((pg.vertexCount >= 4) && ((pg.vertexCount % 2) == 0)) {
                    quad(pg.vertices[pg.vertexCount - 4][X],
                        pg.vertices[pg.vertexCount - 4][Y],
                        pg.vertices[pg.vertexCount - 2][X],
                        pg.vertices[pg.vertexCount - 2][Y],
                        x, y,
                        pg.vertices[pg.vertexCount - 3][X],
                        pg.vertices[pg.vertexCount - 3][Y]);
                }
                break;
            }

            case POLYGON: {
                if (pg.workPath.getNumCommands() == 0 || pg.breakShape) {
                    pg.workPath.moveTo((float) x, (float) y);
                    pg.breakShape = false;
                } else {
                    pg.workPath.lineTo((float) x, (float) y);
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
    }

    public void quad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double[] xPoints = {x1, x2, x3, x4};
        double[] yPoints = {y1, y2, y3, y4};
        if (pg.hasFill) gc.fillPolygon(xPoints, yPoints, 4);
        if (pg.hasStroke) gc.strokePolygon(xPoints, yPoints, 4);
    }

	public void endShape(int mode) {
        System.out.println("Got end shape command");
        if (pg.workPath.getNumCommands() > 0) {
            if (pg.shape == POLYGON) {
              if (mode == CLOSE) {
                pg.workPath.closePath();
              }
              if (pg.auxPath.getNumCommands() > 0) {
                pg.workPath.append(pg.auxPath, false);
              }
              System.out.println("Got before draw shape");
              drawShape(pg.workPath);
            }
          }
          pg.shape = 0;
        
        System.out.println("Finished end shape command");
    }
    
    private void drawShape(Shape s) {
        System.out.println("Got draw shape command");
        gc.beginPath();
        PathIterator pi = s.getPathIterator(null);
        while (!pi.isDone()) {
          int pitype = pi.currentSegment(pg.pathCoordsBuffer);
          switch (pitype) {
            case PathIterator.SEG_MOVETO:
              gc.moveTo(pg.pathCoordsBuffer[0], pg.pathCoordsBuffer[1]);
              break;
            case PathIterator.SEG_LINETO:
              gc.lineTo(pg.pathCoordsBuffer[0], pg.pathCoordsBuffer[1]);
              break;
            case PathIterator.SEG_QUADTO:
              gc.quadraticCurveTo(pg.pathCoordsBuffer[0], pg.pathCoordsBuffer[1],
                pg.pathCoordsBuffer[2], pg.pathCoordsBuffer[3]);
              break;
            case PathIterator.SEG_CUBICTO:
              gc.bezierCurveTo(pg.pathCoordsBuffer[0], pg.pathCoordsBuffer[1],
                    pg.pathCoordsBuffer[2], pg.pathCoordsBuffer[3],
                    pg.pathCoordsBuffer[4], pg.pathCoordsBuffer[5]);
              break;
            case PathIterator.SEG_CLOSE:
              gc.closePath();
              break;
            default:
              break;
          }
          pi.next();
        }
        if (pg.hasFill) gc.fill();
        if (pg.hasStroke) gc.stroke();
      }
}