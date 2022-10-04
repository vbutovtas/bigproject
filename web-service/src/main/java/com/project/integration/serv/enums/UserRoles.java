package com.project.integration.serv.enums;

import java.util.stream.Stream;

public enum UserRoles {
  ADMIN(1),
  MANAGER(2),
  EMPLOYEE(3),
  CUSTOMER(4);

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
