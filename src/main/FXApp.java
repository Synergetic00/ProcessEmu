package main;

import java.util.*;

import types.*;

public class FXApp {

    public PGraphics pg, recorder;
    public int displayWidth, displayHeight;
    public String[] args;
    protected boolean keyRepeatEnabled = false;
    public boolean focused = false;
    protected PSurface surface;

    public static final int DEFAULT_WIDTH = 100;
    public static final int DEFAULT_HEIGHT = 100;
    public int[] pixels;
    public int width = DEFAULT_WIDTH;
    public int height = DEFAULT_HEIGHT;

    public int mouseX, mouseY, pmouseX, pmouseY;
    protected int dmouseX, dmouseY, emouseX, emouseY;
    protected boolean firstMouse = true;
    public int mouseButton;
    public boolean mousePressed;

    public char key;
    public int keyCode;
    public boolean keyPressed;
    List<Long> pressedKeys = new ArrayList<>(6);

    long millisOffset = System.currentTimeMillis();
    public float frameRate = 60;
    public int frameCount;
    protected boolean looping = true;
    protected boolean redraw = true;
    public volatile boolean finished;
    static Throwable uncaughtThrowable;
    protected boolean exitCalled;

    ///////////////////
    // 2D Primitives //
    ///////////////////

    // arc()

    public void arc(float a, float b, float c, float d, float start, float stop) {
        if (recorder != null) recorder.arc(a, b, c, d, start, stop);
        pg.arc(a, b, c, d, start, stop);
    }

    public void arc(float a, float b, float c, float d, float start, float stop, int mode) {
        if (recorder != null) recorder.arc(a, b, c, d, start, stop, mode);
        pg.arc(a, b, c, d, start, stop, mode);
    }

    // circle()

    public void circle(float x, float y, float extent) {
        if (recorder != null) recorder.circle(x, y, extent);
        pg.circle(x, y, extent);
    }

    // ellipse()

    public void ellipse(float a, float b, float c, float d) {
        if (recorder != null) recorder.ellipse(a, b, c, d);
        pg.ellipse(a, b, c, d);
    }

    // line()
    // point()
    // quad()
    // rect()
    // square()
    // triangle()

    ////////////
    // Curves //
    ////////////

    // bezier()

    public void bezier(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        if (recorder != null) recorder.bezier(x1, y1, x2, y2, x3, y3, x4, y4);
        pg.bezier(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    public void bezier(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
        if (recorder != null) recorder.bezier(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
        pg.bezier(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    // bezierDetail()

    public void bezierDetail(int detail) {
        if (recorder != null) recorder.bezierDetail(detail);
        pg.bezierDetail(detail);
    }

    // bezierPoint()

    public float bezierPoint(float a, float b, float c, float d, float t) {
        return pg.bezierPoint(a, b, c, d, t);
    }

    // bezierTangent()

    public float bezierTangent(float a, float b, float c, float d, float t) {
        return pg.bezierTangent(a, b, c, d, t);
    }

    // curve()

    public void curve(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        if (recorder != null) recorder.curve(x1, y1, x2, y2, x3, y3, x4, y4);
        pg.curve(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    public void curve(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
        if (recorder != null) recorder.curve(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
        pg.curve(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    // curveDetail()

    public void curveDetail(int detail) {
        if (recorder != null) recorder.curveDetail(detail);
        pg.curveDetail(detail);
    }

    // curvePoint()

    public float curvePoint(float a, float b, float c, float d, float t) {
        return pg.curvePoint(a, b, c, d, t);
    }

    // curveTangent()

    public float curveTangent(float a, float b, float c, float d, float t) {
        return pg.curveTangent(a, b, c, d, t);
    }

    // curveTightness()

    public void curveTightness(float tightness) {
        if (recorder != null) recorder.curveTightness(tightness);
        pg.curveTightness(tightness);
    }

    ///////////////////
    // 3D Primitives //
    ///////////////////

    // box()

    public void box(float size) {
        if (recorder != null) recorder.box(size);
        pg.box(size);
    }
      
    public void box(float w, float h, float d) {
        if (recorder != null) recorder.box(w, h, d);
        pg.box(w, h, d);
    }

    // sphere()

    public void sphere(float r) {
        if (recorder != null) recorder.sphere(r);
        pg.sphere(r);
    }

    // sphereDetail()

    public void sphereDetail(int res) {
        if (recorder != null) recorder.sphereDetail(res);
        pg.sphereDetail(res);
    }

    public void sphereDetail(int ures, int vres) {
        if (recorder != null) recorder.sphereDetail(ures, vres);
        pg.sphereDetail(ures, vres);
    }

    // Attributes
    // ellipseMode()

    public void ellipseMode(int mode) {
        if (recorder != null) recorder.ellipseMode(mode);
        pg.ellipseMode(mode);
    }

    // rectMode()
    // strokeCap()
    // strokeJoin()
    // strokeWeight()

    ////////////
    // Vertex //
    ////////////

    // beginContour()
    // beginShape()
    // bezierVertex()
    // curveVertex()
    // endContour()
    // endShape()
    // quadraticVertex()
    // vertex()

}