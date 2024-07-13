package edu.school21.sockets.models;

import java.util.List;
import java.util.Objects;

public class Chatroom {
  private Long id;
  private String name;
  private Long ownerId;
  private List<Message> messages;

  private List<User> users;

  public Chatroom(Long id, String name, Long ownerId) {
    this.id = id;
    this.name = name;
    this.ownerId = ownerId;
  }

  public Chatroom(Long id, String name, Long ownerId, List<Message> messages, List<User> users) {
    this.id = id;
    this.name = name;
    this.ownerId = ownerId;
    this.messages = messages;
    this.users = users;
  }

  public Chatroom() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
  }

  public List<Message> getMessages() {
    return messages;
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Chatroom chatroom = (Chatroom) o;
    return Objects.equals(id, chatroom.id)
        && Objects.equals(name, chatroom.name)
        && Objects.equals(ownerId, chatroom.ownerId)
        && Objects.equals(messages, chatroom.messages)
        && Objects.equals(users, chatroom.users);
  }

  @Override
  public String toString() {
    return "Chatroom{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", ownerId="
        + ownerId
        + ", messages="
        + messages
        + ", users="
        + users
        + '}';
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, ownerId, messages, users);
  }
}
