package edu.school21.sockets.server;

import edu.school21.sockets.services.ChatroomService;
import edu.school21.sockets.services.MessagesService;
import edu.school21.sockets.services.UsersService;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("Server")
public class Server {

  private final UsersService usersService;
  private final MessagesService messagesService;
  private final ChatroomService chatroomService;
  private ServerSocket serverSocket;
  private Socket clientSocket;

  public Server(
      @Qualifier("messagesService") MessagesService messagesService,
      @Qualifier("usersService") UsersService usersService,
      @Qualifier("chatroomService") ChatroomService chatroomService) {
    this.messagesService = messagesService;
    this.usersService = usersService;
    this.chatroomService = chatroomService;
  }

  public void go(int port) {
    try {
      serverSocket = new ServerSocket(port);
      while (true) {
        clientSocket = serverSocket.accept();
        ClientHandler clientHandler =
            new ClientHandler(clientSocket, messagesService, usersService, chatroomService);
        Thread clientThread = new Thread(clientHandler);
        clientThread.start();
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    } finally {
      closeResources();
    }
  }

  private void closeResources() {
    try {
      if (serverSocket != null) {
        serverSocket.close();
      }
      if (clientSocket != null) {
        clientSocket.close();
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
