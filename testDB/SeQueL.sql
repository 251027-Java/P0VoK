-- Active: 1762362899455@@127.0.0.1@5432
-- Active: 1762362899455@@127.0.0.1@5432

CREATE DATABASE SeQueL;

CREATE TABLE IF NOT EXISTS public.users (
                    userID SERIAL PRIMARY KEY,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    
                    CONSTRAINT name_length CHECK (LENGTH(username) >= 3)
                )

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

CREATE INDEX IF NOT EXISTS idx_username ON users(username)

CREATE INDEX IF NOT EXISTS idx_movieID ON movies(movieID)

CREATE INDEX IF NOT EXISTS idx_title ON movies(title)

CREATE TABLE IF NOT EXISTS reviews (
                    reviewID INTEGER PRIMARY KEY,
                    userID INTEGER NOT NULL,
                    movieID INTEGER NOT NULL,
                    rating DECIMAL(3,1) NOT NULL,
                    review TEXT,
                    watch_date DATE,
                    
                    FOREIGN KEY (userID) REFERENCES users(userID),
                    FOREIGN KEY (movieID) REFERENCES movies(movieID),
                    
                    CONSTRAINT rating_range CHECK (rating >= 0 AND rating <= 5),
                    CONSTRAINT unique_relation UNIQUE (userID, movieID)
                )

CREATE INDEX IF NOT EXISTS idx_rev_userID ON reviews(userID)

CREATE INDEX IF NOT EXISTS idx_rev_movieID ON reviews(movieID)

CREATE TABLE IF NOT EXISTS genres (
                    genreID INTEGER PRIMARY KEY,
                    movieID INTEGER UNIQUE NOT NULL,
                    name  VARCHAR(50) UNIQUE NOT NULL,
                    
                    CONSTRAINT id_pos CHECK (movieID > 0)
                )

CREATE INDEX IF NOT EXISTS idx_genres_movie ON genres(movieID)

CREATE TABLE IF NOT EXISTS movie_genres (
                    movieID INTEGER NOT NULL,
                    genreID INTEGER NOT NULL,
                    
                    PRIMARY KEY(movieID, genreID),

                    FOREIGN KEY (movieID) REFERENCES movies(movieID),
                    FOREIGN KEY (genreID) REFERENCES genres(genreID)
                )

CREATE INDEX IF NOT EXISTS idx_mg_movie on movie_genres(movieID);

CREATE INDEX IF NOT EXISTS idx_mg_genre on movie_genres(genreID);

CREATE TABLE IF NOT EXISTS watchlist (
    watchlistID INTEGER PRIMARY KEY,
    userID INTEGER NOT NULL,
    movieID INTEGER NOT NULL,
    watchDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE,
    FOREIGN KEY (movieID) REFERENCES movies(movieID) ON DELETE CASCADE,
    
    CONSTRAINT unique_watch UNIQUE (userID, movieID)
)

CREATE INDEX IF NOT EXISTS idx_watchlist_user ON watchlist(userID);

CREATE INDEX IF NOT EXISTS idx_watchlist_movie ON watchlist(movieID);