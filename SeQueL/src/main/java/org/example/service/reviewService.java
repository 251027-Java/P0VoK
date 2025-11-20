package org.example.service;

import org.example.models.review;
import org.example.models.user;
import org.example.models.movie;
import org.example.repos.reviewRepo;
import org.example.repos.userRepo;
import org.example.repos.movieRepo;

import java.sql.*;
import java.time.LocalDate;
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
        }
    }
}
