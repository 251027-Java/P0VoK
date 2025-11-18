package org.example.repos;

import java.sql.*;
import java.util.Optional;

import org.example.models.user;

public class userRepo {
    private DatabaseConnection dbConn;

    public userRepo() {
        this.dbConn = DatabaseConnection.getInstance();
    }

    public user create(user u) throws SQLException {
        String sql = "INSERT INTO users (username, date) VALUES (?, ?) RETURNING userID";
        Connection c = null;

        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(1, u.getUsername());
            stmt.setTimestamp(2, Timestamp.valueOf(u.getDate()));

            ResultSet r = stmt.executeQuery();
            if (r.next()) u.setUserID(r.getInt("userID"));

            return u;
        } finally {
            dbConn.releaseConn(c);
        }
    }

    public Optional<user> readID(Integer userID) throws SQLException {
        String sql = "SELECT * FROM users WHERE userID = ?";
        Connection c = null;
        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setInt(1, userID);

            ResultSet r = stmt.executeQuery();
            if (r.next()) Optional.of(mapRS(r));

            return Optional.empty();
        } finally {
            dbConn.releaseConn(c);
        }
    }

    public Optional<user> readName(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        Connection c = null;
        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(1, username);

            ResultSet r = stmt.executeQuery();
            if (r.next()) return Optional.of(mapRS(r));

            return Optional.empty();
        } finally {
            dbConn.releaseConn(c);
        }
    }

    public void update(user u) throws SQLException {
        String sql = "UPDATE users SET username = ? WHERE userID = ?";
        Connection c = null;
        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(1, u.getUsername());
            stmt.setInt(2, u.getUserID());

            stmt.executeUpdate();
        } finally {
            dbConn.releaseConn(c);
        }
    }

    public void delete(Integer userID) throws SQLException {
        String sql = "DELETE FROM users WHERE userID = ?";
        Connection c = null;
        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setInt(1, userID);

            stmt.executeUpdate();
        } finally {
            dbConn.releaseConn(c);
        }
    }

    private user mapRS(ResultSet r) throws SQLException {
        return new user(r.getInt("userID"), r.getString("username"), r.getTimestamp("date").toLocalDateTime());
    }

    public boolean usernameExists(String username) throws SQLException {
        return readName(username).isPresent();
    }

}
