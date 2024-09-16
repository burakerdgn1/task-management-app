package com.server.taskmanagement.service.interfaces;

import com.server.taskmanagement.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
  Task createTask(Task task);

  Optional<Task> findTaskById(Long id);

  List<Task> findAllTasks();

  Task updateTask(Long id, Task updatedTask);

  void deleteTaskById(Long taskId, Long userId);

  Task createTaskForProject(Long projectId, Task task, Long userId);

  void assignTaskToUser(Long taskId, Long userId, Long assignerId);

  List<Task> getPersonalTasksForUser(Long userId);
}
