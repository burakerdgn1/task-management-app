package com.server.taskmanagement.service.interfaces;

import com.server.taskmanagement.entity.UserTeam;

import java.util.List;
import java.util.Optional;

public interface UserTeamService {
  UserTeam addUserToTeam(UserTeam userTeam);

  Optional<UserTeam> findUserTeamById(Long id);

  List<UserTeam> findAllUserTeams();

  void removeUserFromTeam(Long id);
}

