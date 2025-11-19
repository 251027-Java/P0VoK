package org.example.service;

import org.example.models.user;
import org.example.repos.userRepo;
import org.example.repos.reviewRepo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class userService {
    private userRepo userRepo;
    private reviewRepo reviewRepo;

    public userService() {
        userRepo = new userRepo();
        reviewRepo = new reviewRepo();
    }

    public user register(String username) throws SQLException{
        validateName(username);

        if (userRepo.usernameExists(username)) {
            throw new IllegalArgumentException("username already exists");
        }

        user u = new user(username);
        u.setDate(LocalDateTime.now());

        return userRepo.create(u);
    }

    public Optional<user> login(String username) throws SQLException {
        Optional<user> test = userRepo.readName(username);

        if (test.isEmpty()) {
            return Optional.empty();
        }

        user u = test.get();
        return Optional.of(u); // may add password? bc like anybody can access this..
    }

    public user getStats(int userID) throws SQLException {
        Optional<user> test = userRepo.readID(userID);
        if (test.isEmpty()) {
            throw new IllegalArgumentException("username not found");
        }

        user u = test.get();
        int count = reviewRepo.reviewCount(userID);
        u.setReviewCount(count);

        return u;
    }
    
    private void validateName(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("username cannot be empty");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new IllegalArgumentException("username must be in the bounds" + "\n(more than 3 characters, less than 50 characters)");
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("username can only contain letters numbers and underscores");
        }
    }
}