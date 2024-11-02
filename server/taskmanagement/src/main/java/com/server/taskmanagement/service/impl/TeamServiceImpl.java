package com.server.taskmanagement.service.impl;
import com.server.taskmanagement.dto.ProjectDto;
import com.server.taskmanagement.dto.TeamDto;
import com.server.taskmanagement.entity.Project;
import com.server.taskmanagement.entity.Team;
import com.server.taskmanagement.entity.User;
import com.server.taskmanagement.mappers.ProjectMapper;
import com.server.taskmanagement.mappers.TeamMapper;
import com.server.taskmanagement.repository.ProjectRepository;
import com.server.taskmanagement.repository.TeamRepository;
import com.server.taskmanagement.service.interfaces.TeamService;
import com.server.taskmanagement.service.interfaces.UserService;
import com.server.taskmanagement.service.interfaces.UserTeamService;
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
  private final UserTeamServiceImpl userTeamService;
  private final UserServiceImpl userService;
  private final TeamMapper teamMapper;
  private final ProjectMapper projectMapper;

  @Override
  public List<TeamDto> getAllTeams() {
    List<Team> teams = teamRepository.findAll();
    return teams.stream().map(teamMapper::toDto).toList();
  }

  @Transactional
  @Override
  public Optional<TeamDto> findTeamById(Long id) {
    Optional<Team> team = teamRepository.findById(id);
    User authenticatedUser = userService.getAuthenticatedUser();
    if(team.isEmpty()) {
      //exception
    }
    TeamDto teamDto = teamMapper.toDto(team.get());
    if (!teamDto.getId().equals(authenticatedUser.getId())) {
      //exception
    }
    return team.map(teamMapper::toDto);
  }

  @Override
  public TeamDto createTeam(TeamDto teamDto) {
    User authenticatedUser = userService.getAuthenticatedUser();
    Team team = teamMapper.toEntity(teamDto);
    team.setName(teamDto.getName());
    team.setCreator(authenticatedUser);
    Team savedTeam = teamRepository.save(team);
    return teamMapper.toDto(savedTeam);
  }

  @Override
  @Transactional
  public TeamDto createTeamForProject(ProjectDto projectDto, TeamDto teamDto) {
    Team team = teamMapper.toEntity(teamDto);
    Project project=projectMapper.toProject(projectDto);
    team.setCreator(project.getCreator()); // Assign team creator as the project creator
    team.getProjects().add(project); // Assign team to project
    project.setTeam(team);
    Team savedTeam = teamRepository.save(team);
    return teamMapper.toDto(savedTeam);
  }

  @Override
  public TeamDto updateTeam(Long id, TeamDto teamDto) {
    Optional<Team> existingTeam = teamRepository.findById(id);
    if (existingTeam.isPresent()) {
      Team updatedTeam = existingTeam.get();
      updatedTeam.setName(teamDto.getName());
      Team savedTeam = teamRepository.save(updatedTeam);
      return teamMapper.toDto(savedTeam);
    } else {
      //throw new IllegalArgumentException("Team with ID " + id + " not found.");
      return null;
    }
  }

  /*
  public void assignProjectToTeam(Long teamId, Long projectId) {
    Optional<Team> existingTeam = teamRepository.findById(teamId);
  }
   */

  @Transactional
  @Override
  public void deleteTeam(Long id) {

    Optional<TeamDto> team = findTeamById(id);
    if (team.isEmpty()) {
      //...
    }
    User authenticatedUser = userService.getAuthenticatedUser();

    if (!team.get().getOwner().getId().equals(authenticatedUser.getId())) {
      //exception...
    }

    teamRepository.deleteById(id);

  }

  public boolean isUserPartOfTeam(Long userId, Long teamId) {
    return userTeamService.findUserTeamByUserIdAndTeamId(userId, teamId).isPresent();
  }
}

