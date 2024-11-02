package com.server.taskmanagement.mappers;
import com.server.taskmanagement.dto.TeamDto;
import com.server.taskmanagement.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TeamMapper {

  @Mapping(source = "creator", target = "owner")
  TeamDto toDto(Team team);

  @Mapping(source = "owner", target = "creator")
  Team toEntity(TeamDto teamDto);
}
