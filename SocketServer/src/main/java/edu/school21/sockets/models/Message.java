package edu.school21.sockets.models;

import java.sql.Timestamp;
import java.util.Objects;

public class Message {
  private Long id;
  private User sender;

  private String messageText;
  private Timestamp time;
  private Chatroom chatroom;

  public Message(Long id, User sender, String messageText, Timestamp time, Chatroom chatroom) {
    this.id = id;
    this.sender = sender;
    this.messageText = messageText;
    this.time = time;
    this.chatroom = chatroom;
  }

  public Message(Long id, User sender, String messageText, Timestamp time) {
    this.id = id;
    this.sender = sender;
    this.messageText = messageText;
    this.time = time;
  }

  public Message() {}

  public Long getId() {
    return id;
  }

  public User getSender() {
    return sender;
  }

  public String getMessageText() {
    return messageText;
  }

  public Timestamp getTime() {
    return time;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setSender(User sender) {
    this.sender = sender;
  }

  public void setMessageText(String messageText) {
    this.messageText = messageText;
  }

  public void setTime(Timestamp time) {
    this.time = time;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Message message = (Message) o;
    return Objects.equals(id, message.id)
        && Objects.equals(sender, message.sender)
        && Objects.equals(messageText, message.messageText)
        && Objects.equals(time, message.time);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sender, messageText, time);
  }

  @Override
  public String toString() {
    return "Message{"
        + "id="
        + id
        + ", sender="
        + sender
        + ", messageText='"
        + messageText
        + '\''
        + ", time="
        + time
        + '}';
  }

  public Chatroom getChatroom() {
    return chatroom;
  }

  public void setChatroom(Chatroom chatroom) {
    this.chatroom = chatroom;
  }
}
