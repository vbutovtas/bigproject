package com.project.integration.dao.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Entity(name = "roles")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  @NonNull String name;

  @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
  Set<User> users = new HashSet<>();
}