package edu.school21.sockets.client;

import java.io.*;
import java.net.Socket;

public class Client {
  private PrintWriter out;
  private BufferedReader input;
  private BufferedReader stdin;
  private Socket clientSocket;
  private final String ip;

  private final int port;

  public Client(String ip, int port) {
    this.ip = ip;
    this.port = port;
  }

  public void go() {
    try {
      clientSocket = new Socket(ip, port);
      initialization(clientSocket);
      Thread sendThread = sendMessage();
      Thread getThread = getMessage();
      sendThread.start();
      getThread.start();
      sendThread.join();
      getThread.join();
      closeResources();
    } catch (IOException | InterruptedException e) {
      System.out.println(e.getMessage());
    }
  }

  private Thread sendMessage() {
    return new Thread(
        () -> {
          try {
            String msg;
            while (true) {
              if ((msg = stdin.readLine()) != null) {
                checkExit(msg);
                out.println(msg);
              }
              Thread.sleep(100);
            }
          } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
          }
        });
  }

  private Thread getMessage() {
    return new Thread(
        () -> {
          try {
            String answer;
            while (true) {
              answer = input.readLine();
              checkExit(answer);
              System.out.println(answer);
            }
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
  }

  private void checkExit(String text) {
    if (text.equalsIgnoreCase("exit")) {
      System.out.println("You have left the chat.");
      System.exit(-1);
    }
  }

  private void initialization(Socket clientSocket) throws IOException {
    stdin = new BufferedReader(new InputStreamReader(System.in));
    input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    out = new PrintWriter(clientSocket.getOutputStream(), true);
  }

  private void closeResources() {
    try {
      if (out != null) {
        out.close();
      }
      if (input != null) {
        input.close();
      }
      if (stdin != null) {
        stdin.close();
      }
      if (clientSocket != null) {
        clientSocket.close();
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
