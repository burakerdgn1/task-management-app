package com.server.taskmanagement.entity;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;


  @ManyToOne
  @JoinColumn(name = "project_id")//A task can belong to a single user without project
  @EqualsAndHashCode.Exclude
  private Project project;

  @ManyToOne
  @JoinColumn(name = "team_id")
  @EqualsAndHashCode.Exclude
  private Team team;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude  // Prevent circular references
  private User user;
}

