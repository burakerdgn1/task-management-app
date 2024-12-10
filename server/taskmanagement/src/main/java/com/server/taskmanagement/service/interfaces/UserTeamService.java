package com.server.taskmanagement.service.interfaces;

import com.server.taskmanagement.entity.UserTeam;

import java.util.List;
import java.util.Optional;

public interface UserTeamService {
  void addUserToTeam(Long userId, Long teamId);

  Optional<UserTeam> findUserTeamById(Long id);

  List<UserTeam> findAllUserTeams();

  void removeUserFromTeam(Long userToRemoveId, Long teamId);

  Optional<UserTeam> findUserTeamByUserIdAndTeamId(Long userId, Long teamId);

}

