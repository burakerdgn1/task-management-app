package com.server.taskmanagement.service.impl;


import com.server.taskmanagement.entity.Project;
import com.server.taskmanagement.entity.Task;
import com.server.taskmanagement.repository.TaskRepository;
import com.server.taskmanagement.service.interfaces.ProjectService;
import com.server.taskmanagement.service.interfaces.TaskService;
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

  @Override
  @Transactional
  public void addTaskToProject(Long taskId, Long projectId) {
    Task task = taskRepository.findById(taskId)
      .orElseThrow(
        //() -> new TaskNotFoundException("Task not found with id: " + taskId)
      );
    Project project = projectService.findProjectById(projectId)
      .orElseThrow(
        //() -> new ProjectNotFoundException("Project not found with id: " + projectId)
      );

    task.setProject(project);
    taskRepository.save(task);
  }
}

