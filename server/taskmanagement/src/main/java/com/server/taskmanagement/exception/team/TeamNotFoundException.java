package com.server.taskmanagement.exception.team;

public class TeamNotFoundException extends RuntimeException {
  public TeamNotFoundException(String message) {
    super(message);
  }
}
