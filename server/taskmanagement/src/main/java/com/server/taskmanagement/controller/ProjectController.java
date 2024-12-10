package com.server.taskmanagement.controller;

import com.server.taskmanagement.dto.ProjectDto;
import com.server.taskmanagement.dto.TaskDto;
import com.server.taskmanagement.dto.TeamDto;
import com.server.taskmanagement.dto.UserDto;
import com.server.taskmanagement.entity.Project;
import com.server.taskmanagement.entity.Team;
import com.server.taskmanagement.entity.User;
import com.server.taskmanagement.mappers.ProjectMapper;
import com.server.taskmanagement.service.impl.ProjectServiceImpl;
import com.server.taskmanagement.service.impl.TeamServiceImpl;
import com.server.taskmanagement.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/projects")
public class ProjectController {

  @Autowired
  private ProjectServiceImpl projectService;

  @Autowired
  @Qualifier("userServiceImpl")
  private UserServiceImpl userService;

  @Autowired
  private TeamServiceImpl teamService;
  @Autowired
  private ProjectMapper projectMapper;

  @PostMapping()
  public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectDto projectDto) {
    ProjectDto createdProject = projectService.createProject(projectDto);
    return ResponseEntity.ok(createdProject);
  }


  @GetMapping("/{projectId}")
  public ResponseEntity<ProjectDto> getProject(@PathVariable Long projectId) {
    return projectService.findProjectById(projectId)
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }
  // Create a new team and assign it to the project
  @PostMapping("/{projectId}/teams")
  public ResponseEntity<TeamDto> createTeamForProject(@PathVariable Long projectId, @RequestBody TeamDto teamDto) {
    Optional<ProjectDto> projectOptional = projectService.findProjectById(projectId);
    ProjectDto projectDto=projectOptional.get();
    // Create the new team and assign it to the project
    TeamDto savedTeam = teamService.createTeamForProject(projectDto, teamDto);
    return ResponseEntity.ok(savedTeam);
  }

  // Assign an existing team to a project
  @PostMapping("/{projectId}/teams/{teamId}")
  public ResponseEntity<ProjectDto> assignTeamToProject(@PathVariable Long projectId, @PathVariable Long teamId) {
    // Assign the team to the project
    ProjectDto updatedProject = projectService.assignTeamToProject(teamId, projectId);
    return ResponseEntity.ok(updatedProject);
  }

  @DeleteMapping("/{projectId}/teams")
  public ResponseEntity<Void> unassignTeamFromProject(@PathVariable Long projectId) {


    // Remove the team from the project
    projectService.unassignTeamFromProject(projectId);

    return ResponseEntity.noContent().build(); // 204 No Content on successful unassignment
  }


  @DeleteMapping("/{projectId}")
  public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {

    projectService.deleteProject(projectId);
    return ResponseEntity.noContent().build(); // 204 No Content on successful deletion
  }

  /*
  @PostMapping("/{projectId}/tasks")
  public TaskDTO createTaskForProject(@PathVariable Long projectId, @RequestBody TaskDTO taskDTO) {
    return projectService.createTask(projectId, taskDTO);
  }

  @DeleteMapping("/{projectId}/tasks/{taskId}")
  public void deleteTaskFromProject(@PathVariable Long projectId, @PathVariable Long taskId) {
    projectService.deleteTaskFromProject(projectId, taskId);
  }*/


}

