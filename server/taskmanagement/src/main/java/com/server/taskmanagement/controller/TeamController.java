package com.server.taskmanagement.controller;

import com.server.taskmanagement.dto.TeamDto;
import com.server.taskmanagement.dto.UserDto;
import com.server.taskmanagement.entity.Team;
import com.server.taskmanagement.entity.User;
import com.server.taskmanagement.service.impl.TeamServiceImpl;
import com.server.taskmanagement.service.impl.UserServiceImpl;
import com.server.taskmanagement.service.impl.UserTeamServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/teams")
public class TeamController {

  @Autowired
  private TeamServiceImpl teamService;

  @Autowired
  @Qualifier("userServiceImpl")
  private UserServiceImpl userService;

  @Autowired
  private UserTeamServiceImpl userTeamService;

  @PostMapping()
  public ResponseEntity<TeamDto> createTeam(@RequestBody TeamDto teamDto) {
    User authenticatedUser = userService.getAuthenticatedUser();

    Team team = new Team();
    team.setName(teamDto.getName());
    team.setCreator(authenticatedUser);

    Team savedTeam = teamService.createTeam(team);

    // Mapping to TeamDto
    TeamDto savedTeamDto = new TeamDto();
    savedTeamDto.setId(savedTeam.getId());
    savedTeamDto.setName(savedTeam.getName());

    // Setting creator/owner
    UserDto creatorDto = new UserDto();
    creatorDto.setId(authenticatedUser.getId());
    creatorDto.setUsername(authenticatedUser.getUsername());

    savedTeamDto.setOwner(creatorDto);
    return ResponseEntity.ok(savedTeamDto);
  }

  @GetMapping("/{teamId}")
  public ResponseEntity<TeamDto> getTeam(@PathVariable Long teamId) {
    Team team = teamService.findTeamById(teamId)
      .orElseThrow(
        //() -> new TeamNotFoundException("Team not found with id: " + teamId)
      );

    TeamDto teamDto = new TeamDto();
    teamDto.setId(team.getId());
    teamDto.setName(team.getName());

    UserDto ownerDto = new UserDto();
    ownerDto.setId(team.getCreator().getId());
    ownerDto.setUsername(team.getCreator().getUsername());
    teamDto.setOwner(ownerDto);

    return ResponseEntity.ok(teamDto);
  }

  @DeleteMapping("/{teamId}")
  public ResponseEntity<Void> deleteTeam(@PathVariable Long teamId) {
    User authenticatedUser = userService.getAuthenticatedUser();
    Team team = teamService.findTeamById(teamId)
      .orElseThrow(
        //() -> new TeamNotFoundException("Team not found with id: " + teamId)
      );
    if (!team.getCreator().getId().equals(authenticatedUser.getId())) {
      return ResponseEntity.status(403).build();
    }

    teamService.deleteTeam(teamId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{teamId}/users")
  public ResponseEntity<String> addUserToTeam(@PathVariable Long teamId, @RequestBody Long userId) {
    User authenticatedUser = userService.getAuthenticatedUser();
    Team team = teamService.findTeamById(teamId)
      .orElseThrow(
        //() -> new TeamNotFoundException("Team not found with id: " + teamId)
      );

    if (!team.getCreator().getId().equals(authenticatedUser.getId())) {
      return ResponseEntity.status(403).build();
    }

    // Check if the user is already a member of the team
    if (userTeamService.isUserInTeam(userId, teamId)) {
      return ResponseEntity.badRequest().body("User is already a member of the team.");
    }

    userTeamService.addUserToTeam(userId, teamId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{teamId}/users/{userId}")
  public ResponseEntity<String> deleteUserFromTeam(@PathVariable Long teamId, @PathVariable Long userId) {
    User authenticatedUser = userService.getAuthenticatedUser();
    Team team = teamService.findTeamById(teamId)
      .orElseThrow(
        //() -> new TeamNotFoundException("Team not found with id: " + teamId)
      );

    if (!team.getCreator().getId().equals(authenticatedUser.getId())) {
      return ResponseEntity.status(403).build();
    }
    if (!userTeamService.isUserInTeam(userId, teamId)) {
      return ResponseEntity.badRequest().body("User is not a member of the team.");
    }

    userTeamService.removeUserFromTeam(userId, teamId,authenticatedUser.getId());
    return ResponseEntity.ok().build();
  }
}

