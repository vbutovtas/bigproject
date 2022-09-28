package com.project.integration.dao.entity;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Entity(name = "tickets")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ticket {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  @ManyToOne
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  Employee assignee;

  @ManyToOne
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  Employee reporter;

  @ManyToOne
  @JoinColumn(name = "ticket_id")
  Ticket ticket;

  @NonNull String name;

  @NonNull String description;

  @Column(name = "due_date")
  LocalDate dueDate;

  @Column(name = "estimated_time")
  int estimatedTime;

  @NonNull String status;

  @NonNull String type;

  @NonNull String gitRef;

  @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
  Set<Comment> comments = new HashSet<>();
}
