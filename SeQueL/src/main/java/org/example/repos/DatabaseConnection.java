package org.example.repos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private String url = "jdbc:postgresql://localhost:5432/sequel";
    private String username = "postgres";
    private String pass = "testPass123";

    private List<Connection> connPool;
    private List<Connection> usedConns = new ArrayList<>();

    public DatabaseConnection() {
        connPool = new ArrayList<>(5);

        try {
            for (int i = 0; i < 5; i++) connPool.add(DriverManager.getConnection(url, username, pass));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                instance = new DatabaseConnection();
            }
        }

        return instance;
    }

    public synchronized Connection getConn() throws SQLException {
        if (connPool.isEmpty() && usedConns.size() < 10) {
            connPool.add(DriverManager.getConnection(url, username, pass));
        }

        Connection tempC = connPool.remove(connPool.size() - 1);

        if (!tempC.isValid(1)) {
            tempC = DriverManager.getConnection(url, username, pass);
        }
        usedConns.add(tempC);
        return tempC;
    }

    public synchronized void releaseConn(Connection c) {
        if (c != null) {
            usedConns.remove(c);
            connPool.add(c);
        }
    }

    private void closeConn(Connection c) throws SQLException {
        if (c != null && !c.isClosed()) c.close();
    }

    public int getPoolSize() {
        return connPool.size() + usedConns.size();
    }

    public int availConns() {
        return connPool.size();
    }
}
