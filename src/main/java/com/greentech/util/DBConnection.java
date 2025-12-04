package com.greentech.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/green_tech_db";
    private static final String USER = "root"; 
    private static final String PASS = "your_password"; // <--- UPDATE THIS

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Database Driver not found");
        }
    }
}