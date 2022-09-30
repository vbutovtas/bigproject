package com.project.integration.serv.dto;

import com.project.integration.serv.enums.UserStatus;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
  Integer id;
  RoleDto role;
  String login;
  String password;
  String name;
  String surname;
  String email;
  String phone;
  UserStatus status;
  EmployeeDto employee;
  Set<OrderDto> orders = new HashSet<>();
}
