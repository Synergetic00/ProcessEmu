package ptypes;

import java.nio.IntBuffer;

import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import main.AppState;
import main.Main;

public class PGraphics {

    public int width;
    public int height;
    public int[] pixels;

    WritablePixelFormat<IntBuffer> pixelFormat;
    
    public PGraphics(int w, int h) {
        width = w;
        height = h;
        pixelFormat = PixelFormat.getIntArgbInstance();
    }

    public void beginDraw() {
        pixels = new int[width * height];
    }
    
    public void endDraw() {
        int x = AppState.offsetW() + AppState.renderW();
        int y = AppState.offsetH() + AppState.renderH();

        WritableImage snapshot = Main.gc.getCanvas().snapshot(null, null);
        snapshot.getPixelReader().getPixels(x, y, width, height, pixelFormat, pixels, 0, 0);
    }

}