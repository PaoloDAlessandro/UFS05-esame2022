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

    ClientHandler (Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run () {
        this.buildWines();
        this.inizializeClientHandler();
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
                        String result = selectRedWines();
                        out.println(result);
                        break;

                    case "white":
                        //out.println(gson.toJson(cities));
                        break;

                    case "sorted_by_price":
                        break;

                    case "sorted_by_name":
                        //sort_by_name();
                        //out.println(gson.toJson(cities));
                        break;
                }

            } catch (NullPointerException e) {
                System.out.println("Client: " + clientSocket.getLocalAddress() + " disconnected from the server");
                break;
            }

            if (s == "") break;
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
                    "\", \"type\":" + wines.get(i).type +
                    " }, ";
            }
        }
        return result;
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

    private void tryReconnect() {
        try {
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildWines() {
        wines.add(new Wine(13,"Dom Perignon Vintage Moet & Chandon 2008",225.94, "white"));
        wines.add(new Wine(14,"Pignoli Radikon Radikon 2009",133.0, "red"));
        wines.add(new Wine(124, "Pinot Nero Elena Walch Elena Walch 2018", 43.0, "red"));
    }





/*

    void sort_by_temp() {
        cities.sort((o1, o2) -> {
            if (o1.getTemp() < o2.getTemp())
                return 1;
            if (o1.getTemp() > o2.getTemp())
                return -1;
            return 0;
        });
    }

    void sort_by_name() {
        cities.sort((o1, o2) -> {
            return o1.getName().compareTo(o2.getName());
        });
    }
*/

}