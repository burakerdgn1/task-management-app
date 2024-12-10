package com.server.taskmanagement.exception.task;

public class TaskAlreadyAssignedException extends RuntimeException {
  public TaskAlreadyAssignedException(String message) {
    super(message);
  }
}
