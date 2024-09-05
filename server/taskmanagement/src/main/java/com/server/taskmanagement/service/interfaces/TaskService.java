package com.server.taskmanagement.service.interfaces;

import com.server.taskmanagement.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
  Task createTask(Task task);

  Optional<Task> findTaskById(Long id);

  List<Task> findAllTasks();

  Task updateTask(Long id, Task updatedTask);

  void deleteTask(Long id);

  void addTaskToProject(Long taskId, Long projectId);
}
