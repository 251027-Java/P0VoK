package org.example.models;

import java.time.LocalDate;
import java.util.Date;

public class review {

    // fields
    private int reviewID;
    private int userID;
    private int movieID;
    private double rating; // HAS TO BE IN THE FORM OF 5 STARS LETTERBOXD
    private String reviewTxt;
    private LocalDate watchDate;

    private String movieName;
    private String username;

    // default con
    public review() {}

    // overload con
    public review(int userID, int movieID, double rating, String reviewTxt, LocalDate watchDate) {
        this.userID = userID;
        this.movieID = movieID;
        this.rating = rating;
        this.reviewTxt = reviewTxt;
        this.watchDate = watchDate;
    }

    // methods
    public int getReviewID() {
        return reviewID;
    }
    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
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
    public String getMovieName() {
        return movieName;
    }
    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getDate() {
        return watchDate;
    }
    public void setDate(LocalDate date) {
        this.watchDate = date;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public String getReviewTxt() {
        return reviewTxt;
    }
    public void setReviewTxt(String reviewTxt) {
        this.reviewTxt = reviewTxt;
    }

    public boolean validRating() {
        // Check if rating is between 0 and 5 and is a valid half-star increment
        if (rating < 0 || rating > 5) return false;
        double doubled = rating * 2;
        return Math.abs(doubled - Math.round(doubled)) < 0.001;
    }

    @Override
    public String toString() {
        return "Review: " + "\nUser : " + userID +
                "\nRating : " + rating +
                "\n" + reviewTxt;
    }

    public String getFormattedRating() {
        return String.format("%.1f/5", rating);
    }
}
