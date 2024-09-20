package com.server.taskmanagement.service.impl;

import com.server.taskmanagement.entity.Project;
import com.server.taskmanagement.entity.Team;
import com.server.taskmanagement.entity.User;
import com.server.taskmanagement.entity.UserProject;
import com.server.taskmanagement.repository.ProjectRepository;
import com.server.taskmanagement.repository.TeamRepository;
import com.server.taskmanagement.service.interfaces.ProjectService;
import com.server.taskmanagement.service.interfaces.TeamService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Persistent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository projectRepository;
  private final TeamServiceImpl teamService;
  private final UserServiceImpl userService;
  private final UserProjectServiceImpl userProjectService;

  @PersistenceContext
  private EntityManager entityManager;


  @Override
  public Project createProject(Project project) {
    User authenticatedUser=userService.getAuthenticatedUser();
    if ( !project.getCreator().getId().equals(authenticatedUser.getId())) {
      //throw new UnauthorizedActionException("Only team creator can create teams for this project");
    }
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




  @Transactional
  public Project assignTeamToProject(Long teamId, Long projectId) {
    Project project = findProjectById(projectId)
      .orElseThrow(
        //() -> new ProjectNotFoundException("Project not found")
      );

    if(project.getTeam()!=null){
      throw new RuntimeException("This project already has a team assigned.");
    }

    Team team = teamService.findTeamById(teamId)
      .orElseThrow(
        //() -> new TeamNotFoundException("Team not found")
      );

    project.setTeam(team);

    team.addProject(project);

    team.getUserTeams().forEach(userTeam -> {
      User user = userTeam.getUser();
      UserProject userProject = new UserProject();
      userProject.setUser(user);
      userProject.setProject(project);
      userProjectService.addUserToProject(userProject);
    });

    teamService.createTeam(team);   // Update team with new project

    return createProject(project);
  }

  @Transactional
  public void unassignTeamFromProject(Long projectId) {
    Project project = findProjectById(projectId)
      .orElseThrow(
        //() -> new ProjectNotFoundException("Project not found with id: " + projectId)
      );

    Team team = project.getTeam();

    if (team == null) {
      throw new IllegalStateException("No team is assigned to this project.");
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

    // Save both the team and the project
    teamService.createTeam(team); // Persist the changes to the team
    createProject(project);        // Persist the changes to the project
  }


}

