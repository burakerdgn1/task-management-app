package com.server.taskmanagement.repository;

import com.server.taskmanagement.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
}
