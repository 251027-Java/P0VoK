package org.example.repos;

import org.example.models.movie;
import org.example.models.review;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/*

                    reviewID INTEGER PRIMARY KEY,
                    userID INTEGER NOT NULL,
                    movieID INTEGER NOT NULL,
                    rating DECIMAL(3,1) NOT NULL,
                    review TEXT,
                    watch_date DATE,

                    FOREIGN KEY (userID) REFERENCES users(userID),
                    FOREIGN KEY (movieID) REFERENCES movies(movieID),

                    CONSTRAINT rating_range CHECK (rating >= 0 AND rating <= 5)
                    CONSTRAINT unique_relation UNIQUE (userID, movieID)
 */

public class reviewRepo {
    private DatabaseConnection dbConn;

    public reviewRepo() {
        this.dbConn = DatabaseConnection.getInstance();
    }

    public review create(review re) throws SQLException {
        String sql = "INSERt INTO reviews (userID, movieID, rating, review, watch_date)" +
                "VALUES (?, ?, ?, ?, ?) RETURNING reviewID";

        Connection c = null;
        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setInt(1, re.getUserID());
            stmt.setInt(2, re.getMovieID());
            stmt.setDouble(3, re.getRating());
            stmt.setString(4, re.getReviewTxt());
            stmt.setDate(5, (Date) re.getDate()); // i dont know may have to change

            ResultSet r = stmt.executeQuery();
            if (r.next()) {
                re.setReviewID(r.getInt("reviewID"));
            }

            return re;
        } finally {
            dbConn.releaseConn(c);
        }
    }

    public Optional<review> findByIDS(int userID, int movieID) throws SQLException {
        String sql = """
                SELECT r.*, u.username, m.title as movie_title
                FROM reviews r
                INNER JOIN users u ON r.userID = u.userID
                INNER JOIN movies m ON r.movieID = m.movieID
                WHERE r.userID = ? AND r.movieID = ?
                """;

        Connection c = null;
        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setInt(1, userID);
            stmt.setInt(2, movieID);

            ResultSet r = stmt.executeQuery();
            if (r.next()) {
                return Optional.of((review) mapRS(r));
            }

            return Optional.empty();
        }  finally {
            dbConn.releaseConn(c);
        }
    }

    public List<review> findUser(int userID) throws SQLException {
        String sql = """
                SELECT r.*, u.username, m.title as movie_title
                FROM reviews r
                INNER JOIN users u ON r.userID = u.userID
                INNER JOIN movies m ON r.movieID = m.movieID
                WHERE r.userID = ?
                ORDER BY r.reviewDate DESC;
        """;

        return execQ(sql, userID);
    }

    public List<review> findMovie(int movieID) throws SQLException {
        String sql = """
                SELECT r.*, u.username, m.title as movie_title
                FROM reviews r
                INNER JOIN users u ON r.userID = u.userID
                INNER JOIN movies m ON r.movieID = m.movieID
                WHERE r.movieID = ?
                ORDER BY r.reviewDate DESC;
        """;

        return execQ(sql, movieID);
    }

    public List<review> findRecent(int limit) throws SQLException {
        String sql = """
                SELECT r.*, u.username, m.title as movie_title
                FROM reviews r
                INNER JOIN users u ON r.userID = u.userID
                INNER JOIN movies m ON r.movieID = m.movieID
                ORDER BY r.reviewDate DESC LIMIT ?;
        """;

        return execQ(sql, limit);
    }

    public void update (review re)  throws SQLException {
        String sql = "UPDATE reviews SET rating = ?, review = ?, watch_date = ? WHERE reviewID = ?;";

        Connection c = null;
        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setDouble(1, re.getRating());
            stmt.setString(2, re.getReviewTxt());
            stmt.setDate(3, (Date) re.getDate());
            stmt.setInt(4, re.getReviewID());

            stmt.executeUpdate();
        }  finally {
            dbConn.releaseConn(c);
        }
    }

    public void delete (int reviewID) throws SQLException {
        String sql = "DELETE FROM reviews WHERE reviewID = ?;";

        Connection c = null;
        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, reviewID);

            stmt.executeUpdate();
        }   finally {
            dbConn.releaseConn(c);
        }
    }

    public int reviewCount (int userID)  throws SQLException {
        String sql = "SELECT COUNT(*) FROM reviews WHERE userID = ?;";

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
        }   finally {
            dbConn.releaseConn(c);
        }
    }

    public Double avgRating (int movieID) throws SQLException {
        String sql = "SELECT AVG(rating) as avg_rating FROM reviews WHERE movieID = ?;";

        Connection c = null;
        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, movieID);

            ResultSet r = stmt.executeQuery();
            if (r.next()) {
                return r.getDouble("avg_rating");
            }
            return null;
        }   finally {
            dbConn.releaseConn(c);
        }
    }

    // int userID, int movieID, double rating, String reviewTxt, Date watchDate
    private Object mapRS(ResultSet r) throws SQLException{
        return new review(r.getInt("userID"),
                r.getInt("movieID"),
                r.getDouble("rating"),
                r.getString("reviewTxt"),
                r.getDate("watchDate")
        );
    }

    private List<review> execQ(String sql, int p) throws SQLException {
        List<review> list = new ArrayList<>();
        Connection c = null;

        try {
            c = dbConn.getConn();
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, p);

            ResultSet r = stmt.executeQuery();
            while (r.next()) {
                list.add((review) mapRS(r));
            }

            return list;
        } finally  {
            dbConn.releaseConn(c);
        }
    }

}
