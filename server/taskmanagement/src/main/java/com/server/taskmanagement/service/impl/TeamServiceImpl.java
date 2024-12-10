package com.server.taskmanagement.service.impl;
import com.server.taskmanagement.dto.ProjectDto;
import com.server.taskmanagement.dto.TeamDto;
import com.server.taskmanagement.entity.Project;
import com.server.taskmanagement.entity.Team;
import com.server.taskmanagement.entity.User;
import com.server.taskmanagement.exception.auth.UnauthorizedActionException;
import com.server.taskmanagement.exception.project.TeamAlreadyAssignedException;
import com.server.taskmanagement.exception.team.TeamNotFoundException;
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
  public TeamDto findTeamById(Long id) {
    Team team = teamRepository.findById(id).orElseThrow(
      ()->new TeamNotFoundException("Team with id:" + id + " not found ")
    );

    return teamMapper.toDto(team);
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
    User authenticatedUser = userService.getAuthenticatedUser();
    if (!projectDto.getOwner().getId().equals(authenticatedUser.getId())) {
      throw new UnauthorizedActionException("Only project creator can create teams for this project");
    }
    Project project=projectMapper.toProject(projectDto);
    if(project.getTeam()!=null){
      throw new TeamAlreadyAssignedException("Project already has a team assigned");
    }
    Team team = teamMapper.toEntity(teamDto);
    team.setCreator(project.getCreator()); // Assign team creator as the project creator
    team.getProjects().add(project); // Assign team to project
    project.setTeam(team);
    Team savedTeam = teamRepository.save(team);
    return teamMapper.toDto(savedTeam);
  }

  @Transactional
  @Override
  public TeamDto updateTeam(Long id, TeamDto teamDto) {
    teamRepository.findById(id).orElseThrow(
      ()->new TeamNotFoundException("Team with id:" + id + " not found ")
    );
    Team teamToUpdate = teamMapper.toEntity(teamDto);
    Team savedTeam = teamRepository.save(teamToUpdate);
    return teamMapper.toDto(savedTeam);

  }

  /*
  public void assignProjectToTeam(Long teamId, Long projectId) {
    Optional<Team> existingTeam = teamRepository.findById(teamId);
  }
   */

  @Transactional
  @Override
  public void deleteTeam(Long id) {

    TeamDto team = findTeamById(id);
    User authenticatedUser = userService.getAuthenticatedUser();
    if (!team.getOwner().getId().equals(authenticatedUser.getId())) {
      throw new UnauthorizedActionException("Only the creator can delete a team");
    }
    teamRepository.deleteById(id);

  }

  public boolean isUserPartOfTeam(Long userId, Long teamId) {
    return userTeamService.findUserTeamByUserIdAndTeamId(userId, teamId).isPresent();
  }
}

