package com.project.integration.dao.entity;

import com.project.integration.dao.enums.UserStatus;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Entity(name = "users")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  @ManyToOne
  @JoinColumn(name = "role_id")
  Role role;

  @NonNull String login;
  @NonNull String password;
  @NonNull String name;
  @NonNull String surname;
  @NonNull String email;
  @NonNull String phone;
  @NonNull UserStatus status;
}
