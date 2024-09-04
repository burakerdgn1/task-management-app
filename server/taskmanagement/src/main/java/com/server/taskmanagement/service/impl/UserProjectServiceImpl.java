package com.server.taskmanagement.service.impl;

import com.server.taskmanagement.entity.UserProject;
import com.server.taskmanagement.repository.UserProjectRepository;
import com.server.taskmanagement.service.interfaces.UserProjectService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
}

