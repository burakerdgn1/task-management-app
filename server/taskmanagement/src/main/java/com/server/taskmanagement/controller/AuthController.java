package com.server.taskmanagement.controller;


import com.server.taskmanagement.entity.User;
import com.server.taskmanagement.security.JwtService;
import com.server.taskmanagement.service.impl.UserServiceImpl;
import com.server.taskmanagement.service.interfaces.UserService;
import com.server.taskmanagement.dto.AuthRequest;
import com.server.taskmanagement.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private UserServiceImpl userService;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @PostMapping("/register")
  public ResponseEntity<String> registerUser(@RequestBody AuthRequest authRequest) {
    // Convert AuthRequest to User entity with mappers

    /*
    User user = new User();
    user.setUsername(authRequest.getUsername());
    user.setPassword(authRequest.getPassword()); // Password encoding will be handled in the service layer
    user.setRoles("USER");
    */
    // Set default role for new users
    userService.createUser(authRequest);
    return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> authenticateAndGenerateToken(@RequestBody AuthRequest authRequest) {
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
    );
    if (authentication.isAuthenticated()) {
      String token = jwtService.generateToken(authRequest.getUsername());
      return new ResponseEntity<>(new AuthResponse(token, "Authentication successful!"), HttpStatus.OK);
    } else {
      throw new UsernameNotFoundException("Invalid login credentials!");
    }
  }
}
