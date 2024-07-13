package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.User;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("сhatroomRepositoryyJdbc")
public class ChatroomRepositoryImpl implements ChatroomRepository {
  private final JdbcTemplate jdbcTemplate;
  private final UsersRepository usersRepository;
  private final MessagesRepository messagesRepository;

  @Autowired
  public ChatroomRepositoryImpl(
      @Qualifier("springDataSource") DataSource dataSource,
      @Qualifier("usersRepositoryJdbc") UsersRepository usersRepository,
      @Qualifier("messagesRepositoryJdbc") MessagesRepository messagesRepository) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.usersRepository = usersRepository;
    this.messagesRepository = messagesRepository;
  }

  @Override
  public Optional<Chatroom> findByOwner(User user) {
    String sql = "SELECT * FROM chatrooms WHERE ownerID = ?";
    Chatroom chatroom =
        jdbcTemplate.query(
            sql,
            new Object[] {user.getId()},
            rs -> {
              if (rs.next()) {
                return new Chatroom(rs.getLong("id"), rs.getString("name"), rs.getLong("ownerID"));
              }
              return null;
            });
    return Optional.ofNullable(chatroom);
  }

  public Optional<Chatroom> findById(Long id) {
    String sql = "SELECT * FROM chatrooms WHERE id = ?";
    Chatroom chatroom =
        jdbcTemplate.query(
            sql,
            new Object[] {id},
            rs -> {
              if (rs.next()) {
                return new Chatroom(rs.getLong("id"), rs.getString("name"), rs.getLong("ownerID"));
              }
              return null;
            });
    return Optional.ofNullable(chatroom);
  }

  @Override
  public List<Chatroom> findAll() {
    String sql = "SELECT * FROM chatrooms";
    return jdbcTemplate.query(
        sql,
        (rs, rowNum) ->
            new Chatroom(rs.getLong("id"), rs.getString("name"), rs.getLong("ownerID")));
  }

  @Override
  public void save(Chatroom chatroom) {
    String sql = "INSERT INTO chatrooms (name, ownerID) VALUES (?, ?)";
    jdbcTemplate.update(sql, chatroom.getName(), chatroom.getOwnerId());
  }

  @Override
  public void update(Chatroom chatroom) {
    String sql = "UPDATE chatrooms SET name = ?, ownerID = ? WHERE id = ?";
    jdbcTemplate.update(sql, chatroom.getName(), chatroom.getOwnerId(), chatroom.getId());
  }

  @Override
  public void delete(Long id) {
    String sql = "DELETE FROM chatrooms WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }
}
