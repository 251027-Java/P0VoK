package org.example.repos;

import org.example.models.movie;
import org.example.models.user;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
                    movieID SERIAL PRIMARY KEY,
                    title VARCHAR(50) UNIQUE NOT NULL,
                    director VARCHAR(50) UNIQUE NOT NULL,
                    release_date DATE,
                    overview TEXT,
                    runtime INTEGER,
 */

public class movieRepo {
    private DatabaseConnection dbConn;

    public movieRepo() {
        this.dbConn = DatabaseConnection.getInstance();
    }

    public movie create(movie m) throws SQLException {
        String sql = "INSERT INTO movies (title, director, release_date, overview, runtime) VALUES (?, ?, ?, ?, ?) RETURNING movieID";
        Connection c = null;

        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(1, m.getName());
            stmt.setString(2, m.getDirector());
            stmt.setDate(3, new java.sql.Date(m.getReleaseDate().getTime())); // intellisense answer may change
            stmt.setString(4, m.getOverview());
            stmt.setInt(5, m.getRuntime());

            ResultSet r = stmt.executeQuery();
            if (r.next()) m.setMovieID((r.getInt("movieID")));

            return m;
        } finally {
            dbConn.releaseConn(c);
        }
    }

    public Optional<movie> readID(Integer movieID) throws SQLException {
        String sql = "SELECT * FROM movies WHERE movieID = ?";
        Connection c = null;
        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setInt(1, movieID);

            ResultSet r = stmt.executeQuery();
            if (r.next()) return Optional.of((movie) mapRS(r));

            return Optional.empty();
        } finally {
            dbConn.releaseConn(c);
        }
    }

    public List<movie> titleSearch(String search) throws SQLException {
        String sql = "SELECT * FROM movies WHERE LOWER(title) LIKE LOWER(?) ORDER BY release_date DESC LIMIT 10";
        List<movie> movies = new ArrayList<>();

        Connection c = null;
        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, "%" + search.toLowerCase() + "%");

            ResultSet r = stmt.executeQuery();
            while (r.next()) {
                movies.add((movie) mapRS(r));
            }

            return movies;
        }  finally {
            dbConn.releaseConn(c);
        }
    }

    public void update(movie m) throws SQLException {
        String sql = "UPDATE movies SET title = ?, director = ?, release_date = ?, overview = ?, runtime = ? WHERE movieID = ?";

        Connection c = null;
        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(1, m.getName());
            stmt.setString(2, m.getDirector());
            stmt.setDate(3, new java.sql.Date(m.getReleaseDate().getTime()));
            stmt.setString(4, m.getOverview());
            stmt.setInt(5, m.getRuntime());
            stmt.setInt(6, m.getMovieID());

            stmt.executeUpdate();
        }  finally {
            dbConn.releaseConn(c);
        }
    }

    private Object mapRS(ResultSet r) throws SQLException{
        return new movie(r.getInt("movieID"),
        r.getString("name"),
        r.getDate("releaseDate"),
        r.getString("director"),
        r.getInt("runtime"),
        r.getString("overview")
        );
    }

}
