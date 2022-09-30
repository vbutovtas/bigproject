package com.project.integration.serv.enums;

import java.util.stream.Stream;

public enum TicketStatus {
  ACTIVE("ACTIVE"); //TODO ticket status

  private final String value;

  TicketStatus(String value) {
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
