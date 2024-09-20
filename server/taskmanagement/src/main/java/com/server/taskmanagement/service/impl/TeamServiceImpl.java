package com.server.taskmanagement.service.impl;
import com.server.taskmanagement.entity.Project;
import com.server.taskmanagement.entity.Team;
import com.server.taskmanagement.entity.User;
import com.server.taskmanagement.repository.ProjectRepository;
import com.server.taskmanagement.repository.TeamRepository;
import com.server.taskmanagement.repository.UserTeamRepository;
import com.server.taskmanagement.service.interfaces.TeamService;
import com.server.taskmanagement.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor

public class TeamServiceImpl implements TeamService {

  private final TeamRepository teamRepository;
  private final UserTeamRepository userTeamRepository;


  private final UserServiceImpl userService;

  @Override
  public List<Team> getAllTeams() {
    return teamRepository.findAll();
  }

  @Override
  public Optional<Team> findTeamById(Long id) {
    return teamRepository.findById(id);
  }

  @Override
  public Team createTeam(Team team) {
    User authenticatedUser=userService.getAuthenticatedUser();
    if ( !team.getCreator().getId().equals(authenticatedUser.getId())) {
      //throw new UnauthorizedActionException("Only team creator can create teams for this project");
    }
    return teamRepository.save(team);
  }

  @Override
  @Transactional
  public Team createTeamForProject(Project project, Team team) {

    team.setCreator(project.getCreator()); // Assign team creator as the project creator
    team.getProjects().add(project); // Assign team to project
    project.setTeam(team);
    return teamRepository.save(team);
  }

  @Override
  public Team updateTeam(Long id, Team team) {
    Optional<Team> existingTeam = teamRepository.findById(id);
    if (existingTeam.isPresent()) {
      Team updatedTeam = existingTeam.get();
      updatedTeam.setName(team.getName());
      return teamRepository.save(updatedTeam);
    } else {
      //throw new IllegalArgumentException("Team with ID " + id + " not found.");
      return null;
    }
  }

  public void assignProjectToTeam(Long teamId, Long projectId) {
    Optional<Team> existingTeam = teamRepository.findById(teamId);


  }



  @Override
  public void deleteTeam(Long id) {
    if (teamRepository.existsById(id)) {
      teamRepository.deleteById(id);
    } else {
      //throw new IllegalArgumentException("Team with ID " + id + " not found.");
    }
  }

  public boolean isUserPartOfTeam(Long userId, Long teamId) {
    return userTeamRepository.findUserTeamByUserIdAndTeamId(userId, teamId).isPresent();
  }
}

