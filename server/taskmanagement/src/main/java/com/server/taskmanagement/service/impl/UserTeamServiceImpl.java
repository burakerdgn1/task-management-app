package com.server.taskmanagement.service.impl;
import com.server.taskmanagement.entity.Team;
import com.server.taskmanagement.entity.User;
import com.server.taskmanagement.entity.UserTeam;
import com.server.taskmanagement.repository.UserTeamRepository;
import com.server.taskmanagement.service.interfaces.TeamService;
import com.server.taskmanagement.service.interfaces.UserService;
import com.server.taskmanagement.service.interfaces.UserTeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserTeamServiceImpl implements UserTeamService {
  private final UserService userService;
  private final TeamService teamService;

  private final UserTeamRepository userTeamRepository;

  @Override
  @Transactional
  public void addUserToTeam(Long userId, Long teamId) {
    User user = userService.findUserById(userId)
      .orElseThrow(
        //() -> new UserNotFoundException("User not found with id: " + userId)
      );
    Team team = teamService.findTeamById(teamId)
      .orElseThrow(
        //() -> new TeamNotFoundException("Team not found with id: " + teamId)
      );

    /*
    if (userTeamRepository.existsByUserAndTeam(user, team)) {
      throw new IllegalStateException("User is already a member of the team");
    }
     */

    UserTeam userTeam = new UserTeam();
    userTeam.setUser(user);
    userTeam.setTeam(team);
    userTeamRepository.save(userTeam);
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

