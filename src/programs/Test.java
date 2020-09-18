//Hello

package programs;

import javafx.scene.canvas.GraphicsContext;
import main.FXApp;
public class Test extends FXApp{
public Test(GraphicsContext gc) { super(gc); }
int x = 50, y = 100;

public void setup() {
    System.out.println("It has been loaded!");
}

}