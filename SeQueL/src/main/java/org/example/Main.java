package org.example;

public class Main {
    static void main() {


        }
    }
}

/* tables?
movies
- id prim key
- tmdb id?
- title
- overview
- release date
- rating 5 stars
- year created
users?
- id
- username
- review count
m:m
- movie id
- user id
prim(movie, userid)
 */

/* classes? sep files
user.java
- get functions
- thats it
movie.java
watchlist? act as m to m???
repo layer - userrepo
- use resultset connect to api? prob look up how api works first
- use postgres
movierrepo? maybe to keep both sets of data separated
service
- one for movie and service if we do two for repos
 */