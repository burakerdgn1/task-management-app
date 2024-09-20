package com.server.taskmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "teams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "creator_id", nullable = false)
  @ToString.Exclude  // Prevent circular references
  @EqualsAndHashCode.Exclude
  private User creator;

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
  @ToString.Exclude  // Prevent circular references
  @EqualsAndHashCode.Exclude
  private List<UserTeam> userTeams;

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
  @ToString.Exclude  // Prevent circular references
  @EqualsAndHashCode.Exclude
  private Set<Task> tasks;

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
  @ToString.Exclude  // Prevent circular references
  @EqualsAndHashCode.Exclude
  @JsonIgnore
  private Set<Project> projects;

  // Add project to the set
  public void addProject(Project project) {
    this.projects.add(project);
    project.setTeam(this);
  }

  // Remove project from the set
  public void removeProject(Project project) {
    this.projects.remove(project);
    project.setTeam(null);
  }

  @Override
  public String toString() {
    return "Team{id=" + id + ", name='" + name + "', creator=" + creator.getUsername() + "}";
  }

}
