package com.server.taskmanagement.service.impl;

import com.server.taskmanagement.dto.ProjectDto;
import com.server.taskmanagement.dto.TaskDto;
import com.server.taskmanagement.entity.Project;
import com.server.taskmanagement.entity.Task;
import com.server.taskmanagement.entity.User;
import com.server.taskmanagement.mappers.TaskMapper;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

  private final ProjectServiceImpl projectService;

  private final TaskRepository taskRepository;

  private final UserServiceImpl userService;

  private final TeamService teamService;

  private final TaskMapper taskMapper;

  @Override
  public TaskDto createTask(TaskDto taskDto) {
    User authenticatedUser = userService.getAuthenticatedUser();
    Task task = taskMapper.toEntity(taskDto);  // Map to entity
    task.setUser(authenticatedUser);
    task.setProject(null);
    Task savedTask = taskRepository.save(task);
    return taskMapper.toDto(savedTask);  // Map to DTO
  }

  @Override
  public Optional<TaskDto> findTaskById(Long id) {
    Optional<Task> task= taskRepository.findById(id);
    if(task.isEmpty()){
      //exception
    }
    return task.map(taskMapper::toDto);
  }

  @Override
  public List<TaskDto> findAllTasks() {
    List<Task> tasks = taskRepository.findAll();
    return tasks.stream()
      .map(taskMapper::toDto)  // Map each task to DTO
      .collect(Collectors.toList());
  }

  @Override
  public TaskDto updateTask(Long id, TaskDto updatedTaskDto) {
    Task updatedTask = taskMapper.toEntity(updatedTaskDto);  // Map to entity
    Task savedTask = taskRepository.save(updatedTask);
    return taskMapper.toDto(savedTask);  // Map to DTO
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
  public TaskDto createTaskForProject(Long projectId, TaskDto taskDto, Long userId) throws Error {
    if (!isProjectCreator(projectId, userId)) {
      throw new Error("Only the project creator can add tasks");
    }

    Task task = taskMapper.toEntity(taskDto);  // Map to entity
    task.setProject(projectService.getProjectEntity(projectId));

    Task savedTask = taskRepository.save(task);
    return taskMapper.toDto(savedTask);  // Map to DTO
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

    User user = userService.findUserById(userId);
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
  public List<TaskDto> getPersonalTasksForUser(Long userId) {
    List<Task> tasks = taskRepository.findByUserIdAndProjectIsNull(userId);
    return tasks.stream()
      .map(taskMapper::toDto)  // Map each task to DTO
      .collect(Collectors.toList());
  }

  private boolean isProjectCreator(Long projectId, Long userId) {
    ProjectDto project = projectService.findProjectById(projectId)
      .orElseThrow(
        //() -> new ProjectNotFoundException("Project not found")
      );
      return project.getOwner().getId().equals(userId);
  }


}

