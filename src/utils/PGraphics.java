package utils;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import main.FXApp;

public class PGraphics {

    GraphicsContext gc;
    FXApp parent;
    Renderer r;
    GraphicState gs;
    ArrayList<CommandNode> commands;
    public boolean isPrimary;

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
        if (isPrimary) {
            r.fill(red, green, blue);
        } else {
            commands.add(new CommandNode("fill", red, green, blue, 255));
        }
    }

    public void background(int gray) {
        if (isPrimary) {
            r.background(gray);
        } else {
            commands.add(new CommandNode("background", gray, gray, gray, 255));
        }
    }

	public void rect(double x, double y, double w, double h) {
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
        gc.save();
    }
    
    public void endDraw() {
        gc.restore();
        gc.restore();
    }

	public void render(double x, double y) {
        r.renderPos(x, y);
        for (CommandNode command : commands) {
            command.execute(r, x, y);
        }
	}

}