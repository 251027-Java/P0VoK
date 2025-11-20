package org.example;

import org.example.service.*;
import org.example.models.*;
import org.example.service.TMDbService.TMDb;

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

        cleanup();
    }

    private void loginScreen() {
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

        } catch (IllegalAccessException | SQLException e) {
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

    private void printIntro() {
    }

    private void clearScreen() {
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

    private void mainMenu() {
        clearScreen();
        System.out.println("1.  Search Movies (TMDb)");
        System.out.println("2.  Log a Movie");
        System.out.println("3.  View My Reviews");
        System.out.println("4.  View My Watchlist");
        System.out.println("5.  Browse by Genre");
        System.out.println("6.  Recent Reviews (Social Feed)");
        System.out.println("7.  My Profile & Stats");
        System.out.println("8.  Logout");
        System.out.println("9.  Exit");

        String input = scanner.nextLine();

        switch (input) {
            case "1":
                searchMovies();
                break;
            case "2":
                logMovie();
                break;
            case "3":
                viewReviews();
                break;
            case "4":
                viewWatchlist();
                break;
            case "5":
                browseGenre();
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
            }
        }
    }


}
