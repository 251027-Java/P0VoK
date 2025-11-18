package org.example.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class user {

    // fields
    private Integer userID;
    private String username;
    private LocalDateTime date;

    // default con
    public user() {}

    // overload con
    public user(Integer userID, String username, LocalDateTime date) {
        this.userID = userID;
        this.username = username;
        this.date = date;
    }

    // methods
    public Integer getUserID() {
        return userID;
    }
    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "User: " + "userID : " + userID +
                "\nusername : " + username +
                "\ndate : " + date;
    }
}
