package org.example;

import org.example.service.*;
import org.example.models.*;
import org.example.service.TMDbService.TMDb;
import org.example.service.movieService.MovieWGenres;
import org.example.service.reviewService.stats;

import java.sql.SQLException;
import java.time.*;
import java.util.*;

public class UI {
    private Scanner scanner;
    private userService  userService;
    private movieService  movieService;
    private reviewService  reviewService;
    private watchlistService  watchlistService;
    private TMDbService tmDbService;

    private user currentUser;
    private boolean running;

    public UI() {
        this.scanner = new Scanner(System.in);
        this.userService = new userService();
        this.movieService = new movieService();
        this.reviewService = new reviewService();
        this.watchlistService = new watchlistService();
        this.tmDbService = new TMDbService();
        this.currentUser = null;
        this.running = true;
    }

    public void start() {
        printIntro();

        while (running) {
            try {
                if (currentUser == null) {
                    loginScreen();
                } else {
                    mainMenu();
                }
            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
                System.out.println("press ENTER to continue . . .");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private void loginScreen() throws SQLException {
        clearScreen();
        System.out.println("1. login");
        System.out.println("2. register");
        System.out.println("3. quit");

        String input = scanner.nextLine().trim();

        switch (input) {
            case "1":
                loginReal();
                break;
            case "2":
                registerReal();
                break;
            case "3":
                exit();
                break;
            default:
                System.out.println("invalid input. try again.");

        }
    }

    private void registerReal() {
        clearScreen();

        System.out.print("enter your username: ");
        String username = scanner.nextLine();

        try {
            user newUser = userService.register(username);
            System.out.println("welcome " + username + "!");
            currentUser = newUser;
            pause();

        } catch (SQLException e) {
            System.out.println("registration error: " + e.getMessage());
            pause();
        }
    }

    private void loginReal() throws SQLException {
        clearScreen();

        System.out.print("enter your name: ");
        String username = scanner.nextLine().trim();

        Optional<user> uOpt = userService.login(username); // check this in userService

        if (uOpt.isPresent()) {
            currentUser = uOpt.get();
            System.out.println("login successful! ");
            pause();
        }
        else {
            System.out.println("login failed! ");
            pause();
        }
    }

    private void displayReview(review r) {
        System.out.println("movie: " + r.getMovieName());
        System.out.println("rating: " + r.getFormattedRating());
        System.out.println("review: " + r.getReviewTxt());
        System.out.println("watch date: " + r.getDate());
        System.out.println("\nlogged by: " + r.getUsername());
    }

    private void printIntro() {
        System.out.println("SeQueL");
    }

    private void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private void logout() {
        currentUser = null;
        System.out.println("logged out successfully");
        System.out.println("byebye");
        pause();
    }

    private void exit() {
        System.out.println("byebye");
        running = false;
    }

    private void pause() {
        System.out.print("press ENTER to continue . . .");
        scanner.nextLine();
    }

    private int getIntInput(int min, int max) {
        while (true) {
            try {
                int val = Integer.parseInt(scanner.nextLine());
                if (val >= min || val <= max) {
                    return val;
                }
                System.out.printf("enter value between %d and %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.println("invalid. enter a number: ");
            }
        }
    }

    private double getDoubleInput(double min, double max) {
        while (true) {
            try {
                double val = Double.parseDouble(scanner.nextLine());
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("enter value between %d and %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.println("invalid. enter a number: ");
            }
        }
    }

    private void mainMenu() {
        clearScreen();
        System.out.println("1.  Search Movies (TMDb)");
        //System.out.println("2.  Log a Movie");
        System.out.println("3.  View My Reviews");
        System.out.println("4.  View My Watchlist");
        //System.out.println("5.  Browse by Genre");
        System.out.println("6.  Recent Reviews (Social Feed)");
        System.out.println("7.  My Profile & Stats");
        System.out.println("8.  Logout");
        System.out.println("9.  Exit");

        String input = scanner.nextLine();

        switch (input) {
            case "1":
                searchMovies();
                break;
            /*case "2":
                logMovie();
                break;*/
            case "3":
                viewReviews();
                break;
            case "4":
                viewWatchlist();
                break;
            case "5":
                //browseGenre();
                break;
            case "6":
                recentReviews();
                break;
            case "7":
                profile();
                break;
            case "8":
                logout();
                break;
            case "9":
                exit();
                break;
            default:
                System.out.println("invalid input. try again.");
        }
    }

    private void searchMovies() {
        clearScreen();
        System.out.print("enter movie name: ");
        String movieName = scanner.nextLine();

        if (movieName.isEmpty()) {
            System.out.println("movie name is empty");
        }

        System.out.println("kennethGPT searching TMDb . . .");

        try {
            List<TMDb> results =  tmDbService.searchMovies(movieName);

            if (results.isEmpty()) {
                System.out.println("movie not found");
                pause();
                return;
            }

            System.out.println("search results");
            for (int i = 0; i < results.size(); i++) {
                TMDb result = results.get(i);
                System.out.printf("%d. %s (%s)\n", i + 1, result.getTitle(), result.getYear());
            }

            System.out.println("\n0. main menu");

            int selection = getIntInput(0, results.size());
            if (selection == 0) {
                return;
            }

            TMDb selected = results.get(selection - 1);
            showDetails(selected);

        } catch (Exception e) {
            System.out.println("search failed: " + e.getMessage());
            pause();
        }
    }

    private void showDetails(TMDb tID) {
        clearScreen();

        try {
            movie m = tmDbService.getDetails(tID.getTmdbID());

            System.out.println("title: " + m.getName());
            System.out.println("release date: " + m.getReleaseDate());
            System.out.println("runtime: " + m.getRuntime());
            System.out.println("\noverview: ");
            System.out.println(m.getOverview());

            Optional<movieService.MovieWGenres> cached = movieService.getMovieID(m.getMovieID());
            if (cached.isPresent()) {
                movie tempMovie = cached.get().getMovie();
                System.out.println("movie is in your db!");

                // STOPPED HERE ADD SHOWING STATS
                stats s = reviewService.getMovieStats(tempMovie.getMovieID());
                System.out.printf("avg rating: %s (%d reviews)\n", s.getAvg(), s.getTotal());
            }

            System.out.println("1. log this movie");
            System.out.println("2. add to watchlist");
            System.out.println("3. view reviews");
            System.out.println("0. back");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    logSpecific(m, tID.getGenreIDs());
                    break;
                case "2":
                    addFromSearch(m, tID.getGenreIDs());
                    break;
                case "3":
                    viewSpecificReviews(m, tID.getGenreIDs());
                    break;
                case "0":
                    return;
                default:
                    System.out.println("invalid input. try again.");
            }
            
        } catch (Exception e) {
            System.out.println("failed to load details: " + e.getMessage());
            pause();
        }
    }

    private void logSpecific(movie m, List<Integer> genreIDs) {
        try {
            movie cached = movieService.cache(m, genreIDs);

            if (reviewService.checkReviewed(currentUser.getUserID(), cached.getMovieID())) {
                System.out.println("you already reviewed this movie");
                pause();
                return;
            }

            clearScreen();
            System.out.print("enter your rating (1-10): ");
            double rating = getDoubleInput(0.0, 10.0);

            scanner.nextLine();

            System.out.print("enter your review (press enter to skip): ");
            String review = scanner.nextLine();

            System.out.print("enter the date you watched the movie (YYYY-MM-DD, or ENTER to): ");
            String watchDate = scanner.nextLine();
            LocalDate date = watchDate.isEmpty() ? LocalDate.now() : LocalDate.parse(watchDate);

            review r = reviewService.create(currentUser.getUserID(), cached.getMovieID(), rating, review, date);

            System.out.println("movie logged successfully");
            System.out.println("rating: " + r.getFormattedRating());
            pause();

        } catch (Exception e) {
            System.out.println("failed to log movie: " + e.getMessage());
            pause();
        }
    }

    private void addFromSearch(movie m, List<Integer> genreIDs) {
        try {
        movie cached = movieService.cache(m, genreIDs);

        if (watchlistService.inWatchlist(currentUser.getUserID(), cached.getMovieID())) {
            System.out.println("you already added this movie to your watchlist");
            pause();
            return;
        }

        watchlistService.addToWatchlist(currentUser.getUserID(), cached.getMovieID());
        System.out.println("movie added to watchlist successfully");
        pause();

        } catch (Exception e) {
            System.out.println("failed to add movie to watchlist: " + e.getMessage());
            pause();
        }
    }

    private void viewSpecificReviews(movie m, List<Integer> genreIDs) {
        try {
            Optional<MovieWGenres> cOpt = movieService.getMovieID(m.getMovieID());

            if (cOpt.isEmpty()) {
                System.out.println("no reviews.. yo should review it!");
                pause();
                return;
            }

            movie cached = cOpt.get().getMovie();
            List<review> reviews = reviewService.getMovieReviews(cached.getMovieID());
            clearScreen();

            System.out.println("reviews for " + cached.getName());
            stats s = reviewService.getMovieStats(cached.getMovieID());
            System.out.printf("avg rating: %s (%d reviews)\n", s.getAvg(), s.getTotal());

            System.out.println("reviews: ");
            for (review r : reviews) {
                displayReview(r);
                System.out.println();
            }

            pause();

        } catch (Exception e) {
            System.out.println("failed to view specific reviews: " + e.getMessage());
            pause();
        }
    }

    /*private void logMovie() {
        clearScreen();
        System.out.println("enter movie title: ");
        String title = scanner.nextLine();

        if (title.isEmpty()) {
            System.out.println("movie title is empty");
            pause();
            return;
        }
        
        System.out.println("kennethGPT searching TMDb . . .");

        try {
            List<movie> movies = movieService.searchCached(title);

            if (movies.isEmpty()) {
                System.out.println("no movies found");
                pause();
                return;
            }

            System.out.println("search results");
            for (int i = 0; i < movies.size(); i++) {
                movie m = movies.get(i);
                System.out.printf("%d. %s (%s)\n", i + 1, m.getName(), m.getReleaseDate());
            }
            System.out.println("\n0. main menu");

            int selection = getIntInput(0, movies.size());
            if (selection == 0) return;

            movie selected = movies.get(selection - 1);

            if (reviewService.checkReviewed(currentUser.getUserID(), selected.getMovieID())) {
                System.out.println("you already reviewed this movie");
                pause();
                return;
            }

            System.out.print("enter your rating (1-10): ");
            double rating = getDoubleInput(0.0, 10.0);

            scanner.nextLine();

            System.out.print("enter your review (press enter to skip): ");
            String review = scanner.nextLine();

            System.out.print("enter the date you watched the movie (YYYY-MM-DD, or ENTER to skip): ");
            String watchDate = scanner.nextLine();
            LocalDate date = watchDate.isEmpty() ? LocalDate.now() : LocalDate.parse(watchDate);

            review r = reviewService.create(currentUser.getUserID(), selected.getMovieID(), rating, review, date);

            System.out.println("movie logged successfully");
            System.out.println("rating: " + r.getFormattedRating());
            pause();

        
        } catch (Exception e) {
            System.out.println("failed to log movie: " + e.getMessage());
            pause();
        }

    }*/

    private void viewReviews() {
        clearScreen();

        try {
            List<review> reviews = reviewService.getUserReviews(currentUser.getUserID());
            if (reviews.isEmpty()) {
                System.out.println("no reviews.. yo should review it!");
                pause();
                return;
            }

            System.out.println("total reviews: " + reviews.size());

            for (int i = 0; i < reviews.size(); i++) {
                review r = reviews.get(i);
                System.out.printf("%d. ", i + 1);
                displayReview(r);
                System.out.println("- - -");
            }

            System.out.println("1. delete review");
            System.out.println("0. main menu");

            String input = scanner.nextLine();

            if (input.equals("1")) {
                System.out.print("enter review ID: ");
                int reviewID = getIntInput(1, reviews.size());
                reviewService.deleteReview(reviewID);
                System.out.println("review deleted successfully");
                pause();
            }

        } catch (Exception e) {
            System.out.println("failed to view reviews: " + e.getMessage());
            pause();
        }
    }

    private void viewWatchlist() {
        clearScreen();
        System.out.println("my watchlist :)");

        try {
            List<watchlist> watchlist = watchlistService.getWatchlist(currentUser.getUserID());
            if (watchlist.isEmpty()) {
                System.out.println("your watchlist is empty.");
                System.out.println("do you need some recs...");
                pause();
                return;
            }

            System.out.println("total movies: " + watchlist.size());
            for (int i = 0; i < watchlist.size(); i++) {
                watchlist w = watchlist.get(i);
                System.out.printf("%d. %s (%s)\n", i + 1, w.getMovieName(), w.getReleaseYear());
            }

            System.out.println("\n1. remove from watchlist");
            System.out.println("0. Back");

            String input = scanner.nextLine();

            if (input.equals("1")) {
                System.out.print("enter watchlist ID: ");
                int watchlistID = getIntInput(1, watchlist.size());
                watchlistService.removeFromWatchlist(currentUser.getUserID(), watchlistID);
                System.out.println("movie removed from watchlist successfully");
                pause();
            }
        } catch (Exception e) {
            System.out.println("failed to view watchlist: " + e.getMessage());
            pause();
        }
    }

    private void browseGenre() {
        clearScreen();
        System.out.println("browse by genre");
    }

    private void recentReviews() {
        clearScreen();
        System.out.println("recent reviews");

        try {
            List<review> reviews = reviewService.getRecent(10);

            if (reviews.isEmpty()) {
                System.out.println("no reviews yet.. no ones using my app...");
                pause();
                return;
            }

            for (review r : reviews) {
                displayReview(r);
                System.out.println("- - -");
            }

            pause();
            
        } catch (Exception e) {
            System.out.println("failed to view recent reviews: " + e.getMessage());
            pause();
        }
    }

    private void profile() {
        clearScreen();
        System.out.println("your profile");

        try {
            user s = userService.getStats(currentUser.getUserID());

            System.out.println("username: " + s.getUsername());
            System.out.println("member since: " + s.getDate());
            System.out.println("total reviews: " + s.getReviewCount());
            System.out.println("total watchlist: " + watchlistService.getCount(currentUser.getUserID()));
            pause();
            
        } catch (Exception e) {
            System.out.println("failed to view profile: " + e.getMessage());
            pause();
        }
    }
    
    


}
