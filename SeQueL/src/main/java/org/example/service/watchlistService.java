package org.example.service;

import org.example.models.watchlist;
import org.example.models.movie;
import org.example.models.user;
import org.example.repos.watchlistRepo;
import org.example.repos.movieRepo;
import org.example.repos.userRepo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class watchlistService {
    private watchlistRepo watchlistRepo;
    private movieRepo movieRepo;
    private userRepo userRepo;

    public watchlistService() {
        this.watchlistRepo=new watchlistRepo();
        this.movieRepo=new movieRepo();
        this.userRepo=new userRepo();
    }

    public watchlist addToWatchlist(int userID, int movieID){
        try {
            Optional<user> uOpt=userRepo.readID(userID);
            if (uOpt.isEmpty()) {
                throw new IllegalArgumentException("user not real");
            }

            Optional<movie> oMovie=movieRepo.readID(movieID);
            if (oMovie.isEmpty()) {
                throw new IllegalArgumentException("movie not real");
            }

            if (watchlistRepo.onList(userID,movieID)) {
                throw new IllegalArgumentException("you already reviewed this");
            }

            watchlist entry = new watchlist(userID, movieID);
            entry.setWatchDate(LocalDateTime.now());

            return watchlistRepo.create(entry);

        } catch (SQLException e) {
            throw new RuntimeException("failed to add to watchlist");
        }
    }

    public void removeFromWatchlist(int userID, int movieID){
        try {
            watchlistRepo.deleteUserMovie(userID, movieID);
        }  catch (SQLException e) {
            throw new RuntimeException("failed to remove from watchlist");
        }
    }

    public List<watchlist> getWatchlist(int userID){
        try {
            return watchlistRepo.findUser(userID);
        }  catch (SQLException e) {
            throw new RuntimeException("failed to get watchlist");
        }
    }

    public boolean inWatchlist(int userID, int movieID){
        try {
            return watchlistRepo.onList(userID, movieID);
        }   catch (SQLException e) {
            throw new RuntimeException("failed to check watchlist");
        }
    }

    public int getCount(int userID){
        try {
            return watchlistRepo.watchlistCount(userID);
        }   catch (SQLException e) {
            throw new RuntimeException("failed to get watchlist");
        }
    }
}
