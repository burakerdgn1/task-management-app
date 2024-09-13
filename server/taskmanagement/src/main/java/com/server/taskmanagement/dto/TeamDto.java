package com.server.taskmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDto {
  private Long id;
  private String name;
  private UserDto owner;
  //private List<UserDto> members;
  //private List<ProjectDto> projects;
  //private List<TaskDto> tasks;
}

