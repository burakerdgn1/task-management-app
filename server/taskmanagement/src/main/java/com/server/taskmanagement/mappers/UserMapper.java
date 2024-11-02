package com.server.taskmanagement.mappers;

import com.server.taskmanagement.dto.UserDto;
import com.server.taskmanagement.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toDto(User user);
  User toEntity(UserDto userDto);
}
