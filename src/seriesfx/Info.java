package seriesfx;

import java.io.Serializable;

public class Info implements Serializable {

    String title, scriptwriter, season, genre, seasonsWatched;

    public Info() {}

    public Info(String title, String scriptwriter, String season, String genre, String seasonsWatched) {
        this.title = title;
        this.scriptwriter = scriptwriter;
        this.season = season;
        this.genre = genre;
        this.seasonsWatched = seasonsWatched;
    }

    public String getTitle() {
        return title;
    }

    public String getScriptwriter() {
        return scriptwriter;
    }

    public String getSeason() {
        return season;
    }

    public String getGenre() {
        return genre;
    }

    public String getSeasonsWatched() {
        return seasonsWatched;
    }

}