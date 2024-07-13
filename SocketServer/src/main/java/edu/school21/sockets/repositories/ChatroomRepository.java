package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.User;
import java.util.Optional;

public interface ChatroomRepository extends CrudRepository<Chatroom> {
  Optional<Chatroom> findByOwner(User user);
}
