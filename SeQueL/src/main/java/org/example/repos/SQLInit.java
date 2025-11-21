package org.example.repos;

import java.sql.*;

public class SQLInit {
    private DatabaseConnection dbConn;

    public SQLInit() {
        this.dbConn = DatabaseConnection.getInstance();
    }

    public void init() throws SQLException {
        System.out.println("Checking / Creating database schema...");

        createUsersTable();
        createMoviesTable();
        createReviewsTable();
        createGenresTable();
        createMGJunctionTable();
        createWatchlistTable();

        System.out.println("DB initialized.");
    }

    private void  createUsersTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    userID SERIAL PRIMARY KEY,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    
                    CONSTRAINT name_length CHECK (LENGTH(username) >= 3)
                )
                """;

        execSQL(sql);
        execSQL("CREATE INDEX IF NOT EXISTS idx_username ON users(username)");
    }

    private void createMoviesTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS movies (
                    movieID SERIAL PRIMARY KEY,
                    title VARCHAR(50) UNIQUE NOT NULL,
                    director VARCHAR(50) UNIQUE NOT NULL,
                    release_date DATE,
                    overview TEXT,
                    runtime INTEGER,
                    
                    CONSTRAINT tmdb_id CHECK (movieID > 0),
                    CONSTRAINT runtime_pos CHECK (runtime IS NOT NULL OR runtime > 0)
                    )
        """;

        execSQL(sql);
        execSQL("CREATE INDEX IF NOT EXISTS idx_movieID ON movies(movieID)");
        execSQL("CREATE INDEX IF NOT EXISTS idx_title ON movies(title)");
    }

    private void createReviewsTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS reviews (
                    reviewID SERIAL PRIMARY KEY,
                    userID INTEGER NOT NULL,
                    movieID INTEGER NOT NULL,
                    rating DECIMAL(3,1) NOT NULL,
                    review TEXT,
                    watch_date DATE,
                    
                    FOREIGN KEY (userID) REFERENCES users(userID),
                    FOREIGN KEY (movieID) REFERENCES movies(movieID),
                    
                    CONSTRAINT rating_range CHECK (rating >= 0 AND rating <= 5 AND (rating * 2)::integer = rating * 2),
                    CONSTRAINT unique_relation UNIQUE (userID, movieID)
                )
        """;

        execSQL(sql);
        execSQL("CREATE INDEX IF NOT EXISTS idx_rev_userID ON reviews(userID)");
        execSQL("CREATE INDEX IF NOT EXISTS idx_rev_movieID ON reviews(movieID)");

    }

    private void createGenresTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS genres (
                    genreID INTEGER PRIMARY KEY,
                    movieID INTEGER UNIQUE NOT NULL,
                    name  VARCHAR(50) UNIQUE NOT NULL,
                    
                    CONSTRAINT id_pos CHECK (movieID > 0)
                )
        """;

        execSQL(sql);

        execSQL("CREATE INDEX IF NOT EXISTS idx_genres_movie ON genres(movieID)");
    }

    // copied from the SeQueL.sql file so it looks a little jank may go back and fix after i run it and test
    private void createMGJunctionTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS movie_genres (
                                            movieID INTEGER NOT NULL,
                                            genreID INTEGER NOT NULL,
                
                                            PRIMARY KEY(movieID, genreID),
                
                                            FOREIGN KEY (movieID) REFERENCES movies(movieID),
                                            FOREIGN KEY (genreID) REFERENCES genres(genreID)
                                        )
        """;

        execSQL(sql);

        execSQL("CREATE INDEX IF NOT EXISTS idx_mg_movie on movie_genres(movieID)");
        execSQL("CREATE INDEX IF NOT EXISTS idx_mg_genre on movie_genres(genreID)");
    }

    private void createWatchlistTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS watchlist (
                            watchlistID SERIAL PRIMARY KEY,
                            userID INTEGER NOT NULL,
                            movieID INTEGER NOT NULL,
                            watchDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                
                            FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE,
                            FOREIGN KEY (movieID) REFERENCES movies(movieID) ON DELETE CASCADE,
                
                            CONSTRAINT unique_watch UNIQUE (userID, movieID)
                        )
        """;

        execSQL(sql);

        execSQL("CREATE INDEX IF NOT EXISTS idx_watchlist_user ON watchlist(userID)");
        execSQL("CREATE INDEX IF NOT EXISTS idx_watchlist_movie ON watchlist(movieID)");
    }

    public void dropTables() throws SQLException {
        execSQL("DROP TABLE IF EXISTS watchlist CASCADE");
        execSQL("DROP TABLE IF EXISTS movie_genres CASCADE");
        execSQL("DROP TABLE IF EXISTS reviews CASCADE");
        execSQL("DROP TABLE IF EXISTS genres CASCADE");
        execSQL("DROP TABLE IF EXISTS movies CASCADE");
        execSQL("DROP TABLE IF EXISTS users CASCADE");
    }

    public void reset() throws SQLException {
        dropTables();
        init();
    }

    private void execSQL(String sql) throws SQLException {
        Connection c = null;
        try {
            c = dbConn.getConn();
            Statement stmt = c.createStatement();
            stmt.execute(sql);
        } finally {
            dbConn.releaseConn(c);
        }
    }


}