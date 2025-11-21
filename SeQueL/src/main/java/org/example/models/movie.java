package org.example.models;

import java.time.LocalDate;

/*
movies
- movie id (could use tmdb id for this)
- title
- release date
- overview


                    movieID SERIAL PRIMARY KEY,
                    title VARCHAR(50) UNIQUE NOT NULL,
                    director VARCHAR(50) UNIQUE NOT NULL,
                    release_date DATE,
                    overview TEXT,
                    runtime INTEGER,
 */

public class movie {

    // fields
    private int movieID;
    private String name;
    private LocalDate releaseDate;
    private String director;
    private int runtime;
    private String overview;

    // default con
    public movie() {}

    // overload con
    public movie(int movieID, String name, LocalDate releaseDate, String director, int runtime, String overview) {
        this.movieID = movieID;
        this.name = name;
        this.releaseDate = releaseDate;
        this.director = director;
        this.runtime = runtime;
        this.overview = overview;
    }

    // methods
    public int getMovieID() {
        return movieID;
    }
    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }
    public String getName() {
        return name;
    }
    public LocalDate getReleaseDate() {
        return releaseDate;
    }
    public String getDirector() {
        return director;
    }
    public int getRuntime() {
        return runtime;
    }
    public String getOverview() {
        return overview;
    }

    @Override
    public String toString() {
        return "Movie: " + "movieID : " + movieID +
                "\nname : " + name + " directed by : " + director +
                "\nrelease date : " + releaseDate + " runtime : " + runtime + "min";
    }
}
