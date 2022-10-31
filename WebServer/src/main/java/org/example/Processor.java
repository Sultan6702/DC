package org.example;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * Processor of HTTP request.
 */
public class Processor {
  private final Socket socket;
  private final HttpRequest request;

  public Processor(Socket socket, HttpRequest request) {
    this.socket = socket;
    this.request = request;
  }

  public HttpRequest getRequest(){
    return this.request;
  }

  public void process() throws IOException {
    System.out.println("Got request:");
    System.out.println(request.toString());
    System.out.flush();

    PrintWriter output = new PrintWriter(socket.getOutputStream());

    // We are returning a simple web page now.
    output.println("HTTP/1.1 200 OK");
    output.println("Content-Type: text/html; charset=utf-8");
    output.println();
    output.println("<html>");
    output.println("<head><title>Hello</title></head>");
    output.println("<body><p>Hello, world!</p></body>");
    output.println("</html>");
    output.flush();
    socket.close();
  }

  public void createItem() throws IOException {

    File file = new File("/home/damir/PycharmProjects");
    long size = getFolderSize(file);
    PrintWriter output = new PrintWriter(socket.getOutputStream());

    // We are returning a simple web page now.
    output.println("HTTP/1.1 200 OK");
    output.println("Content-Type: text/html; charset=utf-8");
    output.println();
    output.println("<html>");
    output.println("<head><title>Hello</title></head>");
    output.println("<body><p>"+(double)size / (1024 * 1024) + " MB </p></body>");
    output.println("</html>");
    output.flush();
    socket.close();
  }
  public void deleteItem() throws IOException {

    int x, y, flg;

    PrintWriter output = new PrintWriter(socket.getOutputStream());

    // We are returning a simple web page now.
    output.println("HTTP/1.1 200 OK");
    output.println("Content-Type: text/html; charset=utf-8");
    output.println();
    output.println("<html>");
    output.println("<head><title>Hello</title></head>");
    output.println("<body><p> Prime numbers up to 100000: ");
    for (x = 1; x <= 100000; x++) {

      if (x == 1 || x == 0)
        continue;

      flg = 1;

      for (y = 2; y <= x / 2; ++y) {
        if (x % y == 0) {
          flg = 0;
          break;
        }
      }

      if (flg == 1)
        output.print(x + " ");
    }
    output.print(" </p></body>");
    output.println("</html>");
    output.flush();
    socket.close();
  }
  public void execParams() throws IOException {

    PrintWriter output = new PrintWriter(socket.getOutputStream());
    File file = new File("words.txt");
    FileInputStream fileInputStream = new FileInputStream(file);
    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    String line;
    int wordCount = 0;
    int characterCount = 0;
    int paraCount = 0;
    int whiteSpaceCount = 0;
    int sentenceCount = 0;

    while ((line = bufferedReader.readLine()) != null) {
      if (line.equals("")) {
        paraCount += 1;
      }
      else {
        characterCount += line.length();
        String words[] = line.split("\\s+");
        wordCount += words.length;
        whiteSpaceCount += wordCount - 1;
        String sentence[] = line.split("[!?.:]+");
        sentenceCount += sentence.length;
      }
    }
    if (sentenceCount >= 1) {
      paraCount++;
    }
    // We are returning a simple web page now.
    output.println("HTTP/1.1 200 OK");
    output.println("Content-Type: text/html; charset=utf-8");
    output.println();
    output.println("<html>");
    output.println("<head><title>Words</title></head");
    output.println("<body><p>"+bufferedReader+"<p><body>");
    output.println("<body><p>Total word count = "+wordCount+"<p><body>");
    output.println("<body><p>Total number of sentences = "+ sentenceCount +"<p><body>");
    output.println("<body><p>Total number of characters = "+ characterCount+"<p><body>");
    output.println("<body><p>Number of paragraphs ="+ paraCount+" <p><body>");
    output.println("</html>");
    output.flush();
    socket.close();
  }


  private static long getFolderSize(File folder) {
    long length = 0;

    // listFiles() is used to list the
    // contents of the given folder
    File[] files = folder.listFiles();

    assert files != null;
    int count = files.length;

    // loop for traversing the directory
    for (File file : files) {
      if (file.isFile()) {
        length += file.length();
      } else {
        length += getFolderSize(file);
      }
    }
    return length;
  }

  public void done() {
    switch (this.request.getRequestLine().split(" ")[1]) {
      case "/create/itemid" -> {
        try {
          this.createItem();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      case "/delete/itemid" -> {
        try {
          this.deleteItem();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      case "/exec/params" -> {
        try {
          this.execParams();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      default -> {
        try {
          this.process();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}
