package com.server.taskmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Override
  public String toString() {
    return "Project{id=" + id + ", name='" + name + "'}";
  }


  @ManyToOne
  @JoinColumn(name = "team_id",nullable = true)
  @ToString.Exclude  // Prevent circular references
  @EqualsAndHashCode.Exclude
  @JsonIgnore
  private Team team;

  @ManyToOne
  @JoinColumn(name = "creator_id", nullable = false)
  @ToString.Exclude  // Prevent circular references
  @EqualsAndHashCode.Exclude
  private User creator;

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude  // Prevent circular references
  private Set<Task> tasks;

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude  // Prevent circular references
  private Set<UserProject> userProjects;


}

