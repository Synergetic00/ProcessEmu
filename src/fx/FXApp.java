package fx;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class FXApp {

    public static Canvas appCanvas;
    public static GraphicsContext g;
    
    static boolean hasFill = true;
    static boolean hasStroke = true;

    // Shapes

    public FXApp(Group root) {
        root.getChildren().add(appCanvas);
        g = appCanvas.getGraphicsContext2D();

        rect(100,50,200,150);
	}

	public static void rect(float x, float y, float w, float h) {
        if (hasFill) {
            g.fillRect(x, y, w, h);
        }
        if(hasStroke) {
            g.strokeRect(x, y, w, h);
	    }
    }
    
}