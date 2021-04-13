package com.timsanalytics.crc.standalone;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnectMySQL {
    public static void main(String[] args) {
        Connection conn = null;

        try {
            String userName = "admin";
            String password = "xxxx";
            String url = "jdbc:mysql://tqp-database-1.c8rhlg60ppo7.us-east-1.rds.amazonaws.com:3306/AUTH";
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            conn = DriverManager.getConnection(url, userName, password);
            System.out.println("Database connection established");
        } catch (Exception e) {
            System.err.println("Cannot connect to database server");
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database Connection Terminated");
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                    e.printStackTrace();
                }
            }
        }
    }
}
