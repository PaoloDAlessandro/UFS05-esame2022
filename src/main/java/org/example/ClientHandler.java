package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.sql.ResultSet;
import java.util.ArrayList;
import com.google.gson.Gson;

public class ClientHandler implements Runnable {
    Socket clientSocket;
    BufferedReader in;
    PrintWriter out;
    static ArrayList<Wine> wines = new ArrayList<Wine>();
    DatabaseHandler dbh = null;


    ClientHandler (Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run () {
        this.buildWines();
        this.inizializeClientHandler();
        this.inizializeDatabaseHandler();
        try {
            this.executeClientHandler();
        } catch (SocketException e) {
            System.out.println("error");
        }
    }

    void inizializeClientHandler () {
        try {
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("reader failed" + e);
        }

        out = null;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void executeClientHandler() throws SocketException {
        Gson gson = new Gson();
        String s;
        while (true) {
            s = receive();
            try {
                switch (s) {
                    default:
                        out.println(s + " is not a command");
                        break;
                    case "red":
                        out.println(dbh.selectRedWines());
                        break;

                    case "white":
                        out.println(dbh.selectWhiteWines());
                        break;

                    case "sorted_by_price":
                        sort_by_price();
                        out.println(dbh.sort_by_price());
                        break;

                    case "sorted_by_name":
                        out.println(dbh.sort_by_name());
                        break;
                }

            } catch (NullPointerException e) {
                System.out.println("Client: " + clientSocket.getLocalAddress() + " disconnected from the server");
                break;
            }
        }
    }

    /*
    void inizializeDatabaseHandler() {
        dbh = new DatabaseHandler();
    }

    */


    public String selectRedWines() {
        String result = "[";
        for(int i = 0; i < wines.size(); i++) {
            if(wines.get(i).type.equals("red")) {
                result += "{\"id\":" + wines.get(i).id +
                        ", \"name\":\"" + wines.get(i).name +
                        "\", \"price\":" + wines.get(i).price +
                        ", \"type\":\"" + wines.get(i).type +
                        "\" }, ";
            }
        }

        result = cleanResult(result);

        return result;
    }

    public String selectWhiteWines() {
        String result = "[";
        for(int i = 0; i < wines.size(); i++) {
            if(wines.get(i).type.equals("white")) {
                result += "{\"id\":" + wines.get(i).id +
                        ", \"name\":\"" + wines.get(i).name +
                        "\", \"price\":" + wines.get(i).price +
                        ", \"type\":\"" + wines.get(i).type +
                        "\" }, ";
            }
        }

        result = cleanResult(result);

        return result;
    }

    void inizializeDatabaseHandler() {
        dbh = new DatabaseHandler();
    }

    String receive() {
        String s = "";
        try {
            s = in.readLine();
        } catch (IOException e) {
            //tryReconnect();
        }
        return s;
    }


    public void buildWines() {
        wines.add(new Wine(13,"Dom Perignon Vintage Moet & Chandon 2008",225.94, "white"));
        wines.add(new Wine(14,"Amarone della Valpolicella DOCG",29.70, "red"));
        wines.add(new Wine(14,"Pignoli Radikon Radikon 2009",133.0, "red"));
        wines.add(new Wine(124, "Pinot Nero Elena Walch Elena Walch 2018", 43.0, "red"));
    }

    String cleanResult(String result) {
        return result.substring(0, result.length() - 2) + "]";
    }

    void sort_by_price() {
        wines.sort((o1, o2) -> {
            if (o1.getPrice() < o2.getPrice())
                return 1;
            if (o1.getPrice() > o2.getPrice())
                return -1;
            return 0;
        });
    }

    void sort_by_name() {
        wines.sort((o1, o2) -> {
            return o1.getName().compareTo(o2.getName());
        });
    }

}