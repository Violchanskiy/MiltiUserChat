package edu.school21.sockets.server;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.services.ChatroomService;
import edu.school21.sockets.services.MessagesService;
import edu.school21.sockets.services.UsersService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class ClientHandler implements Runnable {
  private final Socket clientSocket;
  private final MessagesService messagesService;
  private final UsersService usersService;
  private final ChatroomService chatroomService;
  private BufferedReader input;
  private PrintWriter out;
  private Timestamp lastMessageTime;
  private User user;
  private boolean exit;

  public ClientHandler(
      Socket clientSocket,
      MessagesService messagesService,
      UsersService usersService,
      ChatroomService chatroomService) {
    this.clientSocket = clientSocket;
    this.messagesService = messagesService;
    this.usersService = usersService;
    this.chatroomService = chatroomService;
  }

  @Override
  public void run() {
    try {
      input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      sendWelcomeMessage();
      String answer;
      exit = true;
      while (exit && ((answer = input.readLine()) != null)) {
        switch (answer.trim()) {
          case "1":
            goSignIn();
            break;
          case "2":
            goSignUp();
            break;
          case "3":
            sendByeMessage();
            break;
          default:
            out.println("Wrong command, try again");
            break;
        }
      }
      closeResources();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void goSignUp() throws IOException {
    out.println("Enter username:");
    String userName = input.readLine();
    out.println("Enter password:");
    String password = input.readLine();
    signUp(userName, password);
  }

  private void signUp(String userName, String password) {
    String encodePassword = usersService.signUp(userName, password);
    String status = encodePassword == null ? "User already exists" : "Successful!";
    out.println(status);
    sendCommands();
  }

  private void goSignIn() throws IOException {
    out.println("Enter username:");
    String userName = input.readLine();
    out.println("Enter password:");
    String password = input.readLine();
    signIn(userName, password);
  }

  private void signIn(String userName, String password) throws IOException {
    int status = usersService.signIn(userName, password);
    switch (status) {
      case 1:
        out.println("User does not exist");
        sendCommands();
        break;
      case 2:
        out.println("Wrong password");
        sendCommands();
        break;
      case 3:
        user = usersService.findUser(userName);
        getChatrooms();
        break;
    }
  }

  private void getChatrooms() throws IOException {
    out.println("1.Create room\n" + "2.Choose room\n" + "3.Exit");
    String choice = input.readLine();
    switch (choice.trim()) {
      case "1":
        createRoom();
        break;
      case "2":
        chooseRoom();
        break;
      case "3":
        sendByeMessage();
        break;
      default:
        out.println("Wrong command, try again");
        sendCommands();
        break;
    }
  }

  private void createRoom() throws IOException {
    out.println("Enter a name for the chatroom");
    String name = input.readLine();
    chatroomService.createChatroom(name, user.getId());
    out.println("The room was created successfully");
    sendCommands();
  }

  private void chooseRoom() throws IOException {
    List<Chatroom> chatrooms = chatroomService.findAll();
    out.println("Rooms:");
    int num = 1;
    for (Chatroom chatroom : chatrooms) {
      out.println(num + ". " + chatroom.getName());
      num++;
    }
    out.println(num + ". Exit");
    checkRoom(num, chatrooms);
  }

  private void checkRoom(int num, List<Chatroom> chatrooms) throws IOException {
    String answer = input.readLine();
    int roomIndex;
    try {
      roomIndex = Integer.parseInt(answer);
    } catch (NumberFormatException e) {
      roomIndex = -1;
    }
    if (roomIndex == num) {
      sendByeMessage();
    } else if (roomIndex > 0 && roomIndex <= chatrooms.size()) {
      welcomeRoom(chatrooms.get(roomIndex - 1));
    } else {
      out.println("Invalid room choice, try again");
      sendCommands();
    }
  }

  private void welcomeRoom(Chatroom chatroom) throws IOException {
    out.println("Java Room ---");
    printLast30Messages(chatroom);
    chating(chatroom);
  }

  private void printLast30Messages(Chatroom chatroom) {
    List<Message> last30Messages = messagesService.getLast30Messages(chatroom);
    for (Message message : last30Messages) {
      out.println(message.getSender().getEmail() + ": " + message.getMessageText());
    }
  }

  private void chating(Chatroom chatroom) throws IOException {
    String text = input.readLine();
    if (text != null) {
      lastMessageTime = Timestamp.valueOf(LocalDateTime.now());
      Thread allMessagesFromClients = printMessages();
      allMessagesFromClients.start();
      messagesService.sendMessage(text, user, chatroom);
      while ((text = input.readLine()) != null) {
        messagesService.sendMessage(text, user, chatroom);
      }
      allMessagesFromClients.interrupt();
    }
  }

  private Thread printMessages() {
    return new Thread(
        () -> {
          try {
            while (true) {
              List<Message> messages = messagesService.getNewMessages(lastMessageTime);
              for (Message message : messages) {
                out.println(message.getSender().getEmail() + ": " + message.getMessageText());
                lastMessageTime = message.getTime();
              }
              Thread.sleep(1000);
            }
          } catch (InterruptedException e) {
            System.err.println(e.getMessage());
          }
        });
  }

  private void sendWelcomeMessage() {
    out.println("Hello from Server!");
    sendCommands();
  }

  private void sendByeMessage() {
    out.println("exit");
    exit = false;
  }

  private void sendCommands() {
    out.println("1. signIn\n" + "2. SignUp\n" + "3. Exit");
  }

  private void closeResources() {
    try {
      if (out != null) {
        out.close();
      }
      if (input != null) {
        input.close();
      }
      if (clientSocket != null) {
        clientSocket.close();
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
