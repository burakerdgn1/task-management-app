package com.server.taskmanagement.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
  private Long id;
  private String name;
  //private List<TaskDto> tasks;
  //private List<TeamDto> teams;
}

