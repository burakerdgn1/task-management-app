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
    User authenticatedUser = userService.getAuthenticatedUser();

    Task task = new Task();
    task.setTitle(taskDto.getTitle());
    task.setDescription(taskDto.getDescription());

    Task savedTask = taskService.createTaskForProject(projectId, task, authenticatedUser.getId());

    return ResponseEntity.ok(mapTaskToDto(savedTask));
  }

  // Create a personal task (independent from any project)
  @PostMapping("/personal")
  public ResponseEntity<TaskDto> createPersonalTask(@RequestBody TaskDto taskDto) {
    User authenticatedUser = userService.getAuthenticatedUser();

    Task task = new Task();
    task.setTitle(taskDto.getTitle());
    task.setDescription(taskDto.getDescription());
    task.setUser(authenticatedUser);  // Assigning to the authenticated user
    task.setProject(null);  // No project for personal tasks

    Task savedTask = taskService.createTask(task);

    return ResponseEntity.ok(mapTaskToDto(savedTask));
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
    Task task = taskService.findTaskById(taskId)
      .orElseThrow(() -> new RuntimeException("Task not found"));

    return ResponseEntity.ok(mapTaskToDto(task));
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
    List<Task> tasks = taskService.getPersonalTasksForUser(authenticatedUser.getId());

    List<TaskDto> taskDtos = tasks.stream().map(this::mapTaskToDto).collect(Collectors.toList());
    return ResponseEntity.ok(taskDtos);
  }

  // Helper method to map Task to TaskDto
  private TaskDto mapTaskToDto(Task task) {
    TaskDto taskDto = new TaskDto();
    taskDto.setId(task.getId());
    taskDto.setTitle(task.getTitle());
    taskDto.setDescription(task.getDescription());

    if (task.getUser() != null) {
      taskDto.setUserId(task.getUser().getId());
      taskDto.setAssignedUsername(task.getUser().getUsername());
    }

    if (task.getProject() != null) {
      taskDto.setProjectId(task.getProject().getId());
      taskDto.setProjectName(task.getProject().getName());    }

    if (task.getTeam() != null) {
      taskDto.setTeamId(task.getTeam().getId());
      taskDto.setTeamName(task.getTeam().getName());  // Optionally map team name
    }

    return taskDto;
  }
}

