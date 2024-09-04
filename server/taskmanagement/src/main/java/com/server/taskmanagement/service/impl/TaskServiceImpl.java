package com.server.taskmanagement.service.impl;


import com.server.taskmanagement.entity.Task;
import com.server.taskmanagement.repository.TaskRepository;
import com.server.taskmanagement.service.interfaces.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;

  @Autowired
  public TaskServiceImpl(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

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
}

