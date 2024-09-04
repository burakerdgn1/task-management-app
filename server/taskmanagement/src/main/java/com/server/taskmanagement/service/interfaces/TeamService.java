package com.server.taskmanagement.service.interfaces;

import com.server.taskmanagement.entity.Team;

import java.util.List;
import java.util.Optional;

public interface TeamService {
  List<Team> getAllTeams();
  Optional<Team> findTeamById(Long id);
  Team createTeam(Team team);
  Team updateTeam(Long id, Team team);
  void deleteTeam(Long id);
}
