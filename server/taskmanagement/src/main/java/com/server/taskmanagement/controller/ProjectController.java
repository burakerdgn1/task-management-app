package com.server.taskmanagement.controller;

import com.server.taskmanagement.dto.ProjectDto;
import com.server.taskmanagement.dto.UserDto;
import com.server.taskmanagement.entity.Project;
import com.server.taskmanagement.entity.User;
import com.server.taskmanagement.service.impl.ProjectServiceImpl;
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

  @PostMapping()
  public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectDto projectDto) {
    Project project= new Project();
    project.setName(projectDto.getName());
    project.setDescription(projectDto.getDescription());
    User authenticatedUser=userService.getAuthenticatedUser();
    project.setCreator(authenticatedUser);
    Project savedProject=projectService.createProject(project);

    ProjectDto savedProjectDto = new ProjectDto();
    savedProjectDto.setId(savedProject.getId());
    savedProjectDto.setName(savedProject.getName());
    savedProjectDto.setDescription(savedProject.getDescription());

    UserDto creatorDto = new UserDto();
    creatorDto.setId(authenticatedUser.getId());
    creatorDto.setUsername(authenticatedUser.getUsername());

    savedProjectDto.setOwner(creatorDto);
    return ResponseEntity.ok(savedProjectDto);
  }

  @GetMapping("/{projectId}")
  public ResponseEntity<ProjectDto> getProject(@PathVariable Long projectId) {
    Optional<Project> projectOptional = projectService.findProjectById(projectId);

    // If the project is not found, return 404 NOT FOUND
    if (projectOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    // Convert the Project entity to ProjectDto(will be done by mapper)
    Project project = projectOptional.get();
    ProjectDto projectDto = new ProjectDto();
    projectDto.setId(project.getId());
    projectDto.setName(project.getName());
    projectDto.setDescription(project.getDescription());
    UserDto ownerDto = new UserDto();
    ownerDto.setId(project.getCreator().getId());
    ownerDto.setUsername(project.getCreator().getUsername());
    projectDto.setOwner(ownerDto);
    return ResponseEntity.ok(projectDto);
  }

  @DeleteMapping("/{projectId}")
  public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
    User authenticatedUser = userService.getAuthenticatedUser();
    Optional<Project> project = projectService.findProjectById(projectId);

    // Check if the authenticated user is the creator of the project
    if (project.isPresent()&&!project.get().getCreator().getId().equals(authenticatedUser.getId())) {
      // 403 Forbidden if the user is not the owner
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
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

