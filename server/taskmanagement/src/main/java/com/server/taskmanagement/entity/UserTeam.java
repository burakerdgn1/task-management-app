package com.server.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_team")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTeam {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @ToString.Exclude  // Prevent circular references
  @EqualsAndHashCode.Exclude
  private User user;

  @ManyToOne
  @JoinColumn(name = "team_id", nullable = false)
  @ToString.Exclude  // Prevent circular references
  @EqualsAndHashCode.Exclude
  private Team team;
}

