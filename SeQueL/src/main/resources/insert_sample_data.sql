-- Sample data insertion script for SeQueL database
-- This script inserts users, movies, and reviews

-- First, insert some sample movies
INSERT INTO movies (title, director, release_date, overview, runtime) VALUES
('The Matrix', 'Wachowski Brothers', '1999-03-31', 'A computer hacker learns about the true nature of reality', 136),
('Inception', 'Christopher Nolan', '2010-07-16', 'A thief who steals corporate secrets through dream-sharing technology', 148),
('The Dark Knight', 'Christopher Nolan', '2008-07-18', 'Batman faces the Joker in Gotham City', 152),
('Pulp Fiction', 'Quentin Tarantino', '1994-10-14', 'The lives of two mob hitmen, a boxer, and more intertwine', 154),
('Fight Club', 'David Fincher', '1999-10-15', 'An insomniac office worker and a devil-may-care soapmaker form an underground fight club', 139),
('Forrest Gump', 'Robert Zemeckis', '1994-07-06', 'The presidencies of Kennedy and Johnson unfold through the perspective of an Alabama man', 142),
('The Shawshank Redemption', 'Frank Darabont', '1994-09-23', 'Two imprisoned men bond over a number of years', 142),
('Interstellar', 'Christopher Nolan', '2014-11-07', 'A team of explorers travel through a wormhole in space', 169),
('The Godfather', 'Francis Ford Coppola', '1972-03-24', 'The aging patriarch of an organized crime dynasty transfers control to his son', 175),
('Goodfellas', 'Martin Scorsese', '1990-09-21', 'The story of Henry Hill and his life in the mob', 146),
('The Departed', 'Martin Scorsese', '2006-10-06', 'An undercover cop and a mole in the police force try to identify each other', 151),
('Gladiator', 'Ridley Scott', '2000-05-05', 'A former Roman General sets out to exact vengeance', 155),
('The Lord of the Rings: Fellowship', 'Peter Jackson', '2001-12-19', 'A hobbit embarks on a journey to destroy a powerful ring', 178),
('Avatar', 'James Cameron', '2009-12-18', 'A paraplegic marine dispatched to the moon Pandora', 162),
('Titanic', 'James Cameron', '1997-12-19', 'A seventeen-year-old aristocrat falls in love with a kind but poor artist', 194),
('Jurassic Park', 'Steven Spielberg', '1993-06-11', 'A pragmatic paleontologist visits an almost complete theme park', 127),
('Star Wars: A New Hope', 'George Lucas', '1977-05-25', 'Luke Skywalker joins forces with a Jedi Knight', 121),
('The Avengers', 'Joss Whedon', '2012-05-04', 'Earth''s mightiest heroes must come together', 143),
('Dune', 'Denis Villeneuve', '2021-10-22', 'Paul Atreides leads a rebellion to restore his family''s reign', 155),
('Parasite', 'Bong Joon-ho', '2019-10-11', 'A poor family schemes to become employed by a wealthy family', 132)
ON CONFLICT (title) DO NOTHING;

-- Insert users with randomized dates
INSERT INTO users (username, date) VALUES
('liam', '2023-01-15 10:30:00'),
('matt', '2023-02-22 14:45:00'),
('marcus', '2023-03-08 09:15:00'),
('sydney', '2023-04-12 16:20:00'),
('natalia', '2023-05-03 11:00:00'),
('manu', '2023-06-18 13:30:00'),
('brody', '2023-07-25 08:45:00'),
('omar', '2023-08-09 15:10:00'),
('pranav', '2023-09-14 12:25:00'),
('alvey', '2023-10-07 10:50:00'),
('raman', '2023-11-20 14:15:00'),
('richard', '2023-12-05 09:40:00'),
('osi', '2024-01-11 16:05:00'),
('shara', '2024-02-16 11:30:00'),
('walker', '2024-03-22 13:55:00'),
('JB', '2024-04-28 08:20:00'),
('juan', '2024-05-13 15:45:00'),
('donovan', '2024-06-19 10:10:00'),
('fahad', '2024-07-24 12:35:00'),
('shurya', '2024-08-30 14:00:00'),
('zheng', '2024-09-15 09:25:00')
ON CONFLICT (username) DO NOTHING;

-- Insert reviews for each user (1-2 random movies per user)
-- Ratings are in increments of 0.5 (0.0, 0.5, 1.0, ..., 5.0)
-- Watch dates are randomized

INSERT INTO reviews (userID, movieID, rating, review, watch_date) VALUES
-- liam reviews 2 movies
((SELECT userID FROM users WHERE username = 'liam'), (SELECT movieID FROM movies WHERE title = 'The Matrix'), 4.5, 'yea i liked it', '2023-06-10'),
((SELECT userID FROM users WHERE username = 'liam'), (SELECT movieID FROM movies WHERE title = 'Inception'), 5.0, 'yea i liked it', '2023-08-22'),

-- matt reviews 1 movie
((SELECT userID FROM users WHERE username = 'matt'), (SELECT movieID FROM movies WHERE title = 'The Dark Knight'), 4.0, 'yea i liked it', '2023-09-15'),

-- marcus reviews 2 movies
((SELECT userID FROM users WHERE username = 'marcus'), (SELECT movieID FROM movies WHERE title = 'Pulp Fiction'), 4.5, 'yea i liked it', '2023-10-05'),
((SELECT userID FROM users WHERE username = 'marcus'), (SELECT movieID FROM movies WHERE title = 'Fight Club'), 3.5, 'yea i liked it', '2023-11-18'),

-- sydney reviews 2 movies
((SELECT userID FROM users WHERE username = 'sydney'), (SELECT movieID FROM movies WHERE title = 'Forrest Gump'), 5.0, 'yea i liked it', '2023-12-01'),
((SELECT userID FROM users WHERE username = 'sydney'), (SELECT movieID FROM movies WHERE title = 'The Shawshank Redemption'), 4.5, 'yea i liked it', '2024-01-12'),

-- natalia reviews 1 movie
((SELECT userID FROM users WHERE username = 'natalia'), (SELECT movieID FROM movies WHERE title = 'Interstellar'), 4.0, 'yea i liked it', '2024-02-20'),

-- manu reviews 2 movies
((SELECT userID FROM users WHERE username = 'manu'), (SELECT movieID FROM movies WHERE title = 'The Godfather'), 5.0, 'yea i liked it', '2024-03-08'),
((SELECT userID FROM users WHERE username = 'manu'), (SELECT movieID FROM movies WHERE title = 'Goodfellas'), 4.5, 'yea i liked it', '2024-04-15'),

-- brody reviews 1 movie
((SELECT userID FROM users WHERE username = 'brody'), (SELECT movieID FROM movies WHERE title = 'The Departed'), 4.0, 'yea i liked it', '2024-05-22'),

-- omar reviews 2 movies
((SELECT userID FROM users WHERE username = 'omar'), (SELECT movieID FROM movies WHERE title = 'Gladiator'), 4.5, 'yea i liked it', '2024-06-10'),
((SELECT userID FROM users WHERE username = 'omar'), (SELECT movieID FROM movies WHERE title = 'The Lord of the Rings: Fellowship'), 5.0, 'yea i liked it', '2024-07-18'),

-- pranav reviews 2 movies
((SELECT userID FROM users WHERE username = 'pranav'), (SELECT movieID FROM movies WHERE title = 'Avatar'), 3.5, 'yea i liked it', '2024-08-05'),
((SELECT userID FROM users WHERE username = 'pranav'), (SELECT movieID FROM movies WHERE title = 'Titanic'), 4.0, 'yea i liked it', '2024-09-12'),

-- alvey reviews 1 movie
((SELECT userID FROM users WHERE username = 'alvey'), (SELECT movieID FROM movies WHERE title = 'Jurassic Park'), 4.5, 'yea i liked it', '2024-10-20'),

-- raman reviews 2 movies
((SELECT userID FROM users WHERE username = 'raman'), (SELECT movieID FROM movies WHERE title = 'Star Wars: A New Hope'), 5.0, 'yea i liked it', '2024-11-08'),
((SELECT userID FROM users WHERE username = 'raman'), (SELECT movieID FROM movies WHERE title = 'The Avengers'), 4.0, 'yea i liked it', '2024-12-15'),

-- richard reviews 1 movie
((SELECT userID FROM users WHERE username = 'richard'), (SELECT movieID FROM movies WHERE title = 'Dune'), 4.5, 'yea i liked it', '2025-01-22'),

-- osi reviews 2 movies
((SELECT userID FROM users WHERE username = 'osi'), (SELECT movieID FROM movies WHERE title = 'Parasite'), 5.0, 'yea i liked it', '2025-02-10'),
((SELECT userID FROM users WHERE username = 'osi'), (SELECT movieID FROM movies WHERE title = 'The Matrix'), 4.0, 'yea i liked it', '2025-03-18'),

-- shara reviews 2 movies
((SELECT userID FROM users WHERE username = 'shara'), (SELECT movieID FROM movies WHERE title = 'Inception'), 4.5, 'yea i liked it', '2025-04-05'),
((SELECT userID FROM users WHERE username = 'shara'), (SELECT movieID FROM movies WHERE title = 'The Dark Knight'), 5.0, 'yea i liked it', '2025-05-12'),

-- walker reviews 1 movie
((SELECT userID FROM users WHERE username = 'walker'), (SELECT movieID FROM movies WHERE title = 'Pulp Fiction'), 4.0, 'yea i liked it', '2025-06-20'),

-- JB reviews 2 movies
((SELECT userID FROM users WHERE username = 'JB'), (SELECT movieID FROM movies WHERE title = 'Fight Club'), 4.5, 'yea i liked it', '2025-07-08'),
((SELECT userID FROM users WHERE username = 'JB'), (SELECT movieID FROM movies WHERE title = 'Forrest Gump'), 4.0, 'yea i liked it', '2025-08-15'),

-- juan reviews 1 movie
((SELECT userID FROM users WHERE username = 'juan'), (SELECT movieID FROM movies WHERE title = 'The Shawshank Redemption'), 5.0, 'yea i liked it', '2025-09-22'),

-- donovan reviews 2 movies
((SELECT userID FROM users WHERE username = 'donovan'), (SELECT movieID FROM movies WHERE title = 'Interstellar'), 4.5, 'yea i liked it', '2025-10-10'),
((SELECT userID FROM users WHERE username = 'donovan'), (SELECT movieID FROM movies WHERE title = 'The Godfather'), 5.0, 'yea i liked it', '2025-11-18'),

-- fahad reviews 2 movies
((SELECT userID FROM users WHERE username = 'fahad'), (SELECT movieID FROM movies WHERE title = 'Goodfellas'), 4.0, 'yea i liked it', '2025-12-05'),
((SELECT userID FROM users WHERE username = 'fahad'), (SELECT movieID FROM movies WHERE title = 'The Departed'), 4.5, 'yea i liked it', '2026-01-12'),

-- shurya reviews 1 movie
((SELECT userID FROM users WHERE username = 'shurya'), (SELECT movieID FROM movies WHERE title = 'Gladiator'), 4.0, 'yea i liked it', '2026-02-20'),

-- zheng reviews 2 movies
((SELECT userID FROM users WHERE username = 'zheng'), (SELECT movieID FROM movies WHERE title = 'The Lord of the Rings: Fellowship'), 4.5, 'yea i liked it', '2026-03-08'),
((SELECT userID FROM users WHERE username = 'zheng'), (SELECT movieID FROM movies WHERE title = 'Avatar'), 3.5, 'yea i liked it', '2026-04-15')
ON CONFLICT (userID, movieID) DO NOTHING;

