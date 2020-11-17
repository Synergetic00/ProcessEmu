package misc;

import java.util.ArrayList;
import com.sun.javafx.geom.Path2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.*;

import static utils.Constants.*;

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

	public PGraphics(GraphicsContext gc, FXApp parent) {
        this.gc = gc;
        this.parent = parent;
        gs = new GraphicState();
        r = new Renderer(gc, gs);
        gs.screenW = gc.getCanvas().getWidth();
        gs.screenH = gc.getCanvas().getHeight();
    }

    public void hello(String text) {
        System.out.println(""+text+" "+isPrimary+" "+gs.width);
    }

    public void size(int w, int h) {
        gs.width = w;
        gs.height = h;

        if (isPrimary) {
            GraphicState.setOffset(((gs.screenW - gs.width) / 2), ((gs.screenH - gs.height) / 2));
        }

    }

    public void fill(int red, int green, int blue) {
        this.fillColour = Color.rgb(red, green, blue);

        if (isPrimary) {
            r.fill(fillColour);
        } else {
            commands.add(new CommandNode("fill", fillColour));
        }
    }

    public void background(int gray) {
        this.backgroundColour = Color.rgb(gray, gray, gray);

        if (isPrimary) {
            r.background(backgroundColour);
        } else {
            commands.add(new CommandNode("background", backgroundColour));
        }
    }

	public void rect(double x, double y, double w, double h) {
        r.fill(fillColour);
        r.stroke(strokeColour);
        if (isPrimary) {
            r.rect(x, y, w, h);
        } else {
            commands.add(new CommandNode("rect", x, y, w, h));
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





    // [Shape]

    protected int shape;
    protected int vertexCount; // total number of vertices
    protected int curveVertexCount;
    boolean breakShape;

    public static final int DEFAULT_VERTICES = 512;
    static public final int VERTEX_FIELD_COUNT = 37;
    protected double[][] vertices = new double[DEFAULT_VERTICES][VERTEX_FIELD_COUNT];

    Path2D workPath = new Path2D();
    Path2D auxPath = new Path2D();

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
          //message(CHATTER, "allocating more vertices " + vertices.length);
        }
        // not everyone needs this, but just easier to store rather
        // than adding another moving part to the code...
        vertices[vertexCount][X] = x;
        vertices[vertexCount][Y] = y;
        vertexCount++;
    
        switch (shape) {
    
        case POINTS:
        r.point(x, y);
          break;
    
        case LINES:
          if ((vertexCount % 2) == 0) {
            r.line(vertices[vertexCount-2][X],
                 vertices[vertexCount-2][Y], x, y);
          }
          break;
    
        case TRIANGLES:
          if ((vertexCount % 3) == 0) {
            r.triangle(vertices[vertexCount - 3][X],
                     vertices[vertexCount - 3][Y],
                     vertices[vertexCount - 2][X],
                     vertices[vertexCount - 2][Y],
                     x, y);
          }
          break;
    
        case TRIANGLE_STRIP:
          if (vertexCount >= 3) {
            r.triangle(vertices[vertexCount - 2][X],
                     vertices[vertexCount - 2][Y],
                     vertices[vertexCount - 1][X],
                     vertices[vertexCount - 1][Y],
                     vertices[vertexCount - 3][X],
                     vertices[vertexCount - 3][Y]);
          }
          break;
    
        case TRIANGLE_FAN:
          if (vertexCount >= 3) {
            // This is an unfortunate implementation because the stroke for an
            // adjacent triangle will be repeated. However, if the stroke is not
            // redrawn, it will replace the adjacent line (when it lines up
            // perfectly) or show a faint line (when off by a small amount).
            // The alternative would be to wait, then draw the shape as a
            // polygon fill, followed by a series of vertices. But that's a
            // poor method when used with PDF, DXF, or other recording objects,
            // since discrete triangles would likely be preferred.
            r.triangle(vertices[0][X],
                     vertices[0][Y],
                     vertices[vertexCount - 2][X],
                     vertices[vertexCount - 2][Y],
                     x, y);
          }
          break;
    
        case QUAD:
        case QUADS:
          if ((vertexCount % 4) == 0) {
            r.quad(vertices[vertexCount - 4][X],
                 vertices[vertexCount - 4][Y],
                 vertices[vertexCount - 3][X],
                 vertices[vertexCount - 3][Y],
                 vertices[vertexCount - 2][X],
                 vertices[vertexCount - 2][Y],
                 x, y);
          }
          break;
    
        case QUAD_STRIP:
          // 0---2---4
          // |   |   |
          // 1---3---5
          if ((vertexCount >= 4) && ((vertexCount % 2) == 0)) {
            r.quad(vertices[vertexCount - 4][X],
                 vertices[vertexCount - 4][Y],
                 vertices[vertexCount - 2][X],
                 vertices[vertexCount - 2][Y],
                 x, y,
                 vertices[vertexCount - 3][X],
                 vertices[vertexCount - 3][Y]);
          }
          break;
    
        case POLYGON:
          if (workPath.getNumCommands() == 0 || breakShape) {
            workPath.moveTo((float)x, (float)y);
            breakShape = false;
          } else {
            workPath.lineTo((float)x, (float)y);
          }
          break;
        }
      }



































	public void render(double x, double y) {
        r.renderPos(x, y);
        for (CommandNode command : commands) {
            command.execute(r, x, y);
        }
	}

}