package edu.school21.sockets.services;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.repositories.ChatroomRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("chatroomService")
public class ChatroomServiceImpl implements ChatroomService {

  private final ChatroomRepository chatroomRepository;

  @Autowired
  public ChatroomServiceImpl(
      @Qualifier("сhatroomRepositoryyJdbc") ChatroomRepository chatroomRepository) {
    this.chatroomRepository = chatroomRepository;
  }

  @Override
  public void createChatroom(String name, Long ownerID) {
    Chatroom chatroom = new Chatroom(null, name, ownerID, new ArrayList<>(), new ArrayList<>());
    chatroomRepository.save(chatroom);
  }

  @Override
  public List<Chatroom> findAll() {
    return chatroomRepository.findAll();
  }
}
