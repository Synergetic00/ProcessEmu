package misc;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;

import static misc.DynLoader.loadProgram;

public class AppTemplate {

    String title, authour, description;

    public AppTemplate(String t, String a, String d) {
        title = t; authour = a; description = d;
    }

    public void launch(GraphicsContext gc) throws Exception {
        gc.setTransform(new Affine());
        loadProgram("src/programs/"+title+".pde");
    }
    
}
