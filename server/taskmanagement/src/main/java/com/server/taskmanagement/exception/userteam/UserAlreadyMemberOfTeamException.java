package com.server.taskmanagement.exception.userteam;

public class UserAlreadyMemberOfTeamException extends RuntimeException {
  public UserAlreadyMemberOfTeamException(String message) {
    super(message);
  }
}
