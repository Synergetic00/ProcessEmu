package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class FXApp {

    GraphicsContext gc;

    public FXApp(GraphicsContext gc) { this.gc = gc; }
    
    public void handleSettings() {}
    
    public void handleSetup() {}
    
    public void handleDraw() {
        draw();
    }
    
    public void handleKeyPressed(KeyEvent keyEvent) {}
    
    public void handleKeyReleased(KeyEvent keyEvent) {}
    
    public void handleKeyTyped(KeyEvent keyEvent) {}
    
    public void handleMouseClicked(MouseEvent mouseEvent) {}
    
    public void handleMouseDragged(MouseEvent mouseEvent) {}
    
    public void handleMouseMoved(MouseEvent mouseEvent) {}
    
    public void handleMousePressed(MouseEvent mouseEvent) {}
    
    public void handleMouseReleased(MouseEvent mouseEvent) {}
    
    public void handleMouseWheel(ScrollEvent scrollEvent) {}
    
    public void setup() {}
    
    public void draw() {}

    public void size(int a, int b) {}
    
    public void rect(int a, int b, int c, int d) {
        gc.fillRect(a, b, c, d);
    }

}