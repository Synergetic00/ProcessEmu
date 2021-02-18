package main;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import data.XML;
import event.MouseEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import types.*;

public class FXApp {

    public static int platform;
	public static String javaVersionName;
	GraphicsContext gc;
    public PGraphics g;
    public int width, height;
	public boolean keyRepeatEnabled;
	public int frameCount;
	public int pixelDensity;
	public boolean focused;
	public int displayWidth;
	public int displayHeight;

    public FXApp(GraphicsContext gc) {
        this.gc = gc;
        g = new PGraphics(gc, this);
        g.setPrimary(true);
        //fill(255);
        //stroke(0);
    }

    public void size(int w, int h) {
        g.setSize(w, h);
    }

    public void fill(int rh, int gs, int bb) {
        gc.setFill(Color.rgb(rh, gs, bb));
        //g.fill(rh, gs, bb);
    }

    public void rect(int x, int y, int w, int h) {
        //gc.fillRect(x, y, w, h);
        g.rect(x, y, w, h);
    }

    // Methods to be overwritten
    public void settings() {}
    public void setup() {}
    public void draw() {}
    public void mouseClicked() {}
    public void mouseDragged() {}
    public void mousedMoved() {}
    public void mousePressed() {}
    public void mouseReleased() {}
    public void mouseWheel(MouseEvent event) {}
    public void keyPressed() {}
    public void keyReleased() {}
    public void keyTyped() {}

    // Handled methods by Main.java

    public void handleSettings() {
        settings();
    }
    
    public void handleSetup() {
        setup();
    }

    public void handleDraw() {
        draw();
    }

    public void handleMouseClicked(javafx.scene.input.MouseEvent event) {
        mouseClicked();
    }

    public void handleMouseDragged(javafx.scene.input.MouseEvent event) {
        mouseDragged();
    }

    public void handleMouseMoved(javafx.scene.input.MouseEvent event) {
        mousedMoved();
    }

    public void handleMousePressed(javafx.scene.input.MouseEvent event) {
        mousePressed();
    }

    public void handleMouseReleased(javafx.scene.input.MouseEvent event) {
        mouseReleased();
    }

    public void handleMouseWheel(ScrollEvent scrollEvent) {
        //int count = (int) -(scrollEvent.getDeltaY() / scrollEvent.getMultiplierY());
        //MouseEvent event = new MouseEvent(count);
        //mouseWheel(event);
    }

    public void handleKeyPressed(KeyEvent event) {
        keyPressed();
    }

    public void handleKeyReleased(KeyEvent event) {
        keyReleased();
    }

    public void handleKeyTyped(KeyEvent event) {
        keyTyped();
    }

	public static void createPath(File file) {
	}

	public String savePath(String filename) {
		return null;
	}

	public static OutputStream createOutput(File file) {
		return null;
	}

	public int sketchSmooth() {
		return 0;
	}

	public int sketchPixelDensity() {
		return 0;
	}

	public static String getExtension(String filename) {
		return null;
	}

	public XML loadXML(String filename) {
		return null;
	}

	public static int[] expand(int[] vertexCodes) {
		return null;
	}

	public static float[][] expand(float[][] vertices) {
		return null;
	}

	public PShape createShape(int group) {
		return null;
	}

	public PShape createShape(int kind, float[] params) {
		return null;
	}

	public static File createReader(File file) {
		return null;
	}

	public void smooth(int quality) {
	}

	public File sketchFile(String filename) {
		return null;
	}

	public static PStyle[] expand(PStyle[] styleStack) {
		return null;
	}

	public InputStream createInput(String name) {
		return null;
	}

	public PImage loadImage(String imagePath) {
		return null;
	}

	public static PShape[] subset(PShape[] children, int i, int childCount) {
		return null;
	}

	public static PShape[] expand(PShape[] children) {
		return null;
	}

	public static int[] subset(int[] vertexCodes, int i, int vertexCodeCount) {
		return null;
	}

	public static Object subset(String[] pathTokens, int i, int i2) {
		return null;
	}

    public PGraphics createGraphics(int w, int h) {
        PGraphics pg = new PGraphics(gc, this);
        pg.setPrimary(false);
        pg.setSize(w, h);
        return pg;
    }

    public void image(PImage img, float a, float b) {
        g.image(img, a, b);
    }

    public void image(PImage img, float a, float b, float c, float d) {
        img.set(1, 1, 0xff00ff00);
        g.image(img, a, b, c, d);
    }

	public void focusGained() {
	}

	public void focusLost() {
	}

	public void setSize(int intValue, int height2) {
	}

	public int sketchWidth() {
		return 0;
	}

	public int sketchHeight() {
		return 0;
	}

	public int sketchDisplay() {
		return 0;
	}

	public boolean exitCalled() {
		return false;
	}

	public void exit() {
	}

	public void frameMoved(int intValue, int intValue2) {
	}

	public static void hideMenuBar() {
	}

	public boolean sketchFullScreen() {
		return false;
	}

	public void dispose() {
	}

	public void postEvent(MouseEvent mouseEvent) {
	}

	public void postEvent(event.KeyEvent keyEvent) {
	}

}