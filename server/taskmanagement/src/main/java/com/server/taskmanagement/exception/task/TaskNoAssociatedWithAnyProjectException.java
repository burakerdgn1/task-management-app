package com.server.taskmanagement.exception.task;

public class TaskNoAssociatedWithAnyProjectException extends RuntimeException {
  public TaskNoAssociatedWithAnyProjectException(String message) {
    super(message);
  }
}
