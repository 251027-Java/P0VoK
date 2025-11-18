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
    
    private void validateName(String username) {
    }
}

