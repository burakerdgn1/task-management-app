package com.server.taskmanagement.exception.task;

public class ProjectHasNoTeamException extends RuntimeException {
  public ProjectHasNoTeamException(String message) {
    super(message);
  }
}
