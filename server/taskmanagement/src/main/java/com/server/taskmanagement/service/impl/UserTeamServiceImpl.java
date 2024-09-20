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
import java.util.stream.Collectors;

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

    if (!isTeamCreator(team, team.getCreator().getId())) {
      //throw new UnauthorizedActionException("Only the team creator can add users to the team");
    }

    if (userTeamRepository.existsByUserAndTeam(user, team)) {
      //throw new IllegalStateException("User is already a member of the team");
    }

    UserTeam userTeam = new UserTeam();
    userTeam.setUser(user);
    userTeam.setTeam(team);
    userTeamRepository.save(userTeam);
  }

  public List<Team> getUserTeamsByUserId(Long userId) {
    return userTeamRepository.findUserTeamsByUserId(userId)
      .stream()
      .map(UserTeam::getTeam) // Accesses the Project entity
      .collect(Collectors.toList());
  }

  @Override
  public Optional<UserTeam> findUserTeamById(Long id) {
    return userTeamRepository.findById(id);
  }

  @Override
  public List<UserTeam> findAllUserTeams() {
    return userTeamRepository.findAll();
  }

  // Method to find UserTeam relationship
  private Optional<UserTeam> findUserTeam(User user, Team team) {
    return userTeamRepository.findByUserAndTeam(user, team);
  }

  public boolean isUserInTeam(Long userId, Long teamId) {
    return userTeamRepository.findUserTeamByUserIdAndTeamId(userId, teamId).isPresent();
  }

  @Override
  @Transactional
  public void removeUserFromTeam(Long userId, Long teamId, Long creatorId) {
    User user = userService.findUserById(userId)
      .orElseThrow(
        //() -> new UserNotFoundException("User not found with id: " + userId)
      );

    Team team = teamService.findTeamById(teamId)
      .orElseThrow(
        //() -> new TeamNotFoundException("Team not found with id: " + teamId)
      );

    if (!isTeamCreator(team, creatorId)) {
      //throw new UnauthorizedActionException("Only the team creator can remove users from the team");
    }
    UserTeam userTeam = userTeamRepository.findByUserAndTeam(user, team)
      .orElseThrow(
        () -> new IllegalStateException("User is not a member of this team")
      );
    // Additional check: Do not allow the creator to remove themselves, if needed
    if (user.getId().equals(creatorId)) {
      throw new IllegalStateException("Team creator cannot be removed from the team");
    }

    userTeamRepository.delete(userTeam);
  }

  private boolean isTeamCreator(Team team, Long creatorId) {
    return team.getCreator().getId().equals(creatorId);
  }
}

