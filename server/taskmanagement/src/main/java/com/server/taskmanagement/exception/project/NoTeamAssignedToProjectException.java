package com.server.taskmanagement.exception.project;

public class NoTeamAssignedToProjectException extends RuntimeException {
  public NoTeamAssignedToProjectException(String message) {
    super(message);
  }
}
