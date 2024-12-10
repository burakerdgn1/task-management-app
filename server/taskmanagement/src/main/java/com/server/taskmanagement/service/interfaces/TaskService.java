package com.server.taskmanagement.service.interfaces;

import com.server.taskmanagement.dto.TaskDto;
import com.server.taskmanagement.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
  TaskDto createPersonalTask(TaskDto task);

  Optional<TaskDto> findTaskById(Long id);

  List<TaskDto> findAllTasks();

  TaskDto updateTask(Long id, TaskDto updatedTaskDto);

  void deleteTaskById(Long taskId);

  TaskDto createTaskForProject(Long projectId, TaskDto taskDto, Long userId);

  void assignTaskToUser(Long taskId, Long userId);

  List<TaskDto> getPersonalTasksForUser(Long userId);
}
