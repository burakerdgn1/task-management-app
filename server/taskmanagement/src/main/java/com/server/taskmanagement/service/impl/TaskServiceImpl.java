package com.server.taskmanagement.service.impl;


import com.server.taskmanagement.entity.Project;
import com.server.taskmanagement.entity.Task;
import com.server.taskmanagement.entity.User;
import com.server.taskmanagement.repository.ProjectRepository;
import com.server.taskmanagement.repository.TaskRepository;
import com.server.taskmanagement.repository.UserRepository;
import com.server.taskmanagement.service.interfaces.ProjectService;
import com.server.taskmanagement.service.interfaces.TaskService;
import com.server.taskmanagement.service.interfaces.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

  private final ProjectService projectService;

  private final TaskRepository taskRepository;

  private final ProjectRepository projectRepository;

  private final UserRepository userRepository;

  private final TeamService teamService;

  @Override
  public Task createTask(Task task) {
    return taskRepository.save(task);
  }

  @Override
  public Optional<Task> findTaskById(Long id) {
    return taskRepository.findById(id);
  }

  @Override
  public List<Task> findAllTasks() {
    return taskRepository.findAll();
  }

  @Override
  public Task updateTask(Long id, Task updatedTask) {
    return taskRepository.findById(id)
      .map(existingTask -> {
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        //existingTask.setDeadline(updatedTask.getDeadline());
        //existingTask.setStatus(updatedTask.getStatus());
        // Update other fields as necessary
        return taskRepository.save(existingTask);
      })
      .orElseThrow(
        //() -> new TaskNotFoundException("Task not found with id: " + id)
      );
  }

  @Override
  public void deleteTask(Long id) {
    taskRepository.deleteById(id);
  }

  @Transactional
  public Task createTaskForProject(Long projectId, Task task, Long userId) {
    if (!isProjectCreator(projectId, userId)) {
      //throw new UnauthorizedActionException("Only the project creator can add tasks");
    }
    Project project = projectRepository.findById(projectId)
      .orElseThrow(
        //() -> new ProjectNotFoundException("Project not found")
      );

    task.setProject(project);
    return taskRepository.save(task);
  }

  @Transactional
  public void assignTaskToUser(Long taskId, Long userId, Long assignerId) {
    Task task = taskRepository.findById(taskId)
      .orElseThrow(
        //() -> new TaskNotFoundException("Task not found")
      );

    if (!isProjectCreator(task.getProject().getId(), assignerId)) {
      //throw new UnauthorizedActionException("Only the project creator can assign tasks");
    }

    User user = userRepository.findById(userId)
      .orElseThrow(
        //() -> new UserNotFoundException("User not found")
      );

    if (!teamService.isUserPartOfTeam(userId, task.getProject().getTeam().getId())) {
      //throw new UnauthorizedActionException("User is not part of the project team");
    }

    task.setUser(user);
    taskRepository.save(task);
  }

  private boolean isProjectCreator(Long projectId, Long userId) {
    return projectRepository.findById(projectId)
      .map(project -> project.getCreator().getId().equals(userId))
      .orElse(false);
  }


}

