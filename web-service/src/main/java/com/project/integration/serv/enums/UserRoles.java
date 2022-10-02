package com.project.integration.serv.enums;

import java.util.stream.Stream;

public enum UserRoles {
  ADMIN("ADMIN"),
  MANAGER("MANAGER"),
  EMPLOYEE("EMPLOYEE"),
  CUSTOMER("CUSTOMER");

  private final String value;

  UserRoles(String value) {
    this.value = value;
  }

  public static UserStatus getEnumByValue(String value) {
    return Stream.of(UserStatus.values())
        .filter(v -> v.getValue().equals(value))
        .findFirst()
        .orElse(null);
  }

  public String getValue() {
    return value;
  }
}
