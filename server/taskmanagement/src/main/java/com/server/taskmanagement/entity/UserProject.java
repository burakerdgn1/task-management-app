package com.server.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @ToString.Exclude  // Prevent circular references
  @EqualsAndHashCode.Exclude
  private User user;

  @ManyToOne
  @JoinColumn(name = "project_id", nullable = false)
  @ToString.Exclude  // Prevent circular references
  @EqualsAndHashCode.Exclude
  private Project project;
}
