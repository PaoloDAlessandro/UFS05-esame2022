package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    Database() {
        String DB_URL = "jdbc:mysql://localhost/";
        final String USER = "root";
        final String PASSWORD = "";
        String sql;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Statement stmt = null;

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            sql = "CREATE DATABASE wines";
            stmt.executeUpdate(sql);
            System.out.println("Database created successfully...");
        } catch (SQLException e) {
            System.out.println("Database already exists");
        }

        DB_URL = "jdbc:mysql://localhost/wines";

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            sql = "CREATE TABLE `wines_list` ( `id` INT NOT NULL AUTO_INCREMENT , `name` VARCHAR(255) NOT NULL , `price` DOUBLE NOT NULL , `type` VARCHAR(100) NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;";
            stmt.executeUpdate(sql);
            System.out.println("Table created");

            sql = "INSERT INTO `wines_list`(`name`, `price`, `type`) VALUES ('Dom Perignon Vintage Moet & Chandon 2008',225.94,'white'), ('Amarone della Valpolicella DOCG',29.70,'red'), ('Pignoli Radikon Radikon 2009',133.0,'red'), ('Pinot Nero Elena Walch Elena Walch 2018',43.0,'red')";

            stmt.executeUpdate(sql);
            System.out.println("table records added");

        } catch (SQLException e) {
            System.out.println("Table already exists");
        }
    }
}
