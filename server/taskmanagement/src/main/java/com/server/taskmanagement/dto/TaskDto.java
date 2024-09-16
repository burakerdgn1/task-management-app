package com.server.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  // Only include non-null fields in the response
public class TaskDto {

  private Long id;

  private String title;

  private String description;

  // Nullable: For independent tasks (not linked to any project)
  private Long projectId;

  // Nullable: For personal tasks (not linked to any team)
  private Long teamId;

  // Always included: Assigned user for this task
  private Long userId;

  // Optional fields for response
  private String projectName;

  private String teamName;

  private String assignedUsername;
}
