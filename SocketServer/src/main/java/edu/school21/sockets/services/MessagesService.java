package edu.school21.sockets.services;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import java.sql.Timestamp;
import java.util.List;

public interface MessagesService {
  void sendMessage(String messageText, User user, Chatroom chatroom);

  List<Message> getNewMessages(Timestamp time);

  List<Message> getLast30Messages(Chatroom chatroom);
}
