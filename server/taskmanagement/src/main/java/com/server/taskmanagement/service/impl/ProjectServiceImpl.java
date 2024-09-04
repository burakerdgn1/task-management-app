package com.server.taskmanagement.service.impl;

import com.server.taskmanagement.entity.Project;
import com.server.taskmanagement.entity.Team;
import com.server.taskmanagement.repository.ProjectRepository;
import com.server.taskmanagement.service.interfaces.ProjectService;
import com.server.taskmanagement.service.interfaces.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository projectRepository;
  private final TeamService teamService;

  @Override
  public Project createProject(Project project) {
    return projectRepository.save(project);
  }

  @Override
  public Optional<Project> findProjectById(Long id) {
    return projectRepository.findById(id);
  }

  @Override
  public List<Project> findAllProjects() {
    return projectRepository.findAll();
  }

  @Override
  public Project updateProject(Long id, Project updatedProject) {
    return projectRepository.findById(id)
      .map(existingProject -> {
        existingProject.setName(updatedProject.getName());
        //existingProject.setDescription(updatedProject.getDescription());
        // Other fields will be updated necessarily
        return projectRepository.save(existingProject);
      })
      .orElseThrow(
        //() -> new ProjectNotFoundException("Project not found with id: " + id)
      );
  }

  @Override
  public void deleteProject(Long id) {
    projectRepository.deleteById(id);
  }

  @Override
  @Transactional
  public void addProjectToTeam(Long projectId, Long teamId) {
    Project project = projectRepository.findById(projectId)
      .orElseThrow(
        //() -> new ProjectNotFoundException("Project not found with id: " + projectId)
      );
    Team team = teamService.findTeamById(teamId)
      .orElseThrow(
        //() -> new TeamNotFoundException("Team not found with id: " + teamId)
      );

    project.setTeam(team);
    projectRepository.save(project);
  }
}

