package main;

import java.util.ArrayList;

import fx.FXApp;
import javafx.scene.Group;

public class App extends FXApp {
    
	public App(Group root) {
        super(root);
    }

    ArrayList<FXApp> applications = new ArrayList<FXApp>();
    
}