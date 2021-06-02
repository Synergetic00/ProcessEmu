package main;

public class AppEntry implements Comparable<AppEntry> {

    String title, authour, description;

    public AppEntry(String t, String a, String d) {
        title = t; authour = a; description = d;
    }

    public void launch() throws Exception {
        Loader.launchProgram("src/sketches/"+title+".pde");
    }

    @Override
    public int compareTo(AppEntry other) {
        if (this.authour.compareTo(other.authour) != 0) return this.authour.compareTo(other.authour);
        if (this.title.compareTo(other.title) != 0) return this.title.compareTo(other.title);
        if (this.description.compareTo(other.description) != 0) return this.description.compareTo(other.description);
        return 0;
    }
    
}