package com.server.taskmanagement.service.interfaces;

import com.server.taskmanagement.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

  User createUser(User user);

  //User updateUser(Long userId, User user);

  Optional<User> findUserById(Long id);

  List<User> findAllUsers();

  void deleteUser(Long id);
}

