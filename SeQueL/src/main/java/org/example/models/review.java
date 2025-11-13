package org.example.models;

import java.util.Date;

public class review {

    // fields
    private int reviewID;
    private int userID;
    private int movieID;
    private double rating; // HAS TO BE IN THE FORM OF 5 STARS LETTERBOXD
    private String reviewTxt;
    private Date watchDate;

    // default con
    public review() {}

    // overload con
    public review(int userID, int movieID, double rating, String reviewTxt, Date watchDate) {
        this.userID = userID;
        this.movieID = movieID;
        this.rating = rating;
        this.reviewTxt = reviewTxt;
        this.watchDate = watchDate;
    }

    // methods
    public int getReviewID() { return reviewID; }
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    public int getMovieID() { return movieID; }
    public void setMovieID(int movieID) { this.movieID = movieID; }
    public Date getDate() { return watchDate; }
    public void setDate(Date date) { this.watchDate = date; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public boolean validRating() {
        // temp thing for rating bc idk how to check if its .0 or .5
        if (rating > 0 && rating < 10) return true;
        return false;
    }

    @Override
    public String toString() {
        return "Review: " + "\nUser : " + userID +
                "\nRating : " + rating +
                "\n" + reviewTxt;
    }
}
