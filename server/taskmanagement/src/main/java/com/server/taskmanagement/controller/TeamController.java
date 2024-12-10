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

import java.util.Optional;

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
    TeamDto savedTeam = teamService.createTeam(teamDto);
    // Setting creator
    /*UserDto creatorDto = new UserDto();
    creatorDto.setId(authenticatedUser.getId());
    creatorDto.setUsername(authenticatedUser.getUsername());
    savedTeam.setOwner(creatorDto);*/
    return ResponseEntity.ok(savedTeam);
  }

  @GetMapping("/{teamId}")
  public ResponseEntity<TeamDto> getTeam(@PathVariable Long teamId) {
    TeamDto teamDto = teamService.findTeamById(teamId);

    /*
    UserDto ownerDto = new UserDto();
    ownerDto.setId(teamDto.getOwner().getId());
    ownerDto.setUsername(teamDto.getOwner().getUsername());
    teamDto.setOwner(ownerDto);*/

    return ResponseEntity.ok(teamDto);
  }

  @DeleteMapping("/{teamId}")
  public ResponseEntity<Void> deleteTeam(@PathVariable Long teamId) {

    teamService.deleteTeam(teamId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{teamId}/users")
  public ResponseEntity<String> addUserToTeam(@PathVariable Long teamId, @RequestBody Long userId) {

    userTeamService.addUserToTeam(userId, teamId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{teamId}/users/{userId}")
  public ResponseEntity<String> deleteUserFromTeam(@PathVariable Long teamId, @PathVariable Long userId) {

    userTeamService.removeUserFromTeam(userId, teamId);
    return ResponseEntity.ok().build();
  }
}

