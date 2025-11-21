package org.example.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class watchlist {
    private int watchlistID;
    private int userID;
    private int movieID;
    private LocalDateTime watchDate;

    private String movieName;
    private int releaseYear;

    public watchlist() {}

    public watchlist(int userID, int movieID) {
        this.userID = userID;
        this.movieID = movieID;
        this.watchDate = LocalDateTime.now();
    }

    public watchlist(int watchlistID, int userID, int movieID, LocalDateTime watchDate) {
        this.watchlistID = watchlistID;
        this.userID = userID;
        this.movieID = movieID;
        this.watchDate = watchDate;
    }

    public int getWatchlistID() {
        return watchlistID;
    }
    public void setWatchlistID(int watchlistID) {
        this.watchlistID = watchlistID;
    }

    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getMovieID() {
        return movieID;
    }
    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public LocalDateTime getWatchDate() {
        return watchDate;
    }
    public void setWatchDate(LocalDateTime watchDate) {
        this.watchDate = watchDate;
    }

    public String getMovieName() {
        return movieName;
    }
    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public int getReleaseYear() {
        return releaseYear;
    }
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

}
