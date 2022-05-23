package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    static int portNumer = 8000;
    static Socket clientSocket = null;

    public static void main(String[] args) {
        initHTTPServer();
        ServerSocket serverSocket = openToServer();
        System.out.println("Server socket started at port n: " + serverSocket.getLocalPort());
        Database db = new Database();
        System.out.println("Database schema inizialized");


        while (true) {
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("failed to accept client request connection" + e);
            }

            System.out.println(clientSocket.getLocalAddress());

            ClientHandler clientHandler = new ClientHandler(clientSocket);

            new Thread(clientHandler).start();
        }

    }

    static private ServerSocket openToServer() {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(portNumer);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return serverSocket;
    }

    static void initHTTPServer() {
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //server.createContext("/applications/myapp", new MyHandler());

        server.createContext("/", new ClientHandlerHTTP());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}