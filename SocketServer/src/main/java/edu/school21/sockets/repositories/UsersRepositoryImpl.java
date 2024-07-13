package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component("usersRepositoryJdbc")
public class UsersRepositoryImpl implements UsersRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public UsersRepositoryImpl(@Qualifier("springDataSource") DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public Optional<User> findById(Long id) {
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT * FROM users WHERE id = ?", new BeanPropertyRowMapper<>(User.class), id));
  }

  @Override
  public List<User> findAll() {
    return jdbcTemplate.query("SELECT * FROM users", new BeanPropertyRowMapper<>(User.class));
  }

  @Override
  public void save(User entity) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(
        connection -> {
          PreparedStatement statement =
              connection.prepareStatement(
                  "INSERT INTO users (email,password) VALUES (?,?)", new String[] {"id"});
          statement.setString(1, entity.getEmail());
          statement.setString(2, entity.getPassword());
          return statement;
        },
        keyHolder);
    entity.setId(keyHolder.getKey().longValue());
  }

  @Override
  public void update(User entity) {
    jdbcTemplate.update(
        "UPDATE users SET email = ? WHERE id = ?", entity.getEmail(), entity.getId());
  }

  @Override
  public void delete(Long id) {
    jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    List<User> users =
        jdbcTemplate.query(
            "SELECT * FROM users WHERE email = ?", new BeanPropertyRowMapper<>(User.class), email);

    if (users.isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.of(users.get(0));
    }
  }
}
