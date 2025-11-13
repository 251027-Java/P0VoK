package org.example.models;

import java.util.Date;

public class user {

    // fields
    private int userID;
    private String username;
    private Date date;

    // default con
    public user() {}

    // overload con
    public user(int userID, String username, Date date) {
        this.userID = userID;
        this.username = username;
        this.date = date;
    }

    // methods
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    @Override
    public String toString() {
        return "User: " + "userID : " + userID +
                "\nusername : " + username +
                "\ndate : " + date;
    }
}
