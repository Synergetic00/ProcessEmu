package main;

public class App {

    String appName;
    String appAuthor;
    //int appId;

    App(String name, String author) {
        appName = name;
        appAuthor = author;
        //appId = id;
    }
    
    public void execute() throws Exception {
        Main.executeProgram(appName, Main.gc);
    }

}