package ptypes;

import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.stage.Screen;

import static utils.Constants.*;

import main.Main;

public class PSurface {

    public void setLocation(int x, int y) {
        Main.stage.setX(x);
        Main.stage.setY(y);
    }

    public void setResizable(boolean resizable) {
        Main.stage.setResizable(resizable);
    }

    public void setTitle(String title) {
        Main.stage.setTitle(title);
    }

	public void reset() {
		switch (System.getProperty("os.name")) {
			case "Windows 10":
				Main.stage.setWidth(1280);
				Main.stage.setHeight(720);
				break;
			case "Mac OS X":
				Main.stage.setWidth(1280);
				Main.stage.setHeight(748);
				break;
			default:
				Main.stage.setWidth(1280);
				Main.stage.setHeight(720);
				break;
		}
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        setLocation((int)(screen.getWidth() - Main.stage.getWidth()) / 2, (int)(screen.getHeight() - Main.stage.getHeight()) / 2);
		setResizable(false);
		setTitle(Main.title);
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