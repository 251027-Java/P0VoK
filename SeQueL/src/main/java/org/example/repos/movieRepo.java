package org.example.repos;

import org.example.models.movie;

import java.sql.*;
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

}
