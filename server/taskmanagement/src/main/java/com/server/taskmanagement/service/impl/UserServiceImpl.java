package com.server.taskmanagement.service.impl;
import com.server.taskmanagement.dto.AuthRequest;
import com.server.taskmanagement.dto.UserDto;
import com.server.taskmanagement.entity.User;
import com.server.taskmanagement.exception.user.UserNotFoundException;
import com.server.taskmanagement.mappers.AuthMapper;
import com.server.taskmanagement.mappers.UserMapper;
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

  @Autowired
  AuthMapper authMapper;

  @Autowired
  UserMapper userMapper;

  @Override
  public User createUser(AuthRequest authRequest) {
    User userToCreate = authMapper.toUser(authRequest);
    userToCreate.setPassword(encoder.encode(authRequest.getPassword()));
    userToCreate.setRoles("USER");
    return userRepository.save(userToCreate);
  }

  @Override
  public UserDto updateUser(Long userId, UserDto userDto) {
    User existingUser = userRepository.findById(userId).orElseThrow(
      ()-> new UserNotFoundException("User with "+userId+" not found")
    );
    existingUser.setUsername(userDto.getUsername());
    //updatedUser.setPassword(encoder.encode(userDto.getPassword())); // Encrypt password
    return userMapper.toDto(userRepository.save(existingUser));  // Convert User to UserDto

  }

  @Override
  public User findUserById(Long id) {
    return userRepository.findById(id).orElseThrow(
      () -> new UserNotFoundException("User with "+id+" not found")
    );
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

  }
  public User getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();
    return userDetails.getUser();  // Access the full User entity
  }



}

