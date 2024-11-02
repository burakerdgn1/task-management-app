package com.server.taskmanagement.service.interfaces;

import com.server.taskmanagement.dto.AuthRequest;
import com.server.taskmanagement.dto.UserDto;
import com.server.taskmanagement.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

  User createUser(AuthRequest authRequest);

  UserDto updateUser(Long userId, UserDto userDto);

  User findUserById(Long id);

  List<User> findAllUsers();

  void deleteUser(Long id);


}

