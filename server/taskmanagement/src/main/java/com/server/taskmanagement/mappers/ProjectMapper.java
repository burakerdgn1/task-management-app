package com.server.taskmanagement.mappers;

import com.server.taskmanagement.dto.ProjectDto;
import com.server.taskmanagement.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

  @Mapping(source = "creator", target = "owner")
  ProjectDto toProjectDto(Project project);

  @Mapping(source = "owner", target = "creator")
  Project toProject(ProjectDto projectDto);



}

