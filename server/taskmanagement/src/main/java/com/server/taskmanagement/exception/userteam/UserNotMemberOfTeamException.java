package com.server.taskmanagement.exception.userteam;

public class UserNotMemberOfTeamException extends RuntimeException {
  public UserNotMemberOfTeamException(String message) {
    super(message);
  }
}
