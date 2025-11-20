package org.example;

import org.example.repos.DatabaseConnection;
import org.example.repos.SQLInit;

import java.sql.SQLException;

public class Main {
    static void main() {
        DatabaseConnection c = new DatabaseConnection();

        try {
            SQLInit init = new SQLInit();

            init.init();

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }


    }
}

/* MAIN M:M
user and movies have reviews act as the junction
- one user can review many, one movie can be reviewed by many
- reviews table is the junction (user id, movie id, rating, review text, watch date)

possible other m to m
movies and genre - IF movies and users is too barebones idk yet
- use for browsing or filtering movies
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

genres ?
- genre id
- genre name

genre junction table
- FK movie id
- FK genre id
- composite key it
 */