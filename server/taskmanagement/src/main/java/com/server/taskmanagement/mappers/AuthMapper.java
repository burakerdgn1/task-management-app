package com.server.taskmanagement.mappers;
import com.server.taskmanagement.dto.AuthRequest;
import com.server.taskmanagement.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
  User toUser(AuthRequest authRequest);
  AuthRequest toAuthRequest(User user); // Reverse mapping, if needed
}

