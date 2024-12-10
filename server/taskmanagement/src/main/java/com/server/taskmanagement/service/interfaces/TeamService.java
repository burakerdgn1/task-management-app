package com.server.taskmanagement.service.interfaces;

import com.server.taskmanagement.dto.ProjectDto;
import com.server.taskmanagement.dto.TeamDto;
import com.server.taskmanagement.entity.Project;
import com.server.taskmanagement.entity.Team;

import java.util.List;
import java.util.Optional;

public interface TeamService {
  List<TeamDto> getAllTeams();

  TeamDto findTeamById(Long id);

  TeamDto createTeam(TeamDto team);

  TeamDto updateTeam(Long id, TeamDto team);

  void deleteTeam(Long id);

  TeamDto createTeamForProject(ProjectDto project, TeamDto team);

  boolean isUserPartOfTeam(Long userId, Long teamId);
}
