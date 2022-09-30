package com.project.integration.serv.enums;

import java.util.stream.Stream;

public enum TicketType {
  ACTIVE("ACTIVE"); //TODO ticket types

  private final String value;

  TicketType(String value) {
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
