package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Simple web server.
 */
public class WebServerpool {

  public static void main(String[] args) {
    // Port number for http request
    int port = args.length > 1 ? Integer.parseInt(args[1]) : 9981;
    // int numOfThreads = args.length > 2 ? Integer.parseInt(args[2]) : 4;
    // The maximum queue length for incoming connection
    int queueLength = args.length > 2 ? Integer.parseInt(args[2]) : 50;
    
    int numOfThreads = (args.length > 1 ? Integer.parseInt(args[1]) : 15);
    System.out.println(numOfThreads);
    int numOfItems = (args.length > 2 ? Integer.parseInt(args[2]) : 100);
    ThreadSafeQueue<Socket> queue = new ThreadSafeQueue<>();

    try (ServerSocket serverSocket = new ServerSocket(port, queueLength)) {
      System.out.println("Web Server is starting up, listening at port " + port + ".");
      System.out.println("You can access http://localhost:" + port + " now.");

      for (int i = 0; i < numOfThreads; i++) {
        Client<Socket> cons = new Client<Socket>(i, queue);
        cons.start();
      }
      while (true) {
        // Make the server socket wait for the next client request
        Socket socket = serverSocket.accept();
        System.out.println("Got connection!");

        queue.add(socket);
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      for (int i = 0; i < numOfThreads; i++) {
        queue.add(null);
      }
      System.out.println("Server has been shutdown!");
    }
  }
}
