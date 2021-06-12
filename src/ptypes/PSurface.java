package ptypes;

import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

import static utils.Constants.*;

import main.Main;

public class PSurface {

    public void setLocation(int x, int y) {
        //Main.stage.setX(x-5);
        //Main.stage.setY(y-5);
    }

    public void setResizable(boolean resizable) {
        //Main.stage.setResizable(resizable);
    }

    public void setTitle(String title) {
        //Main.stage.setTitle(title);
    }

    Cursor lastCursor = Cursor.DEFAULT;
    
    public void setCursor(int kind) {
		Cursor c;
		switch (kind) {
		case ARROW: c = Cursor.DEFAULT; break;
		case CROSS: c = Cursor.CROSSHAIR; break;
		case HAND: c = Cursor.HAND; break;
		case MOVE: c = Cursor.MOVE; break;
		case TEXT: c = Cursor.TEXT; break;
		case WAIT: c = Cursor.WAIT; break;
		default: c = Cursor.DEFAULT; break;
		}
		lastCursor = c;
		Main.canvas.getScene().setCursor(c);
	}

	public void setCursor(PImage image, int hotspotX, int hotspotY) {
		int w = image.pixelWidth;
		int h = image.pixelHeight;
		WritableImage im = new WritableImage(w, h);
		im.getPixelWriter().setPixels(0, 0, w, h, PixelFormat.getIntArgbInstance(), image.pixels, 0, w);
		ImageCursor c = new ImageCursor(im, hotspotX, hotspotY);
		lastCursor = c;
		Main.canvas.getScene().setCursor(c);
	}

	public void showCursor() {
		Main.canvas.getScene().setCursor(lastCursor);
	}

	public void hideCursor() {
		Main.canvas.getScene().setCursor(Cursor.NONE);
	}
    
}