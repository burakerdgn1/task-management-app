package com.server.taskmanagement.service.impl;

import com.server.taskmanagement.entity.User;
import com.server.taskmanagement.repository.UserRepository;
import com.server.taskmanagement.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserServiceImpl implements UserService {

  private UserRepository userRepository;

  @Override
  public User createUser(User user) {
    // Business logic (e.g., check if the username or email is already taken)
    return userRepository.save(user);
  }

  /*
  @Override
  public User updateUser(Long userId, User user) {
    Optional<User> existingUser = userRepository.findById(userId);
    if (existingUser.isPresent()) {
      User updatedUser = existingUser.get();
      updatedUser.setUsername(user.getUsername());
      updatedUser.setPassword(user.getPassword());
      // Update other fields as necessary
      return userRepository.save(updatedUser);
    } else {
      // Handle user not found scenario
      return null;
    }
  }
   */

  @Override
  public Optional<User> findUserById(Long id) {
    return userRepository.findById(id);
  }

  @Override
  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}

