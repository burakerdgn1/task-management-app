package com.server.taskmanagement.service.impl;

import com.server.taskmanagement.entity.Project;
import com.server.taskmanagement.entity.UserProject;
import com.server.taskmanagement.repository.UserProjectRepository;
import com.server.taskmanagement.service.interfaces.UserProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserProjectServiceImpl implements UserProjectService {

  private final UserProjectRepository userProjectRepository;

  @Override
  public UserProject addUserToProject(UserProject userProject) {
    return userProjectRepository.save(userProject);
  }

  @Override
  public Optional<UserProject> findUserProjectById(Long id) {
    return userProjectRepository.findById(id);
  }

  @Override
  public List<UserProject> findAllUserProjects() {
    return userProjectRepository.findAll();
  }

  @Override
  public void removeUserFromProject(Long id) {
    userProjectRepository.deleteById(id);
  }

  public List<Project> getProjectsByUserId(Long userId) {
    return userProjectRepository.findUserProjectsByUserId(userId)
      .stream()
      .map(UserProject::getProject)
      .collect(Collectors.toList());
  }
}

