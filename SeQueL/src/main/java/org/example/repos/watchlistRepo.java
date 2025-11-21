package org.example.repos;

import org.example.models.watchlist;
import java.sql.*;
import java.util.*;

public class watchlistRepo {
    private DatabaseConnection dbConn;

    public watchlistRepo() {
        this.dbConn = DatabaseConnection.getInstance();
    }

    public watchlist create(watchlist w) throws SQLException {
        String sql = "INSERT INTO watchlist (userID, movieID, watchDate) VALUES (?, ?, ?) RETURNING watchlistID";

        Connection c = null;
        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setInt(1, w.getUserID());
            stmt.setInt(2, w.getMovieID());
            stmt.setTimestamp(3, Timestamp.valueOf(w.getWatchDate()));

            ResultSet r = stmt.executeQuery();
            if (r.next()) {
                w.setWatchlistID(r.getInt("watchlistID"));
            }

            return w;
        } finally {
            dbConn.releaseConn(c);
        }
    }

    public List<watchlist> findUser(int userID) throws SQLException {
        String sql = """
                SELECT w.*, m.title,
                EXTRACT(YEAR FROM m.release_date) as release_year
                FROM watchlist w
                JOIN movies m ON w.movieID = m.movieID
                WHERE w.userID = ?
                ORDER BY w.watchDate DESC
                """;

        List<watchlist> entries = new ArrayList<>();
        Connection c = null;

        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setInt(1, userID);

            ResultSet r = stmt.executeQuery();
            while (r.next()) {
                entries.add(mapRS(r));
            }

            return entries;
        } finally {
            dbConn.releaseConn(c);
        }
    }

    public Optional<watchlist> findUserMovie(int userID, int movieID) throws SQLException {
        String sql = """
                SELECT w.*, m.title,
                EXTRACT(YEAR FROM m.release_date) as release_year
                FROM watchlist w
                JOIN movies m ON w.movieID = m.movieID
                WHERE w.userID = ? AND w.movieID = ?
                """;

        Connection c = null;

        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setInt(1, userID);
            stmt.setInt(2, movieID);

            ResultSet r = stmt.executeQuery();
            if (r.next()) {
                return Optional.of(mapRS(r));
            }

            return Optional.empty();
        } finally {
            dbConn.releaseConn(c);
        }
    }

    public void delete(int watchlistID) throws SQLException {
        String sql = "DELETE FROM watchlist WHERE watchlistID = ?";

        Connection c = null;

        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, watchlistID);
            stmt.executeUpdate();
        } finally {
            dbConn.releaseConn(c);
        }
    }

    public void deleteUserMovie(int userID, int movieID) throws SQLException {
        String sql = "DELETE FROM watchlist WHERE userID = ? AND movieID = ?";

        Connection c = null;

        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, userID);
            stmt.setInt(2, movieID);
            stmt.executeUpdate();
        } finally {
            dbConn.releaseConn(c);
        }
    }

    public int watchlistCount(int userID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM watchlist WHERE userID = ?";

        Connection c = null;

        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, userID);

            ResultSet r = stmt.executeQuery();
            if (r.next()) {
                return r.getInt(1);
            }
            return 0;
        } finally {
            dbConn.releaseConn(c);
        }
    }

    // use for inWatchlist
    public boolean onList(int userID, int movieID) throws SQLException {
        return findUserMovie(userID, movieID).isPresent();
    }

    private watchlist mapRS(ResultSet r) throws SQLException {
        watchlist w = new watchlist(r.getInt("watchlistID"),
                r.getInt("userID"),
                r.getInt("movieID"),
                r.getTimestamp("watchDate").toLocalDateTime()
        );

        w.setMovieName(r.getString("title"));
        Object releaseYear = r.getObject("release_year");
        if (releaseYear != null) {
            w.setReleaseYear(((Number) releaseYear).intValue());
        }

        return w;
    }

}
