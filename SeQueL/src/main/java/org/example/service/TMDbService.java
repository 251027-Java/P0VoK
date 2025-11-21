package org.example.service;

import org.example.models.movie;

import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.time.LocalDate;

public class TMDbService {
    private static String URL = "https://api.themoviedb.org/3";
    private String apiKey = "";
    private Gson gson = new Gson();

    public TMDbService() {
        this.apiKey = "abbd15cbef07d75707399ba1bce3d65b";
        this.gson = new Gson();
    }

    /*private String loadApiKey() {
        Properties p =  new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties file not found");
            }

            p.load(input);
            String key = p.getProperty("tmdb.api.key");
            if (key == null || key.trim().equals("")) {
                throw new RuntimeException("key not in config.properties file");
            }

            return key;
        }  catch (IOException e) {
            throw new RuntimeException("error loading API config");
        }
    }*/

    public List<TMDb> searchMovies(String q) {
        try {
            String encodedQ = URLEncoder.encode(q, StandardCharsets.UTF_8);
            String endpoint = String.format("%s/search/movie?api_key=%s&query=%s", URL, apiKey, encodedQ);

            String jsonResponse = makeApiReq(endpoint);
            JsonObject response = gson.fromJson(jsonResponse, JsonObject.class);
            JsonArray results = response.getAsJsonArray("results");

            List<TMDb> searchList = new ArrayList<>();
            for (JsonElement result : results) {
                JsonObject mJson = result.getAsJsonObject();
                searchList.add(parseSearch(mJson));
            }

            return searchList;
        }  catch (Exception e) {
            throw new RuntimeException("TMDb search failed: " + e.getMessage(), e);
        }
    }

    public movie getDetails(int ID) {
        try {
            String endpoint = String.format("%s/movie/%d?api_key=%s", URL, ID, apiKey);

            String jsonResponse = makeApiReq(endpoint);
            JsonObject response = gson.fromJson(jsonResponse, JsonObject.class);

            // Fetch director from credits endpoint
            String director = getDirector(ID);
            if (director == null || director.isEmpty()) {
                director = "Unknown";
            }

            return parseDetails(response, director);
        } catch (Exception e) {
            throw new RuntimeException("failed to get details from TMDb: " + e.getMessage(), e);
        }
    }

    private String getDirector(int movieID) {
        try {
            String endpoint = String.format("%s/movie/%d/credits?api_key=%s", URL, movieID, apiKey);
            String jsonResponse = makeApiReq(endpoint);
            JsonObject response = gson.fromJson(jsonResponse, JsonObject.class);
            
            JsonArray crew = response.getAsJsonArray("crew");
            for (JsonElement element : crew) {
                JsonObject person = element.getAsJsonObject();
                String job = person.has("job") ? person.get("job").getAsString() : "";
                if ("Director".equals(job)) {
                    return person.get("name").getAsString();
                }
            }
            return null;
        } catch (Exception e) {
            // If credits fetch fails, return null and we'll use "Unknown"
            return null;
        }
    }

    private movie parseDetails(JsonObject json, String director) {
        int tmdbID = json.get("id").getAsInt();
        String title = json.get("title").getAsString();

        LocalDate releaseDate = null;
        if (json.has("release_date") && !json.get("release_date").isJsonNull()) {
            String releaseDateStr = json.get("release_date").getAsString();
            if (releaseDateStr != null && !releaseDateStr.isEmpty()) {
                try {
                    releaseDate = LocalDate.parse(releaseDateStr);
                } catch (Exception e) {
                    // If date parsing fails, leave as null
                }
            }
        }

        String overview = json.has("overview") && !json.get("overview").isJsonNull() 
            ? json.get("overview").getAsString() : null;
        
        // Database constraint requires runtime to be NULL or > 0, but movie model uses int (not Integer)
        // So we default to 1 if runtime is missing or 0
        int runtime = 1; // Default value
        if (json.has("runtime") && !json.get("runtime").isJsonNull()) {
            int apiRuntime = json.get("runtime").getAsInt();
            if (apiRuntime > 0) {
                runtime = apiRuntime;
            }
        }

        return new movie(tmdbID, title, releaseDate, director, runtime, overview);
    }

    private TMDb parseSearch(JsonObject json) {
        int tmdbID = json.get("id").getAsInt();
        String title = json.get("title").getAsString();

        String releaseDate = json.has("release_date") &&
                !json.get("release_date").isJsonNull() ? json.get("release_date").getAsString() : null;

        String overview = json.has("overview") &&
                !json.get("overview").isJsonNull() ? json.get("overview").getAsString() : "";

        JsonArray genreID = json.has("genre_ids") ? json.getAsJsonArray("genre_ids") : new JsonArray();

        List<Integer> genres = new ArrayList<>();
        for (JsonElement genre : genreID) {
            genres.add(genre.getAsInt()); // may change back to regular loop struct
        }

        return new TMDb(tmdbID, title, releaseDate, overview, genres);
    }

    private String makeApiReq(String endpoint) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int respCode =  conn.getResponseCode();
        if (respCode != 200) {
            // Try to read error response
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            errorReader.close();
            throw new IOException("HTTP error code: " + respCode + " - " + errorResponse.toString());
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        conn.disconnect();
        return response.toString();
    }

    public class TMDb {
        private int tmdbID;
        private String title;
        private String releaseDate;
        private String overview;
        private List<Integer> genreIDs;

        public TMDb(int tmdbID, String title, String releaseDate, String overview, List<Integer> genres) {
            this.tmdbID = tmdbID;
            this.title = title;
            this.releaseDate = releaseDate;
            this.overview = overview;
            this.genreIDs = genres;
        }

        public int getTmdbID() {
            return tmdbID;
        }
        public String getTitle() {
            return title;
        }
        public String getReleaseDate() {
            return releaseDate;
        }
        public String getOverview() {
            return overview;
        }
        public List<Integer> getGenreIDs() {
            return genreIDs;
        }

        public String getYear() {
            if (releaseDate == null || releaseDate.isEmpty()) {
                return "Unknown";
            }

            return releaseDate.split("-")[0];
        }
    }
}
