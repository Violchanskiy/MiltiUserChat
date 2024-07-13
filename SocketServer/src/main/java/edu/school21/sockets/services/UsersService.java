package edu.school21.sockets.services;

import edu.school21.sockets.models.User;

public interface UsersService {
  String signUp(String email, String password);

  int signIn(String email, String password);

  User findUser(String email);
}
