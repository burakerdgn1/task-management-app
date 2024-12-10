package com.server.taskmanagement.repository;

import com.server.taskmanagement.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findByUserIdAndProjectIsNull(Long userId);


  @Query("SELECT t FROM Task t LEFT JOIN FETCH t.project p LEFT JOIN FETCH t.user u WHERE t.id = :taskId")
  Optional<Task> findTaskWithProjectAndUserById(@Param("taskId") Long taskId);
}
