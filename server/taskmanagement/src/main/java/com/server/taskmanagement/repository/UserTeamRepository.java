package com.server.taskmanagement.repository;

import com.server.taskmanagement.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
  List<UserTeam> findUserTeamsByUserId(Long userId);
}
