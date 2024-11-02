package com.server.taskmanagement.service.interfaces;

import com.server.taskmanagement.dto.ProjectDto;
import com.server.taskmanagement.entity.Project;
import com.server.taskmanagement.entity.Team;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
  ProjectDto createProject(ProjectDto projectDto);

  ProjectDto createProjectWithTeam(ProjectDto projectDto, Team team);

  Optional<ProjectDto> findProjectById(Long id);

  List<Project> findAllProjects();

  Project updateProject(Long id, Project updatedProject);

  void deleteProject(Long id);

  ProjectDto assignTeamToProject(Long teamId, Long projectId);
}

