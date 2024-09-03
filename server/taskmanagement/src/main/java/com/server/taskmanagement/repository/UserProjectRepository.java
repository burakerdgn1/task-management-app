package com.server.taskmanagement.repository;

import com.server.taskmanagement.entity.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
}
