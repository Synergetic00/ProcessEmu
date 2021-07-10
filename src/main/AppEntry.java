package main;

import java.util.Arrays;

import utils.Data;
import utils.Maths;

public class AppEntry implements Comparable<AppEntry> {

    public String title, authour, description, path;
    public String[] folders;

    public AppEntry(String t, String a, String d, String p) {
        title = t.replaceAll("([^_])([A-Z][a-z])", "$1 $2").trim();
        path = p;
        authour = a.trim();
        description = d.trim();

        String stripped = path.substring(9, path.length()-4);
        String[] split = Data.split(stripped, '\\');
        if (split.length <= 1) {
            folders = new String[0];
        } else {
            folders = Arrays.copyOf(split, split.length-1);
        }
    }

    public void launch() throws Exception {
        Loader.launchProgram(path);
    }

    @Override
    public int compareTo(AppEntry other) {
        String[] thisPathArgs = Data.split(this.actualPath(), '\\');
        String[] otherPathArgs = Data.split(other.actualPath(), '\\');
        int lowest = Maths.min(thisPathArgs.length, otherPathArgs.length);

        for (int i = 0; i < lowest; i++) {
            if (thisPathArgs[i].compareTo(otherPathArgs[i]) != 0) {
                return thisPathArgs[i].compareTo(otherPathArgs[i]);
            } else {
                continue;
            }
        }

        return 0;
    }

    public String actualPath() {
        return new String(path);
    }

    public String path() {
        if (folders.length == 0) {
            return "root directory";
        } else {
            return Data.join(folders, "/");
        }
    }

    public String title() {
        return new String(title);
    }

    public String authour() {
        return new String(authour);
    }

    public String description() {
        return new String(description);
    }

}