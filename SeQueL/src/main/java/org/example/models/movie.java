package org.example.models;

import java.util.Date;

/*
movies
- movie id (could use tmdb id for this)
- title
- release date
- overview
 */

public class movie {

    // fields
    private int movieID;
    private String name;
    private Date releaseDate;

    // default con
    public movie() {}

    // overload con
    public movie(int movieID, String name, Date releaseDate) {
        this.movieID = movieID;
        this.name = name;
        this.releaseDate = releaseDate;
    }

    // methods
    public int getMovieID() { return movieID; }
    public void setMovieID(int movieID) { this.movieID = movieID; }
    public String getName() { return name; }
    public Date getReleaseDate() { return releaseDate; }

    @Override
    public String toString() {
        return "Movie: " + "movieID : " + movieID +
                "\nname : " + name +
                "\nrelease date : " + releaseDate;
    }
}
