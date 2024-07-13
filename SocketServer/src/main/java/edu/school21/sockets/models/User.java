package edu.school21.sockets.models;

import java.util.Objects;

public class User {
  private Long id;
  private String email;
  private String password;

  public User(Long id, String email, String password) {
    this.id = id;
    this.email = email;
    this.password = password;
  }

  public User() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    User user = (User) obj;
    return Objects.equals(id, user.id)
        && Objects.equals(email, user.email)
        && Objects.equals(password, user.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, password);
  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + ", email=" + email + ", password=" + password + '}';
  }
}
