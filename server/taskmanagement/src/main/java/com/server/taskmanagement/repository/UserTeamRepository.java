package com.server.taskmanagement.repository;

import com.server.taskmanagement.entity.Team;
import com.server.taskmanagement.entity.User;
import com.server.taskmanagement.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
  Optional<UserTeam> findUserTeamByUserIdAndTeamId(Long userId, Long teamId);
  List<UserTeam> findUserTeamsByUserId(Long userId);
  boolean existsByUserAndTeam(User user, Team team);
  Optional<UserTeam> findByUserAndTeam(User user, Team team);

}
