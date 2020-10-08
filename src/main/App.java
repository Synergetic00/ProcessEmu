package main;

public class App {

    String appName;
    String appAuthor;
    String appDesc;
    //int appId;

    App(String name, String author, String desc) {
        appName = name;
        appAuthor = author;
        appDesc = desc;
        //appId = id;
    }
    
    public void execute() throws Exception {
        Main.executeProgram(appName, Main.gc);
    }

}