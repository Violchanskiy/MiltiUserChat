package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("messagesRepositoryJdbc")
public class MessagesRepositoryImpl implements MessagesRepository {

  private final JdbcTemplate jdbcTemplate;
  private final UsersRepository usersRepository;

  @Autowired
  public MessagesRepositoryImpl(
      @Qualifier("springDataSource") DataSource dataSource,
      @Qualifier("usersRepositoryJdbc") UsersRepository usersRepository) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.usersRepository = usersRepository;
  }

  @Override
  public Optional<Message> findById(Long id) {
    String sql = "SELECT * FROM messages WHERE id = ?";
    return jdbcTemplate.query(
        sql,
        new Object[] {id},
        rs -> {
          if (rs.next()) {
            return Optional.of(
                new Message(
                    rs.getLong("id"),
                    usersRepository.findById(rs.getLong("author")).orElse(null),
                    rs.getString("text"),
                    rs.getTimestamp("time")));
          } else {
            return Optional.empty();
          }
        });
  }

  @Override
  public List<Message> findAll() {
    String sql = "SELECT * FROM messages";
    return jdbcTemplate.query(
        sql,
        (rs, rowNum) ->
            new Message(
                rs.getLong("id"),
                usersRepository.findById(rs.getLong("author")).orElse(null),
                rs.getString("text"),
                rs.getTimestamp("time")));
  }

  public void save(Message message) {
    String sql = "INSERT INTO messages (author, room, text, time) VALUES (?, ?, ?, ?)";
    jdbcTemplate.update(
        sql,
        message.getSender().getId(),
        message.getChatroom().getId(),
        message.getMessageText(),
        message.getTime());
  }

  @Override
  public void update(Message message) {
    String sql = "UPDATE messages SET author = ?, room = ?, text = ?, time = ? WHERE id = ?";
    jdbcTemplate.update(
        sql,
        message.getSender().getId(),
        message.getChatroom().getId(),
        message.getMessageText(),
        message.getTime(),
        message.getId());
  }

  @Override
  public void delete(Long id) {
    String sql = "DELETE FROM messages WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  @Override
  public Optional<Message> findByUser(User user) {
    String sql = "SELECT * FROM messages WHERE author = ?";
    return jdbcTemplate.query(
        sql,
        new Object[] {user.getId()},
        rs -> {
          if (rs.next()) {
            return Optional.of(
                new Message(
                    rs.getLong("id"),
                    usersRepository.findById(rs.getLong("author")).orElse(null),
                    rs.getString("text"),
                    rs.getTimestamp("time")));
          } else {
            return Optional.empty();
          }
        });
  }

  @Override
  public List<Message> findAllAfterTime(Timestamp time) {
    String sql = "SELECT * FROM messages WHERE time > ?";
    return jdbcTemplate.query(
        sql,
        new Object[] {time},
        (rs, rowNum) ->
            new Message(
                rs.getLong("id"),
                usersRepository.findById(rs.getLong("author")).orElse(null),
                rs.getString("text"),
                rs.getTimestamp("time")));
  }

  @Override
  public List<Message> findLast30Messages(Chatroom chatroom) {
    String sql = "SELECT * FROM messages WHERE room = ? ORDER BY time LIMIT 30";
    return jdbcTemplate.query(
        sql,
        new Object[] {chatroom.getId()},
        (rs, rowNum) ->
            new Message(
                rs.getLong("id"),
                usersRepository.findById(rs.getLong("author")).orElse(null),
                rs.getString("text"),
                rs.getTimestamp("time"),
                chatroom));
  }
}
