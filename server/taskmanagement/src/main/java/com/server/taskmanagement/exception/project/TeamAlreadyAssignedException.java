package com.server.taskmanagement.exception.project;

public class TeamAlreadyAssignedException extends RuntimeException {
  public TeamAlreadyAssignedException(String message) {
    super(message);
  }
}
