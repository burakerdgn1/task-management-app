package com.server.taskmanagement.service.impl;
import com.server.taskmanagement.dto.TeamDto;
import com.server.taskmanagement.entity.Team;
import com.server.taskmanagement.entity.User;
import com.server.taskmanagement.entity.UserTeam;
import com.server.taskmanagement.exception.auth.UnauthorizedActionException;
import com.server.taskmanagement.exception.team.TeamNotFoundException;
import com.server.taskmanagement.exception.userteam.TeamCreatorRemoveHimselfException;
import com.server.taskmanagement.exception.userteam.UserAlreadyMemberOfTeamException;
import com.server.taskmanagement.exception.userteam.UserNotMemberOfTeamException;
import com.server.taskmanagement.exception.userteam.UserTeamNotFoundException;
import com.server.taskmanagement.mappers.TeamMapper;
import com.server.taskmanagement.repository.UserTeamRepository;
import com.server.taskmanagement.service.interfaces.TeamService;
import com.server.taskmanagement.service.interfaces.UserService;
import com.server.taskmanagement.service.interfaces.UserTeamService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserTeamServiceImpl implements UserTeamService {
  private final UserServiceImpl userService;
  private final TeamServiceImpl teamService;
  private final UserTeamRepository userTeamRepository;
  private final TeamMapper teamMapper;

  @Override
  @Transactional
  public void addUserToTeam(Long userId, Long teamId) {

    User authenticatedUser = userService.getAuthenticatedUser();
    TeamDto teamDto = teamService.findTeamById(teamId);

    if (!teamDto.getOwner().getId().equals(authenticatedUser.getId())) {
      throw new UnauthorizedActionException("Only team owner can add users");
    }

    User user = userService.findUserById(userId);

    Team team= teamMapper.toEntity(teamDto);

    if (userTeamRepository.existsByUserAndTeam(user, team)) {
      throw new UserAlreadyMemberOfTeamException("User is already a member of the team");
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
    UserTeam userTeam=userTeamRepository.findUserTeamById(id).orElseThrow(
      () -> new UserTeamNotFoundException("UserTeam with id " + id + " not found")
    );
    return Optional.of(userTeam);
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
  public void removeUserFromTeam(Long userToRemoveId, Long teamId) {
    TeamDto teamDto = teamService.findTeamById(teamId);

    User authenticatedUser = userService.getAuthenticatedUser();
    User userToRemove = userService.findUserById(userToRemoveId);

    Team team = teamMapper.toEntity(teamDto);

    if (!isTeamCreator(team, authenticatedUser.getId())) {
      throw new UnauthorizedActionException("Only the team creator can remove users from the team");
    }
    UserTeam userTeam = userTeamRepository.findByUserAndTeam(userToRemove, team)
      .orElseThrow(
        () -> new UserNotMemberOfTeamException("User is not a member of this team")
      );
    // Additional check: Do not allow the creator to remove themselves, if needed
    if (userToRemove.getId().equals(authenticatedUser.getId())) {
      throw new TeamCreatorRemoveHimselfException("Team creator cannot be removed from the team");
    }

    userTeamRepository.delete(userTeam);

  }

  @Override
  public Optional<UserTeam> findUserTeamByUserIdAndTeamId(Long userId, Long teamId) {
    // Check if the user exists
    userService.findUserById(userId);
    // Check if the team exists
    teamService.findTeamById(teamId);

    UserTeam userTeam = userTeamRepository.findUserTeamByUserIdAndTeamId(userId,teamId).orElseThrow(
      () -> new UserTeamNotFoundException("UserTeam with id " + teamId + " not found")
    );
    // Query the repository to find the UserTeam relationship
    return Optional.of(userTeam);
  }

  private boolean isTeamCreator(Team team, Long userId) {
    return team.getCreator().getId().equals(userId);
  }
}

