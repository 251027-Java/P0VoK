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
            Optional<movie> opt = movieRepo.readID(movieID);

            if (opt.isEmpty()) {
                return Optional.empty();
            }

            movie m = opt.get();
            return Optional.of(m);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
