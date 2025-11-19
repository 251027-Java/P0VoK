package org.example.models;

import java.util.Objects;

public class genre {
    private int genreID;
    private int movieID;
    private String name;

    public genre() {}

    public genre(int genreID, int movieID, String name) {
        this.genreID = genreID;
        this.movieID = movieID;
        this.name = name;
    }

    public int getGenreID() {
        return genreID;
    }
    public void setGenreID(int genreID) {
        this.genreID = genreID;
    }

    public int getMovieID() {
        return movieID;
    }
    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
