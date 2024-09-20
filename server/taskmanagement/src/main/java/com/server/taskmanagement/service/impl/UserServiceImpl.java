package com.server.taskmanagement.service.impl;
import com.server.taskmanagement.entity.User;
import com.server.taskmanagement.repository.UserRepository;
import com.server.taskmanagement.security.UserInfoDetails;
import com.server.taskmanagement.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("userServiceImpl")
@Primary
public class UserServiceImpl implements UserService, UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder encoder;

  @Override
  public User createUser(User user) {
    user.setPassword(encoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

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

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User not found");
    }

    // Implement UserDetails directly from your User entity
    return user.map(UserInfoDetails::new)
      .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    //return new UserInfoDetails(user.get());

  }
  public User getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();
    return userDetails.getUser();  // Access the full User entity
  }



}

