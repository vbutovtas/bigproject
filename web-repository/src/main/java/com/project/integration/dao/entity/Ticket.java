package com.project.integration.dao.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ticket {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  @ManyToOne
  @JoinColumn(name = "assignee_id")
  Employee assignee;

  @ManyToOne
  @JoinColumn(name = "reporter_id")
  Employee reporter;

  @ManyToOne
  @JoinColumn(name = "ticket_id")
  Ticket ticket;

  String name;

  String description;

  @Column(name = "create_date")
  LocalDateTime createDate;

  @Column(name = "due_date")
  LocalDate dueDate;

  @Column(name = "estimated_time")
  Float estimatedTime;

  @Column(name = "logged_time")
  Float loggedTime;

  String status;

  String type;

  Integer severity;

  @Column(name = "git")
  String gitRef;

  @Column(name = "order_number")
  Integer order;

  @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
  Set<Comment> comments = new HashSet<>();

  @ManyToMany(mappedBy = "projects")
  Set<Employee> employees = new HashSet<>();
}
