package com.server.taskmanagement.service.impl;

import com.server.taskmanagement.dto.ProjectDto;
import com.server.taskmanagement.dto.TeamDto;
import com.server.taskmanagement.entity.Project;
import com.server.taskmanagement.entity.Team;
import com.server.taskmanagement.entity.User;
import com.server.taskmanagement.entity.UserProject;
import com.server.taskmanagement.exception.auth.UnauthorizedActionException;
import com.server.taskmanagement.exception.project.NoTeamAssignedToProjectException;
import com.server.taskmanagement.exception.project.ProjectNotFoundException;
import com.server.taskmanagement.exception.project.TeamAlreadyAssignedException;
import com.server.taskmanagement.mappers.ProjectMapper;
import com.server.taskmanagement.mappers.TeamMapper;
import com.server.taskmanagement.repository.ProjectRepository;
import com.server.taskmanagement.service.interfaces.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository projectRepository;
  private final TeamServiceImpl teamService;
  private final UserServiceImpl userService;
  private final UserProjectServiceImpl userProjectService;
  private final ProjectMapper projectMapper;
  private final TeamMapper teamMapper;


  @Override
  public ProjectDto createProject(ProjectDto projectDto) {
    Project project = projectMapper.toProject(projectDto);
    User authenticatedUser = userService.getAuthenticatedUser();
    project.setCreator(authenticatedUser);  // Set the authenticated user as the project creator
    Project savedProject = projectRepository.save(project);
    return projectMapper.toProjectDto(savedProject);

  }

  @Override
  public ProjectDto createProjectWithTeam(ProjectDto projectDto,Team team) {
    Project project = projectMapper.toProject(projectDto);
    User authenticatedUser = userService.getAuthenticatedUser();
    if (!projectDto.getOwner().getId().equals(authenticatedUser.getId())) {
      throw new UnauthorizedActionException("Only project creator can create teams for this project");
    }
    project.setCreator(authenticatedUser);
    project.setTeam(team);// Set the authenticated user as the project creator
    Project savedProject = projectRepository.save(project);
    return projectMapper.toProjectDto(savedProject);

  }

  @Override
  public Optional<ProjectDto> findProjectById(Long id) {
    User authenticatedUser = userService.getAuthenticatedUser();

    // Verify that the authenticated user is the creator of the project
    Optional<Project> projectOptional = projectRepository.findById(id);
    if(projectOptional.isEmpty()) {
      throw new ProjectNotFoundException("Project with id " + id + " not found");
    }
    if (!projectOptional.get().getCreator().getId().equals(authenticatedUser.getId())) {
      throw new UnauthorizedActionException("Only team creator can create teams for this project");
    }
    return projectOptional
      .map(projectMapper::toProjectDto);  // Directly map to ProjectDto
  }

  public Project getProjectEntity(Long projectId) {
    return projectRepository.findById(projectId)
      .orElseThrow(() -> new ProjectNotFoundException("Project with id " + projectId + " not found")); // Add specific exception if needed
  }

  @Override
  public List<Project> findAllProjects() {
    return projectRepository.findAll();
  }

  //will be updated with project mapper
  @Override
  public ProjectDto updateProject(Long id, ProjectDto updatedProject) {
   projectRepository.findById(id)
      .orElseThrow(
        () -> new ProjectNotFoundException("Project with id " + id + " not found")
      );
   Project project = projectMapper.toProject(updatedProject);
   Project savedProject = projectRepository.save(project);
   return projectMapper.toProjectDto(savedProject);
  }

  /*
  @Override
  public Project updateProject(Long id, Project updatedProject) {
    return projectRepository.findById(id)
      .map(existingProject -> {
        existingProject.setName(updatedProject.getName());
        existingProject.setDescription(updatedProject.getDescription());
        return projectRepository.save(existingProject);
      })
      .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
  }*/

  @Transactional
  @Override
  public void deleteProject(Long id) {
    User authenticatedUser = userService.getAuthenticatedUser();
    ProjectDto project = findProjectById(id).orElseThrow(
      () -> new ProjectNotFoundException("Project with id " + id + " not found")
    );
    // Check if the authenticated user is the creator of the project
    if (!project.getOwner().getId().equals(authenticatedUser.getId())) {
      throw new UnauthorizedActionException("Only project creator can delete this project");
    }
    projectRepository.deleteById(id);
  }


  @Transactional
  @Override
  public ProjectDto assignTeamToProject(Long teamId, Long projectId) {
    Optional<ProjectDto> projectDto = findProjectById(projectId);


    User authenticatedUser = userService.getAuthenticatedUser();

    // Verify that the authenticated user is the creator of the project
    if (!projectDto.get().getOwner().getId().equals(authenticatedUser.getId())) {
      throw new UnauthorizedActionException("Only team creator can create teams for this project");
    }

    Project project= projectMapper.toProject(projectDto.get());

    if (project.getTeam()!=null){
      throw new TeamAlreadyAssignedException("This project already has a team assigned.");
    }

    TeamDto teamDto = teamService.findTeamById(teamId);

    Team team = teamMapper.toEntity(teamDto);

    project.setTeam(team);

    team.addProject(project);

    team.getUserTeams().forEach(userTeam -> {
      User user = userTeam.getUser();
      UserProject userProject = new UserProject();
      userProject.setUser(user);
      userProject.setProject(project);
      userProjectService.addUserToProject(userProject);
    });

    teamService.createTeam(teamDto);   // Update team with new project
    return createProjectWithTeam(projectDto.get(),team);
  }

  @Transactional
  public void unassignTeamFromProject(Long projectId) {
    ProjectDto projectDto = findProjectById(projectId).get();

    User authenticatedUser = userService.getAuthenticatedUser();

    // Verify that the authenticated user is the creator of the project
    if ( !projectDto.getOwner().getId().equals(authenticatedUser.getId())) {
      throw new UnauthorizedActionException("Only team creator can unassign team from this project");
    }

    Project project = projectMapper.toProject(projectDto);
    Team team = project.getTeam();

    if (team == null) {
      throw new NoTeamAssignedToProjectException("There is no team assigned to this project.");
    }

    /*
    Set<UserProject> userProjects = project.getUserProjects();
    userProjects.forEach(userProject -> {
      entityManager.detach(userProject); // Detach the entity from persistence context
      userProjectService.removeUserFromProject(userProject.getId()); // Manually delete the UserProject record
    });
    */

    // Unassign the team from the project
    project.setTeam(null);
    team.getProjects().remove(project);

    TeamDto teamDto = teamMapper.toDto(team);
    // Save both the team and the project
    teamService.createTeam(teamDto); // Persist the changes to the team
    createProjectWithTeam(projectDto,null);        // Persist the changes to the project
  }


}

