package org.example;

import org.example.repos.DatabaseConnection;
import org.example.repos.SQLInit;
import org.example.UI;

import java.sql.SQLException;

public class Main {
    static void main() {
        System.out.println("starting SeQueL...");

        try {
            initDB();

            UI ui = new UI();
            ui.start();

            shutdown();
        } catch (Exception e) {
            System.err.println("fatal error... " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

    }

    private static void initDB() {
        try {
            System.out.println("checking db connection...");

            DatabaseConnection dbConn = DatabaseConnection.getInstance();
            System.out.println("db connection established");

            System.out.println("initializing db schema...");
            SQLInit init = new SQLInit();
            init.init();
            System.out.println("schema ready");
        } catch (SQLException e) {
            System.err.println("failed to initialize db: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

    }

    private static void shutdown() {
        System.out.println("shutting down SeQueL. . .");

        try {
            DatabaseConnection dbConn = DatabaseConnection.getInstance();
            dbConn.shutdown();
            System.out.println("db connections closed");
        } catch (Exception e) {
            System.err.println("failed to shutdown db: " + e.getMessage());
        }

        System.out.println("byebye");
    }
}

/* MAIN M:M
user and movies have reviews act as the junction
- one user can review many, one movie can be reviewed by many
- reviews table is the junction (user id, movie id, rating, review text, watch date)
 */

/* tables brainstorming
users
- user id
- username (shouldn't be unique? display above their review)
- created_at (date time)

movies
- movie id (could use tmdb id for this)
- title
- release date
- overview

reviews
- FK user id
- FK movie id
- constraint on user id movie id make it unique
- rating
- review text
- watch date
- date watched
 */