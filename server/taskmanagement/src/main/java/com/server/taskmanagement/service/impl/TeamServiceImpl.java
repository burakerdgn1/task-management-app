package com.server.taskmanagement.service.impl;
import com.server.taskmanagement.entity.Project;
import com.server.taskmanagement.entity.Team;
import com.server.taskmanagement.repository.ProjectRepository;
import com.server.taskmanagement.repository.TeamRepository;
import com.server.taskmanagement.repository.UserTeamRepository;
import com.server.taskmanagement.service.interfaces.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class TeamServiceImpl implements TeamService {

  private final TeamRepository teamRepository;
  private final ProjectRepository projectRepository;
  private final UserTeamRepository userTeamRepository;

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
  @Transactional
  public Team createTeamForProject(Long projectId, Team team, Long userId) {
    Project project = projectRepository.findById(projectId)
      .orElseThrow(
        //() -> new ProjectNotFoundException("Project not found")
      );

    if (!project.getCreator().getId().equals(userId)) {
      //throw new UnauthorizedActionException("Only project creator can create teams for this project");
    }

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

  @Override
  public void deleteTeam(Long id) {
    if (teamRepository.existsById(id)) {
      teamRepository.deleteById(id);
    } else {
      //throw new IllegalArgumentException("Team with ID " + id + " not found.");
    }
  }

  public boolean isUserPartOfTeam(Long userId, Long teamId) {
    return userTeamRepository.findByUserIdAndTeamId(userId, teamId).isPresent();
  }
}

