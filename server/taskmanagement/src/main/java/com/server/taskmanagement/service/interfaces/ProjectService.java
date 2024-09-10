package com.server.taskmanagement.service.interfaces;

import com.server.taskmanagement.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
  Project createProject(Project project);

  Optional<Project> findProjectById(Long id);

  List<Project> findAllProjects();

  Project updateProject(Long id, Project updatedProject);

  void deleteProject(Long id);

  void addTeamToProject(Long teamId, Long projectId,Long userId);
}

