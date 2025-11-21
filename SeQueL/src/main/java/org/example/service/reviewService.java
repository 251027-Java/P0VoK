package org.example.service;

import org.example.models.review;
import org.example.models.user;
import org.example.models.movie;
import org.example.repos.reviewRepo;
import org.example.repos.userRepo;
import org.example.repos.movieRepo;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class reviewService {
    private reviewRepo reviewRepo;
    private movieRepo movieRepo;
    private userRepo userRepo;

    public reviewService() {
        this.reviewRepo = new reviewRepo();
        this.movieRepo = new movieRepo();
        this.userRepo = new userRepo();
    }

    public review create(int userID, int movieID, double rating, String review, LocalDate watchDate) {
        try {
            validateRating(rating);

            // check for user
            Optional<user> uOpt = userRepo.readID(userID);
            if (uOpt.isEmpty()) {
                throw new IllegalArgumentException("user not real");
            }

            // check for movie
            Optional<movie> moOpt = movieRepo.readID(movieID);
            if (moOpt.isEmpty()) {
                throw new IllegalArgumentException("movie not real");
            }

            // check if already reviewed
            Optional<review> rOpt = reviewRepo.findUserMovie(userID, movieID); // YOOO MAKE THIS
            if (rOpt.isPresent()) {
                throw new IllegalArgumentException("review already exists");
            }

            review r = new review(userID, movieID, rating, review, watchDate);
            r.setDate(LocalDate.from(LocalDateTime.now())); // MAKE THE DATE STUFF FOR THIS CONSISTENT

            return reviewRepo.create(r);

        } catch (SQLException e) {
            throw new RuntimeException("failed to create review: " + e.getMessage(), e);
        }
    }

    public void updateReview(int reviewID, double rating, String review, LocalDate watchDate) {
        try {
            validateRating(rating);

            review r = new review();
            r.setReviewID(reviewID);
            r.setRating(rating);
            r.setReviewTxt(review);
            r.setDate(LocalDate.from(LocalDateTime.now()));

            reviewRepo.update(r);
        } catch (SQLException e) {
            throw new RuntimeException("failed to update review: " + e.getMessage(), e);
        }
    }

    public void deleteReview(int reviewID) {
        try {
            reviewRepo.delete(reviewID);
        }  catch (SQLException e) {
            throw new RuntimeException("failed to delete review: " + e.getMessage(), e);
        }
    }

    public List<review> getUserReviews(int userID) {
        try {
            return reviewRepo.findUser(userID);
        }  catch (SQLException e) {
            throw new RuntimeException("failed to get user reviews: " + e.getMessage(), e);
        }
    }

    public List<review> getMovieReviews(int movieID) {
        try {
            return reviewRepo.findMovie(movieID);
        }   catch (SQLException e) {
            throw new RuntimeException("failed to get movie reviews: " + e.getMessage(), e);
        }
    }

    public List<review> getRecent(int limit) {
        try {
            if (limit <= 0 || limit > 50) {
                throw new IllegalArgumentException("limit must be 1-50");
            }

            return  reviewRepo.findRecent(limit);
        }   catch (SQLException e) {
            throw new RuntimeException("failed to get recent reviews: " + e.getMessage(), e);
        }
    }

    public boolean checkReviewed(int userID, int movieID) {
        try {
            return reviewRepo.findUserMovie(userID, movieID).isPresent();
        } catch (SQLException e) {
            throw new RuntimeException("failed to check the review status: " + e.getMessage(), e);
        }
    }

    public stats getMovieStats(int movieID) {
        try {
            Optional<movie> moOpt = movieRepo.readID(movieID);
            if (moOpt.isEmpty()) {
                throw new IllegalArgumentException("movie not real");
            }

            Double avg = reviewRepo.avgRating(movieID);
            List<review> r = reviewRepo.findMovie(movieID);

            return new stats(moOpt.get(), avg != null ? avg : 0.0, r.size());

        } catch (SQLException e) {
            throw new RuntimeException("failed to get movie stats: " + e.getMessage(), e);
        }
    }

    private void validateRating(Double rating) {
        if (rating == null) {
            throw new IllegalArgumentException("rating cannot be null");
        }
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("rating must be between 0.0 and 5.0");
        }
        double doubled = rating * 2;
        if (Math.abs(doubled - Math.round(doubled)) > 0.001) {
            throw new IllegalArgumentException("rating must be in half-star increments (0, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5)");
        }
    }

    public static class stats {
        private movie m;
        private double avg;
        private int total;

        public stats(movie m, double avg, int total) {
            this.m = m;
            this.avg = avg;
            this.total = total;
        }

        public movie getMovie() {
            return m;
        }
        public double getAvg() {
            return avg;
        }
        public int getTotal() {
            return total;
        }

        public String getFormattedAvg() {
            return String.format("%.1f/5", avg);
        }
    }
}
