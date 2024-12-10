package com.server.taskmanagement.exception.task;

public class UserNotPartOfTaskProjectTeamException extends RuntimeException {
  public UserNotPartOfTaskProjectTeamException(String message) {
    super(message);
  }
}
