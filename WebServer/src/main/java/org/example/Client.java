package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client<T> extends Thread {
  // private final Socket clientSocket;
    private final int id;
  private final ThreadSafeQueue<T> queue;

  // Constructor
  public Client(int id, ThreadSafeQueue<T> queue) {
    // this.clientSocket = socket;
    this.id = id;
    this.queue = queue; 
  }

  public void run() {
    try {
      while(true){
        T elem = queue.pop();

        Socket clientSocket = (Socket) elem;
  
        BufferedReader input = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
  
        // Get request
        HttpRequest request = HttpRequest.parse(input);
  
        System.out.println("Client "+id + " is running!");
  
        // Process request
        Processor proc = new Processor(clientSocket, request);
        proc.done();
      }
      
    } catch (IOException ex) {
      ex.printStackTrace();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      System.out.println("Client "+id+" has been shutdown!");
    }
  }
}
