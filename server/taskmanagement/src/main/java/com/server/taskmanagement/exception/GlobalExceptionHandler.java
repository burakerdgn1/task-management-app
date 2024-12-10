package com.server.taskmanagement.exception;

import com.server.taskmanagement.exception.auth.UnauthorizedActionException;
import com.server.taskmanagement.exception.project.NoTeamAssignedToProjectException;
import com.server.taskmanagement.exception.project.ProjectNotFoundException;
import com.server.taskmanagement.exception.project.TeamAlreadyAssignedException;
import com.server.taskmanagement.exception.task.*;
import com.server.taskmanagement.exception.team.TeamNotFoundException;
import com.server.taskmanagement.exception.user.UserNotFoundException;
import com.server.taskmanagement.exception.userteam.TeamCreatorRemoveHimselfException;
import com.server.taskmanagement.exception.userteam.UserAlreadyMemberOfTeamException;
import com.server.taskmanagement.exception.userteam.UserNotMemberOfTeamException;
import com.server.taskmanagement.exception.userteam.UserTeamNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(UnauthorizedActionException.class)
  public ResponseEntity<String> handleUnauthorizedActionException(UnauthorizedActionException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(NoTeamAssignedToProjectException.class)
  public ResponseEntity<String> handleNoTeamAssignedToProjectException(NoTeamAssignedToProjectException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ProjectNotFoundException.class)
  public ResponseEntity<String> handleProjectNotFoundException(ProjectNotFoundException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(TeamAlreadyAssignedException.class)
  public ResponseEntity<String> handleTeamAlreadyAssignedException(TeamAlreadyAssignedException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ProjectHasNoTeamException.class)
  public ResponseEntity<String> handleProjectHasNoTeamException(ProjectHasNoTeamException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(TaskAlreadyAssignedException.class)
  public ResponseEntity<String> handleTaskAlreadyAssignedException(TaskAlreadyAssignedException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(TaskNoAssociatedWithAnyProjectException.class)
  public ResponseEntity<String> handleTaskNoAssociatedWithAnyProjectException(TaskNoAssociatedWithAnyProjectException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(TaskNotFoundException.class)
  public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserNotPartOfTaskProjectTeamException.class)
  public ResponseEntity<String> handleUserNotPartOfTaskProjectTeamException(UserNotPartOfTaskProjectTeamException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(TeamNotFoundException.class)
  public ResponseEntity<String> handleTeamNotFoundException(TeamNotFoundException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(TeamCreatorRemoveHimselfException.class)
  public ResponseEntity<String> handleTeamCreatorRemoveHimselfException(TeamCreatorRemoveHimselfException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(UserAlreadyMemberOfTeamException.class)
  public ResponseEntity<String> handleUserAlreadyMemberOfTeamException(UserAlreadyMemberOfTeamException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UserNotMemberOfTeamException.class)
  public ResponseEntity<String> handleUserNotMemberOfTeamException(UserNotMemberOfTeamException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UserTeamNotFoundException.class)
  public ResponseEntity<String> handleUserTeamNotFoundException(UserTeamNotFoundException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGeneralException(Exception ex) {
    return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
