package org.example;

import java.sql.*;

public class DatabaseHandler {
    final String DB_URL = "jdbc:mysql://localhost/wines";
    final String USER = "root";
    final String PASSWORD = "";
    Connection conn;
    Statement stmt = null;

    DatabaseHandler() {

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String sort_by_name() {
        String result = "[";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM wines_list ORDER BY name");
            while(rs.next())
                result += "{\"id\":" + rs.getInt(1) +
                        ", \"name\":\"" + rs.getString(2) +
                        "\", \"price\":" + rs.getDouble(3) +
                        ", \"type\":\"" + rs.getString(4) +
                        "\" }, ";

        } catch(Exception e){
            System.out.println(e);
        }
        result = cleanResult(result);

        return result;
    }

    String selectAll() {
        String result = "[";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM cities");
            while(rs.next())
                result += "{\"id\":" + rs.getInt(1) +
                        ", \"name\":\"" + rs.getString(2) +
                        "\", \"temp\":" + rs.getDouble(3) +
                        " }, ";

        }catch(Exception e){
            System.out.println(e);
        }
        result = cleanResult(result);

        return result;
    }

    String sort_by_price() {
        String result = "[";
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM wines_list ORDER BY price DESC");
            while(rs.next())
                result += "{\"id\":" + rs.getInt(1) +
                        ", \"name\":\"" + rs.getString(2) +
                        "\", \"price\":" + rs.getDouble(3) +
                        ", \"type\":\"" + rs.getString(4) +
                        "\" }, ";
        }catch(Exception e){
            System.out.println(e);
        }
        result = cleanResult(result);
        return result;
    }

    String selectRedWines() {
        String result = "[";
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM wines_list WHERE type = 'red'");
            while(rs.next())
                result += "{\"id\":" + rs.getInt(1) +
                        ", \"name\":\"" + rs.getString(2) +
                        "\", \"price\":" + rs.getDouble(3) +
                        ", \"type\":\"" + rs.getString(4) +
                        "\" }, ";
        }catch(Exception e){
            System.out.println(e);
        }
        result = cleanResult(result);
        return result;
    }

    String selectWhiteWines() {
        String result = "[";
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM wines_list WHERE type = 'white'");
            while(rs.next())
                result += "{\"id\":" + rs.getInt(1) +
                        ", \"name\":\"" + rs.getString(2) +
                        "\", \"price\":" + rs.getDouble(3) +
                        ", \"type\":\"" + rs.getString(4) +
                        "\" }, ";
        }catch(Exception e){
            System.out.println(e);
        }
        result = cleanResult(result);
        return result;
    }

    void insertCity(String name, Double temp) {
        try {
            stmt = conn.createStatement();
            String sql = "INSERT INTO cities (name, temp) VALUES('" +  name + "', " +  temp + ");";
            System.out.println(sql);
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String searchCity(String name) {

        String result = "";
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM cities WHERE name = '" + name + "';";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            result += "{\"id\":" + rs.getInt(1) +
                    ", \"name\":\"" + rs.getString(2) +
                    "\", \"temp\":" + rs.getDouble(3) +
                    " }, ";

        } catch (SQLException e) {
            result = "ERROR: " + name + " is not present in the database";
        }

        return result;
    }

    String hottest() {

        String result = "";

        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM cities ORDER BY temp DESC LIMIT 1";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            result += "{\"id\":" + rs.getInt(1) +
                    ", \"name\":\"" + rs.getString(2) +
                    "\", \"temp\":" + rs.getDouble(3) +
                    " }, ";
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    void removeCity(String name) {

        try {
            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM cities WHERE name = '" + name +  "';";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    String cleanResult(String result) {
        return result.substring(0, result.length() - 2) + "]";
    }

}
