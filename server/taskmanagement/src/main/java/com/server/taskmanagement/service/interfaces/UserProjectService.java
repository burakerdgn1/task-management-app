package com.server.taskmanagement.service.interfaces;

import com.server.taskmanagement.entity.UserProject;

import java.util.List;
import java.util.Optional;

public interface UserProjectService {
  UserProject addUserToProject(UserProject userProject);

  Optional<UserProject> findUserProjectById(Long id);

  List<UserProject> findAllUserProjects();

  void removeUserFromProject(Long id);
}

