package main;

public class AppEntry implements Comparable<AppEntry> {

    public String title, authour, description, fileName;

    public AppEntry(String t, String a, String d) {
        title = t.replaceAll("([^_])([A-Z])", "$1 $2").trim();
        fileName = t;
        authour = a.trim();
        description = d.trim();
    }

    public void launch() throws Exception {
        Loader.launchProgram("sketches/"+fileName+".pde");
    }

    @Override
    public int compareTo(AppEntry other) {
        if (this.authour.compareTo(other.authour) != 0) return this.authour.compareTo(other.authour);
        if (this.title.compareTo(other.title) != 0) return this.title.compareTo(other.title);
        if (this.description.compareTo(other.description) != 0) return this.description.compareTo(other.description);
        return 0;
    }
    
}