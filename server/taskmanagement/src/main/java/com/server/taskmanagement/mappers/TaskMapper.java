package com.server.taskmanagement.mappers;

import com.server.taskmanagement.dto.TaskDto;
import com.server.taskmanagement.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TaskMapper {

  @Mappings({
    @Mapping(source = "task.project.id", target = "projectId"),
    @Mapping(source = "task.project.name", target = "projectName"),
    @Mapping(source = "task.team.id", target = "teamId"),
    @Mapping(source = "task.team.name", target = "teamName"),
    @Mapping(source = "task.user.id", target = "userId"),
    @Mapping(source = "task.user.username", target = "assignedUsername")
  })
  TaskDto toDto(Task task);

  @Mappings({
    @Mapping(source = "projectId", target = "project.id"),
    @Mapping(source = "teamId", target = "team.id"),
    @Mapping(source = "userId", target = "user.id")
  })
  Task toEntity(TaskDto taskDto);
}

