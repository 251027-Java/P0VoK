package org.example.service;

import org.example.models.movie;
import org.example.repos.movieRepo;
import java.sql.*;
import java.util.*;

public class movieService {
    private movieRepo movieRepo;

    public movieService() {
        this.movieRepo = new movieRepo();
    }

    public Optional<movie> getMovieID(int movieID) {
        try {
            return movieRepo.readID(movieID);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get movie: " + e.getMessage(), e);
        }
    }

    public movie cache(movie m, List<Integer> genreID) {
        try {
            Optional<movie> checkM = movieRepo.readID(m.getMovieID());
            if (checkM.isPresent()) {
                return checkM.get();
            }

            movie save = movieRepo.create(m);

            return save;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<movie> searchCached(String search) {
        try {
            if (search == null || search.trim().isEmpty()) {
                throw new IllegalArgumentException("search term cannot be empty");
            }

            return movieRepo.titleSearch(search);
        } catch (SQLException e) {
            throw new RuntimeException("search failed: " + e.getMessage(), e);
        }
    }
}


