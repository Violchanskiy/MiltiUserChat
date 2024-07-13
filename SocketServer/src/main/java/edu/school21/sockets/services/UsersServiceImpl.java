package edu.school21.sockets.services;

import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("usersService")
public class UsersServiceImpl implements UsersService {
  private final UsersRepository usersRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UsersServiceImpl(
      @Qualifier("usersRepositoryJdbc") UsersRepository usersRepository,
      @Qualifier("passwordEncoder") PasswordEncoder passwordEncoder) {
    this.usersRepository = usersRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public String signUp(String email, String password) {
    if (usersRepository.findByEmail(email).isPresent()) {
      return null;
    }
    String encodePassword = passwordEncoder.encode(password);
    User user = new User(null, email, encodePassword);
    usersRepository.save(user);
    return encodePassword;
  }

  @Override
  public User findUser(String email) {
    return usersRepository.findByEmail(email).get();
  }

  @Override
  public int signIn(String email, String password) {
    Optional<User> userOptional = usersRepository.findByEmail(email);
    if (!userOptional.isPresent()) {
      return 1;
    }
    User user = userOptional.get();
    if (!passwordEncoder.matches(password, user.getPassword())) {
      return 2;
    }
    return 3;
  }
}
