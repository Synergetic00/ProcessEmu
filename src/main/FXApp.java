package main;

import java.util.*;

import types.PGraphics;

public class FXApp {

    public PGraphics pg, recorder;
    public int displayWidth, displayHeight;
    public String[] args;
    protected boolean keyRepeatEnabled = false;
    public boolean focused = false;

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
    protected boolean exitCalled;

}