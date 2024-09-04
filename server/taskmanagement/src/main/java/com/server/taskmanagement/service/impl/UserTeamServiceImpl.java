package com.server.taskmanagement.service.impl;
import com.server.taskmanagement.entity.UserTeam;
import com.server.taskmanagement.repository.UserTeamRepository;
import com.server.taskmanagement.service.interfaces.UserTeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserTeamServiceImpl implements UserTeamService {

  private final UserTeamRepository userTeamRepository;

  @Override
  public UserTeam addUserToTeam(UserTeam userTeam) {
    return userTeamRepository.save(userTeam);
  }

  @Override
  public Optional<UserTeam> findUserTeamById(Long id) {
    return userTeamRepository.findById(id);
  }

  @Override
  public List<UserTeam> findAllUserTeams() {
    return userTeamRepository.findAll();
  }

  @Override
  public void removeUserFromTeam(Long id) {
    userTeamRepository.deleteById(id);
  }
}

