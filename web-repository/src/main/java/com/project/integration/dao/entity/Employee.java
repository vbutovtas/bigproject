package com.project.integration.dao.entity;

import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Entity(name = "employees")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  User user;

  @Column(name = "date_of_birth")
  @NonNull
  LocalDate birthDate;

  @NonNull String position;
  @NonNull String technologies;

  @NonNull
  @Column(name = "work_experience")
  Float experience;

  Blob photo;

  @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL)
  Set<Ticket> assigneeTickets = new HashSet<>();

  @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL)
  Set<Ticket> reporterTickets = new HashSet<>();

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  Set<Comment> comments = new HashSet<>();
}
