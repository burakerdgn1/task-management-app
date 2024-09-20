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

  private final UserServiceImpl userService;


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
  public void deleteTaskById(Long taskId, Long userId) {
    Task task = taskRepository.findById(taskId)
      .orElseThrow(() -> new RuntimeException("Task not found"));

    // Check if the task belongs to a project
    if (task.getProject() != null) {
      // Allow deletion if the user is the project creator
      if (task.getProject().getCreator().getId().equals(userId)) {
        taskRepository.deleteById(taskId);
      } else {
        // throw new UnauthorizedActionException("You are not authorized to delete this project task");
      }
    } else {
      // If the task is a personal task, check if the user is the creator
      if (task.getUser() != null && task.getUser().getId().equals(userId)) {
        taskRepository.deleteById(taskId);
      } else {
        // throw new UnauthorizedActionException("You are not authorized to delete this personal task");
      }
    }
  }

  @Transactional
  public Task createTaskForProject(Long projectId, Task task, Long userId) throws Error{
    if (!isProjectCreator(projectId, userId)) {
      throw new Error("Only the project creator can add tasks");
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
    Task task = taskRepository.findTaskWithProjectAndUserById(taskId)
      .orElseThrow(
        //() -> new TaskNotFoundException("Task not found")
      );
    Project project = task.getProject();
    if (project == null) {
      //throw new ProjectNotFoundException("Task is not associated with any project.");
    }

    if (!isProjectCreator(task.getProject().getId(), assignerId)) {
      //throw new UnauthorizedActionException("Only the project creator can assign tasks");
    }
    if(task.getProject().getTeam()==null){
      //throw new ProjectHasNoTeamException("")
    }

    User user = userService.findUserById(userId)
      .orElseThrow(
        //() -> new UserNotFoundException("User not found")
      );
    if(user.getTasks().contains(task)){
      //Task is already assigned
    }

    if (!teamService.isUserPartOfTeam(userId, task.getProject().getTeam().getId())) {
      //throw new UnauthorizedActionException("User is not part of the project team");
    }

    task.setUser(user);
    task.setTeam(task.getProject().getTeam());
    taskRepository.save(task);
  }

  @Override
  public List<Task> getPersonalTasksForUser(Long userId) {
    return taskRepository.findByUserIdAndProjectIsNull(userId);
  }


  private boolean isProjectCreator(Long projectId, Long userId) {
    return projectRepository.findById(projectId)
      .map(project -> project.getCreator().getId().equals(userId))
      .orElse(false);
  }


}

