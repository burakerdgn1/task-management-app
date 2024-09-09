package com.server.taskmanagement.repository;

import com.server.taskmanagement.entity.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
  List<UserProject> findUserProjectsByUserId(Long userId);
}
