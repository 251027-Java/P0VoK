package org.example;

import org.example.service.*;
import org.example.models.*;

import java.time.*;
import java.util.*;

public class UI {
    private Scanner scanner;
    private userService  userService;
    private movieService  movieService;
    private reviewService  reviewService;
    private watchlistService  watchlistService;
    private TMDbService tmDbService;

    private user current;
    private boolean running;

    public UI() {
        this.scanner = new Scanner(System.in);
        this.userService = new userService();
        this.movieService = new movieService();
        this.reviewService = new reviewService();
        this.watchlistService = new watchlistService();
        this.tmDbService = new TMDbService();
        this.current = null;
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

    private void printIntro() {
    }
}
