# SeQueL

A personal movie database and review management system built with Java. SeQueL allows you to track movies, write reviews, manage watchlists, and search for movies using The Movie Database (TMDb) API.

## Features

- Movie Database: Search and add movies from TMDb API
- Accounts: Create and manage user accounts
- User Reviews: Rate and review movies with ratings (0-5 scale)
- Watchlist: Keep track of movies you want to watch
- PostgreSQL: Robust data persistence with relational database
- Unit Testing: Comprehensive test suite using JUnit and Mockito

## Tech Stack

- Java 25
- PostgreSQL
- TMDb (The Movie Database) API
- JUnit 5, Mockito

## Prerequisites

Before you begin, ensure you have the following installed:

- Java JDK 25 (or compatible version)
- Maven 3.6+
- PostgreSQL 12+ (or compatible version) - **or Docker** (recommended)
- TMDb API Key

## Installation

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd P0VoK
   ```

2. **Set up PostgreSQL Database**

   **Option A: Using Docker (Recommended)**
   ```bash
   # Run PostgreSQL in a Docker container
   docker run --name sequel-postgres \
     -e POSTGRES_PASSWORD=postgres \
     -e POSTGRES_DB=SeQueL \
     -p 5432:5432 \
     -d postgres:15
   
   # Wait a few seconds for the container to start, then initialize the schema
   docker exec -i sequel-postgres psql -U postgres -d SeQueL < testDB/SeQueL.sql
   ```

   **Option B: Using Local PostgreSQL Installation**
   ```bash
   # Create the database
   psql -U postgres -f testDB/SeQueL.sql
   ```

3. **Configure Database Connection**
   
   Update `SeQueL/src/main/resources/config.properties` with your database credentials:
   
   **If using Docker:**
   ```properties
   db.url=jdbc:postgresql://localhost:5432/SeQueL
   db.username=postgres
   db.password=postgres
   ```
   
   **If using local PostgreSQL:**
   ```properties
   db.url=jdbc:postgresql://localhost:5432/SeQueL
   db.username=your_username
   db.password=your_password
   ```

4. **Configure TMDb API Key**
   
   Update the API key in `SeQueL/src/main/java/org/example/service/TMDbService.java` or configure it in `config.properties` (currently hardcoded).

5. **Build the project**
   ```bash
   cd SeQueL
   mvn clean install
   ```

## Usage

Run the application:
```bash
cd SeQueL
mvn exec:java -Dexec.mainClass="org.example.Main"
```

The application will:
1. Initialize the database connection
2. Set up the database schema (if not already created)
3. Launch the user interface

## Project Structure

```
P0VoK/
├── SeQueL/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/org/example/
│   │   │   │   ├── Main.java              # Application entry point
│   │   │   │   ├── UI.java                # User interface
│   │   │   │   ├── models/                # Data models
│   │   │   │   │   ├── movie.java
│   │   │   │   │   ├── review.java
│   │   │   │   │   ├── user.java
│   │   │   │   │   └── watchlist.java
│   │   │   │   ├── repos/                 # Database repositories
│   │   │   │   │   ├── DatabaseConnection.java
│   │   │   │   │   ├── SQLInit.java
│   │   │   │   │   ├── movieRepo.java
│   │   │   │   │   ├── reviewRepo.java
│   │   │   │   │   ├── userRepo.java
│   │   │   │   │   └── watchlistRepo.java
│   │   │   │   └── service/               # Business logic
│   │   │   │       ├── movieService.java
│   │   │   │       ├── reviewService.java
│   │   │   │       ├── TMDbService.java
│   │   │   │       ├── userService.java
│   │   │   │       └── watchlistService.java
│   │   │   └── resources/
│   │   │       ├── config.properties
│   │   │       └── insert_sample_data.sql
│   │   └── test/                           # Unit tests
│   │       └── java/org/example/tests/
│   │           ├── reviewTest.java
│   │           ├── userTest.java
│   │           └── watchlistTest.java
│   └── pom.xml                             # Maven configuration
├── testDB/
│   └── SeQueL.sql                          # Database schema
└── README.md
```

## Database Schema

The application uses the following main tables:

- **users**: User accounts with usernames
- **movies**: Movie information (title, director, release date, overview, runtime)
- **reviews**: User reviews of movies (rating, review text, watch date)
- **watchlist**: Movies users want to watch

## Testing

Run the test suite:
```bash
cd SeQueL
mvn test
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is open source and available under the MIT License (or your preferred license).

## Acknowledgments

- [The Movie Database (TMDb)](https://www.themoviedb.org/) for providing the movie data API
