package com.project.integration.serv.enums;

import java.util.stream.Stream;

public enum UserRoles {
  ROLE_ADMIN(1),
  ROLE_MANAGER(2),
  ROLE_EMPLOYEE(3),
  ROLE_CUSTOMER(4);

  private final int value;

  UserRoles(int value) {
    this.value = value;
  }

  public static UserRoles getEnumByValue(int value) {
    return Stream.of(UserRoles.values())
        .filter(v -> v.getValue() == value)
        .findFirst()
        .orElse(null);
  }

  public int getValue() {
    return value;
  }
}
