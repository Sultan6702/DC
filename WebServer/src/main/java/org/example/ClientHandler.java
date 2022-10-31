package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler extends Thread {
  private final Socket clientSocket;

  // Constructor
  public ClientHandler(Socket socket) {
    this.clientSocket = socket;
  }

  public void run() {
    try {
      BufferedReader input = new BufferedReader(
          new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));

      // Get request
      HttpRequest request = HttpRequest.parse(input);

      // Process request
      Processor proc = new Processor(clientSocket, request);
      proc.process();
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (clientSocket != null) {
            clientSocket.close(); 
        } 
      } 
      catch (IOException e) { 
          e.printStackTrace(); 
      } 
      System.out.println("Client has been shutdown!");
    }
  }
}
