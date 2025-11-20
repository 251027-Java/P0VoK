package org.example.service;

import org.example.models.movie;
import org.example.repos.movieRepo;
import org.example.models.genre;
import org.example.repos.genreRepo;
import java.sql.*;
import java.util.*;

public class movieService {
    private movieRepo movieRepo;
    private genreRepo genreRepo;

    public movieService() {
        this.movieRepo = new movieRepo();
        this.genreRepo = new genreRepo();
    }

    public Optional<MovieWGenres> getMovieID(int movieID) {
        try {
            Optional<movie> opt = movieRepo.readID(movieID);

            if (opt.isEmpty()) {
                return Optional.empty();
            }

            movie m = opt.get();
            List<genre> g = movieRepo.getGenre(movieID);

            return Optional.of(new MovieWGenres(m, g));
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

            //

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

    public List<movie> getMoviesByGenre(int genreID) {
        try {
            return movieRepo.getMoviesByGenre(genreID);
        } catch (SQLException e) {
            throw new RuntimeException("failed to get movies by genre: " + e.getMessage(), e);
        }
    }

    public List<genre> allGenres() {
        try {
            return genreRepo.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("failed to get all genres: " + e.getMessage(), e);
        }
    }

    public static class MovieWGenres {
        private movie m;
        private List<genre> g;

        public MovieWGenres(movie m, List<genre> g) {
            this.m = m;
            this.g = g;
        }

        public movie getMovie() {
            return m;
        }
        public List<genre> getGenre() {
            return g;
        }

        public String genreString() {
            if (genreString().isEmpty()) {
                return "No genres";
            }
            return String.join(", ", g.stream().map(genre::getName).toArray(String[]::new));
        }
    }
}


