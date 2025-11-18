package org.example.repos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private String url = "temp";
    private String username = "temp";
    private String pass = "temp";

    private List<Connection> connPool;

    private DatabaseConnection() {
        connPool = new ArrayList<>(5);

        try {
            for (int i = 0; i < 5; i++) connPool.add(DriverManager.getConnection(url, username, pass));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
