package org.example.repos;

import java.sql.*;

public class SQLInit {

    public SQLInit() {
    }

    private void createUserTable() {
        try (Statement stmt = connection.createStatement()) {
            String sql = """
                    CREATE TABLE IF NOT EXIST users (
                        user_id INTEGER PRIMARY KEY,
                        username VARCHAR(50) NOT NULL,
                        created_at TIMESTAMP CURRENT_TIMESTAMP
                    )
                    """;

            stmt.execute(sql);
            System.out.println("User table created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
