package com.server.taskmanagement.service.impl;
import com.server.taskmanagement.entity.Team;
import com.server.taskmanagement.repository.TeamRepository;
import com.server.taskmanagement.service.interfaces.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TeamServiceImpl implements TeamService {

  private final TeamRepository teamRepository;

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

  @Override
  public void deleteTeam(Long id) {
    if (teamRepository.existsById(id)) {
      teamRepository.deleteById(id);
    } else {
      //throw new IllegalArgumentException("Team with ID " + id + " not found.");
    }
  }
}

