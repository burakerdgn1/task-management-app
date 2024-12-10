package com.server.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String roles = "USER";//default USER


  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude  // Prevent circular references
  private Set<Task> tasks;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude  // Prevent circular references
  private Set<UserTeam> userTeams;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude  // Prevent circular references
  private Set<UserProject> userProjects;

}


