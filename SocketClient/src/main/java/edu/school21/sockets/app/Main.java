package edu.school21.sockets.app;

import edu.school21.sockets.client.Client;

public class Main {
  public static void main(String[] args) {
    String ip = "localhost";
    try {
      int port = parseArgs(args);
      new Client(ip, port).go();
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  private static int parseArgs(String[] args) {
    if (args.length != 1 || !args[0].startsWith("--server-port=")) {
      throw new IllegalArgumentException(
          "Error\n" + "Usage: ServerSocket.jar --server-port=[PORT]");
    }
    String value = args[0].substring("--server-port=".length());
    try {
      int port = Integer.parseInt(value);
      if (port < 0 || port > 65535) {
        throw new IllegalArgumentException("Port number out of range");
      }
      return port;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid port number");
    }
  }
}
