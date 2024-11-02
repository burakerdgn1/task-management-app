package com.server.taskmanagement.controller;
import com.server.taskmanagement.dto.TaskDto;
import com.server.taskmanagement.entity.Task;
import com.server.taskmanagement.entity.User;
import com.server.taskmanagement.service.impl.TaskServiceImpl;
import com.server.taskmanagement.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private TaskServiceImpl taskService;

  @Autowired
  @Qualifier("userServiceImpl")
  private UserServiceImpl userService;

  // Create task for a project and assign to a team member
  @PostMapping("/projects/{projectId}")
  public ResponseEntity<TaskDto> createTaskForProject(@PathVariable Long projectId, @RequestBody TaskDto taskDto) {
    Long authenticatedUserId = userService.getAuthenticatedUser().getId();
    TaskDto savedTask = taskService.createTaskForProject(projectId, taskDto, authenticatedUserId);
    return ResponseEntity.ok(savedTask);
  }

  // Create a personal task (independent from any project)
  @PostMapping("/personal")
  public ResponseEntity<TaskDto> createPersonalTask(@RequestBody TaskDto taskDto) {
    TaskDto savedTask = taskService.createTask(taskDto);
    return ResponseEntity.ok(savedTask);
  }

  // Assign task to a user within the project's team
  @PostMapping("/{taskId}/assign/{userId}")
  public ResponseEntity<Void> assignTaskToUser(@PathVariable Long taskId, @PathVariable Long userId) {
    User authenticatedUser = userService.getAuthenticatedUser();

    taskService.assignTaskToUser(taskId, userId, authenticatedUser.getId());

    return ResponseEntity.ok().build();
  }

  // Get task by ID
  @GetMapping("/{taskId}")
  public ResponseEntity<TaskDto> getTask(@PathVariable Long taskId) {
    Optional<TaskDto> task = taskService.findTaskById(taskId);

    return ResponseEntity.ok(task.get());
  }

  @DeleteMapping("/{taskId}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
    User authenticatedUser = userService.getAuthenticatedUser();

    taskService.deleteTaskById(taskId, authenticatedUser.getId());

    return ResponseEntity.noContent().build(); // HTTP 204 No Content
  }


  // Get all tasks for authenticated user
  @GetMapping("/personal")
  public ResponseEntity<List<TaskDto>> getUserPersonalTasks() {
    User authenticatedUser = userService.getAuthenticatedUser();
    List<TaskDto> tasks = taskService.getPersonalTasksForUser(authenticatedUser.getId());
    return ResponseEntity.ok(tasks);
  }


}

