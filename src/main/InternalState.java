package main;

import utils.Constants;

public class InternalState {

    public int offsetW;
    public int offsetH;
    public int renderW;
    public int renderH;

    public InternalState(int ow, int oh, int rw, int rh) {
        offsetW = ow;
        offsetH = oh;
        renderW = rw;
        renderH = rh;
    }

    public int offsetW() {
        return Constants.offsetW();
    }

    public int offsetH() {
        return Constants.offsetH();
    }

    public void offsetW(int ow) {
        Constants.offsetH(ow);
    }

    public void offsetH(int oh) {
        Constants.offsetH(oh);
    }

    public int renderW() {
        return renderW;
    }

    public int renderH() {
        return renderH;
    }

    public void renderW(int ow) {
        renderW = ow;
    }

    public void renderH(int oh) {
        renderH = oh;
    }
    
}