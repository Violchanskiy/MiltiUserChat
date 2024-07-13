package edu.school21.sockets.app;

import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.server.Server;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
  public static void main(String[] args) {
    ApplicationContext context =
        new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
    Server server = context.getBean("Server", Server.class);
    try {
      int port = parseArgs(args);
      server.go(port);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  private static int parseArgs(String[] args) {
    if (args.length != 1 || !args[0].startsWith("--port=")) {
      throw new IllegalArgumentException("Error\n" + "Usage: Server.jar --port=[PORT]");
    }
    String value = args[0].substring("--port=".length());
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
