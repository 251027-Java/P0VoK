package org.example.repos;

import org.example.models.genre;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.*;

public class genreRepo {
    private DatabaseConnection dbConn;

    public genreRepo() {
        this.dbConn = DatabaseConnection.getInstance();
    }

    public genre create(genre g) throws SQLException {
        String sql = "INSERT INTO genres (genreID, name) VALUES (?, ?) RETURNING genreID";

        Connection c = null;
        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setInt(1, g.getGenreID());
            stmt.setString(2, g.getName());

            ResultSet r = stmt.executeQuery();
            if (r.next()) {
                g.setGenreID(r.getInt("genreID"));
            }

            return g;
        } finally {
            dbConn.releaseConn(c);
        }
    }

    public Optional<genre> findGenreID(int genreID) throws SQLException {
        String sql = "SELECT * FROM genres WHERE genreID = ?";

        Connection c = null;
        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, genreID);

            ResultSet r = stmt.executeQuery();
            if (r.next()) {
                return Optional.of(mapRS(r));
            }

            return Optional.empty();
        } finally {
            dbConn.releaseConn(c);
        }
    }

    public List<genre> findAll() throws SQLException {
        String sql = "SELECT * FROM genres ORDER BY name";
        List<genre> g = new ArrayList<>();

        Connection c = null;
        try {
            c = dbConn.getConn();
            Statement stmt = c.createStatement();
            ResultSet r = stmt.executeQuery(sql);

            while (r.next()) {
                g.add(mapRS(r));
            }

            return g;
        } finally {
            dbConn.releaseConn(c);
        }
    }

    private genre mapRS(ResultSet r) throws SQLException {
        return new genre(r.getInt("genreID"), r.getInt("movieID"), r.getString("name"));
    }
}
