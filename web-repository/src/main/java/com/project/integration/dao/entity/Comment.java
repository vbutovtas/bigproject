package com.project.integration.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Entity(name = "comments")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  @NonNull String comment;

  @ManyToOne
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  Employee employee;

  @ManyToOne
  @JoinColumn(name = "id", insertable = false, updatable = false)
  Ticket ticket;
}
