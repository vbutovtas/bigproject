package com.project.integration.serv.dto;

import com.project.integration.dao.entity.Role;
import com.project.integration.serv.enums.UserRoles;
import com.project.integration.serv.enums.UserStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
  Integer id;
  UserRoles role;
  String login;
  String password;
  String name;
  String surname;
  String email;
  String phone;
  UserStatus status;

  public UserDto(Integer id, Role role, String login, String name, String surname, String email, String phone) {
    this.id = id;
    this.role = UserRoles.getEnumByValue(role.getId());
    this.login = login;
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.phone = phone;
  }
  public UserDto(String name, String surname, String email, String phone) {
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.phone = phone;
  }
}
