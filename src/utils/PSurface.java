package utils;

import javafx.scene.Cursor;
import javafx.stage.Stage;
import static utils.PConstants.*;

public class PSurface {

    Stage stage;
    Cursor cursor;

    public PSurface(Stage stage) {
        this.stage = stage;
        cursor = stage.getScene().getCursor();
    }

    public void setLocation(int x, int y) {
        stage.setX(x);
        stage.setY(y);
    }

    public void setResizable(boolean resizable) {
        stage.setResizable(resizable);
    }

    public void setTitle(String title) {
        stage.setTitle(title);
    }

    public void setCursor(int kind) {
        switch (kind) {
            case ARROW: cursor = Cursor.DEFAULT; break;
            case CROSS: cursor = Cursor.CROSSHAIR; break;
            case HAND: cursor = Cursor.HAND; break;
            case MOVE: cursor = Cursor.MOVE; break;
            case TEXT: cursor = Cursor.TEXT; break;
            case WAIT: cursor = Cursor.WAIT; break;
            default: cursor = Cursor.DEFAULT; break;
        }
        stage.getScene().setCursor(cursor);
    }

	public void showCursor() {
	}

	public void hideCursor() {
	}
    
}