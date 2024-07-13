package edu.school21.sockets.services;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.MessagesRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("messagesService")
public class MessagesServiceImpl implements MessagesService {
  private final MessagesRepository messagesRepository;

  @Autowired
  public MessagesServiceImpl(
      @Qualifier("messagesRepositoryJdbc") MessagesRepository messagesRepository) {
    this.messagesRepository = messagesRepository;
  }

  @Override
  public List<Message> getNewMessages(Timestamp time) {
    return messagesRepository.findAllAfterTime(time);
  }

  @Override
  public void sendMessage(String messageText, User user, Chatroom chatroom) {
    Message message =
        new Message(1L, user, messageText, Timestamp.valueOf(LocalDateTime.now()), chatroom);
    messagesRepository.save(message);
  }

  @Override
  public List<Message> getLast30Messages(Chatroom chatroom) {
    return messagesRepository.findLast30Messages(chatroom);
  }
}
