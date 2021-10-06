package utils;

public class ModeState {
    public int imageMode;
    public int rectMode;
    public int ellipseMode;
    public int colorMode;

    public ModeState(int i, int r, int e, int c) {
        this.imageMode = i;
        this.rectMode = r;
        this.ellipseMode = e;
        this.colorMode = c;
    }
}
