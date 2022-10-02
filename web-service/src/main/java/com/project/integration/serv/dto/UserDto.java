package com.project.integration.serv.dto;

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
  EmployeeDto employee;
}
