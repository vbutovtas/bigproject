package com.project.integration.dao.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  @ManyToOne
  @JoinColumn(name = "role_id")
  Role role;

  String login;
  String password;
  @NonNull String name;
  @NonNull String surname;
  String email;
  String phone;
  String status;

  @OneToOne(mappedBy = "user")
  Employee employee;

  @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
  Set<Order> orders = new HashSet<>();

  public User(
      String login, String password, String name, String surname, String email, String phone) {
    this.login = login;
    this.password = password;
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.phone = phone;
  }

  public User(String login, String name, String surname, String email, String phone) {
    this.login = login;
    this.password = password;
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.phone = phone;
  }

  public User(
      Role role,
      String login,
      String password,
      String name,
      String surname,
      String email,
      String phone,
      String status) {
    this.role = role;
    this.login = login;
    this.password = password;
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.phone = phone;
    this.status = status;
  }
}
